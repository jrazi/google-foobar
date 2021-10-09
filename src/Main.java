import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;


/**
 *
 * Tests the solutions with simple tests cases.
 *
 */
public class Main {

    public static void main(String[] args) {
        runL1C1();
        runL2C1();
        runL2C2();
        runL3C1();
        runL3C2();
        runL3C3();
        runL4C1();
        runL4C2();
    }

    private static void runL1C1() {
        List<Map<String, Object>> tests = getC1TestCases();
        runTests(
                L1C1.class,
                tests,
                (expected, actual) -> (int) expected == (int) actual
        );
    }

    private static void runL2C1() {
        List<Map<String, Object>> tests  = getL2C1TestCases();
        runTests(
                L2C1.class,
                tests,
                (expected, actual) -> !IntStream.range(0, 2).anyMatch(i -> ((int[]) expected)[i] != ((int[]) actual)[i])
        );
    }

    private static void runL2C2() {
        List<Map<String, Object>> tests  = getL2C2TestCases();
        runTests(
                L2C2.class,
                tests,
                (expected, actual) -> (int) expected == (int) actual
        );
    }

    private static void runL3C1() {
        List<Map<String, Object>> tests  = getL3C1TestCases();
        runTests(
                L3C1.class,
                tests,
                (expected, actual) -> (int) expected == (int) actual
        );
    }


    private static void runL3C2() {
        List<Map<String, Object>> tests  = getL3C2TestCases();
        runTests(
                L3C2.class,
                tests,
                (expected, actual) -> (int) expected == (int) actual
        );
    }

    private static void runL3C3() {
        List<Map<String, Object>> tests  = getL3C3TestCases();
        runTests(
                L3C3.class,
                tests,
                (expected, actual) -> Arrays.toString((int[]) expected).equals(Arrays.toString((int[]) actual))
        );
    }

    private static void runL4C1() {
        List<Map<String, Object>> tests  = getL4C1TestCases();
        runTests(
                L4C1.class,
                tests,
                (expected, actual) -> (int) expected == (int) actual
        );
    }

    private static void runL4C2() {
        List<Map<String, Object>> tests  = getL4C2TestCases();
        runTests(
                L4C2.class,
                tests,
                (expected, actual) -> (int) expected == (int) actual
        );
    }

