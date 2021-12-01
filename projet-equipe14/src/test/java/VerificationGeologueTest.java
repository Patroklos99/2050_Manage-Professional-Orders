import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;



class VerificationGeologueTest{

    private VerificationGeologue geologues;
    private FormationContinue formation;
    private JSONArray activities;
    private JSONObject fichier;

    @BeforeEach
    void beforeEach() throws Exception {
        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis", "A0001");
        fichier.put("cycle", "2018-2021");
        fichier.put("ordre", "geologues");


        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "cours");
        activity.put("heures", 10);
        activity.put("date", "2019-10-22");
        activities.add(0, activity);

        JSONObject activity2 = new JSONObject();
        activity2.put("description", "Rédaction pour le magazine Architecture moderne");
        activity2.put("categorie", "cours");
        activity2.put("heures", 10);
        activity2.put("date", "2019-10-23");
        activities.add(1, activity2);

        JSONObject activity3 = new JSONObject();
        activity3.put("description", "Rédaction pour le magazine Architecture moderne");
        activity3.put("categorie", "cours");
        activity3.put("heures", 10);
        activity3.put("date", "2019-10-24");
        activities.add(2, activity3);

        JSONObject activity4 = new JSONObject();
        activity4.put("description", "Rédaction pour le magazine Architecture moderne");
        activity4.put("categorie", "groupe de discussion");
        activity4.put("heures", 1);
        activity4.put("date", "2019-10-25");
        activities.add(3, activity4);

        JSONObject activity5 = new JSONObject();
        activity5.put("description", "Rédaction pour le magazine Architecture moderne");
        activity5.put("categorie", "projet de recherche");
        activity5.put("heures", 3);
        activity5.put("date", "2019-10-24");
        activities.add(4, activity5);

        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json");
        geologues = new VerificationGeologue(formation, "resultat.json");
    }

    @Test
    void validationDates() throws ParseException {
        geologues.validationDates();
        int actual = geologues.categorieValide.size();
        int expected = 3;
        assertEquals(expected, actual);

    }

    @Test
    void validationCycle() throws Exception {
        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis", "A0001");
        fichier.put("cycle", "2018-2022");
        fichier.put("ordre", "geologues");

        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "cours");
        activity.put("heures", 10);
        activity.put("date", "2019-10-22");
        activities.add(0, activity);

        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json");
        geologues = new VerificationGeologue(formation, "resultat.json");

        boolean actual = geologues.validationCycle();
        boolean expected = false;
        assertEquals(expected, actual);


    }
}
