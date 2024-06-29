package flappymappydeluxe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The ShopPanel class represents the panel where players can buy and manage different game skins.
 */
public class ShopPanel extends JPanel {

    private CardLayout cardLayout; // CardLayout for switching panels
    private JPanel mainPanel; // Main panel to hold different screens
    private Map<String, Boolean> ownedItems; // Map to track owned skins
    private String activePipeSkin; // Currently active pipe skin
    private String activeBackgroundSkin; // Currently active background skin
    private static String activeBirdSkinIdle; // Currently active bird skin when idle
    private String activeBirdSkinFlap; // Currently active bird skin when flapping

    // Constants for available skins
    private static final String[] PIPE_SKINS = {"Posh Purple", "Business Black", "Royal Blue"};
    private static final String[] BACKGROUND_SKINS = {"Cloudy Blues", "FS School", "Metropolis"};
    private static final String[] BIRD_SKINS = {"Purple", "Blue", "The Original"};
    // Paths to skin images
    private static final String[] PIPE_SKIN_IMAGES = {"NotFlappyBird-main/ShopSkins/purplePipe.png", "NotFlappyBird-main/ShopSkins/blackPipe.png", "NotFlappyBird-main/ShopSkins/bluePipe.png"};
    private static final String[] BACKGROUND_SKIN_IMAGES = {"NotFlappyBird-main/ShopSkins/blueCloudsBackground.png", "NotFlappyBird-main/ShopSkins/FS_pixelBackground.png", "NotFlappyBird-main/ShopSkins/blueBackgroundCity.png"};
    private static final String[] BIRD_SKIN_IMAGES = {"NotFlappyBird-main/ShopSkins/purpleBirdIdle.png", "NotFlappyBird-main/ShopSkins/blueBirdIdle.png", "NotFlappyBird-main/ShopSkins/yellowBirdIdle.png"};
    private static final int SKIN_PRICE = 50; // price for skins

    /**
     * Constructor for ShopPanel.
     * @param cardLayout The CardLayout manager from the main frame
     * @param mainPanel The main panel in which this shop panel resides
     */
    public ShopPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.ownedItems = new HashMap<>();
        initializeOwnedItems(); // Initialize default owned skins
        loadSkins(); // Load saved skins from file

        setLayout(new BorderLayout());

        // Main shop panel with background
        JPanel shopPanel = createBackgroundPanel();

        // Create buttons for navigating different skin categories and collection
        JButton buyPipeSkinButton = createButton("Pipe Skins");
        JButton buyBackgroundSkinButton = createButton("Background Skins");
        JButton buyBirdSkinButton = createButton("Bird Skins");
        JButton collectionButton = createButton("Collection");
        JButton backButton = createButton("Back to Menu");

        // GridBagConstraint for layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add buttons to shopPanel
        shopPanel.add(collectionButton, gbc);
        shopPanel.add(buyPipeSkinButton, gbc);
        shopPanel.add(buyBackgroundSkinButton, gbc);
        shopPanel.add(buyBirdSkinButton, gbc);
        shopPanel.add(backButton, gbc);

        add(shopPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        buyPipeSkinButton.addActionListener(e -> showSkinShop("Pipe Skins", PIPE_SKINS, PIPE_SKIN_IMAGES));
        buyBackgroundSkinButton.addActionListener(e -> showSkinShop("Background Skins", BACKGROUND_SKINS, BACKGROUND_SKIN_IMAGES));
        buyBirdSkinButton.addActionListener(e -> showSkinShop("Bird Skins", BIRD_SKINS, BIRD_SKIN_IMAGES));
        collectionButton.addActionListener(e -> showCollection());
        backButton.addActionListener(e -> {
            MenuPanel.switchMusic("NotFlappyBird-main/Music/1-01. Main Theme (Title Screen).wav"); // Switch music back to main theme
            cardLayout.show(mainPanel, "menu"); // Switch to main menu panel
        });
    }