    private static void runTests(Class<?> clazz, List<Map<String, Object>> testCases, BiPredicate<Object, Object> resultCheck) {
        try {
            Method method = Arrays.stream(clazz.getMethods()).filter(m -> m.getName().equals("solution")).findFirst().get();

            int passedCount = 0;
            for (Map<String, Object> testCase : testCases) {
                List<Object> args = (List<Object>) testCase.get("args");
                Object result = method.invoke(null, args.toArray());
                boolean passed = resultCheck.test(testCase.get("sol"), result);

                if (!passed) {
                	boolean isArray = (result instanceof int[]);
                	
                	String actual = isArray ? Arrays.toString((int[]) result) : result.toString();
                	String expected = isArray ? Arrays.toString((int[]) testCase.get("sol")) : testCase.get("sol").toString();

                    System.out.println(String.format("FAILED! %d tests were passed before for %s", passedCount, (clazz.getSimpleName())));
                    System.out.println(String.format("TEST FAILED! %s expected: %s, actual: %s", clazz.getSimpleName(), expected, actual));
                    throw new RuntimeException("Tests failed");
                }
                passedCount++;
            }
            System.out.println(String.format("All %d tests passed for %s", testCases.size(), (clazz.getSimpleName())));

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static List<Map<String, Object>> getC1TestCases() {

        return Arrays.asList(
                testCase(Arrays.asList(new int[]{13, 5, 6, 2, 5}, new int[]{5, 2, 5, 13}), 6),
                testCase(Arrays.asList(new int[]{14, 27, 1, 4, 2, 50, 3, 1}, new int[]{2, 4, -4, 3, 1, 1, 14, 27, 50}), -4)
        );

    }

    private static List<Map<String, Object>> getL2C1TestCases() {
        return Arrays.asList(
                testCase(new int[]{4, 17, 50}, new int[]{-1, -1}),
                testCase(new int[]{4, 30, 50}, new int[]{12, 1}),
                testCase(new int[]{35, 80}, new int[]{30, 1}),
                testCase(new int[]{10, 97, 184, 264, 375, 498}, new int[]{184, 3})
        );
    }

    private static List<Map<String, Object>> getL2C2TestCases() {
        return Arrays.asList(
                testCase("<<>><", 4),
                testCase(">----<", 2),
                testCase("--->-><-><-->-", 10)
        );
    }

    private static List<Map<String, Object>> getL3C1TestCases() {
        return Arrays.asList(
                testCase(new int[]{1, 1, 1}, 1),
                testCase(new int[]{1, 2, 3, 4, 5, 6}, 3),
                testCase(new int[]{4, 2, 5, 1, 6}, 0),
                testCase(new int[]{1, 2, 5, 1, 6, 10}, 5),
                testCase(new int[]{3, 18, 36, 54, 72, 144, 216, 108, 288}, 34),
                testCase(new int[]{8, 16}, 0),
                testCase(new int[]{8, 16, 32}, 1),
                testCase(new int[]{1, 3, 6, 12, 4, 8, 4, 16, 8}, 16),
                testCase(new int[]{24, 12, 8, 6, 3, 2, 1}, 0),
                testCase(new int[]{24, 12, 8, 6, 3, 2, 1, 1}, 0),
                testCase(new int[]{24, 12, 8, 6, 3, 2, 1, 1, 1}, 1),
                testCase(new int[]{4, 8, 4, 8, 16}, 7),
                testCase(new int[]{1, 5, 8, 2, 1, 1, 144, 1, 54, 1}, 22),
                testCase(new int[]{1, 1, 2, 1, 40, 9, 1, 27, 27, 1}, 42),
                testCase(new int[]{648, 4, 18, 3, 1, 9, 81, 81, 24, 3}, 7),
                testCase(new int[]{1, 13, 1, 1, 3, 324, 1, 5, 36, 10, 108, 1, 432, 1, 1, 1, 20, 1, 5, 36}, 277),
                testCase(new int[]{4, 21, 7, 14, 8, 56, 56, 42}, 9),
                testCase(new int[]{2, 2, 2, 2, 4, 4, 5, 6, 8, 8, 8}, 90),
                testCase(new int[]{4, 21, 7, 14, 56, 8, 56, 4, 42}, 7)
        );

    }

    private static List<Map<String, Object>> getL3C2TestCases() {

        return Arrays.asList(
                testCase("4", 2),
                testCase("15", 5),
                testCase("1024", 10),
                testCase("1022", 11),
                testCase("1021", 12),
                testCase("1020", 11),
                testCase("1", 0),
                testCase("2", 1),
                testCase("3", 2),
                testCase("5", 3)
        );

    }

    private static List<Map<String, Object>> getL3C3TestCases() {
    	
    	return Arrays.asList(
    			testCase(new int[][] {{0, 2, 1, 0, 0}, {0, 0, 0, 3, 4}, {0, 0, 0, 0, 0}, {0, 0, 0, 0,0}, {0, 0, 0, 0, 0}}, new int[]{7, 6, 8, 21}),
    			testCase(new int[][] {{0, 0, 0, 0,0}, {0, 0, 2, 1, 0}, {4, 0, 0, 0, 3}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}}, new int[]{8, 7, 6, 21}),
    			testCase(new int[][] {{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}}, new int[] {0, 3, 2, 9, 14}),
    			testCase(new int[][] {{2, 1, 5, 9, 4, 1}, {4542524, 245427, 4839, 234234, 24242, 7483}, {19, 94, 77, 85, 9, 0}, {55, 849, 298, 43, 22, 85}, {15, 17, 1, 0, 83, 47}, {0, 0, 0, 0, 0, 0}}, new int[] {1, 1}),
    			testCase(new int[][] {{0, Integer.MAX_VALUE - 2, 0}, {1738, 2, 7}, {0, 0, 0}}, new int[] {1, 1}),
    			testCase(new int[][] {{5, 10}, {0, 0}}, new int[]{1, 1}),
    			testCase(new int[][] {{0, 4, 8}, {24, 8, 16}, {0, 0, 0}}, new int[] {1, 1}),
    			testCase(new int[][] {{0, 4, 0,  0}, {2, 0, 4, 0}, {2, 2, 0, 1}, {0, 0, 0, 0}}, new int[] {1, 1}),
    			testCase(new int[][] {{0, 4, 0,  0}, {0, 0, 0, 0}, {2, 0, 4, 0}, {2, 2, 0, 1}}, new int[] {1, 1}),
    			testCase(new int[][] {{0}}, new int[] {1, 1}),
      			testCase(new int[][] {{0, 0}, {0, 0}}, new int[] {1, 1}),
      			testCase(new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}, new int[] {1, 1})
			);
    }

