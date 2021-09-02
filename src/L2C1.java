import java.util.Arrays;
import java.util.Map;

public class L2C1 {

    public static int[] solution(int[] pegs) {
        int[][] eqMatrixA = buildEquationMatrixA(pegs.length);
        int[][] eqMatrixX = buildEquationMatrixX(pegs);
        double[][] eqMatrixAInverse = matrixInverseSpecialCase(eqMatrixA);

        if (eqMatrixAInverse == null)
            return new int[]{-1, -1};

        double[] solution = multiplySpecialCaseMatrices(eqMatrixAInverse, eqMatrixX);

        int normalizationFactor = (solution.length > 0 && solution[0] > 0) ? 1 : -1;
        for (int i = 0; i < solution.length; i++) {
            solution[i] *= normalizationFactor;
        }

        if (!checkConstraintsOnSolution(solution))
            return new int[]{-1, -1};

        else return doubleToRationalNumber(solution[0]);
    }

    private static int[] doubleToRationalNumber(double x) {
        final double EPSILON = 0.000001;
        if (Math.abs(x - ((double) ((int) x))) < EPSILON)
            return new int[]{(int) x, 1};

        int mFactor = 3*2*4*5;
        int nominator = (int) (x * mFactor + EPSILON);
        int denominator = mFactor;


        int gcd = gcd(nominator, denominator);

        return new int[]{nominator/gcd, denominator/gcd};
    }

    private static int gcd(int n1, int n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcd(n2, n1 % n2);
    }

    private static boolean checkConstraintsOnSolution(double[] solution) {
        if (solution == null || solution.length < 2)
            return false;

        for (double radius : solution) {
            if (radius < 1)
                return false;
        }

        return true;
    }

    private static int[][] buildEquationMatrixA(int n) {
        int[][] matrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            int[] row = matrix[i];
            for (int j = 0; j < n; j++) {
                if (j == i || j == ((i+1) % n))
                    row[j] = 1;
                else row[j] = 0;
            }
        }
        matrix[n-1][n-1] = -2;

        return matrix;
    }

    private static int[][] buildEquationMatrixX(int[] pegs) {
        int[][] matrix = new int[pegs.length][1];

        for (int i = 0; i < pegs.length - 1; i++) {
            matrix[i][0] = pegs[i+1] - pegs[i];
        }
        matrix[pegs.length -1][0] = 0;

        return matrix;
    }

    private static double[][] matrixInverseSpecialCase(int[][] matrix) {
        int n = matrix.length;

        double[][] mergedMatrix = new double[n][n*2];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mergedMatrix[i][j] = matrix[i][j];
                mergedMatrix[i][j + n] = 0;
            }
            mergedMatrix[i][i + n] = 1;
        }

        for (int i = 0; i < n; i++) {
            boolean success = convertRowToIdentityMatrixRow(mergedMatrix, i);
            if (!success)
                return null;
        }

        double[][] inverseMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix[i][j] = mergedMatrix[i][j+n];
            }
        }

        return inverseMatrix;
    }

    private static boolean convertRowToIdentityMatrixRow(double[][] matrix, int rowIndex) {
        int n = matrix.length;
        int indexOfExtraValue = -1;
        for (int i = 0; i < n; i++) {
            if (i != rowIndex && matrix[rowIndex][i] != 0) {
                indexOfExtraValue = i;
                break;
            }
        }
        if (indexOfExtraValue < 0) {
            normalizeCalculatedIdentityRow(matrix, rowIndex);
            return true;
        }
        else {
            for (int i = 1; i < n; i++) {
                int selectedRow = (rowIndex + i) % n;
                double rowFactor = -matrix[rowIndex][selectedRow]/matrix[selectedRow][selectedRow];

                rowOperationSum(rowIndex,  rowFactor,selectedRow, matrix);
            }

            normalizeCalculatedIdentityRow(matrix, rowIndex);
        }

        if (matrix[rowIndex][rowIndex] != 1)
            return false;

        for (int i = 0; i < n; i++) {
            if (i != rowIndex && matrix[rowIndex][i] != 0)
                return false;
        }

        return true;
    }

    private static boolean normalizeCalculatedIdentityRow(double[][] matrix, int rowIndex) {
        if (matrix[rowIndex][rowIndex] == 0)
            return false;

        double factor = 1/(matrix[rowIndex][rowIndex]);

        for (int i = 0; i < matrix[rowIndex].length; i++) {
            matrix[rowIndex][i] *= factor;
        }

        return true;
    }

    private static void rowOperationSum(int mainRowIndex, double rFactor, int rIndex, double[][] matrix) {
        for (int i = 0; i < matrix[mainRowIndex].length; i++) {
            matrix[mainRowIndex][i] += rFactor * matrix[rIndex][i];
        }
    }

    private static double[] multiplySpecialCaseMatrices(double[][] nnMatrix, int[][] n1Matrix) {
        int n = nnMatrix.length;

        double[] solution = new double[n];

        for (int i = 0; i < n; i++) {
            solution[i] = 0;
            for (int j = 0; j < n; j++) {
                solution[i] += nnMatrix[i][j] * n1Matrix[j][0];
            }
        }

        return solution;
    }

}
