package exos;
public abstract class Employe {
    protected String nom;
    public Employe(String nom) {
    this.nom = nom;
    }
    public abstract double calculerSalaire();
   }
 
   