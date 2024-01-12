package ru.swat1x.deletebutton;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class DeleteButton implements ModInitializer {

  public static List<ServerWidgetReplica> ACTUAL_SERVER_WIDGETS = Lists.newArrayList();

  public static ServerWidgetReplica getOrCreateReplica(ServerInfo serverInfo, int x, int y) {
    for (var serverWidget : ACTUAL_SERVER_WIDGETS) {
      if (serverWidget.getServerInfo().equals(serverInfo)) {
        serverWidget.update(x, y);
        return serverWidget;
      }
    }
    var replica = new ServerWidgetReplica(serverInfo, x, y);
    ACTUAL_SERVER_WIDGETS.add(replica);
    return replica;
  }

  @Override
  public void onInitialize() {
    // Пися попа чилен
  }

}
