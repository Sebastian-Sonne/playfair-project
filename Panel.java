import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager;

/**
 * Class to setup the panel on the frame of a graphical output, used to encrypt and decrypt messages
 * Encryption based on Playfair-Encryption - this class is the IO of the encryption class
 * 
 * @author Sebastian Sonne
 * @version v4 | 04.12.2023
 */
public class Panel extends JPanel {
    //Frame
    final private static int HEIGHT = 450;
    final private static int WIDTH = 350;

    // UI IO
    private JButton clear;
    private JButton escape; 
    private JButton encrypt;
    private JButton decrypt;

    private JTextArea inputField;
    private JLabel inputLabel;
    private JTextField passwordField;
    private JLabel passwordLabel;
    private JTextArea outputField;
    private JLabel outputLabel;

    // Logic
    private static final Set<Character> ALLOWED_CHARACTERS = new HashSet<>(Set.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' '));
    private Verschluesselung encryAlg;
    
    // Error Messages
    private static final String ERROR_INPUT_EMPTY = "Input Field empty\nPlease enter an input";
    private static final String ERROR_PASSWORD_EMPTY = "Password Field empty\nPlease enter a password";
    private static final String ERROR_INVALID_INPUT = "Invalid Input\nOnly use standard English letters, without 'j'";
    private static final String ERROR_INVALID_PASSWORD = "Invalid password\nOnly use standard English letters, without 'j' or blank";


    /**
     * constructor of Panel Class
     * initializes L&F, all Buttons, Labels, TextFields, and Encryption Algorithm Class
     */
    public Panel() {
        //set system Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException e) {
            showExceptionDialog(e);
        }
        catch (ClassNotFoundException e) {
            showExceptionDialog(e);
        }
        catch (InstantiationException e) {
            showExceptionDialog(e);
        }
        catch (IllegalAccessException e) {
            showExceptionDialog(e);
        }
    
        //set Frame Dimensions
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);

        //Buttons
        clear = new JButton("Clear");
        clear.addActionListener(e -> clearFields());
        
        escape = new JButton("Esc");
        escape.addActionListener(e -> System.exit(0));
        
        encrypt = new JButton("Verschlüsseln");
        encrypt.addActionListener(e -> processCryption(e));

        decrypt = new JButton("Entschlüsseln");
        decrypt.addActionListener(e -> processCryption(e));

        //Textfield Labels
        inputLabel = new JLabel("Eingabe:");
        passwordLabel = new JLabel("Password:");
        outputLabel = new JLabel("Ausgabe:");

        //Text Fields
        inputField = new JTextArea();
        inputField.setLineWrap(true);
        inputField.setWrapStyleWord(true);

        passwordField = new JTextField();
        passwordField.setBorder(null);
        passwordField.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    //prevents user from adding more characters to the password, if password length >= 50
                    if (passwordField.getText().length() >= 50) {
                        e.consume();
                    }
                }
            });

        outputField = new JTextArea();
        outputField.setLineWrap(true);
        outputField.setWrapStyleWord(true);

        //Logic
        encryAlg = new Verschluesselung();
    }

    /**
     * initalizes Java Paintcomponent
     * @param   g   java graphics
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * generates the Graphical UI of the Encryption Interface
     * @param   g   java graphics
     */
    private void draw(Graphics g) {
        //adds buttons with their respective locations/dimensions
        clear.setBounds(245, 5, 80, 30);
        this.add(clear);
        escape.setBounds(25, 5, 60, 30);
        this.add(escape);
        encrypt.setBounds(25, 250, 140, 30);
        this.add(encrypt);
        decrypt.setBounds(185, 250, 140, 30);
        this.add(decrypt);

        //adds labels/fields with their respective location/dimensions
        inputField.setBounds(25, 65, 300, 100);
        this.add(inputField);
        inputLabel.setBounds(25, 45, 300, 20);
        this.add(inputLabel);
        passwordField.setBounds(25, 195, 300, 30);
        this.add(passwordField);
        passwordLabel.setBounds(25, 175, 300, 20);
        this.add(passwordLabel);
        outputField.setBounds(25, 325, 300, 100);
        this.add(outputField);
        outputLabel.setBounds(25, 305, 300, 20);
        this.add(outputLabel);

        //copyright String
        g.setFont(new Font("Calibri", Font.PLAIN, 15));
        g.drawString("©Sebastian Sonne | 2023", 3, 445);
    }

    /**
     * processes the Encryption process, when either encryption or decryption button is clicked
     * @param e used to distinuis between decryption and encryption
     */
    private void processCryption(ActionEvent e) {
        String check = checkInput(inputField.getText().toUpperCase(), passwordField.getText().toUpperCase());

        switch (check) {
            case "e1":
                showMessageDialog(ERROR_INPUT_EMPTY);
                break;
            case "e2":
                showMessageDialog(ERROR_PASSWORD_EMPTY);
                break;
            case "e3":
                showMessageDialog(ERROR_INVALID_INPUT);
                break;
            case "e4":
                showMessageDialog(ERROR_INVALID_PASSWORD);
                break;
            default:
                performCryption(e.getActionCommand()); //ActionCommand is used to distinguish between the buttons clicked
                break;
        }
    }

    /**
     * accesses the Verschluesselung Class to perform the encryption and decryption
     * @param actionCommand used to distinguis between encryption and between decryption
     */
    private void performCryption(String actionCommand) {
        String output;
        switch (actionCommand) {
            case "Verschlüsseln":
                output = encryAlg.encryption(inputField.getText(), passwordField.getText());
                break;
            case "Entschlüsseln":
                output = encryAlg.decryption(inputField.getText(), passwordField.getText());
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + actionCommand);
        }
        
        if (output.length() != 0) { 
            outputField.setText(output.substring(0, output.length() - 1));
        } else {
            outputField.setText("");
        }
    }

    /**
     * shows a JOptionPane with a given message as an Input validation error
     * @param message message to be presented
     */
    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * shows a JOptionPane with a given Exception e
     * @param e Exception that is thrown
     * @param errorType type of error
     */
    private void showExceptionDialog(Exception e) {
        JOptionPane.showMessageDialog(this, e, "Java L&F  Error", JOptionPane.ERROR_MESSAGE);
    }

    /** 
     * clears all text from all textfields
     */
    private void clearFields() {
        inputField.setText("");
        passwordField.setText("");
        outputField.setText("");
    }

    /**
     * checks whether a String is ok to use
     * @param   s   input String
     * @return  returns the according error type -> see actionPerformed(ActionEvent e) {}
     */
    private String checkInput(String s, String pw) {
        if (s.isBlank()) return "e1";

        if (pw.isBlank()) return "e2";

        if (checkString(s, 0)) return "e3";

        if (checkString(pw, 1)) return "e4";

        return "";
    }

    /**
     * checks String if all chars are valid
     * @param s input String to be checked
     * @param rev 2 version for input message and password message
     * @return false if String s is valid
     */
    private boolean checkString(String s, int rev) {
        for (char c : s.toCharArray()) {
            if (!ALLOWED_CHARACTERS.contains(c)) {
                return true;
            } else if (rev == 1 && c == ' ') {
                return true;
            }
        }
        return false;
    }
}
