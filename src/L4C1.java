import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class L4C1 {

    private static class GraphNode {
        public final int[] coordinates;
        public final int[] sourceCoordinates;
        public final int[] targetCoordinates;
        public final int[] topRightCornerCoordinates;
        public final int[] topLeftCornerCoordinates;
        public final int[] bottomRightCornerCoordinates;
        public final int[] bottomLeftCornerCoordinates;

        public GraphNode(int[] coordinates) {
            this.coordinates = coordinates;
            this.sourceCoordinates = calcRootNodeObjectPositionInNewNode(source, coordinates);
            this.targetCoordinates = calcRootNodeObjectPositionInNewNode(target, coordinates);
            this.topRightCornerCoordinates = calcRootNodeObjectPositionInNewNode(new int[]{dimensions[0], dimensions[1]}, coordinates);
            this.topLeftCornerCoordinates = calcRootNodeObjectPositionInNewNode(new int[]{0, dimensions[1]}, coordinates);
            this.bottomRightCornerCoordinates = calcRootNodeObjectPositionInNewNode(new int[]{dimensions[0], 0}, coordinates);
            this.bottomLeftCornerCoordinates = calcRootNodeObjectPositionInNewNode(new int[]{0, 0}, coordinates);
        }


        private static int[] calcRootNodeObjectPositionInNewNode(int[] objectCoordinatesInRoot, int[] nodeCoordinates) {
            boolean sameX = nodeCoordinates[0] % 2 == 0;
            boolean sameY = nodeCoordinates[1] % 2 == 0;
            int[] objectCoordinateInNode = new int[]{
                    sameX ? objectCoordinatesInRoot[0] : dimensions[0] - objectCoordinatesInRoot[0],
                    sameY ? objectCoordinatesInRoot[1] : dimensions[1] - objectCoordinatesInRoot[1]
            };
            int xOffset = dimensions[0]*nodeCoordinates[0];
            int yOffset = dimensions[1]*nodeCoordinates[1];
            return new int[]{xOffset + objectCoordinateInNode[0], yOffset + objectCoordinateInNode[1]};
        }

        @Override
        public int hashCode() {
            return Objects.hashCode("GN_".concat(String.valueOf(coordinates[0]).concat("_").concat(String.valueOf(coordinates[1]))));
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof GraphNode &&
                    ((GraphNode) obj).coordinates[0] == coordinates[0] &&
                    ((GraphNode) obj).coordinates[1] == coordinates[1];
        }

        @Override
        public String toString() {
            return Arrays.toString(coordinates);
        }
    }



    private static int countHits() {

        int hitCount = 0;

        // BFS is done within theta range. The purpose is to limit the size of the line cache, which can be quite high on some inputs.
        // As a result some nodes will be processed multiple times, on each BFS iteration. Since those duplications are limited
        // to the edges of sectors,their effect is fairly insignificant with a moderate step size.
        // Even when theta step was set to maximum(360 degrees), the solution was still accepted.
        final int THETA_STEP = 90;

        nearestTerminatingObjectCache = new HashMap<>();
        Map<String, int[][]> prevBfsCache = new HashMap<>();


        for (double theta = 0; theta < 360; theta+=THETA_STEP) {
            prevBfsCache.forEach((key, value) -> nearestTerminatingObjectCache.put(key, value));

            hitCount += bfsHitsWithinTheta(new double[]{Math.toRadians(theta), Math.toRadians(theta+THETA_STEP)});

            prevBfsCache.putAll(nearestTerminatingObjectCache);
            nearestTerminatingObjectCache.clear();
        }

        return hitCount;
    }


    private static int bfsHitsWithinTheta(double[] thetaInterval) {
        Queue<GraphNode> queue = new ArrayDeque<>();
        Set<String> queueMembers = new HashSet<>();

        queue.add(root);
        queueMembers.add(root.toString());
        int hitCount = 0;
        while(!queue.isEmpty()) {
            GraphNode current = queue.poll();
            boolean withinDistance = isTargetWithinMaxDistance(current);
            if (!withinDistance)
                continue;

            boolean withinTheta = isNodeWithinTheta(current, thetaInterval) || current.equals(root);

            if (!withinTheta)
                continue;

            boolean targetWithinTheta = isTargetWithinTheta(current, thetaInterval);

             if (targetWithinTheta) {
                 hitCount += calculateHits(current);
             }

            List<GraphNode> adj = Arrays.asList(
                    new GraphNode(new int[]{current.coordinates[0] + 1, current.coordinates[1]}),
                    new GraphNode(new int[]{current.coordinates[0] - 1, current.coordinates[1]}),
                    new GraphNode(new int[]{current.coordinates[0], current.coordinates[1] + 1}),
                    new GraphNode(new int[]{current.coordinates[0], current.coordinates[1] - 1})
            );

            adj.stream()
                    .filter(v -> !queueMembers.contains(v.toString()))
                    .forEach(v -> {
                        queue.add(v);
                        queueMembers.add(v.toString());
                    });
        }

        return hitCount;
    }

    private static boolean isNodeWithinTheta(GraphNode node, double[] thetaInterval) {
        List<int[]> corners = Arrays.asList(node.topLeftCornerCoordinates, node.topRightCornerCoordinates, node.bottomLeftCornerCoordinates, node.bottomRightCornerCoordinates);
        List<int[]> cornersSortedByX = corners.stream().sorted(Comparator.comparingInt(c0 -> c0[0])).collect(Collectors.toList());
        List<int[]> cornersSortedByY = corners.stream().sorted(Comparator.comparingInt(c0 -> c0[1])).collect(Collectors.toList());

        return isThetaIntervalCoveringNodeSide(cornersSortedByX.get(0), cornersSortedByX.get(1), thetaInterval) ||
                isThetaIntervalCoveringNodeSide(cornersSortedByX.get(2), cornersSortedByX.get(3), thetaInterval) ||
                isThetaIntervalCoveringNodeSide(cornersSortedByY.get(0), cornersSortedByY.get(1), thetaInterval) ||
                isThetaIntervalCoveringNodeSide(cornersSortedByY.get(2), cornersSortedByY.get(3), thetaInterval);
    }

    private static boolean isThetaIntervalCoveringNodeSide(int[] p0, int[] p1, double[] thetaInterval) {
        double a0 = getAngleWithSource(p0);
        double a1 = getAngleWithSource(p1);

        double t0 = thetaInterval[0];
        double t1 = thetaInterval[1];

        boolean wideAngle = Math.abs(a0 - a1) > Math.PI;
        double aMin = a0 <= a1 && !wideAngle ? a0 : a1;
        double aMax = a0 == aMin ? a1 : a0;

        return isAngleWithinTheta(t0, new double[]{aMin, aMax}) || isAngleWithinTheta(t1, new double[]{aMin, aMax}) ||
                (isAngleWithinTheta(aMin, thetaInterval) && isAngleWithinTheta(aMax, thetaInterval));
    }


    private static boolean isTargetWithinTheta(GraphNode node, double[] thetaInterval) {
        double targetAngle = getAngleWithSource(node.targetCoordinates);

        double t1Normalized = thetaInterval[1] == 2*Math.PI ? 0 : thetaInterval[1];

        return targetAngle == thetaInterval[0] || (targetAngle != t1Normalized &&  isAngleWithinTheta(targetAngle, thetaInterval));
    }

    private static double getAngleWithSource(int[] point) {
        int xd = point[0] - root.sourceCoordinates[0];
        int yd = point[1] - root.sourceCoordinates[1];

        double targetAngle = Math.atan2(yd, xd);
        targetAngle = targetAngle >= 0 ? targetAngle : Math.PI*2 + targetAngle;
        return targetAngle;
    }


    private static boolean isAngleWithinTheta(double angle, double[] thetaInterval) {
        double t0t1 = Math.abs(thetaInterval[1] - thetaInterval[0]);
        double t0a = Math.abs(thetaInterval[0] - angle);
        double t1a = Math.abs(thetaInterval[1] - angle);

        double eps = 0.000000000001;

        return (Math.abs(t0a + t1a - t0t1) < eps) && Math.ulp(t0a + t1a) == Math.ulp(t0t1);
    }

    private static boolean isTargetWithinMaxDistance(GraphNode node) {
        int xd = node.targetCoordinates[0] - root.sourceCoordinates[0];
        int yd = node.targetCoordinates[1] - root.sourceCoordinates[1];

        return maxDistance*maxDistance >= (yd*yd + xd*xd);
    }


    private static int calculateHits(GraphNode node) {
        List<int[]> terminatingObjects = new ArrayList<>(Arrays.asList(
                node.topLeftCornerCoordinates,
                node.topRightCornerCoordinates,
                node.bottomLeftCornerCoordinates,
                node.bottomRightCornerCoordinates
        ));

        if (!node.equals(root))
            terminatingObjects.add(node.sourceCoordinates);

        int hitCount = 0;

        for (int[] point : terminatingObjects) {
            String lineHash = calcLineHashFromSource(point);
            if (lineHash == null)
                continue;

            int[][] nearestTerminatingObjects = getOrCreateTerminatingObjectsInfo(lineHash);

            boolean hitTerminatingObjectBefore = hasHitTerminatingObjectAndThenTrainerInValidRange(nearestTerminatingObjects);

            nearestTerminatingObjects = updateNearestTerminatingObjectForLine(0, lineHash, point);

            if (hitTerminatingObjectBefore || nearestTerminatingObjects == null)
                continue;

            boolean hitTerminatingObject = hasHitTerminatingObjectAndThenTrainerInValidRange(nearestTerminatingObjects);

            if (hitTerminatingObject) {
                hitCount--;
            }
        }

        double[] line = calcLineFromSource(node.targetCoordinates);
        if ((line == null || line[0] == 0)) {
            return node.equals(root) ? hitCount+1 : hitCount;
        }

        String lineHash = calcLineHashFromSource(node.targetCoordinates);
        int[][] nearestTerminatingObjects = getOrCreateTerminatingObjectsInfo(lineHash);

        boolean hitAnythingBefore = hasHitAnyTerminatingObjectBeforePoint(nearestTerminatingObjects, node.targetCoordinates);

        updateNearestTerminatingObjectForLine(1, lineHash, node.targetCoordinates);

        if (!hitAnythingBefore) {
            hitCount++;
        }

        return hitCount;
    }

    private static boolean hasHitAnyTerminatingObjectBeforePoint(int[][] nearestTerminatingObject, int[] point) {
        return nearestTerminatingObject != null &&
                (nearestTerminatingObject[0] != null && getDistanceFromRoot(nearestTerminatingObject[0]) < getDistanceFromRoot(point)) ||
                (nearestTerminatingObject[1] != null && getDistanceFromRoot(nearestTerminatingObject[1]) < getDistanceFromRoot(point));
    }

    private static boolean hasHitTerminatingObjectAndThenTrainerInValidRange(int[][] nearestTerminatingObject) {
        return nearestTerminatingObject != null &&
                nearestTerminatingObject[0] != null &&
                nearestTerminatingObject[1] != null &&
                getDistanceFromRoot(nearestTerminatingObject[0]) < getDistanceFromRoot(nearestTerminatingObject[1]) &&
                getDistanceFromRoot(nearestTerminatingObject[1]) <= maxDistance;
    }


    private static int[][] getOrCreateTerminatingObjectsInfo(String lineHash) {
        return nearestTerminatingObjectCache.getOrDefault(lineHash, new int[2][]);
    }

    private static int[][] updateNearestTerminatingObjectForLine(int index, String lineHash, int[] newTerminatingObject) {
        if (lineHash == null)
            return null;

        int[][] nearestTerminatingObject= nearestTerminatingObjectCache.getOrDefault(lineHash, new int[2][]);

        if (nearestTerminatingObject[index] == null) {
            nearestTerminatingObject[index] = newTerminatingObject;
            nearestTerminatingObjectCache.put(lineHash, nearestTerminatingObject);
            return nearestTerminatingObject;
        }

        boolean newNearest = getDistanceFromRoot(newTerminatingObject) < getDistanceFromRoot(nearestTerminatingObject[index]);

        if (newNearest)
            nearestTerminatingObject[index] = newTerminatingObject;
        return nearestTerminatingObject;
    }

    private static String calcLineHashFromSource(int[] point) {
        double[] line = calcLineFromSource(point);
        boolean dir = root.sourceCoordinates[1] < point[1];
        if (line == null)
            return null;

        return String.format("%s,%s,%d", line[0], line[1], dir ? 1 : 0);
    }

    private static double[] calcLineFromSource(int[] point) {
        return calcLine(root.sourceCoordinates, point);
    }

    private static double[] calcLine(int[] p0, int[] p1) {
        if (p0[0] - p1[0] == 0)
            return null;

        double m = ((double) (p0[1] - p1[1]))/(p0[0] - p1[0]);
        double b = p0[1] - m*p0[0];

        return new double[]{m, b};
    }

    private static double getDistanceFromRoot(int[] point) {
        int[] source = root.sourceCoordinates;
        return Math.sqrt(Math.pow(point[0] - source[0], 2) + Math.pow(point[1] - source[1], 2));
    }


    public static Map<String, int[][]> nearestTerminatingObjectCache;
    public static GraphNode root;
    public static int[] source;
    public static int[] target;
    public static int[] dimensions;
    public static int maxDistance;

    public static int solution(int[] dim, int[] yourPosition, int[] trainerPosition, int distance) {
        source = yourPosition;
        target = trainerPosition;
        maxDistance = distance;
        dimensions = dim;
        root = new GraphNode(new int[]{0, 0});
        return countHits();
    }
}