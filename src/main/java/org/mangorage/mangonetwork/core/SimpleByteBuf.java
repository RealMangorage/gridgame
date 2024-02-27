package org.mangorage.mangonetwork.core;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SimpleByteBuf extends ByteBuf {
    public static void main(String[] args) {
        SimpleByteBuf buf = new SimpleByteBuf(Unpooled.buffer());
        buf.writeString("TEST");
        buf.writeEnum(Side.SERVER);
        System.out.println("Result -> '%s'".formatted(buf.readString()));
        System.out.println(buf.readEnum(Side.class));
    }
    private final ByteBuf source;

    public SimpleByteBuf(ByteBuf source) {
        this.source = source;
    }

    public byte[] readByteArray() {
        byte[] bytes = new byte[readInt()];
        readBytes(bytes);
        return bytes;
    }

    public void writeByteArray(byte[] bytes) {
        writeInt(bytes.length);
        writeBytes(bytes);
    }

    public <E extends Enum> void writeEnum(E Enum) {
        writeInt(Enum.ordinal());
    }

    public <E extends Enum> E readEnum(Class<E> enumClass) {
        int ordinal = readInt();
        E[] enumConstants = enumClass.getEnumConstants();
        if (ordinal >= 0 && ordinal < enumConstants.length) {
            return enumConstants[ordinal];
        } else {
            System.err.println("Not enough data to read enum %s".formatted(enumClass.getName()));
        }
        return null;
    }

    // Write a string to the byte array
    public void writeString(String value) {
        byte[] stringBytes = value.getBytes(StandardCharsets.UTF_8);
        writeInt(stringBytes.length);
        writeBytes(stringBytes);
    }

    // Read a string from the byte array
    public String readString() {
        byte[] bytes = new byte[readInt()];
        readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }


    public int capacity() {
        return this.source.capacity();
    }

    public ByteBuf capacity(int pNewCapacity) {
        return this.source.capacity(pNewCapacity);
    }

    public int maxCapacity() {
        return this.source.maxCapacity();
    }

    public ByteBufAllocator alloc() {
        return this.source.alloc();
    }

    public ByteOrder order() {
        return this.source.order();
    }

    public ByteBuf order(ByteOrder pEndianness) {
        return this.source.order(pEndianness);
    }

    public ByteBuf unwrap() {
        return this.source.unwrap();
    }

    public boolean isDirect() {
        return this.source.isDirect();
    }

    public boolean isReadOnly() {
        return this.source.isReadOnly();
    }

    public ByteBuf asReadOnly() {
        return this.source.asReadOnly();
    }

    public int readerIndex() {
        return this.source.readerIndex();
    }

    public ByteBuf readerIndex(int pReaderIndex) {
        return this.source.readerIndex(pReaderIndex);
    }

    public int writerIndex() {
        return this.source.writerIndex();
    }

    public ByteBuf writerIndex(int pWriterIndex) {
        return this.source.writerIndex(pWriterIndex);
    }

    public ByteBuf setIndex(int pReaderIndex, int pWriterIndex) {
        return this.source.setIndex(pReaderIndex, pWriterIndex);
    }

    public int readableBytes() {
        return this.source.readableBytes();
    }

    public int writableBytes() {
        return this.source.writableBytes();
    }

    public int maxWritableBytes() {
        return this.source.maxWritableBytes();
    }

    public boolean isReadable() {
        return this.source.isReadable();
    }

    public boolean isReadable(int pSize) {
        return this.source.isReadable(pSize);
    }

    public boolean isWritable() {
        return this.source.isWritable();
    }

    public boolean isWritable(int pSize) {
        return this.source.isWritable(pSize);
    }

    public ByteBuf clear() {
        return this.source.clear();
    }

    public ByteBuf markReaderIndex() {
        return this.source.markReaderIndex();
    }

    public ByteBuf resetReaderIndex() {
        return this.source.resetReaderIndex();
    }

    public ByteBuf markWriterIndex() {
        return this.source.markWriterIndex();
    }

    public ByteBuf resetWriterIndex() {
        return this.source.resetWriterIndex();
    }

    public ByteBuf discardReadBytes() {
        return this.source.discardReadBytes();
    }

    public ByteBuf discardSomeReadBytes() {
        return this.source.discardSomeReadBytes();
    }

    public ByteBuf ensureWritable(int pSize) {
        return this.source.ensureWritable(pSize);
    }

    public int ensureWritable(int pSize, boolean pForce) {
        return this.source.ensureWritable(pSize, pForce);
    }

    public boolean getBoolean(int pIndex) {
        return this.source.getBoolean(pIndex);
    }

    public byte getByte(int pIndex) {
        return this.source.getByte(pIndex);
    }

    public short getUnsignedByte(int pIndex) {
        return this.source.getUnsignedByte(pIndex);
    }

    public short getShort(int pIndex) {
        return this.source.getShort(pIndex);
    }

    public short getShortLE(int pIndex) {
        return this.source.getShortLE(pIndex);
    }

    public int getUnsignedShort(int pIndex) {
        return this.source.getUnsignedShort(pIndex);
    }

    public int getUnsignedShortLE(int pIndex) {
        return this.source.getUnsignedShortLE(pIndex);
    }

    public int getMedium(int pIndex) {
        return this.source.getMedium(pIndex);
    }

    public int getMediumLE(int pIndex) {
        return this.source.getMediumLE(pIndex);
    }

    public int getUnsignedMedium(int pIndex) {
        return this.source.getUnsignedMedium(pIndex);
    }

    public int getUnsignedMediumLE(int pIndex) {
        return this.source.getUnsignedMediumLE(pIndex);
    }

    public int getInt(int pIndex) {
        return this.source.getInt(pIndex);
    }

    public int getIntLE(int pIndex) {
        return this.source.getIntLE(pIndex);
    }

    public long getUnsignedInt(int pIndex) {
        return this.source.getUnsignedInt(pIndex);
    }

    public long getUnsignedIntLE(int pIndex) {
        return this.source.getUnsignedIntLE(pIndex);
    }

    public long getLong(int pIndex) {
        return this.source.getLong(pIndex);
    }

    public long getLongLE(int pIndex) {
        return this.source.getLongLE(pIndex);
    }

    public char getChar(int pIndex) {
        return this.source.getChar(pIndex);
    }

    public float getFloat(int pIndex) {
        return this.source.getFloat(pIndex);
    }

    public double getDouble(int pIndex) {
        return this.source.getDouble(pIndex);
    }

    public ByteBuf getBytes(int pIndex, ByteBuf pDestination) {
        return this.source.getBytes(pIndex, pDestination);
    }

    public ByteBuf getBytes(int pIndex, ByteBuf pDestination, int pLength) {
        return this.source.getBytes(pIndex, pDestination, pLength);
    }

    public ByteBuf getBytes(int pIndex, ByteBuf pDestination, int pDestinationIndex, int pLength) {
        return this.source.getBytes(pIndex, pDestination, pDestinationIndex, pLength);
    }

    public ByteBuf getBytes(int pIndex, byte[] pDestination) {
        return this.source.getBytes(pIndex, pDestination);
    }

    public ByteBuf getBytes(int pIndex, byte[] pDestination, int pDestinationIndex, int pLength) {
        return this.source.getBytes(pIndex, pDestination, pDestinationIndex, pLength);
    }

    public ByteBuf getBytes(int pIndex, ByteBuffer pDestination) {
        return this.source.getBytes(pIndex, pDestination);
    }

    public ByteBuf getBytes(int pIndex, OutputStream pOut, int pLength) throws IOException {
        return this.source.getBytes(pIndex, pOut, pLength);
    }

    public int getBytes(int pIndex, GatheringByteChannel pOut, int pLength) throws IOException {
        return this.source.getBytes(pIndex, pOut, pLength);
    }

    public int getBytes(int pIndex, FileChannel pOut, long pPosition, int pLength) throws IOException {
        return this.source.getBytes(pIndex, pOut, pPosition, pLength);
    }

    public CharSequence getCharSequence(int pIndex, int pLength, Charset pCharset) {
        return this.source.getCharSequence(pIndex, pLength, pCharset);
    }

    public ByteBuf setBoolean(int pIndex, boolean pValue) {
        return this.source.setBoolean(pIndex, pValue);
    }

    public ByteBuf setByte(int pIndex, int pValue) {
        return this.source.setByte(pIndex, pValue);
    }

    public ByteBuf setShort(int pIndex, int pValue) {
        return this.source.setShort(pIndex, pValue);
    }

    public ByteBuf setShortLE(int pIndex, int pValue) {
        return this.source.setShortLE(pIndex, pValue);
    }

    public ByteBuf setMedium(int pIndex, int pValue) {
        return this.source.setMedium(pIndex, pValue);
    }

    public ByteBuf setMediumLE(int pIndex, int pValue) {
        return this.source.setMediumLE(pIndex, pValue);
    }

    public ByteBuf setInt(int pIndex, int pValue) {
        return this.source.setInt(pIndex, pValue);
    }

    public ByteBuf setIntLE(int pIndex, int pValue) {
        return this.source.setIntLE(pIndex, pValue);
    }

    public ByteBuf setLong(int pIndex, long pValue) {
        return this.source.setLong(pIndex, pValue);
    }

    public ByteBuf setLongLE(int pIndex, long pValue) {
        return this.source.setLongLE(pIndex, pValue);
    }

    public ByteBuf setChar(int pIndex, int pValue) {
        return this.source.setChar(pIndex, pValue);
    }

    public ByteBuf setFloat(int pIndex, float pValue) {
        return this.source.setFloat(pIndex, pValue);
    }

    public ByteBuf setDouble(int pIndex, double pValue) {
        return this.source.setDouble(pIndex, pValue);
    }

    public ByteBuf setBytes(int pIndex, ByteBuf pSource) {
        return this.source.setBytes(pIndex, pSource);
    }

    public ByteBuf setBytes(int pIndex, ByteBuf pSource, int pLength) {
        return this.source.setBytes(pIndex, pSource, pLength);
    }

    public ByteBuf setBytes(int pIndex, ByteBuf pSource, int pSourceIndex, int pLength) {
        return this.source.setBytes(pIndex, pSource, pSourceIndex, pLength);
    }

    public ByteBuf setBytes(int pIndex, byte[] pSource) {
        return this.source.setBytes(pIndex, pSource);
    }

    public ByteBuf setBytes(int pIndex, byte[] pSource, int pSourceIndex, int pLength) {
        return this.source.setBytes(pIndex, pSource, pSourceIndex, pLength);
    }

    public ByteBuf setBytes(int pIndex, ByteBuffer pSource) {
        return this.source.setBytes(pIndex, pSource);
    }

    public int setBytes(int pIndex, InputStream pIn, int pLength) throws IOException {
        return this.source.setBytes(pIndex, pIn, pLength);
    }

    public int setBytes(int pIndex, ScatteringByteChannel pIn, int pLength) throws IOException {
        return this.source.setBytes(pIndex, pIn, pLength);
    }

    public int setBytes(int pIndex, FileChannel pIn, long pPosition, int pLength) throws IOException {
        return this.source.setBytes(pIndex, pIn, pPosition, pLength);
    }

    public ByteBuf setZero(int pIndex, int pLength) {
        return this.source.setZero(pIndex, pLength);
    }

    public int setCharSequence(int pIndex, CharSequence pCharSequence, Charset pCharset) {
        return this.source.setCharSequence(pIndex, pCharSequence, pCharset);
    }

    public boolean readBoolean() {
        return this.source.readBoolean();
    }

    public byte readByte() {
        return this.source.readByte();
    }

    public short readUnsignedByte() {
        return this.source.readUnsignedByte();
    }

    public short readShort() {
        return this.source.readShort();
    }

    public short readShortLE() {
        return this.source.readShortLE();
    }

    public int readUnsignedShort() {
        return this.source.readUnsignedShort();
    }

    public int readUnsignedShortLE() {
        return this.source.readUnsignedShortLE();
    }

    public int readMedium() {
        return this.source.readMedium();
    }

    public int readMediumLE() {
        return this.source.readMediumLE();
    }

    public int readUnsignedMedium() {
        return this.source.readUnsignedMedium();
    }

    public int readUnsignedMediumLE() {
        return this.source.readUnsignedMediumLE();
    }

    public int readInt() {
        return this.source.readInt();
    }

    public int readIntLE() {
        return this.source.readIntLE();
    }

    public long readUnsignedInt() {
        return this.source.readUnsignedInt();
    }

    public long readUnsignedIntLE() {
        return this.source.readUnsignedIntLE();
    }

    public long readLong() {
        return this.source.readLong();
    }

    public long readLongLE() {
        return this.source.readLongLE();
    }

    public char readChar() {
        return this.source.readChar();
    }

    public float readFloat() {
        return this.source.readFloat();
    }

    public double readDouble() {
        return this.source.readDouble();
    }

    public ByteBuf readBytes(int pLength) {
        return this.source.readBytes(pLength);
    }

    public ByteBuf readSlice(int pLength) {
        return this.source.readSlice(pLength);
    }

    public ByteBuf readRetainedSlice(int pLength) {
        return this.source.readRetainedSlice(pLength);
    }

    public ByteBuf readBytes(ByteBuf pDestination) {
        return this.source.readBytes(pDestination);
    }

    public ByteBuf readBytes(ByteBuf pDestination, int pLength) {
        return this.source.readBytes(pDestination, pLength);
    }

    public ByteBuf readBytes(ByteBuf pDestination, int pDestinationIndex, int pLength) {
        return this.source.readBytes(pDestination, pDestinationIndex, pLength);
    }

    public ByteBuf readBytes(byte[] pDestination) {
        return this.source.readBytes(pDestination);
    }

    public ByteBuf readBytes(byte[] pDestination, int pDestinationIndex, int pLength) {
        return this.source.readBytes(pDestination, pDestinationIndex, pLength);
    }

    public ByteBuf readBytes(ByteBuffer pDestination) {
        return this.source.readBytes(pDestination);
    }

    public ByteBuf readBytes(OutputStream pOut, int pLength) throws IOException {
        return this.source.readBytes(pOut, pLength);
    }

    public int readBytes(GatheringByteChannel pOut, int pLength) throws IOException {
        return this.source.readBytes(pOut, pLength);
    }

    public CharSequence readCharSequence(int pLength, Charset pCharset) {
        return this.source.readCharSequence(pLength, pCharset);
    }

    public int readBytes(FileChannel pOut, long pPosition, int pLength) throws IOException {
        return this.source.readBytes(pOut, pPosition, pLength);
    }

    public ByteBuf skipBytes(int pLength) {
        return this.source.skipBytes(pLength);
    }

    public ByteBuf writeBoolean(boolean pValue) {
        return this.source.writeBoolean(pValue);
    }

    public ByteBuf writeByte(int pValue) {
        return this.source.writeByte(pValue);
    }

    public ByteBuf writeShort(int pValue) {
        return this.source.writeShort(pValue);
    }

    public ByteBuf writeShortLE(int pValue) {
        return this.source.writeShortLE(pValue);
    }

    public ByteBuf writeMedium(int pValue) {
        return this.source.writeMedium(pValue);
    }

    public ByteBuf writeMediumLE(int pValue) {
        return this.source.writeMediumLE(pValue);
    }

    public ByteBuf writeInt(int pValue) {
        return this.source.writeInt(pValue);
    }

    public ByteBuf writeIntLE(int pValue) {
        return this.source.writeIntLE(pValue);
    }

    public ByteBuf writeLong(long pValue) {
        return this.source.writeLong(pValue);
    }

    public ByteBuf writeLongLE(long pValue) {
        return this.source.writeLongLE(pValue);
    }

    public ByteBuf writeChar(int pValue) {
        return this.source.writeChar(pValue);
    }

    public ByteBuf writeFloat(float pValue) {
        return this.source.writeFloat(pValue);
    }

    public ByteBuf writeDouble(double pValue) {
        return this.source.writeDouble(pValue);
    }

    public ByteBuf writeBytes(ByteBuf pSource) {
        return this.source.writeBytes(pSource);
    }

    public ByteBuf writeBytes(ByteBuf pSource, int pLength) {
        return this.source.writeBytes(pSource, pLength);
    }

    public ByteBuf writeBytes(ByteBuf pSource, int pSourceIndex, int pLength) {
        return this.source.writeBytes(pSource, pSourceIndex, pLength);
    }

    public ByteBuf writeBytes(byte[] pSource) {
        return this.source.writeBytes(pSource);
    }

    public ByteBuf writeBytes(byte[] pSource, int pSourceIndex, int pLength) {
        return this.source.writeBytes(pSource, pSourceIndex, pLength);
    }

    public ByteBuf writeBytes(ByteBuffer pSource) {
        return this.source.writeBytes(pSource);
    }

    public int writeBytes(InputStream pIn, int pLength) throws IOException {
        return this.source.writeBytes(pIn, pLength);
    }

    public int writeBytes(ScatteringByteChannel pIn, int pLength) throws IOException {
        return this.source.writeBytes(pIn, pLength);
    }

    public int writeBytes(FileChannel pIn, long pPosition, int pLength) throws IOException {
        return this.source.writeBytes(pIn, pPosition, pLength);
    }

    public ByteBuf writeZero(int pLength) {
        return this.source.writeZero(pLength);
    }

    public int writeCharSequence(CharSequence pCharSequence, Charset pCharset) {
        return this.source.writeCharSequence(pCharSequence, pCharset);
    }

    public int indexOf(int pFromIndex, int pToIndex, byte pValue) {
        return this.source.indexOf(pFromIndex, pToIndex, pValue);
    }

    public int bytesBefore(byte pValue) {
        return this.source.bytesBefore(pValue);
    }

    public int bytesBefore(int pLength, byte pValue) {
        return this.source.bytesBefore(pLength, pValue);
    }

    public int bytesBefore(int pIndex, int pLength, byte pValue) {
        return this.source.bytesBefore(pIndex, pLength, pValue);
    }

    public int forEachByte(ByteProcessor pProcessor) {
        return this.source.forEachByte(pProcessor);
    }

    public int forEachByte(int pIndex, int pLength, ByteProcessor pProcessor) {
        return this.source.forEachByte(pIndex, pLength, pProcessor);
    }

    public int forEachByteDesc(ByteProcessor pProcessor) {
        return this.source.forEachByteDesc(pProcessor);
    }

    public int forEachByteDesc(int pIndex, int pLength, ByteProcessor pProcessor) {
        return this.source.forEachByteDesc(pIndex, pLength, pProcessor);
    }

    public ByteBuf copy() {
        return this.source.copy();
    }

    public ByteBuf copy(int pIndex, int pLength) {
        return this.source.copy(pIndex, pLength);
    }

    public ByteBuf slice() {
        return this.source.slice();
    }

    public ByteBuf retainedSlice() {
        return this.source.retainedSlice();
    }

    public ByteBuf slice(int pIndex, int pLength) {
        return this.source.slice(pIndex, pLength);
    }

    public ByteBuf retainedSlice(int pIndex, int pLength) {
        return this.source.retainedSlice(pIndex, pLength);
    }

    public ByteBuf duplicate() {
        return this.source.duplicate();
    }

    public ByteBuf retainedDuplicate() {
        return this.source.retainedDuplicate();
    }

    public int nioBufferCount() {
        return this.source.nioBufferCount();
    }

    public ByteBuffer nioBuffer() {
        return this.source.nioBuffer();
    }

    public ByteBuffer nioBuffer(int pIndex, int pLength) {
        return this.source.nioBuffer(pIndex, pLength);
    }

    public ByteBuffer internalNioBuffer(int pIndex, int pLength) {
        return this.source.internalNioBuffer(pIndex, pLength);
    }

    public ByteBuffer[] nioBuffers() {
        return this.source.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int pIndex, int pLength) {
        return this.source.nioBuffers(pIndex, pLength);
    }

    public boolean hasArray() {
        return this.source.hasArray();
    }

    public byte[] array() {
        return this.source.array();
    }

    public int arrayOffset() {
        return this.source.arrayOffset();
    }

    public boolean hasMemoryAddress() {
        return this.source.hasMemoryAddress();
    }

    public long memoryAddress() {
        return this.source.memoryAddress();
    }

    public String toString(Charset pCharset) {
        return this.source.toString(pCharset);
    }

    public String toString(int pIndex, int pLength, Charset pCharset) {
        return this.source.toString(pIndex, pLength, pCharset);
    }

    public int hashCode() {
        return this.source.hashCode();
    }

    public boolean equals(Object pOther) {
        return this.source.equals(pOther);
    }

    public int compareTo(ByteBuf pOther) {
        return this.source.compareTo(pOther);
    }

    public String toString() {
        return this.source.toString();
    }

    public ByteBuf retain(int pIncrement) {
        return this.source.retain(pIncrement);
    }

    public ByteBuf retain() {
        return this.source.retain();
    }

    public ByteBuf touch() {
        return this.source.touch();
    }

    public ByteBuf touch(Object pHint) {
        return this.source.touch(pHint);
    }

    public int refCnt() {
        return this.source.refCnt();
    }

    public boolean release() {
        return this.source.release();
    }

    public boolean release(int pDecrement) {
        return this.source.release(pDecrement);
    }
}
