package me.giverplay.lastmessage;

import net.labymod.api.LabyModAddon;
import net.labymod.api.events.RenderIngameOverlayEvent;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class LastMessage extends LabyModAddon {
  private static final String ENABLE_CONFIG = "Enable";
  private static final KeyBinding SEND_LAST_KEYBINDING = new KeyBinding("Send last message", Keyboard.KEY_F, "key.categories.misc");

  private BooleanElement enabledElement;
  private boolean lastKeyState;

  @Override
  public void onEnable() {
    getApi().getEventManager().register((RenderIngameOverlayEvent) this::onRenderOverlay);
  }

  public void onRenderOverlay(float ignore) {
    if (!enabledElement.getCurrentValue() || !(lastKeyState = !lastKeyState && SEND_LAST_KEYBINDING.isPressed()))
      return;

    List<String> sentMessages = Minecraft.getMinecraft().ingameGUI.getChatGUI().getSentMessages();

    if (sentMessages.isEmpty()) return;

    Minecraft.getMinecraft().thePlayer.sendChatMessage(sentMessages.get(sentMessages.size() - 1));
  }

  @Override
  protected void fillSettings(List<SettingsElement> settings) {
    enabledElement = new BooleanElement("Enable LastMessage",
      this,
      new ControlElement.IconData(Material.LEVER),
      ENABLE_CONFIG,
      !getConfig().has(ENABLE_CONFIG) || getConfig().get(ENABLE_CONFIG).getAsBoolean());
    settings.add(enabledElement);
  }

  public void loadConfig() {
  }

  @Override
  public void saveConfig() {
    getConfig().addProperty(ENABLE_CONFIG, enabledElement.getCurrentValue());
    super.saveConfig();
  }

  static {
    GameSettings settings = Minecraft.getMinecraft().gameSettings;
    settings.keyBindings = ArrayUtils.add(settings.keyBindings, SEND_LAST_KEYBINDING);
  }
}
