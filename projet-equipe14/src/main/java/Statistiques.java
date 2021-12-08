import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Statistiques {
    private static final String SQUELETTE = "{\n   \"Completé\": \"0\",\n   \"Traités\": \"0\"\n}";
    private int rapportComplete = 0;
    private int rapportTraiter = 0 ;
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


    public Statistiques() {
        this.load();
    }

    public void load() {
        Path path = FileSystems.getDefault().getPath("C:\\Users\\home\\IdeaProjects\\inf2050-a21-projet-equipe14\\stats.json");
        File file = new File(String.valueOf(path));
        if (file.exists()) {
            System.out.println("existe");
            //setfiles
        } else {
            System.out.println("inexistant, le fichier stats.json sera créé maitenant");
            ecrireJson();
        }

    }

    public void save() {

    }

    private void ecrireJson() {
        String stats = "stats.json";
        File file = new File(stats);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(SQUELETTE);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


