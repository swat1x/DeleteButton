package ru.swat1x.deletebutton;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;

public class ServerWidgetReplica {

  private final ServerInfo serverInfo;
  private int x;
  private int y;

  public ServerWidgetReplica(ServerInfo serverInfo,
                             int x, int y) {
    this.serverInfo = serverInfo;
    update(x, y);
  }

  public void update(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public ServerInfo getServerInfo() {
    return serverInfo;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isMouseOnButton(double mouseX,
                                 double mouseY) {
    return (mouseX >= x - 32 && mouseX <= x)
            && (mouseY <= y + 32 && mouseY >= y);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ServerWidgetReplica replica) {
      return replica.getServerInfo().equals(serverInfo);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 0;
  }

}
