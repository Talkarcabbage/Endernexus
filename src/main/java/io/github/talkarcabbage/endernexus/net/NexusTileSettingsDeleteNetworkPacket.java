package io.github.talkarcabbage.endernexus.net;

import io.github.talkarcabbage.endernexus.EnderNexus;
import io.github.talkarcabbage.endernexus.nexus.EnderNexusManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class NexusTileSettingsDeleteNetworkPacket implements IMessage {
	
	
	String nexusIdToDelete;
	
	public NexusTileSettingsDeleteNetworkPacket() {
		// Nullary constructor because netty
	}
	
	public NexusTileSettingsDeleteNetworkPacket(String toDelete) {
		this.nexusIdToDelete = toDelete;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		nexusIdToDelete = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, nexusIdToDelete);
	}
	
	public static class NexusTileSettingsUpdatePacketHandler implements IMessageHandler<NexusTileSettingsDeleteNetworkPacket, IMessage> {
		@Override
		public IMessage onMessage(NexusTileSettingsDeleteNetworkPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				World serverWorld = ctx.getServerHandler().player.getServerWorld();
				EnderNexusManager.get(serverWorld).deleteEnderNexus(message.nexusIdToDelete);
			} else {
				EnderNexus.logger.warn("Received an update packet on the wrong side!");
			}
			return null;
		}
	}

}
