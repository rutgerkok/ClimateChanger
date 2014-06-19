package nl.rutgerkok.climatechanger.nbt;

import java.io.DataInput;
import java.io.IOException;

public class TagType<T extends Tag> {
    private static final TagType<?>[] ALL_TYPES = new TagType<?>[12];

    public static final TagType<NumberTag> BYTE = createTagType("TAG_Byte", 1);
    public static final TagType<ByteArrayTag> BYTE_ARRAY = createTagType("TAG_Byte_Array", 7);
    public static final TagType<CompoundTag> COMPOUND = createTagType("TAG_Compound", 10);
    public static final TagType<NumberTag> DOUBLE = createTagType("TAG_Double", 6);
    public static final TagType<EndTag> END = createTagType("TAG_End", 0);
    public static final TagType<NumberTag> FLOAT = createTagType("TAG_Float", 5);
    public static final TagType<NumberTag> INT = createTagType("TAG_Int", 3);
    public static final TagType<IntArrayTag> INT_ARRAY = createTagType("TAG_Int_Array", 11);
    public static final TagType<ListTag<?>> LIST = createTagType("TAG_List", 9);
    public static final TagType<NumberTag> LONG = createTagType("TAG_Long", 4);
    public static final TagType<NumberTag> SHORT = createTagType("TAG_Short", 2);
    public static final TagType<StringTag> STRING = createTagType("TAG_String", 8);

    private static final <T extends Tag> TagType<T> createTagType(String name, int typeId) {
        TagType<T> tagType = new TagType<T>(name, (byte) typeId);
        ALL_TYPES[typeId] = tagType;
        return tagType;
    }

    /**
     * Gets the tag type with the given id.
     * 
     * @param typeId
     *            The tag type id.
     * @return The tag type.
     * @throws IOException
     *             Thrown if there is no tag type with the given id.
     */
    static TagType<?> fromByte(byte typeId) throws IOException {
        if (typeId < 0 || typeId >= ALL_TYPES.length) {
            throw new IOException();
        }
        return ALL_TYPES[typeId];
    }

    private final String name;

    private final byte typeId;

    private TagType(String name, byte typeId) {
        this.name = name;
        this.typeId = typeId;
    }

    /**
     * Gets the type id of the type.
     * 
     * @return The type id.
     */
    byte getId() {
        return typeId;
    }

    public String getTagName() {
        return name;
    }
    
    @SuppressWarnings("unchecked")
    public T newTag(DataInput input) throws IOException {
        if (this == TagType.END) {
            return (T) new EndTag();
        } else if (this == TagType.BYTE) {
            return (T) new ByteTag(input);
        } else if (this == TagType.BYTE_ARRAY) {
            return (T) new ByteArrayTag(input);
        } else if (this == TagType.SHORT) {
            return (T) new ShortTag(input);
        } else if (this == TagType.INT) {
            return (T) new IntTag(input);
        } else if (this == TagType.LONG) {
            return (T) new LongTag(input);
        } else if (this == TagType.FLOAT) {
            return (T) new FloatTag(input);
        } else if (this == TagType.DOUBLE) {
            return (T) new DoubleTag(input);
        } else if (this == TagType.INT_ARRAY) {
            return (T) new IntArrayTag(input);
        } else if (this == TagType.STRING) {
            return (T) new StringTag(input);
        } else if (this == TagType.LIST) {
            return (T) new ListTag<Tag>(input);
        } else if (this == TagType.COMPOUND) {
            return (T) new CompoundTag(input);
        }
        throw new AssertionError("Unknown tag type: " + this.getTagName());
    }
}
