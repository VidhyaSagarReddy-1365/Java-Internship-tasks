import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Student {
    String name;
    int roll;
    double marks;

    Student(int roll, String name, double marks) {
        this.roll = roll;
        this.name = name;
        this.marks = marks;
    }
}

public class StudentManagerGUI {
    static ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {

        JFrame frame = new JFrame("Student Grade Manager");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

   
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel rollLabel = new JLabel("Roll No:");
        JTextField rollField = new JTextField();
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel marksLabel = new JLabel("Marks:");
        JTextField marksField = new JTextField();
        JButton addButton = new JButton("Add Student");

        inputPanel.add(rollLabel);
        inputPanel.add(rollField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(marksLabel);
        inputPanel.add(marksField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(addButton);


        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);


        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        JButton displayButton = new JButton("Display All");
        JButton avgButton = new JButton("Average");
        JButton highButton = new JButton("Highest");
        JButton lowButton = new JButton("Lowest");
        JButton searchButton = new JButton("Search by Roll");
        buttonPanel.add(displayButton);
        buttonPanel.add(avgButton);
        buttonPanel.add(highButton);
        buttonPanel.add(lowButton);
        buttonPanel.add(searchButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);


        addButton.addActionListener(e -> {
            try {
                int roll = Integer.parseInt(rollField.getText());
                String name = nameField.getText();
                double marks = Double.parseDouble(marksField.getText());

                students.add(new Student(roll, name, marks));
                outputArea.setText(" Student added: " + name);

                rollField.setText("");
                nameField.setText("");
                marksField.setText("");
            } catch (Exception ex) {
                outputArea.setText("Please enter valid inputs!");
            }
        });

        
        displayButton.addActionListener(e -> {
            if (students.isEmpty()) {
                outputArea.setText("No students data available.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Student s : students) {
                    sb.append("Roll: ").append(s.roll)
                      .append(" | Name: ").append(s.name)
                      .append(" | Marks: ").append(s.marks)
                      .append("\n");
                }
                outputArea.setText(sb.toString());
            }
        });

        avgButton.addActionListener(e -> {
            if (students.isEmpty()) {
                outputArea.setText("No students data available.");
            } else {
                double sum = 0;
                for (Student s : students) {
                    sum += s.marks;
                }
                double avg = sum / students.size();
                outputArea.setText(" Average Marks: " + avg);
            }
        });


        highButton.addActionListener(e -> {
            if (students.isEmpty()) {
                outputArea.setText("No students data available.");
            } else {
                Student max = Collections.max(students, Comparator.comparingDouble(s -> s.marks));
                outputArea.setText(" Highest Marks:\nRoll: " + max.roll + 
                                   "\nName: " + max.name + 
                                   "\nMarks: " + max.marks);
            }
        });

        lowButton.addActionListener(e -> {
            if (students.isEmpty()) {
                outputArea.setText("No students data available.");
            } else {
                Student min = Collections.min(students, Comparator.comparingDouble(s -> s.marks));
                outputArea.setText("Lowest Marks:\nRoll: " + min.roll + 
                                   "\nName: " + min.name + 
                                   "\nMarks: " + min.marks);
            }
        });

        searchButton.addActionListener(e -> {
            if (students.isEmpty()) {
                outputArea.setText("No students data available.");
            } else {
                String rollStr = JOptionPane.showInputDialog(frame, "Enter Roll Number:");
                if (rollStr != null) {
                    try {
                        int roll = Integer.parseInt(rollStr);
                        boolean found = false;
                        for (Student s : students) {
                            if (s.roll == roll) {
                                outputArea.setText(" Student Found:\nRoll: " + s.roll + 
                                                   "\nName: " + s.name + 
                                                   "\nMarks: " + s.marks);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            outputArea.setText(" No student found with Roll No: " + roll);
                        }
                    } catch (Exception ex) {
                        outputArea.setText("Invalid roll number input!");
                    }
                }
            }
        });

        frame.setVisible(true);
    }
}
