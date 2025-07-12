/*  
╔══════════════════════════════════════════════════════╗
║  Projet Java – Le livre dont vous êtes le héros      ║
║  Le Pirate des 7 Mers                               ║
║                                                      ║
║  ESGI 2 – Franck Giordano & Louis Dalet – 2025      ║
╚══════════════════════════════════════════════════════╝
*/

package com.example.view;

import com.example.controller.GameController;
import com.example.model.Chapitre;
import com.example.model.Choix;
import com.example.model.Objet;
import com.example.model.Personnage;
import com.example.model.Scenario;
import com.example.model.GameState;
import com.example.util.SaveManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.html.HTMLDocument;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class GameView extends JFrame {
    // Contrôleur du jeu, initialisé au lancement ou au chargement
    private GameController controller;
    // Scénario de l'aventure, chargé au démarrage
    private final Scenario scenario;
    // Layout pour basculer entre les écrans
    private final CardLayout mainLayout = new CardLayout();
    private JPanel cardContainer;

    // Composants UI principaux
    private JLabel lblTitre;
    private JTextPane txtContent;
    private JPanel pnlChoices;
    private JPanel pnlStatus;

    // Polices et images de fond personnalisées
    private Font pirateFont;
    private Font textFont;
    private Image bgWood;
    private Image bgScroll;

    public GameView(Scenario scenario) {
        // On garde le scénario et on attend de créer le contrôleur plus tard
        this.scenario = scenario;
        this.controller = null;
        // Chargement des images et polices custom
        loadResources();
        // Initialisation de l'interface graphique
        initComponents();
        // Réglages de la fenêtre principale
        setTitle("Le Pirate des Sept Mers");
        setMinimumSize(new Dimension(1280, 800));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadResources() {
        try {
            // Lecture des images de fond
            bgWood = ImageIO.read(getClass().getResourceAsStream("/images/fond_bois.png"));
            bgScroll = ImageIO.read(getClass().getResourceAsStream("/images/parchemin.png"));
            // Chargement d'une police pirate si disponible
            InputStream f = getClass().getResourceAsStream("/polices/police_pirate.ttf");
            if (f != null) {
                pirateFont = Font.createFont(Font.TRUETYPE_FONT, f).deriveFont(Font.BOLD, 24f);
            } else {
                pirateFont = new Font("Serif", Font.BOLD, 24);
            }
        } catch (Exception e) {
            // En cas de souci, on utilise une police standard
            System.err.println("Erreur de chargement des ressources. Utilisation des polices par défaut.");
            pirateFont = new Font("Serif", Font.BOLD, 24);
        }
        // Police pour le texte courant
        textFont = new Font("Georgia", Font.PLAIN, 18);
    }

    private void initComponents() {
        // Panneau de fond avec image bois dessinée
        JLayeredPane rootLayeredPane = new JLayeredPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgWood != null) {
                    g.drawImage(bgWood, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        setContentPane(rootLayeredPane);

        // Redimensionnement automatique des calques
        rootLayeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (Component c : ((JLayeredPane) e.getComponent()).getComponents()) {
                    c.setBounds(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight());
                }
            }
        });

        // Couche pour les particules animées
        ParticlePane particlePane = new ParticlePane();
        rootLayeredPane.add(particlePane, Integer.valueOf(1));

        // Conteneur principal pour les cartes MENU et GAME
        cardContainer = new JPanel(mainLayout);
        cardContainer.setOpaque(false);
        rootLayeredPane.add(cardContainer, Integer.valueOf(2));

        // Création de la barre de menu et des écrans
        creerBarreMenu();
        JPanel menuCard = createMenuScreen();
        JPanel gameCard = createGameScreen();

        cardContainer.add(menuCard, "MENU");
        cardContainer.add(gameCard, "GAME");

        // Affichage par défaut du menu principal
        mainLayout.show(cardContainer, "MENU");
    }

    private void creerBarreMenu() {
        // Menu Fichier avec options Sauvegarder et Retour au menu
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(false);
        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.setFont(pirateFont.deriveFont(16f));

        JMenuItem itemSauvegarder = new JMenuItem("Sauvegarder la partie");
        itemSauvegarder.setFont(pirateFont.deriveFont(16f));
        itemSauvegarder.addActionListener(e -> {
            if (controller != null) {
                controller.sauvegarderPartie();
                styleJOptionPane();
                JOptionPane.showMessageDialog(this, "Partie sauvegardée !", "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenuItem itemRetourMenu = new JMenuItem("Retour au menu principal");
        itemRetourMenu.setFont(pirateFont.deriveFont(16f));
        itemRetourMenu.addActionListener(e -> {
            if (getJMenuBar() != null) {
                getJMenuBar().setVisible(false);
            }
            mainLayout.show(cardContainer, "MENU");
        });

        menuFichier.add(itemSauvegarder);
        menuFichier.add(new JSeparator());
        menuFichier.add(itemRetourMenu);
        menuBar.add(menuFichier);
        setJMenuBar(menuBar);
        getJMenuBar().setVisible(false);
    }

    private JPanel createMenuScreen() {
        // Écran de menu principal avec couverture, titre et boutons
        JPanel menu = new JPanel(new GridBagLayout());
        menu.setOpaque(false);

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        URL urlCouverture = getClass().getResource("/images/couverture_livre.png");
        if (urlCouverture != null) {
            ImageIcon originalIcon = new ImageIcon(urlCouverture);
            Image originalImage = originalIcon.getImage();
            int newHeight = 400;
            int newWidth = (int) (((double) newHeight / originalIcon.getIconHeight()) * originalIcon.getIconWidth());
            Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            JLabel etiquetteCouverture = new JLabel(new ImageIcon(resizedImage));
            etiquetteCouverture.setAlignmentX(Component.CENTER_ALIGNMENT);
            box.add(etiquetteCouverture);
            box.add(Box.createVerticalStrut(20));
        }

        JLabel title = new JLabel("Le Pirate des Sept Mers");
        title.setFont(pirateFont.deriveFont(48f));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(title);
        box.add(Box.createVerticalStrut(60));

        // Bouton Nouvelle Partie
        JButton btnNew = themedButton("Nouvelle Partie");
        btnNew.setFont(pirateFont.deriveFont(24f));
        btnNew.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNew.addActionListener(e -> {
            playSound("sons/vague.wav");
            actionNouvellePartieGraphique();
        });
        box.add(btnNew);

        box.add(Box.createVerticalStrut(20));

        // Bouton Charger Partie
        JButton btnLoad = themedButton("Charger une Partie");
        btnLoad.setFont(pirateFont.deriveFont(24f));
        btnLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLoad.addActionListener(e -> {
            playSound("sons/vague.wav");
            actionChargerPartieGraphique();
        });
        box.add(btnLoad);

        menu.add(box);
        return menu;
    }

    private JPanel createGameScreen() {
        // Écran de jeu principal, status à gauche et contenu au centre
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setOpaque(false);

        // Panneau d'état du joueur
        pnlStatus = new JPanel(new GridBagLayout());
        pnlStatus.setOpaque(false);
        pnlStatus.setBorder(new EmptyBorder(30, 20, 20, 20));
        pnlStatus.setPreferredSize(new Dimension(340, 0));
        gamePanel.add(pnlStatus, BorderLayout.WEST);

        // Panneau parchemin pour le texte
        JPanel pnlParchemin = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgScroll != null) g.drawImage(bgScroll, 0, 0, getWidth(), getHeight(), this);
            }
        };
        pnlParchemin.setOpaque(false);
        gamePanel.add(pnlParchemin, BorderLayout.CENTER);

        // Conteneur pour titre, texte et choix
        JPanel pnlContenu = new JPanel(new BorderLayout(10, 20));
        pnlContenu.setOpaque(false);

        lblTitre = new JLabel("Chapitre");
        lblTitre.setFont(pirateFont.deriveFont(36f));
        lblTitre.setForeground(new Color(87, 65, 43));
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitre.setBorder(new EmptyBorder(0,0,10,0));
        pnlContenu.add(lblTitre, BorderLayout.NORTH);

        txtContent = new JTextPane();
        txtContent.setContentType("text/html");
        txtContent.setOpaque(false);
        txtContent.setEditable(false);
        String css = String.format(
                "body{font-family:'%s';font-size:%dpt;color:#33281E;text-align:justify;} p{margin:0 0 1em;}",
                textFont.getFamily(), textFont.getSize());
        ((HTMLDocument)txtContent.getDocument()).getStyleSheet().addRule(css);

        JScrollPane scroll = new JScrollPane(txtContent);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);

        JScrollBar verticalScrollBar = scroll.getVerticalScrollBar();
        verticalScrollBar.setUI(new PirateScrollBarUI());
        verticalScrollBar.setPreferredSize(new Dimension(12, 0));
        verticalScrollBar.setOpaque(false);

        pnlContenu.add(scroll, BorderLayout.CENTER);

        pnlChoices = new JPanel();
        pnlChoices.setOpaque(false);
        pnlChoices.setLayout(new BoxLayout(pnlChoices, BoxLayout.Y_AXIS));
        pnlContenu.add(pnlChoices, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(80, 100, 100, 100);
        pnlParchemin.add(pnlContenu, gbc);

        return gamePanel;
    }

    private void styleJOptionPane() {
        // Adaptation des couleurs des boîtes de dialogue pour coller au thème
        Color woodColor = new Color(101, 67, 33);
        Color textColor = new Color(240, 230, 210);
        UIManager.put("Panel.background", new ColorUIResource(woodColor));
        UIManager.put("OptionPane.background", new ColorUIResource(woodColor));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(textColor));
        UIManager.put("OptionPane.messageFont", textFont.deriveFont(16f));
        UIManager.put("Label.foreground", new ColorUIResource(textColor));
        UIManager.put("Label.font", textFont.deriveFont(16f));
    }

    private void actionNouvellePartieGraphique() {
        // Dialogue pour saisir le nom du héros puis démarrer la partie
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setOpaque(false);
        JLabel label = infoLabel("Entrez le nom de votre héros :", textFont, Color.WHITE);
        contentPanel.add(label, BorderLayout.NORTH);

        JTextField textField = new JTextField(20);
        textField.setFont(textFont.deriveFont(16f));
        textField.setBackground(new Color(240, 230, 210));
        textField.setForeground(Color.BLACK);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 25, 0)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        contentPanel.add(textField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        JButton okButton = themedButton("Valider");
        JButton cancelButton = themedButton("Annuler");
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        final ThemedDialog dialog = new ThemedDialog(this, "Nouvelle Partie", contentPanel);
        AtomicReference<String> nomHerosResult = new AtomicReference<>();

        okButton.addActionListener(e -> {
            playSound("sons/bang.wav");
            // Petit effet de shake pour l’interaction
            Timer shakeDelayTimer = new Timer(500, evt -> shake());
            shakeDelayTimer.setRepeats(false);
            shakeDelayTimer.start();

            String input = textField.getText();
            if (input != null && !input.trim().isEmpty()) {
                nomHerosResult.set(input.trim());
            } else {
                nomHerosResult.set("L'Aventurier Anonyme");
            }
            dialog.dispose();
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
        String nomHeros = nomHerosResult.get();

        if (nomHeros != null) {
            // Création du personnage et ajout d’équipements de base
            Personnage joueur = new Personnage(nomHeros);
            joueur.getCompetences().add("Escrime");
            joueur.getCompetences().add("Navigation");
            joueur.ajouterObjet(new Objet("Épée", "arme"));
            this.controller = new GameController(scenario, joueur);
            if (getJMenuBar() != null) {
                getJMenuBar().setVisible(true);
            }
            mainLayout.show(cardContainer, "GAME");
            updateGameView();
        }
    }

    private void actionChargerPartieGraphique() {
        // Dialogue pour choisir et charger une partie existante
        List<String> sauvegardes = SaveManager.listerSauvegardes();
        if (sauvegardes.isEmpty()) {
            styleJOptionPane();
            JOptionPane.showMessageDialog(this, "Aucune sauvegarde trouvée.", "Charger", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setOpaque(false);
        JLabel label = infoLabel("Choisissez une partie à charger :", textFont, Color.WHITE);
        contentPanel.add(label, BorderLayout.NORTH);
        JComboBox<String> comboBox = new JComboBox<>(sauvegardes.toArray(new String[0]));
        comboBox.setFont(textFont.deriveFont(16f));
        contentPanel.add(comboBox, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        JButton okButton = themedButton("Charger");
        JButton cancelButton = themedButton("Annuler");
        buttonPanel.add(okButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        final ThemedDialog dialog = new ThemedDialog(this, "Charger une partie", contentPanel);
        AtomicReference<String> fichierChoisi = new AtomicReference<>();

        okButton.addActionListener(e -> {
            playSound("sons/bang.wav");
            Timer shakeDelayTimer = new Timer(500, evt -> shake());
            shakeDelayTimer.setRepeats(false);
            shakeDelayTimer.start();
            fichierChoisi.set((String) comboBox.getSelectedItem());
            dialog.dispose();
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
        String nomFichierChoisi = fichierChoisi.get();

        if (nomFichierChoisi != null) {
            try {
                GameState etatCharge = SaveManager.load(Paths.get(nomFichierChoisi));
                this.controller = new GameController(scenario, etatCharge);
                if (getJMenuBar() != null) {
                    getJMenuBar().setVisible(true);
                }
                mainLayout.show(cardContainer, "GAME");
                updateGameView();
            } catch (Exception ex) {
                styleJOptionPane();
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement de la sauvegarde.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void updateGameView() {
        // Mise à jour de la vue en fonction de l'état actuel du jeu
        if (controller == null) return;
        updateStatusPanel();

        if (controller.isGameOver()) {
            // Affichage de la fin de partie
            lblTitre.setText("Fin de l'aventure");
            txtContent.setText("<html><body><p>Votre aventure s'achève ici. Vous n'avez pas survécu aux dangers de ces mers impitoyables.</p></body></html>");
            pnlChoices.removeAll();
            JLabel fin = infoLabel("GAME OVER", pirateFont.deriveFont(32f), Color.RED);
            fin.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlChoices.add(fin);
        } else {
            // Affichage du chapitre courant et de ses choix
            Chapitre chap = controller.getChapitreCourant();
            lblTitre.setText("Chapitre " + chap.getId());
            String html = "<html><body><p>" + chap.getTexte().replace("\n", "</p><p>") + "</p></body></html>";
            txtContent.setText(html);
            SwingUtilities.invokeLater(() -> txtContent.setCaretPosition(0));

            pnlChoices.removeAll();
            List<Choix> choices = controller.getChoixDisponibles();
            if (choices.isEmpty()) {
                JLabel fin = infoLabel("FIN DE L'AVENTURE", pirateFont.deriveFont(28f), new Color(87, 65, 43));
                fin.setAlignmentX(Component.CENTER_ALIGNMENT);
                pnlChoices.add(fin);
            } else {
                choices.forEach(c -> {
                    JButton b = themedButton(c.getTexte());
                    b.setAlignmentX(Component.CENTER_ALIGNMENT);
                    b.addActionListener(e -> {
                        controller.choisir(c);
                        updateGameView();
                    });
                    pnlChoices.add(b);
                    pnlChoices.add(Box.createVerticalStrut(10));
                });
            }
        }
        revalidate();
        repaint();
    }

    private void updateStatusPanel() {
        // Affiche le nom, la vie, l'or, les compétences et l'inventaire du joueur
        if (controller == null) return;
        pnlStatus.removeAll();
        Personnage p = controller.getJoueur();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        pnlStatus.add(infoLabel(p.getNom(), pirateFont.deriveFont(38f), Color.WHITE), gbc);
        gbc.insets = new Insets(30, 0, 0, 0);
        pnlStatus.add(iconValue("icone_coeur.png", "Vie: ", p.getPointsDeVie() + "/" + p.getPointsDeVieMax()), gbc);
        gbc.insets = new Insets(15, 0, 0, 0);
        pnlStatus.add(iconValue("icone_or.png", "Or: ", String.valueOf(p.getDoublonsOr())), gbc);
        gbc.insets = new Insets(50, 0, 0, 0);
        pnlStatus.add(infoSection("Compétences"), gbc);
        gbc.insets = new Insets(10, 0, 0, 0);
        p.getCompetences().forEach(c -> pnlStatus.add(infoLabel("- " + c, textFont, Color.LIGHT_GRAY), gbc));
        gbc.insets = new Insets(50, 0, 0, 0);
        pnlStatus.add(infoSection("Inventaire"), gbc);
        gbc.insets = new Insets(10, 0, 0, 0);
        List<Objet> inv = controller.getJoueur().getInventaire();
        if (inv.isEmpty()) {
            pnlStatus.add(infoLabel("Vide", textFont, Color.LIGHT_GRAY), gbc);
        } else {
            inv.forEach(o -> pnlStatus.add(infoLabel("- " + o.getNom(), textFont, Color.LIGHT_GRAY), gbc));
        }
        gbc.weighty = 1.0;
        pnlStatus.add(new JLabel(), gbc);
    }

    private void shake() {
        // Effet de tremblement de la fenêtre pour plus de dynamisme
        final Point originalLocation = getLocation();
        final int duration = 150;
        final int interval = 5;
        final int magnitude = 4;

        new Timer(interval, new ActionListener() {
            private long startTime = -1;
            private final Random random = new Random();

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();
                }
                long elapsed = System.currentTimeMillis() - startTime;

                if (elapsed >= duration) {
                    setLocation(originalLocation);
                    ((Timer) e.getSource()).stop();
                } else {
                    int x = random.nextInt(magnitude * 2) - magnitude;
                    int y = random.nextInt(magnitude * 2) - magnitude;
                    setLocation(originalLocation.x + x, originalLocation.y + y);
                }
            }
        }).start();
    }

    private void playSound(String soundFileName) {
        // Lecture d'un fichier son pour les effets (vagues, clic, etc.)
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/" + soundFileName);
            if (audioSrc == null) {
                System.err.println("Fichier son introuvable: " + soundFileName);
                return;
            }
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Erreur lors de la lecture du son: " + e.getMessage());
        }
    }

    private JLabel infoLabel(String text, Font font, Color color) {
        // Création d'un JLabel avec style uniforme
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }

    private JLabel infoSection(String text) {
        // Titre de section stylé pour les panneaux de statut
        return infoLabel(text, pirateFont.deriveFont(28f), new Color(255, 200, 120));
    }

    private JPanel iconValue(String iconName, String label, String value) {
        // Ligne avec icône et texte pour afficher vie, or, etc.
        JPanel p = new JPanel(new BorderLayout(15, 0));
        p.setOpaque(false);
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream("/images/" + iconName));
            JLabel ic = new JLabel(new ImageIcon(img.getScaledInstance(32,32,Image.SCALE_SMOOTH)));
            p.add(ic, BorderLayout.WEST);
        } catch (Exception ignored) {}
        JLabel txtLabel = new JLabel(label + value);
        txtLabel.setFont(textFont.deriveFont(Font.BOLD, 20f));
        txtLabel.setForeground(Color.WHITE);
        p.add(txtLabel, BorderLayout.CENTER);
        return p;
    }

    private JButton themedButton(String text) {
        // Bouton décoré aux couleurs du thème pirate
        JButton btn = new JButton("<html><center>" + text + "</center></html>");
        btn.setFont(textFont.deriveFont(Font.BOLD, 16f));
        btn.setBackground(new Color(80, 40, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 105, 30), 2),
                new EmptyBorder(10, 25, 10, 25)
        ));
        btn.setMaximumSize(new Dimension(450, 60));
        return btn;
    }

    private static class PirateScrollBarUI extends BasicScrollBarUI {
        // Scrollbar minimaliste et ronde pour rester dans l'ambiance
        private final Dimension ZERO_DIMENSION = new Dimension(0, 0);
        private final Color THUMB_COLOR = new Color(80, 40, 0, 180);

        @Override protected void configureScrollBarColors() {}
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {}
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(THUMB_COLOR);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 10, 10);
            g2.dispose();
        }
        @Override protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
        @Override protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(ZERO_DIMENSION);
            button.setMinimumSize(ZERO_DIMENSION);
            button.setMaximumSize(ZERO_DIMENSION);
            return button;
        }
    }

    private static class ThemedDialog extends JDialog {
        // Boîte de dialogue custom détachable et stylée
        private Point initialClick;

        public ThemedDialog(Frame parent, String title, JPanel contentPanel) {
            super(parent, true);
            setUndecorated(true);

            JPanel rootPanel = new JPanel(new BorderLayout());
            rootPanel.setBackground(new Color(101, 67, 33));
            rootPanel.setBorder(BorderFactory.createLineBorder(new Color(188, 150, 100), 2));

            JPanel titleBar = new JPanel(new BorderLayout());
            titleBar.setBackground(new Color(60, 30, 5));
            titleBar.setBorder(new EmptyBorder(5, 10, 5, 5));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(parent.getFont().deriveFont(Font.BOLD, 14f));
            titleBar.add(titleLabel, BorderLayout.CENTER);

            JButton closeButton = new JButton("X");
            closeButton.setForeground(Color.WHITE);
            closeButton.setBackground(new Color(60, 30, 5));
            closeButton.setFocusPainted(false);
            closeButton.setBorder(new EmptyBorder(2, 5, 2, 5));
            closeButton.addActionListener(e -> dispose());
            titleBar.add(closeButton, BorderLayout.EAST);

            MouseAdapter adapter = new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    initialClick = e.getPoint();
                    getComponentAt(initialClick);
                }
                public void mouseDragged(MouseEvent e) {
                    int thisX = getLocation().x;
                    int thisY = getLocation().y;
                    int xMoved = e.getX() - initialClick.x;
                    int yMoved = e.getY() - initialClick.y;
                    setLocation(thisX + xMoved, thisY + yMoved);
                }
            };
            titleBar.addMouseListener(adapter);
            titleBar.addMouseMotionListener(adapter);

            contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

            rootPanel.add(titleBar, BorderLayout.NORTH);
            rootPanel.add(contentPanel, BorderLayout.CENTER);

            setContentPane(rootPanel);
            pack();
            setLocationRelativeTo(parent);
        }
    }

    private static class Particle {
        // Représente une particule animée (flamme, étincelle…)
        double x, y, vx, vy;
        int life, maxLife, size;

        Particle(double x, double y, double vx, double vy, int life, int size) {
            this.x = x; this.y = y; this.vx = vx; this.vy = vy;
            this.life = life; this.maxLife = life;
            this.size = size;
        }
    }

    private class ParticlePane extends JPanel {
        // Panneau qui gère et dessine des particules en continu
        private final List<Particle> particles = new ArrayList<>();
        private final Random random = new Random();
        private final Timer timer;

        public ParticlePane() {
            setOpaque(false);
            timer = new Timer(33, e -> updateAndRepaint());
        }

        @Override
        public void addNotify() {
            super.addNotify();
            if (!timer.isRunning()) {
                timer.start();
            }
        }

        @Override
        public void removeNotify() {
            if (timer.isRunning()) {
                timer.stop();
            }
            super.removeNotify();
        }

        private void updateAndRepaint() {
            // Crée de nouvelles particules et met à jour celles existantes
            if (getWidth() == 0) return;

            if (random.nextInt(100) > 50) {
                int count = random.nextInt(4) + 2;
                for (int i = 0; i < count; i++) {
                    int x = random.nextInt(getWidth());
                    double vx = (random.nextDouble() - 0.5) * 1.5;
                    double vy = -random.nextDouble() * 2.5 - 1.0;
                    int life = random.nextInt(150) + 120;
                    int size = random.nextInt(4) + 7;
                    particles.add(new Particle(x, getHeight() + 10, vx, vy, life, size));
                }
            }

            particles.removeIf(p -> {
                p.x += p.vx;
                p.y += p.vy;
                p.life--;
                return p.life <= 0;
            });
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dessine chaque particule avec un dégradé radial
            for (Particle p : particles) {
                float alpha = Math.max(0, (float) p.life / p.maxLife);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.9f));

                Point2D center = new Point2D.Float((float)p.x + p.size / 2.0f, (float)p.y + p.size / 2.0f);
                float radius = p.size / 2.0f;
                float[] dist = {0.0f, 0.8f, 1.0f};
                Color[] colors = {
                        new Color(255, 255, 220, 220),
                        new Color(255, 150, 0, 100),
                        new Color(200, 50, 0, 0)
                };

                RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colors);
                g2d.setPaint(rgp);

                g2d.fillRoundRect((int)p.x, (int)p.y, p.size, p.size, p.size / 2, p.size / 2);
            }
            g2d.dispose();
        }
    }
}
