import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexJava {
	public static void main(String[] args) {
		String regex = args[0];
		String textInput = args[1];
		// compile() creates a regex non-deterministic-finite-state-automaton from the given string regex
		Pattern regexPattern = Pattern.compile(regex);
		// matcher() creates a Matcher that examines input text against the regex pattern
		// and finds all substrings in the input text that matches the regex.
		Matcher matcher = regexPattern.matcher(textInput);
		// find() is similar to "hasNextMatch()" -- like scanner.hasNextLine()
		while (matcher.find())
			// group() is similar to "nextMatch()" -- like scanner.nextLine()
			System.out.println(matcher.group());
	}
}
