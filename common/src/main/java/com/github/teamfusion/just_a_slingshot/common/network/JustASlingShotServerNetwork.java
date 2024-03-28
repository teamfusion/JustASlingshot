package com.github.teamfusion.just_a_slingshot.common.network;

import com.github.teamfusion.just_a_slingshot.common.item.slingshot.SlingshotPouchItem;
import com.github.teamfusion.just_a_slingshot.common.registry.ItemRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class JustASlingShotServerNetwork implements JustASlingShotNetwork {

    public static void registerReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SELECT_SYNC_PACKET, JustASlingShotServerNetwork::onSelectSync);
    }

    private static void onSelectSync(FriendlyByteBuf friendlyByteBuf, NetworkManager.PacketContext packetContext) {
        int id = friendlyByteBuf.readInt();
        Player player = packetContext.getPlayer();
        ItemStack pouch = player.getMainHandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getMainHandItem() : player.getOffhandItem().is(ItemRegistry.SLINGSHOT_POUCH.get()) ? player.getOffhandItem() : ItemStack.EMPTY;

        if (!pouch.isEmpty()) {
            SlingshotPouchItem.cycle(id, pouch);
        }
    }
}
