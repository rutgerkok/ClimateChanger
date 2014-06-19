package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DoubleTag extends NumberTag {
    private final double data;

    DoubleTag(DataInput dis) throws IOException {
        data = dis.readDouble();
    }

    public DoubleTag(double data) {
        this.data = data;
    }

    @Override
    public Tag copy() {
        return new DoubleTag(data);
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
        return (float) data;
    }

    @Override
    public int getInt() {
        return (int) data;
    }

    @Override
    public long getLong() {
        return (long) data;
    }

    @Override
    public short getShort() {
        return (short) data;
    }

    @Override
    public TagType<?> getType() {
        return TagType.DOUBLE;
    }

    @Override
    public String toString() {
        return "" + data;
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeDouble(data);
    }

}
