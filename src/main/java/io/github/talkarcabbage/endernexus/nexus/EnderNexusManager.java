package io.github.talkarcabbage.endernexus.nexus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.talkarcabbage.endernexus.EnderNexus;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class EnderNexusManager extends WorldSavedData {
	
	public static final String ENDER_NEXUS_DATA_NAME = EnderNexus.MODID + "_enderNexus";
	public static final String ENDER_NEXUS_DATA_NAME_TAGLIST = ENDER_NEXUS_DATA_NAME + "_TagList";
	static EnderNexusManager instance;
	
	Map<String, EnderNexusInstance> map = new HashMap<>();
	
	
	
	public static EnderNexusManager get(World world) {
		MapStorage storage = world.getMapStorage();
		EnderNexusManager instance = (EnderNexusManager) storage.getOrLoadData(EnderNexusManager.class, ENDER_NEXUS_DATA_NAME);
		if (instance==null) {
			instance = new EnderNexusManager();
			storage.setData(ENDER_NEXUS_DATA_NAME, instance);
		}
		return instance;
	}

	public EnderNexusManager() {
		super(ENDER_NEXUS_DATA_NAME);
	}
	
	public EnderNexusManager(String name) {
		super(name);
	}
	
	public Set<String> getNexusNames() {
		return map.keySet();
	}
	
	public EnderNexusInstance createNewNexus(String id) { 
		if ("".equals(id)) return null;
		EnderNexusInstance newNexus = new EnderNexusInstance(id);
		map.put(id, newNexus);
		this.markDirty();
		return newNexus;
	}
	
	public EnderNexusInstance createNewNexusIfNotExists(String id) {
		if ("".equals(id)) return null;
		if (map.get(id) != null) {
			return map.get(id);
		}
		EnderNexusInstance newNexus = new EnderNexusInstance(id);
		map.put(id, newNexus);
		this.markDirty();
		return newNexus;
	}
	
	public void updateNexusId(String oldId, String newId) {
		if (map.get(oldId) instanceof EnderNexusInstance) {
			EnderNexusInstance nexusInstance = map.get(oldId);
			nexusInstance.setId(newId);
			map.remove(oldId);
			map.put(newId, nexusInstance);
		}
	}
	
	public synchronized void deleteEnderNexus(String nexusId) {
		this.map.remove(nexusId);
		this.map.keySet().remove(nexusId);
	}
	
	public EnderNexusInstance getNexus(String id) {
		if ("".equals(id)) return null;
		this.markDirty(); //A vaguely ghetto solution to ensure that any time a nexus is accessed, it is marked dirty to be saved incase it was edited.
		return map.get(id);
		//return map.get(id)==null ? createNewNexus(id) : map.get(id);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtList) {
		EnderNexus.logger.info("Reading nexus data from world load");
		if (!(nbtList.getTag(ENDER_NEXUS_DATA_NAME_TAGLIST) instanceof NBTTagList)) {
			EnderNexus.logger.warn("Not a tag list!" + nbtList.getTag(ENDER_NEXUS_DATA_NAME_TAGLIST).toString());
		} else {
			((NBTTagList)nbtList.getTag(ENDER_NEXUS_DATA_NAME_TAGLIST)).forEach(nbtBase -> {
				if (nbtBase instanceof NBTTagCompound) {
					NBTTagCompound comp = ((NBTTagCompound)nbtBase);
					String id = comp.getString(EnderNexusInstance.ID_STORAGE_STRING);
					if (map.containsKey(id)) { //Update existing
						map.get(id).readFromNBT(comp);
					} else { //Create new
						EnderNexusInstance newInstance = createNewNexus(id);
						newInstance.readFromNBT(comp);
					}
				} else {
					EnderNexus.logger.warn("An EnderNexus nbt list entry was an unexpected type!");
				}
			});
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		EnderNexus.logger.info("Reading nexus data from world load");
		NBTTagList tagList = new NBTTagList(); 
		for (Map.Entry<String, EnderNexusInstance> entry : map.entrySet()) {
			tagList.appendTag(entry.getValue().writeToNBT(new NBTTagCompound()));
		}
		compound.setTag(ENDER_NEXUS_DATA_NAME_TAGLIST, tagList);
		return compound;
	}
	
}
