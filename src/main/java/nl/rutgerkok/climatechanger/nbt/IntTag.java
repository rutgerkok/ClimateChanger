package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntTag extends NumberTag {
    private final int data;

    IntTag(DataInput dis) throws IOException {
        data = dis.readInt();
    }

    public IntTag(int data) {
        this.data = data;
    }

    @Override
    public Tag copy() {
        return new IntTag(data);
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
        return (short) data;
    }

    @Override
    public TagType<?> getType() {
        return TagType.INT;
    }

    @Override
    public String toString() {
        return "" + data;
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeInt(data);
    }

}
