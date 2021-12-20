import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class VerificationPsychologuesTest {

    private VerificationPsychologues psychologues;
    private FormationContinue formation;
    private JSONArray activities;
    private JSONObject fichier;
    private Statistiques stats;


    @BeforeEach
    void beforeEach() throws Exception {
        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis","12345-67");
        fichier.put("cycle", "2018-2023");
        fichier.put("ordre", "psychologues");
        fichier.put("nom", "Frazilien");
        fichier.put("prenom", "William");
        fichier.put("sexe", 1);
        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "cours");
        activity.put("heures", 10);
        activity.put("date", "2021-10-22");
        activities.add(0,activity);
        fichier.put("activites", activities);
        stats = new Statistiques();
        formation = new FormationContinue(fichier, "resultat.json", stats);
        psychologues = new VerificationPsychologues(formation, "resultat.json", stats);
    }

    @Test
    void validationCycle() throws Exception {
        boolean expected = true;
        boolean actual = psychologues.validationCycle();
        assertEquals(expected, actual);

        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis","12345-67");
        fichier.put("cycle", "2018-2020");
        fichier.put("ordre", "psychologues");
        fichier.put("nom", "Frazilien");
        fichier.put("prenom", "William");
        fichier.put("sexe", 1);
        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "cours");
        activity.put("heures", 10);
        activity.put("date", "2021-10-22");
        activities.add(0,activity);
        fichier.put("activites", activities);
        stats = new Statistiques();
        formation = new FormationContinue(fichier, "resultat.json", stats);
        psychologues = new VerificationPsychologues(formation, "resultat.json", stats);

        boolean expected2 = false;
        boolean actual2 = psychologues.validationCycle();
        assertEquals(expected2, actual2);
    }

    @Test
    void regarderCategorie() {
        int actual = psychologues.regarderCategorie("conférence", 20);
        int expected = 15;
        assertEquals(expected, actual);

        int actual2 = psychologues.regarderCategorie("cours", 20);
        int expected2 = 20;
        assertEquals(expected2, actual2);
    }
}