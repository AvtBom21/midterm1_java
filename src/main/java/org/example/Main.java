package org.example;

import org.example.Data.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.List;


public class Main {
    private JFrame frame;
    private JPanel MainPanel;
    private JMenuBar menuBar;
    private JTable table;
    private JButton updateButton;
    private JButton addButton;
    private JLabel ImageLabel;
    private JPanel InformationPanel;
    private JTextField Nametxt;
    private JTextField Phonetxt;
    private JTextField Agetxt;
    private JTextField Usertxt;
    private JPasswordField Passtxt;
    private JPanel TablePanel;

    private static AccountDAO dao;

    public static void main(String[] args) {
        Main window = new Main();
        window.frame.setVisible(true);
    }

    public Main() {
        initialize();
    }

    private void initialize() {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Certificate.class)
                .addAnnotatedClass(HistoryAccount.class).buildSessionFactory();
        dao = new AccountDAO(factory.openSession());

        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainPanel = new JPanel();
        frame.getContentPane().add(MainPanel, BorderLayout.CENTER);
        MainPanel.setLayout(new BorderLayout());
        MenuBar menuBar = new MenuBar(frame, dao.getbyUser("Admin"));

        createTablePanel();
        createInformationPanel();

        MainPanel.add(TablePanel, BorderLayout.CENTER);
        MainPanel.add(InformationPanel, BorderLayout.WEST);
    }

    private void createTablePanel() {
        TablePanel = new JPanel();
        TablePanel.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        tableModel.addColumn("Image");
        tableModel.addColumn("User");
        tableModel.addColumn("Password");
        tableModel.addColumn("Name");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Age");
        tableModel.addColumn("Status");
        tableModel.addColumn("");
        tableModel.addColumn("");

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(120);  // Image column
        columnModel.getColumn(1).setPreferredWidth(150);  // User column
        columnModel.getColumn(2).setPreferredWidth(150);  // Password column
        columnModel.getColumn(3).setPreferredWidth(250);  // Name column
        columnModel.getColumn(4).setPreferredWidth(150);  // Phone column
        columnModel.getColumn(5).setPreferredWidth(20);   // Age column
        columnModel.getColumn(6).setPreferredWidth(20);   // Status column
        columnModel.getColumn(7).setPreferredWidth(50);   // Update button column
        columnModel.getColumn(8).setPreferredWidth(50);

        List<Account> accounts = dao.getAll();

        // Create a custom cell renderer for the Image column
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageRenderer());

        // Create a custom cell editor for the Image column (non-editable)
        TableCellEditor nonEditableCellEditor = new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return null; // Return null to make the cell non-editable
            }
        };

        // Set the custom editor for the Image column
        table.getColumnModel().getColumn(0).setCellEditor(nonEditableCellEditor);

        // Create a custom cell editor for the Image column
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField()) {
            private JButton button = new JButton("Update Image");
            private String imagePath;
            {
                button.addActionListener(e -> {
                    UploadImage();
                });
            }
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                button.setText("Update Image");
                return button;
            }

            @Override
            public Object getCellEditorValue() {
                return imagePath;
            }
        });
        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox(),table));
        table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(),table));

        for (Account acc : accounts) {
            Vector<Object> vec = new Vector<>();
            ImageIcon imageIcon = new ImageIcon("./images/"+acc.getImage());

            Image originalImage = imageIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);

            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

            vec.add(scaledImageIcon);
            vec.add(acc.getUser());
            vec.add(acc.getPass());
            vec.add(acc.getName());
            vec.add(acc.getPhone());
            vec.add(acc.getAge());
            vec.add(acc.getStatus());
            vec.add("update");
            vec.add("delete");
            tableModel.addRow(vec);

        }

        // Set the cell size
        table.setRowHeight(100);
        table.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane scrollPane = new JScrollPane(table);
        TablePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createInformationPanel() {
        InformationPanel = new JPanel();
        InformationPanel.setLayout(new GridLayout(7, 2));

        Nametxt = new JTextField();
        Phonetxt = new JTextField();
        Agetxt = new JTextField();
        Usertxt = new JTextField();
        Passtxt = new JPasswordField();

        Nametxt.setPreferredSize(new Dimension(50, 20)); // Adjust width and height based on your preference
        Phonetxt.setPreferredSize(new Dimension(50, 20));
        Agetxt.setPreferredSize(new Dimension(50, 20));
        Usertxt.setPreferredSize(new Dimension(50, 20));
        Passtxt.setPreferredSize(new Dimension(50, 20));

        Font labelFont = new Font("Arial", Font.BOLD, 18);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        Nametxt.setFont(labelFont);
        InformationPanel.add(nameLabel);
        InformationPanel.add(Nametxt);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(labelFont);
        Phonetxt.setFont(labelFont);
        InformationPanel.add(phoneLabel);
        InformationPanel.add(Phonetxt);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(labelFont);
        Agetxt.setFont(labelFont);
        InformationPanel.add(ageLabel);
        InformationPanel.add(Agetxt);

        JLabel userLabel = new JLabel("User:");
        userLabel.setFont(labelFont);
        Usertxt.setFont(labelFont);
        InformationPanel.add(userLabel);
        InformationPanel.add(Usertxt);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        Passtxt.setFont(labelFont);
        InformationPanel.add(passwordLabel);
        InformationPanel.add(Passtxt);

        addButton = new JButton("Add");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);

        addButton.addActionListener(e -> {
            if(Phonetxt.getText().equals("") || Usertxt.getText().equals("") ||
                    Passtxt.getText().equals("") || Nametxt.getText().equals("") || Agetxt.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Some or a text field is null");
            }else if(!isNumber(Agetxt.getText())){
                JOptionPane.showMessageDialog(null, "Age text field is not number");
            }else{
                Account account = new Account(Phonetxt.getText(), "macdinh.jpg", Usertxt.getText(),
                        Passtxt.getText(), Nametxt.getText(), Integer.parseInt(Agetxt.getText()), 0, null);
                if(dao.get(Phonetxt.getText())!=null){
                    JOptionPane.showMessageDialog(null, "Your phone is used");
                } else if (dao.getbyUser(Usertxt.getText())!=null) {
                    JOptionPane.showMessageDialog(null, "Your Username is used");
                } else{
                    dao.add(account);
                }
                updateTableData();
            }
        });

        InformationPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();

            // Check if the value is an ImageIcon
            if (value instanceof ImageIcon) {
                label.setIcon((Icon) value);
            } else if (value instanceof String) {
                // If the value is a String, assume it's a path to an image and create an ImageIcon
                ImageIcon icon = new ImageIcon((String) value);
                label.setIcon(icon);
            }

            label.setHorizontalAlignment(JLabel.CENTER);
            return label;
        }
    }
    class ButtonRenderer extends DefaultTableCellRenderer {
        private JButton button = new JButton();

        ButtonRenderer() {
            button.setOpaque(true);
            // Set any other button properties as needed
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            button.setText(value == null ? "" : value.toString());
            return button;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String actionCommand;
        private JTable table;
        ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            button = new JButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the row and column indices of the clicked button
                    int row = table.getEditingRow();
                    int column = table.getEditingColumn();

                    Object value = table.getValueAt(row, column);

                    if ("update".equals(value)){
                        Account accounttable = new Account();
                        for(int i = 0; i < 7; i++){
                            Object element = table.getValueAt(row, i);
                            switch (i){
                                case 1:
                                    accounttable.setUser((String) element);
                                    break;
                                case 2:
                                    accounttable.setPass((String) element);
                                    break;
                                case 3:
                                    accounttable.setName((String) element);
                                    break;
                                case 4:
                                    accounttable.setPhone((String) element);
                                    break;
                                case 5:
                                    if (element instanceof Integer) {
                                        accounttable.setAge((Integer) element);
                                    } else if (element instanceof String) {
                                        try {
                                            accounttable.setAge(Integer.parseInt((String) element));
                                        } catch (NumberFormatException a) {
                                            // Handle the case where parsing fails
                                            a.printStackTrace(); // or handle accordingly
                                        }
                                    }
                                    break;
                                case 6:
                                    if (element instanceof Integer) {
                                        accounttable.setStatus((Integer) element);
                                    } else if (element instanceof String) {
                                        try {
                                            accounttable.setStatus(Integer.parseInt((String) element));
                                        } catch (NumberFormatException a) {
                                            a.printStackTrace();
                                        }
                                    }
                                    break;
                            }
                        }
                        accounttable.setId(dao.get(accounttable.getPhone()).getId());
                        dao.update(accounttable);
                        JOptionPane.showMessageDialog(null, "update success: ");
                        fireEditingStopped();
                        SwingUtilities.invokeLater(() -> {
                            updateTableData();
                        });
                    }else if ("delete".equals(value)){
                        Object key = table.getValueAt(row, column - 4);

                        dao.remove(dao.get((String) key));
                        JOptionPane.showMessageDialog(null, "delete Success ");

                        fireEditingStopped();
                        SwingUtilities.invokeLater(() -> {
                            updateTableData();
                        });
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            actionCommand = value == null ? "" : value.toString();
            button.setText(actionCommand);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return actionCommand;
        }
    }
    public void UploadImage(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(frame);

        File imagesFolder = new File("./images");
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(imagesFolder, file.getName()));

                fileOutputStream.write(new FileInputStream(file).readAllBytes());
                fileOutputStream.close();

                int selectedRow = table.getSelectedRow();

                // Update the image path in the data model
                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

                String username = tableModel.getValueAt(selectedRow, 1).toString();
                Account account = dao.getbyUser(username);
                account.setImage(file.getName());
                dao.update(account);

                // Notify listeners that the table data has changed
                updateTableData();

                // Display the username using JOptionPane (you can use a JLabel or any other component)
                JOptionPane.showMessageDialog(frame, "Image updated for user: " + username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void updateTableData() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);

        List<Account> accounts = dao.getAll();

        for (Account acc : accounts) {
            Vector<Object> vec = new Vector<>();
            ImageIcon imageIcon = new ImageIcon("./images/"+acc.getImage());

            Image originalImage = imageIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);

            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

            vec.add(scaledImageIcon);
            vec.add(acc.getUser());
            vec.add(acc.getPass());
            vec.add(acc.getName());
            vec.add(acc.getPhone());
            vec.add(acc.getAge());
            vec.add(acc.getStatus());
            vec.add("update");
            vec.add("delete");
            tableModel.addRow(vec);
        }

        tableModel.fireTableDataChanged(); // Notify listeners that the table data has changed
    }
    public void showForm(){frame.setVisible(true);}
    public void closeForm(){frame.setVisible(false);}
}

