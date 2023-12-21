package org.example;

import org.example.Data.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class Login {
    JFrame frame;
    private JTextField Usertxt;
    private JTextField Passtxt;
    private JButton LoginButton;
    private JLabel errorLabel;
    private JLabel UserLabel;
    private JLabel PassLabel;
    private JLabel Label;
    private static AccountDAO accountDAO;
    private static HistoryAccountDAO historyAccountDAO;


    public Login(){
        HibernateUtils hibernateUtils = new HibernateUtils();
        accountDAO = new AccountDAO(hibernateUtils.getSession());
        historyAccountDAO = new HistoryAccountDAO(hibernateUtils.getSession());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        Label = new JLabel("Login");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(Label, gbc);

        UserLabel = new JLabel("Username:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(UserLabel, gbc);

        // Add username text field
        Usertxt = new JTextField(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0; // Add this line to make it fill horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Add this line to make it fill horizontal space
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(Usertxt, gbc);

        // Add password label
        PassLabel = new JLabel("Password:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(PassLabel, gbc);

        // Add password text field
        Passtxt = new JPasswordField(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0; // Add this line to make it fill horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL; // Add this line to make it fill horizontal space
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(Passtxt, gbc);

        // Add login button
        LoginButton = new JButton("Login");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(LoginButton, gbc);

        // Add error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(errorLabel, gbc);

        // Create the frame and add the panel
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        LoginButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Account account = accountDAO.getbyUser(Usertxt.getText());
                        if(account != null){
                            if(account.getPass().equals(Passtxt.getText())&& account.getUser().equals("Admin")){
                                Main form = new Main();
                                HistoryAccount historyAccount = new HistoryAccount(account);
                                historyAccountDAO.add(historyAccount);
                                account.addHistoryAccount(historyAccount);
                                accountDAO.update(account);
                                form.showForm();
                                closeForm();
                            }else if(account.getPass().equals(Passtxt.getText()) && account.getStatus() == 1){
                                AccountFrame form = new AccountFrame(account);
                                HistoryAccount historyAccount = new HistoryAccount(account);
                                historyAccountDAO.add(historyAccount);
                                account.addHistoryAccount(historyAccount);
                                accountDAO.update(account);
                                form.showForm();
                                closeForm();
                            } else if (!account.getPass().equals(Passtxt.getText())) {
                                errorLabel.setText("User or Password is invalid");
                            } else if (account.getStatus()==0) {
                                errorLabel.setText("Account is locked");
                            }
                        }else{
                            errorLabel.setText("User or Password is invalid");
                        }
                    }
                }
        );
    }
    public void showForm(){frame.setVisible(true);}
    public void closeForm(){frame.setVisible(false);}
    public static void main(String[] args) {
        new Login();
    }
}
