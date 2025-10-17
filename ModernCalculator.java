import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ModernCalculator extends JFrame {
    private JTextField display;
    private JLabel previousDisplay;
    private String currentValue = "0";
    private String previousValue = "";
    private String operation = "";
    private boolean resetDisplay = false;

    // Modern color scheme
    private static final Color BG_COLOR = new Color(15, 23, 42);           // Dark slate
    private static final Color CARD_BG = new Color(30, 41, 59);            // Slate-700
    private static final Color DISPLAY_BG = new Color(15, 23, 42);         // Darker
    private static final Color NUMBER_BTN = new Color(51, 65, 85);         // Slate-600
    private static final Color NUMBER_HOVER = new Color(71, 85, 105);      // Slate-500
    private static final Color OPERATOR_BTN = new Color(139, 92, 246);     // Purple
    private static final Color OPERATOR_HOVER = new Color(124, 58, 237);   // Purple-600
    private static final Color CLEAR_BTN = new Color(239, 68, 68);         // Red
    private static final Color CLEAR_HOVER = new Color(220, 38, 38);       // Red-600
    private static final Color EQUALS_BTN = new Color(168, 85, 247);       // Purple gradient
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color TEXT_SECONDARY = new Color(203, 213, 225);  // Slate-300

    public ModernCalculator() {
        setTitle("Modern Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 15));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Display panel
        JPanel displayPanel = createDisplayPanel();
        mainPanel.add(displayPanel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = createButtonsPanel();
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        
        // Set window background
        getContentPane().setBackground(BG_COLOR);
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 5));
        panel.setBackground(DISPLAY_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Previous operation display
        previousDisplay = new JLabel(" ");
        previousDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        previousDisplay.setForeground(TEXT_SECONDARY);
        previousDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(previousDisplay, BorderLayout.NORTH);

        // Main display
        display = new JTextField("0");
        display.setFont(new Font("Segoe UI Light", Font.PLAIN, 42));
        display.setForeground(TEXT_COLOR);
        display.setBackground(DISPLAY_BG);
        display.setBorder(null);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setFocusable(false);
        panel.add(display, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 10, 10));
        panel.setBackground(BG_COLOR);

        // Button definitions: [text, gridwidth, color, isOperator]
        Object[][] buttons = {
            {"C", 1, CLEAR_BTN, false},
            {"⌫", 1, OPERATOR_BTN, false},
            {"÷", 1, OPERATOR_BTN, true},
            {"×", 1, OPERATOR_BTN, true},
            
            {"7", 1, NUMBER_BTN, false},
            {"8", 1, NUMBER_BTN, false},
            {"9", 1, NUMBER_BTN, false},
            {"−", 1, OPERATOR_BTN, true},
            
            {"4", 1, NUMBER_BTN, false},
            {"5", 1, NUMBER_BTN, false},
            {"6", 1, NUMBER_BTN, false},
            {"+", 1, OPERATOR_BTN, true},
            
            {"1", 1, NUMBER_BTN, false},
            {"2", 1, NUMBER_BTN, false},
            {"3", 1, NUMBER_BTN, false},
            {"=", 1, EQUALS_BTN, false},
            
            {"0", 2, NUMBER_BTN, false},
            {".", 1, NUMBER_BTN, false},
            {null, 1, null, false} // Placeholder for equals button continuation
        };

        for (int i = 0; i < buttons.length; i++) {
            Object[] btnData = buttons[i];
            String text = (String) btnData[0];
            
            if (text == null) continue; // Skip placeholder
            
            Color color = (Color) btnData[2];
            
            ModernButton btn = new ModernButton(text, color);
            btn.addActionListener(e -> handleButtonClick(text));
            
            panel.add(btn);
        }

        return panel;
    }

    private void handleButtonClick(String text) {
        switch (text) {
            case "C":
                clear();
                break;
            case "⌫":
                backspace();
                break;
            case "=":
                equals();
                break;
            case "+":
            case "−":
            case "×":
            case "÷":
                setOperation(text);
                break;
            case ".":
                addDecimal();
                break;
            default:
                addNumber(text);
                break;
        }
    }

    private void addNumber(String num) {
        if (resetDisplay) {
            currentValue = num;
            resetDisplay = false;
        } else {
            currentValue = currentValue.equals("0") ? num : currentValue + num;
        }
        display.setText(currentValue);
    }

    private void addDecimal() {
        if (resetDisplay) {
            currentValue = "0.";
            resetDisplay = false;
        } else if (!currentValue.contains(".")) {
            currentValue += ".";
        }
        display.setText(currentValue);
    }

    private void setOperation(String op) {
        if (!previousValue.isEmpty() && !operation.isEmpty() && !resetDisplay) {
            calculate();
        }
        
        previousValue = currentValue;
        operation = op;
        previousDisplay.setText(previousValue + " " + operation);
        resetDisplay = true;
    }

    private void equals() {
        if (!operation.isEmpty() && !previousValue.isEmpty()) {
            calculate();
            operation = "";
            previousDisplay.setText(" ");
            resetDisplay = true;
        }
    }

    private void calculate() {
        try {
            double prev = Double.parseDouble(previousValue);
            double curr = Double.parseDouble(currentValue);
            double result = 0;

            switch (operation) {
                case "+": result = prev + curr; break;
                case "−": result = prev - curr; break;
                case "×": result = prev * curr; break;
                case "÷": result = prev / curr; break;
            }

            // Format result
            if (result == (long) result) {
                currentValue = String.valueOf((long) result);
            } else {
                currentValue = String.valueOf(result);
            }
            
            display.setText(currentValue);
            previousValue = currentValue;
        } catch (Exception e) {
            display.setText("Error");
            currentValue = "0";
        }
    }

    private void clear() {
        currentValue = "0";
        previousValue = "";
        operation = "";
        display.setText(currentValue);
        previousDisplay.setText(" ");
        resetDisplay = false;
    }

    private void backspace() {
        if (currentValue.length() > 1) {
            currentValue = currentValue.substring(0, currentValue.length() - 1);
        } else {
            currentValue = "0";
        }
        display.setText(currentValue);
    }

    // Custom button class with modern styling
    class ModernButton extends JButton {
        private Color baseColor;
        private Color hoverColor;
        private boolean isHovered = false;

        public ModernButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            
            // Calculate hover color (slightly lighter)
            this.hoverColor = color.equals(NUMBER_BTN) ? NUMBER_HOVER :
                             color.equals(OPERATOR_BTN) ? OPERATOR_HOVER :
                             color.equals(CLEAR_BTN) ? CLEAR_HOVER :
                             new Color(147, 51, 234); // Equals hover

            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setForeground(TEXT_COLOR);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(80, 60));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw rounded rectangle background
            g2.setColor(isHovered ? hoverColor : baseColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            // Draw text
            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            ModernCalculator calculator = new ModernCalculator();
            calculator.setVisible(true);
        });
    }
}