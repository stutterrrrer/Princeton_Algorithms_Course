import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class BaseballElimination {

    private HashMap<String, Integer> teams; // key: team name; value: team index.
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] matches; // matches[i][j] = matches[j][i] = number of remaining (i vs j) games.
    // only create flow-network when checking elimination of team x: graph specific to each team x.

    public BaseballElimination(String fileName) {
        In fileReader = new In(fileName);
        int teamCount = Integer.parseInt(fileReader.readLine());
        initialize(teamCount);

        // can assume all input files conform to the specified format
        for (int teamI = 0; teamI < teamCount; teamI++) {
            String[] splits = fileReader.readLine().split(" +");
            teams.put(splits[0], teamI);
            wins[teamI] = Integer.parseInt(splits[1]);
            losses[teamI] = Integer.parseInt(splits[2]);
            remaining[teamI] = Integer.parseInt(splits[3]);
            // matches[i][<i] will have been filled in previous iterations, and matches[i][i] = 0.
            for (int entry = 4 + teamI + 1; entry < splits.length; entry++) {
                int teamJ = entry - 4;
                final int iVSj = Integer.parseInt(splits[entry]);
                matches[teamI][teamJ] = iVSj;
                matches[teamJ][teamI] = iVSj;
            }
        }
        fileReader.close();
    }

    public int numberOfTeams() {
        return teams.size();
    }

    public Iterable<String> teams() {
        // return the list in the input order
        String[] teamsArr = new String[numberOfTeams()];
        for (String team : teams.keySet()) teamsArr[teams.get(team)] = team;
        return Arrays.asList(teamsArr);
    }

    public int wins(String team) {
        if (team == null || !teams.containsKey(team)) throw new IllegalArgumentException();
        return wins[teams.get(team)];
    }

    public int losses(String team) {
        if (team == null || !teams.containsKey(team)) throw new IllegalArgumentException();
        return losses[teams.get(team)];
    }

    public int remaining(String team) {
        if (team == null || !teams.containsKey(team)) throw new IllegalArgumentException();
        return remaining[teams.get(team)];
    }

    public int against(String team1, String team2) {
        if (team1 == null || team2 == null ||
                !teams.containsKey(team1) || !teams.containsKey(team2))
            throw new IllegalArgumentException();
        return matches[teams.get(team1)][teams.get(team2)];
    }

    private void initialize(int teamCount) {
        teams = new HashMap<>(teamCount);
        wins = new int[teamCount];
        losses = new int[teamCount];
        remaining = new int[teamCount];
        matches = new int[teamCount][teamCount];
    }


    public static void main(String[] args) {
        BaseballElimination baseball = new BaseballElimination(args[0]);
        // test the constructor recorded the entries correctly.
        printStats(baseball);
    }

    public static void printStats(BaseballElimination baseball) {
        final int teamCount = baseball.numberOfTeams();
        System.out.println(teamCount);
        String[] teams = new String[teamCount];
        Iterator<String> iterator = baseball.teams().iterator();
        int longestTeamName = 0;
        for (int i = 0; i < teamCount; i++) {
            String team = iterator.next();
            teams[i] = team;
            longestTeamName = Math.max(team.length(), longestTeamName);
        }

        for (int i = 0; i < teamCount; i++) {
            System.out.printf("%-" + (longestTeamName + 3) + "s", teams[i]);
            System.out.printf("%-3d", baseball.wins(teams[i]));
            System.out.printf("%-3d", baseball.losses(teams[i]));
            System.out.printf("%-6d", baseball.remaining(teams[i]));

            for (int j = 0; j < teamCount; j++)
                System.out.printf("%-3d", baseball.against(teams[i], teams[j]));

            System.out.println();
        }
    }
}
