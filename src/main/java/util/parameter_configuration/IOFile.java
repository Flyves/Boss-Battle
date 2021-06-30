package util.parameter_configuration;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class IOFile {

    public static List<String> getFileContent(String fileName) {
        List<String> fileContent = new ArrayList<>();

        try {
            File file = new File(fileName);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                fileContent.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return fileContent;
    }

    public static List<String> getFileNamesIn(String folderName) {
        try {
            File file = new File(folderName);
            return Arrays.stream(Objects.requireNonNull(file.listFiles()))
                    .map(File::getName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static void createFileWithContent(String fileName, List<String> parameters) {
        try {
            // put all the parameters on distinct lines into a single string object
            StringBuilder fileData = new StringBuilder();
            for(String parameter: parameters) {
                fileData.append(parameter).append("\n");
            }
            // create the file (returns false if a file with the same file name already exists)
            Files.write(Paths.get(fileName), fileData.toString().getBytes());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        file.delete();
    }
}
