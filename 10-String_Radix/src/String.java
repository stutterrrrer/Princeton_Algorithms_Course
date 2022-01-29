public class String {
	private final char[] charArr; // reference to the underlying char array
	private final int offset; // charArr[offset] = this (sub)string's first char.
			public final int length; // public instance variable length.

	private int hash; // cache of hashCode() - because hashCode calculation for strings takes too long to be repeated.

	public char charAt(int i) {
		return charArr[offset + i]; // note the offset.
	}

	public String subString(int from, int to) {
		return new String(offset + from, to - from, charArr);
	}

	private String(int offset, int length, char[] charArr) {
		this.charArr = charArr; // don't create new array, simply copy the reference to the same underlying array.

		this.length = length;
		this.offset = offset;
	}
}
