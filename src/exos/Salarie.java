package exos;
public class Salarie extends Employe {
    private double salaireMensuel;

    public Salarie(String nom, double salaire) {
        super(nom);
        this.salaireMensuel = salaire;
    }
    @Override
    public double calculerSalaire() {
    return salaireMensuel;
    }
   }