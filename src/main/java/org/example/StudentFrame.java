package org.example;

import org.example.Data.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

public class StudentFrame {
    private JFrame frame;
    private JPanel Mainpanel;
    private JTextField Nametxt;
    private JTable TableCertificate;
    private JTextField NameCertificatetxt;
    private JTextArea descriptiontxt;
    private JButton addButton;
    private JPanel CertificatePanel;
    private JPanel InformationPanel;
    private JLabel Certificatelabel;
    private Student student;
    private Account account;
    private StudentDAO studentDAO;
    private CertificateDAO certificateDAO;

    public static void main(String[] args) {
    }

    public StudentFrame(Student student1, Account account1) {
        this.student = student1;
        this.account = account1;
        initialize();
    }
    public void initialize(){
        HibernateUtils hibernateUtils=new HibernateUtils();
        studentDAO = new StudentDAO(hibernateUtils.getSession());
        certificateDAO = new CertificateDAO(hibernateUtils.getSession());

        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Xử lý sự kiện đóng cửa sổ tại đây
                frame.dispose();
            }
        });
        Mainpanel = new JPanel();
        frame.getContentPane().add(Mainpanel, BorderLayout.CENTER);
        Mainpanel.setLayout(new BorderLayout());
        MenuBar menuBar = new MenuBar(frame, account);

        CreateInformation();
        CreateCertificate();

        Mainpanel.add(InformationPanel, BorderLayout.CENTER);
        Mainpanel.add(CertificatePanel, BorderLayout.EAST);
    }
    private void CreateInformation(){
        InformationPanel = new JPanel();
        InformationPanel.setLayout(new GridBagLayout());

        // Constraints for Nametxt
        GridBagConstraints nameLabelConstraints = new GridBagConstraints();
        nameLabelConstraints.gridx = 0;
        nameLabelConstraints.gridy = 0;
        nameLabelConstraints.anchor = GridBagConstraints.WEST;
        nameLabelConstraints.insets = new Insets(5, 5, 5, 5);

        // Constraints for TableCertificate
        GridBagConstraints tableCertificateConstraints = new GridBagConstraints();
        tableCertificateConstraints.gridx = 0;
        tableCertificateConstraints.gridy = 1;
        tableCertificateConstraints.gridwidth = 2; // Span across two columns
        tableCertificateConstraints.fill = GridBagConstraints.BOTH;
        tableCertificateConstraints.weightx = 1.0;
        tableCertificateConstraints.weighty = 1.0;
        tableCertificateConstraints.insets = new Insets(5, 5, 5, 5);

        Nametxt = new JTextField(student.getName());
        Nametxt.setPreferredSize(new Dimension(200, 30));

        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        Nametxt.setFont(new Font("Arial", Font.BOLD, 18));

        InformationPanel.add(nameLabel, nameLabelConstraints);
        nameLabelConstraints.gridx = 1;
        nameLabelConstraints.gridy = 0;
        nameLabelConstraints.anchor = GridBagConstraints.WEST;
        nameLabelConstraints.insets = new Insets(5, 5, 5, 5);
        InformationPanel.add(Nametxt, nameLabelConstraints);

        DefaultTableModel tableModel = new DefaultTableModel();
        TableCertificate = new JTable(tableModel);
        tableModel.addColumn("Name");
        tableModel.addColumn("Description");
        tableModel.addColumn("");
        tableModel.addColumn("");

        TableCertificate.getColumnModel().getColumn(2).setCellRenderer(new StudentFrame.ButtonRenderer());
        TableCertificate.getColumnModel().getColumn(2).setCellEditor(new StudentFrame.ButtonEditor(new JCheckBox(), TableCertificate));
        TableCertificate.getColumnModel().getColumn(3).setCellRenderer(new StudentFrame.ButtonRenderer());
        TableCertificate.getColumnModel().getColumn(3).setCellEditor(new StudentFrame.ButtonEditor(new JCheckBox(), TableCertificate));

        List<Certificate> certificates = student.getCertificates();
        if (certificates != null) {
            for (Certificate certificate : certificates) {
                Vector<Object> vec = new Vector<>();
                vec.add(certificate.getName());
                vec.add(certificate.getDescribe());
                vec.add("update");
                vec.add("delete");

                // Thêm vec vào tableModel
                tableModel.addRow(vec);
            }
        }
        TableCertificate.setRowHeight(100);
        TableCertificate.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane scrollPane = new JScrollPane(TableCertificate);
        InformationPanel.add(scrollPane, tableCertificateConstraints);
    }

    private void CreateCertificate(){
        CertificatePanel = new JPanel();
        CertificatePanel.setLayout(new GridBagLayout());

        Certificatelabel = new JLabel("Certificate");
        Certificatelabel.setPreferredSize(new Dimension(200, 20));
        NameCertificatetxt = new JTextField();
        descriptiontxt = new JTextArea();

        Font labelFont = new Font("Arial", Font.BOLD, 15);

        Certificatelabel.setFont(new Font("Arial", Font.BOLD, 18));
        Certificatelabel.setHorizontalAlignment(JLabel.CENTER);


// Tạo labelpanel để chứa Certificatelabel và đặt layout là FlowLayout
        JPanel labelpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelpanel.add(Certificatelabel);

// Tạo GridBagConstraints cho Certificatelabel
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.anchor = GridBagConstraints.CENTER;
        labelConstraints.insets = new Insets(5, 5, 5, 5);

// Thêm labelpanel vào CertificatePanel bằng GridBagConstraints
        CertificatePanel.add(labelpanel, labelConstraints);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        NameCertificatetxt.setFont(labelFont);
        NameCertificatetxt.setPreferredSize(new Dimension(200, 20));

// Tạo GridBagConstraints cho NameCertificatetxt
        GridBagConstraints nameConstraints = new GridBagConstraints();
        nameConstraints.gridx = 0;
        nameConstraints.gridy = 1;
        nameConstraints.anchor = GridBagConstraints.WEST;
        nameConstraints.insets = new Insets(5, 5, 5, 5);

// Thêm nameLabel và NameCertificatetxt vào CertificatePanel bằng GridBagConstraints
        CertificatePanel.add(nameLabel, nameConstraints);
        nameConstraints.gridy = 2;
        CertificatePanel.add(NameCertificatetxt, nameConstraints);

        JLabel deslabel = new JLabel("Description:");
        deslabel.setFont(labelFont);
        descriptiontxt.setFont(new Font("Arial", Font.PLAIN, 13));
        descriptiontxt.setLineWrap(true);
        descriptiontxt.setWrapStyleWord(true);

// Tạo GridBagConstraints cho deslabel
        GridBagConstraints desConstraints = new GridBagConstraints();
        desConstraints.gridx = 0;
        desConstraints.gridy = 3;
        desConstraints.anchor = GridBagConstraints.WEST;
        desConstraints.insets = new Insets(5, 5, 5, 5);

// Thêm deslabel và descriptiontxt vào CertificatePanel bằng GridBagConstraints
        CertificatePanel.add(deslabel, desConstraints);

        GridBagConstraints textAreaConstraints = new GridBagConstraints();
        textAreaConstraints.gridx = 0;
        textAreaConstraints.gridy = 4;
        textAreaConstraints.fill = GridBagConstraints.BOTH;
        textAreaConstraints.weightx = 1.0;
        textAreaConstraints.weighty = 1.0;
        textAreaConstraints.insets = new Insets(5, 5, 5, 5);

        CertificatePanel.add(new JScrollPane(descriptiontxt), textAreaConstraints);

        addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            student.setName(Nametxt.getText());
            Certificate certificate = new Certificate(NameCertificatetxt.getText(), descriptiontxt.getText(), student);
            student.addCertificates(certificate);
            certificateDAO.add(certificate);
            studentDAO.update(student);
            updateTable();
        });

