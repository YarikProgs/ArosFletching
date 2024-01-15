package net.aros.afletching.mixin;

import net.aros.afletching.init.ModOtherThings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FletchingTableBlock.class)
public class FletchingTableBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUseInject(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        AbstractBlock block = (AbstractBlock) (Object) this;
        if (!(block instanceof FletchingTableBlock)) return;

        if (world.isClient) cir.setReturnValue(ActionResult.SUCCESS);

        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        player.incrementStat(ModOtherThings.INTERACT_WITH_FLETCHING_TABLE_STAT);
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}
