package org.example;

import org.example.Data.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AddStudentFrame {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField StudentNametxt;
    private JButton AddStudentBtn;
    private JTextField Certificatetxt;
    private JTextArea Destxt;
    private JButton AddCertiBtn;
    private JLabel Studentlabel;
    private JLabel CertificateLabel;
    private JPanel StudentPanel;
    private JPanel CertificatePanel;
    private JPanel TablePanel;
    private JTable tableCertificate;
    private JLabel NameLabel;
    private Student student;
    private StudentDAO studentDAO;
    private CertificateDAO certificateDAO;
    private Account account;

    public AddStudentFrame(Account account1){
        this.account = account1;
        initialize();
    }
    public void initialize(){
        HibernateUtils hibernateUtils = new HibernateUtils();
        studentDAO = new StudentDAO(hibernateUtils.getSession());
        certificateDAO = new CertificateDAO(hibernateUtils.getSession());
        frame = new JFrame();
        frame.setBounds(100, 100, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Xử lý sự kiện đóng cửa sổ tại đây
                frame.dispose();

            }
        });
        mainPanel = new JPanel();
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new BorderLayout());

        MenuBar menuBar = new MenuBar(frame,account);
        createStudentPanel();
        createCertificatePanel();
        createTablePanel();

        // Use JSplitPane to divide the frame into two sections
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, StudentPanel, CertificatePanel);
        splitPane.setResizeWeight(0.5); // Equal distribution
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(TablePanel, BorderLayout.SOUTH);
    }
    public void createStudentPanel(){
        StudentPanel = new JPanel();
        StudentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        Studentlabel = new JLabel("Student");
        Studentlabel.setFont(new Font("Arial", Font.BOLD, 18));
        StudentNametxt = new JTextField();
        AddStudentBtn = new JButton("Add Student");
        StudentNametxt.setPreferredSize(new Dimension(200, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        StudentPanel.add(Studentlabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        StudentPanel.add(new JLabel("Student Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        StudentPanel.add(StudentNametxt, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        StudentPanel.add(new JLabel(), gbc);


        AddStudentBtn.addActionListener(e -> {
            student = new Student(StudentNametxt.getText(),new ArrayList<>());
            NameLabel.setText("Student: "+student.getName());
            studentDAO.add(student);
            updateTableData();
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        StudentPanel.add(AddStudentBtn, gbc);
    }
    public void createCertificatePanel(){
        CertificatePanel = new JPanel();
        CertificatePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        CertificateLabel = new JLabel("Certificate");
        CertificateLabel.setFont(new Font("Arial", Font.BOLD, 18));
        Certificatetxt = new JTextField(20);  // Set the number of columns
        Destxt = new JTextArea();
        Destxt.setLineWrap(true);
        Destxt.setWrapStyleWord(true);
        AddCertiBtn = new JButton("Add Certificate");
        Certificatetxt.setColumns(20);  // Set the number of columns
        Certificatetxt.setPreferredSize(new Dimension(200, Certificatetxt.getPreferredSize().height));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        CertificatePanel.add(CertificateLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        CertificatePanel.add(new JLabel("Certificate:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make Certificatetxt fill horizontally
        CertificatePanel.add(Certificatetxt, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        CertificatePanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        CertificatePanel.add(new JLabel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        CertificatePanel.add(AddCertiBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        CertificatePanel.add(new JScrollPane(Destxt), gbc);

        AddCertiBtn.addActionListener(e -> {
            Certificate certificate = new Certificate(Certificatetxt.getText(), Destxt.getText(), student);
            student.addCertificates(certificate);
            certificateDAO.add(certificate);
            studentDAO.update(student);
            updateTableData();
        });
    }
    public void createTablePanel() {
        TablePanel = new JPanel();
        TablePanel.setLayout(new BorderLayout());
        TablePanel.setPreferredSize(new Dimension(1000, 350));

        JPanel labelPanel = new JPanel(new BorderLayout());
        NameLabel = new JLabel("");
        NameLabel.setForeground(Color.BLUE);
        NameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        labelPanel.add(NameLabel, BorderLayout.NORTH);
        TablePanel.add(labelPanel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableCertificate = new JTable(tableModel);

        tableModel.addColumn("Certificate");
        tableModel.addColumn("Description");
        tableModel.addColumn("");
        tableModel.addColumn("");

        if (student != null && student.getCertificates() != null) {
            List<Certificate> certificateList = student.getCertificates();
            for (Certificate certificate : certificateList) {
                Vector<Object> vec = new Vector<>();
                vec.add(certificate.getName());
                vec.add(certificate.getDescribe());
            }
        }

        tableCertificate.setRowHeight(25);
        tableCertificate.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(tableCertificate);
        TablePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void updateTableData() {
        DefaultTableModel tableModel = (DefaultTableModel) tableCertificate.getModel();
        tableModel.setRowCount(0); // Clear existing data

        if (student != null && student.getCertificates() != null) {
            List<Certificate> certificateList = student.getCertificates();
            for (Certificate certificate : certificateList) {
                Vector<Object> vec = new Vector<>();
                vec.add(certificate.getName());
                vec.add(certificate.getDescribe());
                vec.add("");
                vec.add("");
                tableModel.addRow(vec);
            }
        }
    }
    public void showForm(){frame.setVisible(true);}
    public void closeForm(){frame.setVisible(false);}
}
