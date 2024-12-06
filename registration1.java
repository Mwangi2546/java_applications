import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class registration1 {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Registration Form");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout()); // Use GridBagLayout for precise alignment

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Add spacing

        // Create components
        JLabel titleLabel = new JLabel("Registration Form");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);

        JLabel mobileLabel = new JLabel("Mobile:");
        JTextField mobileField = new JTextField(20);

        JLabel genderLabel = new JLabel("Gender:");
        JRadioButton maleButton = new JRadioButton("Male");
        JRadioButton femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);

        JLabel dobLabel = new JLabel("Date of Birth:");
        JComboBox<String> dayCombo = new JComboBox<>(generateDays());
        JComboBox<String> monthCombo = new JComboBox<>(generateMonths());
        JComboBox<String> yearCombo = new JComboBox<>(generateYears());

        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dobPanel.add(dayCombo);
        dobPanel.add(monthCombo);
        dobPanel.add(yearCombo);

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(20);

        JCheckBox termsCheckBox = new JCheckBox("I agree to the Terms and Conditions");
        JButton submitButton = new JButton("Submit");
        JButton resetButton = new JButton("Reset");

        // Add components to frame with proper alignment
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        frame.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        frame.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        frame.add(mobileLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        frame.add(mobileField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        frame.add(genderLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        frame.add(genderPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        frame.add(dobLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        frame.add(dobPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        frame.add(addressLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        frame.add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        frame.add(termsCheckBox, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1;
        frame.add(submitButton, gbc);
        gbc.gridx = 1; gbc.gridy = 7;
        frame.add(resetButton, gbc);

        // Button functionalities
        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String mobile = mobileField.getText().trim();
            String gender = maleButton.isSelected() ? "Male" : femaleButton.isSelected() ? "Female" : "";
            String dob = dayCombo.getSelectedItem() + "-" + monthCombo.getSelectedItem() + "-" + yearCombo.getSelectedItem();
            String address = addressField.getText().trim();

            // Validate fields
            if (name.isEmpty() || mobile.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Name, Mobile, and Address cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!mobile.matches("\\d{10,12}")) {
                JOptionPane.showMessageDialog(frame, "Mobile number must be between 10 to 12 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!termsCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(frame, "You must agree to the terms and conditions.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Convert date to MySQL format
            try {
                String formattedDob = convertToDateFormat(dob);
                DatabaseConnection.insertData(name, mobile, gender, formattedDob, address);
                JOptionPane.showMessageDialog(frame, "Form Submitted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                resetFields(nameField, mobileField, genderGroup, dayCombo, monthCombo, yearCombo, addressField, termsCheckBox);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error submitting form: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        resetButton.addActionListener(e -> resetFields(nameField, mobileField, genderGroup, dayCombo, monthCombo, yearCombo, addressField, termsCheckBox));

        // Make the frame visible
        frame.setVisible(true);
    }

    private static void resetFields(JTextField nameField, JTextField mobileField, ButtonGroup genderGroup,
                                    JComboBox<String> dayCombo, JComboBox<String> monthCombo, JComboBox<String> yearCombo,
                                    JTextField addressField, JCheckBox termsCheckBox) {
        nameField.setText("");
        mobileField.setText("");
        genderGroup.clearSelection();
        dayCombo.setSelectedIndex(0);
        monthCombo.setSelectedIndex(0);
        yearCombo.setSelectedIndex(0);
        addressField.setText("");
        termsCheckBox.setSelected(false);
    }

    private static String convertToDateFormat(String dob) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("d-MMMM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        return outputFormat.format(inputFormat.parse(dob));
    }

    private static String[] generateDays() {
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) days[i - 1] = String.valueOf(i);
        return days;
    }

    private static String[] generateMonths() {
        return new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    private static String[] generateYears() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[100];
        for (int i = 0; i < 100; i++) years[i] = String.valueOf(currentYear - i);
        return years;
    }
}

class DatabaseConnection {
    public static void insertData(String name, String mobile, String gender, String dob, String address) throws Exception {
        // Database connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/form"; // Replace with your database name
        String username = "root"; // Replace with your MySQL username
        String password = ""; // Replace with your MySQL password
        Connection conn = DriverManager.getConnection(url, username, password);

        String sql = "INSERT INTO registration (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, mobile);
        statement.setString(3, gender);
        statement.setString(4, dob);
        statement.setString(5, address);
        statement.executeUpdate();

        conn.close();
    }
}
