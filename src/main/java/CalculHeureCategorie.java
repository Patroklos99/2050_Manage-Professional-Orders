public class CalculHeureCategorie {
    private String categorie;
    private int heure;

    public CalculHeureCategorie(String categorie,int heure){
        this.categorie = categorie;
        this.heure = heure;
    }

    public String getCategorie() {
        return categorie;
    }

    public int getHeure() {
        return heure;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setHeure(int heure) {
        this.heure = heure;
    }

    @Override
    public String toString() {
        return "CalculHeureCategorie{" +
                "categorie='" + categorie + '\'' +
                ", heure=" + heure +
                '}';
    }
}
