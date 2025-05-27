public class TestAnimaux {
    public static void main(String[] args) {
    Animal[] animaux = { new Chien(), new Chat() };
    for (Animal a : animaux) {
    a.crier(); // Appelle la bonne méthode selon l'objet réel
    }
    }
   }
   