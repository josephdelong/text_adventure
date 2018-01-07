package game;

import java.util.HashMap;
import java.util.Map;

/**
 * GameItemPrefix A class that represents a Prefix applied to an Item in the Game
 * @author Joseph DeLong
 */
public class GameItemPrefix {
	protected static Map<String,GameItemPrefix> prefixes;
	
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
	 * GameItemPrefix(String,String,int,int,String[],String[],double[],double[],double) 
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
	protected GameItemPrefix(String name, String itemType, int attackType, int damageType, String[] bonusCategories, String[] bonusTypes, double[] minBonuses, double[] maxBonuses, double baseValueAdd) {
		this.name = name;
		this.itemType = itemType;
		this.attackType = attackType;
		this.damageType = damageType;
		this.bonusCategories = bonusCategories;
		this.bonusTypes = bonusTypes;
		this.minBonuses = minBonuses;
		this.maxBonuses = maxBonuses;
		this.baseValueAdd = baseValueAdd;
		GameItemPrefix.add(this);
	}

	/**
	 * add(GameItemPrefix) Adds the specified GameItemPrefix to the available Prefixes in the Game, if not already present
	 * @param prefix The GameItemPrefix to add
	 */
	protected static void add(GameItemPrefix prefix) {
		if(prefixes == null) {
			prefixes = new HashMap<String,GameItemPrefix>();
		}
		if(!prefixes.containsKey(prefix.name)) {
			prefixes.put(prefix.name.toLowerCase(),prefix);
		}
	}

	/**
	 * getBytes() Converts the current GameItemPrefix into a byte[] so it can be written to disk
	 * @return byte[] containing the data of this GameItemPrefix
	 */
	protected String getByteString() {
		String returnString = this.name + "�" + this.itemType + "�" + this.attackType + "�" + this.damageType + "�";
		if(this.bonusCategories != null && this.bonusCategories.length > 0) {
			for(int i = 0; i < this.bonusCategories.length; i++) {
				returnString += this.bonusCategories[i];
				if(i < this.bonusCategories.length - 1) {
					returnString += "�";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "�";
		if(this.bonusTypes != null && this.bonusTypes.length > 0) {
			for(int i = 0; i < this.bonusTypes.length; i++) {
				returnString += this.bonusTypes[i];
				if(i < this.bonusTypes.length - 1) {
					returnString += "�";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "�";
		if(this.minBonuses != null && this.minBonuses.length > 0) {
			for(int i = 0; i < this.minBonuses.length; i++) {
				returnString += this.minBonuses[i];
				if(i < this.minBonuses.length - 1) {
					returnString += "�";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "�";
		if(this.maxBonuses != null && this.maxBonuses.length > 0) {
			for(int i = 0; i < this.maxBonuses.length; i++) {
				returnString += this.maxBonuses[i];
				if(i < this.maxBonuses.length - 1) {
					returnString += "�";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "�" + this.baseValueAdd;
		return returnString;
	}

	/**
	 * getBytes() Returns a representation of the Prefix in an array of bytes
	 * @return
	 */
	protected byte[] getBytes() {
		String returnString = this.getByteString();
		return returnString.getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameItemPrefix
	 * @param bytes The byte array to read
	 * @return GameItemPrefix containing the data parsed from the byte array
	 */
	protected static GameItemPrefix parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] prefixData = s.split("�");
		GameItemPrefix prefix = null;
		if(prefixData.length < 9) {
			return prefix;
		}
		String[] bonusCategories = prefixData[4].split("�");
		String[] bonusTypes = prefixData[5].split("�");
		String[] minBonusStrings = prefixData[6].split("�");
		String[] maxBonusStrings = prefixData[7].split("�");
		double[] minBonuses = new double[minBonusStrings.length];
		double[] maxBonuses = new double[maxBonusStrings.length];
		for(int i = 0; i < minBonuses.length; i++) {
			minBonuses[i] = Double.parseDouble(minBonusStrings[i]);
		}
		for(int i = 0; i < maxBonuses.length; i++) {
			maxBonuses[i] = Double.parseDouble(maxBonusStrings[i]);
		}
		prefix = new GameItemPrefix(
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
		return prefix;
	}
}