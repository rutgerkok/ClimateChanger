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
import java.util.ArrayList;
import java.util.List;

public class ListTag<T extends Tag> extends Tag {
    private final List<T> list = new ArrayList<T>();
    private final TagType<T> type;

    @SuppressWarnings("unchecked")
    ListTag(DataInput dis) throws IOException {
        type = (TagType<T>) TagType.fromByte(dis.readByte());
        int size = dis.readInt();

        for (int i = 0; i < size; i++) {
            Tag tag = type.newTag(dis);
            list.add((T) tag);
        }
    }

    ListTag(TagType<T> type) {
        this.type = type;
    }

    public void add(T tag) {
        list.add(tag);
    }

    @Override
    public Tag copy() {
        ListTag<T> res = new ListTag<T>(type);
        for (T t : list) {
            @SuppressWarnings("unchecked")
            T copy = (T) t.copy();
            res.list.add(copy);
        }
        return res;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            ListTag o = (ListTag) obj;
            if (type == o.type) {
                return list.equals(o.list);
            }
        }
        return false;
    }

    public T get(int index) {
        return list.get(index);
    }

    @Override
    public TagType<?> getType() {
        return TagType.LIST;
    }

    @Override
    public void print(String prefix, PrintStream out) {
        super.print(prefix, out);

        out.println(prefix + "{");
        String orgPrefix = prefix;
        prefix += "   ";
        for (int i = 0; i < list.size(); i++) {
            list.get(i).print(prefix, out);
        }
        out.println(orgPrefix + "}");
    }

    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        return "" + list.size() + " entries of type " + type.getTagName();
    }

    @Override
    void write(DataOutput dos) throws IOException {
        dos.writeByte(type.getId());
        dos.writeInt(list.size());
        for (int i = 0; i < list.size(); i++) {
            list.get(i).write(dos);
        }
    }

}
