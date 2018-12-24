package io.github.talkarcabbage.endernexus.gui;

import io.github.talkarcabbage.endernexus.block.EnderNexusBlockTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiManager implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int iD, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof EnderNexusBlockTileEntity)
			return new NexusContainer((EnderNexusBlockTileEntity)te);
		return null;
	}

	@Override
	public Object getClientGuiElement(int iD, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x,y,z);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof EnderNexusBlockTileEntity) {
			return new NexusGui((EnderNexusBlockTileEntity)world.getTileEntity(pos), new NexusContainer((EnderNexusBlockTileEntity)te));
		}
		return null;
	}

}
