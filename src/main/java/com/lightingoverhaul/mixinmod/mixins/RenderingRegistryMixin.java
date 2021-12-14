package com.lightingoverhaul.mixinmod.mixins;

import java.util.Map;

import com.lightingoverhaul.mixinmod.interfaces.ITessellatorMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderingRegistry.class)
public abstract class RenderingRegistryMixin {
    @Redirect(method = "renderWorldBlock",
              at = @At(value = "INVOKE",
                       target = "Lcpw/mods/fml/client/registry/ISimpleBlockRenderingHandler;renderWorldBlock(Lnet/minecraft/world/IBlockAccess;IIILnet/minecraft/block/Block;ILnet/minecraft/client/renderer/RenderBlocks;)Z"),
              remap = false,
              require = 1)
    public boolean renderWorldBlock(ISimpleBlockRenderingHandler bri, IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        ITessellatorMixin tessellatorMixin = (ITessellatorMixin) Tessellator.instance;

        String className = block.getClass().getName();
        boolean doLock = className.startsWith("com.carpentersblocks.");

        if (doLock) {
            int light = block.getMixedBrightnessForBlock(world, x, y, z);
            Tessellator.instance.setBrightness(light);
            tessellatorMixin.setLockedBrightness(true);
        }
        boolean ret = bri.renderWorldBlock(world, x, y, z, block, modelId, renderer);
        if (doLock) {
            tessellatorMixin.setLockedBrightness(false);
        }

        return ret;
    }
}
