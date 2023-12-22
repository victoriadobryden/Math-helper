import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseSearchByRoots {
    public static void searchEquaWithOneRoot(Connection connection, ArrayList<Double> roots) throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < roots.size(); i++) {
            sb.append("?");
            if (i < roots.size()-1) {
                sb.append(",");
            }
        }
        String sql = "SELECT DISTINCT e.expression FROM equations e INNER JOIN roots r ON e.id = r.equation_id WHERE r.root_value IN (" + sb + ")";
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < roots.size(); i++) {
            statement.setDouble(i + 1, roots.get(i));
        }
        ResultSet resultSet = statement.executeQuery();
        System.out.println("Equation with at least one of specified roots: ");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("expression"));
        }
    }
    public static void searchEquaWithExactCountRoots (Connection connection, String k) throws SQLException {
        String sql = "SELECT e.expression FROM equations e INNER JOIN roots r ON e.id = r.equation_id GROUP BY e.id," +
                " e.expression HAVING COUNT(r.id) = " + k + " ";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        System.out.println("Equations with exactly " + k + " roots: ");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("expression"));
        }
    }

}
