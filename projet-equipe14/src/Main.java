import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        Object obj = new JSONParser().parse(new FileReader("test.json"));
        JSONObject fichierEntree = (JSONObject) obj;

        FormationContinue formation = new FormationContinue(fichierEntree);
        Verification verificateur = new Verification(formation);

        System.out.println("Catégorie: " + verificateur.validationCategories());
        System.out.println("Heures Catégorie Multiple: " + verificateur.validationHeuresCatégorieMultiple());
        System.out.println("Heures Présentation: " + verificateur.calculHeuresPresentation());
        System.out.println("Heures Groupe de Discussion: " + verificateur.calculHeuresGroupeDeDiscussion());
    }
}
