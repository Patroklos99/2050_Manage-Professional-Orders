import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class VerificationPsychologues extends Verification {
    private JSONObject fichierErreur;
    private ArrayList<String> categorieValide = new ArrayList<String>();
    private ArrayList<String> categorieTotale = new ArrayList<String>();

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    private String[] categoriesRequise = {"cours"};


    public VerificationPsychologues(FormationContinue formation, String fichierSortie) throws Exception {
        super(formation, "test");
        this.fichierErreur = new JSONObject();
        JSONArray listeErreurs = new JSONArray();
        fichierErreur.put("Complet", true);
        fichierErreur.put("Erreurs", listeErreurs);
        validationFinal(fichierSortie);
    }

    public void ajouterDateValideTab() throws ParseException {
        JSONArray activities = validationFormatDate();
        for (Object o : activities) {
            JSONObject activite = (JSONObject) o;
            if (Arrays.asList(CATEGORIE).contains(activite.get("categorie"))) {
                String date = (String) activite.get("date");
                String categorie = (String) activite.get("categorie");
                if (validerDatePeriode(date, categorie))
                    categorieValide.add(categorie);
            }
        }
    }

    public boolean validerDatePeriode(String date, String categorie)
            throws ParseException {
        boolean bonneDate = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
            Date entree = sdf.parse(date);
            Date min = sdf.parse("2018-01-01");
            Date max = sdf.parse("2023-01-01");
            bonneDate = validerDateBornes(entree, min, max, categorie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bonneDate;
    }

    public boolean validerDateBornes(Date dateEntree, Date dateMin,
                                     Date dateMax,
                                     String categorie) {
        boolean bonneDate = true;
        if (!(dateEntree.after(dateMin)) || !(dateEntree.before(dateMax))) {
            ajoutMsgErreur("La date de la catégorie (" + categorie
                    + ") n'est pas valide.");
            bonneDate = false;
        }
        return bonneDate;
    }

    public void validerHeuresCategorieCours(JSONArray activites){
        int heures = 0;
        for (Object o : activites) {
            JSONObject activite = (JSONObject) o;
            if(Arrays.asList(categoriesRequise).contains(
                    activite.get("categorie")))
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

    public void validerHeures(int pHeureMin, JSONArray pActiviteValide){
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

    public void ecrireMsgErrHeureTotal (int heuresTotal, int pHeureMin){
        if (heuresTotal < pHeureMin) {
            ajoutMsgErreur("L'étudiant a complete seulement "
                    + (heuresTotal) + " de 90h");
        }
    }

    public int regarderCategorie(String pCategorie, JSONArray pActiviteValide){
        int heure = 0;

        if(pCategorie.equals("conférence"))
        heure = calculHeuresMaxCategories(pCategorie, 15, pActiviteValide);

        return heure;
    }



    public void validationFinal(String fichierSortie) throws Exception {
        JSONArray activiteValide = creationListeBonnesActivites();
        ajouterCategorieTotale();
        if(validationCycle()) {
            validationHeureFormat();
            ajouterDateValideTab();
            validationCategories(activiteValide);
            validerHeures(90, activiteValide);
            validerHeuresCategorieCours(activiteValide);
        }

        imprimer(fichierSortie);
    }


}
