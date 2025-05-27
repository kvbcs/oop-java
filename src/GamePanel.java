// Importation des classes nécessaires pour l'interface graphique et la logique
import javax.swing.*;          // Pour les composants Swing (boutons, champs de texte, labels...)
import java.awt.*;             // Pour dessiner à l'écran (Graphics, Color, etc.)
import java.util.ArrayList;    // Pour stocker la trajectoire du projectile
import java.util.Random;       // Pour générer des positions aléatoires et le vent

// La classe GamePanel hérite de JPanel : c’est notre zone de jeu personnalisée
public class GamePanel extends JPanel {

    // Générateur de nombres aléatoires
    private final Random random = new Random();

    // Variables pour les saisies du joueur et le vent
    private int angle, speed, wind;

    // Coordonnées du canon et de la cible
    private int cannonX, cannonY;
    private int targetX, targetY;

    // Position et vitesse du projectile
    private double projX, projY, vx, vy;

    // Gravité qui fait descendre le projectile
    private final int gravity = 1;

    // Score du joueur
    private int score = 0;

    // Indique si un tir est en cours
    private boolean isShooting = false;

    // Liste des points pour dessiner la trajectoire
    private final java.util.List<Point> trajectory = new ArrayList<>();

    // Champs de saisie pour l’angle et la vitesse
    private final JTextField angleField = new JTextField(5);
    private final JTextField speedField = new JTextField(5);

    // Label pour afficher le score
    private final JLabel scoreLabel = new JLabel("Score: 0");

    // Timer pour actualiser l’animation du projectile
    private final Timer timer;

    // Constructeur du panneau de jeu
    public GamePanel() {
        // On utilise une disposition simple : les composants s’ajoutent les uns à la suite
        setLayout(new FlowLayout());

        // Ajout des éléments de saisie et des boutons à l’interface
        add(new JLabel("Angle (0-90):"));
        add(angleField);
        add(new JLabel("Vitesse (10-100):"));
        add(speedField);

        JButton shootButton = new JButton("Tirer"); // Bouton pour tirer
        JButton resetButton = new JButton("Nouvelle position"); // Bouton pour recommencer

        add(shootButton);
        add(resetButton);
        add(scoreLabel);

        // Action quand on clique sur "Tirer"
        shootButton.addActionListener(e -> startShooting());

        // Action quand on clique sur "Nouvelle position"
        resetButton.addActionListener(e -> {
            // On relance le placement du canon et de la cible
            SwingUtilities.invokeLater(() -> {
                placeCannonAndTarget();
                repaint(); // Redessiner l’écran
            });
        });

        // Initialisation du timer : toutes les 50 ms, on met à jour le projectile
        timer = new Timer(50, e -> updateProjectile());

        // Placement initial du canon et de la cible
        placeCannonAndTarget();
    }

    // Méthode pour placer le canon et la cible à des positions aléatoires
    private void placeCannonAndTarget() {
        int panelHeight = getHeight();
        if (panelHeight <= 100) panelHeight = 600; // Si la hauteur est trop petite, on utilise une valeur par défaut

        cannonX = random.nextInt(100); // Position du canon côté gauche
        cannonY = panelHeight - 50;    // En bas du panneau
        targetX = 400 + random.nextInt(150); // Position de la cible côté droit
        targetY = 50 + random.nextInt(panelHeight - 100); // Hauteur aléatoire pour la cible

        trajectory.clear(); // On efface l’ancienne trajectoire
        isShooting = false; // Aucun tir en cours
        repaint();          // Redessiner l’écran
    }

    // Méthode appelée lorsqu'on clique sur "Tirer"
    private void startShooting() {
        try {
            // Lecture et conversion des saisies
            angle = Integer.parseInt(angleField.getText());
            speed = Integer.parseInt(speedField.getText());

            // Vérification des valeurs entrées
            if (angle < 0 || angle > 90 || speed < 10 || speed > 100) {
                JOptionPane.showMessageDialog(this, "Valeurs invalides.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Saisir des valeurs valides !");
            return;
        }

        // Vent aléatoire entre -5 (gauche) et +5 (droite)
        wind = -5 + random.nextInt(11);

        // Conversion de l’angle en radians
        double radians = Math.toRadians(angle);

        // Calcul des vitesses initiales selon l’angle et la vitesse
        vx = Math.cos(radians) * speed / 2;
        vy = -Math.sin(radians) * speed / 2;

        // Le projectile démarre depuis le canon
        projX = cannonX;
        projY = cannonY;

        trajectory.clear(); // Efface l’ancienne trajectoire
        isShooting = true;  // Le tir commence

        timer.start();      // Lancement du timer
    }

    // Méthode appelée toutes les 50ms par le timer
    private void updateProjectile() {
        if (!isShooting) return;

        // Mise à jour de la position du projectile
        projX += vx;
        vx += wind / 10.0; // Influence du vent
        projY += vy;
        vy += gravity;     // Influence de la gravité

        // Sauvegarde de la position actuelle pour dessiner la trajectoire
        trajectory.add(new Point((int) projX, (int) projY));

        // Si le projectile sort de l’écran : on arrête le tir
        if (projX < 0 || projX > getWidth() || projY > getHeight()) {
            isShooting = false;
            timer.stop();
        }

        // Création de rectangles pour tester la collision
        Rectangle projectile = new Rectangle((int) projX, (int) projY, 5, 5);
        Rectangle cible = new Rectangle(targetX, targetY, 20, 20);

        // Si collision avec la cible : on augmente le score
        if (projectile.intersects(cible)) {
            score++;
            scoreLabel.setText("Score: " + score);
            isShooting = false;
            timer.stop();
        }

        // Rafraîchissement de l’écran
        repaint();
    }

    // Méthode qui dessine tous les éléments à l’écran
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Efface l’écran avec du blanc
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Dessine le canon en bleu
        g.setColor(Color.BLUE);
        g.fillRect(cannonX, cannonY, 20, 20);

        // Dessine la cible en rouge
        g.setColor(Color.RED);
        g.fillRect(targetX, targetY, 20, 20);

        // Dessine la trajectoire du projectile (en noir)
        g.setColor(Color.BLACK);
        for (Point p : trajectory) {
            g.fillOval(p.x, p.y, 3, 3); // Petits points noirs
        }

        // Si le tir est en cours, on dessine le projectile (en vert)
        if (isShooting) {
            g.setColor(Color.GREEN);
            g.fillOval((int) projX, (int) projY, 8, 8);
        }
    }
}
