import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class VerificationGeologue extends Verification{

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    public VerificationGeologue(FormationContinue formation, String fichierSortie) throws Exception {
        super(formation, fichierSortie);
    }

    @Override
    public void validationDates() throws ParseException {
        JSONArray activities = validationFormatDate();
        for (Object o : activities) {
            JSONObject activite = (JSONObject) o;
            if (Arrays.asList(CATEGORIE).contains(activite.get("categorie"))) {
                String date = (String) activite.get("date");
                String categorie = (String) activite.get("categorie");
                if (validationDatesPeriode(date, categorie))
                    categorieValide.add(categorie);
            }
        }
    }

    @Override
    public boolean validationDatesPeriode(String date, String categorie)
            throws ParseException {
        boolean bonneDate = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date entree = sdf.parse(date);
            Date min = sdf.parse("2018-06-01");
            Date max = sdf.parse("2021-06-01");
            bonneDate = conditionValidDatePeriode(entree, min, max, categorie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bonneDate;
    }

    @Override
    public void verifierChampHeuresTransf() throws Exception {
        if(!formationAVerifier.isHeuresTransfereesNull())
            causerErreurVerif("Il ne devrait pas avoir des heures transférées");
    }

    @Override
    public boolean validationCycle() {
        boolean bonCycle = true;
        String cycle = formationAVerifier.getCycle();
        if(!cycle.equals("2018-2021")) {
            ajoutMsgErreur("Le cycle de la formation n'est pas valide");
            bonCycle = false;
        }
        return bonCycle;
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
        ajouterCategorieTotale();
        if(validationCycle()) {
            validationHeureFormat();
            validationDates();
            validationCategories(activiteValide);
            validationHeures(55, activiteValide);
            validationHeuresCategorieMultiple(activiteValide);
        }
        imprimer(fichierSortie);
    }
}
