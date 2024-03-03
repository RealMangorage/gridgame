package org.mangorage.gridgame.client;

import io.netty.buffer.Unpooled;
import net.querz.nbt.tag.CompoundTag;
import org.mangorage.mangonetwork.core.SimpleByteBuf;

public class Test {
    public static void main(String[] args) {
        CompoundTag tag = new CompoundTag();
        CompoundTag tagB = new CompoundTag();
        tagB.putInt("testb", 1000);
        tag.putInt("test", 100);
        tag.put("testc", tagB);

        SimpleByteBuf byteBuf = new SimpleByteBuf(Unpooled.buffer());

        byteBuf.writeNBT(tag);

        CompoundTag result = byteBuf.readNBT();
        System.out.println(result.getInt("test"));
        System.out.println(result.get("testc", CompoundTag.class).getInt("testb"));

    }
}
