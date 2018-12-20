package io.github.talkarcabbage.endernexus;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = EnderNexus.MODID, name = EnderNexus.NAME, version = EnderNexus.VERSION)
public class EnderNexus {
    public static final String MODID = "examplemod";
    public static final String NAME = "Example Mod";
    public static final String VERSION = "1.12.2-0.0.0.1";
    
    @SidedProxy(clientSide="io.github.talkarcabbage.endernexus.ClientProxy", serverSide="io.github.talkarcabbage.endernexus.CommonProxy")
    public static CommonProxy proxy; //NOSONAR pretend it's final because forge.
    
    @Mod.Instance
    public static EnderNexus instance; //NOSONAR pretend it's final because forge.

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
