package game.asset.item;

import java.util.ArrayList;
import java.util.List;

import game.asset.GameAsset;
import game.asset.GameItemModifier;

/**
 * GameItemEquip Class that represents a GameItem that a GameActor may equip
 * @author Joseph DeLong
 */
public class GameItemEquip extends GameItem {
	private String equipSlot;
	private boolean isEquipped;
	private List<GameItemModifier> modifiers;

	/**
	 * GameItemEquip() Constructor used for TEMPLATE creation
	 */
	public GameItemEquip() {
		super();
		this.setAssetType(GameAsset.assetType_ITEM_EQUIP);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType(GameItem.itemType_EQUIP);

		this.setEquipSlot("null");
		this.setIsEquipped(false);
		this.setModifiers(new ArrayList<GameItemModifier>());
	}

	/**
	 * GameItemEquip(String,String,String,String,String,int,int,int,int,String,String,boolean,List<GameItemModifier>)
	 */
	public GameItemEquip(
			String assetType, String uid, String itemType, String name, String description, int itemValue, int itemWeight, int quantity, int rarity, String owner, 
			String equipSlot, boolean isEquipped, List<GameItemModifier> modifiers) {
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

		this.setEquipSlot(equipSlot);
		this.setIsEquipped(isEquipped);
		this.setModifiers(modifiers);
	}

	/**
	 * GameItemEquip(GameItemEquip) Given a base GameItemEquip, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameItem.lookupTemplate(itemName)</b></code> in order to create in-game
	 * INSTANCEs of the GameItem TEMPLATE (stored in <code>GameItem.itemTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param itemTemplate The GameItemEquip TEMPLATE to make an INSTANCE copy of
	 */
	public GameItemEquip(GameItemEquip itemTemplate) {
		super(itemTemplate);
		this.setAssetType(GameAsset.assetType_ITEM_EQUIP);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType(GameItem.itemType_EQUIP);
		this.setName(new String(itemTemplate.getName()));
		this.setDescription(new String(itemTemplate.getDescription()));
		this.setItemValue(Integer.valueOf(itemTemplate.getItemValue()));
		this.setItemWeight(Integer.valueOf(itemTemplate.getItemWeight()));
		this.setQuantity(Integer.valueOf(itemTemplate.getQuantity()));
		this.setRarity(Integer.valueOf(itemTemplate.getRarity()));
		this.setOwner(new String(itemTemplate.getOwner()));

		this.setEquipSlot(new String(itemTemplate.getEquipSlot()));
		this.setIsEquipped(Boolean.valueOf(itemTemplate.isEquipped()));
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

	public String getEquipSlot() {return this.equipSlot;}
	public boolean isEquipped() {return this.isEquipped;}
	public List<GameItemModifier> getModifiers() {return this.modifiers;}

	public void setEquipSlot(String equipSlot) {this.equipSlot = equipSlot;}
	public void setIsEquipped(boolean isEquipped) {this.isEquipped = isEquipped;}
	public void setModifiers(List<GameItemModifier> modifiers) {this.modifiers = modifiers;}

	/**
	 * getByteString() Converts the current GameItemEquip into a String so it can be written to disk
	 * @return String containing the data of this GameItemEquip
	 */
	public String getByteString() {
		String dataString = this.getAssetType() + "|" + this.getUid() + "|" + this.getItemType() + "|" + this.getName() + "|" + this.getDescription() 
				+ "|" + this.getItemValue() + "|" + this.getItemWeight() + "|" + this.getQuantity() + "|" + this.getRarity() + "|" + this.getOwner()
				+ "|" + this.getEquipSlot() + "|" + this.isEquipped() + "|";
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
	 * getBytes() Returns a representation of the GameItemEquip in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameItemEquip
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameItemEquip contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameItemEquip containing the data parsed from the byte array
	 */
	public static GameItem parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] itemData = s.split("\\|");
		GameItemEquip item = null;
		if(itemData != null && itemData.length == 13 && itemData[0].equals("GameItemEquip")) {//GameAsset(2) + GameItem(8) + GameItemEquip(3)
			String[] modifierData = itemData[12].split("ß");
			List<GameItemModifier> modifiers = new ArrayList<GameItemModifier>();
			for(String modData: modifierData) {
				GameItemModifier modifier = GameItemModifier.parseBytes(modData.getBytes(),false);
				if(item != null) {
					modifiers.add(modifier);
				}
			}
			item = new GameItemEquip();
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
			item.setEquipSlot(itemData[10]);//equipSlot
			item.setIsEquipped(Boolean.parseBoolean(itemData[11]));//isEquipped
			item.setModifiers(modifiers);//modifiers
			if(isTemplate) {
				GameItem.add(item);
			}
		}
		return item;
	}
}
