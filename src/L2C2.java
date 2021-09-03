
public class L2C2 {

    private static int[][] timePlaceMatrix;

    public static int solution(String s) {
        StringBuilder modifiedS = new StringBuilder("");
        for (int i = 0; i < s.length(); i++) {
            modifiedS.append(s.charAt(i));
            modifiedS.append("-");
        }
        s = modifiedS.toString();

        int hallwayLength = s.length();

        timePlaceMatrix = new int[hallwayLength][hallwayLength];

        for (int i = 0; i < hallwayLength; i++) {
            computeTimePlaceForEmployee(i, s.charAt(i));
        }

        int sum = 0;
        for (int i = 0; i < hallwayLength; i++) {
            for (int j = 0; j < hallwayLength; j++) {
                if (timePlaceMatrix[i][j] == 2)
                    sum += 2;
            }
        }

        return sum;
    }

    private static void computeTimePlaceForEmployee(int initialLocation, char walkingDirection) {
        if (walkingDirection == '-')
            return;

        int step = walkingDirection == '>' ? 1 : -1;
        int endpoint = step == 1 ? timePlaceMatrix.length - 1 : 0;

        for (int i = initialLocation; i*step <= endpoint; i += step) {
            int time = Math.abs(i - initialLocation);
            timePlaceMatrix[time][i] += 1;
        }
    }
}
