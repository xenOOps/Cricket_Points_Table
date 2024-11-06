//PointsTable.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class PointsTable {
    private JTable pointsTable;
    private JTable matchHistory;
    private DefaultTableModel pointsTableModel;
    private DefaultTableModel matchHistoryModel;
    private JTextField oversBowledField, oversFacedField, runsScoredField;
    private JTextField runsConcededField, marginField;
    private JComboBox<String> team1ComboBox, team2ComboBox, winTypeComboBox, resultComboBox;
    private Map<String, Teams> teamMap = new HashMap<>();
    private Stack<MatchEntry> matchHistoryStack = new Stack<>();
    private int matchCounter = 1;
    public PointsTable() {
        JFrame frame = new JFrame("WC 2024 Points Table");
        ImageIcon image=new ImageIcon("icc-men-s-t20-world-cup-logo-2024-free-vector.jpg");
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(image.getImage());
        frame.setLayout(null);

        String[] pointsColumns = {"Teams", "Matches", "Win", "Loss", "Tie", "No Result", "Points", "NRR"};
        pointsTableModel = new DefaultTableModel(pointsColumns, 0);
        pointsTable = new JTable(pointsTableModel);
        JScrollPane pointsScrollPane = new JScrollPane(pointsTable);
        pointsScrollPane.setBounds(20, 20, 640, 150);
        frame.add(pointsScrollPane);

        String[] matchColumns = {"Match No.", "Team 1", "Team 2", "Result & Margin"};
        matchHistoryModel = new DefaultTableModel(matchColumns, 0);
        matchHistory = new JTable(matchHistoryModel);
        JScrollPane matchHistoryScrollPane = new JScrollPane(matchHistory);
        matchHistoryScrollPane.setBounds(20, 180, 750, 150);
        frame.add(matchHistoryScrollPane);

        // ComboBoxes, TextFields, and Labels setup here...
        JLabel team1label=new JLabel("Team 1");
        team1label.setBounds(20,350,80,30);
        frame.add(team1label);
        team1ComboBox=new JComboBox<>();
        team1ComboBox.setBounds(120,350,120,30);
        frame.add(team1ComboBox);

        JLabel team2label=new JLabel("Team 2");
        team2label.setBounds(280,350,80,30);
        frame.add(team2label);
        team2ComboBox=new JComboBox<>();
        team2ComboBox.setBounds(370,350,120,30);
        frame.add(team2ComboBox);

        JLabel resultLabel=new JLabel("Result");
        resultLabel.setBounds(20,400,80,30);
        frame.add(resultLabel);
        resultComboBox=new JComboBox<>(new String[]{"Team 1 won","Team 2 won","Tie","No Result"});
        resultComboBox.setBounds(120,400,120,30);
        frame.add(resultComboBox);

        JLabel winTypeLabel=new JLabel("Win Type");
        winTypeLabel.setBounds(280,350,80,130);
        frame.add(winTypeLabel);
        winTypeComboBox=new JComboBox<>(new String[]{"By Wickets","By Runs","N/A"});
        winTypeComboBox.setBounds(370,400,120,30);
        frame.add(winTypeComboBox);

        JLabel runsScoredLabel=new JLabel("Runs Scored");
        runsScoredLabel.setBounds(20,450,80,30);
        frame.add(runsScoredLabel);
        runsScoredField=new JTextField();
        runsScoredField.setBounds(120,450,120,30);
        frame.add(runsScoredField);

        JLabel oversFacedLabel=new JLabel("Overs faced");
        oversFacedLabel.setBounds(280,400,80,130);
        frame.add(oversFacedLabel);
        oversFacedField=new JTextField();
        oversFacedField.setBounds(370,450,120,30);
        frame.add(oversFacedField);

        JLabel runsConcededLabel=new JLabel("Runs Conceded");
        runsConcededLabel.setBounds(20,500,100,30);
        frame.add(runsConcededLabel);
        runsConcededField=new JTextField();
        runsConcededField.setBounds(120,500,120,30);
        frame.add(runsConcededField);

        JLabel oversBowledLabel=new JLabel("Overs Bowled");
        oversBowledLabel.setBounds(280,450,80,130);
        frame.add(oversBowledLabel);
        oversBowledField=new JTextField();
        oversBowledField.setBounds(370,500,120,30);
        frame.add(oversBowledField);

        JLabel marginLabel=new JLabel("Margin");
        marginLabel.setBounds(20,550,80,30);
        frame.add(marginLabel);
        marginField=new JTextField();
        marginField.setBounds(120,550,120,30);
        frame.add(marginField);


        JButton addMatchButton = new JButton("Add Match");
        addMatchButton.setBounds(230, 600, 130, 25);
        frame.add(addMatchButton);
        addMatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String team1 = (String) team1ComboBox.getSelectedItem();
                String team2 = (String) team2ComboBox.getSelectedItem();
                String result = (String) resultComboBox.getSelectedItem();
                String winType = (String) winTypeComboBox.getSelectedItem();
                int runsScored, runsConceded, margin;
                double oversFaced, oversBowled;
                try {
                    runsScored = Integer.parseInt(runsScoredField.getText());
                    oversFaced = Double.parseDouble(oversFacedField.getText());
                    runsConceded = Integer.parseInt(runsConcededField.getText());
                    oversBowled = Double.parseDouble(oversBowledField.getText());
                    margin = Integer.parseInt(marginField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid entries for match details");
                    return;
                }
                if (team1.equals(team2)) {
                    JOptionPane.showMessageDialog(null, "One team cannot play against itself");
                    return;
                }
                addMatch(team1, team2, result, winType, runsScored, oversFaced, runsConceded, oversBowled, margin);
            }
        });

        JButton undoButton = new JButton("Undo");
        undoButton.setBounds(380, 600, 130, 25);
        frame.add(undoButton);
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoLastMatch();
            }
        });

        initializeTeams();
        frame.setVisible(true);
    }

    private void initializeTeams() {
        String[] teams = {"India", "Australia", "Bangladesh", "England", "New Zealand", "South Africa", "Pakistan", "Sri Lanka", "West Indies", "Afghanistan"};
        for (String team : teams) {
            teamMap.put(team, new Teams(team));
            team1ComboBox.addItem(team);
            team2ComboBox.addItem(team);
        }
        refreshPointsTable();
    }

    private void addMatch(String team1, String team2, String result, String winType, int runsScored, double oversFaced, int runsConceded, double oversBowled, int margin) {
        Teams t1 = teamMap.get(team1);
        Teams t2 = teamMap.get(team2);

        // Save current state for undo
        matchHistoryStack.push(new MatchEntry(team1, team2, result, winType, runsScored, oversFaced, runsConceded, oversBowled, margin, t1.getPoints(), t1.getNetRunRate(), t2.getPoints(), t2.getNetRunRate()));

        updatePointsTable(t1, t2, result, winType, runsScored, oversFaced, runsConceded, oversBowled, margin);
        addToMatchHistory(team1, team2, result, winType, margin);
    }

    private void updatePointsTable(Teams t1, Teams t2, String result, String winType, int runsScored, double oversFaced, int runsConceded, double oversBowled, int margin) {
        if ("Team 1 won".equals(result)) {
            t1.updateMatch("Win", runsScored, oversFaced, runsConceded, oversBowled);
            t2.updateMatch("Lose", runsConceded, oversBowled, runsScored, oversFaced);
        } else if ("Team 2 won".equals(result)) {
            t2.updateMatch("Win", runsScored, oversFaced, runsConceded, oversBowled);
            t1.updateMatch("Lose", runsConceded, oversBowled, runsScored, oversFaced);
        } else if ("Tie".equals(result)) {
            t1.updateMatch("Tie", runsScored, oversFaced, runsConceded, oversBowled);
            t2.updateMatch("Tie", runsConceded, oversBowled, runsScored, oversFaced);
        } else if ("No Result".equals(result)) {
            t1.updateMatch("No Result", 0, 0.0, 0, 0.0);
            t2.updateMatch("No Result", 0, 0.0, 0, 0.0);
        }
        refreshPointsTable();
    }

    private void addToMatchHistory(String team1, String team2, String result, String winType, int margin) {
        String resultStr;
        if ("Team 1 won".equals(result)) {
            resultStr = team1 + " Won by " + margin + (winType.equals("By Runs") ? " runs" : " wickets");
        } else if ("Team 2 won".equals(result)) {
            resultStr = team2 + " Won by " + margin + (winType.equals("By Runs") ? " runs" : " wickets");
        } else if ("Tie".equals(result)) {
            resultStr = "Match Tied";
        } else {
            resultStr = "No Result. Match Abandoned";
        }
        matchHistoryModel.addRow(new Object[]{matchCounter++, team1, team2, resultStr});
    }

    private void undoLastMatch() {
        if (matchHistoryStack.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No match to undo");
            return;
        }
        MatchEntry lastMatch = matchHistoryStack.pop();
        Teams t1 = teamMap.get(lastMatch.team1);
        Teams t2 = teamMap.get(lastMatch.team2);



        // Reverse the match for team 1
        String resultForT1, resultForT2;

        if (lastMatch.result.equals("Team 1 won")) {
            resultForT1 = "Win";
            resultForT2 = "Lose";
        } else if (lastMatch.result.equals("Team 2 won")) {
            resultForT1 = "Lose";
            resultForT2 = "Win";
        } else { // "Tie" or "No Result"
            resultForT1 = lastMatch.result;
            resultForT2 = lastMatch.result;
        }

        // Reverse the match for team 1
        t1.reverseMatch(resultForT1, lastMatch.runsScored, lastMatch.oversFaced, lastMatch.runsConceded, lastMatch.oversBowled);

        // Reverse the match for team 2 with opposite result
        t2.reverseMatch(resultForT2, lastMatch.runsConceded, lastMatch.oversBowled, lastMatch.runsScored, lastMatch.oversFaced);

        matchHistoryModel.removeRow(matchHistoryModel.getRowCount() - 1);
        matchCounter--;
        refreshPointsTable();
    }

    private void refreshPointsTable() {
        pointsTableModel.setRowCount(0);
        List<Teams> sortedTeams = new ArrayList<>(teamMap.values());
        sortedTeams.sort((t1, t2) -> {
            if (t1.getPoints() != t2.getPoints()) {
                return Integer.compare(t2.getPoints(), t1.getPoints());
            } else {
                return Double.compare(t2.getNetRunRate(), t1.getNetRunRate());
            }
        });

        for (Teams teams : sortedTeams) {
            pointsTableModel.addRow(new Object[]{
                    teams.getName(),
                    teams.getMatches(),
                    teams.getWins(),
                    teams.getLosses(),
                    teams.getTies(),
                    teams.getNoResults(),
                    teams.getPoints(),
                    String.format("%.2f", teams.getNetRunRate())
            });
        }
    }
    public static void main(String[] args) {
        new PointsTable();
    }
}
