package game.asset.item;

import java.util.ArrayList;
import java.util.List;

import game.asset.GameAsset;
import game.asset.GameItemModifier;

/**
 * GameItemWorn Class that represents a GameItem that a GameActor may wield as a Weapon, etc.
 * @author Joseph DeLong
 */
public class GameItemWorn extends GameItemEquip {
	private int damageReduction;
	private int magicResistance;
	private int armorClass;

	/**
	 * GameItemWorn() Constructor used for TEMPLATE creation
	 */
	public GameItemWorn() {
		super();
		this.setAssetType(GameAsset.assetType_ITEM_WORN);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType("WORN");

		this.setEquipSlot("null");
		this.setIsEquipped(false);
		this.setModifiers(new ArrayList<GameItemModifier>());

		this.setDamageReduction(0);
		this.setMagicResistance(0);
		this.setArmorClass(0);
	}

	/**
	 * GameItemWorn(String,String,String,String,String,int,int,int,int,String,String,boolean,List<GameItemModifier>,int,int,int,int,int,int,int,int)
	 */
	public GameItemWorn(
			String assetType, String uid, String itemType, String name, String description, int itemValue, int itemWeight, int quantity, int rarity, String owner, 
			String equipSlot, boolean isEquipped, List<GameItemModifier> modifiers, int damageReduction, int magicResistance, int armorClass
		) {
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

		this.setDamageReduction(damageReduction);
		this.setMagicResistance(magicResistance);
		this.setArmorClass(armorClass);
	}

	/**
	 * GameItemWorn(GameItemWorn) Given a base GameItemWorn, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameItem.lookupTemplate(itemName)</b></code> in order to create in-game
	 * INSTANCEs of the GameItem TEMPLATE (stored in <code>GameItem.itemTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param itemTemplate The GameItemWorn TEMPLATE to make an INSTANCE copy of
	 */
	public GameItemWorn(GameItemWorn itemTemplate) {
		super(itemTemplate);
		this.setAssetType(GameAsset.assetType_ITEM_WORN);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType(new String(itemTemplate.getItemType()));
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
		this.setDamageReduction(Integer.valueOf(itemTemplate.getDamageReduction()));
		this.setMagicResistance(Integer.valueOf(itemTemplate.getMagicResistance()));
		this.setArmorClass(Integer.valueOf(itemTemplate.getArmorClass()));
	}

	public int getDamageReduction() {return this.damageReduction;}
	public int getMagicResistance() {return this.magicResistance;}
	public int getArmorClass() {return this.armorClass;}

	public void setDamageReduction(int damageReduction) {this.damageReduction = damageReduction;}
	public void setMagicResistance(int magicResistance) {this.magicResistance = magicResistance;}
	public void setArmorClass(int armorClass) {this.armorClass = armorClass;}

	/**
	 * getByteString() Converts the current GameItemWorn into a String so it can be written to disk
	 * @return String containing the data of this GameItemWorn
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
		dataString += "|" + this.getDamageReduction() + "|" + this.getMagicResistance() + "|" + this.getArmorClass();
		return dataString;
	}

	/**
	 * getBytes() Returns a representation of the GameItemWorn in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameItemWorn
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameItemWorn contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameItemWorn containing the data parsed from the byte array
	 */
	public static GameItem parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] itemData = s.split("\\|");
		GameItemWorn item = null;
		if(itemData != null && itemData.length == 16 && itemData[0].equals("GameItemWorn")) {//GameAsset(2) + GameItem(8) + GameItemEquip(3) + GameItemWorn(3)
			String[] modifierData = itemData[12].split("ß");
			List<GameItemModifier> modifiers = new ArrayList<GameItemModifier>();
			for(String modData: modifierData) {
				GameItemModifier modifier = GameItemModifier.parseBytes(modData.getBytes(),false);
				if(item != null) {
					modifiers.add(modifier);
				}
			}
			item = new GameItemWorn();
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
			item.setDamageReduction(Integer.parseInt(itemData[13]));//damageReduction
			item.setMagicResistance(Integer.parseInt(itemData[14]));//magicResistance
			item.setArmorClass(Integer.parseInt(itemData[15]));//armorClass
			if(isTemplate) {
				GameItem.add(item);
			}
		}
		return item;
	}
}
