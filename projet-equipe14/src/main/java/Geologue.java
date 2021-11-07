import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Geologue extends Verification{
    public Geologue(FormationContinue formation, String fichierSortie) throws Exception {
        super(formation, fichierSortie);
    }

    public boolean checkHeuresTransferees(FormationContinue formation){
        return formation.isHeuresTransfereesNull;
    }

    @Override
    public boolean validationCycle() {
        return super.validationCycle();
    }

    @Override
    public int regarderCategorie(String pCategorie, JSONArray pActiviteValide) {
        int heure = 0;

        if(pCategorie.equals("cours"))
            heure = calculHeuresMaxCategories(pCategorie, 22, pActiviteValide);

        if(pCategorie.equals("projet de recherche"));
        heure = calculHeuresMaxCategories(pCategorie, 3, pActiviteValide);

        if(pCategorie.equals("groupe de discussion"))
            heure = calculHeuresMaxCategories(pCategorie, 1, pActiviteValide);

        return heure;
    }

    @Override
    public void validationFinal(String fichierSortie) throws Exception {
        JSONArray activiteValide = creationListeBonnesActivites();
        super.ajouterCategorieTotale();
        if(validationCycle()) {
            super.validationHeureFormat();
            super.validationDates();
            super.validationCategories(activiteValide);
            this.checkHeuresTransferees(super.formationAVerifier);
            super.validationHeures(55, activiteValide);
            super.validationHeuresCategorieMultiple(activiteValide);
        }
        imprimer(fichierSortie);
    }
}
