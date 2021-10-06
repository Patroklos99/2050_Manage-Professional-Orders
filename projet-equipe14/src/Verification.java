import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.*;

public class Verification {
    private FormationContinue formationAVerifier;
    private JSONObject fichierErreur;
    private ArrayList<String> categorieValide = new ArrayList<String>();

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    private String[] categoriesRequise = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée"};

    public Verification(FormationContinue formation, String fichierSortie) throws Exception {
        this.formationAVerifier = formation;
        this.fichierErreur = new JSONObject();
        JSONArray listeErreurs = new JSONArray();
        fichierErreur.put("Complet", true);
        fichierErreur.put("Erreurs", listeErreurs);
        validationFinal(fichierSortie);
    }

    public JSONObject resultat(){
        return fichierErreur;
    }

    public void validationCategories(JSONArray activites){
        for (Object o : activites) {
            JSONObject activite = (JSONObject) o;
            if (!Arrays.asList(CATEGORIE).contains(activite.get("categorie"))){
                String nom = activite.get("categorie").toString();
                ajoutMsgErreur("La catégorie " + nom
                        + " n'existe pas dans la banque de catégories");
            }
        }
    }

    public void validationDates() throws ParseException {
        JSONArray activities = validationFormatDate();
        for (Object o : activities) {
            JSONObject activite = (JSONObject) o;
            if(Arrays.asList(CATEGORIE).contains(activite.get("categorie"))) {
                String date = (String) activite.get("date");
                String categorie = (String) activite.get("categorie");
                if (validationDatesPeriode(date, categorie))
                    categorieValide.add(categorie);
            }
        }
    }

    public boolean conditionValidDatePeriode(Date dateEntree,Date dateMin,
                                                  Date dateMax,
                                                  String categorie){
        boolean bonneDate = true;
        if (!(dateEntree.after(dateMin)) || !(dateEntree.before(dateMax))) {
            ajoutMsgErreur("La date de la categorie ("+ categorie
                    + ") n'est pas valide.");
            bonneDate = false;
        }
        return bonneDate;
    }

    public boolean validationDatesPeriode(String date, String categorie)
            throws ParseException {
        boolean bonneDate = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
            Date entree = sdf.parse(date);
            Date min = sdf.parse("2020-04-01");
            Date max = sdf.parse("2022-04-01");
            bonneDate = conditionValidDatePeriode(entree,min, max,categorie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bonneDate;
    }

    public void validationHeuresTransferees(int pHeureMax, int pHeureMin){
        long heures = formationAVerifier.getHeuresTransferees();
        long heuresFixe = heures;
            if (heures < pHeureMin)
                formationAVerifier.setHeuresTransferees(pHeureMin);
            if (heures > pHeureMax){
                formationAVerifier.setHeuresTransferees(7);
                ajoutMsgErreur("Le nombre d'heures transferees ("+ heuresFixe
                        +") depasse la limite permise, seulement" +
                        " 7h seront transferees");
            }
    }

    public void validationHeures(int pHeureMin, JSONArray pActiviteValide){
        int heuresTotal = 0;
        JSONObject activite;
        for (Object o : pActiviteValide) {
            activite = (JSONObject) o;
            heuresTotal = ecrireHeuresTotal(heuresTotal, activite,
                    pActiviteValide);
        }
        heuresTotal += formationAVerifier.getHeuresTransferees();
        ecrireMsgErrHeureTotal(heuresTotal, pHeureMin );
    }

    public int ecrireHeuresTotal (int heuresTotal, JSONObject activite,
                                  JSONArray pActiviteValide){
        if(categorieValide.contains(activite.get("categorie"))) {
            heuresTotal += regarderCategorie(
                    activite.get("categorie").toString(), pActiviteValide,
                    Integer.parseInt(activite.get("heures").toString()));
        }
        return heuresTotal;
    }

    public void ecrireMsgErrHeureTotal (int heuresTotal, int pHeureMin){
        if (heuresTotal < pHeureMin) {
            ajoutMsgErreur("L'etudiant a complete seulement "
                    + (heuresTotal) + " de 40h");
        }
    }

    public void validationHeuresCatégorieMultiple(JSONArray activites){
        int heures = 0;
        for (Object o : activites) {
            JSONObject activite = (JSONObject) o;
            if(Arrays.asList(categoriesRequise).contains(
                    activite.get("categorie")))
                heures += Integer.parseInt(activite.get("heures").toString());
        }
        if(!validationNbHeuresActivite(17, heures))
            ajoutMsgErreur("Les heures totales de l'ensemble des categories " +
                    "n'est pas pas superieur a 17h");
    }

    public boolean validationNbHeuresActivite(int pHeuresRequises,
                                              int pHeuresCompletes){
        if(pHeuresRequises <= pHeuresCompletes)
            return true;
        return false;
    }

    public int calculHeuresMaxCategories(String categorie, int heureMax,
                                         JSONArray activities){
        int heures = 0;
        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if(activity.get("categorie").toString().contentEquals(categorie))
                heures += Integer.parseInt(activity.get("heures").toString());
        }
        if(validationNbHeuresActivite(heureMax, heures))
            return heureMax;
        return heures;
    }

