package de.xcraft.INemesisI.AnimalProtect;

import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.xcraft.INemesisI.Library.XcraftPlugin;

public class XcraftAnimalProtect extends XcraftPlugin {
	public WorldGuardPlugin worldguard;

	@Override
	protected void setup() {
		if (!setupWorldguard())
			return;
		configManager = new ConfigManager(this);
		eventListener = new EventListener(this);
		configManager.load();
	}

	private boolean setupWorldguard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return false; // Maybe you want throw an exception instead
		}
		worldguard = (WorldGuardPlugin) plugin;
		return worldguard != null;
	}
}
