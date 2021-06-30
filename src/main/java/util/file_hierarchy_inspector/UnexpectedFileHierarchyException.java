package util.file_hierarchy_inspector;

public class UnexpectedFileHierarchyException extends RuntimeException {

    private ExpectedFileHierarchy missingFileHierarchy;

    public UnexpectedFileHierarchyException(ExpectedFileHierarchy expectedFileTree) {
        this.missingFileHierarchy = expectedFileTree;
    }

    @Override
    public void printStackTrace() {
        System.out.println("expected file tree is:\n" + missingFileHierarchy + "\n\n" + "Missing elements are:\n" + missingFileHierarchy.getMissingFileHierarchy());
    }

}