    /**
     * Creates a JButton with customized properties.
     * @param text The text label of the button
     * @return The created JButton
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
     * Creates an image button with customized properties.
     * @param text The text label of the button
     * @param imagePath The path to the image icon
     * @return The created JButton with image icon
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
            button.setIcon(icon); // Set button icon
        }
        return button;
    }

    /**
     * Creates a JPanel with a background image.
     * @return The created JPanel with background image
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
     * Shows the skin shop panel for a specific skin category.
     * @param skinType The type of skins (Pipe Skins, Background Skins, Bird Skins)
     * @param skins Array of skin names
     * @param skinImages Array of skin image paths
     */
    private void showSkinShop(String skinType, String[] skins, String[] skinImages) {
        JPanel skinShopPanel = createBackgroundPanel();
        skinShopPanel.setLayout(new GridLayout(skins.length + 1, 1, 10, 10));

        // Iterate through each skin and create buttons for non-owned skins
        for (int i = 0; i < skins.length; i++) {
            final int index = i; // Final variable for ActionListener
            String skin = skins[index];
            if (!ownedItems.containsKey(skin)) {
                JButton skinButton = createImageButton(skins[i] + " - " + SKIN_PRICE + " coins", skinImages[i]);
                skinButton.addActionListener(e -> handleSkinPurchase(skins[index], skinType)); // ActionListener for purchasing skin
                skinShopPanel.add(skinButton);
            }
        }

        // Button to go back to main shop panel
        JButton backButton = createButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "shop"));
        skinShopPanel.add(backButton);

        mainPanel.add(skinShopPanel, "skinShop");
        cardLayout.show(mainPanel, "skinShop"); // Show skin shop panel
    }

    /**
     * Handles the purchase of a skin.
     * @param skin The skin name to purchase
     * @param skinType The type of skin (Pipe Skins, Background Skins, Bird Skins)
     */
    private void handleSkinPurchase(String skin, String skinType) {
        if (CoinImage.TotalCoins >= SKIN_PRICE) { // Check if player has enough coins
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to buy " + skin + "?", "Confirm Purchase", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                CoinImage.TotalCoins -= SKIN_PRICE; // Deduct coins
                ownedItems.put(skin, true); // Mark skin as owned
                JOptionPane.showMessageDialog(this, "You have purchased " + skin + "!"); // Confirmation message
                setActiveSkin(skin, skinType); // Set purchased skin as active
                saveSkins(); // Save skins to file
            }
        } else {
            JOptionPane.showMessageDialog(this, "You don't have enough coins to buy " + skin + "!");
        }
    }

    /**
     * Shows the collection of owned skins.
     */
    private void showCollection() {
        JPanel collectionPanel= createBackgroundPanel();
        
        collectionPanel.setLayout(new BoxLayout(collectionPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical alignment

        JScrollPane scrollPane = new JScrollPane(collectionPanel); // Scroll pane for collection
        scrollPane.setPreferredSize(new Dimension(500, 400)); // Adjust scroll pane size
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Increase scroll speed

        // Iterate through owned items and add buttons for each owned skin
        for (Map.Entry<String, Boolean> entry : ownedItems.entrySet()) {
            if (entry.getValue()) {
                String imagePath = getImagePath(entry.getKey());
                JButton itemButton = createLargeImageButton("Use " + entry.getKey(), imagePath);
                itemButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center alignment for buttons
                itemButton.addActionListener(e -> setActiveSkin(entry.getKey(), determineSkinType(entry.getKey())));
                collectionPanel.add(itemButton);
            }
        }

        // Add default skins to collection if not owned
        addDefaultSkins(collectionPanel);

        JButton backButton = createLargeButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "shop"));
        collectionPanel.add(backButton);

        mainPanel.add(collectionPanel, "collection");
        cardLayout.show(mainPanel, "collection"); // Show collection panel
    }

    /**
     * Creates a large JButton with customized properties.
     * @param text The text label of the button
     * @return The created large JButton
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
     * Creates a large image button with customized properties.
     * @param text The text label of the button
     * @param imagePath The path to the image icon
     * @return The created large JButton with image icon
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
            button.setIcon(icon); // Set button icon
        }
        return button;
    }

    /**
     * Adds default skins to the collection panel if not already owned.
     * @param collectionPanel The panel to which default skins will be added
     */
    private void addDefaultSkins(JPanel collectionPanel) {
        // Check and add default skins if not owned
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
     * Determines the type of skin based on its name.
     * @param skin The name of the skin
     * @return The type of skin (Pipe Skins, Background Skins, Bird Skins)
     */
    private String determineSkinType(String skin) {
        // Determine skin type based on skin name
        if (skin.equals("Original Pipe") || skin.equals("Posh Purple") || skin.equals("Business Black") || skin.equals("Royal Blue")) {
            return "Pipe Skins";
        } else if (skin.equals("Original Background") || skin.equals("Cloudy Blues") || skin.equals("FS School") || skin.equals("Metropolis")) {
            return "Background Skins";
        } else if (skin.equals("Original Bird") || skin.equals("Purple") || skin.equals("Blue") || skin.equals("The Original")) {
            return "Bird Skins";
        } else {
            return "";
        }
    }

    /**
     * Sets the active skin of a given type.
     * @param skin The skin name to set as active
     * @param skinType The type of skin (Pipe Skins, Background Skins, Bird Skins)
     */
    private void setActiveSkin(String skin, String skinType) {
        // Set active skin based on skin type
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
        JOptionPane.showMessageDialog(this, "You have equipped " + skin + "!"+"\n" + "Restart the game for changes to apply.");
        saveSkins(); // Save skins after setting active skin
    }

    /**
     * Retrieves the image path for a given skin.
     * @param skin The name of the skin
     * @return The path to the image of the skin
     */
    private String getImagePath(String skin) {
        // Return image path based on skin name
        switch (skin) {
            case "Posh Purple":
                return "NotFlappyBird-main/ShopSkins/purplePipe.png";
            case "Business Black":
                return "NotFlappyBird-main/ShopSkins/blackPipe.png";
            case "Royal Blue":
                return "NotFlappyBird-main/ShopSkins/bluePipe.png";
            case "Cloudy Blues":
                return "NotFlappyBird-main/ShopSkins/blueCloudsBackground.png";
            case "Ocean Landscape":
                return "NotFlappyBird-main/ShopSkins/FS_pixelBackground.png";
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
     * Initializes the owned items map with default skins.
     */
    private void initializeOwnedItems() {
        // Initialize original skins as owned
        ownedItems.put("Original Pipe", true);
        ownedItems.put("Original Background", true);
        ownedItems.put("Original Bird", true);
    }

    /**
     * Retrieves the currently active pipe skin.
     * @return The currently active pipe skin
     */
    public String getActivePipeSkin() {
        return activePipeSkin;
    }

    /**
     * Retrieves the currently active background skin.
     * @return The currently active background skin
     */
    public String getActiveBackgroundSkin() {
        return activeBackgroundSkin;
    }

    /**
     * Retrieves the currently active bird skin when idle.
     * @return The currently active bird skin when idle
     */
    public static String getActiveBirdSkinIdle() {
        return activeBirdSkinIdle;
    }

    /**
     * Saves the currently active skins and owned items to a file.
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
     * Loads saved skins and owned items from a file.
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
