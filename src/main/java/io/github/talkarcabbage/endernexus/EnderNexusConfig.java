package io.github.talkarcabbage.endernexus;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = EnderNexus.MODID)
public class EnderNexusConfig {

	@Comment("Enables an alternative harder recipe for the ender nexus block. Not yet implemented.")
	public static boolean hardRecipe = false;
	
	@Comment("Sets the maximum amount of energy per tick that can be transferred into and out of the ender nexus.")
	public static int maxEnergyTransfer = Integer.MAX_VALUE;

	@Comment("Sets the maximum amount of fluid per tick that can be transferred into and out of the ender nexus.")
	public static int maxLiquidTransfer;
	
	private EnderNexusConfig() {}
}
