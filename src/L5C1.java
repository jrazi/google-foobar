import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class L5C1 {

    private static int height;
    private static int width;
    private static boolean[][] finalState;


    private static int countPossibleStates() {
        List<Integer> prevValidRows = getValidRows(0);
        Map<Integer, Integer> prevValidStatesForEachRowCount = new HashMap<>();

        for (Integer rowId : prevValidRows) {
            prevValidStatesForEachRowCount.put(rowId, 1);
        }

        for (int i = 1; i < height; i++) {
            List<Integer> validRows = getValidRows(i);
            Map<Integer, Integer> validStatesForEachRowCount = new HashMap<>();

            for (int j = 0; j < validRows.size(); j++) {
                int rowId = validRows.get(j);
                validStatesForEachRowCount.put(rowId, 0);

                for (int k = 0; k < prevValidRows.size(); k++) {
                    int prevRowId = prevValidRows.get(k);
                    if (isRowsCombinationValid(prevRowId, rowId, i-1)) {
                        int prevCount = prevValidStatesForEachRowCount.get(prevRowId);
                        int count = validStatesForEachRowCount.get(rowId);
                        validStatesForEachRowCount.put(rowId, prevCount + count);
                    }
                }
            }

            prevValidRows = validRows;
            prevValidStatesForEachRowCount = validStatesForEachRowCount;
        }

        return prevValidStatesForEachRowCount.values().stream().mapToInt(c -> c).sum();
    }

    private static boolean isRowsCombinationValid(int c0Id, int c1Id, int finalStateInd) {
        boolean[] r0 = rowIdToRow(c0Id);
        boolean[] r1 = rowIdToRow(c1Id);

        for (int i = 0; i < width - 1; i++) {
            int a = r0[i] ? 1 : 0;
            int b = r0[i+1] ? 1 : 0;
            int c = r1[i] ? 1 : 0;
            int d = r1[i+1] ? 1 : 0;

            boolean squareValue = (a + b + c + d) == 1;
            if (squareValue != finalState[finalStateInd][i])
                return false;
        }
        return true;
    }

    private static List<Integer> getValidRows(int rowIndex) {
        int possibleRows = (int) Math.pow(2, width);
        List<Integer> validRows = new ArrayList<>();

        int finalStateInd = rowIndex == (height-1) ? rowIndex - 1 : rowIndex;

        boolean[] finalStateRow = finalState[finalStateInd];

        for (int rowId = 0; rowId < possibleRows; rowId++) {
            boolean[] rowToCheck = rowIdToRow(rowId);
            boolean valid = true;
            for (int j = 0; j < finalStateRow.length; j++) {
                if (finalStateRow[j] && (rowToCheck[j] && rowToCheck[j+1])) {
                    valid = false;
                    break;
                }
            }
            if (valid)
                validRows.add(rowId);
        }

        return validRows;
    }

    private static boolean[] rowIdToRow(int rowId) {
        boolean[] row = new boolean[width];
        for (int i = 0; i < width; i++) {
            int p = 1 << i;
            boolean value = (p & rowId) == p;
            row[i] = value;
        }
        return row;
    }

    public static int solution(boolean[][] g) {

        // We use the transpose matrix because we mainly want to access the original graph by column. It's easier to access by row for a simple 2d array
        boolean[][] transpose = new boolean[g[0].length][g.length];

        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[i].length; j++) {
                transpose[j][i] = g[i][j];
            }
        }
        
        finalState = transpose;
        height = finalState.length + 1;
        width = finalState[0].length + 1;

        return countPossibleStates();
    }
}
