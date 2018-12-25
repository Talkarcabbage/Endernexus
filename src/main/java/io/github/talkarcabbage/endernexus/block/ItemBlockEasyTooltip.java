package io.github.talkarcabbage.endernexus.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockEasyTooltip extends ItemBlock {

	String tooltipKey;
	
	public ItemBlockEasyTooltip(Block block, String tooltipKey) {
		super(block);
		this.tooltipKey=tooltipKey;
	}
	
	public String getTooltipKey() {
		return tooltipKey;
	}
	
	public void setTooltipKey(String tooltipKey) {
		this.tooltipKey = tooltipKey;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (tooltipKey != null)
			tooltip.add(I18n.format("endernexus.itemblock.nexusblock.tooltip"));
	}

}
