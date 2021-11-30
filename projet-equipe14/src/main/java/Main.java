import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;

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
        FormationContinue formation = new FormationContinue(jsonObj,fichierSortie);
        Verification verificateur = choisiVerif(formation,fichierSortie);
        verifNonNull(fichierSortie,verificateur);
        verificateur.imprimer(fichierSortie);
    }

    public static Verification choisiVerif(FormationContinue formation,String fichierSortie) throws Exception {
        Verification verificateur = null;
        if(formation.getOrdre().equals("architectes"))
            verificateur = new Verification(formation,fichierSortie);
        if(formation.getOrdre().equals("géologues"))
            verificateur = new VerificationGeologue(formation,fichierSortie);
        if(formation.getOrdre().equals("psychologues"))
            verificateur = new VerificationPsychologues(formation, fichierSortie);
        if(formation.getOrdre().equals("podiatres"))
            verificateur = new VerificationPodiatres(formation, fichierSortie);
        return verificateur;
    }

    public static void verifNonNull(String fichierSortie, Verification verificateur) throws Exception {
        if(verificateur == null) {
            System.err.println("L'ordre est incorrect");
            FormationContinue.imprimerErreurStructure(fichierSortie);
            System.exit(-1);
        }
    }
}
