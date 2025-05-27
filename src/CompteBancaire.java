public class CompteBancaire {
    private double solde;
    public CompteBancaire(double soldeInitial) {
    this.solde = soldeInitial;
    }

    public void deposer(double montant) {
        this.solde += montant;
        System.out.print("Montant déposé, CB : " + solde);
    }

    public void retirer(double montant) {
        if (solde >= montant){

            this.solde -= montant;
            System.out.print("Montant retiré, CB : " + solde);

        } else {
            System.out.print("Montant insuffisant");
        }
    }
    public double getSolde() {
    return solde;
    }
   }