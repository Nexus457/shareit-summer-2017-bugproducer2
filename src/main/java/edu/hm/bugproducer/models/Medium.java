package edu.hm.bugproducer.models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Medium Class.
 *
 * @author Mark Tripolt
 * @author Johannes Arzt
 * @author Tom Maier
 * @author Patrick Kuntz
 */
@MappedSuperclass
public abstract class Medium implements Serializable {
    /**
     * name of a medium
     */
    private String title;

    /**
     * Medium Constructor with no title.
     */
    public Medium() {
        title = "Medium NO TITLE";
    }

    /**
     * Medium Constructor.
     *
     * @param title name of a medium
     */
    public Medium(String title) {
        this.title = title;
    }

    /**
     * getTitle method.
     * gets the title of a medium
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * setTitle method.
     * sets the title of a medium
     *
     * @param title name of it
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medium)) {
            return false;
        }

        Medium medium = (Medium) o;

        return getTitle().equals(medium.getTitle());
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }
}
