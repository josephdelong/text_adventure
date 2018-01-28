package game.asset.item;

import game.asset.GameAsset;

import java.util.HashMap;
import java.util.Map;

/**
 * GameItem A class that represents an Item in the Game
 * @author Joseph DeLong
 */
public class GameItem extends GameAsset {
	private static Map<String,GameItem> itemTemplates;
	
	private String itemType;
	private String name;
	private String description;
	private int itemValue;
	private int itemWeight;
	private int quantity;
	private int rarity;
	private String owner;

	public static final String BACK = "BACK";
	public static final String BELT = "BELT";
	public static final String BOOTS = "BOOTS";
	public static final String CUIRASS = "CUIRASS";
	public static final String GAUNTLETS = "GAUNTLETS";
	public static final String GREAVES = "GREAVES";
	public static final String HELM = "HELM";
	public static final String MAIN_HAND = "MAIN_HAND";
	public static final String OFF_HAND = "OFF_HAND";
	public static final String TWO_HAND = "TWO_HAND";
	public static final String PANTS = "PANTS";
	public static final String SHIRT = "SHIRT";
	public static final String SHOES = "SHOES";
	public static final String RING = "RING";
	public static final String AMULET = "AMULET";
	public static final String EARRING = "EARRING";
	public static final String CLOAK = "CLOAK";

	public static final String itemType_EQUIP = "EQUIP";
	public static final String itemType_WEAPON = "WEAPON";
	public static final String itemType_ARMOR = "ARMOR";
	public static final String itemType_CLOTHING = "WORN";
	public static final String itemType_TRINKET = "TRINKET";
	public static final String itemType_CONSUMABLE = "CONSUMABLE";
	public static final String itemType_HELD = "HELD";
	public static final String itemType_USE = "USE";
	public static final String itemType_KEY = "KEY";

	public static final int attackType_MELEE = 0;
	public static final int attackType_RANGED = 1;
	public static final int attackType_THROWN = 2;
	public static final int attackType_MAGIC = 3;//tells the game to not use damageDie from base item, but from Prefixes/Suffixes instead
	
	public static final int damageType_BLUDGEON = 0;
	public static final int damageType_SLASH = 1;
	public static final int damageType_PIERCE = 2;
	public static final int damageType_MAGIC = 3;//use the damageType of the GameSpell cast / inferred from magic itemTemplates

	/**
	 * GameItem() Constructor used for CREATION MODE
	 */
	public GameItem() {
		this.setAssetType(GameAsset.assetType_ITEM);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType("null");
		this.setName("null");
		this.setDescription("null");
		this.setItemValue(0);
		this.setItemWeight(0);
		this.setQuantity(0);
		this.setRarity(0);
		this.setOwner("null");
	}

	/**
	 * GameItem(String,String,String,int,int,int,int,String) Constructor which creates a GameItem with the given parameters
	 */
	public GameItem(
			String assetType, String uid, String itemType, String name, String description, int itemValue, int itemWeight, int quantity, int rarity, String owner) {
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
	}

	/**
	 * GameItem(GameItem) Given a base GameItem, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameItem.lookupTemplate(itemName)</b></code> in order to create in-game
	 * INSTANCEs of the GameItem Template (stored in <code>GameItem.itemTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param itemTemplate The GameItem TEMPLATE to make an INSTANCE copy of
	 */
	public GameItem(GameItem itemTemplate) {
		this.setAssetType(GameAsset.assetType_ITEM);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType(new String(itemTemplate.getItemType()));
		this.setName(new String(itemTemplate.getName()));
		this.setDescription(new String(itemTemplate.getDescription()));
		this.setItemValue(Integer.valueOf(itemTemplate.getItemValue()));
		this.setItemWeight(Integer.valueOf(itemTemplate.getItemWeight()));
		this.setQuantity(Integer.valueOf(itemTemplate.getQuantity()));
		this.setRarity(Integer.valueOf(itemTemplate.getRarity()));
		this.setOwner(new String(itemTemplate.getOwner()));
	}

	public static Map<String, GameItem> getItemTemplates() {return GameItem.itemTemplates;}

	public String getItemType() {return this.itemType;}
	public String getName() {return this.name;}
	public String getDescription() {return this.description;}
	public int getItemValue() {return this.itemValue;}
	public int getItemWeight() {return this.itemWeight;}
	public int getQuantity() {return this.quantity;}
	public int getRarity() {return this.rarity;}
	public String getOwner() {return this.owner;}

	public void setItemType(String itemType) {this.itemType = itemType;}
	public void setName(String name) {this.name = name;}
	public void setDescription(String description) {this.description = description;}
	public void setItemValue(int itemValue) {this.itemValue = itemValue;}
	public void setItemWeight(int itemWeight) {this.itemWeight = itemWeight;}
	public void setQuantity(int quantity) {this.quantity = quantity;}
	public void setRarity(int rarity) {this.rarity = rarity;}
	public void setOwner(String owner) {this.owner = owner;}

	/**
	 * add(GameItem) Adds the specified GameItem TEMPLATE to the available Item TEMPLATEs in the Game, if not already present
	 * @param item The GameItem TEMPLATE to add
	 */
	public static void add(GameItem item) {
		if(itemTemplates == null) {
			itemTemplates = new HashMap<String,GameItem>();
		}
		if(!itemTemplates.containsKey(item.name)) {
			itemTemplates.put(item.name.toLowerCase(),item);
		}
	}

	/**
	 * lookupTemplate(String) Returns a GameItem TEMPLATE by its name
	 * @param itemName Name of the GameItem TEMPLATE to lookup
	 * @return GameItem TEMPLATE of the specified name, if found
	 */
	public static GameItem lookupTemplate(String itemName) {
		if(itemTemplates == null || itemTemplates.isEmpty()) {
			return null;
		}
		return itemTemplates.get(itemName.toLowerCase());
	}

	/**
	 * getTotalWeight() 
	 * @return The total Weight of the GameItem (itemWeight * quantity)
	 */
	public int getTotalWeight() {
		return this.getItemWeight() * this.getQuantity();
	}

	/**
	 * getByteString() Converts the current GameItem into a String so it can be written to disk
	 * @return String containing the data of this GameItem
	 */
	public String getByteString() {
		String dataString = this.getAssetType() + "|" + this.getUid() + "|" + this.getItemType() + "|" + this.getName() + "|" + this.getDescription() 
				+ "|" + this.getItemValue() + "|" + this.getItemWeight() + "|" + this.getQuantity() + "|" + this.getRarity() + "|" + this.getOwner();
		return dataString;
	}

	/**
	 * getBytes() Returns a representation of the GameItem in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameItem
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameItem contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameItem containing the data parsed from the byte array
	 */
	public static GameItem parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] itemData = s.split("\\|");
		GameItem item = null;
		if(itemData != null && itemData.length == 10 && itemData[0].equals("GameItem")) {//GameAsset(2) + GameItem(8)
			item = new GameItem();
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
			if(isTemplate) {
				GameItem.add(item);
			}
		}
		return item;
	}
}