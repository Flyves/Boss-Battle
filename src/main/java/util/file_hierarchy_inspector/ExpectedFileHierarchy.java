package util.file_hierarchy_inspector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExpectedFileHierarchy {

    public String hierarchyRoot;
    public List<ExpectedFileHierarchy> expectedFileHierarchyList;
    public List<String> expectedFileList;

    public ExpectedFileHierarchy(String hierarchyRoot) {
        this.hierarchyRoot = hierarchyRoot;
        this.expectedFileHierarchyList = new ArrayList<>();
        this.expectedFileList = new ArrayList<>();
    }

    public ExpectedFileHierarchy withFolder(ExpectedFileHierarchy folder) {
        expectedFileHierarchyList.add(folder);
        return this;
    }

    public ExpectedFileHierarchy withFile(String fileName) {
        expectedFileList.add("\\" + fileName);

        return this;
    }

    public boolean inspect() {
        boolean isInRule = true;

        // test the root folder
        File root = new File(hierarchyRoot);
        if(!root.exists()) {
            isInRule = false;
        }
        // test all files in the root folder
        for(String fileName: expectedFileList) {
            File fileToTest = new File(hierarchyRoot + "\\" + fileName);
            if(!fileToTest.exists()) {
                isInRule = false;
            }
        }
        // test all nested folders (and all nested files and folders recursively)
        for(ExpectedFileHierarchy folder: expectedFileHierarchyList) {
            if(!folder.inspect()) {
                isInRule = false;
            }
        }

        return isInRule;
    }

    public ExpectedFileHierarchy getMissingFileHierarchy() {
        ExpectedFileHierarchy missingFileHierarchy = new ExpectedFileHierarchy(hierarchyRoot);

        // test all fileNames
        for(String fileName: expectedFileList) {
            File fileToTest = new File(hierarchyRoot + "\\" + fileName);
            if(!fileToTest.exists()) {
                missingFileHierarchy.withFile(fileName);
            }
        }
        // test all folderNames (and all file and folder names recursively)
        for(ExpectedFileHierarchy folder: expectedFileHierarchyList) {
            if(!folder.inspect()) {
                missingFileHierarchy.withFolder(new ExpectedFileHierarchy(folder.hierarchyRoot));
            }
        }

        return missingFileHierarchy;
    }

    @Override
    public String toString() {
        FileHierarchySerializer serializer = new FileHierarchySerializer(this);
        return "Expected file hierarchy:\n"
                + serializer.serialize();
    }
}

