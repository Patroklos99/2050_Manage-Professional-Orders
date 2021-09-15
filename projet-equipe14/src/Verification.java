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


}
