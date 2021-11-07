import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Géologue {

    public boolean isGeologue;

    public Géologue() {
        this.isGeologue = true;
    }

    public boolean isGeologue() {
        return isGeologue;
    }

    public void setGeologue(boolean geologue) {
        isGeologue = geologue;
    }

    public boolean checkHeureTotal(JSONArray activities){
        int heures = 0;

        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            heures += Integer.parseInt(activity.get("heures").toString());
        }

        return heures >= 55;
    }

    public boolean checkActivite(JSONArray activities, String categorie, int pHeureRequise){
        int heures = 0;

        for (Object o : activities) {
            JSONObject activity = (JSONObject) o;
            if(activity.get("categorie").toString().contentEquals(categorie))
                heures += Integer.parseInt(activity.get("heures").toString());
        }

        return heures >= pHeureRequise;
    }
}
