package io.wdsj.hybridfix.mixin.late.techguns.tick;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import techguns.events.TGTickHandler;
import io.wdsj.hybridfix.HybridFix;

@Mixin(value = TGTickHandler.class, remap = false)
public class TGTickHandlerMixin {

    @WrapOperation(method = "onPlayerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/datasync/EntityDataManager;set(Lnet/minecraft/network/datasync/DataParameter;Ljava/lang/Object;)V"), remap = false)
    private static <T> void safeDataManagerSet(EntityDataManager dataManager, DataParameter<T> key, T value, Operation<Void> original) {
        try {
            original.call(dataManager, key, value);
        } catch (Exception e) {
            HybridFix.LOGGER.warn("Failed to set DataManager value during TechGuns player tick", e);
        }
    }
    
    @WrapOperation(method = "onPlayerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/datasync/EntityDataManager;get(Lnet/minecraft/network/datasync/DataParameter;)Ljava/lang/Object;"), remap = false)
    private static <T> T safeDataManagerGet(EntityDataManager dataManager, DataParameter<T> key, Operation<T> original) {
        try {
            return original.call(dataManager, key);
        } catch (Exception e) {
            HybridFix.LOGGER.warn("Failed to get DataManager value during TechGuns player tick", e);
            return null;
        }
    }
}