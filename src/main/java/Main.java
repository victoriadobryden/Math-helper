import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/equations";
        String user = "root";
        String password = "Vikchik232Vikchik232";
        String equation = "";
        Scanner sc = new Scanner(System.in);
        equation = EquationChecker.checkIfAllowable(sc, equation);
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to the MySQL server successfully.");
        int equationId = DatabaseInsertion.insertionEquationToDB(connection, equation);
        String root0;
        while (true) {
            System.out.println("Enter a root of this equation: \nexit - press k");
            root0 = sc.nextLine();
            if ("k".equals(root0)) break;
            DatabaseInsertion.checkAndInsertRoot(root0, equation, connection, equationId);
        }
        ArrayList<Double> al = new ArrayList<>();
        System.out.println("Enter roots, I will show you list of equations with at least one of this roots. " +
                "stop - enter z. Roots:");
        String input;
        while (!(input = sc.nextLine()).equals("z")) {
            try {
                double root = Double.parseDouble(input);
                al.add(root);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or 'z' to stop.");
            }
        }
        DatabaseSearchByRoots.searchEquaWithOneRoot(connection, al);
        System.out.println("Enter count of roots, I will show you equations with this number of roots. Count: ");
        String k = sc.nextLine();
        DatabaseSearchByRoots.searchEquaWithExactCountRoots(connection, k);
        connection.close();
        sc.close();
    }
}