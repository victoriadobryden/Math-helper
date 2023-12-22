import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

class DatabaseSearchByRootsTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void searchEquaWithOneRoot_ExecutesQueryWithRoots() throws SQLException {
        ArrayList<Double> roots = new ArrayList<>(Arrays.asList(2.0, -2.0));
        when(mockResultSet.next()).thenReturn(true, true, false);

        DatabaseSearchByRoots.searchEquaWithOneRoot(mockConnection, roots);

        verify(mockStatement, times(1)).executeQuery();
        verify(mockStatement, times(2)).setDouble(anyInt(), anyDouble());
        verify(mockResultSet, times(2)).getString("expression");
    }

    @Test
    void searchEquaWithExactCountRoots_ExecutesQueryWithCount() throws SQLException {
        String k = "1";
        when(mockResultSet.next()).thenReturn(true, true, false);

        DatabaseSearchByRoots.searchEquaWithExactCountRoots(mockConnection, k);

        verify(mockStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).getString("expression");
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockConnection, mockStatement, mockResultSet);
    }

    public static class DatabaseSearchByRoots {
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
}
