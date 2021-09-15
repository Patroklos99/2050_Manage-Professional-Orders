import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class Verification {
    private FormationContinue formationAVerifier;

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée", "présentation", "groupe de discussion", "projet de recherche", "rédaction professionnelle"};

    public Verification(FormationContinue formation){
        this.formationAVerifier = formation;
    }

    public boolean validationCategories(){
        boolean isOK = true;

        JSONArray activities = formationAVerifier.getActivities();

        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if (!Arrays.asList(CATEGORIE).contains(activity.get("categorie"))) {
                isOK = false;
            }
        }
        return isOK;
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

    public int calculHeuresPresentation(){
        JSONArray activities = formationAVerifier.getActivities();
        int heures = 0;

        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;

            if(activity.get("categorie").toString().contentEquals(CATEGORIE[6]))
                heures += Integer.parseInt(activity.get("heures").toString());
        }

        if(validationNbHeuresActivite(23, heures))
            return 23;

        return heures;
    }

    public int calculHeuresGroupeDeDiscussion(){
        JSONArray activities = formationAVerifier.getActivities();
        int heures = 0;

        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;

            if(activity.get("categorie").toString().contentEquals(CATEGORIE[7]))
                heures += Integer.parseInt(activity.get("heures").toString());
        }

        if(validationNbHeuresActivite(17, heures))
            return 17;

        return heures;
    }
}
