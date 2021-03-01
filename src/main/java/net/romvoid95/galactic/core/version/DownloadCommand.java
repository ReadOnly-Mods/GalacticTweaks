package net.romvoid95.galactic.core.version;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
<<<<<<< Updated upstream:src/main/java/net/romvoid95/gctweaks/internal/command/DownloadCommand.java
import net.romvoid95.gctweaks.internal.versioning.DownloadUpdate;
import net.romvoid95.gctweaks.internal.versioning.VersionChecker;
=======
>>>>>>> Stashed changes:src/main/java/net/romvoid95/galactic/core/version/DownloadCommand.java


public class DownloadCommand extends CommandBase {

	@Override
	public String getName () {
		return "download-latest-gctweaks";
	}

	@Override
	public String getUsage (ICommandSender sender) {
		return "/download-latest-gctweaks";
	}

	@Override
	public boolean checkPermission (MinecraftServer server, ICommandSender sender) {
		return server.isSinglePlayer() || super.checkPermission(server, sender);
	}

	@Override
	public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
<<<<<<< Updated upstream:src/main/java/net/romvoid95/gctweaks/internal/command/DownloadCommand.java
			if (VersionChecker.downloadedFile) {
				sender.sendMessage(new TextComponentTranslation("galactictweaks.versions.downloadedAlready")
						.setStyle(new Style().setColor(TextFormatting.RED)));
			}
			else if (VersionChecker.startedDownload) {
=======
			if (Update.downloadedFile) {
				sender.sendMessage(new TextComponentTranslation("galactictweaks.versions.downloadedAlready")
						.setStyle(new Style().setColor(TextFormatting.RED)));
			}
			else if (Update.startedDownload) {
>>>>>>> Stashed changes:src/main/java/net/romvoid95/galactic/core/version/DownloadCommand.java
				sender.sendMessage(new TextComponentTranslation("galactictweaks.versions.downloadingAlready")
						.setStyle(new Style().setColor(TextFormatting.RED)));
			}
			else {
				new Update();
			}
		}
		else {

		}
	}
}