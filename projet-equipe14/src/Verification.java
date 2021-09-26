import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Verification {
    private FormationContinue formationAVerifier;
    private JSONObject fichierErreur;

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée", "présentat ion", "groupe de discussion", "projet de recherche", "rédaction professionnelle"};

    public Verification(FormationContinue formation){
        this.formationAVerifier = formation;

        this.fichierErreur = new JSONObject();
        JSONArray listeErreurs = new JSONArray();

        fichierErreur.put("complet", false);
        fichierErreur.put("erreurs", listeErreurs);
    }

    public JSONObject resultat(){
        return fichierErreur;
    }

    public void validationCategories(){
        JSONArray activities = formationAVerifier.getActivities();
        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if (!Arrays.asList(CATEGORIE).contains(activity.get("categorie"))){
                String nom = (String) activity.get("description");
                JSONArray erreurs = (JSONArray) fichierErreur.get("erreurs");
                erreurs.add("La catégorie " + nom + " n'existe pas dans la banque de catégories");

            }
        }
    }

    public boolean validationDates() throws ParseException {
        boolean [] result = new boolean [1];
        result [0] = true;
            JSONArray activities = formationAVerifier.getActivities();
            for (Object o : activities) {
                JSONObject activity = (JSONObject) o;
                String date = (String) activity.get("date");
                    if (!(date.matches("[0-9]{4}[-]{1}[0-9]{2}[-]{1}[0-9]{2}")) | !validationDatesPeriode(date)) {
                            result [0] = false;
                    }
            }
        return result [0];
    }

    public boolean validationDatesPeriode(String date) throws ParseException {
        boolean resultat = true;
        SimpleDateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat("yyyy-M-dd");
        Date dateEntree = sdf.parse(date);
        Date dateMin = sdf.parse("2020-04-01");
        Date dateMax = sdf.parse("2022-04-01");
            if (!(dateEntree.after(dateMin)) || !(dateEntree.before(dateMax))) {
            JSONArray erreurs = (JSONArray) fichierErreur.get("erreurs");
            erreurs.add("Not a validate date");
            resultat = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultat;
    }

    public boolean validationHeuresTransferees(){
        boolean resultat = true;
        long heuresMax = 7;
        long heuresMin = 0;
        long heures = formationAVerifier.getHeuresTransferees();
        long heuresFixe = heures;
            if (heures < heuresMin) {
                formationAVerifier.setHeuresTransferees(heuresMin);
            }
            if (heures > heuresMax){
                formationAVerifier.setHeuresTransferees(7);
                JSONArray erreurs = (JSONArray) fichierErreur.get("erreurs");
                erreurs.add("Le nombre d'heures transferes ("+heuresFixe+") depasse la limite permise, seulement sept heures seront transferees");
                resultat = false;
            }
            return resultat;
    }

    public boolean validationHeures(){
        boolean resultat = true;
        long heuresTotal = 0;
        long heuresMin = 40;
        JSONObject activity;
        JSONArray activities = formationAVerifier.getActivities();
        for (Object o : activities) {
            activity = (JSONObject) o;
            heuresTotal += (long) activity.get("heures");
        }
            if ((heuresTotal + formationAVerifier.getHeuresTransferees()) < heuresMin) {
                JSONArray erreurs = (JSONArray) fichierErreur.get("erreurs");
                erreurs.add("\nL'etudiant a complete seulement " + (heuresTotal + formationAVerifier.getHeuresTransferees()) + " de 40");
                resultat = false;
            }
        //System.out.println(resultat());
        return resultat;
    }

    public boolean validationNbHeuresActivite(int pHeuresRequises, int pHeuresCompletes){
        if(pHeuresRequises <= pHeuresCompletes)
            return true;

        return false;
    }

    public boolean validationHeuresCatégorieMultiple(){
        String[] categoriesRequise = {"cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée"};
        JSONArray activities = formationAVerifier.getActivities();
        int heures = 0;

        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;

            if(Arrays.asList(categoriesRequise).contains(activity.get("categorie")))
                heures += Integer.parseInt(activity.get("heures").toString());
        }

        return validationNbHeuresActivite(17, heures);
    }

    public int calculHeuresMaxCategories(String categorie, int heureMax){
        JSONArray activities = formationAVerifier.getActivities();
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
}
