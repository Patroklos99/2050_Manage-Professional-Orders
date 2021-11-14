import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.sf.json.*;
import static org.junit.jupiter.api.Assertions.*;

class FormationContinueTest {

    private MockFormationContinue formation;
    private JSONArray activities;
    private JSONObject fichier;

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
        activity.put("heures", 100);
        activity.put("date", "2021-10-22");
        activities.add(0,activity);
        JSONObject activity2 = new JSONObject();
        activity2.put("description", "Rédaction pour le magazine Architecture moderne");
        activity2.put("categorie", "conférence");
        activity2.put("heures", 140);
        activity2.put("date", "2021-10-22");
        activities.add(1, activity2);
        fichier.put("activites", activities);
        formation = new MockFormationContinue(fichier, "resultat.json");
    }

    @Test
    void getFichier() {
    }

    @Test
    void getOrdre() {
        String expected = "psychologues";
        String actual = formation.getOrdre();
        assertEquals(expected, actual);
    }

    @Test
    void getNumeroPermis() {
        String expected = "A0001";
        String actual = formation.getNumeroPermis();
        assertEquals(expected, actual);
    }

    @Test
    void getCycle() {
        String expected = "2018-2023";
        String actual = formation.getCycle();
        assertEquals(expected, actual);
    }

    @Test
    void getActivites() {
        JSONArray actual =  formation.getActivites();
        JSONArray expected = activities;
        assertEquals(expected, actual);
    }

    @Test
    void getHeuresTransferees() {
        int expected = 0;
        int actual = formation.getHeuresTransferees();
        assertEquals(expected, actual);
    }

    @Test
    void isHeuresTransfereesNull() {
        boolean expected = true;
        boolean actual = formation.isHeuresTransfereesNull();
        assertEquals(expected, actual);
    }

    @Test
    void setHeuresTransferees() {
        int expected = 0;
        int actual = formation.getHeuresTransferees();
        assertEquals(expected, actual);

        formation.setHeuresTransferees(15);

        int expected2 = 15;
        int actual2 = formation.getHeuresTransferees();
        assertEquals(expected2, actual2);
    }

    @Test
    void verifierTypeActivities() throws Exception {
        JSONObject fichierType = new JSONObject();
        JSONObject activities = new JSONObject();
        fichierType.put("numero_de_permis","A0001");
        fichierType.put("cycle", "2018-2023");
        fichierType.put("ordre", "psychologues");
        fichierType.put("activites", activities);
        formation.verifierType(fichierType, "resultat.json");
        String expected = "Les activités doivent être stocké dans un tableau";
        String actual = formation.getErreur();
        assertEquals(expected, actual);
    }

    @Test
    void verifierTypePermis() throws Exception {
        JSONObject fichierType = new JSONObject();
        JSONArray activities = new JSONArray();
        fichierType.put("numero_de_permis", 12345);
        fichierType.put("cycle", "2018-2023");
        fichierType.put("ordre", "psychologues");
        fichierType.put("activites", activities);
        formation.verifierType(fichierType, "resultat.json");
        String expected = "Le numéros de permis doit être une chaîne de caractères";
        String actual = formation.getErreur();
        assertEquals(expected, actual);
    }

    @Test
    void verifierTypeCycle() throws Exception {
        JSONObject fichierType = new JSONObject();
        JSONArray activities = new JSONArray();
        fichierType.put("numero_de_permis", "A0001");
        fichierType.put("cycle", 2018-2023);
        fichierType.put("ordre", "psychologues");
        fichierType.put("activites", activities);
        formation.verifierType(fichierType, "resultat.json");
        String expected = "Le cycle doit être une chaîne de caractères";
        String actual = formation.getErreur();
        assertEquals(expected, actual);
    }

    @Test
    void verifierTypeOrdrre() throws Exception {
        JSONObject fichierType = new JSONObject();
        JSONArray activities = new JSONArray();
        fichierType.put("numero_de_permis", "A0001");
        fichierType.put("cycle", "2018-2023");
        fichierType.put("ordre", 56);
        fichierType.put("activites", activities);
        formation.verifierType(fichierType, "resultat.json");
        String expected = "L'ordre doit être une chaîne de caractères";
        String actual = formation.getErreur();
        assertEquals(expected, actual);
    }

    @Test
    void assignerChampHeuresTranf() throws Exception {
        JSONArray activities = new JSONArray();
        JSONObject fichier = new JSONObject();
        fichier.put("heures_transferees_du_cycle_precedent", 24);
        fichier.put("numero_de_permis","A0001");
        fichier.put("cycle", "2018-2023");
        fichier.put("ordre", "psychologues");
        fichier.put("activites", activities);
        formation = new MockFormationContinue(fichier, "resultat.json");
        int expected = 24;
        int actual = formation.getHeuresTransferees();
        assertEquals(expected, actual);
    }
}

