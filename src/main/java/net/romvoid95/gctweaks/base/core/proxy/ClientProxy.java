package net.romvoid95.gctweaks.base.core.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.romvoid95.gctweaks.ModuleController;
import net.romvoid95.gctweaks.base.Module;
//import net.romvoid95.gctweaks.base.version.VersionChecker;
import net.romvoid95.gctweaks.internal.versioning.VersionChecker;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ModuleController.modules.forEach(Module::proxyPreInit);
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		ModuleController.modules.forEach(Module::proxyInit);
		VersionChecker.init();
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		ModuleController.modules.forEach(Module::proxyPostInit);
		super.postInit(event);
	}

}
