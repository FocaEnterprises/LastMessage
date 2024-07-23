package me.giverplay.lastmessage;

import com.google.gson.JsonObject;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class LastMessage extends LabyModAddon {
  private static final String ENABLE_CONFIG = "Enable";
  private static final BooleanElement ENABLED_ELEMENT = new BooleanElement("Enable module", ENABLE_CONFIG, new ControlElement.IconData(Material.LEVER));
  private static final KeyBinding SEND_LAST_KEYBINDING = new KeyBinding("Send last message", Keyboard.KEY_F, "LastMessage");

  private boolean isEnabled;

  @Override
  public void onEnable() {
    getApi().registerForgeListener(this);
  }

  @SubscribeEvent
  public void onKeyPress(InputEvent.KeyInputEvent ignore) {
    if (!isEnabled || !SEND_LAST_KEYBINDING.isPressed()) return;

    List<String> sentMessages = Minecraft.getMinecraft().ingameGUI.getChatGUI().getSentMessages();

    if (sentMessages.isEmpty()) return;

    Minecraft.getMinecraft().thePlayer.sendChatMessage(sentMessages.get(sentMessages.size() - 1));
  }

  @Override
  protected void fillSettings(List<SettingsElement> settings) {
    settings.add(ENABLED_ELEMENT);
  }

  @Override
  public void loadConfig() {
    JsonObject config = getConfig();
    this.isEnabled = !config.has(ENABLE_CONFIG) || config.get(ENABLE_CONFIG).getAsBoolean();
  }

  @Override
  public void saveConfig() {
    getConfig().addProperty(ENABLE_CONFIG, ENABLED_ELEMENT.getCurrentValue());
    super.saveConfig();
  }

  static {
    ClientRegistry.registerKeyBinding(SEND_LAST_KEYBINDING);
  }
}
