package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LongTag extends NumberTag {
    private final long data;

    LongTag(DataInput dis) throws IOException {
        data = dis.readLong();
    }

    public LongTag(long data) {
        this.data = data;
    }

    @Override
    public Tag copy() {
        return new LongTag(data);
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
        return (int) data;
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
        return TagType.LONG;
    }

    @Override
    public String toString() {
        return "" + data;
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeLong(data);
    }

}
