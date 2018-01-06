package game;

import java.util.HashMap;
import java.util.Map;

/**
 * GameItem A class that represents a Spell in the Game
 * @author Joseph DeLong
 */
public class GameSpell {
	protected static Map<String,GameSpell> spells;

	protected String name;
	protected int spellLevel;
	protected int spellPointCost;
	protected String description;
	protected int damageDie;
	protected int numDie;
	protected int bonusDamage;
	protected String damageType;
	
	//TODO: implement onCast(int attackRoll)?

	protected static final String damage_FIRE = "FIRE";
	protected static final String damage_COLD = "COLD";
	protected static final String damage_ELECTRIC = "ELECTRIC";
	protected static final String damage_FORCE = "FORCE";
	protected static final String damage_PSYCHIC = "PSYCHIC";
	protected static final String damage_CRUSHING = "CRUSHING";
	protected static final String damage_HEALING = "HEALING";

	/**
	 * GameSpell() Constructor used for CREATION MODE
	 */
	protected GameSpell() {
		this.name = "";
		this.spellLevel = 0;
		this.spellPointCost = 0;
		this.description = "";
		this.damageDie = 0;
		this.numDie = 0;
		this.bonusDamage = 0;
		this.damageType = null;
	}

	/**
	 * GameSpell(String,int,int,String,int,int,int,String) Constructs a new instance of GameSpell with the specified parameters
	 * @param name The Name of the Spell
	 * @param spellLevel The minimum Level the Player must be at to cast this Spell
	 * @param spellPointCost The number of Spell Points required to cast this Spell
	 * @param description The flavor text of the Spell
	 * @param damageDie When the Spell is Cast, a random number between 1 and damageDie will determine the damage inflicted for each die rolled
	 * @param numDie The number of Dice that are rolled for damage when the Spell is cast
	 * @param bonusDamage Any damage applied after the damage dice are rolled
	 * @param damageType The Type of damage this Spell inflicts
	 */
	protected GameSpell(String name, int spellLevel, int spellPointCost, String description, int damageDie, int numDie, int bonusDamage, String damageType) {
		this.name = name;
		this.spellLevel = spellLevel;
		this.spellPointCost = spellPointCost;
		this.description = description;
		this.damageDie = damageDie;
		this.numDie = numDie;
		this.bonusDamage = bonusDamage;
		this.damageType = damageType;
		GameSpell.add(this);
	}

	/**
	 * add(GameSpell) Adds the specified GameEpell to the available Spells in the Game, if not already present
	 * @param spell The GameItem to add
	 */
	protected static void add(GameSpell spell) {
		if(spells == null) {
			spells = new HashMap<String,GameSpell>();
		}
		if(!spells.containsKey(spell.name)) {
			spells.put(spell.name.toLowerCase(),spell);
		}
	}

	/**
	 * lookup(String) Returns a Spell by its name
	 * @param spellName Name of the Spell to lookup
	 * @return GameSpell of the specified name, if found
	 */
	protected static GameSpell lookup(String spellName) {
		if(spells == null || spells.isEmpty()) {
			return null;
		}
		return spells.get(spellName);
	}

	/**
	 * calculateDamage(String) Calculates the total damage of the Spell when cast (before any modifiers)
	 * @param spellName The name of the Spell that is being cast
	 * @return The total Damage casting this instance of the Spell inflicts
	 */
	protected static int calculateDamage(String spellName) {
		int damage = 0;
		GameSpell spellToCast = GameSpell.lookup(spellName);
		if(spellToCast != null) {
			for(int i = 0; i < spellToCast.numDie; i++) {
				damage += Double.valueOf(Math.random() * spellToCast.damageDie).intValue() + 1;
			}
		}
		return damage;
	}

	/**
	 * Returns a String representation of the Spell & its properties
	 * @return
	 */
	protected String getByteString() {
		return this.name + "|" + this.spellLevel + "|" + this.spellPointCost + "|" + this.description + "|" + this.damageDie + "|" + this.numDie + "|" + this.bonusDamage + "|" + this.damageType;
	}

	/**
	 * getBytes() Returns a representation of the Spell in an array of bytes
	 * @return
	 */
	protected byte[] getBytes() {
		String returnString = this.getByteString();
		return returnString.getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameSpell
	 * @param bytes The byte array to read
	 * @return GameSpell containing the data parsed from the byte array
	 */
	protected static GameSpell parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] spellData = s.split("\\|");
		if(spellData.length != 8) {
			return null;
		}
		return new GameSpell(
				spellData[0],//name
				Integer.parseInt(spellData[1]),//spellLevel
				Integer.parseInt(spellData[2]),//spellPointCost
				spellData[3],//description
				Integer.parseInt(spellData[4]),//damageDie
				Integer.parseInt(spellData[5]),//numDie
				Integer.parseInt(spellData[6]),//bonusDamage
				spellData[7]//damageType
		);
	}
	
}
