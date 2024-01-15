package ru.swat1x.deletebutton.config.screen;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import ru.swat1x.deletebutton.DeleteButton;

@Log4j2
@Environment(EnvType.CLIENT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialSelectScreen extends Screen {

  @Getter
  final Screen parent;

  MaterialSelectEntryListWidget materialSelectWidget;
  boolean initialized = false;

  public MaterialSelectScreen(@NotNull Screen parent) {
    super(Text.translatable("text.deletebutton.settings.title"));
    this.parent = parent;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (super.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    }
    var entry = this.materialSelectWidget.getSelectedOrNull();
    if (entry != null) {
      // updating material and save
      if (KeyCodes.isToggle(keyCode)) {
        DeleteButton.saveNewMaterial(entry.getItem());
        client.setScreen(parent);
        return true;
      }

      return this.materialSelectWidget.keyPressed(keyCode, scanCode, modifiers);
    }
    return false;
  }

  @Override
  protected void init() {
    if (initialized) {
      materialSelectWidget.updateSize(this.width, this.height, 32, this.height - 32);
    } else {
      initialized = true;
      this.materialSelectWidget = new MaterialSelectEntryListWidget(this, this.client, this.width, this.height, 32, this.height - 64, 30);
    }
    addSelectableChild(materialSelectWidget);

    ButtonWidget backButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, button -> this.client.setScreen(this.parent)).width(74).build());
    TextFieldWidget textWidget = this.addDrawableChild(new TextFieldWidget(client.textRenderer, 0, 0, 150, 20, Text.empty()));

    textWidget.setMaxLength(40);
    textWidget.setChangedListener(str -> materialSelectWidget.updateFilterText(str));

    GridWidget gridWidget = new GridWidget();
    GridWidget.Adder adder = gridWidget.createAdder(1);
    AxisGridWidget axisGridWidget = adder.add(new AxisGridWidget(308, 20, AxisGridWidget.DisplayAxis.HORIZONTAL));
    axisGridWidget.add(backButton);
    axisGridWidget.add(textWidget);
    gridWidget.refreshPositions();
    SimplePositioningWidget.setPos(gridWidget, 0, this.height - 64, this.width, 64);
  }

  @Override
  public void render(DrawContext context, int mouseX, int mouseY, float delta) {
    this.renderBackground(context);
    this.materialSelectWidget.render(context, mouseX, mouseY, delta);
    context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    super.render(context, mouseX, mouseY, delta);
  }

  @Override
  public void close() {
    client.setScreen(parent);
  }

}
