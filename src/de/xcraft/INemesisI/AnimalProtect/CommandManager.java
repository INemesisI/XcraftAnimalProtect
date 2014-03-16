package de.xcraft.INemesisI.AnimalProtect;

import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;

public class CommandManager extends XcraftCommandManager {

	public CommandManager(XcraftAnimalProtect plugin) {
		super(plugin);
	}

	@Override
	protected void registerCommands() {
		// registerCommand(new setOwnerCommand(this, "animal", "give", "g.*", "<Player>",
		// "Gives the animal, you are sitting on permanently to a player",
		// "XcraftAnimalProtect.Give"));

	}

}
