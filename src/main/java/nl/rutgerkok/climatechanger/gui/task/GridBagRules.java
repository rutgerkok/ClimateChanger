package nl.rutgerkok.climatechanger.gui.task;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * Facade for the horrible {@link GridBagConstraints} API.
 *
 */
public final class GridBagRules {

    /**
     * Represents the rule of how the component behaves when added to a
     * {@link GridBagLayout}.
     *
     * <p>
     * Note that the implementation is mutable, it always changes itself.
     * Luckily {@link GridBagLayout} always makes a clone of this object, so you
     * can reuse the builders.
     *
     * @implSpec All implementations of this interface need to inherit from
     *           {@link GridBagConstraints}. This interface purely exists to
     *           mark the added API and to hide the {@link GridBagConstraints}
     *           fields.
     *
     */
    public interface GridBagRule {
        /**
         * Modifies the amount of columns that the component will span.
         * 
         * @param colSpan
         *            Amount of columns.
         * @return This, for chaining.
         */
        GridBagRule colSpan(int colSpan);

        /**
         * Modifies the vertical margin. This is the margin between each element
         * in the grid bag layout, and also the margin around the elements.
         * 
         * @param marginVertical
         *            The new vertical margin.
         * @return This, for chaining.
         */
        GridBagRule marginVertical(int marginVertical);

        /**
         * Modifies the horizontal margin. This is the margin between each
         * element in the grid bag layout, and also the margin around the
         * elements.
         *
         * @param marginHorizontal
         *            The new right margin.
         * @return This, for chaining.
         */
        GridBagRule marginHorizontal(int marginHorizontal);

        /**
         * Modifies the resize/overflow behavior.
         * 
         * @param behavior
         *            The new behavior.
         * @return This, for chaining.
         */
        GridBagRule resizeBehavior(ResizeBehavior behavior);

        /**
         * Modifies the amount of rows that the component will span.
         * 
         * @param rowSpan
         *            Amount of rows.
         * @return This, for chaining.
         */
        GridBagRule rowSpan(int rowSpan);
    }

    private static class GridBagRuleImpl extends GridBagConstraints implements GridBagRule {

        private GridBagRuleImpl(int gridX, int gridY) {
            this.gridx = gridX;
            this.gridy = gridY;

            this.anchor = GridBagConstraints.LINE_START;
            resizeBehavior(ResizeBehavior.GROW_HORIZONTAL);
            marginVertical(5);
            marginHorizontal(7);
        }

        @Override
        public GridBagRule colSpan(int colSpan) {
            this.gridwidth = colSpan;
            return this;
        }

        @Override
        public GridBagRule resizeBehavior(ResizeBehavior behavior) {
            this.fill = behavior.gbConstant;
            this.weightx = behavior.weightX;
            this.weighty = behavior.weightY;
            return this;
        }

        @Override
        public GridBagRule rowSpan(int rowSpan) {
            this.gridheight = rowSpan;
            return this;
        }

        @Override
        public GridBagRule marginVertical(int marginVertical) {
            // We don't want double margins between the elements, so the top
            // margin is only set for the first element
            if (this.gridy == 0) {
                this.insets.top = marginVertical;
            }
            this.insets.bottom = marginVertical;
            return this;
        }

        @Override
        public GridBagRule marginHorizontal(int marginHorizontal) {
            // We don't want double margins between the elements, so the left
            // margin is only set for the first element
            if (this.gridx == 0) {
                this.insets.left = marginHorizontal;
            }
            this.insets.right = marginHorizontal;
            return this;
        }
    }

    /**
     * How components are allowed to be resized.
     */
    public enum ResizeBehavior {
        DONT_GROW(GridBagConstraints.NONE, 0, 0),
        GROW_BOTH(GridBagConstraints.BOTH, 0.5, 0.5),
        GROW_HORIZONTAL(GridBagConstraints.HORIZONTAL, 0.5, 0),
        GROW_VERTICAL(GridBagConstraints.VERTICAL, 0, 0.5);

        private final int gbConstant;
        private final double weightX;
        private final double weightY;

        private ResizeBehavior(int gbConstant, double weightX, double weightY) {
            this.gbConstant = gbConstant;
            this.weightX = weightX;
            this.weightY = weightY;
        }
    }

    /**
     * Creates a new rule with the specified coords. The rule will dictate by
     * default that the component anchors left and that the overflow happens
     * horizontally.
     * 
     * @param x
     *            X cell coord.
     * @param y
     *            Y cell coord.
     * @return The builder.
     */
    public static GridBagRule cellPos(int x, int y) {
        return new GridBagRuleImpl(x, y);
    }
}
