public class Freelance extends Employe {
    private int heures;
    private double tarifHoraire;

    public Freelance(String nom, int heures, double tarif) {
        super(nom);
        this.heures = heures;
        this.tarifHoraire = tarif;
    }

    @Override
    public double calculerSalaire() {
        return heures * tarifHoraire;
    }
   }