package util.file_hierarchy_inspector;

public class FileHierarchySerializer {

    private ExpectedFileHierarchy expectedFileHierarchy;

    public FileHierarchySerializer(ExpectedFileHierarchy expectedFileHierarchy) {
        this.expectedFileHierarchy = expectedFileHierarchy;
    }

    public String serialize() {
        return serialize(0);
    }

    private String serialize(int numberOfTabulations) {
        StringBuilder result;

        // put n tabulations if we're in the nth folder deep down from root
        StringBuilder tabulations = new StringBuilder();
        for(int i = 0; i < numberOfTabulations; i++) {
            tabulations.append("\t");
        }

        // add root folder name
        result = new StringBuilder(tabulations + expectedFileHierarchy.hierarchyRoot + "\n");

        // add all file names
        for(String fileName: expectedFileHierarchy.expectedFileList) {
            result.append("\t").append(tabulations).append(fileName).append("\n");
        }
        // test all files and folders recursively in the folders
        for(ExpectedFileHierarchy folder: expectedFileHierarchy.expectedFileHierarchyList) {
            FileHierarchySerializer folderSerializer = new FileHierarchySerializer(folder);
            result.append(folderSerializer.serialize(numberOfTabulations + 1));
        }

        return result.toString();
    }
}
