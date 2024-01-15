package ru.swat1x.deletebutton.config;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

public interface ConfigRepository<T> {

  Class<T> getFileClass();

  T loadOrSave(Path filePath, Supplier<T> defaultSupplier) throws IOException;

  Optional<T> load(Path filePath);

  void save(T instance, Path filePath) throws IOException;

  void save(T instance);

}
