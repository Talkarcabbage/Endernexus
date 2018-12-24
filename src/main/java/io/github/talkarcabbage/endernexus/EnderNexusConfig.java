package io.github.talkarcabbage.endernexus;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = EnderNexus.MODID)
@SuppressWarnings("squid:S1444")
public class EnderNexusConfig { 
	
	@Comment("Sets the maximum amount of energy per tick that can be transferred into and out of the ender nexus.")
	public static int maxEnergyTransfer = 100000;

	@Comment("Sets the maximum amount of fluid per tick that can be transferred into and out of the ender nexus.")
	public static int maxLiquidTransfer = 1000;
	
	@Comment("Sets the max number of possible networks.")
	public static int maxNetworks = 20;
	
	private EnderNexusConfig() {}
}
