/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
	public static void main(String[] args) {
		String champion = "";
		for (int i = 1; !StdIn.isEmpty(); i++) {
			String input = StdIn.readString();
			champion = StdRandom.bernoulli((double) 1 / i) ? input : champion;
		}
		System.out.println(champion);
	}
}
