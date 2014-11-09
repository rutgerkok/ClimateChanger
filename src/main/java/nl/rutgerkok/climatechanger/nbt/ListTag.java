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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListTag<T extends Tag> extends Tag implements List<T> {
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

    @Override
    public void add(int index, T element) {
        list.add(index, element);
    }

    @Override
    public boolean add(T tag) {
        return list.add(tag);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
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

    @Override
    public T get(int index) {
        return list.get(index);
    }

    /**
     * Gets the type of the elements in the list.
     *
     * @return The type of the elements.
     */
    public TagType<T> getElementsType() {
        return type;
    }

    @Override
    public TagType<?> getType() {
        return TagType.LIST;
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
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

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
        return list.toArray(a);
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
