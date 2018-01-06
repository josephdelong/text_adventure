package game;

import java.util.HashMap;
import java.util.Map;

/**
 * GameItem A class that represents an Item in the Game
 * @author Joseph DeLong
 */
public class GameItem {
	protected static Map<String,GameItem> items;

	protected String name;
	protected double weight;
	protected double value;
	protected int quantity;
	protected int remainingUses;
	protected int maxUses;
	protected String owner;
	protected String description;
	protected int damageDealt;
	protected String equipSlot;
	protected boolean isEquipped;
	//additional parameters for armor / equipped items
	protected int armorClass;
	protected int damageReduction;
	protected int magicResistance;
	protected int healthPoints;
	protected int spellPoints;
	protected int attackBonusMelee;
	protected int attackBonusRanged;
	protected int attackBonusMagic;//probably won't differentiate this after all, but it's a neat idea
	protected boolean isBreakable;
	protected int curDurability;
	protected int maxDurability;
	protected int attackType;
	protected int minRange;
	protected int maxRange;
	protected int reachDistance;
	protected int damageType;

	//TODO: implement onBreak, onUse, onEquip, onUnEquip, onTake, onDrop, onAttack, onCrit, onFail, onBlock, onMove, [onOpen, onClose], onThrow, onLook, onListen, onTouch, onSmell, onTaste

	protected static final String BACK = "BACK";
	protected static final String BELT = "BELT";
	protected static final String BOOTS = "BOOTS";
	protected static final String CUIRASS = "CUIRASS";
	protected static final String GAUNTLETS = "GAUNTLETS";
	protected static final String GREAVES = "GREAVES";
	protected static final String HELM = "HELM";
	protected static final String MAIN_HAND = "MAIN_HAND";
	protected static final String OFF_HAND = "OFF_HAND";
	protected static final String PANTS = "PANTS";
	protected static final String SHIRT = "SHIRT";
	protected static final String SHOES = "SHOES";
	protected static final String RING = "RING";
	protected static final String AMULET = "AMULET";
	protected static final String EARRING = "EARRING";
	protected static final String CLOAK = "CLOAK";
	
	protected static final int attackType_MELEE = 0;
	protected static final int attackType_RANGED = 1;
	protected static final int attackType_THROWN = 2;
	protected static final int attackType_MAGIC = 3;//might not ever use...
	
	protected static final int damageType_BLUDGEON = 0;
	protected static final int damageType_SLASH = 1;
	protected static final int damageType_PIERCE = 2;
	protected static final int damageType_MAGIC = 3;//use the damageType of the GameSpell cast / inferred from magic items

	/**
	 * GameItem() Constructor used for CREATION MODE
	 */
	protected GameItem() {
		this.name = "";
		this.weight = 0;
		this.value = 0;
		this.quantity = 0;
		this.remainingUses = -1;
		this.maxUses = -1;
		this.owner = null;
		this.description = "";
		this.damageDealt = 0;
		this.equipSlot = null;
		this.isEquipped = false;
		this.armorClass = 0;;
		this.damageReduction = 0;
		this.magicResistance = 0;
		this.healthPoints = 0;
		this.spellPoints = 0;
		this.attackBonusMelee = 0;
		this.attackBonusRanged = 0;
		this.attackBonusMagic = 0;
		this.isBreakable = false;
		this.curDurability = -1;
		this.maxDurability = -1;
	}

	/**
	 * GameItem(String,double,double,int,int,int,String,String,double,String) Constructor which creates a GameItem with the given parameters
	 * @param name The Name of the Item
	 * @param weight The Item's weight per unit
	 * @param value The Item's value per unit
	 * @param quantity How many of the Item the Player has
	 * @param remainingUses How many uses the Item has left
	 * @param maxUses The maximum number of uses the Item has
	 * @param owner The Owner of the Item
	 * @param description The Item's Description
	 * @param damageDealt The Damage this Item will deal to an Enemy when struck
	 * @param equipSlot The equipment slot the item will occupy if equipped / worn
	 */
	protected GameItem(String name, double weight, double value, int quantity, int remainingUses, int maxUses, String owner, String description, 
			int damageDealt, String equipSlot, boolean isEquipped) {
		this.name = name;
		this.weight = weight;
		this.value = value;
		this.quantity = quantity;
		this.remainingUses = remainingUses;
		this.maxUses = maxUses;
		this.owner = owner;
		this.description = description;
		this.damageDealt = damageDealt;
		this.equipSlot = equipSlot;
		this.isEquipped = isEquipped;
		GameItem.add(this);
	}

