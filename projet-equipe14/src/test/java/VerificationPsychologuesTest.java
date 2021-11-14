import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerificationPsychologuesTest {

    private VerificationPsychologues psychologues;
    private FormationContinue formation;
    private JSONArray activities = new JSONArray();
    private JSONObject fichier = new JSONObject();

    @BeforeEach
    void beforeEach() throws Exception {
        fichier.put("numero_de_permis","A0001");
        fichier.put("cycle", "2018-2023");
        fichier.put("ordre", "psychologues");

        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "cours");
        activity.put("heures", 10);
        activity.put("date", "2021-10-22");
        activities.add(0,activity);

        JSONObject activity3 = new JSONObject();
        activity3.put("description", "Rédaction pour le magazine Architecture moderne");
        activity3.put("categorie", "cours");
        activity3.put("heures", 10);
        activity3.put("date", "2021-10-22");
        activities.add(2,activity3);

        JSONObject activity2 = new JSONObject();
        activity2.put("description", "Rédaction pour le magazine Architecture moderne");
        activity2.put("categorie", "conférence");
        activity2.put("heures", 140);
        activity2.put("date", "2021-10-22");
        activities.add(1, activity2);
        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json");
        psychologues = new VerificationPsychologues(formation, "resultat.json");
    }

    @Test
    void validationCycle() {
        boolean expected = true;
        boolean actual = psychologues.validationCycle();
        assertEquals(expected, actual);
    }

    @Test
    void validationDates() {
    }

    @Test
    void validDateCycleListe() {
    }

    @Test
    void regarderCategorie() {
    }

    @Test
    void calculHeureTotal() {
    }

    @Test
    void validationHeureMinimum() {
    }

    @Test
    void verifierChampHeuresTransf() {
    }

    @Test
    void validationFinal() {
    }
}