package game.asset.item;

import java.util.ArrayList;
import java.util.List;

import game.asset.GameAsset;
import game.asset.GameItemModifier;

/**
 * GameItemUse Class that represents a usable GameItem
 * @author Joseph DeLong
 */
public class GameItemUse extends GameItem {
	private int curUses;
	private int maxUses;
	private List<GameItemModifier> modifiers;

	/**
	 * GameItemUse() Constructor used for TEMPLATE creation
	 */
	public GameItemUse() {
		super();
		this.setAssetType(GameAsset.assetType_ITEM_USE);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType(GameItem.itemType_USE);

		this.setCurUses(0);
		this.setMaxUses(0);
		this.setModifiers(new ArrayList<GameItemModifier>());
	}

	/**
	 * GameItemUse(String,String,String,String,String,int,int,int,int,String,int,int,List<GameItemModifier>)
	 */
	public GameItemUse(
			String assetType, String uid, String itemType, String name, String description, int itemValue, int itemWeight, int quantity, int rarity, String owner, 
			int curUses, int maxUses, List<GameItemModifier> modifiers) {
		this.setAssetType(assetType);
		this.setUid(uid);

		this.setItemType(itemType);
		this.setName(name);
		this.setDescription(description);
		this.setItemValue(itemValue);
		this.setItemWeight(itemWeight);
		this.setQuantity(quantity);
		this.setRarity(rarity);
		this.setOwner(owner);

		this.setCurUses(curUses);
		this.setMaxUses(maxUses);
		this.setModifiers(modifiers);
	}

	/**
	 * GameItemUse(GameItemUse) Given a base GameItemUse, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameItem.lookupTemplate(itemName)</b></code> in order to create in-game
	 * INSTANCEs of the GameItem TEMPLATE (stored in <code>GameItem.itemTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param itemTemplate The GameItemUse TEMPLATE to make an INSTANCE copy of
	 */
	public GameItemUse(GameItemUse itemTemplate) {
		super(itemTemplate);
		this.setAssetType(GameAsset.assetType_ITEM_USE);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType(GameItem.itemType_USE);
		this.setName(new String(itemTemplate.getName()));
		this.setDescription(new String(itemTemplate.getDescription()));
		this.setItemValue(Integer.valueOf(itemTemplate.getItemValue()));
		this.setItemWeight(Integer.valueOf(itemTemplate.getItemWeight()));
		this.setQuantity(Integer.valueOf(itemTemplate.getQuantity()));
		this.setRarity(Integer.valueOf(itemTemplate.getRarity()));
		this.setOwner(new String(itemTemplate.getOwner()));

		this.setCurUses(Integer.valueOf(itemTemplate.getCurUses()));
		this.setMaxUses(Integer.valueOf(itemTemplate.getMaxUses()));
		List<GameItemModifier> templateModifiers = itemTemplate.getModifiers();
		List<GameItemModifier> modifiers = null;
		if(templateModifiers != null && templateModifiers.size() > 0) {
			modifiers = new ArrayList<GameItemModifier>();
			for(int i = 0; i < templateModifiers.size(); i++) {
				modifiers.add(new GameItemModifier(templateModifiers.get(i)));
			}
		} else {
			modifiers = new ArrayList<GameItemModifier>();
		}
		this.setModifiers(modifiers);
	}

	public int getCurUses() {return this.curUses;}
	public int getMaxUses() {return this.maxUses;}
	public List<GameItemModifier> getModifiers() {return this.modifiers;}

	public void setCurUses(int curUses) {this.curUses = curUses;}
	public void setMaxUses(int maxUses) {this.maxUses = maxUses;}
	public void setModifiers(List<GameItemModifier> modifiers) {this.modifiers = modifiers;}

	/**
	 * getFullItemName() Returns the full name of this GameItemUse, including GameItemModifiers
	 * @return String representation of the full name of this GameItemUse
	 */
	public String getFullItemName() {
		String itemName = "";
		List<GameItemModifier> modifiers = this.getModifiers();
		if(modifiers != null && modifiers.size() > 0) {
			List<GameItemModifier> modPre = new ArrayList<GameItemModifier>();
			List<GameItemModifier> modSuf = new ArrayList<GameItemModifier>();
			for(GameItemModifier mod: modifiers) {
				if(mod.getModifierType().equals(GameItemModifier.modifierType_PREFIX)) {
					modPre.add(mod);
				} else {
					modSuf.add(mod);
				}
			}
			for(GameItemModifier prefix: modPre) {
				itemName += prefix.getName() + " ";
			}
			itemName += this.getName();
			if(modSuf != null && modSuf.size() > 0) {
				itemName += " of ";
				for(int i = 0; i < modSuf.size(); i++) {
					itemName += modSuf.get(i).getName();
					if(i != modSuf.size() - 1) {
						itemName += " and ";
					}
				}
			}
		}
		return itemName;
	}

