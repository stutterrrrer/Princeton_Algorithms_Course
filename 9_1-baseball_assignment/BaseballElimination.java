import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BaseballElimination {

    private String[] teamArr;
    private HashMap<String, Integer> teamMap; // key: team name; value: team index.
    private int[] wins;
    private int teamWithMostWinsSoFar;
    private int[] losses;
    private int[] remaining;
    private int[][] matches; // matches[i][j] = matches[j][i] = number of remaining (i vs j) games.

    // graph specific to each target team
    private String eliminationCandidate;
    private int remainingGamesBetweenOtherTeams;
    private FordFulkerson maxFlowMinCut;

    public BaseballElimination(String fileName) {
        In fileReader = new In(fileName);
        int teamCount = Integer.parseInt(fileReader.readLine());
        initialize(teamCount);

        // can assume all input files conform to the specified format
        for (int teamI = 0; teamI < teamCount; teamI++) {
            String[] splits = fileReader.readLine().split(" +");
            teamArr[teamI] = splits[0];
            teamMap.put(splits[0], teamI);
            wins[teamI] = Integer.parseInt(splits[1]);
            teamWithMostWinsSoFar = wins[teamI] >= wins[teamWithMostWinsSoFar] ?
                                    teamI : teamWithMostWinsSoFar;
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

    public boolean isEliminated(String team) {
        if (team == null || !teamMap.containsKey(team)) throw new IllegalArgumentException();
        int candidate = teamMap.get(team);
        if (wins[candidate] + remaining[candidate] < wins[teamWithMostWinsSoFar])
            return true; // trivial elimination - don't need to build network

        if (!team.equals(eliminationCandidate)) buildNewMaxFlowNetwork(team);
        return maxFlowMinCut.value() < remainingGamesBetweenOtherTeams;
    }

    private void buildNewMaxFlowNetwork(String team) {
        eliminationCandidate = team;
        remainingGamesBetweenOtherTeams = 0;
        int teamVs = numberOfTeams() - 1; // target team not in the graph.
        int pairVs = teamVs * (teamVs - 1) / 2;
        int vertices = teamVs + pairVs + 2;
        FlowNetwork graph = new FlowNetwork(vertices);

        int s = teamMap.get(team); // assign target team index as the source vertex.
        int t = vertices - 1; // assign last vertex as the target vertex.
        // vertices:
        // [0, teamCount]: each team vertex + source vertex s
        // [teamCount, t - 1]: assign a vertex to each pair i vs j - by iterating through each combination
        int pairVertex = numberOfTeams();

        for (int i = 0; i < numberOfTeams() - 1; i++) {
            if (i == s) continue;
            for (int j = i + 1; j < numberOfTeams(); j++) {
                if (j == s) continue;
                // from s to this pair-vertex
                final int remainingGamesIJ = against(teamArr[i], teamArr[j]);
                graph.addEdge(new FlowEdge(s, pairVertex, remainingGamesIJ));
                remainingGamesBetweenOtherTeams += remainingGamesIJ;
                // from this pair-vertex to both team vertices
                graph.addEdge(new FlowEdge(pairVertex, i, Double.POSITIVE_INFINITY));
                graph.addEdge(new FlowEdge(pairVertex, j, Double.POSITIVE_INFINITY));

                pairVertex++;
            }
        }
        assert pairVertex == t - 1; // TODO enable assertion.
        // from each team vertex to t:
        for (int i = 0; i < numberOfTeams(); i++)
            if (i != s) {
                int maxWinsForI = wins[s] + remaining[s] - wins[i];
                graph.addEdge(new FlowEdge(i, t, maxWinsForI));
            }

        maxFlowMinCut = new FordFulkerson(graph, s, t);
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !teamMap.containsKey(team)) throw new IllegalArgumentException();
        // calling isEliminated() will build the FF flow network if necessary
        if (!isEliminated(team)) return null;

        int candidate = teamMap.get(team);
        List<String> rTeams = new ArrayList<>();
        if (wins[candidate] + remaining[candidate] < wins[teamWithMostWinsSoFar]) {
            // trivial elimination - don't need a network
            rTeams.add(teamArr[teamWithMostWinsSoFar]);
            return rTeams;
        }

        for (int i = 0; i < numberOfTeams(); i++)
            if (i != candidate && maxFlowMinCut.inCut(i))
                rTeams.add(teamArr[i]);

        return rTeams;
    }

    public int numberOfTeams() {
        return teamMap.size();
    }

    public Iterable<String> teams() {
        List<String> teamsList = new ArrayList<>(numberOfTeams());
        Collections.addAll(teamsList, teamArr);
        return teamsList;
    }

    public int wins(String team) {
        if (team == null || !teamMap.containsKey(team)) throw new IllegalArgumentException();
        return wins[teamMap.get(team)];
    }

    public int losses(String team) {
        if (team == null || !teamMap.containsKey(team)) throw new IllegalArgumentException();
        return losses[teamMap.get(team)];
    }

    public int remaining(String team) {
        if (team == null || !teamMap.containsKey(team)) throw new IllegalArgumentException();
        return remaining[teamMap.get(team)];
    }

    public int against(String team1, String team2) {
        if (team1 == null || team2 == null ||
                !teamMap.containsKey(team1) || !teamMap.containsKey(team2))
            throw new IllegalArgumentException();
        return matches[teamMap.get(team1)][teamMap.get(team2)];
    }

    private void initialize(int teamCount) {
        teamArr = new String[teamCount];
        teamMap = new HashMap<>(teamCount);
        wins = new int[teamCount];
        losses = new int[teamCount];
        remaining = new int[teamCount];
        matches = new int[teamCount][teamCount];
    }


    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        // test the constructor recorded the entries correctly.
        // printStats(division);

        // provided test:
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private static void printStats(BaseballElimination baseball) {
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
