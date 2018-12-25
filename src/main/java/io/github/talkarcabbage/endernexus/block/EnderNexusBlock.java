package io.github.talkarcabbage.endernexus.block;

import java.util.Random;

import io.github.talkarcabbage.endernexus.EnderNexus;
import io.github.talkarcabbage.endernexus.nexus.EnderNexusInstance;
import io.github.talkarcabbage.endernexus.nexus.EnderNexusManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnderNexusBlock extends Block {

	public EnderNexusBlock(Material materialIn, String name) {
		super(materialIn);
		setUnlocalizedName(EnderNexus.MODID + "." + name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new EnderNexusBlockTileEntity();
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		Item item = this.getItemDropped(state, rand, fortune);
		if (item != Items.AIR) {
			ItemStack returnStack = new ItemStack(item, 1, this.damageDropped(state));
			if (world.getTileEntity(pos) != null) {
				NBTTagCompound entityData = world.getTileEntity(pos).writeToNBT(new NBTTagCompound());
				entityData.removeTag("x");
				entityData.removeTag("y");
				entityData.removeTag("z");
				NBTTagCompound stackCompound = new NBTTagCompound();
				stackCompound.setTag("BlockEntityTag", entityData);
				returnStack.setTagCompound(stackCompound);
			} else {
				EnderNexus.logger.info("Tile Entity was null");
			}
			drops.add(returnStack);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (worldIn.getTileEntity(pos) instanceof EnderNexusBlockTileEntity && ((EnderNexusBlockTileEntity)worldIn.getTileEntity(pos)).hasValidNetworkClient()) {
			for (int i = 0; i < 4; ++i) {
				int j = rand.nextInt(2) * 2-1;
				int k = rand.nextInt(2) * 2-1;
				double x = (double) pos.getX() + 0.25D * (double) (j)+.5d;
				double y = (double) ((float) pos.getY() + rand.nextFloat());
				double z = (double) pos.getZ() + 0.25D * (double) (k)+.5d;
				double xSpeed = (double) (rand.nextFloat() * (float) -1*j); 
				double ySpeed = ((double) rand.nextFloat() - 0.5D) * 0.5D;
				double zSpeed = (double) (rand.nextFloat() * (float) -1*k);
				worldIn.spawnParticle(EnumParticleTypes.PORTAL, x, y, z, xSpeed, ySpeed, zSpeed);
			}
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if (willHarvest)
			return true;
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return true;
		if (worldIn.isBlockLoaded(pos)) {
			TileEntity entity = worldIn.getTileEntity(pos);
			if (entity instanceof EnderNexusBlockTileEntity) {
				EnderNexusInstance instance = EnderNexusManager.get(worldIn).getNexus(((EnderNexusBlockTileEntity) entity).getNexusId());
				if (instance != null) {
					EnderNexus.logger.info("ID:" + instance.getId());
					EnderNexus.logger.info("Item:" + instance.getItemHandler().get().getStackInSlot(0));
					EnderNexus.logger.info("Fluid:" + instance.getFluidHandler().get().getFluidAmount() + "/"
							+ instance.getFluidHandler().get().getCapacity() + "mb "
							+ (instance.getFluidHandler().get().getFluid() == null ? "Empty"
									: instance.getFluidHandler().get().getFluid().getLocalizedName()));
					EnderNexus.logger.info("Energy:" + instance.getEnergyStorage().get().getEnergyStored());
					EnderNexus.logger.info("Settings:" + ((EnderNexusBlockTileEntity) entity).getItemType() + " "
							+ ((EnderNexusBlockTileEntity) entity).getFluidType() + " "
							+ ((EnderNexusBlockTileEntity) entity).getEnergyType());
				}
				playerIn.openGui(EnderNexus.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

}
