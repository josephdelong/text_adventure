package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	protected int damageDealt;//TODO: change this to damageDie
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
	protected int attackBonusMagic;
	protected boolean isBreakable;
	protected int curDurability;
	protected int maxDurability;
	protected int attackType;
	protected int minRange;
	protected int maxRange;
	protected int reachDistance;
	protected int damageType;
	//more additional parameters to enable random item prefixes/suffixes
	protected String itemType;
	protected int minDamage;//TODO: change this to numDie & move up into other constructor
	protected int maxDamage;//TODO: change this to bonusDamage & move up into other constructor
	protected double rarityLevel;//a multiplier for any instances of Prefix/Suffix bonuses (so that they scale appropriately with base item rarity)
	protected GameItemPrefix[] prefixes;
	protected GameItemSuffix[] suffixes;

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

	protected static final String itemType_WEAPON = "WEAPON";
	protected static final String itemType_ARMOR = "ARMOR";
	protected static final String itemType_WORN = "WORN";
	protected static final String itemType_MISC = "MISC";

	protected static final int attackType_MELEE = 0;
	protected static final int attackType_RANGED = 1;
	protected static final int attackType_THROWN = 2;
	protected static final int attackType_MAGIC = 3;//tells the game to not use damageDie from base item, but from Prefixes/Suffixes instead
	
	protected static final int damageType_BLUDGEON = 0;
	protected static final int damageType_SLASH = 1;
	protected static final int damageType_PIERCE = 2;
	protected static final int damageType_MAGIC = 3;//use the damageType of the GameSpell cast / inferred from magic items
	//taken from GameSpell:
	protected static final String damage_FIRE = "FIRE";
	protected static final String damage_COLD = "COLD";
	protected static final String damage_ELECTRIC = "ELECTRIC";
	protected static final String damage_FORCE = "FORCE";
	protected static final String damage_PSYCHIC = "PSYCHIC";
	protected static final String damage_CRUSHING = "CRUSHING";
	protected static final String damage_HEALING = "HEALING";

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
		this.attackType = -1;
		this.minRange = -1;
		this.maxRange = -1;
		this.reachDistance = -1;
		this.damageType = -1;
		this.itemType = null;
		this.minDamage = -1;
		this.maxDamage = -1;
		this.rarityLevel = 0.0d;
		this.prefixes = null;
		this.suffixes = null;
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
	 * GameItem(GameItem,String,int,int,double,GameItemPrefix[],GameItemSuffix) Constructor which creates a GameItem with the given additional parameters
	 * @param gameItem The Item that the additional parameters will be applied to
	 * @param itemType The type of Item (WEAPON,ARMOR,TRINKET,CLOTHING,MISC,etc.)
	 * @param minDamage The minimum Damage this GameItem will inflict if wielded, CRITs notwithstanding
	 * @param maxDamage The maximum Damage this GameItem will inflict if wielded, CRITs notwithstanding
	 * @param rarityLevel The representation of how rare this GameItem is (This is, generally speaking, an aggregation of the Item & it's modifiers' values)
	 * @param prefixes Any GameItemPrefix which is applied to this GameItem
	 * @param suffixes Anu GameItemSuffix which is applied to this GameItem
	 */
	protected GameItem(GameItem gameItem, String itemType, int minDamage, int maxDamage, double rarityLevel, GameItemPrefix[] prefixes, GameItemSuffix[] suffixes) {
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
		this.armorClass = gameItem.armorClass;
		this.damageReduction = gameItem.damageReduction;
		this.magicResistance = gameItem.magicResistance;
		this.healthPoints = gameItem.healthPoints;
		this.spellPoints = gameItem.spellPoints;
		this.attackBonusMelee = gameItem.attackBonusMelee;
		this.attackBonusRanged = gameItem.attackBonusRanged;
		this.attackBonusMagic = gameItem.attackBonusMagic;
		this.isBreakable = gameItem.isBreakable;
		this.curDurability = gameItem.curDurability;
		this.maxDurability = gameItem.maxDurability;
		this.attackType = gameItem.attackType;
		this.minRange = gameItem.minRange;
		this.maxRange = gameItem.maxRange;
		this.reachDistance = gameItem.reachDistance;
		this.damageType = gameItem.damageType;
		this.itemType = itemType;
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
		this.rarityLevel = rarityLevel;
		this.prefixes = prefixes;
		this.suffixes = suffixes;
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
	 * getFullItemName() Returns the full name of this GameItem, including Prefixes and Suffixes
	 * @return String representation of the full name of this GameItem
	 */
	protected String getFullItemName() {
		String itemName = "";
		for(GameItemPrefix p: this.prefixes) {
			itemName += p.name + " ";
		}
		itemName += this.name;
		if(this.suffixes != null && this.suffixes.length > 0) {
			itemName += " of ";
			for(int i = 0; i < this.suffixes.length; i++) {
				itemName += this.suffixes[i].name;
				if(i != this.suffixes.length - 1) {
					itemName += " and ";
				}
			}
		}
		return itemName;
	}

	/**
	 * addRandomPrefix(GameItem,int,List<GameItemPrefix>) If any Prefixes exist that can be applied to the specified
	 * 		GameItem, this method will add up to numNewPrefixes GameItemPrefixes to the GameItem
	 * @param item The GameItem to add a number of Prefixes to
	 * @param numNewPrefixes The number of new Prefixes to add to the GameItem
	 * @param allPrefixes A list of all Prefixes available in the Game
	 * @return The GameItem, with numNewPrefixes GameItemPrefixes added, and GameItem statistics adjusted accordingly
	 */
	protected static GameItem addRandomPrefix(GameItem item, int numNewPrefixes, List<GameItemPrefix> allPrefixes) {
		List<GameItemPrefix> filteredPrefixes = getApplicablePrefixes(item,allPrefixes);
		if(filteredPrefixes.size() > 1) {
			GameItemPrefix[] prefixes = item.prefixes;
			List<GameItemPrefix> newPrefixes = new ArrayList<GameItemPrefix>();
			for(int i = 0; i < prefixes.length; i++) {
				newPrefixes.add(prefixes[i]);
			}
			for(int i = 0; i < numNewPrefixes; i++) {
				filteredPrefixes.removeAll(newPrefixes);//make sure we don't add a duplicate Prefix
				newPrefixes.add(filteredPrefixes.get((int)(Math.random() * filteredPrefixes.size())));
			}
			prefixes = new GameItemPrefix[newPrefixes.size()];
			for(int i = 0; i < prefixes.length; i++) {
				prefixes[i] = newPrefixes.get(i);
			}
			item.prefixes = prefixes;
		}
		return item;
	}

	/**
	 * addRandomSuffix(GameItem,int,List<GameItemSuffix>) If any Suffixes exist that can be applied to the specified
	 * 		GameItem, this method will add up to numNewSuffixes GameItemSuffixes to the GameItem
	 * @param item The GameItem to add a number of Suffixes to
	 * @param numNewSuffixes The number of new Suffixes to add to the GameItem
	 * @param allSuffixes A list of all Suffixes available in the Game
	 * @return The GameItem, with numNewSuffixes GameItemSuffixes added, and GameItem statistics adjusted accordingly
	 */
	protected static GameItem addRandomSuffix(GameItem item, int numNewSuffixes, List<GameItemSuffix> allSuffixes) {
		List<GameItemSuffix> filteredSuffixes = getApplicableSuffixes(item,allSuffixes);
		if(filteredSuffixes.size() > 1) {
			GameItemSuffix[] suffixes = item.suffixes;
			List<GameItemSuffix> newSuffixes = new ArrayList<GameItemSuffix>();
			for(int i = 0; i < suffixes.length; i++) {
				newSuffixes.add(suffixes[i]);
			}
			for(int i = 0; i < numNewSuffixes; i++) {
				filteredSuffixes.removeAll(newSuffixes);//make sure we don't add a duplicate Suffix
				newSuffixes.add(filteredSuffixes.get((int)(Math.random() * filteredSuffixes.size())));
			}
			suffixes = new GameItemSuffix[newSuffixes.size()];
			for(int i = 0; i < suffixes.length; i++) {
				suffixes[i] = newSuffixes.get(i);
			}
			item.suffixes = suffixes;
		}
		return item;
	}

	/**
	 * getApplicablePrefixes(GameItem,List<GameItemPrefix>) Returns a list of all GameItemPrefixes that can be applied to the specified GameItem
	 * @param item The GameItem which to determine applicable Prefixes for
	 * @param allPrefixes A list of all GameItemPrefixes in the Game
	 * @return A list of all GameItemPrefixes that could be applied to the specified GameItem
	 */
	protected static List<GameItemPrefix> getApplicablePrefixes(GameItem item, List<GameItemPrefix> allPrefixes) {
		List<GameItemPrefix> filteredPrefixes = new ArrayList<GameItemPrefix>();
		for(GameItemPrefix p: allPrefixes) {
			if(		(p.itemType.equals(item.itemType) || p.itemType.equals("itemType_ANY")) && 
					(p.attackType == item.attackType || p.attackType == -1) && 
					(p.damageType == item.damageType || p.damageType == -1)		) {
				filteredPrefixes.add(p);
			}
		}
		return filteredPrefixes;
	}

	/**
	 * getApplicableSuffixes(GameItem,List<GameItemSuffix>) Returns a list of all GameItemSuffixes that can be applied to the specified GameItem
	 * @param item The GameItem which to determine applicable Suffixes for
	 * @param allSuffixes A list of all GameItemSuffixes in the Game
	 * @return A list of all GameItemSuffixes that could be applied to the specified GameItem
	 */
	protected static List<GameItemSuffix> getApplicableSuffixes(GameItem item, List<GameItemSuffix> allSuffixes) {
		List<GameItemSuffix> filteredSuffixes = new ArrayList<GameItemSuffix>();
		for(GameItemSuffix s: allSuffixes) {
			if(		(s.itemType.equals(item.itemType) || s.itemType.equals("itemType_ANY")) && 
					(s.attackType == item.attackType || s.attackType == -1) && 
					(s.damageType == item.damageType || s.damageType == -1)		) {
				filteredSuffixes.add(s);
			}
		}
		return filteredSuffixes;
	}

	/**
	 * printStats() Test method which returns a String representation of this GameItem's data fields
	 * @return String representation of this GameItem's data fields
	 */
	protected String printStats() {//TODO: might be able to remove some fields now, as they can reside in Prefix/Suffix instead
		String statString = "";
		statString += "FullName: " + this.getFullItemName() + "\n";
		statString += "BaseName: " + this.name + "\n";
		statString += "Weight: " + this.weight + "\n";
		statString += "Value: " + this.value + "\n";
		statString += "Quantity: " + this.quantity + "\n";
		statString += "RemainingUses: " + this.remainingUses + "\n";
		statString += "MaxUses: " + this.maxUses + "\n";
		statString += "Owner: " + this.owner + "\n";
		statString += "Description: " + this.description + "\n";
		statString += "DamageDealt: " + this.damageDealt + "\n";
		statString += "EquipSlot: " + this.equipSlot + "\n";
		statString += "IsEquipped: " + this.isEquipped + "\n";
		statString += "ArmorClass: " + this.armorClass + "\n";
		statString += "DamageReduction: " + this.damageReduction + "\n";
		statString += "MagicResistance: " + this.magicResistance + "\n";
		statString += "HP: " + this.healthPoints + "\n";
		statString += "SP: " + this.spellPoints + "\n";
		statString += "MeleeAttackBonus: " + this.attackBonusMelee + "\n";
		statString += "RangedAttackBonus: " + this.attackBonusRanged + "\n";
		statString += "MagicAttackBonus: " + this.attackBonusMagic + "\n";
		statString += "Breakable: " + this.isBreakable + "\n";
		statString += "Durability: " + this.curDurability + "\n";
		statString += "MaxDurability: " + this.maxDurability + "\n";
		statString += "AttackType: " + this.attackType + "\n";
		statString += "MinRange: " + this.minRange + "\n";
		statString += "MaxRange: " + this.maxRange + "\n";
		statString += "Reach: " + this.reachDistance + "\n";
		statString += "DamageType: " + this.damageType + "\n";
		statString += "ItemType: " + this.itemType + "\n";
		statString += "MinDamage: " + this.minDamage + "\n";
		statString += "MaxDamage: " + this.maxDamage + "\n";
		statString += "Rarity: " + this.rarityLevel + "\n";
		statString += "Prefixes:\n";
		for(GameItemPrefix p: prefixes) {
			statString += "\tName: " + p.name + "\n";
			statString += "\tItemType: " + p.itemType + "\n";
			statString += "\tAttackType: " + p.attackType + "\n";
			statString += "\tDamageType: " + p.damageType + "\n";
			statString += "\tBonusCategories:\n";
			for(String x: p.bonusCategories) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tBonusTypes:\n";
			for(String x: p.bonusTypes) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tMinBonuses:\n";
			for(double x: p.minBonuses) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tMaxBonuses:\n";
			for(double x: p.maxBonuses) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tBaseValueAdd: " + p.baseValueAdd + "\n";
		}
		statString += "Suffixes:\n";
		for(GameItemSuffix s: suffixes) {
			statString += "\tName: " + s.name + "\n";
			statString += "\tItemType: " + s.itemType + "\n";
			statString += "\tAttackType: " + s.attackType + "\n";
			statString += "\tDamageType: " + s.damageType + "\n";
			statString += "\tBonusCategories:\n";
			for(String x: s.bonusCategories) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tBonusTypes:\n";
			for(String x: s.bonusTypes) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tMinBonuses:\n";
			for(double x: s.minBonuses) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tMaxBonuses:\n";
			for(double x: s.maxBonuses) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tBaseValueAdd: " + s.baseValueAdd + "\n";
		}
		return statString;
	}

	/**
	 * getBytes() Converts the current GameItem into a byte[] so it can be written to disk
	 * @return byte[] containing the data of this GameItem
	 */
	protected String getByteString() {
		String returnString = this.name + "|" + this.weight + "|" + this.value + "|" + this.quantity + "|"
				+ this.remainingUses + "|" + this.maxUses + "|" + this.owner + "|" + this.description + "|" 
				+ this.damageDealt + "|" + this.equipSlot + "|" + this.isEquipped + "|" + this.armorClass + "|" 
				+ this.damageReduction + "|" + this.magicResistance + "|" + this.healthPoints + "|" 
				+ this.spellPoints + "|" + this.attackBonusMelee + "|" + this.attackBonusRanged + "|" 
				+ this.attackBonusMagic + "|" + this.isBreakable + "|" + this.curDurability + "|" + this.maxDurability + "|"
				+ this.attackType + "|" + this.minRange + "|" + this.maxRange + "|" + this.reachDistance + "|" 
				+ this.damageType + "|" + this.itemType + "|" + this.minDamage + "|" + this.maxDamage + "|" 
				+ this.rarityLevel + "|";
		if(this.prefixes != null && this.prefixes.length > 0) {
			for(int i = 0; i < prefixes.length; i++) {
				returnString += prefixes[i].getByteString();
				if(i < prefixes.length - 1) {
					returnString += "ß";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "|";
		if(this.suffixes != null && this.suffixes.length > 0) {
			for(int i = 0; i < suffixes.length; i++) {
				returnString += suffixes[i].getByteString();
				if(i < suffixes.length - 1) {
					returnString += "ß";
				}
			}
		} else {
			returnString += "null";
		}
		return returnString;
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
		if(itemData.length >= 11) {
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
			if(itemData.length >= 27) {
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
				if(itemData.length == 33) {
					GameItemPrefix[] prefixes = null;
					GameItemSuffix[] suffixes = null;
					String[] prefixData = itemData[31].split("ß");
					String[] suffixData = itemData[32].split("ß");
					if(prefixData != null && prefixData.length > 0 && !prefixData[0].equals("null")) {
						prefixes = new GameItemPrefix[prefixData.length];
						for(int i = 0; i < prefixes.length; i++) {
							GameItemPrefix prefix = GameItemPrefix.parseBytes(prefixData[i].getBytes());
							if(prefix != null) {
								prefixes[i] = prefix;
							}
						}
					}
					if(suffixData != null && suffixData.length > 0 && !suffixData[0].equals("null")) {
						suffixes = new GameItemSuffix[suffixData.length];
						for(int i = 0; i < suffixes.length; i++) {
							GameItemSuffix suffix = GameItemSuffix.parseBytes(suffixData[i].getBytes());
							if(suffix != null) {
								suffixes[i] = suffix;
							}
						}
					}
					item = new GameItem(
						item,
						itemData[27],//itemType
						Integer.parseInt(itemData[28]),//minDamage
						Integer.parseInt(itemData[29]),//maxDamage
						Double.parseDouble(itemData[30]),//rarityLevel
						prefixes,
						suffixes
					);
				}
			}
		}
		return item;
	}
}