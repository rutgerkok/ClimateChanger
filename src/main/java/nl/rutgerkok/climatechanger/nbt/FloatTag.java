package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FloatTag extends NumberTag {
    private final float data;

    FloatTag(DataInput dis) throws IOException {
        data = dis.readFloat();
    }

    public FloatTag(float data) {
        this.data = data;
    }

    @Override
    public Tag copy() {
        return new FloatTag(data);
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
        return (long) data;
    }

    @Override
    public short getShort() {
        return (short) data;
    }

    @Override
    public TagType<?> getType() {
        return TagType.FLOAT;
    }

    @Override
    public String toString() {
        return "" + data;
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeFloat(data);
    }

}
