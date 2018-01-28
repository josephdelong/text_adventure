package game.asset;

import java.util.HashMap;
import java.util.Map;

/**
 * GameItemModifier A class that represents a Prefix applied to an Item in the Game
 * @author Joseph DeLong
 */
public class GameItemModifier extends GameAsset {
	private static Map<String,GameItemModifier> modifierTemplates;
	
	private String modifierType;
	private String name;
	private String itemType;
	private int attackType;
	private int damageType;
	private String[] bonusCategories;
	private String[] bonusTypes;
	private int[] minBonuses;
	private int[] maxBonuses;
	private int baseValueAdd;
	
	public static final String modifierType_PREFIX = "prefix";
	public static final String modifierType_SUFFIX = "suffix";

	/**
	 * GameItemModifier() Constructor used for TEMPLATE CREATION MODE
	 */
	public GameItemModifier() {
		this.setAssetType("GameItemModifier");
		this.setUid("GameItemModifier-null-null");
		
		this.setModifierType("");
		this.setName("");
		this.setItemType("");
		this.setAttackType(-1);
		this.setDamageType(-1);
		this.setBonusCategories(new String[]{});
		this.setBonusTypes(new String[]{});
		this.setMinBonuses(new int[]{});
		this.setMaxBonuses(new int[]{});
		this.setBaseValueAdd(0);
	}

	/**
	 * GameItemModifer(String,String,String,String,String,int,int,String[],String[],int[],int[],int)
	 */
	public GameItemModifier(
			String assetType, String uid, String modifierType, String name, String itemType, int attackType, int damageType, String[] bonusCategories, 
			String[] bonusTypes, int[] minBonuses, int[] maxBonuses, int baseValueAdd
		) {
		this.setAssetType(assetType);
		this.setUid(uid);
		
		this.setModifierType(modifierType);
		this.setName(name);
		this.setItemType(itemType);
		this.setAttackType(attackType);
		this.setDamageType(damageType);
		this.setBonusCategories(bonusCategories);
		this.setBonusTypes(bonusTypes);
		this.setMinBonuses(minBonuses);
		this.setMaxBonuses(maxBonuses);
		this.setBaseValueAdd(baseValueAdd);
		
		this.setUid(this.getAssetType() + "-" + this.getModifierType() + "-" + this.getName());
	}

	/**
	 * GameItemModifier(GameItemModifier) Given a base GameItemModifier TEMPLATE, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE data.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameItemModifier.lookupTemplate(templateName)</b></code> in order to create in-game
	 * INSTANCEs of the GameItemModifier TEMPLATE (stored in <code>GameItemModifier.modifierTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param modifierTemplate The GameItemModifier TEMPLATE to make an INSTANCE copy of
	 */
	public GameItemModifier(GameItemModifier modifierTemplate) {
		this.setAssetType(GameAsset.assetType_ITEM_MODIFIER);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setModifierType(new String(modifierTemplate.getModifierType()));
		this.setName(new String(modifierTemplate.getName()));
		this.setItemType(new String(modifierTemplate.getItemType()));
		this.setAttackType(Integer.valueOf(modifierTemplate.getAttackType()));
		this.setDamageType(Integer.valueOf(modifierTemplate.getDamageType()));
		String[] bonusCategories = null;
		String[] templateCategories = modifierTemplate.getBonusCategories();
		if(templateCategories != null && templateCategories.length > 0) {
			bonusCategories = new String[templateCategories.length];
			for(int i = 0; i < bonusCategories.length; i++) {
				bonusCategories[i] = new String(templateCategories[i]);
			}
		} else {
			bonusCategories = new String[]{};
		}
		this.setBonusCategories(bonusCategories);
		String[] bonusTypes = null;
		String[] templateTypes = modifierTemplate.getBonusTypes();
		if(templateTypes != null && templateTypes.length > 0) {
			bonusTypes = new String[templateTypes.length];
			for(int i = 0; i < bonusTypes.length; i++) {
				bonusTypes[i] = new String (templateTypes[i]);
			}
		} else {
			bonusTypes = new String[]{};
		}
		this.setBonusTypes(bonusTypes);
		int[] minBonuses = null;
		int[] templateMinBonuses = modifierTemplate.getMinBonuses();
		if(templateMinBonuses != null && templateMinBonuses.length > 0) {
			minBonuses = new int[templateMinBonuses.length];
			for(int i = 0; i < minBonuses.length; i++) {
				minBonuses[i] = Integer.valueOf(templateMinBonuses[i]);
			}
		} else {
			minBonuses = new int[]{};
		}
		this.setMinBonuses(minBonuses);
		int[] maxBonuses = null;
		int[] templateMaxBonuses = modifierTemplate.getMaxBonuses();
		if(templateMaxBonuses != null && templateMaxBonuses.length > 0) {
			maxBonuses = new int[templateMaxBonuses.length];
			for(int i = 0; i < maxBonuses.length; i++) {
				maxBonuses[i] = Integer.valueOf(templateMaxBonuses[i]);
			}
		} else maxBonuses = new int[]{};
		this.setMaxBonuses(maxBonuses);
		this.setBaseValueAdd(Integer.valueOf(modifierTemplate.getBaseValueAdd()));
	}

