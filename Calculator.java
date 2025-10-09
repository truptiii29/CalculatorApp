import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {
    JTextField textField;
    JButton[] numberButtons = new JButton[10];
    JButton add, sub, mul, div, equ, dec, clr, del;
    JPanel panel;
    double num1 = 0, num2 = 0, result = 0;
    char operator;

    public Calculator() {
        setTitle("Calculator App");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Text field
        textField = new JTextField();
        textField.setBounds(50, 25, 300, 50);
        textField.setFont(new Font("Arial", Font.BOLD, 18));
        textField.setEditable(false);
        add(textField);

        // Function buttons
        add = new JButton("+"); sub = new JButton("-"); mul = new JButton("*"); div = new JButton("/");
        equ = new JButton("="); dec = new JButton("."); clr = new JButton("Clr"); del = new JButton("Del");

        JButton[] funcButtons = {add, sub, mul, div, equ, dec, clr, del};
        for (JButton btn : funcButtons) {
            btn.addActionListener(this);
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.setFocusable(false);
        }

        // Number buttons 0-9
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(new Font("Arial", Font.BOLD, 18));
            numberButtons[i].setFocusable(false);
        }

        // Panel for number and operator buttons
        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));

        panel.add(numberButtons[1]); panel.add(numberButtons[2]); panel.add(numberButtons[3]); panel.add(add);
        panel.add(numberButtons[4]); panel.add(numberButtons[5]); panel.add(numberButtons[6]); panel.add(sub);
        panel.add(numberButtons[7]); panel.add(numberButtons[8]); panel.add(numberButtons[9]); panel.add(mul);
        panel.add(dec); panel.add(numberButtons[0]); panel.add(equ); panel.add(div);

        add(panel);

        // Clear and Delete buttons
        clr.setBounds(50, 430, 145, 50); del.setBounds(205, 430, 145, 50);
        add(clr); add(del);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) textField.setText(textField.getText() + i);
        }

        if (e.getSource() == dec) textField.setText(textField.getText() + ".");

        if (e.getSource() == add) { num1 = Double.parseDouble(textField.getText()); operator = '+'; textField.setText(""); }
        if (e.getSource() == sub) { num1 = Double.parseDouble(textField.getText()); operator = '-'; textField.setText(""); }
        if (e.getSource() == mul) { num1 = Double.parseDouble(textField.getText()); operator = '*'; textField.setText(""); }
        if (e.getSource() == div) { num1 = Double.parseDouble(textField.getText()); operator = '/'; textField.setText(""); }

        if (e.getSource() == equ) {
            num2 = Double.parseDouble(textField.getText());
            switch (operator) {
                case '+': result = num1 + num2; break;
                case '-': result = num1 - num2; break;
                case '*': result = num1 * num2; break;
                case '/': result = num1 / num2; break;
            }
            textField.setText(String.valueOf(result));
            num1 = result;
        }

        if (e.getSource() == clr) textField.setText("");
        if (e.getSource() == del) {
            String s = textField.getText();
            if (s.length() > 0) textField.setText(s.substring(0, s.length() - 1));
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
