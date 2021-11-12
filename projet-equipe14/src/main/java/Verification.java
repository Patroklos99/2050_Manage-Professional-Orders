import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.*;

public class Verification {
    protected FormationContinue formationAVerifier;
    private JSONObject fichierErreur;
    private String fichierSortie;
    protected ArrayList<String> categorieValide = new ArrayList<>();

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    private String[] categoriesRequise = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée"};

    public Verification(FormationContinue formation, String fichierSortie) throws Exception {
        this.formationAVerifier = formation;
        this.fichierSortie = fichierSortie;
        this.fichierErreur = new JSONObject();
        JSONArray listeErreurs = new JSONArray();
        fichierErreur.put("Complet", true);
        fichierErreur.put("Erreurs", listeErreurs);


        validationFinal(fichierSortie);
    }

    public String getFichierSortie(){
        return fichierSortie;
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
                validationDateParCycle(date,categorie);
            }
        }
    }

    public void validationDateParCycle(String date, String categorie) throws ParseException {
        if (formationAVerifier.getCycle().equals("2020-2022")) {
            if (validationDatesPeriode(date, categorie, "2020-04-01", "2022-04-01"))
                categorieValide.add(categorie);
        } else if (formationAVerifier.getCycle().equals("2018-2020")) {
            if (validationDatesPeriode(date, categorie, "2018-04-01", "2020-04-01"))
                categorieValide.add(categorie);
        } else if (validationDatesPeriode(date, categorie, "2016-04-01", "2018-07-01")){
            categorieValide.add(categorie);
        }
    }

    public boolean conditionValidDatePeriode(Date dateEntree,Date dateMin,
                                             Date dateMax,
                                             String categorie){
        boolean bonneDate = true;
        if (!(dateEntree.after(dateMin)) || !(dateEntree.before(dateMax))) {
            ajoutMsgErreur("La date de la catégorie ("+ categorie
                    + ") n'est pas valide.");
            bonneDate = false;
        }
        return bonneDate;
    }

    public boolean validationDatesPeriode(String date, String categorie, String dateMin, String dateMax)
            throws ParseException {
        boolean bonneDate = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date entree = sdf.parse(date);
            Date min = sdf.parse(dateMin);
            Date max = sdf.parse(dateMax);
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
        heuresTotal = ajusterHeureTotal(heuresTotal, pActiviteValide);
        ecrireMsgErrHeureTotal(heuresTotal, pHeureMin);
    }

    public int ajusterHeureTotal(int pHeureTotal, JSONArray pActiviteValide){
        //Check max heures des catégories multiples sélectionné
        ArrayList<String> tabCat = creeTableauCategorie(pActiviteValide);
        pHeureTotal = enleverHeureTotal(pHeureTotal, tabCat, pActiviteValide);

        //Check 10h par jour par catégorie
        ArrayList<ArrayList<String>> tabCatDate
                = creeTableauCategorieMultiple(pActiviteValide);
        pHeureTotal = checkSameDate(pHeureTotal, tabCatDate, pActiviteValide);

        //Ajout des heures transférées
        pHeureTotal += formationAVerifier.getHeuresTransferees();

        return pHeureTotal;
    }

    public int enleverHeureTotal(int pHTotal, ArrayList<String> pTabCat,
                                 JSONArray pActiviteValide){
        for (int i = 0; i < pTabCat.size(); i++){
            pHTotal = checkHeureDiff(pTabCat.get(i), pActiviteValide, pHTotal);
        }
        return pHTotal;
    }

    public int checkHeureDiff(String pCategorie, JSONArray pActiviteValide,
                              int pHeureTotal){
        int heureDiff = regarderCategorie(pCategorie, pActiviteValide);

        if(heureDiff >= 0)
            return pHeureTotal - heureDiff;

        return pHeureTotal;
    }

    public int checkSameDate(int pHTotal, ArrayList<ArrayList<String>> pTabCat,
                             JSONArray pActiviteValide){
        for (int i = 0; i < pTabCat.size(); i++){
            int hCat = 0;
            for (Object o : pActiviteValide) {
                JSONObject act = (JSONObject) o;
                if(pTabCat.get(i).get(0).equals(act.get("categorie"))
                   && pTabCat.get(i).get(1).equals(act.get("date")))
                    hCat += Integer.parseInt(act.get("heures").toString());
            }
            pHTotal = pHTotal - calculDébordement10H(hCat, 10);
        }
        return pHTotal;
    }

    public int calculDébordement10H(int pHeureCategorie, int pHeuremax){
        int heure;

        if(pHeureCategorie > pHeuremax)
            heure = pHeureCategorie - pHeuremax;
        else
            heure = 0;

        return heure;
    }

    public void validationHeuresParCycle(JSONArray pActiviteValide){
        if(formationAVerifier.getCycle().equals("2020-2022")){
            validationHeures(40,pActiviteValide);
        }
        else{
            validationHeures(42,pActiviteValide);
        }
    }

    public int ecrireHeuresTotal (int heuresTotal, JSONObject activite,
                                  JSONArray pActiviteValide){
        String categorie = activite.get("categorie").toString();
        if(categorieValide.contains(categorie)) {
            heuresTotal += Integer.parseInt(activite.get("heures").toString());
        }
        return heuresTotal;
    }

    public void ecrireMsgErrHeureTotal (int heuresTotal, int pHeureMin){
        if (heuresTotal < pHeureMin) {
            ajoutMsgErreur("L'etudiant a complete seulement "
                    + (heuresTotal) + " de " + pHeureMin + "h");
        }
    }

    public void validationHeuresCategorieMultiple(JSONArray activites){
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
            if(activity.get("categorie").toString().equals(categorie))
                heures += Integer.parseInt(activity.get("heures").toString());
        }
        if(heures > heureMax)
            heures = heureMax;
        return heures;
    }

    public boolean validationCycle(){
        boolean bonCycle = true;
        String cycle = formationAVerifier.getCycle();
        if(!cycle.equals("2020-2022") && !cycle.equals("2018-2020") && !cycle.equals("2016-2018")) {
            ajoutMsgErreur("Le cycle de la formation n'est pas valide");
            bonCycle = false;
        }
        return bonCycle;
    }

    public void validationHeureFormat() throws Exception {
        for (Object o : formationAVerifier.getActivites()) {
            JSONObject activity = (JSONObject) o;
            if (!(activity.get("heures").toString()).matches("^[0-9]+$") ||
                    Double.parseDouble((activity.get("heures")).toString()) < 1)
                causerErreurVerif("L'activité " + activity.get("description")
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
                    "[0-9]{4}[-]{1}[0-1]{1}[0-9]{1}[-]{1}[0-3]{1}[0-9]{1}"))) {
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

    public int regarderCategorie(String pCategorie, JSONArray pActiviteValide){
        int heure = 0;

        if(pCategorie.equals("présentation")
                || pCategorie.equals("projet de recherche"))
            heure = calculHeuresMaxCategories(pCategorie, 23, pActiviteValide);

        if(pCategorie.equals("groupe de discussion")
                || pCategorie.equals("redaction professionnelle"))
            heure = calculHeuresMaxCategories(pCategorie, 17, pActiviteValide);

        return heure;
    }

    public void validationNumeroPermis() throws Exception {
        String numeroPermis = formationAVerifier.getNumeroPermis();
        if(!numeroPermis.matches("^[ARSZ]{1}[0-9]{4}$"))
            causerErreurVerif("Le numero de permis n'est pas du bon format (A, R, S ou Z suivit de " +
                    "4 chiffres).");
    }

    public void validationDescription() throws Exception {
        for (Object o : formationAVerifier.getActivites()) {
            JSONObject activity = (JSONObject) o;
            if (!(activity.get("description").toString()).matches("^.{21,}$"))
                causerErreurVerif("La description de l'activité " + activity.get("description")
                        + " ne contient pas plus de 20 caractères.");
        }
    }

    public void verifierChampHeuresTransf() throws Exception {
        if(formationAVerifier.isHeuresTransfereesNull())
            causerErreurVerif("Les heures transférées doivent être un nombre");
    }

    public ArrayList<String> creeTableauCategorie(JSONArray tabActiviteValide){
        ArrayList<String> tabCategorie = new ArrayList<>();
        for (Object o : tabActiviteValide) {
            JSONObject activite = (JSONObject) o;
            if(!tabCategorie.contains(activite.get("categorie").toString()))
                tabCategorie.add(activite.get("categorie").toString());
        }
        return tabCategorie;
    }

    public ArrayList<ArrayList<String>> creeTableauCategorieMultiple(JSONArray tabActiviteValide){
        ArrayList<ArrayList<String>> listCategorie = new ArrayList<>();
        ArrayList<String> listActivite = new ArrayList<>();
        for (Object o : tabActiviteValide) {
            JSONObject act = (JSONObject) o;
                for (Object oo : tabActiviteValide) {
                    JSONObject act2 = (JSONObject) oo;
                    if(isDateAndCategorieSame(act, act2) && !isItInList(listCategorie, act)){
                        listActivite.add(act.get("categorie").toString());
                        listActivite.add(act.get("date").toString());
                        listCategorie.add(listActivite);
                    }
                }
        }
        return listCategorie;
    }

    public boolean isItInList(ArrayList<ArrayList<String>> pListCategorie, JSONObject pAct){
        boolean inList = false;

        for (int i = 0; i < pListCategorie.size(); i++){
            if(pListCategorie.get(i).get(0).equals(pAct.get("categorie"))
                    && pListCategorie.get(i).get(1).equals(pAct.get("date")))
                inList = true;
        }

        return inList;
    }

    public boolean isDateAndCategorieSame(JSONObject pAct, JSONObject pAct2){
        if(pAct.get("categorie").toString().equals(
                pAct2.get("categorie").toString())
                && pAct.get("date").toString().equals(
                pAct2.get("date").toString()))
            return true;

        return false;
    }

    public void causerErreurVerif(String pMessage) throws Exception {
        System.err.println(pMessage);
        ajoutMsgErreur("Le fichier d'entrée est invalide et le cycle est incomplet.");
        imprimer(getFichierSortie());
        System.exit( -1 );
    }

    public void validationFinal(String fichierSortie) throws Exception {
        validationGenerale();
        JSONArray activiteValide = creationListeBonnesActivites();
        if(validationCycle()) {
            validationDates();
            validationCategories(activiteValide);
            validationHeuresTransferees(7, 0);
            validationHeuresParCycle(activiteValide);
            validationHeuresCategorieMultiple(activiteValide);
        }
        imprimer(fichierSortie);
    }

    public void validationGenerale() throws Exception {
        verifierChampHeuresTransf();
        validationNumeroPermis();
        validationDescription();
        validationHeureFormat();
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