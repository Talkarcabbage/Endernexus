package io.github.talkarcabbage.endernexus.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import io.github.talkarcabbage.endernexus.EnderNexus;
import io.github.talkarcabbage.endernexus.block.EnderNexusBlockTileEntity;
import io.github.talkarcabbage.endernexus.net.NexusNetworkManager;
import io.github.talkarcabbage.endernexus.net.NexusTileSettingsDeleteNetworkPacket;
import io.github.talkarcabbage.endernexus.net.NexusTileSettingsUpdatePacket;
import io.github.talkarcabbage.endernexus.nexus.TransferType;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class NexusGui extends GuiContainer implements ButtonCallbackListener {

	ResourceLocation background = new ResourceLocation(EnderNexus.MODID, "textures/gui/endernexusgui.png");
	EnderNexusBlockTileEntity entity;
	NexusNetworksScrollGui scrollList;
	
	IconButton itemButton;
	IconButton fluidButton;
	IconButton energyButton;
	
	IconButton okButton;
	IconButton deleteButton;
	
	GuiTextField newIdTextField;

	public NexusGui(EnderNexusBlockTileEntity entity, Container inventorySlotsIn) {
		super(inventorySlotsIn);
		this.entity=entity;

	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (scrollList==null) {
			scrollList = new NexusNetworksScrollGui(mc, entity, 90, 76, guiTop+26, guiTop+101, guiLeft+7, 11, width, height);
		}
		ArrayList<String> exampleList = new ArrayList<>();
		for (int i = 0; i < entity.getNetworksCacheList().size(); i++) {
			exampleList.add(entity.getNetworksCacheList().get(i));
		}
		scrollList.updateList(exampleList);
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		
		
		if (newIdTextField==null) {
			newIdTextField = new GuiTextField(0, this.fontRenderer, this.guiLeft+8, this.guiTop+9, 88, 10);
			newIdTextField.setEnabled(true);
			newIdTextField.setVisible(true);
			newIdTextField.setMaxStringLength(24);
			newIdTextField.setText(entity.getNexusId());
	        Keyboard.enableRepeatEvents(true);
		}
		
		
		if (okButton==null) {
			okButton = new IconButton(0, guiLeft+98, guiTop+6, background, 192, 0, this, 15, 12);
			this.addButton(okButton);
		}
		if (deleteButton==null) {
			deleteButton = new IconButton(1, guiLeft+116, guiTop+6, background, 192, 11, this, 15, 12);
			this.addButton(deleteButton);
		}
		if (itemButton==null) {
			itemButton = new IconButton(2, guiLeft+135, guiTop+23, background, 176, 0, this);
			this.addButton(itemButton);
		}
		if (fluidButton==null) {
			fluidButton = new IconButton(3, guiLeft+135, guiTop+52, background, 176, 0, this);
			this.addButton(fluidButton);
		}
		if (energyButton==null) {
			energyButton = new IconButton(4, guiLeft+135, guiTop+81, background, 176, 0, this);
			this.addButton(energyButton);
		}
		
		itemButton.setIconIndex(TransferType.toInt(entity.getItemType()));
		fluidButton.setIconIndex(TransferType.toInt(entity.getFluidType()));
		energyButton.setIconIndex(TransferType.toInt(entity.getEnergyType()));
		
		itemButton.drawButton(mc, mouseX, mouseY, partialTicks);
		fluidButton.drawButton(mc, mouseX, mouseY, partialTicks);
		energyButton.drawButton(mc, mouseX, mouseY, partialTicks);
		

		newIdTextField.drawTextBox();
		scrollList.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		newIdTextField.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		newIdTextField.textboxKeyTyped(typedChar, keyCode);
	}
	
	@Override
	public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}
	
	public int getTopOfGui() {
		return guiTop;
	}
	
	public int getLeftOfGui() {
		return guiLeft;
	}

	
	public void setBackgroundLocation(ResourceLocation location) {
		this.background = location;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	public void onClick(IconButton button, int mouseX, int mouseY) {
		if (button==okButton) {
			processUpdateButton(getTransferTypeFromIconIndex(itemButton.getIconIndex()), getTransferTypeFromIconIndex(fluidButton.getIconIndex()), getTransferTypeFromIconIndex(energyButton.getIconIndex()), newIdTextField.getText());
			return;
		}
		if (button==deleteButton) {
			if (scrollList.selectedListIndex>=0) {
				NexusNetworkManager.INSTANCE.sendToServer(new NexusTileSettingsDeleteNetworkPacket(scrollList.list.get(scrollList.selectedListIndex)));
			}
			return;
		}
		
		button.setIconIndex(button.getIconIndex()+1);
		if (button.getIconIndex()>3) {
			button.setIconIndex(0);
		}
		processUpdateButton(getTransferTypeFromIconIndex(itemButton.getIconIndex()), getTransferTypeFromIconIndex(fluidButton.getIconIndex()), getTransferTypeFromIconIndex(energyButton.getIconIndex()), entity.getNexusId());
	}
	
	public static TransferType getTransferTypeFromIconIndex(int index) {
		return TransferType.fromInt(index);
	}
	
	public static int getIconIndexFromTransferType(TransferType type) {
		return TransferType.toInt(type);
	}
	
	public void processUpdateButton(TransferType itemType, TransferType fluidType, TransferType energyType, String nexusId) {
		NexusNetworkManager.INSTANCE.sendToServer(new NexusTileSettingsUpdatePacket(itemType, fluidType, energyType, nexusId, entity.getPos()));
	}
	
}
