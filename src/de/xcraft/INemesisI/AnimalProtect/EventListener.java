package de.xcraft.INemesisI.AnimalProtect;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.inventory.ItemStack;

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
		if (event.isCancelled() || !(event.getDamager() instanceof Player))
			return;
		Player player = (Player) event.getDamager();
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
				if (tameable.getOwner().equals(event.getPlayer()) && event.getPlayer().getItemInHand().getType().equals(Material.NAME_TAG)) {
					ItemStack item = event.getPlayer().getItemInHand();
					if (item.getItemMeta().getDisplayName().startsWith("setowner ")) {
						OfflinePlayer player = plugin.getServer().getOfflinePlayer(item.getItemMeta().getDisplayName().replace("setowner ", ""));
						if (player == null) {
							event.setCancelled(true);
							plugin.getMessenger().sendInfo(event.getPlayer(),
									configManager.setOwnerFailMessage.replace("%", item.getItemMeta().getDisplayName().replace("setowner ", "")),
									true);
						} else {
							tameable.setOwner(player);
							String name = player.getName() + "'s " + configManager.names.get(event.getRightClicked().getType());
							((LivingEntity) event.getRightClicked()).setCustomName(name);
							plugin.getMessenger().sendInfo(event.getPlayer(), configManager.setOwnerSucceedMessage.replace("%", player.getName()),
									true);
						}
					}
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
		String name = event.getOwner().getName() + "'s " + configManager.names.get(event.getEntityType());
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
