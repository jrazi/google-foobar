import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;


/**
 *
 * Tests the solutions with simple tests cases. Didn't want to use any extra testing library.
 *
 */
public class Main {

    public static void main(String[] args) {
        runL1C1();
        runL2C1();
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


    private static void runTests(Class<?> clazz, List<Map<String, Object>> testCases, BiPredicate<Object, Object> resultCheck) {
        try {
            Method method = Arrays.stream(clazz.getMethods()).filter(m -> m.getName().equals("solution")).findFirst().get();

            for (Map<String, Object> testCase : testCases) {
                List<Object> args = (List<Object>) testCase.get("args");
                Object result = method.invoke(null, args.toArray());
                boolean passed = resultCheck.test(testCase.get("sol"), result);

                if (!passed) {
                    System.out.println(String.format("TEST FAILED! %s", clazz.getSimpleName()));
                }
            }
            System.out.println("All tests passed for ".concat(clazz.getSimpleName()));

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static List<Map<String, Object>> getC1TestCases() {

        int[] x1 = new int[]{13, 5, 6, 2, 5};
        int[] y1 = new int[]{5, 2, 5, 13};
        int s1 = 6;

        int[] x2 = new int[]{14, 27, 1, 4, 2, 50, 3, 1};
        int[] y2 = new int[]{2, 4, -4, 3, 1, 1, 14, 27, 50};
        int s2 = -4;

        Map<String, Object> t1 = new HashMap<>();
        t1.put("args", Arrays.asList(x1, y1));
        t1.put("sol", s1);

        Map<String, Object> t2 = new HashMap<>();
        t2.put("args", Arrays.asList(x2, y2));
        t2.put("sol", s2);

        return Arrays.asList(t1, t2);
    }

    private static List<Map<String, Object>> getL2C1TestCases() {

        int[] x1 = new int[]{4, 17, 50};
        int[] s1 = new int[]{-1, -1};

        int[] x2 = new int[]{4, 30, 50};
        int[] s2 = new int[]{12, 1};

        Map<String, Object> t1 = new HashMap<>();
        t1.put("args", Arrays.asList(x1));
        t1.put("sol", s1);

        Map<String, Object> t2 = new HashMap<>();
        t2.put("args", Arrays.asList(x2));
        t2.put("sol", s2);


        Map<String, Object> t3 = new HashMap<>();
        t3.put("args", Arrays.asList(new int[]{35, 80}));
        t3.put("sol", new int[]{30, 1});

        Map<String, Object> t4 = new HashMap<>();
        t4.put("args", Arrays.asList(new int[]{10, 97, 184, 264, 375, 498}));
        t4.put("sol", new int[]{184, 3});


        return Arrays.asList(t1, t2, t3, t4);
    }

}
