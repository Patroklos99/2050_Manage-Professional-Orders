import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class VerificationGeologueTest{
    private String fichierEntree = "test.json";
    private String fichierSortie = "resultat.json";

    //private VerificationGeologue geologues = new VerificationGeologue(fichierEntree, fichierSortie);


    @Test
    void validationDates() {
        String expected = "1$";
        String actual = "1$";
        assertEquals(expected, actual);

    }

    @Test
    void validDateCycleListe() {
    }

    @Test
    void validationHeures1() {
    }

    @Test
    void calculHeureTotal() {
    }

    @Test
    void verifierChampHeuresTransf() {
    }

    @Test
    void validationCycle() {
    }

    @Test
    void validationHeureMinimum() {
    }

    @Test
    void validationFinal() {
    }

    @Test
    void validationToutes() {
    }
}