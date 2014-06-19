package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataOutput;
import java.io.IOException;
import java.io.PrintStream;

public abstract class Tag {

    public static void writeNamedTag(String name, Tag tag, DataOutput dos) throws IOException {
        dos.writeByte(tag.getType().getId());
        if (tag.getType() == TagType.END) {
            return;
        }

        dos.writeUTF(name);

        tag.write(dos);
    }

    public abstract Tag copy();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }

        Tag other = (Tag) obj;
        if (getType() != other.getType()) {
            return false;
        }
        return true;
    }

    public abstract TagType<?> getType();

    @Override
    public int hashCode() {
        return 31 * getType().hashCode();
    }

    public void print(PrintStream out) {
        print("", out);
    }

    public void print(String prefix, PrintStream out) {
        out.print(prefix);
        out.print(getType().getTagName());
        out.print(": ");
        out.println(toString());
    }

    @Override
    public abstract String toString();

    abstract void write(DataOutput dos) throws IOException;

}