	public static Map<String,GameItemModifier> getModifierTemplates() {return GameItemModifier.modifierTemplates;}

	public String getModifierType() {return this.modifierType;}
	public String getName() {return this.name;}
	public String getItemType() {return this.itemType;}
	public int getAttackType() {return this.attackType;}
	public int getDamageType() {return this.damageType;}
	public String[] getBonusCategories() {return this.bonusCategories;}
	public String[] getBonusTypes() {return this.bonusTypes;}
	public int[] getMinBonuses() {return this.minBonuses;}
	public int[] getMaxBonuses() {return this.maxBonuses;}
	public int getBaseValueAdd() {return this.baseValueAdd;}

	public void setModifierType(String modifierType) {this.modifierType = modifierType;}
	public void setName(String name) {this.name = name;}
	public void setItemType(String itemType) {this.itemType = itemType;}
	public void setAttackType(int attackType) {this.attackType = attackType;}
	public void setDamageType(int damageType) {this.damageType = damageType;}
	public void setBonusCategories(String[] bonusCategories) {this.bonusCategories = bonusCategories;}
	public void setBonusTypes(String[] bonusTypes) {this.bonusTypes = bonusTypes;}
	public void setMinBonuses(int[] minBonuses) {this.minBonuses = minBonuses;}
	public void setMaxBonuses(int[] maxBonuses) {this.maxBonuses = maxBonuses;}
	public void setBaseValueAdd(int baseValueAdd) {this.baseValueAdd = baseValueAdd;}

	/**
	 * add(GameItemModifier) Adds the specified GameItemModifier to the available Prefix TEMPLATEs in the Game, if not already present
	 * @param modifier The GameItemModifier to add
	 */
	public static void add(GameItemModifier modifier) {
		if(modifierTemplates == null) {
			modifierTemplates = new HashMap<String,GameItemModifier>();
		}
		if(!modifierTemplates.containsKey(modifier.getName())) {
			modifierTemplates.put(modifier.getName().toLowerCase(),modifier);
		}
	}

	/**
	 * lookup(String) Look up the GameItemModifier TEMPLATE by its Name in the list of current GameItemModifier TEMPLATEs
	 * @param modifierName The Name of the Prefix to find
	 * @return GameItemModifier TEMPLATE identified by the specified name, if found
	 */
	public static GameItemModifier lookup(String modifierName) {
		if(modifierTemplates == null || modifierTemplates.isEmpty()) {
			return null;
		}
		return modifierTemplates.get(modifierName.toLowerCase());
	}

