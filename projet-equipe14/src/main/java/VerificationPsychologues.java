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
        super(formation, fichierSortie);
    }

    @Override
    public boolean validationCycle() {
        boolean bonCycle = true;
        String cycle = formationAVerifier.getCycle();
        if(!cycle.equals("2018-2023")) {
            ajoutMsgErreur("Le cycle de la formation pour les psychologues " +
                    "n'est pas valide");
            bonCycle = false;
        }
        return bonCycle;
    }

    @Override
    public void validationDates() throws ParseException {
        JSONArray activities = validationFormatDate();
        for (Object o : activities) {
            JSONObject activite = (JSONObject) o;
            if (Arrays.asList(CATEGORIE).contains(activite.get("categorie"))) {
                String date = (String) activite.get("date");
                String categorie = (String) activite.get("categorie");
                if (validationDatesPeriode(date, categorie, "2020-04-01", "2022-04-01"))
                    categorieValide.add(categorie);
            }
        }
    }

    @Override
    public boolean validationDatesPeriode(String date, String categorie, String dateMin, String dateMax)
            throws ParseException {
        boolean bonneDate = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date entree = sdf.parse(date);
            Date min = sdf.parse(dateMin);
            Date max = sdf.parse(dateMax);
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
            if(activite.get("categorie").equals("cours"))
                heures += Integer.parseInt(activite.get("heures").toString());
        }
        if(!validationNbHeuresActivite(25, heures))
            ajoutMsgErreur("Les heures totales de la catégorie -cours- " +
                    "n'est pas pas superieur a 25h");
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
        heuresTotal += regarderCategorie("conférence", pActiviteValide);
        ecrireMsgErrHeureTotal(heuresTotal, pHeureMin );
    }

    public void validationHeureMinimum(String categorie, int heureRequise, JSONArray activite){
        int heure = 0;
        for (Object o : activite) {
            JSONObject activity = (JSONObject) o;
            if(activity.get("categorie").toString().contentEquals(categorie))
                heure += Integer.parseInt(activity.get("heures").toString());
        }
        if(heure < heureRequise)
            ajoutMsgErreur("La catégorie " + categorie +
                    " doit avoir au minimum " + heureRequise + "h");
    }

    @Override
    public int ecrireHeuresTotal (int heuresTotal, JSONObject activite,
                                  JSONArray pActiviteValide){
        String categorie = activite.get("categorie").toString();
        if(categorieValide.contains(categorie)&&!categorie.equals("conférence"))
            heuresTotal += dixHeuresMax(activite);
        return heuresTotal;
    }

    public int dixHeuresMax(JSONObject activite) {
        int heures = Integer.parseInt(activite.get("heures").toString());
        if(Integer.parseInt(activite.get("heures").toString()) > 10){
            heures = 10;
            ajoutMsgErreur("Le nombre d'heures de la categorie (" +
                    activite.get("categorie") + ") dépasse la limite permise." +
                    " Seulement 10h seront considérées dans les calculs.");
        }
        return heures;
    }

    @Override
    public int calculHeuresMaxCategories(String categorie, int heureMax,
                                         JSONArray activities){
        int heures = 0;
        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if(activity.get("categorie").toString().equals(categorie))
                heures += dixHeuresMax(activity);
        }
        if(heures > heureMax)
            heures = heureMax;
        return heures;
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
        validationGenerale(); //Heritage
        validationGenerale(); //Heritage
        JSONArray activiteValide = creationListeBonnesActivites();
        if(validationCycle()) { //Heritage
            validationDates(); //Class
            validationCategories(activiteValide); //Heritage
            validationHeures(90, activiteValide); //Class
            //validationHeuresCategorieMultiple(activiteValide); //Class
            validationHeureMinimum("cours", 25, activiteValide);
        }
        imprimer(fichierSortie);
    }


}
