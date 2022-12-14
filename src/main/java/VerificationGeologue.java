import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                                String fichierSortie, Statistiques stats) throws Exception {
        super(formation, fichierSortie, stats);
    }

    @Override
    public void validationDates() throws ParseException {
        JSONArray activities = validationFormatDate();
        for (Object o : activities) {
            JSONObject activite = (JSONObject) o;
            if (Arrays.asList(CATEGORIE).contains(activite.get("categorie"))) {
                String date = (String) activite.get("date");
                String categorie = (String) activite.get("categorie");
                if (validationDatesPeriode(date, categorie,"2018-06-01",
                        "2021-06-01"))
                    ajoutCategorieListe(categorie);
            }
        }
    }


    @Override
    public void validDateCycleListe(String date, JSONArray bonneActivites,
                                    JSONObject activite){
        if (formationAVerifier.getCycle().equals("2018-2021")) {
            if (validDatePeriode(date,"2018-06-01","2021-06-01"))
                bonneActivites.add(activite);
        }
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

    @Override
    public void validationHeures2(int pHeureMin, JSONArray pActiviteValide,
                                  ArrayList<String> listeDate){
        creationListeCategorieHeure();
        for(String date : listeDate) {
            int heureDate = 0;
            verifierActivitePourHeure(pActiviteValide,date,heureDate);
        }
        calculHeureTotal(pHeureMin);
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
    public void validationNumeroPermis() throws Exception {
        String numeroPermis = formationAVerifier.getNumeroPermis();
        String initialesPermis = numeroPermis.substring(0,2);
        String initialesReels = formationAVerifier.getNom().charAt(0) + "" + formationAVerifier.getPrenom().charAt(0);
        if(!numeroPermis.matches("^[a-zA-Z]{2}[0-9]{4}$") || !initialesPermis.equals(initialesReels))
            causerErreurVerif("Le numero de permis du geologue n'est pas du bon " +
                    "format (2 lettres, égales aux initiales du nom et prenom, suivis de 4 chiffres).");
    }

    @Override
    public void validationFinal(String fichierSortie) throws Exception {
        validationGenerale();
        if(validationCycle()) {
            JSONArray activiteValide = creationListeBonnesActivites();
            validationToutes(activiteValide);
        }
    }

    public void validationToutes(JSONArray activiteValide) throws Exception {
        validationDates();
        ArrayList<String> listeDate = creationListeDates(activiteValide);
        validationCategories();
        validationHeures2(55, activiteValide,listeDate);
        validationHeureMinimum("cours", 22);
        validationHeureMinimum("projet de recherche", 3);
        validationHeureMinimum("groupe de discussion", 1);
    }
}
