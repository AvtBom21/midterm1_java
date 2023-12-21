package org.example;

import org.example.Data.Account;

import javax.swing.*;
import java.awt.*;

public class MenuBar {
    private JMenuBar menuBar;
    private Account account;
    public MenuBar(JFrame frame, Account account1){
        menuBar = new JMenuBar();
        this.account = account1;
        if(account.getUser().equals("Admin")){
            AdminMenuBar(frame);
        }else{
            AccountMenuBar(frame);
        }
    }
    private void AdminMenuBar(JFrame frame){
        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open");

        JMenuItem historyItem = new JMenuItem("History");
        historyItem.addActionListener(e -> {
            frame.dispose();
            HistoryAccountFrame historyAccountFrame = new HistoryAccountFrame(account);
            historyAccountFrame.showForm();

        });

        JMenuItem LogoutItem = new JMenuItem("Logout");
        LogoutItem.addActionListener(e -> {
            frame.dispose();
            Login login = new Login();
            login.showForm();
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem);
        fileMenu.add(historyItem);
        fileMenu.add(LogoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu Gomenu = new JMenu("Go");

        JMenuItem Mainmenu = new JMenuItem("Main");
        Mainmenu.addActionListener(e -> {
            frame.dispose();
            Main main = new Main();
            main.showForm();
        });

        JMenuItem Studentmenu = new JMenuItem("Add Student");
        Studentmenu.addActionListener(e -> {
            AddStudentFrame studentFrame = new AddStudentFrame(account);
            studentFrame.showForm();
        });

        Gomenu.add(Mainmenu);
        Gomenu.add(Studentmenu);

        menuBar.add(fileMenu);
        menuBar.add(Gomenu);

        frame.setJMenuBar(menuBar);
    }
    private void AccountMenuBar(JFrame frame){
        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open");

        JMenuItem historyItem = new JMenuItem("History");
        historyItem.addActionListener(e -> {
            frame.dispose();
            HistoryAccountFrame historyAccountFrame = new HistoryAccountFrame(account);
            historyAccountFrame.showForm();

        });

        JMenuItem LogoutItem = new JMenuItem("Logout");
        LogoutItem.addActionListener(e -> {
            frame.dispose();
            Login login = new Login();
            login.showForm();
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem);
        fileMenu.add(historyItem);
        fileMenu.add(LogoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu Gomenu = new JMenu("Go");

        JMenuItem accountmenu = new JMenuItem("Account");
        accountmenu.addActionListener(e -> {
            frame.dispose();
            AccountFrame main = new AccountFrame(account);
            main.showForm();
        });

        JMenuItem Studentmenu = new JMenuItem("Add Student");
        Studentmenu.addActionListener(e -> {
            AddStudentFrame studentFrame = new AddStudentFrame(account);
            studentFrame.showForm();
        });

        Gomenu.add(accountmenu);
        Gomenu.add(Studentmenu);

        menuBar.add(fileMenu);
        menuBar.add(Gomenu);

        frame.setJMenuBar(menuBar);
    }
}
