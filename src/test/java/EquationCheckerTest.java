import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EquationCheckerTest {

    @Test
    void testHasBracketProblems_WithCorrectBrackets_ReturnsFalse() {
        String equation = "(x+1)*(x-1)";
        char[] equas = equation.toCharArray();

        boolean result = EquationChecker.hasBracketProblems(equation, equas);

        assertFalse(result, "Equation with correct brackets should not have problems.");
    }

    @Test
    void testHasBracketProblems_WithMismatchedBrackets_ReturnsTrue() {
        String equation = "(x+1)*(x-1))";
        char[] equas = equation.toCharArray();

        boolean result = EquationChecker.hasBracketProblems(equation, equas);

        assertTrue(result, "Equation with mismatched brackets should have problems.");
    }

    @Test
    void testIsValidEquation_WithValidEquation_ReturnsTrue() {
        String equation = "x+1=x-1";

        char[] equas = equation.toCharArray();

        boolean result = EquationChecker.isValidEquation(equation, equas);

        assertTrue(result, "Valid equation should return true.");
    }

    @Test
    void testIsValidEquation_WithInvalidCharacters_ReturnsFalse() {
        String equation = "x+1=x-1%";

        char[] equas = equation.toCharArray();

        boolean result = EquationChecker.isValidEquation(equation, equas);

        assertFalse(result, "Equation with invalid characters should return false.");
    }
}

