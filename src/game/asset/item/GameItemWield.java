package game.asset.item;

import java.util.ArrayList;
import java.util.List;

import game.asset.GameAsset;
import game.asset.GameItemModifier;

/**
 * GameItemWield Class that represents a GameItem that a GameActor may wield as a Weapon, etc.
 * @author Joseph DeLong
 */
public class GameItemWield extends GameItemEquip {
	private int attackType;
	private int damageType;
	private int damageDie;
	private int numDie;
	private int bonusDamage;
	private int minRange;
	private int maxRange;
	private int reach;

	/**
	 * GameItemWield() Constructor used for TEMPLATE creation
	 */
	public GameItemWield() {
		super();
		this.setAssetType(GameAsset.assetType_ITEM_WIELD);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType("WIELD");

		this.setEquipSlot("null");
		this.setIsEquipped(false);
		this.setModifiers(new ArrayList<GameItemModifier>());

		this.setAttackType(-1);
		this.setDamageType(-1);
		this.setDamageDie(0);
		this.setNumDie(0);
		this.setBonusDamage(0);
		this.setMinRange(0);
		this.setMaxRange(0);
		this.setReach(0);
	}

	/**
	 * GameItemWield(String,String,String,String,int,int,int,int,String,String,boolean,List<GameItemModifier>,int,int,int,int,int,int,int,int)
	 */
	public GameItemWield(
			String assetType, String uid, String itemType, String name, String description, int itemValue, int itemWeight, int quantity, int rarity, String owner, 
			String equipSlot, boolean isEquipped, List<GameItemModifier> modifiers, int attackType, int damageType, int damageDie, int numDie, int bonusDamage, 
			int minRange, int maxRange, int reach
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

		this.setAttackType(attackType);
		this.setDamageType(damageType);
		this.setDamageDie(damageDie);
		this.setNumDie(numDie);
		this.setBonusDamage(bonusDamage);
		this.setMinRange(minRange);
		this.setMaxRange(maxRange);
		this.setReach(reach);
	}

	/**
	 * GameItemWield(GameItemWield) Given a base GameItemWield, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameItem.lookupTemplate(itemName)</b></code> in order to create in-game
	 * INSTANCEs of the GameItem TEMPLATE (stored in <code>GameItem.itemTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param itemTemplate The GameItemWield TEMPLATE to make an INSTANCE copy of
	 */
	public GameItemWield(GameItemWield itemTemplate) {
		super(itemTemplate);
		this.setAssetType(GameAsset.assetType_ITEM_WIELD);
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
		this.setAttackType(Integer.valueOf(itemTemplate.getAttackType()));
		this.setDamageType(Integer.valueOf(itemTemplate.getDamageType()));
		this.setDamageDie(Integer.valueOf(itemTemplate.getDamageDie()));
		this.setNumDie(Integer.valueOf(itemTemplate.getNumDie()));
		this.setBonusDamage(Integer.valueOf(itemTemplate.getBonusDamage()));
		this.setMinRange(Integer.valueOf(itemTemplate.getMinRange()));
		this.setMaxRange(Integer.valueOf(itemTemplate.getMaxRange()));
		this.setReach(Integer.valueOf(itemTemplate.getReach()));
	}

	public int getAttackType() {return attackType;}
	public int getDamageType() {return damageType;}
	public int getDamageDie() {return damageDie;}
	public int getNumDie() {return numDie;}
	public int getBonusDamage() {return bonusDamage;}
	public int getMinRange() {return minRange;}
	public int getMaxRange() {return maxRange;}
	public int getReach() {return reach;}

	public void setAttackType(int attackType) {this.attackType = attackType;}
	public void setDamageType(int damageType) {this.damageType = damageType;}
	public void setDamageDie(int damageDie) {this.damageDie = damageDie;}
	public void setNumDie(int numDie) {this.numDie = numDie;}
	public void setBonusDamage(int bonusDamage) {this.bonusDamage = bonusDamage;}
	public void setMinRange(int minRange) {this.minRange = minRange;}
	public void setMaxRange(int maxRange) {this.maxRange = maxRange;}
	public void setReach(int reach) {this.reach = reach;}

