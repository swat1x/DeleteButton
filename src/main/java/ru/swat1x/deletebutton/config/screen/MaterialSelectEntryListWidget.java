package ru.swat1x.deletebutton.config.screen;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import ru.swat1x.deletebutton.DeleteButton;

import java.util.Comparator;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialSelectEntryListWidget extends AlwaysSelectedEntryListWidget<MaterialSelectEntryListWidget.MaterialEntry> {

  final MaterialSelectScreen screen;
  final MinecraftClient client;

  String filterText = null;

  public MaterialSelectEntryListWidget(MaterialSelectScreen screen,
                                       MinecraftClient client,
                                       int width, int height,
                                       int top, int bottom,
                                       int entryHeight) {
    super(client, width, height, top, bottom, entryHeight);
    this.screen = screen;
    this.client = client;

    updateEntries();
  }

  public void updateFilterText(String value) {
    filterText = value;
    updateEntries();
  }

  private void updateEntries() {
    clearEntries();
    Registries.ITEM
            .stream()
            .sorted(Comparator.comparing(i -> i == Items.BARRIER ? 0 : 1))
            .forEach(item -> {
      if (item != Items.AIR) {
        var entry = new MaterialEntry(item);
        if (filterText == null
                || (item.getName().getString().toLowerCase().contains(filterText.toLowerCase()) ||
                Registries.ITEM.getId(item).toString().contains(filterText.toLowerCase()))
        ) {
          addEntry(entry);
        }
      }
    });
  }

  @Override
  protected int getScrollbarPositionX() {
    return super.getScrollbarPositionX() + 30;
  }

  @Override
  public int getRowWidth() {
    return super.getRowWidth() + 85;
  }

  @Getter
  public class MaterialEntry extends AlwaysSelectedEntryListWidget.Entry<MaterialSelectEntryListWidget.MaterialEntry> implements AutoCloseable {

    private final Item item;

    public MaterialEntry(Item item) {
      this.item = item;
    }

    @Override
    public void close() {

    }

    @Override
    public Text getNarration() {
      return item.getName();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
      context.drawItem(new ItemStack(item), x + entryHeight / 4, y + entryHeight / 4);
      context.drawText(
              client.textRenderer,
              item.getName(),
              x + 35,
              (y + entryHeight / 4) + 3,
              0xffffff,
              true
      );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
        if (getSelectedOrNull() == this) {
          DeleteButton.saveNewMaterial(item);
          client.setScreen(screen.getParent());
        }
      }
      return true;
    }
  }

}
