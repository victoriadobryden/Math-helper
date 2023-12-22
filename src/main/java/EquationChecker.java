import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationChecker {
    private static boolean regexMatcher(String regex, String s) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(s);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    static String checkIfAllowable(Scanner sc, String equation) {
        boolean validEquation = false;

        while (!validEquation) {
            System.out.println("Enter equation: ");
            equation = sc.nextLine();
            equation = equation.replaceAll("\\s+", "");
            char[] equas = equation.toCharArray();
            if (hasBracketProblems(equation, equas) || !isValidEquation(equation, equas)) {
                System.out.println("Inappropriate equation, please try again.");
            } else {
                validEquation = true;
                System.out.println("Equation is allowable.");
            }
        }
        return equation;
    }

    static boolean hasBracketProblems(String equation, char[] equas) {
        Stack<Character> s = new Stack<>();
        StringBuilder expInBr = new StringBuilder("");
        boolean expInBrStarted = false;
        for (char eq : equas) {
            if (expInBrStarted) {
                expInBr = expInBr.append(eq);
            }
            if (eq == '(') {
                expInBrStarted = true;
                s.push(eq);
            }
            if (eq == ')') {
                if (s.empty()) {
                    return true;
                }
                if (expInBrStarted) {
                    if (expInBr.toString().length() < 2) {
                        return true;
                    }
                    if (!regexMatcher("^(?![*/+])(?!.*=).*[\\-+*/].*[^*/+-]$", expInBr.toString())) return true;
                }
                s.pop();
            }
        }

        if (!s.empty()) {
            return true;
        }
        return false;
    }

    static boolean isValidEquation(String equation, char [] equas) {
        if (!regexMatcher("^[a-zA-Z0-9\\+\\-\\/\\*\\=\\(\\).]+$", equation) || !regexMatcher("^(?![^=]*=.*=).*x.*=.*|" +
                ".*=.*x.*$", equation) || !regexMatcher("^(?![^=]*=.*=).*x.*=.*|.*=.*x.*$", equation) ||
                !regexMatcher("^(?!.*(\\+\\-|\\-\\+|\\*\\+|\\+\\*|\\+\\/|\\/\\+|\\+\\+|\\*\\*|\\/\\/|\\-\\-|\\*\\/|" +
                        "\\/\\*|\\-\\*|\\-\\/|\\.\\.|=\\+|=\\-|=\\*|=\\/|\\+=|\\-=|\\*=|" +
                        "\\/=))[0-9a-zA-Z+\\-*/=().]+$", equation)) return false;
        if ((equas[equas.length - 1] == '+') || (equas[equas.length - 1] == '-') || (equas[equas.length - 1] == '/') ||
                (equas[equas.length - 1] == '=') || (equas[equas.length - 1] == '*') || (equas[0] == '+') ||
                (equas[0] == '*') || (equas[0] == '/') || (equas[0] == '=')) {
            return false;
        }
        return true;
    }
}
