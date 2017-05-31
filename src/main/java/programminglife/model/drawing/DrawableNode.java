package programminglife.model.drawing;

import javafx.scene.shape.Rectangle;
import programminglife.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Segment} that also Implements {@link Drawable}.
 */
public class DrawableNode extends Rectangle {
    private Set<Node> nodes;
    private boolean drawDimensionsUpToDate = false;


    /**
     * Create a DrawableSegment from a Segment.
     * @param node The segment to create this DrawableSegment from.
     */
    public DrawableNode(Node node) {
        this.nodes = new HashSet<>();
        nodes.add(node);
        this.setDrawDimensions();
    }

    /**
     * Create a glyph.
     * @param nodes The set of Segments this DrawableNode
     */
    public DrawableNode(Set<Node> nodes) {
        this.nodes = nodes;
    }


    /**
     * Get all the edges to the children.
     * @return childEdges {@link Collection<DrawableEdge>} are all the edges
     * to the children of the node {@link DrawableNode}.
     */
    public Collection<DrawableEdge> getChildEdges() {
        HashSet<DrawableEdge> childEdges = new HashSet<DrawableEdge>();
        for (Node n : nodes) {
            for (Edge e: n.getChildEdges()) {
                childEdges.add(new DrawableEdge(e, this, new DrawableNode(e.getEnd())));
            }
        }
        return childEdges;
    }

    /**
     * Get all the edges to the parents.
     * @return parentEdges {@link Collection<DrawableEdge>} are all the edges
     * to the parents of the node {@link DrawableNode}.
     */
    public Collection<DrawableEdge> getParentEdges() {
        HashSet<DrawableEdge> parentEdges = new HashSet<DrawableEdge>();
        for (Node n : nodes) {
            for (Edge e : n.getParentEdges()) {
                parentEdges.add(new DrawableEdge(e, new DrawableNode(e.getStart()), this));
            }
        }
        return parentEdges;
    }

    /**
     * Get all the children of the node {@link DrawableNode}.
     * @return children {@link Collection<DrawableNode>} are the direct children of the node {@link DrawableNode}.
     */
    public Collection<DrawableNode> getChildren() {
        Collection<DrawableNode> children = new HashSet<>();
        for (Node n : this.nodes) {
            for (Node child : n.getChildren()) {
                children.add(new DrawableNode(child));
            }
        }
        return children;
    }

    /**
     * Get all the parents of the node {@link DrawableNode}.
     * @return parent {@link Collection<DrawableNode>} are the direct parents of the node {@link DrawableNode}.
     **/
    public Collection<DrawableNode> getParents() {
        Collection<DrawableNode> parents = new HashSet<>();
        for (Node n : this.nodes) {
            System.out.println(n.getParents().size());
            for (Node parent : n.getParents()) {
                parents.add(new DrawableNode(parent));
            }
        }
        return parents;
    }

    public int getIntX() {
        return (int) Math.ceil(this.getX());
    }

    public int getIntY() {
        return (int) Math.ceil(this.getY());
    }

    public int getIntWidth() {
        return (int) Math.ceil(this.getWidth());
    }

    public int getIntHeight() {
        return (int) Math.ceil(this.getHeight());
    }

    /**
     * Get a {@link XYCoordinate} representing the size of the {@link Segment}.
     * @return The size of the {@link Segment}
     */
    public XYCoordinate getSize() {
        if (!drawDimensionsUpToDate) {
            setDrawDimensions();
        }
        return new XYCoordinate((int) this.getWidth(), (int) this.getHeight());
    }

    /**
     * Set the size {@link XYCoordinate} of the {@link Segment}.
     * @param size The {@link XYCoordinate} representing the size
     */
    void setSize(XYCoordinate size) {
        this.setWidth(size.getX());
        this.setHeight(size.getY());
        this.drawDimensionsUpToDate = true;
    }

    /**
     * Getter for top left corner of a {@link Segment}.
     * @return {@link XYCoordinate} with the values of the top left corner.
     */
    public XYCoordinate getLocation() {
        return new XYCoordinate((int) this.getX(), (int) this.getY());
    }

    /**
     * Set an {@link XYCoordinate} representing the location of the {@link Segment}.
     * @param location The {@link XYCoordinate}
     */
    public void setLocation(XYCoordinate location) {
        this.setX(location.getX());
        this.setY(location.getY());
    }

    /**
     * getter for the width coordinate.
     * @return XYCoordinate.
     */
    public XYCoordinate getWidthCoordinate() {
        if (!drawDimensionsUpToDate) {
            setDrawDimensions();
        }
        return new XYCoordinate((int) this.getWidth(), 0);
    }

    /**
     * getter for the height coordinate.
     * @return XYCoordinate.
     */
    public XYCoordinate getHeightCoordinate() {
        if (!drawDimensionsUpToDate) {
            setDrawDimensions();
        }
        return new XYCoordinate(0, (int) this.getHeight());
    }

    /**
     * Setter for the dimension of the node.
     */
    private void setDrawDimensions() {
        int segmentLength = this.getSequenceLength();
        int width, height;

        width = 10 + (int) Math.pow(segmentLength, 1.0 / 2);
        height = 10;

        this.setSize(new XYCoordinate(width, height));
        this.drawDimensionsUpToDate = true;
    }

    /**
     * get the length of the sequence of this segment.
     * @return the length of the sequence of this segment
     */
    public int getSequenceLength() {
        int length = 0;
        for (Node n : this.nodes) {
            length += n.getSequenceLength();
        }
        return length;
    }

    public String getSequence() {
        StringBuilder result = new StringBuilder();
        for (Node n : this.nodes) {
            result.append(DataManager.getSequence(n.getIdentifier()));
        }
        return result.toString();
    }

    /**
     * getter for the center of the left border.
     * @return XYCoordinate.
     */
    public XYCoordinate getLeftBorderCenter() {
        if (!drawDimensionsUpToDate) {
            setDrawDimensions();
        }
        return this.getCenter().add(-(this.getSize().getX() >> 1), 0);
    }

    /**
     * getter for the center.
     * @return XYCoordinate.
     */
    public XYCoordinate getCenter() {
        if (!drawDimensionsUpToDate) {
            setDrawDimensions();
        }
        return this.getLocation().add(this.getSize().multiply(0.5));
    }

    /**
     * getter for the center of the right border.
     * @return XYCoordinate.
     */
    public XYCoordinate getRightBorderCenter() {
        if (!drawDimensionsUpToDate) {
            setDrawDimensions();
        }
        return this.getCenter().add(this.getSize().getX() >> 1, 0);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DrawableNode) {
            DrawableNode that = (DrawableNode) other;
            if (that.nodes.equals(this.nodes)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        // TODO: improve
        return 1;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("DrawableNode[Nodes: ");
        for (Node n : nodes) {
            res.append(n.getIdentifier()).append(", ");
        }
        if (!nodes.isEmpty()) {
            res.delete(res.length() - 2, res.length());
        }
        return res.append("]").toString();
    }
}