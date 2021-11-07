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
        String fichierEntree = args[0];
        String fichierSortie = args[1];


        String stringJson = IOUtils.toString(new
                FileInputStream(fichierEntree), "UTF-8");
        try {
            JSONObject jsonObj = (JSONObject) JSONSerializer.toJSON(stringJson);
            verifImprime(jsonObj,fichierSortie);
        }catch(JSONException e){
            System.out.println("Le fichier d'entrée n'est pas valide.");
        }
    }

    public static void verifImprime(JSONObject jsonObj,
                                    String fichierSortie) throws Exception {
        FormationContinue formation = new FormationContinue(jsonObj);

        Verification verificateur = new Verification(formation, fichierSortie);
        verificateur.imprimer(fichierSortie);
    }
}
