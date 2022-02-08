public class SubstringKMP {

	private static final int RADIX = 256;
	// don't need to store the input text - save space.
	// since KMP doesn't retrace, we can read the input text as a stream - character by character
	private String pattern; // pattern to search for
	private int[][] dfa; // deterministic finite-state automation

	private void buildDFA(String pattern) {
		this.pattern = pattern;
		int patternLength = pattern.length();
		dfa = new int[RADIX][patternLength];

		dfa[pattern.charAt(0)][0] = 1;
		// sofpc: state omitting first pattern character
		for (int sofpc = 0, j = 1; j < patternLength; j++) {
			for (int curChar = 0; curChar < RADIX; curChar++)
				// 1. copy over the mismatch transitions from sofpc column (state)
				dfa[curChar][j] = dfa[curChar][sofpc];
			// 2. match transition:
			dfa[pattern.charAt(j)][j] = j + 1;
			// 3. update sofpc:
			sofpc = dfa[pattern.charAt(j)][sofpc];
		}
	}
}
