import net.sf.json.*;
import org.apache.commons.io.IOUtils;

import java.util.Arrays;

public class FormationContinue {

    private String ordre;
    private String numeroPermis;
    private String cycle;
    private int heuresTransferees;
    private JSONArray activites;
    private JSONObject fichier;

    public FormationContinue (JSONObject fichier){
        verifierType(fichier);
        this.fichier = fichier;
        this.ordre = fichier.get("ordre").toString();
        this.numeroPermis = fichier.get("numero_de_permis").toString();
        this.cycle = fichier.get("cycle").toString();
        this.heuresTransferees = Integer.parseInt(fichier.get(
                "heures_transferees_du_cycle_precedent").toString());
        this.activites = (JSONArray) fichier.get("activites");

    }

    public JSONObject getFichier(){
        return fichier;
    }

    public String getOrdre(){
        return ordre;
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

    public void verifierType(JSONObject f){
        if(!(f.get("activites") instanceof JSONArray))
            Erreur("Les activités doivent être stocké dans un tableau");
        if(!(f.get("heures_transferees_du_cycle_precedent") instanceof Integer))
            Erreur("Les heures transférées doivent être un chiffre");
        if(!(f.get("numero_de_permis") instanceof String))
            Erreur("Le numéros de permis doit être une chaîne de caractères");
        if(!(f.get("cycle") instanceof String))
            Erreur("Le cycle doit être une chaîne de caractères");
        if(!(f.get("ordre") instanceof String))
            Erreur("L'ordre doit être une chaîne de caractères");
    }

    public void Erreur(String pMessage){
        System.err.println(pMessage);
        System.exit( -1 );
    }
}