    private static List<Map<String, Object>> getL4C1TestCases() {
        return Arrays.asList(
                testCase(Arrays.asList(new int[]{3, 2}, new int[]{1, 1}, new int[]{2, 1}, 4), 7),
                testCase(Arrays.asList(new int[]{3, 2}, new int[]{1, 1}, new int[]{2, 1}, 1), 1),
                testCase(Arrays.asList(new int[]{3, 2}, new int[]{1, 1}, new int[]{2, 2}, 1), 0),
                testCase(Arrays.asList(new int[]{3, 2}, new int[]{1, 1}, new int[]{1, 2}, 1), 1),
                testCase(Arrays.asList(new int[]{4, 4}, new int[]{2, 2}, new int[]{3, 3}, 2), 1),
                testCase(Arrays.asList(new int[]{4, 4}, new int[]{3, 3}, new int[]{2, 2}, 2), 1),
                testCase(Arrays.asList(new int[]{100, 100}, new int[]{3, 3}, new int[]{2, 2}, 15), 3),
                testCase(Arrays.asList(new int[]{100, 100}, new int[]{2, 2}, new int[]{3, 3}, 15), 3),
                testCase(Arrays.asList(new int[]{100, 100}, new int[]{3, 4}, new int[]{2, 2}, 20), 4),
                testCase(Arrays.asList(new int[]{1000, 1000}, new int[]{1, 1}, new int[]{1, 2}, 1), 1),
                testCase(Arrays.asList(new int[]{1200, 1200}, new int[]{3, 3}, new int[]{2, 2}, 2), 1),
                testCase(Arrays.asList(new int[]{300,275}, new int[]{150, 150}, new int[]{185,100}, 500), 9),
                testCase(Arrays.asList(new int[]{42, 59}, new int[]{34, 44}, new int[]{6, 34}, 5000), 30904),
                testCase(Arrays.asList(new int[]{10, 2}, new int[]{1, 1}, new int[]{9, 1}, 7), 0),
                testCase(Arrays.asList(new int[]{869, 128}, new int[]{524, 86}, new int[]{288, 28}, 5671), 911),
                testCase(Arrays.asList(new int[]{113, 174}, new int[]{1, 1}, new int[]{3, 2}, 5000), 4026),
                testCase(Arrays.asList(new int[]{300,300}, new int[]{100, 100}, new int[]{140,140}, 5000), 775)
        );
    }


    private static List<Map<String, Object>> getL4C2TestCases() {
        return Arrays.asList(
            testCase(Arrays.asList(new int[]{0, 1}, new int[]{4, 5}, new int[][]{{0, 0, 4, 6, 0, 0}, {0, 0, 5, 2, 0, 0}, {0, 0, 0, 0, 4, 4}, {0, 0, 0, 0, 6, 6}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}}), 16),
            testCase(Arrays.asList(new int[]{0}, new int[]{3}, new int[][]{{0, 7, 0, 0}, {0, 0, 6, 0}, {0, 0, 0, 8}, {9, 0, 0, 0}}), 6),
            testCase(Arrays.asList(new int[]{0}, new int[]{3}, new int[][]{{0, 2, 2, 0}, {0, 0, 1, 2}, {0, 0, 0, 2}, {0, 0, 0, 0}}), 4),
            testCase(Arrays.asList(
                    new int[]{0}, new int[]{5},
                    new int[][]{
                            {0, 10, 10, 0, 0, 0},
                            {0, 0, 2, 4, 8, 0},
                            {0, 0, 0, 0, 9, 0},
                            {0, 0, 0, 0, 0, 10},
                            {0, 0, 0, 6, 0, 10},
                            {0, 0, 0, 0, 0, 0}
                    }),
                    19
            )
        );
    }

    private static Map<String, Object> testCase(List<Object> args, Object expectedOutput) {
        Map<String, Object> testCase = new HashMap<>();
        testCase.put("args", args);
        testCase.put("sol", expectedOutput);

        return testCase;
    }

    private static Map<String, Object> testCase(Object singleArg, Object expectedOutput) {
        Map<String, Object> testCase = new HashMap<>();
        testCase.put("args", Arrays.asList(singleArg));
        testCase.put("sol", expectedOutput);
        
        return testCase;
    }

}
