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

        verificateur.validationCategories();

        JSONArray activites = verificateur.validationHeureFormat();

        System.out.print(verificateur.resultat());

        System.out.println("Heures Catégorie Multiple: " + verificateur.validationHeuresCatégorieMultiple(activites));
        System.out.println("Heures Présentation: " + verificateur.calculHeuresMaxCategories("présentation", 23, activites));
        System.out.println("Heures Groupe de Discussion: " + verificateur.calculHeuresMaxCategories("groupe de discussion", 17, activites));
    }
}
