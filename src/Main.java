import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;
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
        runL2C2();
        runL3C1();
        runL3C2();
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

    private static void runTests(Class<?> clazz, List<Map<String, Object>> testCases, BiPredicate<Object, Object> resultCheck) {
        try {
            Method method = Arrays.stream(clazz.getMethods()).filter(m -> m.getName().equals("solution")).findFirst().get();

            int passedCount = 0;
            for (Map<String, Object> testCase : testCases) {
                List<Object> args = (List<Object>) testCase.get("args");
                Object result = method.invoke(null, args.toArray());
                boolean passed = resultCheck.test(testCase.get("sol"), result);

                if (!passed) {
                    System.out.println(String.format("FAILED! %d tests were passed before for %s", passedCount, (clazz.getSimpleName())));
                    System.out.println(String.format("TEST FAILED! %s expected: %s, actual: %s", clazz.getSimpleName(), testCase.get("sol").toString(), result.toString()));
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

    private static List<Map<String, Object>> getL2C2TestCases() {


        Map<String, Object> t1 = new HashMap<>();
        t1.put("args", Arrays.asList("<<>><"));
        t1.put("sol", 4);

        Map<String, Object> t2 = new HashMap<>();
        t2.put("args", Arrays.asList(">----<"));
        t2.put("sol", 2);

        Map<String, Object> t3 = new HashMap<>();
        t3.put("args", Arrays.asList("--->-><-><-->-"));
        t3.put("sol", 10);


        return Arrays.asList(t1, t2, t3);
    }

    private static List<Map<String, Object>> getL3C1TestCases() {


        Map<String, Object> t1 = new HashMap<>();
        t1.put("args", Arrays.asList(new int[]{1, 1, 1}));
        t1.put("sol", 1);

        Map<String, Object> t2 = new HashMap<>();
        t2.put("args", Arrays.asList(new int[]{1, 2, 3, 4, 5, 6}));
        t2.put("sol", 3);

        Map<String, Object> t3 = new HashMap<>();
        t3.put("args", Arrays.asList(new int[]{4, 2, 5, 1, 6}));
        t3.put("sol", 0);

        Map<String, Object> t4 = new HashMap<>();
        t4.put("args", Arrays.asList(new int[]{1, 2, 5, 1, 6, 10}));
        t4.put("sol", 5);


        Map<String, Object> t5 = new HashMap<>();
        t5.put("args", Arrays.asList(new int[]{3, 18, 36, 54, 72, 144, 216, 108, 288}));
        t5.put("sol", 34);

        Map<String, Object> t6 = new HashMap<>();
        t6.put("args", Arrays.asList(new int[]{8, 16}));
        t6.put("sol", 0);

        Map<String, Object> t7 = new HashMap<>();
        t7.put("args", Arrays.asList(new int[]{8, 16, 32}));
        t7.put("sol", 1);

        Map<String, Object> t8 = new HashMap<>();
        t8.put("args", Arrays.asList(new int[]{1, 3, 6, 12, 4, 8, 4, 16, 8}));
        t8.put("sol", 16);

        Map<String, Object> t9 = new HashMap<>();
        t9.put("args", Arrays.asList(new int[]{24, 12, 8, 6, 3, 2, 1}));
        t9.put("sol", 0);

        Map<String, Object> t10 = new HashMap<>();
        t10.put("args", Arrays.asList(new int[]{24, 12, 8, 6, 3, 2, 1, 1}));
        t10.put("sol", 0);

        Map<String, Object> t11 = new HashMap<>();
        t11.put("args", Arrays.asList(new int[]{24, 12, 8, 6, 3, 2, 1, 1, 1}));
        t11.put("sol", 1);



        int elemCount = 100;

        Map<String, Object> randoms = new HashMap<>();
        int[] randNums = new int[elemCount];
        int[] allSameNums = new int[elemCount];
        int[] twoPowers = new int[10];
        int[] threePowers = new int[10];

        int[] primeFactorMultiplications = new int[elemCount];

        for (int i = 0; i < 10; i++) {
            twoPowers[i] = (int) Math.pow(2, i);
            threePowers[i] = (int) Math.pow(3, i);
        }

        for (int i = 0; i < elemCount; i++) {
            int n = new Random().nextInt() % 1000;
            randNums[i] = n;
            allSameNums[i] = 999999;
            int twoPower = new Random().nextInt() % 5;
            int threePower = new Random().nextInt() % 5;
            int fivePower = new Random().nextInt() % 1;

            int mul = (int) (Math.pow(2,twoPower) * Math.pow(3, threePower) * Math.pow(5, fivePower));
            primeFactorMultiplications[i] = mul > 0 ? mul : 1;
        }
        randoms.put("args", Arrays.asList(randNums));
        randoms.put("sol", 1);


        Map<String, Object> t12 = new HashMap<>();
        t12.put("args", Arrays.asList(allSameNums));
        t12.put("sol", 161700);

        Map<String, Object> t13 = new HashMap<>();
        t13.put("args", Arrays.asList((twoPowers)));
        t13.put("sol", 120);

        Map<String, Object> t14 = new HashMap<>();
        t14.put("args", Arrays.asList(primeFactorMultiplications));
        t14.put("sol", 1);

        Map<String, Object> t15 = new HashMap<>();
        t15.put("args", Arrays.asList(new int[]{4, 8, 4, 8, 16}));
        t15.put("sol", 7);

        Map<String, Object> t16 = new HashMap<>();
        t16.put("args", Arrays.asList(new int[]{1, 5, 8, 2, 1, 1, 144, 1, 54, 1}));
        t16.put("sol", 22);

        Map<String, Object> t17 = new HashMap<>();
        t17.put("args", Arrays.asList(new int[]{1, 1, 2, 1, 40, 9, 1, 27, 27, 1}));
        t17.put("sol", 42);

        Map<String, Object> t18 = new HashMap<>();
        t18.put("args", Arrays.asList(new int[]{648, 4, 18, 3, 1, 9, 81, 81, 24, 3}));
        t18.put("sol", 7);

        Map<String, Object> t19 = new HashMap<>();
        t19.put("args", Arrays.asList(new int[]{1, 13, 1, 1, 3, 324, 1, 5, 36, 10, 108, 1, 432, 1, 1, 1, 20, 1, 5, 36}));
        t19.put("sol", 277);

        Map<String, Object> t20 = new HashMap<>();
        t20.put("args", Arrays.asList(new int[]{4, 21, 7, 14, 8, 56, 56, 42}));
        t20.put("sol", 9);

        Map<String, Object> t21 = new HashMap<>();
        t21.put("args", Arrays.asList(new int[]{2, 2, 2, 2, 4, 4, 5, 6, 8, 8, 8}));
        t21.put("sol", 90);

        Map<String, Object> t23 = new HashMap<>();
        t23.put("args", Arrays.asList(new int[]{4, 21, 7, 14, 56, 8, 56, 4, 42}));
        t23.put("sol", 7);


        return Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t15, t16, t17, t18, t19, t20, t21, t23);
    }

    private static List<Map<String, Object>> getL3C2TestCases() {


        Map<String, Object> t1 = new HashMap<>();
        t1.put("args", Arrays.asList("4"));
        t1.put("sol", 2);

        Map<String, Object> t2 = new HashMap<>();
        t2.put("args", Arrays.asList("15"));
        t2.put("sol", 5);

        Map<String, Object> t3 = new HashMap<>();
        t3.put("args", Arrays.asList("1024"));
        t3.put("sol", 10);


        Map<String, Object> t4 = new HashMap<>();
        t4.put("args", Arrays.asList("1022"));
        t4.put("sol", 11);

        Map<String, Object> t5 = new HashMap<>();
        t5.put("args", Arrays.asList("1021"));
        t5.put("sol", 12);

        Map<String, Object> t6 = new HashMap<>();
        t6.put("args", Arrays.asList("1020"));
        t6.put("sol", 11);

        Map<String, Object> t7 = new HashMap<>();
        t7.put("args", Arrays.asList("1"));
        t7.put("sol", 0);


        Map<String, Object> t8 = new HashMap<>();
        t8.put("args", Arrays.asList("2"));
        t8.put("sol", 1);


        Map<String, Object> t9 = new HashMap<>();
        t9.put("args", Arrays.asList("3"));
        t9.put("sol", 2);

        Map<String, Object> t10 = new HashMap<>();
        t10.put("args", Arrays.asList("5"));
        t10.put("sol", 3);

        String twoPow10000 = "19950631168807583848837421626835850838234968318861924548520089498529438830221946631919961684036194597899331129423209124271556491349413781117593785932096323957855730046793794526765246551266059895520550086918193311542508608460618104685509074866089624888090489894838009253941633257850621568309473902556912388065225096643874441046759871626985453222868538161694315775629640762836880760732228535091641476183956381458969463899410840960536267821064621427333394036525565649530603142680234969400335934316651459297773279665775606172582031407994198179607378245683762280037302885487251900834464581454650557929601414833921615734588139257095379769119277800826957735674444123062018757836325502728323789270710373802866393031428133241401624195671690574061419654342324638801248856147305207431992259611796250130992860241708340807605932320161268492288496255841312844061536738951487114256315111089745514203313820202931640957596464756010405845841566072044962867016515061920631004186422275908670900574606417856951911456055068251250406007519842261898059237118054444788072906395242548339221982707404473162376760846613033778706039803413197133493654622700563169937455508241780972810983291314403571877524768509857276937926433221599399876886660808368837838027643282775172273657572744784112294389733810861607423253291974813120197604178281965697475898164531258434135959862784130128185406283476649088690521047580882615823961985770122407044330583075869039319604603404973156583208672105913300903752823415539745394397715257455290510212310947321610753474825740775273986348298498340756937955646638621874569499279016572103701364433135817214311791398222983845847334440270964182851005072927748364550578634501100852987812389473928699540834346158807043959118985815145779177143619698728131459483783202081474982171858011389071228250905826817436220577475921417653715687725614904582904992461028630081535583308130101987675856234343538955409175623400844887526162643568648833519463720377293240094456246923254350400678027273837755376406726898636241037491410966718557050759098100246789880178271925953381282421954028302759408448955014676668389697996886241636313376393903373455801407636741877711055384225739499110186468219696581651485130494222369947714763069155468217682876200362777257723781365331611196811280792669481887201298643660768551639860534602297871557517947385246369446923087894265948217008051120322365496288169035739121368338393591756418733850510970271613915439590991598154654417336311656936031122249937969999226781732358023111862644575299135758175008199839236284615249881088960232244362173771618086357015468484058622329792853875623486556440536962622018963571028812361567512543338303270029097668650568557157505516727518899194129711337690149916181315171544007728650573189557450920330185304847113818315407324053319038462084036421763703911550639789000742853672196280903477974533320468368795868580237952218629120080742819551317948157624448298518461509704888027274721574688131594750409732115080498190455803416826949787141316063210686391511681774304792596709376";

        String twoPow10000Plus1 = new BigInteger(twoPow10000).add(BigInteger.ONE).toString();
        
        Map<String, Object> t11 = new HashMap<>();
        t11.put("args", Arrays.asList(twoPow10000));
        t11.put("sol", 10000);

        Map<String, Object> t12 = new HashMap<>();
        t12.put("args", Arrays.asList(twoPow10000Plus1));
        t12.put("sol", 10001);

        return Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }


}
