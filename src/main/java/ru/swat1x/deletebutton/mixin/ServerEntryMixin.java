package ru.swat1x.deletebutton.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.swat1x.deletebutton.DeleteButton;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public abstract class ServerEntryMixin extends MultiplayerServerListWidget.Entry {

  @Shadow public abstract ServerInfo getServer();

  @Inject(method = "render", at = @At("HEAD"))
  public void renderHead(DrawContext context, int index, int y, int x,
                         int entryWidth, int entryHeight, int mouseX,
                         int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
    // TODO: Let's use this pipi kaka
    var replica = DeleteButton.getOrCreateReplica(getServer(), x, y);

    if (isMouseInside(x, y, mouseX, mouseY)) {
      var buttonX = x - 24;
      var buttonY = y + 8;

      context.drawItem(new ItemStack(Blocks.BARRIER), buttonX, buttonY);
    }
  }

  @Unique
  private boolean isMouseInside(int widgetX, int widgetY, int mouseX, int mouseY) {
    return mouseY >= widgetY && mouseY <= widgetY + 32;
  }

}
