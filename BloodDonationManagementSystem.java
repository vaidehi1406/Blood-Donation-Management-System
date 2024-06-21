import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BloodDonationManagementSystem extends JFrame {

    // GUI components
    private JTextField textFieldName;
    private JTextField textFieldBloodGroup;
    private JTextField textFieldAddress;
    private JTextField textFieldPhoneNumber;
    private JButton btnAddDonor;
    private JButton btnDeleteDonor;
    private JButton btnSearchDonor;
    private JButton btnUpdateDonor;
    private JButton btnDisplayDonors; // Added display button
    private JTextArea textAreaDonors;

     // Database connection and prepared statements
    private Connection connection;
    private PreparedStatement insertStatement;
    private PreparedStatement deleteStatement;
    private PreparedStatement searchStatement;
    private PreparedStatement updateStatement;

    // Constructor
    public BloodDonationManagementSystem() {
        initializeGUI();

        // Establish database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood_donation_management", "root", "VKale@123");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Prepare database statements
        try {
            insertStatement = connection.prepareStatement("INSERT INTO donor (name, blood_group, address, phone_number) VALUES (?, ?, ?, ?)");
            deleteStatement = connection.prepareStatement("DELETE FROM donor WHERE blood_group = ?");
            searchStatement = connection.prepareStatement("SELECT * FROM donor WHERE blood_group = ?");
            updateStatement = connection.prepareStatement("UPDATE donor SET name = ?, blood_group = ?, address = ?, phone_number = ? WHERE blood_group = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load existing donors on startup
        loadDonors();

        // Action listener for the "Add Donor" button
        btnAddDonor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textFieldName.getText();
                String bloodGroup = textFieldBloodGroup.getText();
                String address = textFieldAddress.getText();
                String phoneNumber = textFieldPhoneNumber.getText();

                addDonor(name, bloodGroup, address, phoneNumber);

                // Clear input fields
                clearInputFields();
            }
        });

        // Action listener for the "Delete Donor" button
        btnDeleteDonor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bloodGroup = textFieldBloodGroup.getText();

                deleteDonor(bloodGroup);

                // Clear input fields
                clearInputFields();
            }
        });

        // Action listener for the "Search Donor" button
        btnSearchDonor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bloodGroup = textFieldBloodGroup.getText();

                searchDonor(bloodGroup);
            }
        });

        // Action listener for the "Update Donor" button
        btnUpdateDonor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textFieldName.getText();
                String bloodGroup = textFieldBloodGroup.getText();
                String address = textFieldAddress.getText();
                String phoneNumber = textFieldPhoneNumber.getText();

                updateDonor(name, bloodGroup, address, phoneNumber);

                // Clear input fields
                clearInputFields();
            }
        });

        // Action listener for the "Display Donors" button
        btnDisplayDonors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display all donors
                displayDonors();
            }
        });

        // Close database connection and exit on window close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    insertStatement.close();
                    deleteStatement.close();
                    searchStatement.close();
                    updateStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeGUI() {
        setTitle("Blood Donation Management System");
        setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 30, 100, 30);
        add(lblName);

        textFieldName = new JTextField();
        textFieldName.setBounds(130, 30, 200, 30);
        add(textFieldName);

        JLabel lblBloodGroup = new JLabel("Blood Group:");
        lblBloodGroup.setBounds(30, 80, 100, 30);
        add(lblBloodGroup);

        textFieldBloodGroup = new JTextField();
        textFieldBloodGroup.setBounds(130, 80, 200, 30);
        add(textFieldBloodGroup);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(30, 130, 100, 30);
        add(lblAddress);

        textFieldAddress = new JTextField();
        textFieldAddress.setBounds(130, 130, 200, 30);
        add(textFieldAddress);

        JLabel lblPhoneNumber = new JLabel("Phone Number:");
        lblPhoneNumber.setBounds(30, 180, 100, 30);
        add(lblPhoneNumber);

        textFieldPhoneNumber = new JTextField();
        textFieldPhoneNumber.setBounds(130, 180, 200, 30);
        add(textFieldPhoneNumber);

        btnAddDonor = new JButton("Add Donor");
        btnAddDonor.setBounds(30, 230, 300, 30);
        add(btnAddDonor);

        btnDeleteDonor = new JButton("Delete Donor");
        btnDeleteDonor.setBounds(30, 280, 300, 30);
        add(btnDeleteDonor);

        btnSearchDonor = new JButton("Search Donor");
        btnSearchDonor.setBounds(30, 330, 300, 30);
        add(btnSearchDonor);

        btnUpdateDonor = new JButton("Update Donor");
        btnUpdateDonor.setBounds(30, 380, 300, 30);
        add(btnUpdateDonor);

        btnDisplayDonors = new JButton("Display Donors");
        btnDisplayDonors.setBounds(30, 430, 300, 30);
        add(btnDisplayDonors);

        textAreaDonors = new JTextArea();
        textAreaDonors.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textAreaDonors);
        scrollPane.setBounds(400, 30, 350, 430);
        add(scrollPane);

        setSize(800, 520);
        setVisible(true);
    }

    private void loadDonors() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM donor");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String bloodGroup = resultSet.getString("blood_group");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");

                textAreaDonors.append("ID: " + id + ", Name: " + name + ", Blood Group: " + bloodGroup + ", Address: " + address + ", Phone Number: " + phoneNumber + "\n");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDonor(String name, String bloodGroup, String address, String phoneNumber) {
        try {
            insertStatement.setString(1, name);
            insertStatement.setString(2, bloodGroup);
            insertStatement.setString(3, address);
            insertStatement.setString(4, phoneNumber);
            insertStatement.executeUpdate();

            // Update the UI with the newly added donor
            textAreaDonors.append("Name: " + name + ", Blood Group: " + bloodGroup + ", Address: " + address + ", Phone Number: " + phoneNumber + "\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteDonor(String bloodGroup) {
        try {
            deleteStatement.setString(1, bloodGroup);
            int rowsDeleted = deleteStatement.executeUpdate();

            if (rowsDeleted > 0) {
                // Update the UI by reloading the donors
                reloadDonors();
            } else {
                JOptionPane.showMessageDialog(this, "No donors found with the given blood group!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchDonor(String bloodGroup) {
        try {
            searchStatement.setString(1, bloodGroup);
            ResultSet resultSet = searchStatement.executeQuery();

            textAreaDonors.setText("");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");

                textAreaDonors.append("ID: " + id + ", Name: " + name + ", Blood Group: " + bloodGroup + ", Address: " + address + ", Phone Number: " + phoneNumber + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDonor(String name, String bloodGroup, String address, String phoneNumber) {
        try {
            updateStatement.setString(1, name);
            updateStatement.setString(2, bloodGroup);
            updateStatement.setString(3, address);
            updateStatement.setString(4, phoneNumber);
            updateStatement.setString(5, bloodGroup);
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // Update the UI by reloading the donors
                reloadDonors();
            } else {
                JOptionPane.showMessageDialog(this, "No donors found with the given blood group!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayDonors() {
        // Clear the text area
        textAreaDonors.setText("");

        // Reload and display all donors
        loadDonors();
    }

    private void clearInputFields() {
        textFieldName.setText("");
        textFieldBloodGroup.setText("");
        textFieldAddress.setText("");
        textFieldPhoneNumber.setText("");
    }

    private void reloadDonors() {
        textAreaDonors.setText("");
        loadDonors();
    }

     // Main method
    public static void main(String[] args) {
         // Run the application on the event dispatch thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BloodDonationManagementSystem();
            }
        });
    }
}

