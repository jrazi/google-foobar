import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class L3C2 {

	private static Map<BigInteger, Object[]> distances;
	
	private static boolean isPowerOfTwo(BigInteger n) {
		return n.and(n.subtract(BigInteger.ONE)).equals(BigInteger.ZERO);
	}
	
	private static int log2ForPowerOf2(BigInteger n) {
		BigInteger m = n;
		int pow = 0;
		
		while (m.compareTo(BigInteger.ONE) > 0) {
			m = m.shiftRight(1);
			pow++;
		}
		
		return pow;
	}

	private static int shortestPathToOne(BigInteger source, int opType) {
		Object[] d = distances.get(source);
		if (d != null)
			return (int) d[0];
		
		if (isPowerOfTwo(source)) {
			d = new Object[] {log2ForPowerOf2(source)};
			distances.put(source, d);
			return (int) d[0];
		}
		
		Object[][] adj = getAdj(source, opType);
		
		int minSp = Integer.MAX_VALUE - 1;
				
		for (Object[] w : adj) {
			int sp = shortestPathToOne((BigInteger) w[0], (int) w[1]);
			if (sp < minSp)
				minSp = sp;
		}
		minSp++;
		
		d = new Object[] {minSp};
		if (opType == 2)
			distances.put(source, d);
		return minSp;
	}
	
	
	private static Object[][] getAdj(BigInteger source, int opType) {
		boolean even = source.mod(BigInteger.TWO).equals(BigInteger.ZERO);
		boolean hasDiv = even;
		boolean hasOtherOps = opType == 2;
		
		int length = hasOtherOps ? (even ? 3 : 2) : (even ? 1 : 0);
		
		Object[][] adj = new Object[length][2];
		int i = 0;
		if (hasDiv)
			adj[i++] = new Object[] {source.divide(BigInteger.TWO), 2};
		if (hasOtherOps) {
			adj[i++] = new Object[] {source.subtract(BigInteger.ONE), 1};
			adj[i++] = new Object[] {source.add(BigInteger.ONE), 0};
		}
		
		return adj;
 	}
	
	public static int shortestPath(BigInteger n) {
		return shortestPathToOne(n, 2);		
	}
	
    public static int solution(String x) {
    	distances = new HashMap<>();
    	BigInteger n = new BigInteger(x);
    	return shortestPath(n);
    }
}
