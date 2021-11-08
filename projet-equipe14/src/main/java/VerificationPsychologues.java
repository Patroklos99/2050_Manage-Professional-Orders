import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class VerificationPsychologues extends Verification {

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};


    public VerificationPsychologues(FormationContinue formation, String fichierSortie) throws Exception {
<<<<<<< HEAD
            super(formation, fichierSortie);

=======
        super(formation, fichierSortie);
    }

    @Override
    public boolean validationCycle() {
        boolean bonCycle = true;
        String cycle = formationAVerifier.getCycle();
        if(!cycle.equals("2018-2023")) {
            ajoutMsgErreur("Le cycle de la formation n'est pas valide");
            bonCycle = false;
        }
        return bonCycle;
>>>>>>> origin/dev
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
            Date min = sdf.parse("2018-01-01");
            Date max = sdf.parse("2023-01-01");
            bonneDate = conditionValidDatePeriode(entree, min, max, categorie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bonneDate;
    }

    @Override
    public void validationHeuresCategorieMultiple(JSONArray activites){
        int heures = 0;
        for (Object o : activites) {
            JSONObject activite = (JSONObject) o;
            if(activite.get("categorie").equals(
                    "cours"))
                heures += Integer.parseInt(activite.get("heures").toString());
        }
        if(!validationNbHeuresActivite(25, heures))
            ajoutMsgErreur("Les heures totales de la categorie cours " +
                    "n'est pas pas superieur a 25h");
    }

    public boolean validationNbHeuresActivite(int pHeuresRequises,
                                              int pHeuresCompletes){
        if(pHeuresRequises <= pHeuresCompletes)
            return true;
        return false;
    }

    public void validationHeureFormat(){
        for (Object o : formationAVerifier.getActivites()) {
            JSONObject activity = (JSONObject) o;
            if (!(activity.get("heures").toString()).matches("^[0-9]+$") ||
                    Double.parseDouble((activity.get("heures")).toString()) < 1)
                ajoutMsgErreur("L'activité " + activity.get("description")
                        + " n'a pas un nombre valide d'heures");
        }
    }

    @Override
    public void validationHeures(int pHeureMin, JSONArray pActiviteValide){
        int heuresTotal = 0;
        JSONObject activite;
        for (Object o : pActiviteValide) {
            activite = (JSONObject) o;
            heuresTotal = ecrireHeuresTotal(heuresTotal, activite,
                    pActiviteValide);
        }

        for (int i = 0; i < categorieTotale.size(); i++)
            heuresTotal += regarderCategorie(categorieTotale.get(i), pActiviteValide);

        ecrireMsgErrHeureTotal(heuresTotal, pHeureMin );
    }

    @Override
    public int regarderCategorie(String pCategorie, JSONArray pActiviteValide){
        int heure = 0;

        if(pCategorie.equals("conférence"))
        heure = calculHeuresMaxCategories(pCategorie, 15, pActiviteValide);

        return heure;
    }

    @Override
    public void verifierChampHeuresTransf() throws Exception {
        if(!formationAVerifier.isHeuresTransfereesNull())
            causerErreurVerif("Il ne devrait pas avoir des heures transférées");
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
            validationHeures(90, activiteValide);
            validationHeuresCategorieMultiple(activiteValide);
        }
        imprimer(fichierSortie);
    }


}
