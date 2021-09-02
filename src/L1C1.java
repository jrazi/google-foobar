import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class L1C1 {

    public static int solution(int[] x, int[] y) {
        Set<Integer> sharedIDs = new HashSet<>();

        int[] listWithAdditionalID = x.length > y.length ? x : y;
        int[] otherList = listWithAdditionalID == x ? y : x;

        for (int id : otherList) {
            sharedIDs.add(id);
        }

        for (int id : listWithAdditionalID) {
            if (!sharedIDs.contains(id))
                return id;
        }

        return -1;
    }


}
