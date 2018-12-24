package io.github.talkarcabbage.endernexus.net;

import io.github.talkarcabbage.endernexus.EnderNexus;
import io.github.talkarcabbage.endernexus.EnderNexusConfig;
import io.github.talkarcabbage.endernexus.block.EnderNexusBlockTileEntity;
import io.github.talkarcabbage.endernexus.nexus.EnderNexusManager;
import io.github.talkarcabbage.endernexus.nexus.TransferType;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class NexusTileSettingsUpdatePacket implements IMessage {

	private TransferType inventoryType;
	private TransferType fluidType;
	private TransferType energyType;
	private String networkLabel;
	private BlockPos entityPos;
	
	public NexusTileSettingsUpdatePacket() {
		super();
	}
	
	public NexusTileSettingsUpdatePacket(TransferType inventoryType, TransferType fluidType, TransferType energyType, String networkLabel, BlockPos entityPos) {
		super();
		this.inventoryType = inventoryType;
		this.fluidType = fluidType;
		this.energyType = energyType;
		this.networkLabel = networkLabel;
		this.entityPos=entityPos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		inventoryType = TransferType.fromInt(buf.readInt());
		fluidType = TransferType.fromInt(buf.readInt());
		energyType = TransferType.fromInt(buf.readInt());
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		entityPos = new BlockPos(x,y,z);
		networkLabel = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(TransferType.toInt(inventoryType));
		buf.writeInt(TransferType.toInt(fluidType));
		buf.writeInt(TransferType.toInt(energyType));
		buf.writeInt(entityPos.getX());
		buf.writeInt(entityPos.getY());
		buf.writeInt(entityPos.getZ());
		ByteBufUtils.writeUTF8String(buf, networkLabel);
	}
	
	public static class NexusTileSettingsUpdatePacketHandler implements IMessageHandler<NexusTileSettingsUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(NexusTileSettingsUpdatePacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				WorldServer serverWorld = ctx.getServerHandler().player.getServerWorld();
				if (serverWorld.isBlockLoaded(message.entityPos) && serverWorld.getTileEntity(message.entityPos) instanceof EnderNexusBlockTileEntity) {
					if (EnderNexusManager.get(serverWorld).getNexus(message.networkLabel)==null && EnderNexusManager.get(serverWorld).getNexusNames().size()>=EnderNexusConfig.maxNetworks) {
						ctx.getServerHandler().player.sendMessage(new TextComponentString("Too many networks already exist!"));
					} else {
						EnderNexusManager.get(serverWorld).createNewNexusIfNotExists(message.networkLabel);
						serverWorld.addScheduledTask( () -> ((EnderNexusBlockTileEntity)serverWorld.getTileEntity(message.entityPos)).onNexusDataPacketUpdate(message.networkLabel, message.inventoryType, message.fluidType, message.energyType));
					}
				}
			} else {
				EnderNexus.logger.warn("Received an update packet on the wrong side!");
			}
			return null;
		}
		
	}

}
