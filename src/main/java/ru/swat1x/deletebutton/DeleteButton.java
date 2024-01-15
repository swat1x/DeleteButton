package ru.swat1x.deletebutton;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import org.apache.commons.compress.utils.Lists;
import ru.swat1x.deletebutton.config.ConfigRepository;
import ru.swat1x.deletebutton.config.entity.DeleteButtonConfigEntity;
import ru.swat1x.deletebutton.config.repository.GSONConfigRepository;

import java.io.IOException;
import java.util.List;

@Log4j2
@Getter
public class DeleteButton implements ModInitializer {

  @Getter
  private static DeleteButton instance;
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

  public static void saveNewMaterial(Item item) {
    var config = DeleteButton.getInstance().getConfig();
    config.setMaterial(Registries.ITEM.getId(item).toString());
    DeleteButton.getInstance().getConfigRepository().save(config);
    log.info("New material saved! It's {}", config.getMaterial());
  }

  DeleteButtonConfigEntity config;
  ConfigRepository<DeleteButtonConfigEntity> configRepository;

  @Override
  public void onInitialize() {
    instance = this;
    this.configRepository = new GSONConfigRepository();

    // Config actions
    try {
      var configPath = FabricLoader.getInstance().getConfigDir().resolve("delete-button-config.json");
      // Loading config
      config = configRepository.loadOrSave(
              configPath,
              DeleteButtonConfigEntity::new
      );
      // Add saving hook
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          configRepository.save(config, configPath);
        } catch (IOException e) {
          log.error("Can't save configuration", e);
        }
      }));
    } catch (IOException e) {
      log.error("Can't load configuration", e);
    }
  }

}
