import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        String stringJson = IOUtils.toString(new
                        FileInputStream("test.json"), "UTF-8");
        JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(stringJson);

        FormationContinue formation = new FormationContinue(jsonObj);
        Verification verificateur = new Verification(formation);
        verificateur.imprimer("resultat.json");
    }
}
