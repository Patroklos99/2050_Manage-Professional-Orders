import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.text.*;
import java.time.LocalDate;
import java.time.format.*;
import java.util.*;

public class Verification {
    protected FormationContinue formationAVerifier;
    private JSONObject fichierErreur;
    private String fichierSortie;
    protected ArrayList<String> categorieValide = new ArrayList<>();
    protected ArrayList<CalculHeureCategorie> heureCategorie =
            new ArrayList<>();

    private static final String[] CATEGORIE = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    private String[] categoriesRequise = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée"};

    public Verification(FormationContinue formation, String fichierSortie)
            throws Exception {
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

    public void validationCategories(){
        for (Object o : formationAVerifier.getActivites()) {
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

    public void validationDateParCycle(String date, String categorie)
            throws ParseException {
        if (formationAVerifier.getCycle().equals("2020-2022")) {
            if(validationDatesPeriode(date,categorie,"2020-04-01","2022-04-01"))
                ajoutCategorieListe(categorie);
        } else if (formationAVerifier.getCycle().equals("2018-2020")) {
            if(validationDatesPeriode(date,categorie,"2018-04-01","2020-04-01"))
                ajoutCategorieListe(categorie);
        } else {
            if(validationDatesPeriode(date,categorie,"2016-04-01","2018-07-01"))
                ajoutCategorieListe(categorie);
        }
    }

    public void ajoutCategorieListe(String categorie){
        if(!categorieValide.contains(categorie)){
            categorieValide.add(categorie);
        }
    }

    public boolean conditionValidDatePeriode(Date dateEntree,Date dateMin,
                                             Date dateMax,
                                             String categorie){
        boolean bonneDate = true;
        if (dateEntree.before(dateMin) || dateEntree.after(dateMax)) {
            ajoutMsgErreur("La date de la catégorie ("+ categorie
                    + ") n'est pas valide.");
            bonneDate = false;
        }
        return bonneDate;
    }

    public boolean validationDatesPeriode(String date, String categorie,
                                          String dateMin, String dateMax)
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

    public void validationHeuresParCycle(JSONArray pActiviteValide){
        if(formationAVerifier.getCycle().equals("2020-2022")){
            validationHeures1(40,pActiviteValide);
        }
        else{
            validationHeures1(42,pActiviteValide);
        }
    }

    public void validationHeuresCategorieMultiple(){
        int heures = 0;
        for (CalculHeureCategorie o : heureCategorie) {
            if(Arrays.asList(categoriesRequise).contains(o.getCategorie()))
                heures += o.getHeure();
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

    public int calculHeuresMaxCategories(int heureCat, int heureMax){
        int heures = heureCat;
        if(heures > heureMax)
            heures = heureMax;
        return heures;
    }

    public int regarderCategorie(String pCategorie, int heureCat){
        int heure = heureCat;

        if(pCategorie.equals("présentation")
                || pCategorie.equals("projet de recherche"))
            heure = calculHeuresMaxCategories(heureCat,23);

        if(pCategorie.equals("groupe de discussion")
                || pCategorie.equals("rédaction professionnelle"))
            heure = calculHeuresMaxCategories(heureCat,17);

        return heure;
    }

    public void validationHeures1(int pHeureMin, JSONArray pActiviteValide){
        JSONObject activite;
        creationListeCategorieHeure();
        for (Object o : pActiviteValide) {
            activite = (JSONObject) o;
            ajoutHeureListe(activite);
        }
        ajustementMaxHeures();
        calculHeureTotal(pHeureMin);
    }

    public void creationListeCategorieHeure(){
        CalculHeureCategorie calculHeure;
        for(int i = 0; i< categorieValide.size(); i++){
            calculHeure = new CalculHeureCategorie(categorieValide.get(i),0 );
            heureCategorie.add(i,calculHeure);
        }
    }

    public void ajoutHeureListe(JSONObject activite){
        String categorieAct = activite.get("categorie").toString();
        int heureAct = calculHeure10Max(Integer.parseInt(activite.get("heures")
                .toString()),categorieAct);
        CalculHeureCategorie calculHeure;
        for(int i = 0; i < heureCategorie.size(); i++){
            calculHeure = heureCategorie.get(i);
            if(calculHeure.getCategorie().equals(categorieAct)) {
                ajoutHeureParCategorie(calculHeure, heureAct);
            }
        }
    }

    public void ajoutHeureParCategorie(CalculHeureCategorie calculHeure,
                                       int heure){
        int heureAjoute = calculHeure.getHeure() + heure;
        calculHeure.setHeure(heureAjoute);
    }

    public int calculHeure10Max(int heure,String categorie){
        int bonneHeure;
        if(heure > 10){
            bonneHeure = 10;
            ajoutMsgErreur("Le nombre d'heures d'une activité de catégorie "
                    + categorie + " dépasse 10 heures.");
        }else{
            bonneHeure = heure;
        }
        return bonneHeure;
    }

    public void ajustementMaxHeures(){
        for(int i = 0; i < heureCategorie.size(); i++){
            String pCategorie = heureCategorie.get(i).getCategorie();
            int heureCat = heureCategorie.get(i).getHeure();
            CalculHeureCategorie calculHeureCat = heureCategorie.get(i);
            heureCat = regarderCategorie(pCategorie,heureCat);
            calculHeureCat.setHeure(heureCat);
        }
    }

    public void calculHeureTotal(int heureReq){
        int heureTotal = formationAVerifier.getHeuresTransferees();
        for(int i = 0; i < heureCategorie.size(); i++){
            heureTotal = heureTotal + heureCategorie.get(i).getHeure();
        }
        if(heureTotal < heureReq)
            ajoutMsgErreur("L'etudiant a complete seulement " + heureTotal +
                    " de " + heureReq + "h");
    }

    public boolean validationCycle(){
        boolean bonCycle = true;
        String cycle = formationAVerifier.getCycle();
        if(!cycle.equals("2020-2022") && !cycle.equals("2018-2020") &&
                !cycle.equals("2016-2018")) {
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
                causerErreurVerif("L'activité "
                        + activity.get("description")
                        + " n'a pas un nombre valide d'heures");
        }
    }

    public JSONArray creationListeBonnesActivites(){
        JSONArray bonneActivites = new JSONArray();
        JSONArray activities = validationFormatDate1();
        for (Object o : activities){
            JSONObject activite = (JSONObject) o;
            if(Arrays.asList(CATEGORIE).contains(activite.get("categorie"))) {
                String date = (String) activite.get("date");
                validDateCycleListe(date,bonneActivites,activite);
            }
        }
        return bonneActivites;
    }

    public void validDateCycleListe(String date, JSONArray bonneActivites,
                                    JSONObject activite){
        if (formationAVerifier.getCycle().equals("2020-2022")) {
            if (validDatePeriode(date,"2020-04-01","2022-04-01"))
                bonneActivites.add(activite);
        } else if (formationAVerifier.getCycle().equals("2018-2020")) {
            if (validDatePeriode(date,"2018-04-01","2020-04-01"))
                bonneActivites.add(activite);
        } else {
            if (validDatePeriode(date,"2016-04-01","2018-07-01"))
                bonneActivites.add(activite);
        }
    }

    public boolean validDatePeriode(String date,String dateMin,String dateMax){
        boolean bonneDate = true;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date entree = sdf.parse(date);
            Date min = sdf.parse(dateMin);
            Date max = sdf.parse(dateMax);
            bonneDate = conditValidDate(entree,min, max);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bonneDate;
    }

    public boolean conditValidDate(Date dateEntree, Date dateMin, Date dateMax){
        boolean bonneDate = true;
        if (dateEntree.before(dateMin) || dateEntree.after(dateMax)) {
            bonneDate = false;
        }
        return bonneDate;
    }

    public JSONArray validationFormatDate(){
        JSONArray activites = formationAVerifier.getActivites();
        JSONArray dateValide = new JSONArray();
        for (Object o : activites) {
            JSONObject activite = (JSONObject) o;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);
            validationBonneDate(dateValide,activite,dateTimeFormatter);
        }
        return dateValide;
    }

    public void validationBonneDate(JSONArray dateValide, JSONObject activite,
                                    DateTimeFormatter dateTimeFormatter){
        try {
            LocalDate date = LocalDate.parse(activite.get("date").toString(),
                    dateTimeFormatter);
            dateValide.add(activite);
        } catch (DateTimeParseException e){
            afficherErrFormatDate(activite);
        }
    }

    public JSONArray validationFormatDate1(){
        JSONArray activites = formationAVerifier.getActivites();
        JSONArray dateValide = new JSONArray();
        for (Object o : activites) {
            JSONObject activite = (JSONObject) o;
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);
            validationBonneDate1(dateValide,activite,dateTimeFormatter);
        }
        return dateValide;
    }

    public void validationBonneDate1(JSONArray dateValide, JSONObject activite,
                                     DateTimeFormatter dateTimeFormatter){
        try {
            LocalDate date = LocalDate.parse(activite.get("date").toString(),
                    dateTimeFormatter);
            dateValide.add(activite);
        } catch (DateTimeParseException e){
            e.getClass();
        }
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


    public void validationNumeroPermis() throws Exception {
        String numeroPermis = formationAVerifier.getNumeroPermis();
        if(!numeroPermis.matches("^[AT]{1}[0-9]{4}$"))
            causerErreurVerif("Le numero de permis du architecte " +
                    "n'est pas du bon " +
                    "format (A ou T suivit de 4 chiffres).");
    }

    public void validationDescription() throws Exception {
        for (Object o : formationAVerifier.getActivites()) {
            JSONObject activity = (JSONObject) o;
            if (!(activity.get("description").toString()).
                    matches( "^.{21,}$"))
                causerErreurVerif("La description de l'activité " +
                        activity.get("description")
                        + " ne contient pas plus de 20 caractères.");
        }
    }
    public void validationSexe(){
        int sexe = formationAVerifier.getSexe();
        if(sexe < 0 || sexe > 2){
            ajoutMsgErreur("Le sexe n'a pas une valeur acceptée (0, 1 ou 2)");
        }
    }

    public void verifierChampHeuresTransf() throws Exception {
        if(formationAVerifier.isHeuresTransfereesNull())
            causerErreurVerif("Les heures transférées doivent être " +
                    "un nombre");
    }


    public void causerErreurVerif(String pMessage) throws Exception {
        System.err.println(pMessage);
        ajoutMsgErreur("Le fichier d'entrée est invalide et le cycle est " +
                "incomplet.");
        imprimer(getFichierSortie());
        System.exit( -1 );
    }

    public void validationFinal(String fichierSortie) throws Exception {
        validationGenerale();
        if(validationCycle()) {
            validationDates();
            JSONArray activiteValide = creationListeBonnesActivites();
            validationCategories();
            validationHeuresTransferees(7, 0);
            validationHeuresParCycle(activiteValide);
            validationHeuresCategorieMultiple();
        }
        imprimer(fichierSortie);
    }

    public void validationGenerale() throws Exception {
        verifierChampHeuresTransf();
        validationNumeroPermis();
        validationSexe();
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