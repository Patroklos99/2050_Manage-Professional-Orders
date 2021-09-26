import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class Verification {
    private FormationContinue formationAVerifier;
    private JSONObject fichierErreur;

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée", "présentation", "groupe de discussion", "projet de recherche", "rédaction professionnelle"};

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
}

