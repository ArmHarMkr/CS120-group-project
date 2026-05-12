package aua.ui;

import aua.core.GameManager;
import aua.core.Item;
import aua.core.Product;
import aua.core.exceptions.GameActionException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class ShopWindow extends JDialog {

    private GameManager gameManager;
    private JPanel shopItemsPanel;
    private JPanel inventoryPanel;
    private JLabel moneyLabel;
    private JLabel messageLabel;
    private Image backgroundImage;

    private static final Color TEXT_COLOR   = new Color(255, 235, 180);
    private static final Color GOLD_COLOR   = new Color(255, 200, 50);
    private static final Color BUTTON_BUY   = new Color(80, 140, 60, 220);
    private static final Color BUTTON_SELL  = new Color(180, 80, 40, 220);
    private static final Color BUTTON_EXIT  = new Color(100, 50, 20, 220);
    private static final Color CARD_BG      = new Color(80, 45, 15, 210);
    private static final Color SECTION_BG   = new Color(60, 35, 10, 180);
    private static final Color BORDER_COLOR = new Color(160, 110, 60);

    public ShopWindow(JFrame parent, GameManager gameManager) {
        super(parent, "Grandma's Shop", true);
        this.gameManager = gameManager;
        loadBackground();
        setSize(900, 620);
        setLocationRelativeTo(parent);
        setResizable(false);


        JPanel root = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(101, 67, 33));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        root.setOpaque(true);


        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.setBorder(new EmptyBorder(15, 20, 5, 20));

        JLabel titleLabel = new JLabel("Grandma's Garden Shop", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(GOLD_COLOR);

        moneyLabel = new JLabel("$ " + gameManager.getPlayerMoney(), SwingConstants.RIGHT);
        moneyLabel.setFont(new Font("Serif", Font.BOLD, 20));
        moneyLabel.setForeground(GOLD_COLOR);

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.add(titleLabel, BorderLayout.CENTER);
        titleBar.add(moneyLabel, BorderLayout.EAST);

        messageLabel = new JLabel("Welcome! Buy seeds or sell your harvest.", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Serif", Font.ITALIC, 14));
        messageLabel.setForeground(new Color(255, 220, 150));
        messageLabel.setBorder(new EmptyBorder(4, 0, 0, 0));

        topSection.add(titleBar, BorderLayout.NORTH);
        topSection.add(messageLabel, BorderLayout.SOUTH);


        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel shopSection = createSection("Shop Stock");
        shopItemsPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        shopItemsPanel.setOpaque(false);
        shopSection.add(transparentScroll(shopItemsPanel), BorderLayout.CENTER);

        JPanel inventorySection = createSection("Your Inventory");
        inventoryPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        inventoryPanel.setOpaque(false);
        inventorySection.add(transparentScroll(inventoryPanel), BorderLayout.CENTER);

        centerPanel.add(shopSection);
        centerPanel.add(inventorySection);


        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(0, 0, 10, 0));
        JButton exitButton = createStyledButton("Leave Shop", BUTTON_EXIT);
        exitButton.setPreferredSize(new Dimension(150, 35));
        exitButton.addActionListener(e -> {
            gameManager.exitShop();
            dispose();
        });
        bottomBar.add(exitButton);

        root.add(topSection, BorderLayout.NORTH);
        root.add(centerPanel, BorderLayout.CENTER);
        root.add(bottomBar, BorderLayout.SOUTH);

        setContentPane(root);
        refreshItems();
    }

    private void refreshItems() {
        moneyLabel.setText("$ " + gameManager.getPlayerMoney());


        shopItemsPanel.removeAll();
        Item[] shopItems = gameManager.getShopItems();
        for (int i = 0; i < shopItems.length; i++) {
            final int index = i;
            final String name = shopItems[i].getName();
            final int price = shopItems[i].getBuyPrice();

            JPanel card = createItemCard(name, "Buy: $" + price, BUTTON_BUY, "BUY", () -> {
                try {
                    gameManager.buyItem(index);
                    showMessage("You bought " + name + " for $" + price + "!");
                } catch (GameActionException ex) {
                    showMessage(ex.getMessage());
                }
                SwingUtilities.invokeLater(this::refreshItems);
            });
            shopItemsPanel.add(card);
        }


        inventoryPanel.removeAll();
        Item[] inventoryItems = gameManager.getInventoryItems();
        for (int i = 0; i < inventoryItems.length; i++) {
            final int index = i;
            final String name = inventoryItems[i].getName();
            boolean isProduct = inventoryItems[i] instanceof Product;

            if (isProduct) {
                final int sellPrice = ((Product) inventoryItems[i]).getSellPrice();
                JPanel card = createItemCard(name, "Sell: $" + sellPrice, BUTTON_SELL, "SELL", () -> {
                    try {
                        gameManager.sellItem(index);
                        showMessage("You sold " + name + " for $" + sellPrice + "!");
                    } catch (GameActionException ex) {
                        showMessage(ex.getMessage());
                    }
                    SwingUtilities.invokeLater(this::refreshItems);
                });
                inventoryPanel.add(card);
            } else {
                // Seed — no sell button
                JPanel card = createItemCard(name, "Seed", new Color(80, 80, 80, 180), null, null);
                inventoryPanel.add(card);
            }
        }

        shopItemsPanel.revalidate();
        shopItemsPanel.repaint();
        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }

    private void showMessage(String msg) {
        messageLabel.setText(msg);
    }

    private JScrollPane transparentScroll(JPanel panel) {
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JPanel createSection(String title) {
        JPanel section = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SECTION_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        section.setOpaque(false);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                new EmptyBorder(8, 8, 8, 8)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 16));
        titleLabel.setForeground(GOLD_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        section.add(titleLabel, BorderLayout.NORTH);
        return section;
    }

    private JPanel createItemCard(String name, String price, Color btnColor, String btnText, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(4, 4)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                new EmptyBorder(6, 6, 6, 6)
        ));

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_COLOR);

        JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);
        priceLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        priceLabel.setForeground(GOLD_COLOR);

        JPanel labels = new JPanel(new GridLayout(2, 1));
        labels.setOpaque(false);
        labels.add(nameLabel);
        labels.add(priceLabel);
        card.add(labels, BorderLayout.CENTER);

        if (action != null && btnText != null) {
            JButton btn = createStyledButton(btnText, btnColor);
            btn.setFont(new Font("Serif", Font.BOLD, 11));
            btn.addActionListener(e -> action.run());
            card.add(btn, BorderLayout.SOUTH);
        }

        return card;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Serif", Font.BOLD, 13));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(80, 28));
        return button;
    }

    private void loadBackground() {
        URL imageUrl = getClass().getResource("/aua/images/shop_background.png");
        if (imageUrl != null) {
            backgroundImage = new ImageIcon(imageUrl).getImage();
        }
    }

    public void open() {
        setVisible(true);
    }
}