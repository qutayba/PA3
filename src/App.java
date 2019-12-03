import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigInteger;

public class App {
    private EncryptionManager encryptionManager;
    private Stopwatch stopwatch;

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    // Encryption UI
    private JTextField enc_nTextField;
    private JButton enc_calculateStep1Button;
    private JLabel enc_pResultsLabel;
    private JLabel enc_qResultsLabel;
    private JLabel enc_step1ExcTime;
    private JLabel enc_step1ErrorLabel;
    private JButton enc_generateStep2Button;
    private JLabel enc_step2ResultsLabel;
    private JLabel enc_step2ErrorLabel;
    private JTextField enc_step3Textfield;
    private JButton enc_encryptStep3Button;
    private JTextPane enc_step3ResultsLabel;
    private JLabel enc_step3ErrorLabel;


    // Decryption UI
    private JTextField dec_nTextField;
    private JTextField dec_eTextField;
    private JLabel dec_step1ErrorLabel;
    private JButton dec_calculateStep1Button;
    private JLabel dec_step1ResultsLabel;
    private JTextField dec_decStep2TextField;
    private JButton dec_decryptStep2Button;
    private JTextPane dec_step2ResultsLabel;


    public App() {
        encryptionManager = new EncryptionManager();
        stopwatch = new Stopwatch();

        // Encryption steps
        initEncryptionStep1();
        initEncryptionStep2();
        initEncryptionStep3();

        // Decryption steps
        initDecryptionStep1();
        initDecryptionStep2();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Discrete Mathematics - PA3");
        frame.setContentPane(new App().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.pack();
        frame.setVisible(true);
    }


    private void initEncryptionStep1() {
        enc_calculateStep1Button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSuccess = true;
                try {
                    stopwatch.start();
                    encryptionManager.calculatePAndQ(new BigInteger(enc_nTextField.getText()));
                    stopwatch.stop();

                    enc_pResultsLabel.setText("p is " + encryptionManager.getP());
                    enc_qResultsLabel.setText("q is " + encryptionManager.getQ());
                    enc_step1ExcTime.setText("Amount of time busy finding p and q: " + stopwatch.getDuration() + " ms.");
                } catch (Exception ex) {
                    enc_step1ErrorLabel.setText("Error: " + ex.getMessage());
                    isSuccess = false;
                }

                enc_pResultsLabel.setVisible(isSuccess);
                enc_qResultsLabel.setVisible(isSuccess);
                enc_step1ExcTime.setVisible(isSuccess);
                enc_step1ErrorLabel.setVisible(!isSuccess);
            }
        });
    }

    private void initEncryptionStep2() {
        enc_generateStep2Button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    encryptionManager.calculateE();
                    enc_step2ResultsLabel.setText("e is " + encryptionManager.getE());
                    enc_step2ResultsLabel.setVisible(true);
                    enc_step2ErrorLabel.setVisible(false);

                } catch (Exception ex) {
                    enc_step2ErrorLabel.setText("Error: " + ex.getMessage());
                    enc_step2ErrorLabel.setVisible(true);
                    enc_step2ResultsLabel.setVisible(false);
                }
            }
        });
    }

    private void initEncryptionStep3() {
        enc_encryptStep3Button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String c = enc_step3Textfield.getText();
                    enc_step3ResultsLabel.setText("Message after encryption is: " + encryptionManager.encrypt(c));
                    enc_step3ResultsLabel.setVisible(true);
                    enc_step3ErrorLabel.setVisible(false);

                } catch (Exception ex) {
                    enc_step3ErrorLabel.setText("Error: " + ex.getMessage());
                    enc_step3ErrorLabel.setVisible(true);
                    enc_step3ResultsLabel.setVisible(false);
                }
            }
        });
    }

    private void initDecryptionStep1() {
        dec_calculateStep1Button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    BigInteger e = new BigInteger(dec_eTextField.getText());
                    BigInteger n = new BigInteger(dec_nTextField.getText());
                    encryptionManager.calculateD(n, e);
                    dec_step1ResultsLabel.setText("d is " + encryptionManager.getD());
                    dec_step1ResultsLabel.setVisible(true);
                    dec_step1ErrorLabel.setVisible(false);
                }
                catch (Exception ex) {
                    dec_step1ErrorLabel.setText("Error: " + ex.getMessage());
                    dec_step1ResultsLabel.setVisible(false);
                    dec_step1ErrorLabel.setVisible(true);
                }
            }
        });
    }

    private void initDecryptionStep2() {
        dec_decryptStep2Button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String m = encryptionManager.decrypt(dec_decStep2TextField.getText());
                    dec_step2ResultsLabel.setText("Message after decryption is: " + m);
                    dec_step2ResultsLabel.setVisible(true);

                }
                catch (Exception ex) {
                    dec_step1ErrorLabel.setText("Error: " + ex.getMessage());
                    dec_step2ResultsLabel.setVisible(false);
                    dec_step1ErrorLabel.setVisible(true);
                }
            }
        });
    }
}
