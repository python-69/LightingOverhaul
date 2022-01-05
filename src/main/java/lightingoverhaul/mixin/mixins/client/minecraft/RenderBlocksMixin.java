package lightingoverhaul.mixin.mixins.client.minecraft;

import lightingoverhaul.api.LightingApi;
import lightingoverhaul.helper.BlockHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderBlocks.class)
@SideOnly(Side.CLIENT)
public abstract class RenderBlocksMixin {

    @Redirect(method = { "renderBlockLiquid" },
              at = @At(value = "INVOKE",
                       target = "net.minecraft.block.Block.getMixedBrightnessForBlock(Lnet/minecraft/world/IBlockAccess;III)I"),
              require = 3)
    public int renderBlockLiquid_inject(Block block, IBlockAccess blockAccess, int x, int y, int z) {
        return BlockHelper.getMixedBrightnessForBlockWithColor(blockAccess, x, y + 1, z);
    }

    @Inject(method = "getAoBrightness",
            at = @At(value = "HEAD"),
            cancellable = true,
            require = 1
    )
    public void getAoBrightness(int a, int b, int c, int l, CallbackInfoReturnable<Integer> cir) {
        // Must mix all 5 channels now
        cir.setReturnValue((1 << 30) | mixColorChannel(LightingApi._bitshift_sun_r2, a, b, c, l) | // SSSS
                           mixColorChannel(LightingApi._bitshift_sun_g2, a, b, c, l) | // SSSS
                           mixColorChannel(LightingApi._bitshift_sun_b2, a, b, c, l) | // SSSS
                           mixColorChannel(LightingApi._bitshift_b2, a, b, c, l) | // BBBB
                           mixColorChannel(LightingApi._bitshift_g2, a, b, c, l) | // GGGG this is the problem child
                           mixColorChannel(LightingApi._bitshift_r2, a, b, c, l) | // RRRR
                           mixColorChannel(LightingApi._bitshift_l2, a, b, c, l) // LLLL
        );
    }

    @Inject(method = "mixAoBrightness",
            at = @At(value = "HEAD"),
            cancellable = true,
            require = 1)
    private void mixAoBrightness(int a, int b, int c, int d, double fA, double fB, double fC, double fD, CallbackInfoReturnable<Integer> cir) {
        // Must mix all 5 channels now
        cir.setReturnValue((1 << 30) | mixColorChannel(LightingApi._bitshift_sun_r2, a, b, c, d, fA, fB, fC, fD) | // SSSS
                           mixColorChannel(LightingApi._bitshift_sun_g2, a, b, c, d, fA, fB, fC, fD) | // SSSS
                           mixColorChannel(LightingApi._bitshift_sun_b2, a, b, c, d, fA, fB, fC, fD) | // SSSS
                           mixColorChannel(LightingApi._bitshift_b2, a, b, c, d, fA, fB, fC, fD) | // BBBB
                           mixColorChannel(LightingApi._bitshift_g2, a, b, c, d, fA, fB, fC, fD) | // GGGG this is the problem child
                           mixColorChannel(LightingApi._bitshift_r2, a, b, c, d, fA, fB, fC, fD) | // RRRR
                           mixColorChannel(LightingApi._bitshift_l2, a, b, c, d, fA, fB, fC, fD) // LLLL
                          );
    }

    private int mixColorChannel(int startBit, int p1, int p2, int p3, int p4, double d1, double d2, double d3, double d4) {
        double sum;

        double q1 = (p1 >>> startBit) & 0xf;
        double q2 = (p2 >>> startBit) & 0xf;
        double q3 = (p3 >>> startBit) & 0xf;
        double q4 = (p4 >>> startBit) & 0xf;

        q1 *= d1;
        q2 *= d2;
        q3 *= d3;
        q4 *= d4;

        sum = Math.max(Math.min(15.0, q1 + q2 + q3 + q4), 0.0);

        return ((int)sum & 0xF) << startBit;
    }

    private int mixColorChannel(int startBit, int p1, int p2, int p3, int p4) {
        int avg;

        int q1 = (p1 >>> startBit) & 0xf;
        int q2 = (p2 >>> startBit) & 0xf;
        int q3 = (p3 >>> startBit) & 0xf;
        int q4 = (p4 >>> startBit) & 0xf;

        if (q1 == 0) q1 = q4;
        if (q2 == 0) q2 = q4;
        if (q3 == 0) q3 = q4;

        avg = (q1 + q2 + q3 + q4) / 4;

        return avg << startBit;
    }
}
