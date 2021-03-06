package de.xcraft.INemesisI.AnimalProtect;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.xcraft.INemesisI.Library.XcraftPlugin;
import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Library.Message.Messenger;

//@formatter:off
/***
 * @author INemesisI
 *     by _____   __                         _      ____
 *       /  _/ | / /__  ____ ___  ___  _____(_)____/  _/
 *       / //  |/ / _ \/ __ `__ \/ _ \/ ___/ / ___// /
 *     _/ // /|  /  __/ / / / / /  __(__  ) (__  )/ /
 *    /___/_/ |_/\___/_/ /_/ /_/\___/____/_/____/___/
 */
//@formatter:on

public class XcraftAnimalProtect extends XcraftPlugin {

	private ConfigManager configManager = null;
	private EventListener eventListener = null;
	private Messenger messenger = null;

	public WorldGuardPlugin worldguard;

	public Logger log = Logger.getLogger("Minecraft");

	@Override
	protected void setup() {
		messenger = Messenger.getInstance(this);
		configManager = new ConfigManager(this);
		setupWorldguard();
		eventListener = new EventListener(this);
		configManager.load();
	}

	@Override
	public XcraftPluginManager getPluginManager() {
		return null;
	}

	@Override
	public ConfigManager getConfigManager() {
		return configManager;
	}

	@Override
	public XcraftCommandManager getCommandManager() {
		return null;
	}

	@Override
	public EventListener getEventListener() {
		return eventListener;
	}

	@Override
	public Messenger getMessenger() {
		return messenger;
	}

	private boolean setupWorldguard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin))
			return false; // Maybe you want throw an exception instead
		worldguard = (WorldGuardPlugin) plugin;
		return true;
	}

}
