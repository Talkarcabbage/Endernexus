package io.github.talkarcabbage.endernexus.nexus;

import io.github.talkarcabbage.endernexus.EnderNexusConfig;
import io.github.talkarcabbage.endernexus.util.StorageWrapper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EnderNexusInstance {
	
	int id;
	
	public static final String ITEMS_STORAGE_STRING = "nexusItemStorage";
	public static final String ENERGY_STORAGE_STRING = "nexusEnergyStorage";
	public static final String FLUID_STORAGE_STRING = "nexusFluidStorage";


	@CapabilityInject(IEnergyStorage.class)
	static Capability<IEnergyStorage> I_ENERGY_STORAGE = null; //NOSONAR Forge Magic goes here
	
	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> I_ITEM_HANDLER = null; //NOSONAR Forge Magic goes here
	
	@CapabilityInject(IFluidHandler.class)
	static Capability<IFluidHandler> I_FLUID_HANDLER = null; //NOSONAR Forge Magic goes here
	
	StorageWrapper<IEnergyStorage> energyStorage;
	StorageWrapper<ItemStackHandler> itemHandler;
	StorageWrapper<FluidTank> fluidHandler;
	
	public EnderNexusInstance(int id) {
		init(id);
	}
	
	public void init(int id) {
		this.id=id;
		energyStorage.set(new EnergyStorage(EnderNexusConfig.maxEnergyTransfer, EnderNexusConfig.maxEnergyTransfer, EnderNexusConfig.maxEnergyTransfer));
		itemHandler.set(new ItemStackHandler(1));
		fluidHandler.set(new FluidTank(EnderNexusConfig.maxLiquidTransfer));
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag(ITEMS_STORAGE_STRING, itemHandler.get().serializeNBT());
		compound.setTag(ENERGY_STORAGE_STRING, I_ENERGY_STORAGE.getStorage().writeNBT(I_ENERGY_STORAGE, energyStorage.get(), null));
		fluidHandler.get().writeToNBT(compound);
		return compound;
	}
	
	public void readFromNBT(NBTTagCompound compound) {
		if (compound.hasKey(ITEMS_STORAGE_STRING) && compound.getTag(ITEMS_STORAGE_STRING) instanceof NBTTagCompound) {
			itemHandler.get().deserializeNBT((NBTTagCompound) compound.getTag(ITEMS_STORAGE_STRING));
		}
		if (compound.hasKey(ENERGY_STORAGE_STRING)) {
			I_ENERGY_STORAGE.getStorage().readNBT(I_ENERGY_STORAGE, energyStorage.get(), null, compound.getTag(ENERGY_STORAGE_STRING));
		}
		fluidHandler.get().readFromNBT(compound);
	}
	
}
