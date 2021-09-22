import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FormationContinue {

    private String numeroPermis;
    private String cycle;
    private long heuresTransferees;
    private JSONArray activities;

    public FormationContinue (JSONObject fichier){
        this.numeroPermis = (String) fichier.get("numero_de_permis");
        this.cycle = (String) fichier.get("cycle");
        this.heuresTransferees = (long) fichier.get("heures_transferees_du_cycle_precedent");
        this.activities = (JSONArray) fichier.get("activites");
    }

    public String getNumeroPermis() {
        return numeroPermis;
    }

    public String getCycle() {
        return cycle;
    }

    public JSONArray getActivities() {
        return activities;
    }

    public long getHeuresTransferees() {
        return heuresTransferees;
    }

    public void setHeuresTransferees(long heuresTransferees) {
        this.heuresTransferees = heuresTransferees;
    }
}

