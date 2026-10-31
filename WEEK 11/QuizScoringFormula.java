import java.util.ArrayDeque;
import java.util.Deque;

public class QuizScoringFormula {

    static int precedence(char op) {
        return (op == '*' || op == '/') ? 2 : (op == '+' || op == '-') ? 1 : 0;
    }

    static void applyTop(Deque<Integer> operands, Deque<Character> operators) {
        char op = operators.pop();
        int b = operands.pop();
        int a = operands.pop();
        switch (op) {
            case '+': operands.push(a + b); break;
            case '-': operands.push(a - b); break;
            case '*': operands.push(a * b); break;
            default: operands.push(a / b);
        }
    }

    static int evaluate(String expr) {
        Deque<Integer> operands = new ArrayDeque<>();
        Deque<Character> operators = new ArrayDeque<>();
        int i = 0, n = expr.length();
        while (i < n) {
            char ch = expr.charAt(i);
            if (ch == ' ') {
                i++;
            } else if (Character.isDigit(ch)) {
                int num = 0;
                while (i < n && Character.isDigit(expr.charAt(i))) {
                    num = num * 10 + (expr.charAt(i) - '0');
                    i++;
                }
                operands.push(num);
            } else if (ch == '(') {
                operators.push(ch);
                i++;
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    applyTop(operands, operators);
                }
                operators.pop();
                i++;
            } else {
                while (!operators.isEmpty() && operators.peek() != '('
                        && precedence(operators.peek()) >= precedence(ch)) {
                    applyTop(operands, operators);
                }
                operators.push(ch);
                i++;
            }
        }
        while (!operators.isEmpty()) {
            applyTop(operands, operators);
        }
        return operands.pop();
    }

    public static void main(String[] args) {
        System.out.println(evaluate("2 * (3 + 4) - 10 / 5"));
        System.out.println(evaluate("(8 - 2) * (1 + 2) / 4"));
    }
}
