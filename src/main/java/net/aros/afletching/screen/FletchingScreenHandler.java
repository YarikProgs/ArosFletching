package net.aros.afletching.screen;

import net.aros.afletching.init.ModOtherThings;
import net.aros.afletching.recipes.FletchingRecipe;
import net.aros.afletching.screen.widgets.IngredientSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FletchingScreenHandler extends ScreenHandler {
    public static final int ARROWHEAD = 0;
    public static final int SHAFT = 1;
    public static final int FLETCHING = 2;
    public static final int INGREDIENT_1 = 3;
    public static final int INGREDIENT_2 = 4;
    public static final int OUTPUT = 5;
    public static final int SIZE = 6;

    private final SimpleInventory input = new SimpleInventory(SIZE - 1) {
        @Override
        public void markDirty() {
            super.markDirty();
            FletchingScreenHandler.this.onContentChanged(this);
        }
    };
    private final CraftingResultInventory output = new CraftingResultInventory();
    private final ScreenHandlerContext context;
    private final World world;
    private boolean fast = false;
    private FletchingRecipe currentRecipe = null;

    public FletchingScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        super(ModOtherThings.FLETCHING_SCREEN_HANDLER, syncId);
        this.context = ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos());
        this.world = inventory.player.world;
        List<FletchingRecipe> recipes = world.getRecipeManager().listAllOfType(FletchingRecipe.Type.INSTANCE);
        this.addSlot(new IngredientSlot(input, ARROWHEAD, 66, 17, stack -> recipes.stream().anyMatch(r -> r.getArrowhead().test(stack))));
        this.addSlot(new IngredientSlot(input, SHAFT, 48, 35, stack -> recipes.stream().anyMatch(r -> r.getShaft().test(stack))));
        this.addSlot(new IngredientSlot(input, FLETCHING, 30, 53, stack -> recipes.stream().anyMatch(r -> r.getFletching().test(stack))));
        this.addSlot(new IngredientSlot(input, INGREDIENT_1, 48, 17, stack -> recipes.stream().anyMatch(r -> r.getIngredient1().test(stack))));
        this.addSlot(new IngredientSlot(input, INGREDIENT_2, 66, 35, stack -> recipes.stream().anyMatch(r -> r.getIngredient2().test(stack))));
        this.addSlot(new Slot(output, OUTPUT, 124, 29) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                FletchingScreenHandler.this.onTakeOutput(player, stack);
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return FletchingScreenHandler.this.canTakeOutput() && !fast;
            }
        });

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < SIZE) {
                if (!this.insertItem(originalStack, SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        if (!newStack.isEmpty() && invSlot == OUTPUT) onTakeOutput(player, newStack);

        return newStack;
    }

    public boolean canTakeOutput() {
        return this.currentRecipe != null && this.currentRecipe.matches(this.input, this.world);
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        fast = slotIndex != OUTPUT;
        super.onSlotClick(slotIndex, button, actionType, player);
        fast = false;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.input) this.updateResult();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return input.canPlayerUse(player);
    }

    private void updateResult() {
        if (world.isClient) return;
        List<FletchingRecipe> list = this.world.getRecipeManager().getAllMatches(FletchingRecipe.Type.INSTANCE, this.input, this.world);
        if (list.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
        } else {
            this.currentRecipe = list.get(0);
            ItemStack itemStack = this.currentRecipe.craft(this.input);
            this.output.setLastRecipe(this.currentRecipe);
            this.output.setStack(0, itemStack);
        }
    }

    private void onTakeOutput(PlayerEntity player, @NotNull ItemStack stack) {
        stack.onCraft(player.world, player, stack.getCount());
        this.output.unlockLastRecipe(player);
        for (int i = 0; i < SIZE - 1; i++) input.getStack(i).decrement(1);
        updateResult();
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }
}
