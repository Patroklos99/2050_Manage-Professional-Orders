import net.sf.json.JSONObject;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class MockVerification extends Verification{
    public MockVerification(FormationContinue formation, String fichierSortie)
            throws Exception {
        super(formation, fichierSortie);
    }

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    private String erreur = "";

    public void validationCategories(){
        for (Object o : formationAVerifier.getActivites()) {
            JSONObject activite = (JSONObject) o;
            if (!Arrays.asList(CATEGORIE).contains(activite.get("categorie"))){
                String nom = activite.get("categorie").toString();
                erreur = "La catégorie " + nom
                        + " n'existe pas dans la banque de catégories";
            }
        }
    }

    public String getErreur(){
        return erreur;
    }

}
