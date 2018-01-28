package game.asset.spell;

import game.asset.GameAsset;
import game.asset.effect.GameEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameSpell A class that represents a Spell in the Game
 * @author Joseph DeLong
 */
public class GameSpell extends GameAsset {
	private static Map<String,GameSpell> spellTemplates;

	private String name;
	private String description;
	private int level;
	private int spCost;
	private boolean isFocus;
	private int focusTime;
	private List<GameEffect> effects;
	
	public static final String damage_FIRE = "FIRE";
	public static final String damage_COLD = "COLD";
	public static final String damage_ELECTRIC = "ELECTRIC";
	public static final String damage_FORCE = "FORCE";
	public static final String damage_PSYCHIC = "PSYCHIC";
	public static final String damage_CRUSHING = "CRUSHING";
	public static final String damage_HEALING = "HEALING";

	/**
	 * GameSpell() Constructor used for TEMPLATE CREATION MODE
	 */
	public GameSpell() {
		this.setAssetType(GameAsset.assetType_SPELL);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setName("");
		this.setDescription("");
		this.setLevel(0);
		this.setSpCost(0);
		this.setIsFocus(false);
		this.setFocusTime(0);
		this.setEffects(null);
	}

	/**
	 * GameSpell(String,String,String,String,int,int,boolean,int,List<GameEffect>) Constructs a new GameSpell TEMPLATE with the specified parameters
	 */
	public GameSpell(String assetType, String uid, String name, String description, int level, int spCost, boolean isFocus, int focusTime, List<GameEffect> effects) {
		this.setAssetType(assetType);
		this.setUid(uid);

		this.setName(name);
		this.setDescription(description);
		this.setLevel(level);
		this.setSpCost(spCost);
		this.setIsFocus(isFocus);
		this.setFocusTime(focusTime);
		if(effects != null && !effects.isEmpty()) {
			this.setEffects(effects);
		} else {
			this.setEffects(new ArrayList<GameEffect>());
		}
	}

	/**
	 * GameSpell(GameSpell) Given a base GameSpell, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the template's values directly, but are still a value-for-value direct copy of the template.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameSpell.lookup(spellName)</b></code> in order to create in-game
	 * instances of the Spell Template (stored in <code>GameSpell.spellTemplates</code>). This will prevent the Instances from overwriting the
	 * Templates (which is undesired).
	 * @param spellTemplate The GameSpell TEMPLATE to make an INSTANCE copy of
	 */
	public GameSpell(GameSpell spellTemplate) {
		this.setAssetType(GameAsset.assetType_SPELL);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setName(new String(spellTemplate.getName()));
		this.setDescription(new String(spellTemplate.getDescription()));
		this.setLevel(Integer.valueOf(spellTemplate.getLevel()));
		this.setSpCost(Integer.valueOf(spellTemplate.getSpCost()));
		this.setIsFocus(Boolean.valueOf(spellTemplate.isFocus()));
		this.setFocusTime(Integer.valueOf(spellTemplate.getFocusTime()));
		List<GameEffect> templateEffects = spellTemplate.getEffects();
		List<GameEffect> effects = new ArrayList<GameEffect>();
		for(GameEffect effect: templateEffects) {
			effects.add(new GameEffect(effect));
		}
		this.setEffects(effects);
	}

	public static Map<String,GameSpell> getSpellTemplates() {return GameSpell.spellTemplates;}

	public String getName() {return this.name;}
	public String getDescription() {return this.description;}
	public int getLevel() {return this.level;}
	public int getSpCost() {return this.spCost;}
	public boolean isFocus() {return this.isFocus;}
	public int getFocusTime() {return this.focusTime;}
	public List<GameEffect> getEffects() {return this.effects;}

	public void setName(String name) {this.name = name;}
	public void setDescription(String description) {this.description = description;}
	public void setLevel(int level) {this.level = level;}
	public void setSpCost(int spCost) {this.spCost = spCost;}
	public void setIsFocus(boolean isFocus) {this.isFocus = isFocus;}
	public void setFocusTime(int focusTime) {this.focusTime = focusTime;}
	public void setEffects(List<GameEffect> effects) {this.effects = effects;}

	/**
	 * add(GameSpell) Adds the specified GameEpell to the available Spells in the Game, if not already present
	 * @param spell The GameItem to add
	 */
	public static void add(GameSpell spell) {
		if(spellTemplates == null) {
			spellTemplates = new HashMap<String,GameSpell>();
		}
		if(!spellTemplates.containsKey(spell.name)) {
			spellTemplates.put(spell.name.toLowerCase(),spell);
		}
	}

	/**
	 * lookup(String) Returns a Spell by its name
	 * @param spellName Name of the Spell to lookup
	 * @return GameSpell of the specified name, if found
	 */
	public static GameSpell lookup(String spellName) {
		if(spellTemplates == null || spellTemplates.isEmpty()) {
			return null;
		}
		return spellTemplates.get(spellName.toLowerCase());
	}

	/**
	 * calculateDamage(String) Calculates the total DAMAGE of the GameSpell when cast (before any modifiers)
	 * @param spellName The name of the GameSpell that is being cast
	 * @return The total DAMAGE casting this instance of the GameSpell inflicts
	 */
	public static int calculateDamage(String spellName) {
		int damage = 0;
		GameSpell spellToCast = GameSpell.lookup(spellName);
		if(spellToCast != null) {
			List<GameEffect> effects = spellToCast.getEffects();
			for(int i = 0; i < effects.size(); i++) {
				damage += effects.get(i).calculateDamage();
			}
		}
		return damage;
	}

	/**
	 * Returns a String representation of the Spell & its properties
	 * @return
	 */
	public String getByteString() {
		String dataString = this.getAssetType() + "|" + this.getUid() + "|" + this.getName() + "|" + this.getDescription() + "|" + this.getLevel() 
				+ "|" + this.getSpCost() + "|" + this.isFocus() + "|" + this.getFocusTime() + "|";
		List<GameEffect> effects = this.getEffects();
		for(int i = 0; i < effects.size(); i++) {
			dataString += effects.get(i).getByteString();
			if(i < effects.size() - 1) {
				dataString += "ß";
			}
		}
		return dataString;
	}

	/**
	 * getBytes() Returns a representation of the Spell in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		String returnString = this.getByteString();
		return returnString.getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameSpell
	 * @param bytes The byte array to read\
	 * @param isTemplate Whether the data of GameSpell contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameSpell containing the data parsed from the byte array
	 */
	public static GameSpell parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] spellData = s.split("\\|");
		if(spellData.length != 9) {
			return null;
		}
		String[] effectsData = spellData[8].split("ß");
		List<GameEffect> effects = new ArrayList<GameEffect>();
		for(String effectData: effectsData) {
			effects.add(new GameEffect(GameEffect.parseBytes(effectData.getBytes(),isTemplate)));
		}
		GameSpell spell = new GameSpell();
		spell.setAssetType(spellData[0]);//assetType
		spell.setUid(spellData[1]);//uid
		spell.setName(spellData[2]);//name
		spell.setDescription(spellData[3]);//description
		spell.setLevel(Integer.parseInt(spellData[4]));//level
		spell.setSpCost(Integer.parseInt(spellData[5]));//spCost
		spell.setIsFocus(Boolean.parseBoolean(spellData[6]));//isFocus
		spell.setFocusTime(Integer.parseInt(spellData[7]));//focusTime
		spell.setEffects(effects);
		return spell;
	}
}
