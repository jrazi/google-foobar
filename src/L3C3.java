import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class L3C3 {
	
	public static class RationalNumber implements Comparable<RationalNumber> {
		public final long nominator;
		public final long denominator;
		
		public static final RationalNumber ONE = new RationalNumber(1, 1);
		public static final RationalNumber ZERO = new RationalNumber(0, 1);
		
		public RationalNumber(long nominator, long denominator) {
			if (denominator < 0) {
				denominator *= -1;
				nominator *= -1;
			}
			
			this.nominator = nominator;
			this.denominator = denominator;
		}
		

		public RationalNumber(long nominator) {
			this(nominator, 1);
		}

		public RationalNumber add(RationalNumber r) {
			long denominatorLcm = lcm(this.denominator, r.denominator);
			RationalNumber a = this.normalizeWithDenominator(denominatorLcm);
			RationalNumber b = r.normalizeWithDenominator(denominatorLcm);

			return new RationalNumber(a.nominator + b.nominator, denominatorLcm).getMostSimpleForm();
		}
		
		public RationalNumber multiply(RationalNumber r) {
			RationalNumber a = new RationalNumber(this.nominator, r.denominator).getMostSimpleForm();
			RationalNumber b = new RationalNumber(r.nominator, this.denominator).getMostSimpleForm();

			return new RationalNumber(a.nominator * b.nominator, a.denominator * b.denominator);
		}
		
		public RationalNumber divide(RationalNumber r) {
			return this.multiply(r.invserse());
		}
		
		public RationalNumber negative() {
			return new RationalNumber(-this.nominator, this.denominator);
		}

		public RationalNumber invserse() {
			return new RationalNumber(this.denominator, this.nominator);
		}

		public RationalNumber normalizeWithDenominator(long denominator) {
			if (this.denominator == denominator)
				return this;
						
			long factor = denominator/this.denominator;
			
			return new RationalNumber(this.nominator * factor, this.denominator * factor);
		}
		
		public RationalNumber getMostSimpleForm() {
			if (nominator == 0) {
				return RationalNumber.ZERO;
			}
			
			long gcd = gcd(nominator, denominator);
			
			return new RationalNumber(nominator/gcd, denominator/gcd);
		}
		
		public boolean isNegative() {
			boolean nNeg = nominator < 0;
			boolean dNeg = denominator < 0;

			return nNeg ^ dNeg;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof RationalNumber) {
				RationalNumber num = (RationalNumber) o;
				RationalNumber sub = this.add(num.negative());
				return sub.nominator == 0;
			}
			return false;
		}

		@Override
		public int compareTo(RationalNumber r) {
			RationalNumber sub = this.add(r.negative());
			if (sub.equals(RationalNumber.ZERO))
				return 0;
			if (sub.isNegative())
				return -1;
			return 1;
		}
	}
	
    public static int[] solution(int[][] m) {
    	
    	int[][] equationMatrix = buildEquationMatrix(m);
    	RationalNumber[] sol = solveEquationMatrix(equationMatrix);
    	    	
    	long[] solArray = getSolutionArray(sol);
    	
        return Arrays.stream(solArray).mapToInt(v -> (int) v).toArray();
    }
    

    private static long[] getSolutionArray(RationalNumber[] equationSolution) {
    	if (equationSolution.length == 0)
    		return new long[] {1, 1};
    	
    	RationalNumber sum = RationalNumber.ZERO;
    	for (RationalNumber r : equationSolution) {
    		sum = sum.add(r);
    	}
    	
    	RationalNumber[] normalizedSol = new RationalNumber[equationSolution.length];
    	
    	for(int i = 0; i < equationSolution.length; i++) {
    		normalizedSol[i] = equationSolution[i].divide(sum).getMostSimpleForm();
    	}

    	long[] denominators = Arrays.stream(normalizedSol).mapToLong(r -> r.denominator).toArray();
    	long lcm = lcm(denominators);
    	
    	
    	List<Long> solArray = new ArrayList<>();
    	Arrays.stream(normalizedSol)
    			.map(r -> r.normalizeWithDenominator(lcm))
    			.forEach(r -> solArray.add(r.nominator));
    	
    	solArray.add(lcm);
    	
    	return solArray.stream().mapToLong(n -> n).toArray();
    }

    private static long lcm(long[] nums) {
    	return lcm(nums, 0);
    }
    
    private static long lcm(long[] nums, int strInclusive) {    
    	if (nums.length == 1)
    		return nums[0];
    	
    	if (strInclusive == nums.length - 2)
    		return lcm(nums[strInclusive], nums[strInclusive+1]);
    	
    	return lcm(nums[strInclusive], lcm(nums, strInclusive+1));    	
    }

    public static long lcm(long a, long b) {
    	return Math.abs(a*b)/gcd(a, b);
    }

    public static long gcd(long a, long b) {
    	a = Math.abs(a);
    	b = Math.abs(b);
    	
    	if (a == b)
    		return a;
    	
    	if (a < b) {
    		long t = b;
    		b = a;
    		a = t;
    	}
    	
    	if (a % b == 0)
    		return b;
    	
    	return gcd(b, a % b);    	
    }
    
    private static int[][] buildEquationMatrix(int[][] stateTransitionMatrix) {    	
    	int[] nonTerminalNodeIndexes = IntStream.range(0, stateTransitionMatrix.length)
    				.filter(i -> Arrays.stream(stateTransitionMatrix[i]).anyMatch(val -> val > 0))
    				.toArray();
    	
    	int[] terminalNodeIndexes = IntStream.range(0, stateTransitionMatrix.length)
				.filter(i -> !Arrays.stream(nonTerminalNodeIndexes).anyMatch(index -> index == i))
				.toArray();
    
    	return buildEquationMatrix(stateTransitionMatrix, nonTerminalNodeIndexes, terminalNodeIndexes);
    }

    private static int[][] buildEquationMatrix(int[][] stateTransitionMatrix, int[] nonTerminalNodeIndexes,  int[] terminalNodeIndexes) {
    	int[][] matrix = new int[nonTerminalNodeIndexes.length][nonTerminalNodeIndexes.length + terminalNodeIndexes.length];
    	
    	for (int i = 0; i < nonTerminalNodeIndexes.length; i++) {
    		int index = nonTerminalNodeIndexes[i];
    		int[] stateMatrixRow = stateTransitionMatrix[index];
    		int stateMatrixRowSum = Arrays.stream(stateMatrixRow).sum();
    		int[] row = matrix[i];
    		
    		for (int j = 0; j < nonTerminalNodeIndexes.length; j++) {
    			int colIndex = nonTerminalNodeIndexes[j];
    			
    			row[j] += stateMatrixRow[colIndex];
    		}
    		
    		row[i] += -1 * stateMatrixRowSum;
    		
    		for (int j = 0; j < terminalNodeIndexes.length; j++) {
    			int terminalNodeIndex = terminalNodeIndexes[j];
    			
        		int terminalNodeDirectTransitionValue = stateMatrixRow[terminalNodeIndex];
        		row[nonTerminalNodeIndexes.length + j] = terminalNodeDirectTransitionValue;
    		}

    	}
    	
    	return matrix;
    }
    
    
    private static RationalNumber[] solveEquationMatrix(int[][] equationMatrix) {
    	RationalNumber[][] rMatrix = Arrays.stream(equationMatrix)
    			.map(row -> Arrays.stream(row).boxed().map(n -> new RationalNumber(n)).toArray(size -> new RationalNumber[size]))
    			.toArray(size -> new RationalNumber[size][]);
    	
    	return solveEquationMatrix(rMatrix);
    }
    
    private static RationalNumber[] solveEquationMatrix(RationalNumber[][] equationMatrix) {    
    	if (equationMatrix.length == 0)
    		return new RationalNumber[0];
    	
    	for (int i = 0; i < equationMatrix.length; i++) {
    		reduceRow(equationMatrix, i);
    	}
    	
    	int nonTerminalCount = equationMatrix.length;
    	int terminalCount = equationMatrix[0].length - nonTerminalCount;
    	RationalNumber[] sol = new RationalNumber[terminalCount];
    	
    	rowOpMakePivotOne(equationMatrix, 0);
    	
    	for (int i = 0; i < terminalCount; i++) {
    		sol[i] = equationMatrix[0][nonTerminalCount + i].negative();
    	}

    	return sol;
    }

    private static void reduceRow(RationalNumber[][] equationMatrix, int rowIndex) {
    	
    	replaceRowToHaveMaximumPivotValue(equationMatrix, rowIndex);
    	
    	RationalNumber pivotValue = equationMatrix[rowIndex][rowIndex];
    	
    	if (pivotValue.equals(RationalNumber.ZERO))
    		return;
    	
    	for (int i = 0; i < equationMatrix.length; i++) {
    		if (i == rowIndex)
    			continue;
    		
    		RationalNumber r = equationMatrix[i][rowIndex];
    		
    		if (r.equals(RationalNumber.ZERO))
    			continue;
    		
    		RationalNumber factor = r.negative().divide(pivotValue);
    		rowOpAddRowToAnother(equationMatrix, factor, rowIndex, i);
    	}
    }
    
    private static void replaceRowToHaveMaximumPivotValue(RationalNumber[][] equationMatrix, int rowIndex) {
    	int maxPivotRowIndex = IntStream.range(rowIndex, equationMatrix.length)
    			.boxed()
    			.max((r1, r2) -> equationMatrix[r1][rowIndex].compareTo(equationMatrix[r1][rowIndex]))
    			.orElse(rowIndex);
    	
    	if (maxPivotRowIndex == rowIndex)
    		return;
    	
		rowOpSwapRows(equationMatrix, rowIndex, maxPivotRowIndex);
    }

    private static void rowOpSwapRows(RationalNumber[][] equationMatrix, int firstRowIndex, int secondRowIndex) {
    	RationalNumber[] firstRow = equationMatrix[firstRowIndex];
    	
    	equationMatrix[firstRowIndex] = equationMatrix[secondRowIndex];
    	equationMatrix[secondRowIndex] = firstRow;
    }

    private static void rowOpAddRowToAnother(RationalNumber[][] equationMatrix, RationalNumber additionFactor, int sourceRowIndex, int targeRowIndex) {
    	
    	for (int i = 0; i < equationMatrix[targeRowIndex].length; i++) {
    		RationalNumber cell = equationMatrix[targeRowIndex][i];
    		RationalNumber addition = additionFactor.multiply(equationMatrix[sourceRowIndex][i]);
    		
    		equationMatrix[targeRowIndex][i] = cell.add(addition); 
    	}
    }

    private static void rowOpMakePivotOne(RationalNumber[][] equationMatrix, int rowIndex) {
    	RationalNumber pivot = equationMatrix[rowIndex][rowIndex];
    	if (pivot.equals(RationalNumber.ZERO))
    		return;
    	
    	RationalNumber factor = pivot.invserse();
    	
    	for (int i = 0; i < equationMatrix[rowIndex].length; i++) {
    		equationMatrix[rowIndex][i] = equationMatrix[rowIndex][i].multiply(factor);
    	}
    }
    
}