    public boolean validationCycle(){
        boolean bonCycle = true;
        String cycle = formationAVerifier.getCycle();
        if(!cycle.equals("2020-2022")) {
            ajoutMsgErreur("Le cycle de la formation n'est pas valide");
            bonCycle = false;
        }
        return bonCycle;
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

    public JSONArray creationListeBonnesActivites(){
        JSONArray bonneActivites = new JSONArray();
        for (Object o : formationAVerifier.getActivites()){
            JSONObject activite = (JSONObject) o;
            if((activite.get("heures").toString()).matches("^[0-9]+$") &&
                    Double.parseDouble((activite.get("heures")).toString())
                            >= 1) {
                bonneActivites.add(activite);
            }
        }
        return bonneActivites;
    }

    public JSONArray validationFormatDate(){
        JSONArray activites = formationAVerifier.getActivites();
        JSONArray dateValide = new JSONArray();
        for (Object o : activites) {
            JSONObject activite = (JSONObject) o;
            if (((activite.get("date").toString()).matches(
                    "[0-9]{4}[-]{1}[0-1]{1}[0-2]{1}[-]{1}[0-3]{1}[0-9]{1}"))) {
                dateValide.add(activite);
            }else {
                afficherErrFormatDate(activite);
            }
        }
        return dateValide;
    }

    public void afficherErrFormatDate(JSONObject activite){
        String categorie = (String) activite.get("categorie");
        ajoutMsgErreur("La date de la categorie ("+ categorie
                + ") n'est pas valide (pas bon format).");

    }

    public void ajoutMsgErreur(String msg){
        JSONArray erreur = (JSONArray)fichierErreur.get("Erreurs");
        erreur.add(msg);
        fichierErreur.put("Erreurs", erreur);
        fichierErreur.put("Complet", false);
    }

    public int regarderCategorie(String pCategorie, JSONArray pActiviteValide,
                                 int pHeure){
        if(pCategorie.equals("présentation"))
            pHeure = calculHeuresMaxCategories("présentation", 23,
                    pActiviteValide);
        if(pCategorie.equals("groupe de discussion"))
            pHeure = calculHeuresMaxCategories("groupe de discussion", 17,
                    pActiviteValide);
        if(pCategorie.equals("projet de recherche"))
            pHeure = calculHeuresMaxCategories("projet de recherche", 23,
                    pActiviteValide);
        if(pCategorie.equals("redaction professionnelle"))
            pHeure = calculHeuresMaxCategories("redaction professionnelle",
                    17, pActiviteValide);

        return pHeure;
    }

    public void validationFinal(String fichierSortie) throws Exception {
        JSONArray activiteValide = creationListeBonnesActivites();
        if(validationCycle()) {
            validationHeureFormat();
            validationDates();
            validationCategories(activiteValide);
            validationHeuresTransferees(7, 0);
            validationHeures(40, activiteValide);
            validationHeuresCatégorieMultiple(activiteValide);
        }
        imprimer(fichierSortie);
    }

    /**
     * Code inspire de la methode save() du projet json-lib-ex ecrit par
     * Dogny, Gnagnely Serge
     */
    public void imprimer(String fichierSortie) throws Exception {
        try (FileWriter f = new FileWriter(fichierSortie)) {
            f.write(fichierErreur.toString(3));
            f.flush();
        }catch(IOException e){
            throw new Exception(e.toString());
        }
    }
}