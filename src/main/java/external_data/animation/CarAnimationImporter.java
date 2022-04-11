package external_data.animation;

import rlbotexample.asset.animation.discrete_player.CarData;
import rlbotexample.asset.animation.discrete_player.CarGroup;
import rlbotexample.asset.animation.discrete_player.IndexedCarGroup;
import rlbotexample.asset.animation.discrete_player.Animation;
import util.math.matrix.Matrix3By3;
import util.math.vector.ZyxOrientedPosition;
import util.math.vector.Vector3;
import util.files.IOFile;
import util.files.ObjectSerializer;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This small program works in coordination with blender.
 * Basically, there is a script that gets the positions and orientations of animated cars in blender and outputs them in a file.
 * This algorithm serves as a bridge to transfer the parsed data into streamed objects so we can use them in the main bot.
 *
 * With this, we can load rocket league animations that we carefully crafted in blender.
 */
public class CarAnimationImporter {
    public static final String ANIMATIONS_BASE_FOLDER_PATH = "src\\main\\resources\\car animations";
    public static final String ANIMATIONS_EXTENSION_NAME = ".cop";
    public static final String OBJECT_STREAMING_EXTENSION_NAME = ".sob";
    private static int amountOfFileSerialized = 0;

    public static void main(String[] args) {
        getStreamOfAnimationFileNames()
                .map(fileName -> ANIMATIONS_BASE_FOLDER_PATH + "\\" + fileName)
                .forEach(CarAnimationImporter::fileDataToStreamedObject);
        System.out.println("\nSerialized " + amountOfFileSerialized + " files.");
    }

    public static Stream<String> getStreamOfAnimationFileNames() {
        return IOFile.getFileNamesIn("src\\main\\resources\\car animations")
                .stream()
                .filter(fileName -> {
                    int extensionIndex = fileName.lastIndexOf('.');
                    String extension = fileName.substring(extensionIndex);
                    return extension.equals(ANIMATIONS_EXTENSION_NAME);
                });
    }

    // object streaming of data generated from blender
    private static void fileDataToStreamedObject(String filePath) {
        System.out.println("Serializing \"" + filePath + "\"...");
        final List<String> fileData = IOFile.getFileContent(filePath);

        final AtomicReference<Integer> previousFrameIdRef = new AtomicReference<>(-1);
        final AtomicReference<Integer> newFrameIdRef = new AtomicReference<>(0);

        final List<IndexedCarGroup> carMeshFrames = new ArrayList<>();

        // for each car in every frame
        fileData.forEach(s -> {
            // just get the floats in an array
            final String[] valuesStr = s.split(":");
            final List<Double> valuesDouble = Arrays.stream(valuesStr)
                    .map(Double::valueOf)
                    .collect(Collectors.toList());

            // find the car state for this specific car and for this specific frame that we are currently parsing
            final int objectId = round(valuesDouble.get(0));
            final int frameId = round(valuesDouble.get(1));

            final Vector3 objectPosition = new Vector3(valuesDouble.get(2), valuesDouble.get(3), valuesDouble.get(4));
            final Matrix3By3 objectRotationMatrix = new Matrix3By3(
                    valuesDouble.get(5), valuesDouble.get(6), valuesDouble.get(7),
                    valuesDouble.get(8), valuesDouble.get(9), valuesDouble.get(10),
                    valuesDouble.get(11), valuesDouble.get(12), valuesDouble.get(13)
            );

            final int teamId = round(valuesDouble.get(14));


            // update current frame id reference
            newFrameIdRef.set(frameId);

            // if this is a new frame
            if(!previousFrameIdRef.get().equals(newFrameIdRef.get())) {
                previousFrameIdRef.set(frameId);
                final int safeFrameId = carMeshFrames.size();
                carMeshFrames.add(new IndexedCarGroup(safeFrameId));
            }

            // get the latest frame that we are building
            final CarGroup mesh = carMeshFrames.get(carMeshFrames.size()-1).carGroup;

            // add the parsed car in the frame
            final ZyxOrientedPosition zyxOrientedPosition = new ZyxOrientedPosition(objectPosition, objectRotationMatrix.toEulerZyx());
            final CarData carObject = new CarData(objectId, teamId, zyxOrientedPosition);
            mesh.carObjects.add(carObject);
        });

        carMeshFrames.stream()
            .map(indexedCarGroup -> indexedCarGroup.carGroup)
            .forEach(carGroup -> carGroup.carObjects.sort(Comparator.comparingInt(c -> c.carId)));

        // serialize the data in a file for easy loading
        ObjectSerializer.save(new Animation(carMeshFrames), filePath.replaceAll("\\" + ANIMATIONS_EXTENSION_NAME, OBJECT_STREAMING_EXTENSION_NAME));
        amountOfFileSerialized++;
    }

    private static int round(Double x) {
        return (int)(x + 0.5);
    }
}