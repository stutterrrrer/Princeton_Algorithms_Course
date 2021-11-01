public class Main() {

	public List<List<Integer>> knapsack(int[] candidates, int sum) {
		Arrays.sort(candidates);
		List<List<Integer>> allSubsets = new ArrayList<List<Integer>>();
		List<Integer> currentSubsetTracker = new ArrayList<Integer>();
		knapsack(candidates, 0, sum, allSubsets, currentSubsetTracker);
		return allSubsets;
	}

	public void knapsack(int[] candidates, int startIndex, int sum,
						 List<List<Integer>> allSubsets, List<Integer> currentSubsetTracker) {
		if (sum < 0) return;
		if (sum == 0) {
			allSubsets.add(new ArrayList<Integer>(currentSubsetTracker));
			return;
		}

		/* Loop: the many sub-problems at the same level:
		start from 1st number;
		start from 2nd number and leave out 1st;
		start from 3rd number and leave out 1st & 2nd etc. */
		for (int i = startIndex; i < candidates.length; i++) {
			currentSubsetTracker.add(candidates[i]);
			// recursively look for subsets that sum up to (target sum - current number)
			knapsack(candidates, i + 1, sum - candidates[i], allSubsets, currentSubsetTracker);
			// on the way back (backtracking), remove this number to present a clean slate (new tracker) for the next subset.
			currentSubsetTracker.remove(currentSubsetTracker.size() - 1);
			// ignore duplicate numbers (since array is sorted, they'd be at neighboring indices.
			while (i < candidates.length - 1 && candidates[i] == candidates[i + 1]) i++;
		}
		return;
	}
}
