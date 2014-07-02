package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CompoundTag extends Tag {
    private final Map<String, Tag> tags = new HashMap<String, Tag>();

    public CompoundTag() {
    }

    CompoundTag(DataInput dis) throws IOException {
        TagType<?> tagType;
        while ((tagType = TagType.fromByte(dis.readByte())) != TagType.END) {
            String name = dis.readUTF();
            Tag tag = tagType.newTag(dis);
            tags.put(name, tag);
        }
    }

    public boolean contains(String name) {
        return tags.containsKey(name);
    }

    @Override
    public Tag copy() {
        CompoundTag tag = new CompoundTag();
        for (String key : tags.keySet()) {
            tag.put(key, tags.get(key).copy());
        }
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            CompoundTag o = (CompoundTag) obj;
            return tags.entrySet().equals(o.tags.entrySet());
        }
        return false;
    }

    public Tag get(String name) {
        return tags.get(name);
    }

    public Collection<Tag> getAllTags() {
        return tags.values();
    }

    public boolean getBoolean(String string) {
        return getByte(string) != 0;
    }

    public byte getByte(String name) {
        if (!(tags.get(name) instanceof NumberTag)) {
            return (byte) 0;
        }
        return ((NumberTag) tags.get(name)).getByte();
    }

    public byte[] getByteArray(String name) {
        if (!(tags.get(name) instanceof ByteArrayTag)) {
            return new byte[0];
        }
        return ((ByteArrayTag) tags.get(name)).data;
    }

    public CompoundTag getCompound(String name) {
        if (!(tags.get(name) instanceof CompoundTag)) {
            return new CompoundTag();
        }
        return (CompoundTag) tags.get(name);
    }

    public double getDouble(String name) {
        if (!(tags.get(name) instanceof NumberTag)) {
            return 0;
        }
        return ((NumberTag) tags.get(name)).getDouble();
    }

    public float getFloat(String name) {
        if (!(tags.get(name) instanceof NumberTag)) {
            return 0;
        }
        return ((NumberTag) tags.get(name)).getFloat();
    }

    public int getInt(String name) {
        if (!(tags.get(name) instanceof NumberTag)) {
            return 0;
        }
        return ((NumberTag) tags.get(name)).getInt();
    }

    public int[] getIntArray(String name) {
        if (!(tags.get(name) instanceof IntArrayTag)) {
            return new int[0];
        }
        return ((IntArrayTag) tags.get(name)).data;
    }

    @SuppressWarnings("unchecked")
    public <T extends Tag> ListTag<T> getList(String name, TagType<T> type) {
        if (!(tags.get(name) instanceof ListTag)) {
            return new ListTag<>(type);
        }
        ListTag<?> listTag = (ListTag<?>) tags.get(name);
        if (listTag.getType() != type) {
            // Tag exists, but child tags are of wrong type
            return new ListTag<>(type);
        }
        return (ListTag<T>) listTag;
    }

    public long getLong(String name) {
        if (!(tags.get(name) instanceof NumberTag)) {
            return 0;
        }
        return ((NumberTag) tags.get(name)).getLong();
    }

    public short getShort(String name) {
        if (!(tags.get(name) instanceof NumberTag)) {
            return (short) 0;
        }
        return ((NumberTag) tags.get(name)).getShort();
    }

    public String getString(String name) {
        if (!(tags.get(name) instanceof StringTag)) {
            return "";
        }
        return ((StringTag) tags.get(name)).data;
    }

    @Override
    public TagType<?> getType() {
        return TagType.COMPOUND;
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    @Override
    public void print(String prefix, PrintStream out) {
        super.print(prefix, out);
        out.println(prefix + "{");
        String orgPrefix = prefix;
        prefix += "   ";
        for (Entry<String, Tag> tag : tags.entrySet()) {
            out.print(tag.getKey());
            out.print(": ");
            tag.getValue().print(prefix, out);
        }
        out.println(orgPrefix + "}");
    }

    public void put(String name, Tag tag) {
        tags.put(name, tag);
    }

    public void putBoolean(String string, boolean val) {
        putByte(string, val ? (byte) 1 : 0);
    }

    public void putByte(String name, byte value) {
        tags.put(name, new ByteTag(value));
    }

    public void putByteArray(String name, byte[] value) {
        tags.put(name, new ByteArrayTag(value));
    }

    public void putCompound(String name, CompoundTag value) {
        tags.put(name, value);
    }

    public void putDouble(String name, double value) {
        tags.put(name, new DoubleTag(value));
    }

    public void putFloat(String name, float value) {
        tags.put(name, new FloatTag(value));
    }

    public void putInt(String name, int value) {
        tags.put(name, new IntTag(value));
    }

    public void putIntArray(String name, int[] value) {
        tags.put(name, new IntArrayTag(value));
    }

    public void putLong(String name, long value) {
        tags.put(name, new LongTag(value));
    }

    public void putShort(String name, short value) {
        tags.put(name, new ShortTag(value));
    }

    public void putString(String name, String value) {
        tags.put(name, new StringTag(value));
    }

    @Override
    public String toString() {
        return "" + tags.size() + " entries";
    }

    @Override
    void write(DataOutput dos) throws IOException {
        for (Entry<String, Tag> tag : tags.entrySet()) {
            Tag.writeNamedTag(tag.getKey(), tag.getValue(), dos);
        }
        dos.writeByte(TagType.END.getId());
    }
}
