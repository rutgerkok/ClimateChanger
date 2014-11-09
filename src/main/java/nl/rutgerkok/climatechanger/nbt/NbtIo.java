package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class NbtIo {

    public static final CompoundTag read(DataInput dis) throws IOException {
        byte typeId = dis.readByte();
        if (typeId != TagType.COMPOUND.getId()) {
            throw new IOException("Root tag must be a named compound tag");
        }

        // Name of root tag is no longer used
        dis.readUTF();

        CompoundTag tag = new CompoundTag(dis);

        return tag;
    }

    private static final CompoundTag readCompressed(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(in)));
        try {
            return read(dis);
        } finally {
            dis.close();
        }
    }

    public static final CompoundTag readCompressedFile(Path levelDat) throws IOException {
        if (!Files.exists(levelDat)) {
            return new CompoundTag();
        }
        InputStream dis = Files.newInputStream(levelDat);
        try {
            return readCompressed(dis);
        } finally {
            dis.close();
        }
    }

    public static final void safeWriteCompressed(CompoundTag tag, Path file) throws IOException {
        Path file2 = file.resolveSibling(file.getFileName() + "_tmp");
        if (Files.exists(file2)) {
            Files.delete(file2);
        }
        writeCompressed(tag, file2);
        if (Files.exists(file)) {
            Files.delete(file);
        }
        if (Files.exists(file)) {
            throw new IOException("Failed to delete " + file);
        }
        Files.move(file2, file);
    }

    public static final void write(CompoundTag tag, DataOutput dos) throws IOException {
        Tag.writeNamedTag("", tag, dos);
    }

    public static final void writeCompressed(CompoundTag tag, Path out) throws IOException {
        DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(Files.newOutputStream(out)));
        try {
            write(tag, dos);
        } finally {
            dos.close();
        }
    }
}
