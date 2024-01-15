package ru.swat1x.deletebutton.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import ru.swat1x.deletebutton.DeleteButton;
import ru.swat1x.deletebutton.config.screen.MaterialSelectScreen;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

  @Unique
  private static final Logger logger = LoggerFactory.getLogger("DeleteButton");

  @Shadow
  public abstract ServerList getServerList();

  @Shadow
  public abstract void select(MultiplayerServerListWidget.Entry entry);

  @Shadow
  protected MultiplayerServerListWidget serverListWidget;

  protected MultiplayerScreenMixin(Text title) {
    super(title);
  }

  // Click hook!
  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
//    if (DeleteButton.getInstance().getConfig().isEnabled()) {
    for (var widget : DeleteButton.ACTUAL_SERVER_WIDGETS) {
      if (widget.isMouseOnButton(mouseX, mouseY)) {
        if (Screen.hasShiftDown() || Screen.hasControlDown()) {
          client.setScreen(new MaterialSelectScreen(this));
        } else {
          var serverInfo = widget.getServerInfo();
          select(null);
          removeServer(serverInfo);
          serverListWidget.setServers(getServerList());
          logger.info("Server {} ({}) removed via DeleteButton", serverInfo.name, serverInfo.address);
        }
      }
    }
//    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Unique
  private void removeServer(ServerInfo serverInfo) {
    getServerList().remove(serverInfo);
    getServerList().saveFile();
  }

  @Override
  public void close() {
    super.close();
    DeleteButton.ACTUAL_SERVER_WIDGETS.clear();
  }
}
