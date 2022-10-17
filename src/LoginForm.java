import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog {
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;



    //OKIENKO<
    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login"); //tytuł okna dialogowego//
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);  //Ustawia lokalizację okna względem określonego składnika zgodnie z następującymi scenariuszami//
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //>





        //BTN ZALOGUJ<
        btnOK.addActionListener(new ActionListener() {      // dodaj funkcje po nacjsnieciu
            @Override
            public void actionPerformed(ActionEvent e) {       // Implementuje metode do klasy
                String email = tfEmail.getText();       //Pobiera war pola tekstowego email
                String password = String.valueOf(pfPassword.getPassword()); //Zwraca ciąg znaków reprezentujący argument tablicy char//

                user = getAuthenticatedUser(email, password);

                if (user != null) {     // Jesli nie jest rowna wyczysc okno
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,   // wyswietl okno komunikatu blędu pC-wybierz okno
                            "Coś nie tak wpisane",
                            "Spróbuj ponownie",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //>




    //BTN ANULUJ<
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);


    }







    // ZAPYTANIE DO SERWERA

    public User user;
    private User getAuthenticatedUser(String email, String password) {
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost:3310/bh";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Connected to database successfully...

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            conn.close();

        }catch(Exception e){        //PRZEJMIJ DANE
            e.printStackTrace();
        }


        return user;  //zwróć wartosc
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if (user != null) {
            System.out.println("Successful Authentication of: " + user.name);
            System.out.println("          Email: " + user.email);
            System.out.println("          Phone: " + user.phone);
            System.out.println("          Address: " + user.address);
        }
        else {
            System.out.println("Authentication canceled");
        }
    }
}