	/**
	 * getBytes() Returns a representation of the GameItemModifier in an array of bytes
	 * @return byte[] containing the data fields of this GameItemModifier
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * getBytes() Converts the current GameItemModifier into a byte[] so it can be written to disk
	 * @return byte[] containing the data of this GameItemModifier
	 */
	public String getByteString() {
		String dataString = this.getAssetType() + "µ" + this.getUid() + "µ" + this.getModifierType() + "µ" + this.getName() + "µ" + this.getItemType() 
				+ "µ" + this.getAttackType() + "µ" + this.getDamageType() + "µ";
		String[] bonusCategories = this.getBonusCategories();
		if(bonusCategories != null && bonusCategories.length > 0) {
			for(int i = 0; i < bonusCategories.length; i++) {
				dataString += bonusCategories[i];
				if(i < bonusCategories.length - 1) {
					dataString += "·";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "µ";
		String[] bonusTypes = this.getBonusTypes();
		if(bonusTypes != null && bonusTypes.length > 0) {
			for(int i = 0; i < bonusTypes.length; i++) {
				dataString += bonusTypes[i];
				if(i < bonusTypes.length - 1) {
					dataString += "·";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "µ";
		int[] minBonuses = this.getMinBonuses();
		if(minBonuses != null && minBonuses.length > 0) {
			for(int i = 0; i < minBonuses.length; i++) {
				dataString += minBonuses[i];
				if(i < minBonuses.length - 1) {
					dataString += "·";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "µ";
		int[] maxBonuses = this.getMaxBonuses();
		if(maxBonuses != null && maxBonuses.length > 0) {
			for(int i = 0; i < maxBonuses.length; i++) {
				dataString += maxBonuses[i];
				if(i < maxBonuses.length - 1) {
					dataString += "·";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "µ" + this.getBaseValueAdd();
		return dataString;
	}

	/**
	 * parseBytes(byte[],boolean) Reads an array of bytes and translates the data into a GameItemModifier
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameItemModifier contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameItemModifier containing the data parsed from the byte array
	 */
	public static GameItemModifier parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] modifierData = s.split("µ");
		GameItemModifier modifier = null;
		if(modifierData != null && modifierData.length == 12 && modifierData[0].equals("GameItemModifier")) {//GameAsset(2) + GameItemModifier(10)
			String[] bonusCategories = modifierData[7].split("·");
			String[] bonusTypes = modifierData[8].split("·");
			String[] minBonusStrings = modifierData[9].split("·");
			String[] maxBonusStrings = modifierData[10].split("·");
			int[] minBonuses = new int[minBonusStrings.length];
			int[] maxBonuses = new int[maxBonusStrings.length];
			for(int i = 0; i < minBonuses.length; i++) {
				minBonuses[i] = Integer.parseInt(minBonusStrings[i]);
			}
			for(int i = 0; i < maxBonuses.length; i++) {
				maxBonuses[i] = Integer.parseInt(maxBonusStrings[i]);
			}
			modifier = new GameItemModifier();
			modifier.setAssetType(modifierData[0]);//assetType
			modifier.setUid(modifierData[1]);//uid
			modifier.setModifierType(modifierData[2]);//modifierType
			modifier.setName(modifierData[3]);//name
			modifier.setItemType(modifierData[4]);//itemType
			modifier.setAttackType(Integer.parseInt(modifierData[5]));//attackType
			modifier.setDamageType(Integer.parseInt(modifierData[6]));//damageType
			modifier.setBonusCategories(bonusCategories);//bonusCategories
			modifier.setBonusTypes(bonusTypes);//bonusTypes
			modifier.setMinBonuses(minBonuses);//minBonuses
			modifier.setMaxBonuses(maxBonuses);//maxBonuses
			modifier.setBaseValueAdd(Integer.parseInt(modifierData[11]));//baseValueAdd
			if(isTemplate) {
				GameItemModifier.add(modifier);
			}
		}
		return modifier;
	}
}