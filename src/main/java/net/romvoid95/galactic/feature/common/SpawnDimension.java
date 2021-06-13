package net.romvoid95.galactic.feature.common;

import static net.romvoid95.galactic.Info.ID;

import java.util.Objects;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.romvoid95.api.config.IOrdered;
import net.romvoid95.api.feature.Feature;
import net.romvoid95.galactic.GalacticTweaks;
import net.romvoid95.galactic.core.gc.IOWriter;
import net.romvoid95.galactic.feature.FeatureConfigs;

public class SpawnDimension extends Feature implements IOrdered {

	public SpawnDimension() {
		this.category = "DimensionSpawn";
		this.categoryComment = "Set a certain Planet or Moon as a Spawn Point\n\nYou should have SPAWN-ITEMS Configured and set prior to enabling this setting\n"
				+ "At the moment any moon or planet set to spawn in uses that bodies teleport system (spawns in the lander)";
	}

	@Override
	public void addProp() {
		this.propOrder.add(FeatureConfigs.SpawnDim.key());
		this.propOrder.add(FeatureConfigs.useCoord.key());
		this.propOrder.add(FeatureConfigs.firstJoinOnly.key());
		this.propOrder.add(FeatureConfigs.everyDeath.key());
		this.propOrder.add(FeatureConfigs.spawnPos.key());
	}

	private CelestialBody spawnDim;

	@Override
	public boolean usesEvents() {
		return true;
	}

	@Override
	public void postInit() {
		String idName = FeatureConfigs.SpawnDim.get().replace("_", " ");
		String err = "ERROR: Could not get Celestial Body for name: ";

		for(CelestialBody body : IOWriter.allBodies) {
			if(body.getName().equalsIgnoreCase(idName)) {
				spawnDim = Objects.requireNonNull(GalaxyRegistry.getCelestialBodyFromUnlocalizedName(body.getUnlocalizedName()), err + idName);
			}
		}

		GalacticTweaks.LOG.debug("Spawn Dimension Set: {}", spawnDim.getLocalizedName());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerLoggedInEvent event) {
		handleLoggingEvent(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (FeatureConfigs.everyDeath.get()) {
			handleRespawnEvent(event);
		}
	}

	private void handleLoggingEvent(PlayerLoggedInEvent event) {
		if (event.player instanceof EntityPlayer) {
			final EntityPlayer player = event.player;
			if (FeatureConfigs.firstJoinOnly.get()) {
				final NBTTagCompound entityData = player.getEntityData();
				final NBTTagCompound persistedData = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
				entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistedData);
				final String key = ID + ":" + "FirstSpawn";
				if (!persistedData.getBoolean(key)) {
					teleport((EntityPlayerMP) player, spawnDim.getDimensionID());
					persistedData.setBoolean(key, true);
				}
			} else if (!FeatureConfigs.firstJoinOnly.get()) {
				teleport((EntityPlayerMP) player, spawnDim.getDimensionID());
			}
		}
	}

	private void handleRespawnEvent(PlayerRespawnEvent event) {
		if (event.player instanceof EntityPlayer) {
			final EntityPlayer player = event.player;
			teleport((EntityPlayerMP) player, spawnDim.getDimensionID());
		}
	}

	private void teleport(EntityPlayerMP player, int dimId) {
		BlockPos pos;
		MinecraftServer mcServer = player.getServer();
		final WorldServer world = mcServer.getWorld(dimId);
		String[] spawnString = FeatureConfigs.spawnPos.get().split(",");
		if (FeatureConfigs.useCoord.get()) {
			pos = new BlockPos(Integer.valueOf(spawnString[0]), Integer.valueOf(spawnString[1]),
					Integer.valueOf(spawnString[2]));
		} else {
			int y = world.getChunkFromBlockCoords(world.getSpawnPoint()).getHeight(world.getSpawnPoint());
			pos = new BlockPos(world.getSpawnPoint().getX(), y, world.getSpawnPoint().getZ());
		}
		WorldUtil.teleportEntitySimple(world, dimId, player, new Vector3(pos));
	}

	@Override
	public boolean isEnabled() {
		return FeatureConfigs.SPAWN_DIMENSION;
	}
}
