package game;

import java.util.HashMap;
import java.util.Map;

/**
 * GameItemSuffix A class that represents a Suffix applied to an Item in the Game
 * @author Joseph DeLong
 */
public class GameItemSuffix {
	protected static Map<String,GameItemSuffix> suffixes;
	
	public String name;
	public String itemType;
	public int attackType;
	public int damageType;
	public String[] bonusCategories;
	public String[] bonusTypes;
	public double[] minBonuses;
	public double[] maxBonuses;
	public double baseValueAdd;

	/**
	 * GameItemSuffix(String,String,int,int,String[],String[],double[],double[],double)
	 * @param name
	 * @param itemType
	 * @param attackType
	 * @param damageType
	 * @param bonusCategories
	 * @param bonusTypes
	 * @param minBonuses
	 * @param maxBonuses
	 * @param baseValueAdd
	 */
	protected GameItemSuffix(String name, String itemType, int attackType, int damageType, String[] bonusCategories, String[] bonusTypes, double[] minBonuses, double[] maxBonuses, double baseValueAdd) {
		this.name = name;
		this.itemType = itemType;
		this.attackType = attackType;
		this.damageType = damageType;
		this.bonusCategories = bonusCategories;
		this.bonusTypes = bonusTypes;
		this.minBonuses = minBonuses;
		this.maxBonuses = maxBonuses;
		this.baseValueAdd = baseValueAdd;
		GameItemSuffix.add(this);
	}

	/**
	 * add(GameItemSuffix) Adds the specified GameItemSuffix to the available Suffixes in the Game, if not already sufsent
	 * @param suffix The GameItemSuffix to add
	 */
	protected static void add(GameItemSuffix suffix) {
		if(suffixes == null) {
			suffixes = new HashMap<String,GameItemSuffix>();
		}
		if(!suffixes.containsKey(suffix.name)) {
			suffixes.put(suffix.name.toLowerCase(),suffix);
		}
	}

	/**
	 * getBytes() Converts the current GameItemSuffix into a byte[] so it can be written to disk
	 * @return byte[] containing the data of this GameItemSuffix
	 */
	protected String getByteString() {
		String returnString = this.name + "µ" + this.itemType + "µ" + this.attackType + "µ" + this.damageType + "µ";
		if(this.bonusCategories != null && this.bonusCategories.length > 0) {
			for(int i = 0; i < this.bonusCategories.length; i++) {
				returnString += this.bonusCategories[i];
				if(i < this.bonusCategories.length - 1) {
					returnString += "·";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "µ";
		if(this.bonusTypes != null && this.bonusTypes.length > 0) {
			for(int i = 0; i < this.bonusTypes.length; i++) {
				returnString += this.bonusTypes[i];
				if(i < this.bonusTypes.length - 1) {
					returnString += "·";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "µ";
		if(this.minBonuses != null && this.minBonuses.length > 0) {
			for(int i = 0; i < this.minBonuses.length; i++) {
				returnString += this.minBonuses[i];
				if(i < this.minBonuses.length - 1) {
					returnString += "·";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "µ";
		if(this.maxBonuses != null && this.maxBonuses.length > 0) {
			for(int i = 0; i < this.maxBonuses.length; i++) {
				returnString += this.maxBonuses[i];
				if(i < this.maxBonuses.length - 1) {
					returnString += "·";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "µ" + this.baseValueAdd;
		return returnString;
	}

	/**
	 * getBytes() Returns a representation of the Suffix in an array of bytes
	 * @return
	 */
	protected byte[] getBytes() {
		String returnString = this.getByteString();
		return returnString.getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameItemSuffix
	 * @param bytes The byte array to read
	 * @return GameItemSuffix containing the data parsed from the byte array
	 */
	protected static GameItemSuffix parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] prefixData = s.split("µ");
		GameItemSuffix suffix = null;
		if(prefixData.length < 9) {
			return suffix;
		}
		String[] bonusCategories = prefixData[4].split("·");
		String[] bonusTypes = prefixData[5].split("·");
		String[] minBonusStrings = prefixData[6].split("·");
		String[] maxBonusStrings = prefixData[7].split("·");
		double[] minBonuses = new double[minBonusStrings.length];
		double[] maxBonuses = new double[maxBonusStrings.length];
		for(int i = 0; i < minBonuses.length; i++) {
			minBonuses[i] = Double.parseDouble(minBonusStrings[i]);
		}
		for(int i = 0; i < maxBonuses.length; i++) {
			maxBonuses[i] = Double.parseDouble(maxBonusStrings[i]);
		}
		suffix = new GameItemSuffix(
			prefixData[0],//name
			prefixData[1],//itemType
			Integer.parseInt(prefixData[2]),//attackType
			Integer.parseInt(prefixData[3]),//damageType
			bonusCategories,//bonusCategories
			bonusTypes,//bonusTypes
			minBonuses,//minBonuses
			maxBonuses,//maxBonuses
			Double.parseDouble(prefixData[8])//baseValueAdd
		);
		return suffix;
	}
}