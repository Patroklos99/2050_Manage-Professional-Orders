import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        fichier.put("numero_de_permis","A0001");
        fichier.put("cycle", "2018-2023");
        fichier.put("ordre", "psychologues");


        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "cours");
        activity.put("heures", 10);
        activity.put("date", "2021-10-22");
        activities.add(0,activity);



        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json", stats);
        psychologues = new VerificationPsychologues(formation, "resultat.json", stats);
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
/*
* JSONObject activity2 = new JSONObject();
        activity2.put("description", "Rédaction pour le magazine Architecture moderne");
        activity2.put("categorie", "cours");
        activity2.put("heures", 10);
        activity2.put("date", "2021-10-22");
        activities.add(1,activity2);

        JSONObject activity3 = new JSONObject();
        activity3.put("description", "Rédaction pour le magazine Architecture moderne");
        activity3.put("categorie", "conférence");
        activity3.put("heures", 10);
        activity3.put("date", "2021-10-24");
        activities.add(2,activity3);

        JSONObject activity4 = new JSONObject();
        activity4.put("description", "Rédaction pour le magazine Architecture moderne");
        activity4.put("categorie", "colloque");
        activity4.put("heures", 10);
        activity4.put("date", "2021-10-29");
        activities.add(3,activity4);

        JSONObject activity5 = new JSONObject();
        activity5.put("description", "Rédaction pour le magazine Architecture moderne");
        activity5.put("categorie", "colloque");
        activity5.put("heures", 10);
        activity5.put("date", "2021-10-24");
        activities.add(4,activity5);

        JSONObject activity6 = new JSONObject();
        activity6.put("description", "Rédaction pour le magazine Architecture moderne");
        activity6.put("categorie", "cours");
        activity6.put("heures", 10);
        activity6.put("date", "2021-10-22");
        activities.add(5,activity6);

        JSONObject activity7 = new JSONObject();
        activity7.put("description", "Rédaction pour le magazine Architecture moderne");
        activity7.put("categorie", "séminaire");
        activity7.put("heures", 10);
        activity7.put("date", "2021-10-28");
        activities.add(6,activity7);

        JSONObject activity8 = new JSONObject();
        activity8.put("description", "Rédaction pour le magazine Architecture moderne");
        activity8.put("categorie", "séminaire");
        activity8.put("heures", 10);
        activity8.put("date", "2021-10-28");
        activities.add(7,activity8);
* */