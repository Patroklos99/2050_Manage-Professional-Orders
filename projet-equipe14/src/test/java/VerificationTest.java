import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class VerificationTest {

    private MockVerification verif;
    private FormationContinue formation;
    private JSONArray activities;
    private JSONObject fichier;

    @BeforeEach
    void beforeEach() throws Exception {
        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis","A0001");
        fichier.put("cycle", "2018-2023");
        fichier.put("ordre", "");
        fichier.put("heures_transferees_du_cycle_precedent", 15);
        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "cours");
        activity.put("heures", 10);
        activity.put("date", "2018-01-01");
        activities.add(0,activity);
        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json");
        verif = new MockVerification(formation, "resultat.json");
    }

    @Test
    void getFichierSortie() {
    }

    @Test
    void resultat() {
    }

    @Test
    void validationCategories() throws Exception {
        verif.validationCategories();
        String actual =  verif.getErreur();
        String expected = "";
        assertEquals(expected, actual);

        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis","A0001");
        fichier.put("cycle", "2018-2023");
        fichier.put("ordre", "");
        fichier.put("heures_transferees_du_cycle_precedent", 15);
        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "bidon");
        activity.put("heures", 10);
        activity.put("date", "2018-01-01");
        activities.add(0,activity);
        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json");
        verif = new MockVerification(formation, "resultat.json");

        verif.validationCategories();
        String actual2 =  verif.getErreur();
        String expected2 = "La catégorie bidon n'existe pas dans la banque de catégories";
        assertEquals(expected2, actual2);
    }

    @Test
    void validationDates() throws Exception {
        verif.validationDates();
        int actual =  verif.categorieValide.size();
        int expected = 1;
        assertEquals(expected, actual);

        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis","A0001");
        fichier.put("cycle", "2018-2023");
        fichier.put("ordre", "psychologues");
        fichier.put("heures_transferees_du_cycle_precedent", 15);
        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "cours");
        activity.put("heures", 10);
        activity.put("date", "2050-01-01");
        activities.add(0,activity);
        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json");
        verif = new MockVerification(formation, "resultat.json");

        verif.validationDates();
        int actual2 =  verif.categorieValide.size();
        int expected2 = 0;
        assertEquals(expected2, actual2);
    }

    @Test
    void validationDateParCycle() throws Exception {
        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis","A0001");
        fichier.put("cycle", "2020-2022");
        fichier.put("ordre", "");
        fichier.put("heures_transferees_du_cycle_precedent", 15);
        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json");
        verif = new MockVerification(formation, "resultat.json");
        verif.validationDateParCycle("2021-01-01", "cours");
        int actual =  verif.categorieValide.size();
        int expected = 1;
        assertEquals(expected, actual);

        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis","A0001");
        fichier.put("cycle", "2018-2020");
        fichier.put("ordre", "");
        fichier.put("heures_transferees_du_cycle_precedent", 15);
        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json");
        verif = new MockVerification(formation, "resultat.json");
        verif.validationDateParCycle("2019-01-01", "cours");
        int actual2 =  verif.categorieValide.size();
        int expected2 = 1;
        assertEquals(expected2, actual2);
    }

    @Test
    void validationDatesPeriode() throws ParseException {
        boolean actual =  verif.validationDatesPeriode("2020-01-01", "cours", "2017-01-01",
                "2018-01-01");
        boolean expected = false;
        assertEquals(expected, actual);

        boolean actual2 =  verif.validationDatesPeriode("2020-01-01", "cours", "2017-01-01",
                "2025-01-01");
        boolean expected2 = true;
        assertEquals(expected2, actual2);
    }

    @Test
    void validationHeuresTransferees() throws Exception {
        verif.validationHeuresTransferees(7, 0);
        String actual =  verif.getErreur();
        String expected = "Le nombre d'heures transferees ("+ 15
                +") depasse la limite permise, seulement" +
                " 7h seront transferees";
        assertEquals(expected, actual);

        activities = new JSONArray();
        fichier = new JSONObject();
        fichier.put("numero_de_permis","A0001");
        fichier.put("cycle", "2018-2023");
        fichier.put("ordre", "");
        fichier.put("heures_transferees_du_cycle_precedent", 5);
        JSONObject activity = new JSONObject();
        activity.put("description", "Rédaction pour le magazine Architecture moderne");
        activity.put("categorie", "cours");
        activity.put("heures", 10);
        activity.put("date", "2018-01-01");
        activities.add(0,activity);
        fichier.put("activites", activities);
        formation = new FormationContinue(fichier, "resultat.json");
        verif = new MockVerification(formation, "resultat.json");

        verif.validationHeuresTransferees(7, 0);
        String actual2 =  verif.getErreur();
        String expected2 = "";
        assertEquals(expected2, actual2);
    }

    @Test
    void validationHeuresParCycle() {
    }

    @Test
    void validationHeuresCategorieMultiple() {
    }

    @Test
    void validationNbHeuresActivite() {
    }

    @Test
    void calculHeuresMaxCategories() {
    }

    @Test
    void regarderCategorie() {
    }

    @Test
    void validationHeures1() {
    }

    @Test
    void creationListeCategorieHeure() {
    }

    @Test
    void ajoutHeureListe() {
    }

    @Test
    void ajoutHeureParCategorie() {
    }

    @Test
    void calculHeure10Max() {
    }

    @Test
    void ajustementMaxHeures() {
    }

    @Test
    void calculHeureTotal() {
    }

    @Test
    void validationCycle() {
    }

    @Test
    void validationHeureFormat() {
    }

    @Test
    void creationListeBonnesActivites() {
    }

    @Test
    void validDateCycleListe() {
    }

    @Test
    void validDatePeriode() {
    }

    @Test
    void conditValidDate() {
    }

    @Test
    void validationFormatDate() {
    }

    @Test
    void validationBonneDate() {
    }

    @Test
    void validationFormatDate1() {
    }

    @Test
    void validationBonneDate1() {
    }

    @Test
    void afficherErrFormatDate() {
    }

    @Test
    void ajoutMsgErreur() {
    }

    @Test
    void validationNumeroPermis() {
    }

    @Test
    void validationDescription() {
    }

    @Test
    void verifierChampHeuresTransf() {
    }

    @Test
    void causerErreurVerif() {
    }

    @Test
    void validationFinal() {
    }

    @Test
    void validationGenerale() {
    }

    @Test
    void imprimer() {
    }
}