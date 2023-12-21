package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Data.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AccountFrame {
    private JFrame frame;
    private JPanel InformationPanel;
    private JPanel ListPanel;
    private JLabel ImageLabel;
    private JTextField Nametxt;
    private JPasswordField Passtxt;
    private JButton Updatebtn;
    private JTable tableStudent;
    private JButton Addbtn;
    private JTextField Phonetxt;
    private JPanel MainPanel;
    private Account account;
    private List<Student> studentList = new ArrayList<>();
    private AccountDAO accountDAO;
    private StudentDAO studentDAO;
    private CertificateDAO certificateDAO;

    public AccountFrame(Account account1){
        this.account = account1;
        if(account!=null){
            initialize();
        }else {
            closeForm();
        }

    }
    public void initialize(){
        HibernateUtils hibernateUtils = new HibernateUtils();
        accountDAO = new AccountDAO(hibernateUtils.getSession());
        studentDAO = new StudentDAO(hibernateUtils.getSession());
        certificateDAO = new CertificateDAO(hibernateUtils.getSession());

        studentList = studentDAO.getAll();
        frame = new JFrame();
        frame.setBounds(100, 100, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MenuBar menuBar = new MenuBar(frame,account);

        MainPanel = new JPanel();
        frame.getContentPane().add(MainPanel, BorderLayout.CENTER);
        MainPanel.setLayout(new BorderLayout());

        createListpanel();
        createInformationPanel();

        MainPanel.add(ListPanel, BorderLayout.CENTER);
        MainPanel.add(InformationPanel, BorderLayout.WEST);
    }
    private void createListpanel(){
        ListPanel = new JPanel();
        ListPanel.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        tableStudent = new JTable(tableModel);

        tableModel.addColumn("Name");
        tableModel.addColumn("Certificate");
        tableModel.addColumn("");
        tableModel.addColumn("");
        TableColumnModel columnModel = tableStudent.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(300);
        tableStudent.getColumnModel().getColumn(2).setCellRenderer(new AccountFrame.ButtonRenderer());
        tableStudent.getColumnModel().getColumn(2).setCellEditor(new AccountFrame.ButtonEditor(new JCheckBox(),tableStudent));
        tableStudent.getColumnModel().getColumn(3).setCellRenderer(new AccountFrame.ButtonRenderer());
        tableStudent.getColumnModel().getColumn(3).setCellEditor(new AccountFrame.ButtonEditor(new JCheckBox(),tableStudent));
        for (Student student : studentList) {
            Vector<Object> vec = new Vector<>();
            vec.add(student.getName());
            vec.add(student.getCertificates());
            vec.add("detail");
            vec.add("delete");
            tableModel.addRow(vec);
        }
        tableStudent.setRowHeight(50);
        tableStudent.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(tableStudent);
        ListPanel.add(scrollPane, BorderLayout.CENTER);

        Addbtn = new JButton("Add");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Addbtn.addActionListener(e -> {
            AddStudentFrame addStudentFrame = new AddStudentFrame(account);
            addStudentFrame.showForm();
        });
        buttonPanel.add(Addbtn);
        ListPanel.add(buttonPanel, BorderLayout.SOUTH);

        JTextField nameFilter = new JTextField();
        nameFilter.setPreferredSize(new Dimension(50, 20));
        JButton searchByNameBtn = new JButton("Search by Name");
// Add an ActionListener to the search button
        searchByNameBtn.addActionListener(e -> {
            String name = nameFilter.getText().toLowerCase();
            filterTableByName(name);
        });

        JTextField certificateFilter = new JTextField();
        certificateFilter.setPreferredSize(new Dimension(50, 20));
        JButton searchByCertificateBtn = new JButton("Search by Certificate");
// Add an ActionListener to the search button
        searchByCertificateBtn.addActionListener(e -> {
            String certificate = certificateFilter.getText().toLowerCase();
            filterTableByCertificate(certificate);
        });

        JButton Addfile = new JButton("Import");
        Addfile.addActionListener(e -> {
            chooseExcelFile();
            uploadTable();
        });

        JButton Export = new JButton("Export");
        Export.addActionListener(e -> {
            exportToExcel(tableStudent,"output.xlsx");

        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(nameFilter);
        searchPanel.add(searchByNameBtn);
        searchPanel.add(new JLabel("Search by Certificate:"));
        searchPanel.add(certificateFilter);
        searchPanel.add(searchByCertificateBtn);
        searchPanel.add(Addfile);
        searchPanel.add(Export);
// Add the searchPanel to ListPanel
        ListPanel.add(searchPanel, BorderLayout.NORTH);
    }
    private void createInformationPanel(){
        InformationPanel = new JPanel();
        InformationPanel.setLayout(new GridLayout(7, 1, 10, 10)); // Added horizontal and vertical gap of 10 between cells

        ImageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon("./images/macdinh.jpg");
        Image originalImage = imageIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        ImageLabel.setIcon(scaledImageIcon);
        InformationPanel.add(ImageLabel);

        Nametxt = new JTextField(account.getName());
        Phonetxt = new JTextField(account.getPhone());
        Passtxt = new JPasswordField(account.getPass());

        Nametxt.setPreferredSize(new Dimension(200, 15)); // Adjust width and height based on your preference
        Phonetxt.setPreferredSize(new Dimension(200, 15));
        Passtxt.setPreferredSize(new Dimension(200, 15));

        Font labelFont = new Font("Arial", Font.BOLD, 18);

        Nametxt.setFont(labelFont);
        InformationPanel.add(Nametxt);

        Phonetxt.setFont(labelFont);
        InformationPanel.add(Phonetxt);

        Passtxt.setFont(labelFont);
        InformationPanel.add(Passtxt);

        Updatebtn = new JButton("Update");
        InformationPanel.add(Updatebtn);

        Updatebtn.setPreferredSize(new Dimension(150, 30)); // Adjust button size
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(Updatebtn);

        InformationPanel.add(buttonPanel);
    }
    public void showForm(){frame.setVisible(true);}
    public void closeForm(){frame.setVisible(false);}
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

                    if ("detail".equals(value)){
                        Object key = table.getValueAt(row, column - 2);
                        StudentFrame studentFrame = new StudentFrame(studentDAO.getByName((String)key),account);
                        studentFrame.showForm();

                        fireEditingStopped();
                        SwingUtilities.invokeLater(() -> {
                        });
                    }else if ("delete".equals(value)){
                        Object key = table.getValueAt(row, column - 3);
                        Student student = studentDAO.getByName((String) key);

                        student.clearCertificates();


                        if (studentDAO.getByName((String) key).getCertificates().isEmpty()) {
                            certificateDAO.removeByStudentId(student.getId());
                            studentDAO.remove(student.getId());
                        }

                        uploadTable();
                        JOptionPane.showMessageDialog(null, "delete Clicked: " + key);

                        fireEditingStopped();
                        SwingUtilities.invokeLater(() -> {
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
    private void filterTableByName(String name) {
        DefaultTableModel model = (DefaultTableModel) tableStudent.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            String cellValue = model.getValueAt(row, 0).toString().toLowerCase(); // 0 is the column index for Name
            boolean match = cellValue.contains(name);
            tableStudent.setRowHeight(row, match ? 50 : 1); // Set row height to 1 when hidden
        }
    }
    private void filterTableByCertificate(String certificate) {
        DefaultTableModel model = (DefaultTableModel) tableStudent.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            String cellValue = model.getValueAt(row, 1).toString().toLowerCase(); // 1 is the column index for Certificate
            boolean match = cellValue.contains(certificate);
            tableStudent.setRowHeight(row, match ? 50 : 1); // Set row height to 1 when hidden
        }
    }
    private void chooseExcelFile() {
        // Tạo đối tượng JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Thiết lập bộ lọc để chỉ hiển thị các tệp có định dạng .xlsx
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx");
        fileChooser.setFileFilter(filter);

        // Hiển thị hộp thoại chọn tệp và lấy đường dẫn khi người dùng chọn tệp
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            // In đường dẫn của tệp Excel đã chọn

            handleExcelFile(filePath);
        }
    }

    private void handleExcelFile(String filePath) {
        try {
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                // Nếu file không có định dạng .xlsx, thông báo lỗi
                JOptionPane.showMessageDialog(null, "Invalid file format. Please select a valid Excel file (.xlsx).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
                 Workbook workbook = new XSSFWorkbook(fileInputStream)) {

                // Lấy Sheet đầu tiên từ Workbook
                Sheet sheet = workbook.getSheetAt(0);

                // Lặp qua các hàng trong Sheet
                for (Row row : sheet) {
                    Student student = new Student();
                    for (int columnIndex = 0; columnIndex < row.getPhysicalNumberOfCells(); columnIndex++) {
                        Cell cell = row.getCell(columnIndex);
                        // Kiểm tra nếu không phải cột đầu tiên
                        if (columnIndex != 0) {
                            // Lấy giá trị từ ô và in ra màn hình
                            String cellValue = getCellValueAsString(cell);
                            String[] substring = cellValue.split("-");
                            Certificate certificate = new Certificate(substring[0], substring[1], student);
                            student.addCertificates(certificate);
                            certificateDAO.add(certificate);
                        } else {
                            // Xử lý cột đầu tiên (index 0), ví dụ: cắt chuỗi
                            String cellValue = getCellValueAsString(cell);
                            student.setName(cellValue);
                            student.setCertificates(new ArrayList<>());
                            studentDAO.add(student);
                        }
                        studentDAO.update(student);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // Xử lý ngoại lệ nếu có lỗi khác
            e.printStackTrace();
        }
    }
    private static String getCellValueAsString(Cell cell) {
        // Xử lý các loại dữ liệu ô trong Excel và chuyển thành chuỗi
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    private void uploadTable(){
        DefaultTableModel tableModel = (DefaultTableModel) tableStudent.getModel();
        tableModel.setRowCount(0);
        studentList = studentDAO.getAll();
        for (Student student : studentList) {
            Vector<Object> vec = new Vector<>();
            vec.add(student.getName());
            vec.add(student.getCertificates());
            vec.add("detail");
            vec.add("delete");
            tableModel.addRow(vec);
        }
    }
    public static void exportToExcel(JTable table, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Student Data");

            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
            }

            // Create data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 1);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    // Skip columns "detail" and "delete"
                    if (col == 2 || col == 3) {
                        continue;
                    }

                    Cell cell = dataRow.createCell(col);
                    Object cellValue = model.getValueAt(row, col);
                    if (cellValue != null) {
                        cell.setCellValue(cellValue.toString());
                    }
                }
            }

            // Write the workbook to a file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                JOptionPane.showMessageDialog(null, "Data exported to Excel successfully!", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error exporting data to Excel", "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
