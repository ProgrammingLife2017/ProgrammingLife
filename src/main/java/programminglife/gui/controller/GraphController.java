package programminglife.gui.controller;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import jp.uphy.javafx.console.ConsoleView;
import programminglife.model.GenomeGraph;
import programminglife.model.Node;
import programminglife.model.SubGraph;
import programminglife.model.XYCoordinate;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Martijn van Meerten on 8-5-2017.
 * Controller for drawing the graph.
 */
public class GraphController {
    private static final XYCoordinate INITIAL_OFFSET = new XYCoordinate(50, 50);
    private static final XYCoordinate HORIZONTAL_OFFSET = new XYCoordinate(50, 0);
    private static final double CHILD_OFFSET = 1.7;

    private GenomeGraph graph;
    private Group grpDrawArea;
    private ConsoleView console;

    /**
     * Initialize controller object.
     * @param graph The genome graph to control
     * @param grpDrawArea The {@link Group} to draw in
     */
    public GraphController(GenomeGraph graph, Group grpDrawArea) {
        this.graph = graph;
        this.grpDrawArea = grpDrawArea;
    }

    public void setConsole(ConsoleView console) {
        this.console = console;
    }

    /**
     * Draw the {@link GenomeGraph} with DFS from {@link Node} 1.
     * @param centerNode The {@link Node} to start drawing from
     * @param maxDepth The max depth of child {@link Node}s to draw
     */
    public void draw(int centerNode, int maxDepth) {
        this.drawDFS(null, this.getGraph().getNode(centerNode), INITIAL_OFFSET, maxDepth);
//        SubGraph subGraph = new SubGraph(this.getGraph().getNode(centerNode), maxDepth);
//        subGraph.calculateNodeLocations(50, 20);
//        drawSubGraph(subGraph);
    }

    /**
     * Draws all nodes in the SubGraph g.
     * @param g The SubGraph
     */
    public void drawSubGraph(SubGraph g) {
        this.clear();
        for (Node n : g) {
            for (Node p : n.getParents()) {
                if (!g.contains(p)) {
                    continue;
                }
                Line link = new Line(
                        p.getX() + p.getWidth(),
                        p.getY() + p.getHeight() / 2,
                        n.getX(),
                        n.getY() + n.getHeight() / 2
                );
                link.setStroke(Color.DARKGRAY);
                link.setStrokeWidth(3);
                link.setOnMouseClicked(event -> System.out.printf("Link{%s -> %s}\n", p, n));

                this.grpDrawArea.getChildren().add(link);
                this.drawNode(n);
            }
        }
    }

    /**
     * Draw all nodes recursively on the screen.
     *
     * @param origin The parent {@link Node} that initiated this draw call
     * @param node Draw this node and all its children recursively
     * @param offset Draws nodes at this offset from the top-left of the screen
     * @return a {@link Set} of all drawn {@link Node}s
     */
    private Set<Node> drawDFS(Node origin, Node node, XYCoordinate offset) {
        return this.drawDFS(origin, node, offset, -1, new HashSet<>());
    }

    /**
     * Draw all nodes recursively on the screen.
     *
     * @param origin The parent {@link Node} that initiated this draw call
     * @param node Draw this {@link Node} and all its children recursively
     * @param offset Draws nodes at this offset from the top-left of the screen
     * @param maxDepth The max depth from root to draw nodes
     * @return a {@link Set} of all drawn {@link Node}s
     */
    public Set<Node> drawDFS(Node origin, Node node, XYCoordinate offset, int maxDepth) {
        return this.drawDFS(origin, node, offset, maxDepth, new HashSet<>());
    }

    /**
     * Draw all nodes recursively on the screen.
     * @param origin The parent {@link Node} that initiated this draw call
     * @param node Draw this node and all its children recursively
     * @param offset Draws nodes at this offset from the top-left of the screen
     * @param drawnNodes A set containing all drawn nodes
     * @param maxDepth The max depth from root to draw nodes
     * @return a {@link Set} of all drawn {@link Node}s
     */
    private Set<Node> drawDFS(Node origin, Node node, XYCoordinate offset, int maxDepth, Set<Node> drawnNodes) {
        boolean nodeIsDrawn = drawnNodes.contains(node);
        if (!nodeIsDrawn) {
            node.setLocation(offset);
        }

        if (origin != null && maxDepth != 0) {
            XYCoordinate targetLeft = node.getLeftBorderCenter();
            XYCoordinate originRight = origin.getRightBorderCenter();

            Line link = new Line(targetLeft.getX(), targetLeft.getY(), originRight.getX(), originRight.getY());
            link.setStroke(Color.DARKGRAY);
            link.setStrokeWidth(3);
            link.setOnMouseClicked(event -> System.out.printf("Link{%s -> %s}\n", origin, node));

            this.grpDrawArea.getChildren().add(link);
        }

        if (maxDepth != 0 && !nodeIsDrawn) {
            this.drawNode(node);
            drawnNodes.add(node);

            int childCount = 0;
            for (Node child : node.getChildren()) {
                XYCoordinate newOffset = offset.add(HORIZONTAL_OFFSET)
                        .add(node.getWidthCoordinate())
                        .setY(INITIAL_OFFSET.getY() + (int) (CHILD_OFFSET * childCount * node.getHeight()));
                this.drawDFS(node, child, newOffset, maxDepth - 1, drawnNodes);
                childCount++;
            }
        }

        return drawnNodes;
    }



    /**
     * Draws the node on the canvas.
     *
     * @param nodeID The ID of the node to draw
     * @return The size of the node
     */
    private XYCoordinate drawNode(int nodeID) {
        return this.drawNode(this.graph.getNode(nodeID));
    }

    /**
     * Draw a single {@link Node}.
     * @param node the {@link Node} to draw
     * @return the size of the drawn {@link Node}
     */
    private XYCoordinate drawNode(Node node) {
        node.setOnMouseClicked(event -> {
            System.out.println(node.getSequence());
            System.out.printf("%s (location %s, size %s)\n",
                    node.toString(),
                    node.getLocation(),
                    node.getSize());
        });

        node.setFill(Color.TRANSPARENT);
        node.setStroke(Color.DARKRED);

        this.grpDrawArea.getChildren().add(node);
        this.grpDrawArea.getChildren().add(new Rectangle(1, 2, 3, 4));

        return node.getSize();
    }

    /**
     * Getter for the graph.
     * @return - The graph
     */
    public GenomeGraph getGraph() {
        return graph;
    }

    /**
     * Setter for the graph.
     * @param graph The graph
     */
    void setGraph(GenomeGraph graph) {
        this.graph = graph;
    }

    /**
     * Clear the draw area.
     */
    public void clear() {
        this.grpDrawArea.getChildren().clear();
    }
}