	/**
	 * GameItem(GameItem,int,int,int,int,int,int,int,int) Constructor which creates a GameItem with the given additional parameters
	 * @param gameItem The Item that the additional parameters will be applied to
	 * @param armorClass Armor Class granted to the Player if equipped
	 * @param damageReduction Damage Reduction granted to the Player if equipped
	 * @param magicResistance Magic Resistance granted to the Player if equipped
	 * @param healthPoints Additional Health Points granted to the Player if equipped
	 * @param spellPoints Additional Spell Points granted to the Player if equipped
	 * @param attackBonusMelee Additional Melee Attack Bonus granted granted to the Player if equipped
	 * @param attackBonusRanged Additional Ranged Attack Bonus granted to the Player if equipped
	 * @param attackBonusMagic Additional Magic Attack Bonus granted to the Player if equipped
	 * @param isbReakable Indicates whether the Item is breakable
	 * @param curDurability The current durability of the Item
	 * @param maxDurability The maximum durability of the Item
	 * @param attackType The type of attack the Item (Weapon) 
	 * @param minRange The minimum range in which this Item (Weapon) can be used
	 * @param maxRange The maximum range in which this Item (Weapon) can be used
	 * @param reachDistance How many squares way this Item (Weapon) may reach
	 * @param damageType The type of damage the Item (Weapon) deals
	 */
	protected GameItem(GameItem gameItem, int armorClass, int damageReduction, int magicResistance, int healthPoints, int spellPoints, 
			int attackBonusMelee, int attackBonusRanged, int attackBonusMagic, boolean isBreakable, int curDurability, int maxDurability, 
			int attackType, int minRange, int maxRange, int reachDistance, int damageType) {
		this.name = gameItem.name;
		this.weight = gameItem.weight;
		this.value = gameItem.value;
		this.quantity = gameItem.quantity;
		this.remainingUses = gameItem.remainingUses;
		this.maxUses = gameItem.maxUses;
		this.owner = gameItem.owner;
		this.description = gameItem.description;
		this.damageDealt = gameItem.damageDealt;
		this.equipSlot = gameItem.equipSlot;
		this.isEquipped = gameItem.isEquipped;
		this.armorClass = armorClass;
		this.damageReduction = damageReduction;
		this.magicResistance = magicResistance;
		this.healthPoints = healthPoints;
		this.spellPoints = spellPoints;
		this.attackBonusMelee = attackBonusMelee;
		this.attackBonusRanged = attackBonusRanged;
		this.attackBonusMagic = attackBonusMagic;
		this.isBreakable = isBreakable;
		this.curDurability = curDurability;
		this.maxDurability = maxDurability;
		this.attackType = attackType;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.reachDistance = reachDistance;
		this.damageType = damageType;
		GameItem.items.put(this.name.toLowerCase(),this);
	}

	/**
	 * add(GameItem) Adds the specified GameItem to the available Items in the Game, if not already present
	 * @param item The GameItem to add
	 */
	protected static void add(GameItem item) {
		if(items == null) {
			items = new HashMap<String,GameItem>();
		}
		if(!items.containsKey(item.name)) {
			items.put(item.name.toLowerCase(),item);
		}
	}

	/**
	 * not sure I need / want this
	 */
	protected void breakItem() {
		
	}

	/**
	 * lookup(String) Returns an item by its name
	 * @param itemName Name of the Item to lookup
	 * @return GameItem of the specified name, if found
	 */
	protected static GameItem lookup(String itemName) {
		if(items == null || items.isEmpty()) {
			return null;
		}
		return items.get(itemName);
	}

	/**
	 * getTotalWeight()
	 * @return The total Weight of the item (weight * quantity)
	 */
	protected double getTotalWeight() {
		return this.weight * this.quantity;
	}

	/**
	 * Returns a String representation of the Item & its properties
	 * @return
	 */
	protected String getByteString() {
		return this.name + "|" + this.weight + "|" + this.value + "|" + this.quantity + "|"
				+ this.remainingUses + "|" + this.maxUses + "|" + this.owner + "|" + this.description + "|" 
				+ this.damageDealt + "|" + this.equipSlot + "|" + this.isEquipped + "|" + this.armorClass + "|" 
				+ this.damageReduction + "|" + this.magicResistance + "|" + this.healthPoints + "|" 
				+ this.spellPoints + "|" + this.attackBonusMelee + "|" + this.attackBonusRanged + "|" 
				+ this.attackBonusMagic + "|" + this.isBreakable + "|" + this.curDurability + "|" + this.maxDurability + "|"
				+ this.attackType + "|" + this.minRange + "|" + this.maxRange + "|" + this.reachDistance + "|" 
				+ this.damageType;
	}

	/**
	 * getBytes() Returns a representation of the Item in an array of bytes
	 * @return
	 */
	protected byte[] getBytes() {
		String returnString = this.getByteString();
		return returnString.getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameItem
	 * @param bytes The byte array to read
	 * @return GameItem containing the data parsed from the byte array
	 */
	protected static GameItem parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] itemData = s.split("\\|");
		GameItem item = null;
		if(itemData.length <= 27) {
			if(itemData.length < 11) {
				return item;
			}
			item = new GameItem(
				itemData[0],//name
				Double.parseDouble(itemData[1]),//weight
				Double.parseDouble(itemData[2]),//value
				Integer.parseInt(itemData[3]),//quantity
				Integer.parseInt(itemData[4]),//remainingUses
				Integer.parseInt(itemData[5]),//maxUses
				itemData[6],//owner
				itemData[7],//description
				Integer.parseInt(itemData[8]),//damageDealt
				itemData[9],//equipSlot
				Boolean.parseBoolean(itemData[10])//isEquipped
			);
		}
		if(itemData.length == 11) {
			return item;
		}
		item = new GameItem(
			item,
			Integer.parseInt(itemData[11]),//armorClass
			Integer.parseInt(itemData[12]),//damageReduction
			Integer.parseInt(itemData[13]),//magicResistance
			Integer.parseInt(itemData[14]),//healthPoints
			Integer.parseInt(itemData[15]),//spellPoints
			Integer.parseInt(itemData[16]),//attackBonusMelee
			Integer.parseInt(itemData[17]),//attackBonusRanged
			Integer.parseInt(itemData[18]),//attackBonusMagic
			Boolean.parseBoolean(itemData[19]),//isBreakable
			Integer.parseInt(itemData[20]),//curDurability
			Integer.parseInt(itemData[21]),//maxDurability
			Integer.parseInt(itemData[22]),//attackType
			Integer.parseInt(itemData[23]),//minRange
			Integer.parseInt(itemData[24]),//maxRange
			Integer.parseInt(itemData[25]),//reachDistance
			Integer.parseInt(itemData[26])//damageType
		);
		return item;
	}
}