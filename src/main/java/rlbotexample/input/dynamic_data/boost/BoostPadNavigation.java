package rlbotexample.input.dynamic_data.boost;

import rlbotexample.input.dynamic_data.DataPacket;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BoostPadNavigation {

    public static List<BoostPad> dijkstraPathFinding(BoostPad start, BoostPad end, DataPacket input) {
        List<BoostPad> reverseBoostPadPath = new ArrayList<>();
        reverseBoostPadPath.add(end);
        List<Double> distanceArray = new ArrayList<>();
        Map<Integer, Double> priorityQueue = new HashMap<>();
        AtomicInteger currentNodeIndex = new AtomicInteger(start.boostId);
        Map<Integer, Integer> nodeLink = new HashMap<>();

        for(int i = 0; i < BoostManager.boostPads.size(); i++) {
            distanceArray.add(Double.MAX_VALUE);
            nodeLink.put(i, i);
        }
        distanceArray.set(start.boostId, 0d);
        priorityQueue.put(start.boostId, 0d);

        while(priorityQueue.size() > 0) {
            Integer newIndex = priorityQueue
                    .entrySet()
                    .stream()
                    .findFirst()
                    .get()
                    .getKey();
            currentNodeIndex.set(newIndex);
            priorityQueue.forEach((key, value) -> {
                if(value < priorityQueue.get(currentNodeIndex.get())) {
                    currentNodeIndex.set(key);
                }
            });
            BoostPad visitingPad = BoostManager.boostPads.get(currentNodeIndex.get());

            BoostManager.boostPads.get(currentNodeIndex.get()).neighbours
                    .forEach(visitedPad -> {
                        double pathLength = distanceArray.get(visitingPad.boostId) + visit(visitingPad, visitedPad, input);
                        if(pathLength < distanceArray.get(visitedPad.boostId)) {
                            nodeLink.replace(visitedPad.boostId, visitingPad.boostId);
                            distanceArray.set(visitedPad.boostId, pathLength);
                            priorityQueue.put(visitedPad.boostId, distanceArray.get(visitedPad.boostId));
                        }
            });
            priorityQueue.remove(currentNodeIndex.get());
        }

        // assuming there is a solution to the graph (I think...)
        Integer padId = end.boostId;
        while(reverseBoostPadPath.get(reverseBoostPadPath.size()-1) != start) {
            padId = nodeLink.get(padId);
            reverseBoostPadPath.add(BoostManager.boostPads.get(padId));
            //nodeLink.remove(padId);
        }

        return reverseOrder(reverseBoostPadPath);
    }

    private static List<BoostPad> reverseOrder(List<BoostPad> pads) {
        List<BoostPad> result = new ArrayList<>();

        for(int i = 1; i <= pads.size(); i++) {
            int size = pads.size();
            result.add(pads.get(size - i));
        }

        return result;
    }

    private static double visit(BoostPad visitingPad, BoostPad visitedPad, DataPacket input) {
        if(!visitedPad.isActive) {
            return 100000;
        }
        return visitingPad.location.distance(visitedPad.location) - 300;
    }
}
