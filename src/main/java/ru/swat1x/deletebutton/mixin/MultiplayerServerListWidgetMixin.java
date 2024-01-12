package ru.swat1x.deletebutton.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.option.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.swat1x.deletebutton.DeleteButton;

import java.util.List;

@Mixin(MultiplayerServerListWidget.class)
public abstract class MultiplayerServerListWidgetMixin {

  @Shadow @Final private List<MultiplayerServerListWidget.ServerEntry> servers;

  @Inject(method = "setServers", at = @At("HEAD"))
  public void render(ServerList serverList, CallbackInfo ci) {

  }

}
