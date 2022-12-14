import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

public class Statistiques {
    private static final String SQUELETTE = "{\n   \"Completé\": \"0\",\n   \"Traités\": \"0\"\n}";
    private JSONObject jObject = new JSONObject();

    private static final String[] CAT = {"cours", "atelier", "séminaire",
            "colloque", "conférence", "lecture dirigée", "présentation",
            "groupe de discussion", "projet de recherche",
            "rédaction professionnelle"};

    private static final String[] CATJSON = {"Rapports_Activite_Cours",
            "Rapports_Activite_Atelier", "Rapports_Activite_Seminaire",
            "Rapports_Activite_Colloque", "Rapports_Activite_Conference",
            "Rapports_Activite_Lecture_Dirigee",
            "Rapports_Activite_Presentation",
            "Rapports_Activite_Groupe_De_Discussion",
            "Rapports_Activite_Projet_De_Recherche",
            "Rapports_Activite_Redaction_Professionnelle"};

    private static final String[] ORDRE = {"architectes", "géologues",
                                           "podiatres", "psychologues"};

    private static final String[] ORDRECOMPLET = {"Rapports_Architecte_Complet",
            "Rapports_Geologue_Complet", "Rapports_Podiatres_Complet",
            "Rapports_Psychologue_Complet"};

    private static final String[] ORDREINCOMPLET = {"Rapports_Architecte_Incomplet",
            "Rapports_Geologue_Incomplet", "Rapports_Podiatres_Incomplet",
            "Rapports_Psychologue_Incomplet"};

    private int rapportTraiter = 0;
    private int rapportComplete = 0;
    private int incompleteInvalide = 0;
    private int rapportHommes = 0;
    private int rapportFemmes = 0;
    private int rapportInconnus = 0;
    private int rapportActivite = 0;
    private HashMap<String, Integer> rapportActiviteCategorie = new HashMap<>();
    private HashMap<String, Integer> rapportOrdreCompletes = new HashMap<>();
    private HashMap<String, Integer> rapportOrdreIncompletes = new HashMap<>();
    private int rapportPermisValide = 0;

    public int getRapportActivite() {
        return rapportActivite;
    }

    public void setRapportActivite(int rapportActivite) {
        this.rapportActivite = rapportActivite;
    }

    public HashMap<String, Integer> getRapportActiviteCategorie() {
        return rapportActiviteCategorie;
    }

    public void setRapportActiviteCategorie(HashMap<String, Integer> rapportActiviteCategorie) {
        this.rapportActiviteCategorie = rapportActiviteCategorie;
    }

    public HashMap<String, Integer> getRapportOrdreCompletes() {
        return rapportOrdreCompletes;
    }

    public void setRapportOrdreCompletes(HashMap<String, Integer> rapportOrdreCompletes) {
        this.rapportOrdreCompletes = rapportOrdreCompletes;
    }

    public HashMap<String, Integer> getRapportOrdreIncompletes() {
        return rapportOrdreIncompletes;
    }

    public void setRapportOrdreIncompletes(HashMap<String, Integer> rapportOrdreIncompletes) {
        this.rapportOrdreIncompletes = rapportOrdreIncompletes;
    }

    public int getRapportPermisValide() {
        return rapportPermisValide;
    }

    public void setRapportPermisValide(int rapportPermisValide) {
        this.rapportPermisValide = rapportPermisValide;
    }

    public int getRapportComplete() {
        return rapportComplete;
    }

    public void setRapportComplete(int rapportComplete) {
        this.rapportComplete = rapportComplete;
    }

    public int getRapportTraiter() {
        return rapportTraiter;
    }

    public void setRapportTraiter(int rapportTraiter) {
        this.rapportTraiter = rapportTraiter;
    }

    public int getIncompleteInvalide() {
        return incompleteInvalide;
    }

    public void setIncompleteInvalide(int incompleteInvalide) {
        this.incompleteInvalide = incompleteInvalide;
    }

    public int getRapportHommes() {
        return rapportHommes;
    }

    public void setRapportHommes(int rapportHommes) {
        this.rapportHommes = rapportHommes;
    }

    public int getRapportFemmes() {
        return rapportFemmes;
    }

    public void setRapportFemmes(int rapportFemmes) {
        this.rapportFemmes = rapportFemmes;
    }

    public int getRapportInconnus() {
        return rapportInconnus;
    }

    public void setRapportInconnus(int rapportInconnus) {
        this.rapportInconnus = rapportInconnus;
    }

    public Statistiques() throws IOException {
        this.load();
    }

    public void load() throws IOException {
        Path path = FileSystems.getDefault().getPath("stats.json");
        File file = new File(String.valueOf(path));
        if (file.exists()) {
            String stringJson = IOUtils.toString(new
                    FileInputStream(file), "UTF-8");
            jObject = (JSONObject) JSONSerializer.toJSON(stringJson);
            assignerChamps(jObject);
        } else{
            clear();
            ecrireJson(jObject);
        }
    }

    public void assignerChamps(JSONObject jObject) {
        this.rapportTraiter = Integer.parseInt(String.valueOf(jObject.get("Rapports_Traites")));
        this.rapportComplete = Integer.parseInt(String.valueOf(jObject.get("Rapports_Completes")));
        this.incompleteInvalide = Integer.parseInt(String.valueOf(jObject.get("Rapports_Incomplets_Invalides")));
        this.rapportHommes = Integer.parseInt(String.valueOf(jObject.get("Rapports_Hommes")));
        this.rapportFemmes = Integer.parseInt(String.valueOf(jObject.get("Rapports_Femmes")));
        this.rapportInconnus = Integer.parseInt(String.valueOf(jObject.get("Rapports_Sex_Inconnus")));
        this.rapportActivite = Integer.parseInt(String.valueOf(jObject.get("Rapports_Activite")));
        this.peuplerHashMapCategorie();
        this.peuplerHashMapOrdres();
        this.rapportPermisValide = Integer.parseInt(String.valueOf(jObject.get("Rapports_Permis_Valide")));
    }

