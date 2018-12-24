package io.github.talkarcabbage.endernexus.gui;

import io.github.talkarcabbage.endernexus.block.EnderNexusBlockTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class NexusContainer extends Container {
	
	int updateCounter = 10;
	EnderNexusBlockTileEntity entity;
	
	public NexusContainer(EnderNexusBlockTileEntity entity) {
		this.entity=entity;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return entity.canInteractWith(playerIn);
	}
	
	@Override
	public void detectAndSendChanges() {
		
		super.detectAndSendChanges();
		if (++updateCounter>9) {
			updateCounter=0; //Update the info in the block every half a second\
			entity.getWorld().notifyBlockUpdate(entity.getPos(), entity.getWorld().getBlockState(entity.getPos()), entity.getWorld().getBlockState(entity.getPos()), 2);
		}
	}

}
