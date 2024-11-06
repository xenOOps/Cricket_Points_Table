// MatchEntry.java
public class MatchEntry {
    String team1;
    String team2;
    String result;
    String winType;
    int runsScored;
    double oversFaced;
    int runsConceded;
    double oversBowled;
    int margin;
    int team1Points;
    double team1NetRunRate;
    int team2Points;
    double team2NetRunRate;

    public MatchEntry(String team1, String team2, String result, String winType, int runsScored, double oversFaced,
                      int runsConceded, double oversBowled, int margin, int team1Points, double team1NetRunRate,
                      int team2Points, double team2NetRunRate) {
        this.team1 = team1;
        this.team2 = team2;
        this.result = result;
        this.winType = winType;
        this.runsScored = runsScored;
        this.oversFaced = oversFaced;
        this.runsConceded = runsConceded;
        this.oversBowled = oversBowled;
        this.margin = margin;
        this.team1Points = team1Points;
        this.team1NetRunRate = team1NetRunRate;
        this.team2Points = team2Points;
        this.team2NetRunRate = team2NetRunRate;
    }
}
