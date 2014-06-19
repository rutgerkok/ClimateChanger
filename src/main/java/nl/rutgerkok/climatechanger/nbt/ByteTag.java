package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ByteTag extends NumberTag {
    private final byte data;

    public ByteTag(byte data) {
        this.data = data;
    }

    ByteTag(DataInput dis) throws IOException {
        data = dis.readByte();
    }

    @Override
    public Tag copy() {
        return new ByteTag(data);
    }

    @Override
    public byte getByte() {
        return data;
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
        return TagType.BYTE;
    }

    @Override
    public String toString() {
        return "" + data;
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeByte(data);
    }
}
