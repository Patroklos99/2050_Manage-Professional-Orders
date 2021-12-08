import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        String fichierEntree = args[0];
        String fichierSortie = args[1];
        JSONObject jsonObj;

        Statistiques stats = new Statistiques();
        stats.setRapportTraiter(stats.getRapportTraiter()+1);
        stats.save();
        String stringJson = IOUtils.toString(new
                FileInputStream(fichierEntree), "UTF-8");
        try {
            jsonObj = (JSONObject) JSONSerializer.toJSON(stringJson);
            verifImprime(jsonObj,fichierSortie, stats);
        }catch(JSONException e){
            System.out.println("Le fichier d'entrée n'est pas valide.");
            stats.setIncompleteInvalide(stats.getIncompleteInvalide()+1);
            stats.save();
        }
    }

    public static void additionSex(JSONObject jsonObj, Statistiques stats) {
        int sexe = Integer.parseInt(String.valueOf(jsonObj.get("sexe")));
        switch (sexe) {
            case 0 :
                stats.setRapportInconnus(stats.getRapportInconnus()+1);
                stats.save();
                break;
            case 1 :
                stats.setRapportHommes(stats.getRapportHommes()+1);
                stats.save();
                break;
            case 2 :
                stats.setRapportFemmes(stats.getRapportFemmes()+1);
                stats.save();
                break;
        }
    }

    public static void verifImprime(JSONObject jsonObj,
                                    String fichierSortie, Statistiques stats) throws Exception {
        FormationContinue formation = new FormationContinue(jsonObj,fichierSortie, stats);
        Verification verificateur = choisiVerif(formation,fichierSortie, stats);
        verifNonNull(fichierSortie,verificateur, stats);
        additionSex(jsonObj, stats);
        verificateur.imprimer(fichierSortie, formation.getOrdre());
    }

    public static Verification choisiVerif(FormationContinue formation,String fichierSortie, Statistiques stats) throws Exception {
        Verification verificateur = null;
        if(formation.getOrdre().equals("architectes"))
            verificateur = new Verification(formation,fichierSortie, stats);
        if(formation.getOrdre().equals("géologues"))
            verificateur = new VerificationGeologue(formation,fichierSortie, stats);
        if(formation.getOrdre().equals("psychologues"))
            verificateur = new VerificationPsychologues(formation, fichierSortie, stats);
        if(formation.getOrdre().equals("podiatres"))
            verificateur = new VerificationPodiatres(formation, fichierSortie, stats);
        return verificateur;
    }

    public static void verifNonNull(String fichierSortie, Verification verificateur, Statistiques stats) throws Exception {
        if(verificateur == null) {
            stats.setIncompleteInvalide(stats.getIncompleteInvalide()+1);
            stats.save();
            System.err.println("L'ordre est incorrect");
            FormationContinue.imprimerErreurStructure(fichierSortie);
            System.exit(-1);
        }
    }
}
