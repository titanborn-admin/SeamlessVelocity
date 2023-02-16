package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.CompressedProcesser;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTReader;
import org.jglrxavpok.hephaistos.nbt.NBTWriter;

// TODO: clean up this packet
public class ChunkDataAndUpdateLight implements MinecraftPacket {

    private int x;
    private int z;
    private final ChunkData chunkData = new ChunkData();
    private final LightData lightData = new LightData();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public ChunkData getChunkData() {
        return chunkData;
    }

    public LightData getLightData() {
        return lightData;
    }

    @Override
    public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        this.x = buf.readInt();
        this.z = buf.readInt();
        this.chunkData.decode(buf, direction, protocolVersion);
        this.lightData.decode(buf, direction, protocolVersion);
    }

    @Override
    public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
        buf.writeInt(x);
        buf.writeInt(z);
        this.chunkData.encode(buf, direction, protocolVersion);
        this.lightData.encode(buf, direction, protocolVersion);
    }

    @Override
    public boolean handle(MinecraftSessionHandler handler) {
        return handler.handle(this);
    }

    private static @NotNull NBT readNBT(@NotNull ByteBuf buf) {
        try (NBTReader reader = new NBTReader(new InputStream() {
            @Override
            public int read() {
                return buf.readByte() & 0xFF;
            }

            @Override
            public int available() {
                return buf.readableBytes();
            }
        }, CompressedProcesser.NONE)) {
            return reader.read();
        } catch (IOException | NBTException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeNBT(@NotNull ByteBuf buf, @NotNull NBT nbt) {
        try (NBTWriter writer = new NBTWriter(new OutputStream() {
            @Override
            public void write(int b) {
                buf.writeByte(b);
            }
        }, CompressedProcesser.NONE)) {
            writer.writeNamed("", nbt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ChunkData {

        private NBTCompound heightmaps;
        private byte[] data;
        private Map<Integer, BlockEntity> blockEntities;

        public NBTCompound getHeightmaps() {
            return heightmaps;
        }

        public void setHeightmaps(NBTCompound heightmaps) {
            this.heightmaps = heightmaps;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public Map<Integer, BlockEntity> getBlockEntities() {
            return blockEntities;
        }

        public void setBlockEntities(Map<Integer, BlockEntity> blockEntities) {
            this.blockEntities = blockEntities;
        }

        public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
            this.heightmaps = (NBTCompound) readNBT(buf);
            this.data = ProtocolUtils.readByteArray(buf);
            this.blockEntities = readBlockEntities(buf, direction, protocolVersion);
        }

        public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
            writeNBT(buf, heightmaps);
            ProtocolUtils.writeByteArray(buf, data);
            ProtocolUtils.writeVarInt(buf, blockEntities.size());
            for (BlockEntity blockEntity : blockEntities.values()) blockEntity.encode(buf, direction, protocolVersion);
        }

        private static Map<Integer, BlockEntity> readBlockEntities(@NotNull ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
            Map<Integer, BlockEntity> blockEntities = new HashMap<>();
            int size = ProtocolUtils.readVarInt(buf);
            for (int i = 0; i < size; i++) {
                BlockEntity blockEntity = new BlockEntity();
                blockEntity.decode(buf, direction, protocolVersion);
                blockEntities.put(i, blockEntity);
            }
            return blockEntities;
        }

    }

    public static class LightData {

        private boolean trustEdges;
        private BitSet skyMask;
        private BitSet blockMask;
        private BitSet emptySkyMask;
        private BitSet emptyBlockMask;
        private List<byte[]> skyLight;
        private List<byte[]> blockLight;

        public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
            this.trustEdges = buf.readBoolean();
            this.skyMask = BitSet.valueOf(ProtocolUtils.readLongArray(buf));
            this.blockMask = BitSet.valueOf(ProtocolUtils.readLongArray(buf));
            this.emptySkyMask = BitSet.valueOf(ProtocolUtils.readLongArray(buf));
            this.emptyBlockMask = BitSet.valueOf(ProtocolUtils.readLongArray(buf));

            int skyLightSize = ProtocolUtils.readVarInt(buf);
            this.skyLight = new ArrayList<>(skyLightSize);
            for (int i = 0; i < skyLightSize; i++) this.skyLight.add(ProtocolUtils.readByteArray(buf));

            int blockLightSize = ProtocolUtils.readVarInt(buf);
            this.blockLight = new ArrayList<>(blockLightSize);
            for (int i = 0; i < blockLightSize; i++) this.blockLight.add(ProtocolUtils.readByteArray(buf));
        }

        public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
            buf.writeBoolean(trustEdges);
            ProtocolUtils.writeLongArray(buf, skyMask.toLongArray());
            ProtocolUtils.writeLongArray(buf, blockMask.toLongArray());
            ProtocolUtils.writeLongArray(buf, emptySkyMask.toLongArray());
            ProtocolUtils.writeLongArray(buf, emptyBlockMask.toLongArray());

            ProtocolUtils.writeVarInt(buf, skyLight.size());
            for (byte[] bytes : skyLight) ProtocolUtils.writeByteArray(buf, bytes);

            ProtocolUtils.writeVarInt(buf, blockLight.size());
            for (byte[] bytes : blockLight) ProtocolUtils.writeByteArray(buf, bytes);
        }

    }

    public static class BlockEntity {

        private byte xz;
        private short y;
        private int blockEntityId;
        private NBTCompound nbt;

        public void decode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
            xz = buf.readByte();
            y = buf.readShort();
            blockEntityId = ProtocolUtils.readVarInt(buf);
            nbt = (NBTCompound) readNBT(buf);
        }

        public void encode(ByteBuf buf, ProtocolUtils.Direction direction, ProtocolVersion protocolVersion) {
            buf.writeByte(xz);
            buf.writeShort(y);
            ProtocolUtils.writeVarInt(buf, blockEntityId);
            writeNBT(buf, nbt);
        }

    }

}
