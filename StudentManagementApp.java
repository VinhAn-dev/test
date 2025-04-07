import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Student {
    private String id;
    private String name;
    private String className;
    private double grade;

    public Student(String id, String name, String className, double grade) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}

class StudentManager {
    private final ArrayList<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public ArrayList<Student> getAllStudents() {
        return students;
    }

    public Student findStudentById(String id) {
        return students.stream().filter(student -> student.getId().equals(id)).findFirst().orElse(null);
    }

    public void removeStudent(String id) {
        students.removeIf(student -> student.getId().equals(id));
    }
}

public class StudentManagementApp extends JFrame {
    private final StudentManager manager = new StudentManager();
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Class", "Grade"}, 0);
    private final JTable table = new JTable(tableModel);

    public StudentManagementApp() {
        setTitle("Student Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // North Panel
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton searchButton = new JButton("Search");
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add Action Listeners
        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        searchButton.addActionListener(e -> searchStudent());

        setVisible(true);
    }

    private void addStudent() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField classField = new JTextField();
        JTextField gradeField = new JTextField();
        Object[] message = {
                "ID:", idField,
                "Name:", nameField,
                "Class:", classField,
                "Grade:", gradeField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String name = nameField.getText();
            String className = classField.getText();
            double grade = Double.parseDouble(gradeField.getText());

            Student student = new Student(id, name, className, grade);
            manager.addStudent(student);
            tableModel.addRow(new Object[]{id, name, className, grade});
        }
    }

    private void editStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = manager.findStudentById(id);

        if (student == null) return;

        JTextField nameField = new JTextField(student.getName());
        JTextField classField = new JTextField(student.getClassName());
        JTextField gradeField = new JTextField(String.valueOf(student.getGrade()));
        Object[] message = {
                "Name:", nameField,
                "Class:", classField,
                "Grade:", gradeField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            student.setName(nameField.getText());
            student.setClassName(classField.getText());
            student.setGrade(Double.parseDouble(gradeField.getText()));

            tableModel.setValueAt(student.getName(), selectedRow, 1);
            tableModel.setValueAt(student.getClassName(), selectedRow, 2);
            tableModel.setValueAt(student.getGrade(), selectedRow, 3);
        }
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        manager.removeStudent(id);
        tableModel.removeRow(selectedRow);
    }

    private void searchStudent() {
        String id = JOptionPane.showInputDialog(this, "Enter ID to search:");
        Student student = manager.findStudentById(id);

        if (student != null) {
            JOptionPane.showMessageDialog(this, "Student found:\nID: " + student.getId() +
                    "\nName: " + student.getName() +
                    "\nClass: " + student.getClassName() +
                    "\nGrade: " + student.getGrade());
        } else {
            JOptionPane.showMessageDialog(this, "Student not found.");
        }
    }

    public static void main(String[] args) {
        new StudentManagementApp();
    }
}
