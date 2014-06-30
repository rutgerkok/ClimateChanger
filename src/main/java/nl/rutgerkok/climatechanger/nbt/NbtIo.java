package nl.rutgerkok.climatechanger.nbt;

/**
 * Copyright Mojang AB.
 *
 * Don't do evil.
 */

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NbtIo {
    public static byte[] compress(CompoundTag tag) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(baos));
        try {
            write(tag, dos);
        } finally {
            dos.close();
        }
        return baos.toByteArray();
    }

    public static CompoundTag decompress(byte[] buffer) throws IOException {
        DataInputStream dis = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(buffer)));
        try {
            return read(dis);
        } finally {
            dis.close();
        }
    }

    public static CompoundTag read(DataInput dis) throws IOException {
        byte typeId = dis.readByte();
        if (typeId != TagType.COMPOUND.getId()) {
            throw new IOException("Root tag must be a named compound tag");
        }

        // Name of root tag is no longer used
        dis.readUTF();

        CompoundTag tag = new CompoundTag(dis);

        return tag;
    }

    public static CompoundTag readCompressedFile(File file) throws IOException {
        if (!file.exists()) {
            return new CompoundTag();
        }
        BufferedInputStream dis = new BufferedInputStream(new FileInputStream(file));
        try {
            return readCompressed(dis);
        } finally {
            dis.close();
        }
    }

    public static CompoundTag readCompressed(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(in)));
        try {
            return read(dis);
        } finally {
            dis.close();
        }
    }

    public static void safeWrite(CompoundTag tag, File file) throws IOException {
        File file2 = new File(file.getAbsolutePath() + "_tmp");
        if (file2.exists()) {
            file2.delete();
        }
        write(tag, file2);
        if (file.exists()) {
            file.delete();
        }
        if (file.exists()) {
            throw new IOException("Failed to delete " + file);
        }
        file2.renameTo(file);
    }

    public static void write(CompoundTag tag, DataOutput dos) throws IOException {
        Tag.writeNamedTag("", tag, dos);
    }

    public static void write(CompoundTag tag, File file) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
        try {
            write(tag, dos);
        } finally {
            dos.close();
        }
    }

    public static void writeCompressed(CompoundTag tag, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(new GZIPOutputStream(out));
        try {
            write(tag, dos);
        } finally {
            dos.close();
        }
    }
}
