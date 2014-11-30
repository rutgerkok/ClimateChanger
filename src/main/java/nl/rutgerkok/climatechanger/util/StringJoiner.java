package nl.rutgerkok.climatechanger.util;

import java.util.Collection;
import java.util.List;

/**
 * Simple class to join strings together.
 *
 */
public final class StringJoiner {

    private StringJoiner() {
    }

    /**
     * Joins the given collection of strings together with the given glue.
     * 
     * @param glue
     *            The glue, appears between each element.
     * @param elements
     *            The elements.
     * @return The combined string.
     */
    public static final String join(String glue, Collection<String> elements) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String element : elements) {
            if (!first) {
                builder.append(glue);
            }
            first = false;
            builder.append(element);
        }
        return builder.toString();
    }

    /**
     * Joins all elements of the given list with index >= start together with
     * the given glue.
     * 
     * @param glue
     *            The glue, appears between each element.
     * @param start
     *            Index of the first element.
     * @param elements
     *            The elements.
     * @return The combined string.
     * @throws IndexOutOfBoundsException
     *             If {@code start < 0 || start > elements.size()}
     */
    public static final String join(String glue, int start, List<String> elements) {
        return join(glue, elements.subList(start, elements.size()));
    }
}