    public void peuplerHashMapCategorie(){
        for (int i = 0; i < CAT.length; i++){
            int stats = Integer.parseInt((jObject.get(CATJSON[i])).toString());
            rapportActiviteCategorie.put(CAT[i], stats);
        }
    }

    public void peuplerHashMapOrdres(){
        for (int i = 0; i < ORDRE.length; i++){
            int stats = Integer.parseInt((jObject.get(ORDRECOMPLET[i])).toString());
            rapportOrdreCompletes.put(ORDRE[i], stats);
            stats = Integer.parseInt((jObject.get(ORDREINCOMPLET[i])).toString());
            rapportOrdreIncompletes.put(ORDRE[i], stats);
        }
    }

    public void save() {
        ecrireJson(jObject);
    }

    public void ecrireJson(JSONObject jsonObj) {
        String stats = "stats.json";
        File file = new File(stats);
        jsonObj.put("Rapports_Traites", getRapportTraiter());
        jsonObj.put("Rapports_Completes", getRapportComplete());
        jsonObj.put("Rapports_Incomplets_Invalides", getIncompleteInvalide());
        jsonObj.put("Rapports_Hommes", getRapportHommes());
        jsonObj.put("Rapports_Femmes", getRapportFemmes());
        jsonObj.put("Rapports_Sex_Inconnus", getRapportInconnus());
        jsonObj.put("Rapports_Activite", getRapportActivite());

        for (int i = 0; i < CAT.length; i++) jsonObj.put(CATJSON[i], getRapportActiviteCategorie().get(CAT[i]));

        for (int i = 0; i < ORDRE.length; i++){
            jsonObj.put(ORDRECOMPLET[i], getRapportOrdreCompletes().get(ORDRE[i]));
            jsonObj.put(ORDREINCOMPLET[i], getRapportOrdreIncompletes().get(ORDRE[i]));
        }

        jsonObj.put("Rapports_Permis_Valide", getRapportPermisValide());

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(jsonObj.toString(3));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        rapportTraiter = 0;
        rapportComplete = 0;
        incompleteInvalide = 0;
        rapportHommes = 0;
        rapportFemmes = 0;
        rapportInconnus = 0;
        rapportActivite = 0;
        for (int i = 0; i < CAT.length; i++) rapportActiviteCategorie.put(CAT[i], 0);
        for (int i = 0; i < ORDRE.length; i++){
            rapportOrdreCompletes.put(ORDRE[i], 0);
            rapportOrdreIncompletes.put(ORDRE[i], 0);
        }
        rapportPermisValide = 0;

        save();
    }

    @Override
    public String toString() {
        return "Rapports Traités: " + rapportTraiter + "\n" +
                "Rapports Completés: " + rapportComplete + "\n" +
                "Rapports Incomplets Invalides: " + incompleteInvalide + "\n" +
                "Rapports Hommes: " + rapportHommes + "\n" +
                "Rapports Femmes: " + rapportFemmes + "\n" +
                "Rapports Sex Inconnus: " + rapportInconnus + "\n" +
                "Rapports Activite: " + rapportActivite + "\n" +
                "Rapports Activite Cours: " + rapportActiviteCategorie.get(CAT[0]) + "\n" +
                "Rapports Activite Atelier: " + rapportActiviteCategorie.get(CAT[1])  + "\n" +
                "Rapports Activite Seminaire: " + rapportActiviteCategorie.get(CAT[2])  + "\n" +
                "Rapports Activite Colloque: " + rapportActiviteCategorie.get(CAT[3])  + "\n" +
                "Rapports Activite Conference: " + rapportActiviteCategorie.get(CAT[4])  + "\n" +
                "Rapports Activite Lecture Dirigee: " + rapportActiviteCategorie.get(CAT[5])  + "\n" +
                "Rapports Activite Presentation: " + rapportActiviteCategorie.get(CAT[6])  + "\n" +
                "Rapports Activite Groupe De Discussion: " + rapportActiviteCategorie.get(CAT[7])  + "\n" +
                "Rapports Activite Projet De Recherche: " + rapportActiviteCategorie.get(CAT[8])  + "\n" +
                "Rapports Activite Redaction Professionnelle: " + rapportActiviteCategorie.get(CAT[9])  + "\n" +
                "Rapports Architecte Complet: " + rapportOrdreCompletes.get(ORDRE[0]) + "\n" +
                "Rapports Architecte Incomplet: " + rapportOrdreIncompletes.get(ORDRE[0]) + "\n" +
                "Rapports Geologue Complet: " + rapportOrdreCompletes.get(ORDRE[1]) + "\n" +
                "Rapports Geologue Incomplet: " + rapportOrdreIncompletes.get(ORDRE[1]) + "\n" +
                "Rapports Podiatres Complet: " + rapportOrdreCompletes.get(ORDRE[2]) + "\n" +
                "Rapports Podiatres Incomplet: " + rapportOrdreIncompletes.get(ORDRE[2]) + "\n" +
                "Rapports Psychologue Complet: " + rapportOrdreCompletes.get(ORDRE[3]) + "\n" +
                "Rapports Psychologue Incomplet: " + rapportOrdreIncompletes.get(ORDRE[3]) + "\n" +
                "Rapports Permis Valide: " + rapportPermisValide + "\n";
    }
}




