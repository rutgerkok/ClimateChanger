package nl.rutgerkok.climatechanger.gui.task.window;

import nl.rutgerkok.climatechanger.gui.LabelWithField;
import nl.rutgerkok.climatechanger.gui.task.GridBagRules;
import nl.rutgerkok.climatechanger.gui.task.GridBagRules.ResizeBehavior;
import nl.rutgerkok.climatechanger.task.OldChunkDeleter;
import nl.rutgerkok.climatechanger.task.Task;
import nl.rutgerkok.climatechanger.util.InvalidTaskException;
import nl.rutgerkok.climatechanger.util.ParseUtil;

import java.awt.GridBagLayout;
import java.text.ParseException;

import javax.swing.JLabel;

public final class OldChunkDeleterPanel extends TaskPanel {

    private final LabelWithField minimumMinutesLoaded;

    public OldChunkDeleterPanel() {
        setLayout(new GridBagLayout());

        add(new JLabel("<html>Minecraft 1.7 and up stores for how long a chunk"
                + " has been loaded. To reduce map size, you can delete chunks"
                + " that have been loaded only a few minutes."),
                GridBagRules.cellPos(0, 0).resizeBehavior(ResizeBehavior.GROW_HORIZONTAL));

        add(minimumMinutesLoaded = new LabelWithField("Minimum minutes loaded", "2"),
                GridBagRules.cellPos(0, 1).resizeBehavior(ResizeBehavior.GROW_HORIZONTAL));

        // Fill remaining space with emptyness
        add(new JLabel(), GridBagRules.cellPos(0, 2).resizeBehavior(ResizeBehavior.GROW_BOTH));
    }

    @Override
    public Task getTask() throws InvalidTaskException {
        String fieldContent = this.minimumMinutesLoaded.getText();
        try {
            int minimumMinutesLoaded = ParseUtil.parseInt(fieldContent, 1, 3600);
            return new OldChunkDeleter(minimumMinutesLoaded);
        } catch (ParseException e) {
            throw new InvalidTaskException(e.getMessage());
        }
    }

}
