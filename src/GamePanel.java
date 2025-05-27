import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel {
    private final Random random = new Random();
    private int angle, speed, wind;
    private int cannonX, cannonY;
    private int targetX, targetY;
    private double projX, projY, vx, vy;
    private final int gravity = 1;
    private int score = 0;
    private boolean isShooting = false;
    private final java.util.List<Point> trajectory = new ArrayList<>();

    private final JTextField angleField = new JTextField(5);
    private final JTextField speedField = new JTextField(5);
    private final JLabel scoreLabel = new JLabel("Score: 0");

    private final Timer timer;

    public GamePanel() {
        setLayout(new FlowLayout());

        add(new JLabel("Angle (0-90):"));
        add(angleField);
        add(new JLabel("Vitesse (10-100):"));
        add(speedField);

        JButton shootButton = new JButton("Tirer");
        JButton resetButton = new JButton("Nouvelle position");
        add(shootButton);
        add(resetButton);
        add(scoreLabel);

        shootButton.addActionListener(e -> startShooting());
        resetButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                placeCannonAndTarget();
                repaint();
            });
        });
        

        timer = new Timer(50, e -> updateProjectile());
        placeCannonAndTarget();
    }

    private void placeCannonAndTarget() {
        int panelHeight = getHeight();
        if (panelHeight <= 100) panelHeight = 600; // Valeur par défaut
    
        cannonX = random.nextInt(100);
        cannonY = panelHeight - 50;
        targetX = 400 + random.nextInt(150);
        targetY = 50 + random.nextInt(panelHeight - 100);
    
        trajectory.clear();
        isShooting = false;
        repaint();
    }
    
    private void startShooting() {
        try {
            angle = Integer.parseInt(angleField.getText());
            speed = Integer.parseInt(speedField.getText());
            if (angle < 0 || angle > 90 || speed < 10 || speed > 100) {
                JOptionPane.showMessageDialog(this, "Valeurs invalides.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Saisir des valeurs valides !");
            return;
        }

        wind = -5 + random.nextInt(11); // -5 à +5
        double radians = Math.toRadians(angle);
        vx = Math.cos(radians) * speed / 2;
        vy = -Math.sin(radians) * speed / 2;

        projX = cannonX;
        projY = cannonY;
        trajectory.clear();
        isShooting = true;
        timer.start();
    }

    private void updateProjectile() {
        if (!isShooting) return;

        projX += vx;
        vx += wind / 10.0;
        projY += vy;
        vy += gravity;

        trajectory.add(new Point((int) projX, (int) projY));

        if (projX < 0 || projX > getWidth() || projY > getHeight()) {
            isShooting = false;
            timer.stop();
        }

        Rectangle projectile = new Rectangle((int) projX, (int) projY, 5, 5);
        Rectangle cible = new Rectangle(targetX, targetY, 20, 20);

        if (projectile.intersects(cible)) {
            score++;
            scoreLabel.setText("Score: " + score);
            isShooting = false;
            timer.stop();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fond
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Canon
        g.setColor(Color.BLUE);
        g.fillRect(cannonX, cannonY, 20, 20);

        // Cible
        g.setColor(Color.RED);
        g.fillRect(targetX, targetY, 20, 20);

        // Trajectoire
        g.setColor(Color.BLACK);
        for (Point p : trajectory) {
            g.fillOval(p.x, p.y, 3, 3);
        }

        // Projectile actuel
        if (isShooting) {
            g.setColor(Color.GREEN);
            g.fillOval((int) projX, (int) projY, 8, 8);
        }
    }
}
