package io.wdsj.hybridfix.mixin.late.techguns.tick;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import techguns.capabilities.TGExtendedPlayer;
import techguns.events.TGTickHandler;

@Mixin(value = TGTickHandler.class, remap = false)
public class TGTickHandlerMixin {
    @Redirect(method = "onPlayerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/datasync/EntityDataManager;set(Lnet/minecraft/network/datasync/DataParameter;Ljava/lang/Object;)V"), remap = false)
    private static <T> void safeDataManagerSet(EntityDataManager dataManager, DataParameter<T> key, T value, PlayerTickEvent event) {
        try {
            if (dataManager != null && key != null && value != null && 
                event.player != null && !event.player.isDead && event.player.world != null) {
                dataManager.set(key, value);
            }
        } catch (Exception e) {
            // no-op
        }
    }

    @Redirect(method = "onPlayerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/datasync/EntityDataManager;get(Lnet/minecraft/network/datasync/DataParameter;)Ljava/lang/Object;"), remap = false)
    private static <T> T safeDataManagerGet(EntityDataManager dataManager, DataParameter<T> key, PlayerTickEvent event) {
        try {
            if (dataManager != null && key != null && 
                event.player != null && !event.player.isDead && event.player.world != null) {
                return dataManager.get(key);
            }
        } catch (Exception e) {
            // no-op
        }
        return null;
    }
}