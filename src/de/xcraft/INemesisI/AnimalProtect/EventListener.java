package de.xcraft.INemesisI.AnimalProtect;

import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
		this.configManager = plugin.getConfigManager();
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;
		LivingEntity livingEntity = null;
		if (event.getDamager() instanceof Projectile) {
			Projectile arrow = (Projectile) event.getDamager();
			livingEntity = arrow.getShooter();
		} else if (!(event.getDamager() instanceof Player))
			return;
		Player player = null;
		if (event.getDamager() instanceof Player)
			player = (Player) event.getDamager();
		else if (livingEntity != null && livingEntity instanceof Player)
			player = (Player) livingEntity;
		else
			return;
		if (event.getEntity() instanceof Tameable) {
			Tameable tameable = (Tameable) event.getEntity();
			if (tameable.isTamed() && tameable.isTamed() && !tameable.getOwner().equals(player)
					&& !player.hasPermission("XcraftAnimalProtect.Attack")) {
				event.setCancelled(true);
				plugin.getMessenger().sendInfo(player, configManager.attackMessage, true);
			}
		} else if (event.getEntity() instanceof Animals) {
			if (!worldguard.canBuild(player, event.getEntity().getLocation()) && !player.hasPermission("XcraftAnimalProtect.Attack")) {
				event.setCancelled(true);
				plugin.getMessenger().sendInfo(player, configManager.attackMessage, true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.isCancelled())
			return;
		if (event.getRightClicked() instanceof Tameable) {
			Tameable tameable = (Tameable) event.getRightClicked();
			if (tameable.isTamed()) {
				if (!tameable.getOwner().equals(event.getPlayer()) && !event.getPlayer().hasPermission("XcraftAnimalProtect.Interact")) {
					event.setCancelled(true);
					plugin.getMessenger().sendInfo(event.getPlayer(), configManager.interactMessage, true);
				}
			}
		} else if (event.getRightClicked() instanceof Animals) {
			if (!worldguard.canBuild(event.getPlayer(), event.getRightClicked().getLocation())
					&& !event.getPlayer().hasPermission("XcraftAnimalProtect.Interact")) {
				event.setCancelled(true);
				plugin.getMessenger().sendInfo(event.getPlayer(), configManager.interactMessage, true);
			}
		}
	}

	@EventHandler
	public void onEntityTame(EntityTameEvent event) {
		String name = event.getOwner().getName() + "'s ";
		if (configManager.names.containsKey(event.getEntity().getType()))
			name += configManager.names.get(event.getEntity().getType());
		else
			name += event.getEntity().getType().getName();
		event.getEntity().setCustomName(name);
		event.getEntity().setCustomNameVisible(true);
	}

	@EventHandler
	public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
		if (event.isCancelled())
			return;
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
