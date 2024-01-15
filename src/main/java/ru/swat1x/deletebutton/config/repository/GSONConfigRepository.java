package ru.swat1x.deletebutton.config.repository;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import ru.swat1x.deletebutton.config.ConfigRepository;
import ru.swat1x.deletebutton.config.entity.DeleteButtonConfigEntity;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GSONConfigRepository implements ConfigRepository<DeleteButtonConfigEntity> {

  Path lastPath;

  @Override
  public Class<DeleteButtonConfigEntity> getFileClass() {
    return DeleteButtonConfigEntity.class;
  }

  @Override
  public DeleteButtonConfigEntity loadOrSave(Path filePath,
                                             Supplier<DeleteButtonConfigEntity> defaultSupplier) throws IOException {
    this.lastPath = filePath;
    var file = filePath.toFile();
    if (!file.exists()) {
      // supplying default config
      var defaultConfig = defaultSupplier.get();
      save(defaultConfig, filePath); // saving :)
      return defaultConfig;
    } else {
      // loading exists file
      return load(filePath).orElseGet(defaultSupplier);
    }
  }

  @Override
  public Optional<DeleteButtonConfigEntity> load(Path filePath) {
    this.lastPath = filePath;
    var file = filePath.toFile();
    if (!file.isFile() || !file.exists()) return Optional.empty();
    // start reading file
    try (var reader = new FileReader(file)) {
      return Optional.of(new Gson().fromJson(reader, getFileClass()));
    } catch (IOException e) {
      log.error("Can't read config file in {}", filePath.toString(), e);
      return Optional.empty();
    }
  }

  @Override
  public void save(DeleteButtonConfigEntity instance, Path filePath) throws IOException {
    this.lastPath = filePath;
    var file = filePath.toFile();
    if (!file.exists()) {
      // creating empty file
      file.createNewFile();
    }

    // parsing config to json
    var json = new Gson().toJson(instance);

    // writing json to file
    var writer = new BufferedWriter(new FileWriter(file));
    writer.write(json);
    writer.close();

    log.info("Configuration successfully saved!");
  }

  @Override
  public void save(DeleteButtonConfigEntity instance) {
    if (lastPath == null) throw new NullPointerException("No known config path to save");

    try {
      save(instance, lastPath);
    } catch (IOException e) {
      throw new RuntimeException("Can't save config", e);
    }
  }

}
