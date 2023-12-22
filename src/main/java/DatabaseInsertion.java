import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.sql.*;

public class DatabaseInsertion {
    static int insertionEquationToDB(Connection connection, String equation) throws SQLException {
        String sql = "INSERT INTO equations (expression) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, equation);
        int equationId = -1;
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    equationId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating equation failed, no ID recieved.");
                }
            }
            System.out.println("A new equation was inserted successfully!");
        }
        return equationId;
    }

    static void checkAndInsertRoot(String root0, String equation, Connection connection, int equationId) throws SQLException, ArithmeticException {
        double root = Double.parseDouble(root0);
        double tolerance = 0.000000001;
        String[] parts = equation.split("=");
        Expression rightExp = new ExpressionBuilder(parts[0]).variable("x").build().setVariable("x", root);
        Expression leftExp = new ExpressionBuilder(parts[1]).variable("x").build().setVariable("x", root);
        double rightRes = rightExp.evaluate();
        double leftRes = leftExp.evaluate();
        if (Math.abs(rightRes - leftRes) < tolerance) {
            System.out.println("It is a root of equation");
            String sql = "INSERT INTO roots (equation_id, root_value) VALUES(?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, equationId);
            statement.setDouble(2, root);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new root was inserted successfully!");
            }
        } else System.out.println("It is not a root of equation");
    }
}
