import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseInsertionTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockStatement.executeUpdate()).thenReturn(1);
    }

    @Test
    void insertionEquationToDB_SuccessfulInsert_ReturnsValidId() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        int returnedId = DatabaseInsertion.insertionEquationToDB(mockConnection, "x^2=4");

        verify(mockStatement, times(1)).executeUpdate();
        assertEquals(1, returnedId, "The returned ID should be 1 after a successful insert.");
    }

    @Test
    void insertionEquationToDB_NoGeneratedKeys_ThrowsSQLException() throws Exception {
        when(mockResultSet.next()).thenReturn(false);

        Exception exception = assertThrows(SQLException.class, () -> {
            DatabaseInsertion.insertionEquationToDB(mockConnection, "x^2=4");
        });

        String expectedMessage = "Creating equation failed, no ID received.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    public static class DatabaseInsertion {
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
                        throw new SQLException("Creating equation failed, no ID received.");
                    }
                }
            }
            return equationId;
        }
    }
}
