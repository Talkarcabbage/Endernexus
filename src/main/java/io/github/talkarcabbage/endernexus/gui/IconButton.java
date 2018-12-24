package io.github.talkarcabbage.endernexus.gui;

import io.github.talkarcabbage.endernexus.EnderNexus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class IconButton extends GuiButton {

	private final ResourceLocation iconTexture;
	private final int iconX;
	private final int iconY;
	private boolean selected;
	private int iconIndex;
	
	ButtonCallbackListener listener;

	public IconButton(int buttonId, int x, int y, ResourceLocation iconTextureIn, int iconXIn, int iconYIn, ButtonCallbackListener listener) {
		super(buttonId, x, y, 16, 16, "");
		this.iconTexture = iconTextureIn;
		this.iconX = iconXIn;
		this.iconY = iconYIn;
		this.listener=listener;
	}
	
	public IconButton(int buttonId, int x, int y, ResourceLocation iconTextureIn, int iconXIn, int iconYIn, ButtonCallbackListener listener, int width, int height) {
		super(buttonId, x, y, width, height, "");
		this.iconTexture = iconTextureIn;
		this.iconX = iconXIn;
		this.iconY = iconYIn;
		this.listener=listener;
	}
	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			mc.getTextureManager().bindTexture(iconTexture);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
					&& mouseY < this.y + this.height;
			int j = 0;

			if (!this.enabled) {
				j += this.width * 2;
			} else if (this.selected) {
				j += this.width * 1;
			} else if (this.hovered) {
				j += this.width * 3;
			}

			//this.drawTexturedModalRect(this.x, this.y, j, 219, this.width, this.height);
			this.drawTexturedModalRect(this.x, this.y, this.iconX, this.iconY+(this.height*iconIndex), this.width, this.height);
			
			
		}
	}
	
	public boolean isHoveringOn(int mouseX, int mouseY) {
		return (isNumberBetweenMinAndMax(mouseX, this.x, this.x+this.width-1) && isNumberBetweenMinAndMax(mouseY, this.y, this.y+this.height-1));
	}
	
	public static boolean isNumberBetweenMinAndMax(int num, int min, int max) {
		return num>=min && num<=max; 
	}
	
	public void setIconIndex(int index) {
	iconIndex=index;	
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selectedIn) {
		this.selected = selectedIn;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		super.mouseReleased(mouseX, mouseY);
		listener.onClick(this, mouseX, mouseY);
		EnderNexus.logger.info("Clicked the button!");
	}

	public int getIconIndex() {
		return iconIndex;
	}
	
	
	
}
