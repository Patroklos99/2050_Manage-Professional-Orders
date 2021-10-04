import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.*;

public class Verification {
    private FormationContinue formationAVerifier;
    private JSONObject fichierErreur;
    private ArrayList<String> categorieValide = new ArrayList<String>();

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
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
                String nom = (String) activity.get("categorie");
                ajoutMsgErreur("La catégorie " + nom
                        + " n'existe pas dans la banque de catégories");
            }
        }
    }

    public void validationDates() throws ParseException {
            JSONArray activities = validationFormatDate();
            for (Object o : activities) {
                JSONObject activity = (JSONObject) o;
                if(Arrays.asList(CATEGORIE).contains(activity.get("categorie")))
                {
                    String date = (String) activity.get("date");
                    String categorie = (String) activity.get("categorie");
                    if (validationDatesPeriode(date, categorie)) {
                        categorieValide.add(categorie);
                    }
                }
            }
    }

    public boolean conditionValidDatePeriode(Date dateEntree,Date dateMin,
                                                  Date dateMax,
                                                  String categorie){
        boolean bonneDate = true;
        if (!(dateEntree.after(dateMin)) || !(dateEntree.before(dateMax))) {
            ajoutMsgErreur("La date de la categorie ("+ categorie
                    + ") n'est pas valide.");
            bonneDate = false;
        }
        return bonneDate;
    }

    public boolean validationDatesPeriode(String date, String categorie)
            throws ParseException {
        boolean bonneDate = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
            Date entree = sdf.parse(date);
            Date min = sdf.parse("2020-04-01");
            Date max = sdf.parse("2022-04-01");
            bonneDate = conditionValidDatePeriode(entree,min, max,categorie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bonneDate;
    }

    public void validationHeuresTransferees(int pHeureMax, int pHeureMin){
        long heures = formationAVerifier.getHeuresTransferees();
        long heuresFixe = heures;
            if (heures < pHeureMin)
                formationAVerifier.setHeuresTransferees(pHeureMin);
            if (heures > pHeureMax){
                formationAVerifier.setHeuresTransferees(7);
                ajoutMsgErreur("Le nombre d'heures transferees ("+ heuresFixe
                        +") depasse la limite permise, seulement" +
                        " 7h seront transferees");
            }
    }

    public void validationHeures(int pHeureMin, JSONArray pActiviteValide){       //trop long
        int heuresTotal = 0;
        JSONObject activity;
        for (Object o : pActiviteValide) {
            activity = (JSONObject) o;
            if(categorieValide.contains(activity.get("categorie"))) {
                heuresTotal += regarderCategorie(
                        activity.get("categorie").toString(), pActiviteValide,
                        Integer.parseInt(activity.get("heures").toString()));
            }
        }
        heuresTotal += formationAVerifier.getHeuresTransferees();
            if (heuresTotal < pHeureMin) {
                ajoutMsgErreur("L'etudiant a complete seulement "
                        + (heuresTotal) + " de 40h");
            }
        System.out.println(heuresTotal);
    }

    public void validationHeuresCatégorieMultiple(JSONArray activities){         //trop long
        String[] categoriesRequise = {"cours", "atelier", "séminaire",
                "colloque", "conférence", "lecture dirigée"};
        int heures = 0;
        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if(Arrays.asList(categoriesRequise).contains(
                    activity.get("categorie")))
                heures += Integer.parseInt(activity.get("heures").toString());
        }
        if(!validationNbHeuresActivite(17, heures))
            ajoutMsgErreur("Les heures totales de l'ensemble des categories " +
                    "n'est pas pas superieur a 17h");
    }

    public boolean validationNbHeuresActivite(int pHeuresRequises,
                                              int pHeuresCompletes){
        if(pHeuresRequises <= pHeuresCompletes)
            return true;
        return false;
    }

    public int calculHeuresMaxCategories(String categorie, int heureMax,
                                         JSONArray activities){
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

    public void validationHeureFormat(){
        for (Object o : formationAVerifier.getActivities()) {
            JSONObject activity = (JSONObject) o;
            if (!(activity.get("heures").toString()).matches("^[0-9]+$") ||
                    Double.parseDouble((activity.get("heures")).toString()) < 1)
            {
                ajoutMsgErreur("L'activité " + activity.get("description")
                        + " n'a pas un nombre valide d'heures");
            }
        }
    }

    public JSONArray creationListeBonnesActivites(){
        JSONArray bonneActivites = new JSONArray();
        for (Object o : formationAVerifier.getActivities()){
            JSONObject activity = (JSONObject) o;
            if((activity.get("heures").toString()).matches("^[0-9]+$") &&
                    Double.parseDouble((activity.get("heures")).toString())
                            >= 1) {
                bonneActivites.add(activity);
            }
        }
        return bonneActivites;
    }

    //Methode impossible dappliquer, linstantion de lobjet formation type formationcontinue arrive avant.
    public long validationFormatHeuresTransferees(){
        long heuresTrans = 0;
        if ((!(String.valueOf(formationAVerifier.getHeuresTransferees())).matches("[0-9]+")) ||
                Double.parseDouble(String.valueOf(formationAVerifier.getHeuresTransferees())) < 1
                        || (!String.valueOf(formationAVerifier.getHeuresTransferees()).contains(".")))
        {
        formationAVerifier.setHeuresTransferees(0);
        } else {
            heuresTrans = formationAVerifier.getHeuresTransferees();
        }
        return heuresTrans;
    }

    public JSONArray validationFormatDate(){
        JSONArray activities = formationAVerifier.getActivities();
        JSONArray dateValide = new JSONArray();
        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if (((activity.get("date").toString()).matches(
                    "[0-9]{4}[-]{1}[0-9]{2}[-]{1}[0-9]{2}"))) {
                dateValide.add(activity);
            }
        }
        return dateValide;
    }

    public void ajoutMsgErreur(String msg){
        JSONArray erreur = (JSONArray)fichierErreur.get("Erreurs");
        erreur.add(msg);
        fichierErreur.put("Erreurs", erreur);
        fichierErreur.put("Complet", false);
    }

    public int regarderCategorie(String pCategorie, JSONArray pActiviteValide,
                                 int pHeure){                                       //trop long
        if(pCategorie == "presentation")
            pHeure = calculHeuresMaxCategories("presentation", 23,
                    pActiviteValide);
        if(pCategorie == "groupe de discussion")
            pHeure = calculHeuresMaxCategories("groupe de discussion", 17,
                    pActiviteValide);
        if(pCategorie == "projet de recherche")
            pHeure = calculHeuresMaxCategories("projet de recherche", 23,
                    pActiviteValide);
        if(pCategorie == "redaction professionnelle")
            pHeure = calculHeuresMaxCategories("redaction professionnelle",
                    17, pActiviteValide);
        return pHeure;
    }

    public void validationFinal() throws ParseException {
        JSONArray activiteValide = creationListeBonnesActivites();

        validationHeureFormat();
        validationCycle();
        validationDates();
        validationCategories(activiteValide);
        validationHeuresTransferees(7, 0);
        validationHeures(40, activiteValide);
        validationHeuresCatégorieMultiple(activiteValide);
        System.out.println(resultat());
    }
}