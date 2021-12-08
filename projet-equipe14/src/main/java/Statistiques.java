import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Statistiques {
    private static final String SQUELETTE = "{\n   \"Completé\": \"0\",\n   \"Traités\": \"0\"\n}";
    private JSONObject jObject = new JSONObject();
    private int rapportComplete = 0;
    private int rapportTraiter = 0;
    private int incompleteInvalide = 0;
    private int rapportHommes = 0;
    private int rapportFemmes = 0;
    private int rapportInconnus = 0;


    public int getRapportComplete() {
        return rapportComplete;
    }

    public void setRapportComplete(int rapportComplete) {
        this.rapportComplete = rapportComplete;
    }

    public int getRapportTraiter() {
        return rapportTraiter;
    }

    public void setRapportTraiter(int rapportTraité) {
        this.rapportTraiter = rapportTraité;
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


    public Statistiques(String fichierStats) throws IOException {
        this.load(fichierStats);
    }

    public void load(String fichierStats) throws IOException {
        Path path = FileSystems.getDefault().getPath("stats.json");
        File file = new File(String.valueOf(path));
        if (file.exists()) {
            String stringJson = IOUtils.toString(new
                    FileInputStream(fichierStats), "UTF-8");
            jObject = (JSONObject) JSONSerializer.toJSON(stringJson);
            System.out.println("Existe");
            assignerChamps(jObject);
        } else {
            System.out.println("Le fichier n'existe pas, il sera créé maintenant");
            ecrireJson(jObject);
        }
    }

    public void assignerChamps(JSONObject jObject) {
        System.out.println(jObject.toString());
        this.rapportTraiter = Integer.parseInt(String.valueOf(jObject.get("Rapports_Traités")));
        this.rapportComplete = Integer.parseInt(String.valueOf(jObject.get("Rapports_Completés")));
        this.incompleteInvalide = Integer.parseInt(String.valueOf(jObject.get("Rapports_Incomplets_Invalides")));
        this.rapportHommes = Integer.parseInt(String.valueOf(jObject.get("Rapports_Hommes")));
        this.rapportFemmes = Integer.parseInt(String.valueOf(jObject.get("Rapports_Femmes")));
        this.rapportInconnus = Integer.parseInt(String.valueOf(jObject.get("Rapports_Sex_Inconnus")));
    }


    public void save() {
    }

    public void ecrireJson(JSONObject jsonObj) {
        String stats = "stats.json";
        File file = new File(stats);
        jsonObj.put("Rapports_Traités", getRapportTraiter());
        jsonObj.put("Rapports_Completés", getRapportTraiter());
        jsonObj.put("Rapports_Incomplets_Invalides", getRapportTraiter());
        jsonObj.put("Rapports_Hommes", getRapportTraiter());
        jsonObj.put("Rapports_Femmes", getRapportTraiter());
        jsonObj.put("Rapports_Sex_Inconnus", getRapportTraiter());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(jsonObj.toString(3));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}




