import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class RegexNFA {
	private final char[] regex; // the regex pattern
	private final int reLength; // regex pattern length
	private final Digraph epsilonTransitions;

	public RegexNFA(String regexPattern) {
		// note that the regex pattern is assumed to always be enclosed by parentheses
		// i.e. the states regex[0] and regex[reLength - 1] are respectively '(' and ')'
		regex = regexPattern.toCharArray();
		reLength = regexPattern.length();
		epsilonTransitions = buildEpsilonTransitionsDigraph();
	}

	public boolean matchesRegex(String inputText) {
		Set<Integer> statesReachableBeforeReadingInput = new HashSet<>();

		// finds all reachable states before reading any character of the input text
		// because the vertex 0 of the epsilon transition digraph is assumed to always be a left parenthesis
		DirectedDFS dfs = new DirectedDFS(epsilonTransitions, 0); // mark(visit) all reachable vertices from source vertex 0.
		for (int v = 0; v < epsilonTransitions.V(); v++)
			if (dfs.marked(v)) statesReachableBeforeReadingInput.add(v);

		// rename the variable - can be omitted, but just for easier understanding:
		Set<Integer> statesReachableAfterPreviousInputChar = statesReachableBeforeReadingInput;

		// find all the reachable states after reading the next character of the input text:
		for (int i = 0; i < inputText.length(); i++) {
			Set<Integer> statesReachableAfterCharAtIBeforeEpsilonTransitions = new HashSet<>();
			for (int v : statesReachableAfterPreviousInputChar) {
				// though this is the accept-state: not necessarily a match
				// because we are checking whether this whole inputText matches the regex,
				// not whether this inputText string includes a substring that matches the regex.
				if (v == reLength) continue;
				// successful character match that can move to next state (v + 1):
				if ((regex[v] == inputText.charAt(i) || regex[v] == '.'))
					statesReachableAfterCharAtIBeforeEpsilonTransitions.add(v + 1);
			}

			// use these new reachable states as source vertices,
			// and see if following the epsilon transitions from any of these vertices can reach the accept-state
			if (statesReachableAfterCharAtIBeforeEpsilonTransitions.size() == 0) break;
			dfs = new DirectedDFS(epsilonTransitions, statesReachableAfterCharAtIBeforeEpsilonTransitions);
			// update the source vertices for next character of the input text
			statesReachableAfterPreviousInputChar = new HashSet<>();
			for (int v = 0; v < epsilonTransitions.V(); v++)
				if (dfs.marked(v)) statesReachableAfterPreviousInputChar.add(v);
		}

		// after the last char of the inputText has been read:
		for (int v : statesReachableAfterPreviousInputChar)
			if (v == reLength) return true; // if the last char can reach the accept-state (vertex reLength)
		return false;
	}

	private Digraph buildEpsilonTransitionsDigraph() {
		Digraph epsilonTransGraph = new Digraph(reLength + 1);
		Stack<Integer> parenthesesAndOrOperators = new Stack<>();

		for (int i = 0; i < reLength; i++) {
			// prepare for '*' closure: either this character regex[i] or a left parenthesis before i
			int leftBoundOfClosure = i;

			if (regex[i] == '(' || regex[i] == '|')
				parenthesesAndOrOperators.add(i);
			else if (regex[i] == ')') {
				// the first element popped off might be a '(' or a '|'
				int firstPopped = parenthesesAndOrOperators.pop();
				if (regex[firstPopped] == '|') {
					int or = firstPopped;
					// override the left bound closure from this char regex[i] to the corresponding left parenthesis
					leftBoundOfClosure = parenthesesAndOrOperators.pop();
					// add the 2 epsilon transition edges for the "or" operator:
					epsilonTransGraph.addEdge(leftBoundOfClosure, or + 1);
					epsilonTransGraph.addEdge(or, i);
				} else // override the left bound closure from this char regex[i] to the corresponding left parenthesis
					leftBoundOfClosure = firstPopped;
			}

			// 1-character lookahead to check for '*' closure
			if (i < reLength - 1 && regex[i + 1] == '*') {
				// add the 2 (of 3) epsilon transition edges of a '*' closure that isn't simply pointing to next vertex
				int starMetaChar = i + 1;
				epsilonTransGraph.addEdge(leftBoundOfClosure, starMetaChar);
				epsilonTransGraph.addEdge(starMetaChar, leftBoundOfClosure);
			}

			// 3 of the 4 meta-characters we consider in this simple implementation:
			// (save for '|' who doesn't have an epsilon transition pointing to the next char)
			if (regex[i] == '(' || regex[i] == ')' || regex[i] == '*')
				epsilonTransGraph.addEdge(i, i + 1);
		}
		return epsilonTransGraph;
	}

	public static void main(String[] args) {
		final String regexPattern = "((A*B|AC)D)";
		RegexNFA regexNFA = new RegexNFA(regexPattern);
		final String oneInput = "AAAABD";
		System.out.println(regexNFA.matchesRegex(oneInput));
		System.out.println(regexNFA.matchesRegex("BD"));
		System.out.println(regexNFA.matchesRegex("ACD"));
		System.out.println(regexNFA.matchesRegex("AAAC"));

		System.out.println(oneInput.matches(regexPattern));
	}
}