	/**
	 * calculateDamage(GameItemWield) Calculates the GameItemWield's DAMAGE dealt when a hit lands (before any modifiers (bonusDamage) )
	 * @param item The GameItemWield to use for DAMAGE calculation
	 * @return The total DAMAGE this instance of the GameItemWield inflicts
	 */
	public static int calculateDamage(GameItemWield item) {
		int damage = 0;
		if(item != null) {
			for(int i = 0; i < item.getNumDie(); i++) {//this will change to nnumDie
				damage += Double.valueOf(Math.random() * item.damageDie).intValue() + 1;
			}
			List<GameItemModifier> itemModifiers = item.getModifiers();
			if(itemModifiers != null && !itemModifiers.isEmpty()) {
				for(int i = 0; i < itemModifiers.size(); i++) {
					GameItemModifier modifier = itemModifiers.get(i);
					for(int x = 0; x < modifier.getBonusCategories().length; x++) {
						if(modifier.getBonusCategories()[x].equals("damageDealt")) {
							String type = modifier.getBonusTypes()[x];
							if(type.equals("X")) {//damage multiplication
								damage *= Math.min(modifier.getMinBonuses()[x],(Math.random() * modifier.getMaxBonuses()[x]) + 1);
							} else if(type.equals("/")) {//damage division
								damage /= Math.min(modifier.getMinBonuses()[x],(Math.random() * modifier.getMaxBonuses()[x]) + 1);
							} else if(type.matches("damageType_(HEALING){0}.*")) {//damage addition
								damage += (int) Math.min(modifier.getMinBonuses()[x],Double.valueOf(Math.random() * modifier.getMaxBonuses()[x]).intValue() + 1);
							}
						}
					}
				}
			}
		}
		return damage;
	}

	/**
	 * getFullItemName() Returns the full name of this GameItemWield, including GameItemModifiers
	 * @return String representation of the full name of this GameItemWield
	 */
	public String getFullItemName() {
		String itemName = "";
		List<GameItemModifier> modifiers = this.getModifiers();
		if(modifiers != null && modifiers.size() > 0) {
			List<GameItemModifier> prefixes = new ArrayList<GameItemModifier>();
			List<GameItemModifier> suffixes = new ArrayList<GameItemModifier>();
			for(GameItemModifier mod: modifiers) {
				if(mod.getModifierType().equals(GameItemModifier.modifierType_PREFIX)) {
					prefixes.add(mod);
				} else if(mod.getModifierType().equals(GameItemModifier.modifierType_SUFFIX)) {
					suffixes.add(mod);
				}
			}
			for(GameItemModifier prefix: prefixes) {
				itemName += prefix.getName() + " ";
			}
			itemName += this.getName();
			if(suffixes != null && suffixes.size() > 0) {
				itemName += " of ";
				for(int i = 0; i < suffixes.size(); i++) {
					itemName += suffixes.get(i).getName();
					if(i != suffixes.size() - 1) {
						itemName += " and ";
					}
				}
			}
		}
		return itemName;
	}

