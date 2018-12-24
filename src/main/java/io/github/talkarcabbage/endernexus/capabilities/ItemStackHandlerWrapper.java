package io.github.talkarcabbage.endernexus.capabilities;

import io.github.talkarcabbage.endernexus.nexus.TransferType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerWrapper implements IItemHandler {

	ItemStackHandler wrappedStorage;
	TransferType transferType;
	
	public ItemStackHandlerWrapper(ItemStackHandler wrappedStorage) {
		this.wrappedStorage=wrappedStorage;
	}
	
	public ItemStackHandler getWrappedStorage() {
		return wrappedStorage;
	}
	
	public void setWrappedStorage(ItemStackHandler wrappedStorage) {
		this.wrappedStorage = wrappedStorage;
	}
	
	public TransferType getTransferType() {
		return transferType;
	}
	
	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}
	
	@Override
	public int getSlots() {
		if (!hasStorage()) return 0;
		return wrappedStorage.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (!hasStorage()) return ItemStack.EMPTY.copy();
		return wrappedStorage.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (hasStorage() && (transferType==TransferType.ACCEPT || transferType==TransferType.BOTH)) {
			return wrappedStorage.insertItem(slot, stack, simulate);
		} else {
			return stack;
		}
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (hasStorage() && (transferType==TransferType.EXPORT || transferType==TransferType.BOTH)) {
			return wrappedStorage.extractItem(slot, amount, simulate);
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		return wrappedStorage.getSlotLimit(slot);
	}

	public boolean hasStorage() {
		return wrappedStorage!=null;
	}
	
}