// Tạo GridBagConstraints cho buttonPanel
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 5;
        buttonConstraints.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);

// Thêm buttonPanel vào CertificatePanel bằng GridBagConstraints
        CertificatePanel.add(buttonPanel, buttonConstraints);
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

                    // Kiểm tra xem có dòng nào được chọn không
                        Object value = table.getValueAt(row, column);

                        if ("update".equals(value) && row < student.getCertificates().size()) {
                            Object key = table.getValueAt(row, column - 2);
                            Object des = table.getValueAt(row, column - 1);

                            Certificate certificate = student.getCertificates().get(row);
                            certificate.setName((String) key);
                            certificate.setDescribe((String) des);

                            certificateDAO.update(certificate);
                            updateTable();

                            fireEditingStopped();
                        } else if ("delete".equals(value) && row < student.getCertificates().size()) {
                            Object key = table.getValueAt(row, column - 3);
                            Certificate certificate = certificateDAO.getByName((String) key);
                            student.rmCertificates(certificate);
                            studentDAO.update(student);
                            certificateDAO.remove(certificate);
                            updateTable();

                            fireEditingStopped();
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
    public void updateTable() {
        DefaultTableModel tableModel = (DefaultTableModel) TableCertificate.getModel();
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        tableModel.addColumn("Name");
        tableModel.addColumn("Description");
        tableModel.addColumn("");
        tableModel.addColumn("");

        TableCertificate.getColumnModel().getColumn(2).setCellRenderer(new StudentFrame.ButtonRenderer());
        TableCertificate.getColumnModel().getColumn(2).setCellEditor(new StudentFrame.ButtonEditor(new JCheckBox(), TableCertificate));
        TableCertificate.getColumnModel().getColumn(3).setCellRenderer(new StudentFrame.ButtonRenderer());
        TableCertificate.getColumnModel().getColumn(3).setCellEditor(new StudentFrame.ButtonEditor(new JCheckBox(), TableCertificate));

        student = studentDAO.get(student.getId());
        List<Certificate> certificates = student.getCertificates();
        if (certificates != null) {
            for (Certificate certificate : certificates) {
                Vector<Object> vec = new Vector<>();
                vec.add(certificate.getName());
                vec.add(certificate.getDescribe());
                vec.add("update");
                vec.add("delete");

                // Thêm vec vào tableModel
                tableModel.addRow(vec);
            }
        }
    }
    public void showForm(){frame.setVisible(true);}
    public void closeForm(){frame.setVisible(false);}
}
