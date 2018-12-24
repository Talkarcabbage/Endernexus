package io.github.talkarcabbage.endernexus.capabilities;

import io.github.talkarcabbage.endernexus.nexus.TransferType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidTankWrapper implements IFluidHandler {
	TransferType transferType;
	FluidTank wrappedStorage;
	
	public FluidTankWrapper(FluidTank wrappedStorage) {
		this.wrappedStorage=wrappedStorage;
	}
	
	public FluidTank getWrappedStorage() {
		return wrappedStorage;
	}
	
	public void setWrappedStorage(FluidTank wrappedStorage) {
		this.wrappedStorage = wrappedStorage;
	}
	
	public TransferType getTransferType() {
		return transferType;
	}
	
	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		if (!hasStorage()) return new IFluidTankProperties[] {};
		return wrappedStorage.getTankProperties();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (hasStorage() && (transferType==TransferType.ACCEPT || transferType==TransferType.BOTH)) {
			return wrappedStorage.fill(resource, doFill);
		} else {
			return 0;
		}
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (hasStorage() && (transferType==TransferType.ACCEPT || transferType==TransferType.BOTH)) {
			return wrappedStorage.drain(resource, doDrain);
		} else {
			return null;
		}
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (hasStorage() && (transferType==TransferType.ACCEPT || transferType==TransferType.BOTH)) {
			return wrappedStorage.drain(maxDrain, doDrain);
		} else {
			return null;
		}
	}
	
	public boolean hasStorage() {
		return wrappedStorage!=null;
	}
	
}
