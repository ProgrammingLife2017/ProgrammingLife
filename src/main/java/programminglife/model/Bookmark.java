package programminglife.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class representing a bookmark. Consists of a location and radius.
 */
public class Bookmark {
    private String file;
    private String path;
    private int radius;
    private int nodeID;
    private String bookmarkName;
    private String description;

    /**
     * Initialize a bookmark.
     * @param file The graph file where this bookmark belongs to
     * @param path The path where the graph file is located.
     * @param bookmarkName this is the bookmarkName of the file in which this genome and location is present
     * @param nodeID is the ID of the node where the bookmark is present.
     * @param radius is the depth to which surrounding nodes will be visualized.
     * @param description The text describing this bookmark.
     */
    public Bookmark(String file, String path, int nodeID, int radius, String bookmarkName, String description) {
        this.file = file;
        this.path = path;
        this.radius = radius;
        this.nodeID = nodeID;
        this.bookmarkName = bookmarkName;
        this.description = description;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(this.bookmarkName);
    }

    public StringProperty getDescriptionProperty() {
        return new SimpleStringProperty(this.description);
    }

    public StringProperty getFileProperty() {
        return new SimpleStringProperty(this.file);
    }

    public String getBookmarkName() {
        return bookmarkName;
    }

    public void setBookmarkName(String bookmarkName) {
        this.bookmarkName = bookmarkName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Bookmark) {
            Bookmark that = (Bookmark) other;
            if (this.file.equals(that.getFile())
                    && this.radius == that.getRadius()
                    && this.nodeID == that.getNodeID()
                    && this.bookmarkName.equals(that.getBookmarkName())
                    && this.description.equals(that.getDescription())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return ("{file: " + this.file + ", name: " + this.bookmarkName
                + ", description: " + this.description
                + ", ID " + this.nodeID
                + ", radius: " + this.radius + "}");
    }
}
