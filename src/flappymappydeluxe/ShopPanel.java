package flappymappydeluxe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


// Dev note: You can change the currently owned amount of coins in the coins .txt file to test all skins!
public class ShopPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, Boolean> ownedItems;
    private String activePipeSkin;
    private String activeBackgroundSkin; 
    private static String activeBirdSkinIdle;
    private String activeBirdSkinFlap;

    private static final String[] PIPE_SKINS = {"Posh Purple", "Business Black", "Royal Blue"};
    private static final String[] BACKGROUND_SKINS = {"Cloudy Blues", "FS_School", "Metropolis"};
    private static final String[] BIRD_SKINS = {"Purple", "Blue", "The Original"};
    private static final String[] PIPE_SKIN_IMAGES = {
        "NotFlappyBird-main/ShopSkins/purplePipe.png", 
        "NotFlappyBird-main/ShopSkins/blackPipe.png", 
        "NotFlappyBird-main/ShopSkins/bluePipe.png"
    };
    private static final String[] BACKGROUND_SKIN_IMAGES = {
        "NotFlappyBird-main/ShopSkins/blueCloudsBackground.png", 
        "NotFlappyBird-main/ShopSkins/FS_School.png", 
        "NotFlappyBird-main/ShopSkins/blueBackgroundCity.png"
    };
    private static final String[] BIRD_SKIN_IMAGES = {
        "NotFlappyBird-main/ShopSkins/purpleBirdIdle.png", 
        "NotFlappyBird-main/ShopSkins/blueBirdIdle.png", 
        "NotFlappyBird-main/ShopSkins/yellowBirdIdle.png"
    };
    private static final int SKIN_PRICE = 50; // Placeholder price

    /**
     * Constructor for ShopPanel.
     * Initializes the shop panel with buttons and sets up the action listeners.
     * @param cardLayout The CardLayout used for switching between panels.
     * @param mainPanel The main JPanel that holds the different screens.
     */
    public ShopPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.ownedItems = new HashMap<>();
        initializeOwnedItems(); // Initialize default owned skins
        loadSkins(); // Load skins from file

        setLayout(new BorderLayout());

        // Main Shop Panel with background
        JPanel shopPanel = createBackgroundPanel();

        JButton buyPipeSkinButton = createButton("Pipe Skins");
        JButton buyBackgroundSkinButton = createButton("Background Skins");
        JButton buyBirdSkinButton = createButton("Bird Skins");
        JButton collectionButton = createButton("Inventory");
        JButton backButton = createButton("Back to Menu");

        GridBagConstraints gbc = new GridBagConstraints();  
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        shopPanel.add(collectionButton, gbc);
        shopPanel.add(buyPipeSkinButton, gbc);
        shopPanel.add(buyBackgroundSkinButton, gbc);
        shopPanel.add(buyBirdSkinButton, gbc);
        shopPanel.add(backButton, gbc);

        add(shopPanel, BorderLayout.CENTER);

        // Add action listeners
        buyPipeSkinButton.addActionListener(e -> showSkinShop("Pipe Skins", PIPE_SKINS, PIPE_SKIN_IMAGES));
        buyBackgroundSkinButton.addActionListener(e -> showSkinShop("Background Skins", BACKGROUND_SKINS, BACKGROUND_SKIN_IMAGES));
        buyBirdSkinButton.addActionListener(e -> showSkinShop("Bird Skins", BIRD_SKINS, BIRD_SKIN_IMAGES));
        collectionButton.addActionListener(e -> showCollection());
        backButton.addActionListener(e -> {
            MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav"); // Reinstate the main menu theme
            cardLayout.show(mainPanel, "menu");
        });
    }

    /**
     * Creates a button with the specified text.
     * @param text The text to be displayed on the button.
     * @return A JButton with the specified text and predefined styling.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));  // Adjusting button size
        button.setFont(new Font("Arial", Font.BOLD, 14));   // Adjusting font size and style
        button.setBackground(new Color(240, 228, 204)); // Light beige color
        button.setForeground(Color.BLACK); // Text color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }

    /**
     * Creates a button with an image and text.
     * @param text The text to be displayed on the button.
     * @param imagePath The path to the image to be displayed on the button.
     * @return A JButton with the specified text, image, and predefined styling.
     */
    private JButton createImageButton(String text, String imagePath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 50));  // Adjusting button size
        button.setFont(new Font("Arial", Font.BOLD, 14));  // Adjusting font size and style
        button.setBackground(new Color(240, 228, 204)); // Light Beige color
        button.setForeground(Color.BLACK); // Text color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            button.setIcon(icon);
        }
        return button;
    }

    /**
     * Creates a panel with a background image.
     * @return A JPanel with a background image.
     */
    private JPanel createBackgroundPanel() {
        return new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("NotFlappyBird-main/Images/shopBackground1.png"); // shopBackground image
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
    }

    /**
     * Displays the skin shop for a specific type of skin.
     * @param skinType The type of skin (e.g., "Pipe Skins").
     * @param skins An array of skin names.
     * @param skinImages An array of skin image paths.
     */
    private void showSkinShop(String skinType, String[] skins, String[] skinImages) {
        JPanel skinShopPanel = createBackgroundPanel();
        skinShopPanel.setLayout(new GridLayout(skins.length + 1, 1, 10, 10));

        for (int i = 0; i < skins.length; i++) {
            final int index = i; // Create a final variable to use inside the ActionListener
            String skin = skins[index];
            if (!ownedItems.containsKey(skin)) {
                JButton skinButton = createImageButton(skins[i] + " - " + SKIN_PRICE + " coins", skinImages[i]);
                skinButton.addActionListener(e -> handleSkinPurchase(skins[index], skinType));
                skinShopPanel.add(skinButton);
            }
        }

        JButton backButton = createButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "shop"));
        skinShopPanel.add(backButton);

        mainPanel.add(skinShopPanel, "skinShop");
        cardLayout.show(mainPanel, "skinShop");
    }

    /**
     * Handles the purchase of a skin.
     * @param skin The name of the skin to be purchased.
     * @param skinType The type of skin being purchased.
     */
    private void handleSkinPurchase(String skin, String skinType) {
        if (CoinImage.TotalCoins >= SKIN_PRICE) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to buy " + skin + "?", "Confirm Purchase", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                CoinImage.TotalCoins -= SKIN_PRICE;
                ownedItems.put(skin, true);
                JOptionPane.showMessageDialog(this, "You have purchased " + skin + "!");
                setActiveSkin(skin, skinType);
                saveSkins(); // Save skins after purchase
            }
        } else {
            JOptionPane.showMessageDialog(this, "You don't have enough coins to buy " + skin + "!");
        }
    }

    /**
     * Displays the player's collection of owned skins.
     */
    private void showCollection() {
        JPanel collectionPanel = createBackgroundPanel();
        collectionPanel.setLayout(new BoxLayout(collectionPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical alignment

        JScrollPane scrollPane = new JScrollPane(collectionPanel);
        scrollPane.setPreferredSize(new Dimension(500, 400)); // Adjust scroll pane size
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Increase scroll speed

        for (Map.Entry<String, Boolean> entry : ownedItems.entrySet()) {
            if (entry.getValue()) {
                String imagePath = getImagePath(entry.getKey());
                JButton itemButton = createLargeImageButton("Use " + entry.getKey(), imagePath);
                itemButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment for buttons
                itemButton.addActionListener(e -> setActiveSkin(entry.getKey(), determineSkinType(entry.getKey())));
                collectionPanel.add(itemButton);
            }
        }

        // Add the default skins to the collection if not already owned
        addDefaultSkins(collectionPanel);

        JButton backButton = createLargeButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "shop"));
        collectionPanel.add(backButton);

        mainPanel.add(collectionPanel, "Inventory");
        cardLayout.show(mainPanel, "Inventory");
    }

    /**
     * Creates a large button with the specified text.
     * @param text The text to be displayed on the button.
     * @return A JButton with the specified text and predefined styling.
     */
    private JButton createLargeButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 70));  // Adjusting button size
        button.setMaximumSize(new Dimension(300, 70));    // Ensure maximum size
        button.setFont(new Font("Arial", Font.BOLD, 20));   // Adjusting font size and style
        button.setBackground(new Color(240, 228, 204)); // Light beige color
        button.setForeground(Color.BLACK); // Text color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }

    /**
     * Creates a large button with an image and text.
     * @param text The text to be displayed on the button.
     * @param imagePath The path to the image to be displayed on the button.
     * @return A JButton with the specified text, image, and predefined styling.
     */
    private JButton createLargeImageButton(String text, String imagePath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 70));  // Adjusting button size
        button.setMaximumSize(new Dimension(300, 70));    // Ensure maximum size
        button.setFont(new Font("Arial", Font.BOLD, 20));  // Adjusting font size and style
        button.setBackground(new Color(240, 228, 204)); // Light Beige color
        button.setForeground(Color.BLACK); // Text color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            button.setIcon(icon);
        }
        return button;
    }

    /**
     * Adds default skins to the collection if they are not already owned.
     * @param collectionPanel The panel to which the default skins will be added.
     */
    private void addDefaultSkins(JPanel collectionPanel) {
        if (!ownedItems.containsKey("Original Pipe")) {
            JButton defaultPipeSkinButton = createImageButton("Use Original Pipe", "NotFlappyBird-main/Images/pipe-greendoublefinal.png");
            defaultPipeSkinButton.addActionListener(e -> setActiveSkin("Original Pipe", "Pipe Skins"));
            collectionPanel.add(defaultPipeSkinButton);
        }

        if (!ownedItems.containsKey("Original Background")) {
            JButton defaultBackgroundSkinButton = createImageButton("Use Original Background", "NotFlappyBird-main/Images/origbigtruesize.png");
            defaultBackgroundSkinButton.addActionListener(e -> setActiveSkin("Original Background", "Background Skins"));
            collectionPanel.add(defaultBackgroundSkinButton);
        }

        if (!ownedItems.containsKey("Original Bird")) {
            JButton defaultBirdSkinButton = createImageButton("Use Original Bird", "NotFlappyBird-main/Images/redbird-midflap.png");
            defaultBirdSkinButton.addActionListener(e -> setActiveSkin("Original Bird", "Bird Skins"));
            collectionPanel.add(defaultBirdSkinButton);
        }
    }

    /**
     * Determines the type of skin based on the skin name.
     * @param skin The name of the skin.
     * @return The type of skin (e.g., "Pipe Skins").
     */
    private String determineSkinType(String skin) {
        if (skin.equals("Original Pipe") || skin.equals("Posh Purple") || skin.equals("Business Black") || skin.equals("Royal Blue")) {
            return "Pipe Skins";
        } else if (skin.equals("Original Background") || skin.equals("Cloudy Blues") || skin.equals("FS_School") || skin.equals("Metropolis")) {
            return "Background Skins";
        } else if (skin.equals("Original Bird") || skin.equals("Purple") || skin.equals("Blue") || skin.equals("The Original")) {
            return "Bird Skins";
        } else {
            return "";
        }
    }

    /**
     * Sets the active skin based on the skin type.
     * @param skin The name of the skin.
     * @param skinType The type of skin.
     */
    private void setActiveSkin(String skin, String skinType) {
        switch (skinType) {
            case "Pipe Skins":
                activePipeSkin = skin;
                break;
            case "Background Skins":
                activeBackgroundSkin = skin;
                break;
            case "Bird Skins":
                activeBirdSkinIdle = skin;
                break; 
        }
        JOptionPane.showMessageDialog(this, "You have equipped " + skin + "!" + "\n" + "Restart the game for changes to apply.");
        saveSkins(); // Save skins after setting the active skin
    }

    /**
     * Retrieves the image path for a given skin.
     * @param skin The name of the skin.
     * @return The image path for the skin.
     */
    private String getImagePath(String skin) {
        switch (skin) {
            case "Posh Purple":
                return "NotFlappyBird-main/ShopSkins/purplePipe.png";
            case "Business Black":
                return "NotFlappyBird-main/ShopSkins/blackPipe.png";
            case "Royal Blue":
                return "NotFlappyBird-main/ShopSkins/bluePipe.png";
            case "Cloudy Blues":
                return "NotFlappyBird-main/ShopSkins/blueCloudsBackground.png";
            case "FS_School":
                return "NotFlappyBird-main/ShopSkins/FS_School.png";
            case "Metropolis":
                return "NotFlappyBird-main/ShopSkins/blueBackgroundCity.png";
            case "Purple":
                return "NotFlappyBird-main/ShopSkins/purpleBirdIdle.png";
            case "Blue":
                return "NotFlappyBird-main/ShopSkins/blueBirdIdle.png";
            case "The Original":
                return "NotFlappyBird-main/ShopSkins/yellowBirdIdle.png";
            case "Original Pipe":
                return "NotFlappyBird-main/Images/pipe-greendoublefinal.png";
            case "Original Background":
                return "NotFlappyBird-main/Images/origbigtruesize.png";
            case "Original Bird":
                return "NotFlappyBird-main/Images/redbird-midflap.png";
            default:
                return "";
        }
    }

    /**
     * Initializes the default owned items.
     * By default, the original skins are owned.
     */
    private void initializeOwnedItems() {
        // Initialize original skins as owned
        ownedItems.put("Original Pipe", true);
        ownedItems.put("Original Background", true);
        ownedItems.put("Original Bird", true);
    }

    /**
     * Gets the active pipe skin.
     * @return The active pipe skin.
     */
    public String getActivePipeSkin() {
        return activePipeSkin;
    }

    /**
     * Gets the active background skin.
     * @return The active background skin.
     */
    public String getActiveBackgroundSkin() {
        return activeBackgroundSkin;
    }

    /**
     * Gets the active idle bird skin.
     * @return The active idle bird skin.
     */
    public static String getActiveBirdSkinIdle() {
        return activeBirdSkinIdle;
    }

    /**
     * Saves the skins to a file.
     * This method saves the active skins and the owned items to "skins.txt".
     */
    private void saveSkins() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("skins.txt"))) {
            writer.write(activePipeSkin + "\n");
            writer.write(activeBackgroundSkin + "\n");
            writer.write(activeBirdSkinIdle + "\n");
            for (Map.Entry<String, Boolean> entry : ownedItems.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the skins from a file.
     * This method loads the active skins and the owned items from "skins.txt".
     */
    private void loadSkins() {
        try (BufferedReader reader = new BufferedReader(new FileReader("skins.txt"))) {
            activePipeSkin = reader.readLine();
            activeBackgroundSkin = reader.readLine();
            activeBirdSkinIdle = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    ownedItems.put(parts[0], Boolean.parseBoolean(parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
