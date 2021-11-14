import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculHeureCategorieTest {

    private CalculHeureCategorie test = new CalculHeureCategorie("categorie", 25);

    @Test
    void getCategorie() {
        String actual = test.getCategorie();
        String expected = "categorie";
        assertEquals(expected, actual);
    }

    @Test
    void getHeure() {
        int actual = test.getHeure();
        int expected = 25;
        assertEquals(expected, actual);
    }

    @Test
    void setCategorie() {
        test.setCategorie("nouvelle categorie");
        String actual = test.getCategorie();
        String expected = "nouvelle categorie";
        assertEquals(expected, actual);
    }

    @Test
    void setHeure() {
        test.setHeure(10);
        int actual = test.getHeure();
        int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    void testToString() {
        test = new CalculHeureCategorie("categorie", 25);
        String actual = test.toString();
        String expected = "CalculHeureCategorie{" + "categorie='" + "categorie" + '\'' +
        ", heure=" + 25 + '}';
        assertEquals(expected, actual);
    }
}