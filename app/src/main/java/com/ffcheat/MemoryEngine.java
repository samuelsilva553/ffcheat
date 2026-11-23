package com.ffcheat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MemoryEngine {
    // OFFSETS — preencha apos analise do libil2cpp.so no Ghidra
    public static final long LIB_BASE_OFFSET    = 0x0L;
    public static final long ENTITY_LIST_OFF    = 0x0L;
    public static final long ENTITY_STRIDE      = 0x8L;
    public static final long ENTITY_MAX         = 64L;
    public static final long OFFSET_HEALTH      = 0x0L;
    public static final long OFFSET_HEAD_POS    = 0x0L;
    public static final long OFFSET_LOCAL_POS   = 0x0L;
    public static final long OFFSET_VIEW_PITCH  = 0x0L;
    public static final long OFFSET_VIEW_YAW    = 0x0L;

    private final int pid;
    public MemoryEngine(int pid) { this.pid = pid; }
    public int getPid() { return pid; }

    public long readPtr(long addr) {
        byte[] b = ShizukuBridge.readMem(pid, addr, 8);
        if (b == null || b.length < 8) return 0;
        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public float readF32(long addr) {
        byte[] b = ShizukuBridge.readMem(pid, addr, 4);
        if (b == null || b.length < 4) return 0f;
        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public float[] readVec3(long addr) {
        byte[] b = ShizukuBridge.readMem(pid, addr, 12);
        if (b == null || b.length < 12) return null;
        ByteBuffer bb = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
        return new float[]{bb.getFloat(), bb.getFloat(), bb.getFloat()};
    }
}
