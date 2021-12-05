import net.sf.json.*;
import java.io.FileWriter;

public class FormationContinue {

    private String ordre;
    private String nom;
    private String prenom;
    private int sexe;
    private String numeroPermis;
    private String cycle;
    private int heuresTransferees;
    protected boolean isHeuresTransfereesNull;
    private JSONArray activites;
    private JSONObject fichier;
    final private String msgErrAct = "Les activités doivent être stocké dans "+
            "un tableau";
    final private String msgErrActMiss = "Il manque un champs dans une activité";
    final private String msgErrNo = "Le numéros de permis doit être une " +
            "chaîne de caractères";
    final private String msgErrCyc = "Le cycle doit être une chaîne de " +
            "caractères";
    final private String msgErrOrd = "L'ordre doit être une chaîne de " +
            "caractères";
    final private String msgErrNom = "Le nom doit être une chaîne de caractères";
    final private String msgErrPre = "Le prénom doit être une chaîne de " +
            "caractères";
    final private String msgErrSexe = "Le sexe doit être un chiffre";

    public FormationContinue (JSONObject fichier, String fichierSortie)
            throws Exception {
        verifierType(fichier,fichierSortie);
        verifierType2(fichier,fichierSortie);
        assignerSeptChamps(fichier);
        assignerChampHeuresTranf(fichier);
        this.activites = (JSONArray) fichier.get("activites");
        verifierAct(fichier,fichierSortie);
    }

    public void assignerSeptChamps(JSONObject fichier){
        this.fichier = fichier;
        this.ordre = fichier.get("ordre").toString();
        this.nom = fichier.get("nom").toString();
        this.prenom = fichier.get("prenom").toString();
        this.sexe = Integer.parseInt(fichier.get("sexe").toString());
        this.numeroPermis = fichier.get("numero_de_permis").toString();
        this.cycle = fichier.get("cycle").toString();
    }

    public JSONObject getFichier(){
        return fichier;
    }

    public String getOrdre(){
        return ordre;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getSexe() {
        return sexe;
    }

    public String getNumeroPermis() {
        return numeroPermis;
    }

    public String getCycle() {
        return cycle;
    }

    public JSONArray getActivites() {
        return activites;
    }

    public int getHeuresTransferees() {
        return heuresTransferees;
    }

    public boolean isHeuresTransfereesNull() {
        return isHeuresTransfereesNull;
    }

    public void setHeuresTransferees(int heuresTransferees) {
        this.heuresTransferees = heuresTransferees;
    }

    public void verifierType(JSONObject f,String fichierSortie)
            throws Exception {
        if(!(f.get("activites") instanceof JSONArray))
            afficheErreur(msgErrAct, fichierSortie);
        if(!(f.get("numero_de_permis") instanceof String))
            afficheErreur(msgErrNo,fichierSortie);
        if(!(f.get("cycle") instanceof String))
            afficheErreur(msgErrCyc,fichierSortie);
        if(!(f.get("ordre") instanceof String))
            afficheErreur(msgErrOrd,fichierSortie);
    }

    public void verifierType2(JSONObject f,String fichierSortie)
            throws Exception {
        if(!(f.get("nom") instanceof String))
            afficheErreur(msgErrNom,fichierSortie);
        if(!(f.get("prenom") instanceof String))
            afficheErreur(msgErrPre,fichierSortie);
        if(!(f.get("sexe") instanceof Integer))
            afficheErreur(msgErrSexe,fichierSortie);
    }

    public void verifierAct(JSONObject f,String fichierSortie)
            throws Exception {
        for (int i = 0; i < this.activites.size(); i++){
            JSONObject objectAct = (JSONObject) this.activites.get(i);
            JSONArray caractAct = objectAct.toJSONArray(objectAct.names());

            if(caractAct.size() < 4)
                afficheErreur(msgErrActMiss, fichierSortie);
        }
    }

    public void afficheErreur(String pMessage, String fichierSortie)
            throws Exception {
        System.err.println(pMessage);
        imprimerErreurStructure(fichierSortie);
        System.exit( -1 );
    }

    /**
     * Code inspire de la methode save() du projet json-lib-ex ecrit par
     * Dogny, Gnagnely Serge
     */
    public static void imprimerErreurStructure(String fichierSortie)
            throws Exception {
        JSONObject fichierErr = new JSONObject();
        JSONArray listeErr = new JSONArray();
        fichierErr.put("Complet",true);
        listeErr.add("Le fichier d'entrée est invalide et le cycle est " +
                "incomplet.");
        fichierErr.put("Erreurs",listeErr);
        try(FileWriter f = new FileWriter(fichierSortie)){
            f.write(fichierErr.toString(3));
            f.flush();
        }
    }

    public void assignerChampHeuresTranf(JSONObject fichier){
        if(getFichier().get("heures_transferees_du_cycle_precedent")
                instanceof Integer){
            this.heuresTransferees = Integer.parseInt(fichier.get(
                    "heures_transferees_du_cycle_precedent").toString());
            isHeuresTransfereesNull = false;
        }else{
            this.heuresTransferees = 0;
            isHeuresTransfereesNull = true;
        }
    }
}

