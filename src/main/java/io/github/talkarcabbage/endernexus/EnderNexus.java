package io.github.talkarcabbage.endernexus;

import org.apache.logging.log4j.Logger;

import io.github.talkarcabbage.endernexus.block.EnderNexusBlock;
import io.github.talkarcabbage.endernexus.block.EnderNexusBlockTileEntity;
import io.github.talkarcabbage.endernexus.gui.GuiManager;
import io.github.talkarcabbage.endernexus.net.NexusNetworkManager;
import io.github.talkarcabbage.endernexus.registries.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = EnderNexus.MODID, name = EnderNexus.NAME, version = EnderNexus.VERSION)
@Mod.EventBusSubscriber(modid=EnderNexus.MODID)
public class EnderNexus {
    public static final String MODID = "endernexus";
    public static final String NAME = "Ender Nexus";
    public static final String VERSION = "1.12.2-1.0.0.0";
    
    @SidedProxy(clientSide="io.github.talkarcabbage.endernexus.ClientProxy", serverSide="io.github.talkarcabbage.endernexus.CommonProxy")
    public static CommonProxy proxy; //NOSONAR pretend it's final because forge.
    
    @Mod.Instance
    public static EnderNexus instance; //NOSONAR pretend it's final because forge.

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        logger.info("Registering tile entity.");
        GameRegistry.registerTileEntity(EnderNexusBlockTileEntity.class, new ResourceLocation(MODID, "nexus_entity"));
        
        logger.info("Registering gui");
        NetworkRegistry.INSTANCE.registerGuiHandler(EnderNexus.instance, new GuiManager());
        
        NexusNetworkManager.registerPackets();
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
    	logger.info("Registering blocks.");
    	RegistryHelper.addBlock(new EnderNexusBlock(Material.ANVIL, "ender_nexus").setHardness(5));
    	RegistryHelper.registerBlocks(event);
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	logger.info("Registering items.");
    	RegistryHelper.registerItems(event);
    }
    
    @SubscribeEvent
    public static void modelEvent(ModelRegistryEvent event) {
    	logger.info(() -> "Registering block models");
    	RegistryHelper.registerBlockModels(event);
    	
    	logger.info(() -> "Registering item models");
    	RegistryHelper.registerItemModels(event);
    }
    
    @SubscribeEvent
    public static void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
    	if (!event.getModID().equals(MODID)) return;
    	ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }
}
