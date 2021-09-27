import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Verification {
    private FormationContinue formationAVerifier;
    private JSONObject fichierErreur;

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire", "colloque", "conférence",
            "lecture dirigée", "présentation", "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    public Verification(FormationContinue formation) throws ParseException {
        this.formationAVerifier = formation;

        this.fichierErreur = new JSONObject();
        JSONArray listeErreurs = new JSONArray();

        fichierErreur.put("Complet", true);
        fichierErreur.put("Erreurs", listeErreurs);

        validationFinal();
    }

    public JSONObject resultat(){
        return fichierErreur;
    }

    public void validationCategories(JSONArray activities){
        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if (!Arrays.asList(CATEGORIE).contains(activity.get("categorie"))){
                String nom = (String) activity.get("description");
                ajoutMsgErreur("La catégorie " + nom + " n'existe pas dans la banque de catégories");
            }
        }
    }

    public void validationDates() throws ParseException {
            JSONArray activities = formationAVerifier.getActivities();
            for (Object o : activities) {
                JSONObject activity = (JSONObject) o;
                String date = (String) activity.get("date");
                    if ((date.matches("[0-9]{4}[-]{1}[0-9]{2}[-]{1}[0-9]{2}"))) {
                            validationDatesPeriode(date);
                    }
            }
    }

    public void validationDatesPeriode(String date) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
        Date dateEntree = sdf.parse(date);
        Date dateMin = sdf.parse("2020-04-01");
        Date dateMax = sdf.parse("2022-04-01");
            if (!(dateEntree.after(dateMin)) || !(dateEntree.before(dateMax))) {
                ajoutMsgErreur("Date non valide");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validationHeuresTransferees(int pHeureMax, int pHeureMin){
        long heures = formationAVerifier.getHeuresTransferees();
        long heuresFixe = heures;
            if (heures < pHeureMin)
                formationAVerifier.setHeuresTransferees(pHeureMin);
            if (heures > pHeureMax){
                formationAVerifier.setHeuresTransferees(7);
                ajoutMsgErreur("Le nombre d'heures transferes ("+heuresFixe+") depasse la limite permise, seulement" +
                        " sept heures seront transferees");
            }
    }

    public void validationHeures(int pHeureMax, int pHeureMin, JSONArray pActiviteValide){
        int heuresTotal = 0;
        JSONObject activity;
        for (Object o : pActiviteValide) {
            activity = (JSONObject) o;
            heuresTotal += regarderCategorie(activity.get("categorie").toString(), pActiviteValide,
                            Integer.parseInt(activity.get("heures").toString()));
        }
            if ((heuresTotal + formationAVerifier.getHeuresTransferees()) < pHeureMin) {
                ajoutMsgErreur("L'etudiant a complete seulement " + (heuresTotal +
                        (formationAVerifier.getHeuresTransferees()) + " de 40"));
            }
    }

    public boolean validationNbHeuresActivite(int pHeuresRequises, int pHeuresCompletes){
        if(pHeuresRequises <= pHeuresCompletes)
            return true;

        return false;
    }

    public void validationHeuresCatégorieMultiple(JSONArray activities){
        String[] categoriesRequise = {"cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée"};
        int heures = 0;

        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if(Arrays.asList(categoriesRequise).contains(activity.get("categorie")))
                heures += Integer.parseInt(activity.get("heures").toString());
        }

        if(!validationNbHeuresActivite(17, heures))
            ajoutMsgErreur("abc");
    }

    public int calculHeuresMaxCategories(String categorie, int heureMax, JSONArray activities){
        int heures = 0;

        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if(activity.get("categorie").toString().contentEquals(categorie))
                heures += Integer.parseInt(activity.get("heures").toString());
        }

        if(validationNbHeuresActivite(heureMax, heures))
            return heureMax;

        return heures;
    }

    public void validationCycle(){
        String cycle = formationAVerifier.getCycle();

        if(!cycle.equals("2020-2022")){
            ajoutMsgErreur("Le cycle de la formation n'est pas valide");
        }
    }

    public JSONArray validationHeureFormat(){
        JSONArray activities = formationAVerifier.getActivities();
        JSONArray bonneActivites = new JSONArray();
        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if (Double.parseDouble((activity.get("heures")).toString()) < 1 ||
                    (activity.get("heures")).toString().contains(".")){
                ajoutMsgErreur("L'activité " + activity.get("description") + " n'a pas un nombre valide d'heures");
            }else{
                bonneActivites.add(activity);
            }
        }
        return bonneActivites;
    }

    public void ajoutMsgErreur(String msg){
        //Boolean complet = (Boolean) fichierErreur.remove("complet");
        JSONArray erreur = (JSONArray)fichierErreur.get("Erreurs");

        erreur.add(msg);

        fichierErreur.put("Erreurs", erreur);
        fichierErreur.put("Complet", false);
    }

    public int regarderCategorie(String pCategorie, JSONArray pActiviteValide, int pHeure){
        if(pCategorie == "presentation")
            pHeure = calculHeuresMaxCategories("presentation", 23, pActiviteValide);

        if(pCategorie == "groupe de discussion")
            pHeure = calculHeuresMaxCategories("groupe de discussion", 17, pActiviteValide);

        if(pCategorie == "projet de recherche")
            pHeure = calculHeuresMaxCategories("projet de recherche", 23, pActiviteValide);

        if(pCategorie == "redaction professionnelle")
            pHeure = calculHeuresMaxCategories("redaction professionnelle", 17, pActiviteValide);

        return pHeure;
    }

    public void validationFinal() throws ParseException {
        JSONArray activiteValide = validationHeureFormat();

        validationCycle();
        validationDates();
        validationCategories(activiteValide);
        validationHeuresTransferees(7, 0);
        validationHeures(40, 0, activiteValide);
        validationHeuresCatégorieMultiple(activiteValide);

        System.out.println(resultat());
    }
}