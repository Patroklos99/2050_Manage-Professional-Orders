import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class StatistiquesTest {
    private Statistiques stats;

    @BeforeEach
    void beforeEach() throws Exception {
        stats = new Statistiques();
        stats.setRapportTraiter(60);
        stats.setRapportComplete(48);
        stats.setIncompleteInvalide(12);
        stats.setRapportHommes(15);
        stats.setRapportFemmes(30);
        stats.setRapportInconnus(15);
        stats.setRapportActivite(124);
        stats.save();
    }

    @Test
    void validationGetRapportTraiter() {
        int actual = stats.getRapportTraiter();
        int expected = 60;
        assertEquals(expected, actual);
    }
    @Test
    void validationGetRapportComplete() {
        int actual = stats.getRapportComplete();
        int expected = 48;
        assertEquals(expected, actual);
    }
    @Test
    void validationGetIncompleteInvalide() {
        int actual = stats.getIncompleteInvalide();
        int expected = 12;
        assertEquals(expected, actual);
    }
    @Test
    void validationGetRapportHommes() {
        int actual = stats.getRapportHommes();
        int expected = 15;
        assertEquals(expected, actual);
    }
    @Test
    void validationGetRapportFemmes() {
        int actual = stats.getRapportFemmes();
        int expected = 30;
        assertEquals(expected, actual);
    }
    @Test
    void validationGetRapportInconnus() {
        int actual = stats.getRapportInconnus();
        int expected = 15;
        assertEquals(expected, actual);
    }
    @Test
    void validationGetRapportActivite() {
        int actual = stats.getRapportActivite();
        int expected = 124;
        assertEquals(expected, actual);
    }

    @Test
    void validationClear() {
        stats.clear();
        int actual = stats.getRapportActivite();
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    void validationSetRapportOrdreIncompletes() {
        HashMap<String, Integer> tempMap = stats.getRapportOrdreIncompletes();
        tempMap.put("psychologues", stats.getRapportOrdreIncompletes().get("psychologues")+1);
        stats.setRapportOrdreIncompletes(tempMap);
    }

    @Test
    void validationSetRapportOrdreCompletes() {
        HashMap<String, Integer> tempMap = stats.getRapportOrdreCompletes();
        tempMap.put("podiatres", stats.getRapportOrdreCompletes().get("podiatres")+1);
        stats.setRapportOrdreCompletes(tempMap);
    }

    @Test
    void validationAffichage() {
        validationSetRapportOrdreCompletes();
        validationSetRapportOrdreIncompletes();

        String actual = stats.toString();
        String expected = "Rapports Traités: " + 60 + "\n" +
        "Rapports Completés: " + 48 + "\n" +
        "Rapports Incomplets Invalides: " + 12 + "\n" +
        "Rapports Hommes: " + 15 + "\n" +
        "Rapports Femmes: " + 30 + "\n" +
        "Rapports Sex Inconnus: " + 15 + "\n" +
        "Rapports Activite: " + 124 + "\n" +
        "Rapports Activite Cours: " + 0 + "\n" +
        "Rapports Activite Atelier: " + 0 + "\n" +
        "Rapports Activite Seminaire: " + 0 + "\n" +
        "Rapports Activite Colloque: " + 0 + "\n" +
        "Rapports Activite Conference: " + 0 + "\n" +
        "Rapports Activite Lecture Dirigee: " + 0 + "\n" +
        "Rapports Activite Presentation: " + 0 + "\n" +
        "Rapports Activite Groupe De Discussion: " + 0 + "\n" +
        "Rapports Activite Projet De Recherche: " + 0 + "\n" +
        "Rapports Activite Redaction Professionnelle: " + 0 + "\n" +
        "Rapports Architecte Complet: " + 0 + "\n" +
        "Rapports Architecte Incomplet: " + 0 + "\n" +
        "Rapports Geologue Complet: " + 0 + "\n" +
        "Rapports Geologue Incomplet: " + 0 + "\n" +
        "Rapports Podiatres Complet: " + 1 + "\n" +
        "Rapports Podiatres Incomplet: " + 0 + "\n" +
        "Rapports Psychologue Complet: " + 0 + "\n" +
        "Rapports Psychologue Incomplet: " + 1 + "\n" +
        "Rapports Permis Valide: " + 0 + "\n";

        assertEquals(expected, actual);
    }
}