	/**
	 * addModifer(GameItemModifier) Adds the specified GameItemModifer to the specified GameItemUse, if able
	 * @param modifier The GameItemModifier to add to the GameItemUse
	 * @return The specified GameItemUse with the specified GameItemModifier applied, or the original GameItemUse if unable to add the GameItemModifier
	 */
	public GameItem addModifier(GameItemModifier modifier) {
		List<GameItemModifier> modifiers = this.getModifiers();
		if(modifiers != null && !modifiers.isEmpty()) {
			for(int i = 0; i < modifiers.size(); i++) {
				if(modifiers.get(i).getName().equals(modifier.getName())) {//if GameItemUse already has GameItemModifier desired
					return this;
				}
			}
		} else {
			modifiers.add(modifier);
			this.setModifiers(modifiers);//add desired prefix to the array
		}
		return this;
	}

	/**
	 * addRandomModifier(int,List<GameItemModifier>) If any GameItemModifiers exist that can be applied to the specified
	 * 		GameItemUse, this method will add up to numNewModifiers GameItemModifiers to the GameItemUse
	 * @param numNewModifiers The number of new GameItemModifiers to add to the GameItem
	 * @param allModifiers A list of all GameItemModifiers available in the Game
	 * @return The GameItemUse, with numNewModifiers GameItemModifiers added, and GameItemUse statistics adjusted accordingly
	 */
	public GameItem addRandomPrefix(int numNewModifiers, List<GameItemModifier> allModifiers) {
		List<GameItemModifier> filteredModifiers = this.getApplicableModifiers(allModifiers);
		if(filteredModifiers.size() > 0) {
			for(int i = 0; i < numNewModifiers; i++) {
				List<GameItemModifier> modifiers = this.getModifiers();
				filteredModifiers.removeAll(modifiers);//make sure we don't add a duplicate GameItemModifier
				if(filteredModifiers.size() > 0) {
					modifiers.add(filteredModifiers.get((int)(Math.random() * filteredModifiers.size())));
				} else {
					this.setModifiers(modifiers);
					return this;
				}
				this.setModifiers(modifiers);
			}
		}
		return this;
	}

	/**
	 * getApplicableModifiers(List<GameItemModifier>) Returns a list of all GameItemModifiers that can be applied to the specified GameItemUse
	 * @param allModifiers A list of all GameItemModifiers in the Game
	 * @return A list of all GameItemModifiers that could be applied to the specified GameItemUse
	 */
	public List<GameItemModifier> getApplicableModifiers(List<GameItemModifier> allModifiers) {
		List<GameItemModifier> filteredPrefixes = new ArrayList<GameItemModifier>();
		for(GameItemModifier modifier: allModifiers) {
			if(		(modifier.getItemType().equals(this.getItemType()) || modifier.getItemType().equals("ANY")) && 
					(modifier.getAttackType() == -1) && 
					(modifier.getDamageType() == -1)		) {
				filteredPrefixes.add(modifier);
			}
		}
		return filteredPrefixes;
	}

	/**
	 * getByteString() Converts the current GameItemUse into a String so it can be written to disk
	 * @return String containing the data of this GameItemUse
	 */
	public String getByteString() {
		String dataString = this.getAssetType() + "|" + this.getUid() + "|" + this.getItemType() + "|" + this.getName() + "|" + this.getDescription() 
				+ "|" + this.getItemValue() + "|" + this.getItemWeight() + "|" + this.getQuantity() + "|" + this.getRarity() + "|" + this.getOwner()
				+ "|" + this.getCurUses() + "|" + this.getMaxUses() + "|";
		List<GameItemModifier> modifiers = this.getModifiers();
		if(modifiers != null && !modifiers.isEmpty()) {
			for(int i = 0; i < modifiers.size(); i++) {
				dataString += modifiers.get(i).getByteString();
				if(i < modifiers.size() - 1) {
					dataString += "ß";
				}
			}
		} else {
			dataString += "null";
		}
		return dataString;
	}

	/**
	 * getBytes() Returns a representation of the GameItemUse in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameItemUse
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameItemUse contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameItemUse containing the data parsed from the byte array
	 */
	public static GameItem parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] itemData = s.split("\\|");
		GameItemUse item = null;
		if(itemData != null && itemData.length == 13 && itemData[0].equals("GameItemUse")) {//GameAsset(2) + GameItem(8) + GameItemUse(3)
			String[] modifierData = itemData[12].split("ß");
			List<GameItemModifier> modifiers = new ArrayList<GameItemModifier>();
			for(String modData: modifierData) {
				GameItemModifier modifier = GameItemModifier.parseBytes(modData.getBytes(),false);
				if(item != null) {
					modifiers.add(modifier);
				}
			}
			item = new GameItemUse();
			item.setAssetType(itemData[0]);//assetType
			item.setUid(itemData[1]);//uid
			item.setItemType(itemData[2]);//itemType
			item.setName(itemData[3]);//name
			item.setDescription(itemData[4]);//description
			item.setItemValue(Integer.parseInt(itemData[5]));//itemValue
			item.setItemWeight(Integer.parseInt(itemData[6]));//itemWeight
			item.setQuantity(Integer.parseInt(itemData[7]));//quantity
			item.setRarity(Integer.parseInt(itemData[8]));//rarity
			item.setOwner(itemData[9]);//owner
			item.setCurUses(Integer.parseInt(itemData[10]));//curUses
			item.setMaxUses(Integer.parseInt(itemData[11]));//maxUses
			item.setModifiers(modifiers);//modifiers
			if(isTemplate) {
				GameItem.add(item);
			}
		}
		return item;
	}
}
