package game.asset.effect;

import java.util.HashMap;
import java.util.Map;

import game.asset.GameAsset;

/**
 * GameEffect Class that represents an in-Game Effect, such as an environmental hazard or GameSpellEffect
 * @author Joseph DeLong
 */
public class GameEffect extends GameAsset {
	private static Map<String,GameEffect> effectTemplates;

	private String effectType;
	private String name;
	private String description;

	public static final String effectType_CHANCE = "chance";
	public static final String effectType_ENVIRONMENT = "environment";
	public static final String effectType_SPELL = "spell";
	public static final String effectType_TRAP = "trap";

	/**
	 * GameEffect() Constructor used in TEMPLATE creation
	 */
	public GameEffect() {
		super();
		this.setAssetType("GameEffect");
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setEffectType("null");
		this.setName("null");
		this.setDescription("null");
	}

	/**
	 * GameEffect(String,String,String,String,String)
	 */
	public GameEffect(String assetType, String uid, String effectType, String name, String description) {
		this.setAssetType(assetType);
		this.setUid(uid);

		this.setEffectType(effectType);
		this.setName(name);
		this.setDescription(description);
	}

	/**
	 * GameEffect(GameEffect) Given a base GameEffect, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the template's values directly, but are still a value-for-value direct copy of the template.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameEffect.lookupTemplate(effectName)</b></code> in order to create in-game
	 * instances of the Item Template (stored in <code>GameEffect.effectTemplates</code>). This will prevent the Instances from overwriting the
	 * Templates (which is undesired).
	 * @param itemTemplate The GameEffect TEMPLATE to make an INSTANCE copy of
	 */
	public GameEffect(GameEffect effectTemplate) {
		this.setAssetType("GameEffect");
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setEffectType(new String(effectTemplate.getEffectType()));
		this.setName(new String(effectTemplate.getName()));
		this.setDescription(new String(effectTemplate.getDescription()));
	}

	public static Map<String, GameEffect> getEffectTemplates() {return effectTemplates;}

	public String getEffectType() {return this.effectType;}
	public String getName() {return this.name;}
	public String getDescription() {return this.description;}

	public void setEffectType(String effectType) {this.effectType = effectType;}
	public void setName(String name) {this.name = name;}
	public void setDescription(String description) {this.description = description;}

	/**
	 * calculateDamage() Calculates the total DAMAGE of the GameEffect when cast (before any modifiers)
	 * @return The total DAMAGE casting this instance of the GameEffect inflicts
	 */
	public int calculateDamage() {return 0;} /*{
		int damage = 0;
		for(int i = 0; i < this.getNumDie(); i++) {
			damage += Double.valueOf(Math.random() * this.getDamageDie()).intValue() + 1;
		}
		return damage + this.getBonusDamage();
	}*/

	/**
	 * add(GameEffect) Adds the specified GameEffect TEMPLATE to the available Item TEMPLATEs in the Game, if not already present
	 * @param effect The GameEffect TEMPLATE to add
	 */
	public static void add(GameEffect effect) {
		if(effectTemplates == null) {
			effectTemplates = new HashMap<String,GameEffect>();
		}
		if(!effectTemplates.containsKey(effect.name)) {
			effectTemplates.put(effect.name.toLowerCase(),effect);
		}
	}

	/**
	 * lookupTemplate(String) Returns a GameEffect TEMPLATE by its name
	 * @param effectName Name of the GameEffect TEMPLATE to lookup
	 * @return GameEffect TEMPLATE of the specified name, if found
	 */
	public static GameEffect lookupTemplate(String effectName) {
		if(effectTemplates == null || effectTemplates.isEmpty()) {
			return null;
		}
		return effectTemplates.get(effectName.toLowerCase());
	}

	/**
	 * getByteString() Converts the current GameEffect into a String so it can be written to disk
	 * @return String containing the data of this GameEffect
	 */
	public String getByteString() {
		String dataString = this.getAssetType() + "|" + this.getUid() + "|" + this.getEffectType() + "|" + this.getName() + "|" + this.getDescription();
		return dataString;
	}

	/**
	 * getBytes() Returns a representation of the GameEffect in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameEffect
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameEffect contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameEffect containing the data parsed from the byte array
	 */
	public static GameEffect parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] effectData = s.split("\\|");
		GameEffect effect = null;
		if(effectData != null && effectData.length == 5 && effectData[0].equals("GameEffect")) {//GameAsset(2) + GameEffect(3)
			effect = new GameEffect();
			effect.setAssetType(effectData[0]);//assetType
			effect.setUid(effectData[1]);//uid
			effect.setEffectType(effectData[2]);//effectType
			effect.setName(effectData[3]);//name
			effect.setDescription(effectData[4]);//description
		}
		return effect;
	}
}
