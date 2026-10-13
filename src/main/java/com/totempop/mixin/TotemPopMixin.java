package com.totempop.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class TotemPopMixin {

    // Entity status 35 = "totem of undying used"
    private static final byte TOTEM_STATUS = 35;

    @Inject(method = "onEntityStatus", at = @At("HEAD"))
    private void totempop$onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (packet.getStatus() != TOTEM_STATUS) return;

        MinecraftClient client = MinecraftClient.getInstance();
        // Packet handlers get invoked once off-thread then re-run on the main thread; only act once.
        if (!client.isOnThread()) return;

        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return;

        // Only trigger when it's YOUR totem that popped
        if (packet.getEntity(client.world) != player) return;

        player.networkHandler.sendChatMessage("p");
    }
}
