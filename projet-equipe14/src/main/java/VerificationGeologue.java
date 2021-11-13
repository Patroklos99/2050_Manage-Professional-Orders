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

    protected static final String[] CATEGORIETOTAL = {"cours",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    public VerificationGeologue(FormationContinue formation,
                                String fichierSortie) throws Exception {
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
                    ajoutCategorieListe(categorie);
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
    public void validDateCycleListe(String date, JSONArray bonneActivites,
                                    JSONObject activite){
        if (formationAVerifier.getCycle().equals("2018-2021")) {
            if (validDatePeriode(date))
                bonneActivites.add(activite);
        }
    }

    @Override
    public boolean validDatePeriode(String date) {
        boolean bonneDate = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date entree = sdf.parse(date);
            Date min = sdf.parse("2018-06-01");
            Date max = sdf.parse("2021-06-01");
            bonneDate = conditValidDate(entree,min, max);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bonneDate;
    }

    @Override
    public void validationHeures1(int pHeureMin, JSONArray pActiviteValide){
        JSONObject activite;
        creationListeCategorieHeure();
        for (Object o : pActiviteValide) {
            activite = (JSONObject) o;
            ajoutHeureListe(activite);
        }
        calculHeureTotal(pHeureMin);
    }

    @Override
    public void calculHeureTotal(int heureReq){
        int heureTotal = 0;
        for(int i = 0; i < heureCategorie.size(); i++){
            heureTotal = heureTotal + heureCategorie.get(i).getHeure();
        }
        if(heureTotal < heureReq)
            ajoutMsgErreur("L'etudiant a complete seulement " +
                    heureTotal + " de " + heureReq + "h");
    }

    @Override
    public void verifierChampHeuresTransf() throws Exception {
        if(!formationAVerifier.isHeuresTransfereesNull())
            causerErreurVerif("Il ne devrait pas avoir des heures " +
                    "transférées");
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

    public void validationHeureMinimum(String categorie, int heureRequise){
        CalculHeureCategorie calculHeure;
        int heure = 0;
        for (int i = 0; i < heureCategorie.size(); i++){
            calculHeure = heureCategorie.get(i);
            if(calculHeure.getCategorie().equals(categorie))
                heure = calculHeure.getHeure();
        }
        if(heureRequise > heure)
            ajoutMsgErreur("La catégorie " + categorie +
                    " doit avoir au minimum " + heureRequise + "h");
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

    public void validationToutes(JSONArray activiteValide) throws Exception {
        validationDates();
        validationCategories();
        validationHeures1(55, activiteValide);
        validationHeureMinimum("cours", 22);
        validationHeureMinimum("projet de recherche", 3);
        validationHeureMinimum("groupe de discussion", 1);
    }
}
