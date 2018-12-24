package io.github.talkarcabbage.endernexus.registries;

import java.util.ArrayList;
import java.util.List;

import io.github.talkarcabbage.endernexus.EnderNexus;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;

public class RegistryHelper {

	static List<Item> items = new ArrayList<>();
	static List<Block> blocks = new ArrayList<>();
	
	
	public static Item addItem(Item item) {
		items.add(item);
		return item;
	}
	
	
	public static Block addBlock(Block block) {
		blocks.add(block);
		return block;
	}
	
	//Items and ItemBlocks
	
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	registerItemBlocks(event);
    	for (Item item : items) {
    		event.getRegistry().register(item);
    	}
    }
    
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
    	for (Block block : blocks) {
        	event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    	}
    }
    
    public static void registerItemModels(ModelRegistryEvent event) {
    	for (Item item : items) {
    		EnderNexus.proxy.initItemModel(item);
    	}
    }
    
    
    //Blocks
	
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
    	for (Block block : blocks) {
    		event.getRegistry().register(block);
    	}
    }
    
    public static void registerBlockModels(ModelRegistryEvent event) {
    	for (Block block : blocks) {
    		EnderNexus.proxy.initBlockModel(block);
    	}
    }
	
	private RegistryHelper() {}
}
