package de.xcraft.INemesisI.AnimalProtect;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import de.xcraft.INemesisI.Library.Manager.XcraftConfigManager;

public class ConfigManager extends XcraftConfigManager {

	String attackMessage;
	String interactMessage;
	String setOwnerFailMessage;
	Map<EntityType, String> names;

	public ConfigManager(XcraftAnimalProtect plugin) {
		super(plugin);
	}

	@Override
	public void load() {
		this.attackMessage = config.getString("Messages.Attack");
		this.interactMessage = config.getString("Messages.Interact");
		names = new HashMap<EntityType, String>();
		ConfigurationSection cs = config.getConfigurationSection("Names");
		for (String key : cs.getKeys(false)) {
			if (EntityType.valueOf(key) != null) {
				names.put(EntityType.valueOf(key), cs.getString(key));
			}
		}
	}

	@Override
	public void save() {
	}

}
