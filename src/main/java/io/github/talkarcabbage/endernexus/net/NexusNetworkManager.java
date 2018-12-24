package io.github.talkarcabbage.endernexus.net;

import io.github.talkarcabbage.endernexus.EnderNexus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NexusNetworkManager {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(EnderNexus.MODID);
	static int packetIdCounter;
	
	public static void registerPackets() {
		INSTANCE.registerMessage(NexusTileSettingsUpdatePacket.NexusTileSettingsUpdatePacketHandler.class, NexusTileSettingsUpdatePacket.class, packetIdCounter++, Side.SERVER);
		INSTANCE.registerMessage(NexusTileSettingsDeleteNetworkPacket.NexusTileSettingsUpdatePacketHandler.class, NexusTileSettingsDeleteNetworkPacket.class, packetIdCounter++, Side.SERVER);
	}
	
	
}
