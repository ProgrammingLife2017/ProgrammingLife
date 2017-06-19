package programminglife.model.drawing;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import programminglife.model.XYCoordinate;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * A link that also Implements {@link Drawable}.
 */

public class DrawableEdge implements Drawable {
    private XYCoordinate startLocation;
    private XYCoordinate endLocation;

    private double strokeWidth;
    private Color strokeColor;

    private DrawableNode parent;
    private DrawableNode child;
    private Collection<Integer> genomes;

    /**
     * Create a Drawable edge.
     * @param parent The {@link DrawableSegment} parent of this edge
     * @param child The {@link DrawableSegment} child of this edge
     */
    public DrawableEdge(DrawableNode parent, DrawableNode child) {
        this.startLocation = parent.getRightBorderCenter();
        this.endLocation = child.getLeftBorderCenter();
        this.parent = parent;
        this.child = child;
        this.genomes = new LinkedHashSet<>();
    }

    public DrawableNode getStart() {
        return parent;
    }

    public DrawableNode getEnd() {
        return child;
    }

    /**
     * Set the starting location of this edge.
     * @param startNode The {@link XYCoordinate} to start drawing from.
     */
    public void setStartNode(DrawableNode startNode) {
        XYCoordinate rightBorderCenter = startNode.getRightBorderCenter();
        this.startLocation= new XYCoordinate(rightBorderCenter.getX(), rightBorderCenter.getY());
    }

    /**
     * Set the end location of this edge.
     * @param endNode The {@link XYCoordinate} to end the drawing at.
     */
    public void setEndNode(DrawableNode endNode) {
        XYCoordinate leftBorderCenter = endNode.getLeftBorderCenter();
        this.endLocation = new XYCoordinate(leftBorderCenter.getX(), leftBorderCenter.getY());
    }

    @Override
    public String toString() {
        return String.format("Link from %s to %s", this.parent, this.child);
    }

    /**
     * Color a {@link DrawableEdge} depending on its properties.
     * @param sg the {@link SubGraph} belonging to the {@link DrawableEdge}
     */
    public void colorize(SubGraph sg) {
        double genomeFraction = 0.d;
        Map<DrawableNode, Collection<Integer>> from = sg.getGenomes().get(this.getStart().getParentSegment());
        if (from != null) {
            Collection<Integer> genomes = from.get(this.getEnd().getChildSegment());
            if (genomes != null) {
                genomeFraction = genomes.size() / (double) sg.getNumberOfGenomes();
            }
        }

        double minStrokeWidth = 1.d, maxStrokeWidth = 6.5;
        double strokeWidth = minStrokeWidth + genomeFraction * (maxStrokeWidth - minStrokeWidth);

        double minBrightness = 0.6, maxBrightness = 0.25;
        double brightness = minBrightness + genomeFraction * (maxBrightness - minBrightness);

        Color strokeColor = Color.hsb(0.d, 0.d, brightness);

        this.setStrokeWidth(strokeWidth);
        this.setStrokeColor(strokeColor);
    }

    @Override
    public Collection<Integer> getGenomes() {
        return this.genomes;
    }

    /**
     * Add genomes in/through this {@link Drawable}.
     *
     * @param genomes a {@link Collection} of genome IDs
     */
    @Override
    public void addGenomes(Collection<Integer> genomes) {
        this.genomes.addAll(genomes);
    }

    public double getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public XYCoordinate getStartLocation() {
        return this.startLocation;
    }

    public XYCoordinate getEndLocation() {
        return this.endLocation;
    }
}
