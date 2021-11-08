import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VerificationGeologue extends Verification{
    public VerificationGeologue(FormationContinue formation, String fichierSortie) throws Exception {
        super(formation, fichierSortie);
    }

    @Override
    public void verifierChampHeuresTransf() throws Exception {
        if(!formationAVerifier.isHeuresTransfereesNull())
            causerErreurVerif("Il ne devrait pas avoir des heures transférées");
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
        validationGenerale();
        JSONArray activiteValide = creationListeBonnesActivites();
        super.ajouterCategorieTotale();
        if(validationCycle()) {
            super.validationHeureFormat();
            super.validationDates();
            super.validationCategories(activiteValide);
            super.validationHeures(55, activiteValide);
            super.validationHeuresCategorieMultiple(activiteValide);
        }
        imprimer(fichierSortie);
    }
}