	/**
	 * addModifer(GameItemModifier) Adds the specified GameItemModifer to the specified GameItemWield, if able
	 * @param modifier The GameItemModifier to add to the GameItemWield
	 * @return The specified GameItemWield with the specified GameItemModifier applied, or the original GameItemWield if unable to add the GameItemModifier
	 */
	public GameItem addModifier(GameItemModifier modifier) {
		List<GameItemModifier> modifiers = this.getModifiers();
		if(modifiers != null && !modifiers.isEmpty()) {
			for(int i = 0; i < modifiers.size(); i++) {
				if(modifiers.get(i).getName().equals(modifier.getName())) {//if GameItemWield already has GameItemModifier desired
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
	 * 		GameItemWield, this method will add up to numNewModifiers GameItemModifiers to the GameItemWield
	 * @param numNewModifiers The number of new GameItemModifiers to add to the GameItem
	 * @param allModifiers A list of all GameItemModifiers available in the Game
	 * @return The GameItemWield, with numNewModifiers GameItemModifiers added, and GameItemWield statistics adjusted accordingly
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
	 * getApplicableModifiers(List<GameItemModifier>) Returns a list of all GameItemModifiers that can be applied to the specified GameItemWield
	 * @param allModifiers A list of all GameItemModifiers in the Game
	 * @return A list of all GameItemModifiers that could be applied to the specified GameItemWield
	 */
	public List<GameItemModifier> getApplicableModifiers(List<GameItemModifier> allModifiers) {
		List<GameItemModifier> filteredPrefixes = new ArrayList<GameItemModifier>();
		for(GameItemModifier modifier: allModifiers) {
			if(		(modifier.getItemType().equals(this.getItemType()) || modifier.getItemType().equals("ANY")) && 
					(modifier.getAttackType() == this.getAttackType() || modifier.getAttackType() == -1) && 
					(modifier.getAttackType() == this.getDamageType() || modifier.getDamageType() == -1)		) {
				filteredPrefixes.add(modifier);
			}
		}
		return filteredPrefixes;
	}

	/**
	 * getByteString() Converts the current GameItemWield into a String so it can be written to disk
	 * @return String containing the data of this GameItemWield
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
		dataString += "|" + this.getAttackType() + "|" + this.getDamageType() + "|" + this.getDamageDie() + "|" + this.getNumDie() + "|" + this.getBonusDamage() 
				+ "|" + this.getMinRange() + "|" + this.getMaxRange() + "|" + this.getReach();
		return dataString;
	}

	/**
	 * getBytes() Returns a representation of the GameItemWield in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameItemWield
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameItemWield contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameItemWield containing the data parsed from the byte array
	 */
	public static GameItem parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] itemData = s.split("\\|");
		GameItemWield item = null;
		if(itemData != null && itemData.length == 21 && itemData[0].equals("GameItemWield")) {//GameAsset(2) + GameItem(8) + GameItemEquip(3) + GameItemWield(8)
			String[] modifierData = itemData[12].split("ß");
			List<GameItemModifier> modifiers = new ArrayList<GameItemModifier>();
			for(String modData: modifierData) {
				GameItemModifier modifier = GameItemModifier.parseBytes(modData.getBytes(),false);
				if(item != null) {
					modifiers.add(modifier);
				}
			}
			item = new GameItemWield();
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
			item.setAttackType(Integer.parseInt(itemData[13]));//attackType
			item.setDamageType(Integer.parseInt(itemData[14]));//damageType
			item.setDamageDie(Integer.parseInt(itemData[15]));//damageDie
			item.setNumDie(Integer.parseInt(itemData[16]));//numDie
			item.setBonusDamage(Integer.parseInt(itemData[17]));//bonusDamage
			item.setMinRange(Integer.parseInt(itemData[18]));//minRange
			item.setMaxRange(Integer.parseInt(itemData[19]));//maxRange
			item.setReach(Integer.parseInt(itemData[20]));//reach
			if(isTemplate) {
				GameItem.add(item);
			}
		}
		return item;
	}

	/**
	 * printStats() Test method which returns a String representation of this GameItem's data fields
	 * @return String representation of this GameItem's data fields
	 */
	public String printStats() {
		String statString = "";
		statString += "FullName: " + this.getFullItemName() + "\n";
		statString += "AssetType: " + this.getAssetType() + "\n";
		statString += "UID: " + this.getUid() + "\n";
		statString += "ItemType: " + this.getItemType() + "\n";
		statString += "Name: " + this.getName() + "\n";
		statString += "Description: " + this.getDescription() + "\n";
		statString += "ItemValue: " + this.getItemValue() + "\n";
		statString += "ItemWeight: " + this.getItemWeight() + "\n";
		statString += "Quantity: " + this.getQuantity() + "\n";
		statString += "Rarity: " + this.getRarity() + "\n";
		statString += "Owner: " + this.getOwner() + "\n";
		statString += "EquipSlot: " + this.getEquipSlot() + "\n";
		statString += "IsEquipped: " + this.isEquipped() + "\n";
		statString += "Modifiers:\n";
		for(GameItemModifier modifier: this.getModifiers()) {
			statString += "\tName: " + modifier.getName() + "\n";
			statString += "\tItemType: " + modifier.getItemType() + "\n";
			statString += "\tAttackType: " + modifier.getAttackType() + "\n";
			statString += "\tDamageType: " + modifier.getDamageType() + "\n";
			statString += "\tBonusCategories:\n";
			for(String x: modifier.getBonusCategories()) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tBonusTypes:\n";
			for(String x: modifier.getBonusTypes()) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tMinBonuses:\n";
			for(double x: modifier.getMinBonuses()) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tMaxBonuses:\n";
			for(double x: modifier.getMaxBonuses()) {
				statString += "\t\t" + x + "\n";
			}
			statString += "\tBaseValueAdd: " + modifier.getBaseValueAdd() + "\n";
		}
		statString += "AttackType: " + this.getAttackType() + "\n";
		statString += "DamageType: " + this.getDamageType() + "\n";
		statString += "DamageDie: " + this.getDamageDie() + "\n";
		statString += "NumDie: " + this.getNumDie() + "\n";
		statString += "BonusDamage: " + this.getBonusDamage() + "\n";
		statString += "MinRange: " + this.getMinRange() + "\n";
		statString += "MaxRange: " + this.getMaxRange() + "\n";
		statString += "Reach: " + this.getReach() + "\n";
		return statString;
	}
}
