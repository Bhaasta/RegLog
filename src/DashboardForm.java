import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;

    public DashboardForm() {
        setTitle("Dashborad");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegistredUsers = connectToDatabase();
        if (hasRegistredUsers) {
            //Pokaz login
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if (user != null) {
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        else {
            //show Registration form
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            if (user != null) {
                lbAdmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashboardForm.this);
                User user = registrationForm.user;

                if (user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New user: " + user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }






    private boolean connectToDatabase() {
        boolean hasRegistredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost:3310/";
        final String DB_URL = "jdbc:mysql://localhost:3310/bh";
        final String USERNAME = "root";
        final String PASSWORD = "";


        try{
            //Najpierw połącz się z serwerem MYSQL i utwórz bazę danych, jeśli nie została utworzona
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS bh");
            statement.close();
            conn.close();

            //Po drugie, połącz się z bazą danych i utwórz tabelę „users”, jeśli utworzono cot
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(200) NOT NULL,"
                    + "email VARCHAR(200) NOT NULL UNIQUE,"
                    + "phone VARCHAR(200),"
                    + "address VARCHAR(200),"
                    + "password VARCHAR(200) NOT NULL"
                    + ")";
            statement.executeUpdate(sql);

            //sprawdź czy mamy użytkowników w tabeli users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegistredUsers = true;
                }
            }

            statement.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return hasRegistredUsers;
    }

    public static void main(String[] args) {
        DashboardForm myForm = new DashboardForm();
    }
}
