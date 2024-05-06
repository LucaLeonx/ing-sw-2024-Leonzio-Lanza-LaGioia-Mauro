package it.polimi.ingsw.model.card;

import java.io.Serializable;

/**
 * This class represents the contents visible on the angles and center of the Cards
 */
public enum Symbol implements Serializable {
    PLANT,
    ANIMAL,
    INSECT,
    FUNGI,
    QUILL,
    INKWELL,
    MANUSCRIPT,
    /**
     * No content is visible
     */
    BLANK,
    /**
     * Hidden content (for angles only)
     */
    HIDDEN
}
