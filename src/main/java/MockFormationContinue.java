import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MockFormationContinue extends FormationContinue{
    public MockFormationContinue(JSONObject fichier, String fichierSortie, Statistiques stats)
            throws Exception {
        super(fichier, fichierSortie, stats);
    }

    private String msgErrAct = "Les activités doivent être stocké dans " +
            "un tableau";
    private String msgErrNo = "Le numéros de permis doit être une " +
            "chaîne de caractères";
    private String msgErrCyc = "Le cycle doit être une chaîne de " +
            "caractères";
    private String msgErrOrd = "L'ordre doit être une chaîne de " +
            "caractères";

    private String err;

    public void verifierType(JSONObject f,String fichierSortie)
            throws Exception {
        if(!(f.get("activites") instanceof JSONArray))
            err = msgErrAct;
            afficheErreur(msgErrAct, fichierSortie);
        if(!(f.get("numero_de_permis") instanceof String))
            err = msgErrNo;
            afficheErreur(msgErrAct, fichierSortie);
        if(!(f.get("cycle") instanceof String))
            err = msgErrCyc;
            afficheErreur(msgErrAct, fichierSortie);
        if(!(f.get("ordre") instanceof String))
            err = msgErrOrd;
            afficheErreur(msgErrAct, fichierSortie);
    }

    public void afficheErreur(String pMessage, String fichierSortie)
            throws Exception {
        imprimerErreurStructure(fichierSortie);
        //System.exit( -1 );
    }

    public String getErreur(){
        return err;
    }

}
