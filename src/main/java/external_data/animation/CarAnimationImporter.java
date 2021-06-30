package external_data.animation;

import rlbotexample.animations.CarGroup;
import rlbotexample.animations.IndexedCarGroup;
import rlbotexample.animations.CarGroupAnimation;
import util.math.matrix.Matrix3By3;
import util.math.vector.ZyxOrientedPosition;
import util.math.vector.Vector3;
import util.parameter_configuration.IOFile;
import util.parameter_configuration.ObjectSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    public static void main(String[] args) {
        IOFile.getFileNamesIn("src\\main\\resources\\car animations")
                .stream()
                .filter(fileName -> {
                    int extensionIndex = fileName.lastIndexOf('.');
                    String extension = fileName.substring(extensionIndex);
                    return extension.equals(ANIMATIONS_EXTENSION_NAME);
                })
                .map(fileName -> ANIMATIONS_BASE_FOLDER_PATH + "\\" + fileName)
                .forEach(CarAnimationImporter::fileDataToStreamedObject);
    }

    // object streaming of data generated from blender
    private static void fileDataToStreamedObject(String filePath) {
        List<String> fileData = IOFile.getFileContent(filePath);

        AtomicReference<Integer> previousFrameIdRef = new AtomicReference<>(-1);
        AtomicReference<Integer> newFrameIdRef = new AtomicReference<>(0);

        List<IndexedCarGroup> carMeshFrames = new ArrayList<>();

        // for each car in every frame
        fileData.forEach(s -> {
            // just get the floats in an array
            String[] valuesStr = s.split(":");
            List<Double> valuesDouble = Arrays.stream(valuesStr)
                    .map(Double::valueOf)
                    .collect(Collectors.toList());

            // find the car state for this specific car and for this specific frame that we are currently parsing
            int objectId = valuesDouble.get(0).intValue();
            int frameId = valuesDouble.get(1).intValue();

            Vector3 objectPosition = new Vector3(valuesDouble.get(2), valuesDouble.get(3), valuesDouble.get(4));
            Matrix3By3 objectRotationMatrix = new Matrix3By3(
                    valuesDouble.get(5), valuesDouble.get(6), valuesDouble.get(7),
                    valuesDouble.get(8), valuesDouble.get(9), valuesDouble.get(10),
                    valuesDouble.get(11), valuesDouble.get(12), valuesDouble.get(13)
            );

            // update current frame id reference
            newFrameIdRef.set(frameId);

            // if this is a new frame
            if(!previousFrameIdRef.get().equals(newFrameIdRef.get())) {
                previousFrameIdRef.set(frameId);
                int safeFrameId = carMeshFrames.size();
                carMeshFrames.add(new IndexedCarGroup(safeFrameId));
            }

            // get the latest frame that we are building
            CarGroup mesh = carMeshFrames.get(carMeshFrames.size()-1).carGroup;

            // add the parsed car in the frame
            mesh.orientedPositions.add(new ZyxOrientedPosition(
                    objectPosition,
                    objectRotationMatrix.toEulerZyx()));
        });

        // serialize the data in a file for easy loading
        ObjectSerializer.save(new CarGroupAnimation(carMeshFrames), filePath.replaceAll("\\" + ANIMATIONS_EXTENSION_NAME, OBJECT_STREAMING_EXTENSION_NAME));
    }
}