package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ShortTag extends NumberTag {
    private final short data;

    ShortTag(DataInput dis) throws IOException {
        data = dis.readShort();
    }

    public ShortTag(short data) {
        this.data = data;
    }

    @Override
    public Tag copy() {
        return new ShortTag(data);
    }

    @Override
    public byte getByte() {
        return (byte) data;
    }

    @Override
    public double getDouble() {
        return data;
    }

    @Override
    public float getFloat() {
        return data;
    }

    @Override
    public int getInt() {
        return data;
    }

    @Override
    public long getLong() {
        return data;
    }

    @Override
    public short getShort() {
        return data;
    }

    @Override
    public TagType<?> getType() {
        return TagType.SHORT;
    }

    @Override
    public String toString() {
        return "" + data;
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeShort(data);
    }

}
