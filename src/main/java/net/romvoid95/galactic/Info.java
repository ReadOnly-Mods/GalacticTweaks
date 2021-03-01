package net.romvoid95.galactic;

import net.romvoid95.api.ReadOnlyConfig.*;

public final class Info {
	public static final String ID = "galactictweaks";
	public static final String NAME = "GalacticTweaks";
	public static final String VERSION = "5.0.0";//"@VERSION@";
	public static final String FINGERPRINT = "@FINGERPRINT@";
	public static final int BUILD_NUM = 0;
	
	public static final ConfigVersion CONFVERSION = new ConfigVersion(VERSION + "." + String.valueOf(BUILD_NUM));
	
	public static final String FORGE_MIN = "14.23.5.2847";
	public static final String OPT_MODS = "after:asmodeuscore@[0.0.23,];after:extraplanets;after:galaxyspace;after:zollerngalaxy;after:moreplanets;";
	public static final String GC = "required-after:galacticraftcore@[4.0.2.280,];required-after:galacticraftplanets;";
	
	public static final String DEPS = "required:forge@[" + FORGE_MIN + ",);" + GC + ";" + OPT_MODS;
}
