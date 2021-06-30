package rlbotexample.input.dynamic_data.boost;

import rlbot.cppinterop.RLBotDll;
import rlbot.flat.BoostPadState;
import rlbot.flat.FieldInfo;
import rlbot.flat.GameTickPacket;
import rlbotexample.input.dynamic_data.RlUtils;
import util.math.vector.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Information about where boost pads are located on the field and what status they have.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class BoostManager {

    public static final double BIG_BOOST_RELOAD_TIME = 10;
    public static final double BIG_BOOST_AMOUNT = 100;
    public static final double SMALL_BOOST_RELOAD_TIME = 4;
    public static final double SMALL_BOOST_AMOUNT = 12;
    
    public static final List<BoostPad> boostPads = new ArrayList<>();
    public static final List<BoostPad> bigBoosts = new ArrayList<>();
    public static final List<BoostPad> smallBoosts = new ArrayList<>();

    private static void loadFieldInfo(FieldInfo fieldInfo) {
        synchronized (boostPads) {
            boostPads.clear();
            bigBoosts.clear();
            smallBoosts.clear();

            for(int i = 0; i < fieldInfo.boostPadsLength(); i++) {
                rlbot.flat.BoostPad flatPad = fieldInfo.boostPads(i);
                boolean isBigBoost = flatPad.isFullBoost();
                Vector3 location = new Vector3(flatPad.location());
                BoostPad ourPad = new BoostPad(
                        new Vector3(flatPad.location()),
                        flatPad.isFullBoost(),
                        isBigBoost ? new BigBoostHitBox(location) : new SmallBoostHitBox(location),
                        i);
                boostPads.add(ourPad);
                if(ourPad.isBigBoost) {
                    bigBoosts.add(ourPad);
                }
                else {
                    smallBoosts.add(ourPad);
                }
            }
        }
    }

    public static void loadGameTickPacket(GameTickPacket packet) {
        if(packet.boostPadStatesLength() > boostPads.size()) {
            try {
                loadFieldInfo(RLBotDll.getFieldInfo());
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        for(int i = 0; i < packet.boostPadStatesLength(); i++) {
            BoostPadState boost = packet.boostPadStates(i);
            BoostPad existingPad = boostPads.get(i); // existingPad is also referenced from the fullBoosts and smallBoosts lists
            existingPad.setActive(boost.isActive());

            RlUtils.updateBoostPadReloadingTimer(existingPad);
            double timeBeforeReloaded = RlUtils.getTimeBeforePadReloads(existingPad);
            existingPad.setTimeBeforeReloaded(timeBeforeReloaded);
        }

        for(BoostPad currentNode: boostPads) {
            for(BoostPad potentialNeighbour: boostPads) {
                // skip self references
                if(currentNode == potentialNeighbour) {
                    continue;
                }

                currentNode.addNeighbourNode(potentialNeighbour);
            }
        }
    }

    public static Optional<BoostPad> closestActivePadFrom(Vector3 position) {
        return BoostManager.boostPads.stream().min(new Comparator<BoostPad>() {
            @Override
            public int compare(BoostPad o1, BoostPad o2) {
                if(!o1.isActive) {
                    return 1;
                }
                if(!o2.isActive) {
                    return -1;
                }
                if(position.minus(o1.location).magnitudeSquared() <
                        position.minus(o2.location).magnitudeSquared()) {
                    return -1;
                }
                else if(position.minus(o1.location).magnitudeSquared() >
                        position.minus(o2.location).magnitudeSquared()) {
                    return 1;
                }
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
    }
}
