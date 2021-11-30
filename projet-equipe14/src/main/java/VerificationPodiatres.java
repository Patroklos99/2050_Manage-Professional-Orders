import net.sf.json.JSONArray;

public class VerificationPodiatres extends VerificationGeologue{

    public VerificationPodiatres(FormationContinue formation, String fichierSortie) throws Exception {
        super(formation, fichierSortie);
    }

    @Override
    public void validationNumeroPermis() throws Exception {
        String numeroPermis = formationAVerifier.getNumeroPermis();
        if(!numeroPermis.matches("^[0-9]{5}$"))
            causerErreurVerif("Le numero de permis du podiatre n'est pas du bon " +
                    "format (5 Chiffres uniquement).");
    }

    @Override
    public void validationFinal(String fichierSortie) throws Exception {
        validationGenerale();
        if(validationCycle()) {
            JSONArray activiteValide = creationListeBonnesActivites();
            validationToutes(activiteValide);
        }
        imprimer(fichierSortie);
    }

    @Override
    public void validationToutes(JSONArray activiteValide) throws Exception {
        validationDates();
        validationCategories();
        validationHeures1(60, activiteValide);
        validationHeureMinimum("cours", 22);
        validationHeureMinimum("projet de recherche", 3);
        validationHeureMinimum("groupe de discussion", 1);
    }

}
