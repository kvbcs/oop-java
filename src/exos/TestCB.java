package exos;
public class TestCB {
    public static void main(String[] args) {
        CompteBancaire cb = new CompteBancaire(100);

        cb.getSolde();
        cb.deposer(40);
        cb.getSolde();
        cb.retirer(100);
        cb.getSolde();

    }

}
