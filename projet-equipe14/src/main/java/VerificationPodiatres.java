import net.sf.json.JSONArray;

import java.util.ArrayList;

public class VerificationPodiatres extends VerificationGeologue{

    public VerificationPodiatres(FormationContinue formation, String fichierSortie, Statistiques stats) throws Exception {
        super(formation, fichierSortie, stats);
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
        ArrayList<String> listeDate = creationListeDates(activiteValide);
        validationCategories();
        validationHeures2(60, activiteValide,listeDate);
        validationHeureMinimum("cours", 22);
        validationHeureMinimum("projet de recherche", 3);
        validationHeureMinimum("groupe de discussion", 1);
    }

}
