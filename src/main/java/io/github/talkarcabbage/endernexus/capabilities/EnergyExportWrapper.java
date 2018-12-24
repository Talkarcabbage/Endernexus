package io.github.talkarcabbage.endernexus.capabilities;

import io.github.talkarcabbage.endernexus.nexus.TransferType;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyExportWrapper implements IEnergyStorage {

	IEnergyStorage wrappedStorage;
	TransferType transferType;
	
	public EnergyExportWrapper(IEnergyStorage nexusEnergyStorage) {
		this.wrappedStorage=nexusEnergyStorage;
	}
	
	public TransferType getTransferType() {
		return transferType;
	}
	
	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}
	
	public IEnergyStorage getWrappedStorage() {
		return wrappedStorage;
	}
	
	public void setWrappedStorage(IEnergyStorage wrappedStorage) {
		this.wrappedStorage = wrappedStorage;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (canReceive()) {
			return wrappedStorage.receiveEnergy(maxReceive, simulate);
		}
		return 0;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (canExtract()) {
			return wrappedStorage.extractEnergy(maxExtract, simulate);
		}
		return 0;
	}

	@Override
	public int getEnergyStored() {
		if (!hasStorage()) return 0;
		return wrappedStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		if (!hasStorage()) return 0;
		return wrappedStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canExtract() {
		return hasStorage() && (transferType==TransferType.EXPORT || transferType==TransferType.BOTH);
	}

	@Override
	public boolean canReceive() {
		
		return hasStorage() && (transferType==TransferType.ACCEPT || transferType==TransferType.BOTH);
	}

	public boolean hasStorage() {
		return wrappedStorage!=null;
	}
	

}
