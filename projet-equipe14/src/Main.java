import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {

        Object obj = new JSONParser().parse(new FileReader("test.json"));
        JSONObject fichierEntree = (JSONObject) obj;

        FormationContinue formation = new FormationContinue(fichierEntree);
        Verification verificateur = new Verification(formation);

        JSONArray activites = verificateur.validationHeureFormat();
    }
}
