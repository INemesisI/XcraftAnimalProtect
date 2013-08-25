package de.xcraft.INemesisI.AnimalProtect;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.xcraft.INemesisI.Library.XcraftEventListener;

public class EventListener extends XcraftEventListener {
	ConfigManager configManager;
	WorldGuardPlugin worldguard;

	public EventListener(XcraftAnimalProtect plugin) {
		super(plugin);
		this.worldguard = plugin.worldguard;
		this.configManager = (ConfigManager) plugin.configManager;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		Player player = (Player) event.getDamager();
		if (event.getEntity() instanceof Tameable) {
			Tameable tameable = (Tameable) event.getEntity();
			if (tameable.isTamed() && tameable.isTamed() && !tameable.getOwner().equals(player)) {
				event.setCancelled(!player.hasPermission("XcraftAnimalProtect.Attack"));
			}
		} else if (event.getEntity() instanceof Animals) {
			if (!worldguard.canBuild(player, event.getEntity().getLocation())) {
				event.setCancelled(!player.hasPermission("XcraftAnimalProtect.Attack"));
			}
		}
		if (event.isCancelled()) {
			plugin.messenger.sendInfo(player, configManager.attackMessage, true);
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Tameable) {
			Tameable tameable = (Tameable) event.getRightClicked();
			if (tameable.isTamed() && !tameable.getOwner().equals(event.getPlayer())) {
				event.setCancelled(!event.getPlayer().hasPermission("XcraftAnimalProtect.Interact"));
			}
		} else if (event.getRightClicked() instanceof Animals) {
			if (!worldguard.canBuild(event.getPlayer(), event.getRightClicked().getLocation())) {
				event.setCancelled(!event.getPlayer().hasPermission("XcraftAnimalProtect.Interact"));
			}
		}
		if (event.isCancelled()) {
			plugin.messenger.sendInfo(event.getPlayer(), configManager.interactMessage, true);
		}
	}

	@EventHandler
	public void onEntityTame(EntityTameEvent event) {
		String name = event.getOwner().getName() + "'s "
				+ configManager.names.get(event.getEntityType());
		event.getEntity().setCustomName(name);
		event.getEntity().setCustomNameVisible(true);
	}

	@EventHandler
	public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
		if (event.getEntity() instanceof Tameable) {
			Tameable tameable = (Tameable) event.getEntity();
			if (tameable.isTamed() && !tameable.getOwner().equals(event.getPlayer())) {
				event.setCancelled(!event.getPlayer().hasPermission("XcraftAnimalProtect.Interact"));
			}
		}
		if (!worldguard.canBuild(event.getPlayer(), event.getEntity().getLocation())) {
			event.setCancelled(!event.getPlayer().hasPermission("XcraftAnimalProtect.Interact"));
		}
	}
}
