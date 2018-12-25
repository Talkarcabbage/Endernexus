package io.github.talkarcabbage.endernexus.block;

import static io.github.talkarcabbage.endernexus.nexus.TransferType.BOTH;
import static io.github.talkarcabbage.endernexus.nexus.TransferType.EXPORT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.talkarcabbage.endernexus.EnderNexus;
import io.github.talkarcabbage.endernexus.capabilities.EnergyExportWrapper;
import io.github.talkarcabbage.endernexus.capabilities.FluidTankWrapper;
import io.github.talkarcabbage.endernexus.capabilities.ItemStackHandlerWrapper;
import io.github.talkarcabbage.endernexus.nexus.EnderNexusInstance;
import io.github.talkarcabbage.endernexus.nexus.EnderNexusManager;
import io.github.talkarcabbage.endernexus.nexus.TransferType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class EnderNexusBlockTileEntity extends TileEntity implements ITickable {

	protected String nexusId = "";
	
	EnergyExportWrapper energyWrapper;
	FluidTankWrapper fluidTankWrapper;
	ItemStackHandlerWrapper itemHandlerWrapper;
	
	TransferType itemType = TransferType.BOTH;
	public TransferType getItemType() {
		return itemType;
	}

	public void setItemType(TransferType itemType) {
		this.itemType = itemType;
	}

	public TransferType getFluidType() {
		return fluidType;
	}

	public void setFluidType(TransferType fluidType) {
		this.fluidType = fluidType;
	}

	public TransferType getEnergyType() {
		return energyType;
	}

	public void setEnergyType(TransferType energyType) {
		this.energyType = energyType;
	}

	TransferType fluidType = TransferType.BOTH;
	TransferType energyType = TransferType.BOTH;
	
	List<String> networksCache = new ArrayList<>();
	
	public static final String NETWORKS_CACHE_UPDATE_STRING = "nexusNetworksListCache";
	public static final String NETWORK_ID_STRING = "nexusIdConfig";
	public static final String ITEMS_TYPE_UPDATE_STRING = "nexusItemsConfig";
	public static final String FLUIDS_TYPE_UPDATE_STRING = "nexusFluidsConfig";
	public static final String ENERGY_TYPE_UPDATE_STRING = "nexusEnergyConfig";

	
	@CapabilityInject(IEnergyStorage.class)
	static Capability<IEnergyStorage> I_ENERGY_STORAGE = null; //NOSONAR Forge Magic goes here
	
	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> I_ITEM_HANDLER = null; //NOSONAR Forge Magic goes here
	
	@CapabilityInject(IFluidHandler.class)
	static Capability<IFluidHandler> I_FLUID_HANDLER = null; //NOSONAR Forge Magic goes here

	public void setNexusId(String id) {
		this.nexusId = id;
	}
	
	public String getNexusId() {
		return nexusId;
	}
	
	public List<String> getNetworksCacheList() {
		return networksCache;
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos) <= 64D;
	}
	
	public boolean hasValidNetworkClient() {
		return networksCache.contains(nexusId);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString(NETWORK_ID_STRING, nexusId);
		compound.setInteger(ITEMS_TYPE_UPDATE_STRING, TransferType.toInt(itemType));
		compound.setInteger(FLUIDS_TYPE_UPDATE_STRING, TransferType.toInt(fluidType));
		compound.setInteger(ENERGY_TYPE_UPDATE_STRING, TransferType.toInt(energyType));
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound.hasKey(NETWORK_ID_STRING)) {
			this.nexusId=compound.getString(NETWORK_ID_STRING);
		}
		if (compound.hasKey(ITEMS_TYPE_UPDATE_STRING)) {
			this.itemType=TransferType.fromInt(compound.getInteger(ITEMS_TYPE_UPDATE_STRING));
		}
		if (compound.hasKey(FLUIDS_TYPE_UPDATE_STRING)) {
			this.fluidType=TransferType.fromInt(compound.getInteger(FLUIDS_TYPE_UPDATE_STRING));
		}
		if (compound.hasKey(ENERGY_TYPE_UPDATE_STRING)) {
			this.energyType=TransferType.fromInt(compound.getInteger(ENERGY_TYPE_UPDATE_STRING));
		}
		super.readFromNBT(compound);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		EnderNexusInstance instance = EnderNexusManager.get(world).getNexus(nexusId);
		energyWrapper = new EnergyExportWrapper(instance==null ? null : instance.getEnergyStorage().get());
		itemHandlerWrapper = new ItemStackHandlerWrapper(instance==null ? null : instance.getItemHandler().get());
		fluidTankWrapper = new FluidTankWrapper(instance==null ? null : instance.getFluidHandler().get());
		updateStorageWrappers();
	}
	
	public void updateStorageWrappers() {
		EnderNexusInstance instance = EnderNexusManager.get(world).getNexus(nexusId);
		energyWrapper.setWrappedStorage(instance==null ? null : instance.getEnergyStorage().get());
		itemHandlerWrapper.setWrappedStorage(instance==null ? null : instance.getItemHandler().get());
		fluidTankWrapper.setWrappedStorage(instance==null ? null : instance.getFluidHandler().get());
		energyWrapper.setTransferType(energyType);
		itemHandlerWrapper.setTransferType(itemType);
		fluidTankWrapper.setTransferType(fluidType);
		this.markDirty();
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound updateTag = super.getUpdateTag();
		NBTTagList tagList = new NBTTagList();
		EnderNexusManager.get(world).getNexusNames().forEach( string -> tagList.appendTag(new NBTTagString(string)));
		updateTag.setTag(NETWORKS_CACHE_UPDATE_STRING, tagList);
		updateTag.setInteger(ITEMS_TYPE_UPDATE_STRING, TransferType.toInt(itemType));
		updateTag.setInteger(FLUIDS_TYPE_UPDATE_STRING, TransferType.toInt(fluidType));
		updateTag.setInteger(ENERGY_TYPE_UPDATE_STRING, TransferType.toInt(energyType));
		updateTag.setString(NETWORK_ID_STRING, nexusId);
		return updateTag;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound updateTag = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		EnderNexusManager.get(world).getNexusNames().forEach( string -> tagList.appendTag(new NBTTagString(string)));
		updateTag.setTag(NETWORKS_CACHE_UPDATE_STRING, tagList);
		updateTag.setInteger(ITEMS_TYPE_UPDATE_STRING, TransferType.toInt(itemType));
		updateTag.setInteger(FLUIDS_TYPE_UPDATE_STRING, TransferType.toInt(fluidType));
		updateTag.setInteger(ENERGY_TYPE_UPDATE_STRING, TransferType.toInt(energyType));
		updateTag.setString(NETWORK_ID_STRING, nexusId);
		return new SPacketUpdateTileEntity(getPos(), 1, updateTag);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		if (tag.getTag(NETWORKS_CACHE_UPDATE_STRING) instanceof NBTTagList) {
			networksCache = new ArrayList<>();
			((NBTTagList)tag.getTag(NETWORKS_CACHE_UPDATE_STRING)).forEach(nbtBase -> networksCache.add(((NBTTagString)nbtBase).getString()));
		} else {
			EnderNexus.logger.warn("Encountered invalid network update tag packet");
		}
		super.handleUpdateTag(tag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		if (world.isRemote) { //Update client data
			if (pkt.getNbtCompound().getTag(NETWORKS_CACHE_UPDATE_STRING) instanceof NBTTagList) {
				networksCache = new ArrayList<>();
				((NBTTagList)pkt.getNbtCompound().getTag(NETWORKS_CACHE_UPDATE_STRING)).forEach(nbtBase -> networksCache.add(((NBTTagString)nbtBase).getString()));
			} else {
				EnderNexus.logger.warn("Encountered invalid network list update packet");
			}
			this.itemType = TransferType.fromInt(pkt.getNbtCompound().getInteger(ITEMS_TYPE_UPDATE_STRING));
			this.fluidType = TransferType.fromInt(pkt.getNbtCompound().getInteger(FLUIDS_TYPE_UPDATE_STRING));
			this.energyType = TransferType.fromInt(pkt.getNbtCompound().getInteger(ENERGY_TYPE_UPDATE_STRING));
			this.nexusId = pkt.getNbtCompound().getString(NETWORK_ID_STRING);
		}
	}
	
	public void onNexusDataPacketUpdate(String newId, TransferType item, TransferType fluid, TransferType energy) {
		if (world.isRemote) {
			EnderNexus.logger.warn("onNexusDataPacket used by unexpected client side.");
		}
		this.nexusId = newId;
		this.itemType=item;
		this.fluidType=fluid;
		this.energyType=energy;
		this.updateStorageWrappers();
		this.world.notifyNeighborsOfStateChange(pos, blockType, true);
		this.world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
	}
	
	@Override
	public void update() {
		if (world.isRemote) return;
		
		if (EnderNexusManager.get(world).getNexus(nexusId)!= null && (itemType == EXPORT || itemType==BOTH)) {
			for (Map.Entry<BlockPos, EnumFacing> entry : getTouchingBlockPositions(this.pos).entrySet()) {
				if (world.getTileEntity(entry.getKey())!= null && world.getTileEntity(entry.getKey()).hasCapability(I_ITEM_HANDLER, entry.getValue())) {
					ItemStackHandler itemHandler = EnderNexusManager.get(world).getNexus(nexusId).getItemHandler().get();
					IItemHandler foreignItemHandler = world.getTileEntity(entry.getKey()).getCapability(I_ITEM_HANDLER, entry.getValue());
					ItemStack remainder = ItemHandlerHelper.insertItem(foreignItemHandler, itemHandler.getStackInSlot(0).copy(), false);
					if (remainder.getCount() < itemHandler.getStackInSlot(0).getCount()) {
						EnderNexus.logger.info("Orig:" + itemHandler.getStackInSlot(0).getDisplayName() + itemHandler.getStackInSlot(0).getCount() + " Remainder:" + remainder.getCount());
						itemHandler.extractItem(0, itemHandler.getStackInSlot(0).getCount()-remainder.getCount(), false);
						EnderNexus.logger.info("Post:" + itemHandler.getStackInSlot(0));
						break;
					}
				}
			}
		}
		if (EnderNexusManager.get(world).getNexus(nexusId)!= null && (fluidType == EXPORT || fluidType==BOTH)) {
			for (Map.Entry<BlockPos, EnumFacing> entry : getTouchingBlockPositions(this.pos).entrySet()) {
				if (world.getTileEntity(entry.getKey())!= null && world.getTileEntity(entry.getKey()).hasCapability(I_FLUID_HANDLER, entry.getValue())) {
					FluidTank fluidHandler = EnderNexusManager.get(world).getNexus(nexusId).getFluidHandler().get();
					IFluidHandler foreignFluidHandler = world.getTileEntity(entry.getKey()).getCapability(I_FLUID_HANDLER, entry.getValue());
					fluidHandler.drain(foreignFluidHandler.fill(fluidHandler.getFluid()==null ? null : fluidHandler.getFluid().copy(), true), true);
				}
			}
		}
		if (EnderNexusManager.get(world).getNexus(nexusId)!= null && (energyType == EXPORT || energyType==BOTH)) {
			for (Map.Entry<BlockPos, EnumFacing> entry : getTouchingBlockPositions(this.pos).entrySet()) {
				if (world.getTileEntity(entry.getKey())!= null && world.getTileEntity(entry.getKey()).hasCapability(I_ENERGY_STORAGE, entry.getValue())) {
					IEnergyStorage energyStorage = EnderNexusManager.get(world).getNexus(nexusId).getEnergyStorage().get();
					energyStorage.extractEnergy(world.getTileEntity(entry.getKey()).getCapability(I_ENERGY_STORAGE, entry.getValue()).receiveEnergy(energyStorage.getEnergyStored(), false), false);
				}
			}
		}
	}
	
	/**
	 * Returns a map of BlockPos objects and the side the BlockPos object is being interacted with relative
	 * to the initial block position.
	 * 
	 * @param initial Initial position
	 * @return
	 */
	public static Map<BlockPos, EnumFacing> getTouchingBlockPositions(BlockPos initial) {
		HashMap<BlockPos, EnumFacing> list = new HashMap<>(6);
		list.put(initial.north(), EnumFacing.SOUTH);
		list.put(initial.south(), EnumFacing.NORTH);
		list.put(initial.east(), EnumFacing.WEST);
		list.put(initial.west(), EnumFacing.EAST);
		list.put(initial.up(), EnumFacing.DOWN);
		list.put(initial.down(), EnumFacing.UP);
		return list;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == I_ENERGY_STORAGE && (energyType!=TransferType.NONE)) {
			return (T) energyWrapper;
		}
		if (capability == I_ITEM_HANDLER && (energyType!=TransferType.NONE)) {
			return (T) itemHandlerWrapper;
		}
		if (capability == I_FLUID_HANDLER && (energyType!=TransferType.NONE)) {
			return (T) fluidTankWrapper;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == I_ENERGY_STORAGE && (energyType!=TransferType.NONE)) {
			return true;
		}
		if (capability == I_ITEM_HANDLER && (itemType!=TransferType.NONE)) {
			return true;
		}
		if (capability == I_FLUID_HANDLER && (fluidType!=TransferType.NONE)) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
}
