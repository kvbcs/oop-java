package exos;
public class Personne {
    private String nom;
    private String prenom;
    private int age;

    // Constructeur standard
    public Personne(String nom, String prenom, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void sePresenter() {
        // Ajout d'un espace entre prénom et nom
        System.out.println("Bonjour " + prenom + " " + nom + ", tu as " + age + " ans.");
    }
}


