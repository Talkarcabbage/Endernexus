package io.github.talkarcabbage.endernexus.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import io.github.talkarcabbage.endernexus.block.EnderNexusBlockTileEntity;
import io.github.talkarcabbage.endernexus.net.NexusNetworkManager;
import io.github.talkarcabbage.endernexus.net.NexusTileSettingsUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

public class NexusNetworksScrollGui extends GuiScrollingList {
	
	Minecraft mc;
	EnderNexusBlockTileEntity entity;
	
	List<String> list = new ArrayList<>();
	int selectedListIndex = -1;
	int activeListIndex = -1;

	public NexusNetworksScrollGui(Minecraft client, EnderNexusBlockTileEntity entity, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
		super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
		mc=client;
		this.entity=entity;
	}
	
	public void updateList(List<String> newList) {
		this.list=newList;
	}

	@Override
	protected int getSize() {
		return list.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		this.selectedListIndex=index;
		if (doubleClick) {
			activeListIndex=index;
			NexusNetworkManager.INSTANCE.sendToServer(new NexusTileSettingsUpdatePacket(entity.getItemType(), entity.getFluidType(), entity.getEnergyType(), list.get(activeListIndex), entity.getPos()));
		}
	}

	@Override
	protected boolean isSelected(int index) {
		return index==selectedListIndex;
	}

	@Override
	protected void drawBackground() {
		// I mean I guess it works as-is without more background.
		
	}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		String text = list.get(slotIdx);
		if (text==null) text = "";
		if (text.equals(entity.getNexusId())) {
			mc.fontRenderer.drawString(text, entryRight+12-listWidth, slotTop, Color.green.getRGB());
		} else {
			mc.fontRenderer.drawString(text, entryRight+12-listWidth, slotTop, Color.white.getRGB());
		}
		
		
	}

}
