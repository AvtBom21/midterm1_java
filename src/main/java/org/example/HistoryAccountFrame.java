package org.example;

import org.example.Data.Account;
import org.example.Data.AccountDAO;
import org.example.Data.HibernateUtils;
import org.example.Data.HistoryAccount;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;
import java.util.Set;
import java.util.HashSet;


public class HistoryAccountFrame {
    private JFrame frame;
    private JPanel MainPanel;
    private JPanel HistoryTablePanel;
    private JTable Historytb;
    private JComboBox HistoryCBB;
    private JTextField SearchField;
    private JButton searchbtn;
    private Account account;
    private AccountDAO accountDAO;

    public HistoryAccountFrame(Account account1){
        this.account = account1;
        initialize();
    }
    private void initialize(){
        HibernateUtils hibernateUtils = new HibernateUtils();
        accountDAO = new AccountDAO(hibernateUtils.getSession());
        frame = new JFrame();
        frame.setBounds(100, 100, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MenuBar menuBar = new MenuBar(frame, account);

        MainPanel = new JPanel();
        MainPanel.setLayout(new BorderLayout());  // Set BorderLayout for MainPanel

        if(account.getUser().equals("Admin")){
            CreateFunction();
            CreateTableHistory();
        }else{
            CreateaFunction();
            CreateTableaHistory();
        }

        frame.getContentPane().add(MainPanel, BorderLayout.CENTER);
    }
    private void CreateTableaHistory(){
        HistoryTablePanel = new JPanel();
        HistoryTablePanel.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        Historytb = new JTable(tableModel);

        List<HistoryAccount> historyAccounts = account.getHistoryAccount();

        tableModel.addColumn("User");
        tableModel.addColumn("Time");

        for (HistoryAccount history : historyAccounts) {
            if(history.getDayLogin().equals((String) HistoryCBB.getSelectedItem())) {
                Vector<Object> vec = new Vector<>();
                vec.add(history.getAccount().getName());
                vec.add(history.getTimeLogin());
                tableModel.addRow(vec);
            }
        }


        Historytb.setRowHeight(100);
        Historytb.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane scrollPane = new JScrollPane(Historytb);
        HistoryTablePanel.add(scrollPane, BorderLayout.CENTER);

        MainPanel.add(HistoryTablePanel, BorderLayout.CENTER);
    }
    private void CreateTableHistory(){
        HistoryTablePanel = new JPanel();
        HistoryTablePanel.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        Historytb = new JTable(tableModel);

        List<Account> accounts = accountDAO.getAll();

        tableModel.addColumn("User");
        tableModel.addColumn("Time");

        for (Account acc : accounts) {
            List<HistoryAccount> historyAccounts = acc.getHistoryAccount();

            for (HistoryAccount history : historyAccounts) {
                if(history.getDayLogin().equals((String) HistoryCBB.getSelectedItem())){
                    Vector<Object> vec = new Vector<>();
                    vec.add(history.getAccount().getName());
                    vec.add(history.getTimeLogin());
                    tableModel.addRow(vec);
                }
            }
        }

        Historytb.setRowHeight(100);
        Historytb.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane scrollPane = new JScrollPane(Historytb);
        HistoryTablePanel.add(scrollPane, BorderLayout.CENTER);

        MainPanel.add(HistoryTablePanel, BorderLayout.CENTER);
    }
    private void CreateaFunction(){
        JPanel functionPanel = new JPanel();
        functionPanel.setLayout(new FlowLayout());

        // JComboBox
        HistoryCBB = new JComboBox();

        Set<String> uniqueDayLogins = new HashSet<>();

        List<HistoryAccount> historyList = account.getHistoryAccount();

        for (HistoryAccount history : historyList) {
            // Add only unique dayLogin values to the JComboBox
            if (uniqueDayLogins.add(history.getDayLogin())) {
                HistoryCBB.addItem(history.getDayLogin());
            }
        }

        HistoryCBB.addItemListener(e -> {
            String selectedDay = (String) HistoryCBB.getSelectedItem();
            DefaultTableModel tableModel = (DefaultTableModel) Historytb.getModel();

            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            List<HistoryAccount> historyAccounts = account.getHistoryAccount();

            tableModel.addColumn("User");
            tableModel.addColumn("Time");

            for (HistoryAccount history : historyAccounts) {
                if (history.getDayLogin().equals(selectedDay)) {
                    Vector<Object> vec = new Vector<>();
                    vec.add(history.getAccount().getName());
                    vec.add(history.getTimeLogin());
                    tableModel.addRow(vec);
                }
            }
        });

        functionPanel.add(HistoryCBB);

        MainPanel.add(functionPanel, BorderLayout.SOUTH);
    }
    private void CreateFunction(){
        JPanel functionPanel = new JPanel();
        functionPanel.setLayout(new FlowLayout());

        // JComboBox
        HistoryCBB = new JComboBox();

        Set<String> uniqueDayLogins = new HashSet<>();

        List<Account> accounts = accountDAO.getAll();
        for (Account acc : accounts) {
            List<HistoryAccount> historyList = acc.getHistoryAccount();

            for (HistoryAccount history : historyList) {
                // Add only unique dayLogin values to the JComboBox
                if (uniqueDayLogins.add(history.getDayLogin())) {
                    HistoryCBB.addItem(history.getDayLogin());
                }
            }
        }

        HistoryCBB.addItemListener(e -> {
            String selectedDay = (String) HistoryCBB.getSelectedItem();
            DefaultTableModel tableModel = (DefaultTableModel) Historytb.getModel();
            tableModel.setRowCount(0);

            List<Account> accounts1s = accountDAO.getAll();

            for (Account acc : accounts1s) {
                List<HistoryAccount> historyAccounts = acc.getHistoryAccount();

                for (HistoryAccount history : historyAccounts) {
                    if (selectedDay.equals(history.getDayLogin())) {
                        Vector<Object> vec = new Vector<>();
                        vec.add(history.getAccount().getName());
                        vec.add(history.getTimeLogin());
                        tableModel.addRow(vec);
                    }
                }
            }
        });

        functionPanel.add(HistoryCBB);

        // JTextField
        SearchField = new JTextField(20);
        functionPanel.add(SearchField);

        // JButton
        searchbtn = new JButton("Search");
        searchbtn.addActionListener(e -> performSearch(SearchField.getText()));
        functionPanel.add(searchbtn);

        MainPanel.add(functionPanel, BorderLayout.SOUTH);
    }
    private void performSearch(String selectedUser) {
        DefaultTableModel tableModel = (DefaultTableModel) Historytb.getModel();
        tableModel.setRowCount(0);

        List<Account> accounts = accountDAO.getAll();
        String selectedDay = (String) HistoryCBB.getSelectedItem();

        for (Account acc : accounts) {
            if (acc.getUser().toLowerCase().contains(selectedUser.toLowerCase())) {
                List<HistoryAccount> historyAccounts = acc.getHistoryAccount();

                for (HistoryAccount history : historyAccounts) {
                    if (selectedDay.equals(history.getDayLogin())) {
                        Vector<Object> vec = new Vector<>();
                        vec.add(history.getAccount().getName());
                        vec.add(history.getTimeLogin());
                        tableModel.addRow(vec);
                    }
                }
            }
        }
    }
    public void showForm(){frame.setVisible(true);}
    public void closeForm(){frame.setVisible(false);}

}
