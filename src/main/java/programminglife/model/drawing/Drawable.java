package programminglife.model.drawing;

import javafx.scene.paint.Color;
import java.util.Collection;


/**
 * Something that can be drawn on the screen.
 * A Drawable has a location, which is in the top left of the Drawable
 */
public interface Drawable {

    void setStrokeColor(Color color);

    void setStrokeWidth(double width);

    /**
     * Get the genomes in/through this {@link Drawable}.
     * @return a {@link Collection} of genome IDs
     */
    Collection<Integer> getGenomes();

    /**
     * Add genomes in/through this {@link Drawable}.
     * @param genomes a {@link Collection} of genome IDs
     */
    void addGenomes(Collection<Integer> genomes);

}
