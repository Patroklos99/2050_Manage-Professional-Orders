import net.sf.json.JSONObject;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class MockVerification extends Verification{
    public MockVerification(FormationContinue formation, String fichierSortie, Statistiques stats)
            throws Exception {
        super(formation, fichierSortie, stats);
    }

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    private String erreur = "";
/*
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
    */
/*
    public void validationHeuresTransferees(int pHeureMax, int pHeureMin){
        long heures = formationAVerifier.getHeuresTransferees();
        long heuresFixe = heures;
        if (heures < pHeureMin)
            formationAVerifier.setHeuresTransferees(pHeureMin);
        if (heures > pHeureMax){
            formationAVerifier.setHeuresTransferees(7);
            erreur = "Le nombre d'heures transferees ("+ heuresFixe
                    +") depasse la limite permise, seulement" +
                    " 7h seront transferees";
        }
    }
*/
    public String getErreur(){
        return erreur;
    }

}
