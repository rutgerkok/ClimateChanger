package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataOutput;
import java.io.IOException;

public class EndTag extends Tag {

    public EndTag() {
    }

    @Override
    public Tag copy() {
        return new EndTag();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public TagType<?> getType() {
        return TagType.END;
    }

    @Override
    public String toString() {
        return "END";
    }

    @Override
    void write(DataOutput dos) throws IOException {
    }

}
