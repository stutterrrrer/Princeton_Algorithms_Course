import java.util.Scanner;
import java.util.Stack;

public class TwoStackArithmetic {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Stack<String> operatorsStack = new Stack<>();
		Stack<Double> valuesStack = new Stack<>();

		while (scanner.hasNext()) {
			String next = scanner.next();
			switch (next) {
				case "(":
					// ignore left parenthesis
					break;
				case "+": case "*":
					operatorsStack.push(next);
					break;
				case ")":
					String op = operatorsStack.pop();
					if (op.equals("+")) {
						valuesStack.push(valuesStack.pop() + valuesStack.pop());
					} else if (op.equals("*")) {
						valuesStack.push(valuesStack.pop() * valuesStack.pop());
					}
					break;
				default: // if it's none of the above operators it's a value (simplified)
					valuesStack.push(Double.parseDouble(next));
					break;
			}
		}
		System.out.println(valuesStack.pop());
	}
}