package de.xcraft.INemesisI.AnimalProtect;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;

public class setOwnerCommand extends XcraftCommand {
	public setOwnerCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
		super(cManager, command, name, pattern, usage, desc, permission);
	}

	XcraftAnimalProtect plugin;

	@Override
	public boolean execute(XcraftPluginManager manager, CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player) sender;
		OfflinePlayer newOwner = plugin.getServer().getOfflinePlayer(args[1]);
		if (!newOwner.hasPlayedBefore()) {
			plugin.getMessenger().sendInfo(sender, args[0] + " has never played on this server!", true);
			return true;
		}
		if (player.getVehicle() != null && player.getVehicle() instanceof Tameable) {
			Tameable t = (Tameable) player.getVehicle();
			if (!t.isTamed()) {
				plugin.getMessenger().sendInfo(sender, "!This animal must be tamed!", true);
				return true;
			}
			t.setOwner(newOwner);

			LivingEntity e = (LivingEntity) player.getVehicle();
			String name = sender.getName() + "'s ";
			if (plugin.getConfigManager().names.containsKey(e.getType()))
				name += plugin.getConfigManager().names.get(e.getType());
			else
				name += e.getType().getName();
			e.setCustomName(name);
			e.setCustomNameVisible(true);
			plugin.getMessenger().sendInfo(sender, "You sucessfully gave the Animal to " + newOwner.getName(), true);
		} else {
			plugin.getMessenger().sendInfo(sender, "You need to be sitting on a animal", true);
		}
		return true;
	}
}
