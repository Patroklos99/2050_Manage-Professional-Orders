import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;

public class FormationContinue {

    private String numeroPermis;
    private String cycle;
    private int heuresTransferees;
    private JSONArray activites;

    public FormationContinue (JSONObject fichier){
        this.numeroPermis = (String) fichier.get("numero_de_permis");
        this.cycle = (String) fichier.get("cycle");
        this.heuresTransferees = (int) fichier.get(
                "heures_transferees_du_cycle_precedent");
        this.activites = (JSONArray) fichier.get("activites");
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

    public void setHeuresTransferees(int heuresTransferees) {
        this.heuresTransferees = heuresTransferees;
    }
}

