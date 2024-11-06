// Teams.java
public class Teams {
    private String name;
    private int matches;
    private int wins;
    private int losses;
    private int ties;
    private int noResults;
    private int points;
    private double netRunRate;
    private int totalRunsScored;
    private double totalOversFaced;
    private int totalRunsConceded;
    private double totalOversBowled;

    public Teams(String name) {
        this.name = name;
        this.matches = 0;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.noResults = 0;
        this.points = 0;
        this.netRunRate = 0.0;
        this.totalRunsScored = 0;
        this.totalOversFaced = 0.0;
        this.totalRunsConceded = 0;
        this.totalOversBowled = 0.0;
    }

    public void updateMatch(String result, int runsScored, double oversFaced, int runsConceded, double oversBowled) {
        matches++;
        totalRunsScored += runsScored;
        totalOversFaced += oversFaced;
        totalRunsConceded += runsConceded;
        totalOversBowled += oversBowled;

        switch (result) {
            case "Win":
                wins++;
                points += 2;
                break;
            case "Lose":
                losses++;
                break;
            case "Tie":
                ties++;
                points += 1;
                break;
            case "No Result":
                noResults++;
                points += 1;
                break;
        }
        updateNetRunRate();
    }

    public void reverseMatch(String result, int runsScored, double oversFaced, int runsConceded, double oversBowled) {
        matches--;
        totalRunsScored -= runsScored;
        totalOversFaced -= oversFaced;
        totalRunsConceded -= runsConceded;
        totalOversBowled -= oversBowled;

        switch (result) {
            case "Win":
                wins--;
                points -= 2;
                break;
            case "Lose":
                losses--;
                break;
            case "Tie":
                ties--;
                points -= 1;
                break;
            case "No Result":
                noResults--;
                points -= 1;
                break;
        }
        updateNetRunRate();
    }

    private void updateNetRunRate() {
        if (totalOversBowled != 0) {
            netRunRate = ((double) totalRunsScored / totalOversFaced) - ((double) totalRunsConceded / totalOversBowled);
        } else {
            netRunRate = 0;
        }
    }

    // Getters for each attribute
    public String getName() {
        return name;
    }

    public int getMatches() {
        return matches;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTies() {
        return ties;
    }

    public int getNoResults() {
        return noResults;
    }

    public int getPoints() {
        return points;
    }

    public double getNetRunRate() {
        return Math.round(netRunRate * 100.0) / 100.0; // Rounds to two decimal places
    }
}

