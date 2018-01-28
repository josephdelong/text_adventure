package game.asset;

/**
 * GameAsset Class that represents an Asset in the Game. This is the base class for all instance classes used within a running instance of the Game.
 * @author Joseph DeLong
 */
public abstract class GameAsset {
	private String assetType;
	private String uid;

	public static final String assetType_ACTOR = "GameActor";
	public static final String assetType_ACTOR_ENEMY = "GameEnemy";
	public static final String assetType_ACTOR_NPC = "GameNpc";
	public static final String assetType_ACTOR_PLAYER = "GamePlayer";
	public static final String assetType_EFFECT_CHANCE = "GameChanceEffect";
	public static final String assetType_EFFECT_ENVIRONMENT = "GameEnvironmentEffect";
	public static final String assetType_EFFECT_SPELL = "GameSpellEffect";
	public static final String assetType_EFFECT_TRAP = "GameTrapEffect";
	public static final String assetType_ITEM = "GameItem";
	public static final String assetType_ITEM_USE = "GameItemUse";
	public static final String assetType_ITEM_CONSUMABLE = "GameItemConsumable";
	public static final String assetType_ITEM_KEY = "GameKey";
	public static final String assetType_ITEM_EQUIP = "GameItemEquip";
	public static final String assetType_ITEM_WIELD = "GameItemWield";
	public static final String assetType_ITEM_WORN = "GameItemWorn";
	public static final String assetType_ITEM_MODIFIER = "GameItemModifier";
	public static final String assetType_OBJECT = "GameObject";
	public static final String assetType_OBJECT_ACTIVATOR = "GameActivator";
	public static final String assetType_OBJECT_CONTAINER = "GameContainer";
	public static final String assetType_OBJECT_PORTAL = "GamePortal";
	public static final String assetType_ROOM = "GameRoom";
	public static final String assetType_ROOM_VOLUME = "GameVolume";
	public static final String assetType_SPELL = "GameSpell";

	public String getAssetType() {return assetType;}
	public String getUid() {return uid;}

	public void setAssetType(String assetType) {this.assetType = assetType;}
	public void setUid(String uid) {this.uid = uid;}

	/**
	 * generateUid(String) Generates a (unique) identifier for an INSTANCE of GameAsset, based on the passed-in String
	 * @param assetType String representation of the GameAsset type
	 * @return String representing a (unique) identifier for an INSTANCE of GameAsset
	 */
	public static String generateUid(String assetType) {
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		String randString = "";
		int numChars = Double.valueOf(Math.random() * 10).intValue() + 1;
		for(int i = 0; i < numChars; i++) {
			randString += chars[Double.valueOf(Math.random() * chars.length).intValue()];
		}
		return assetType + "=" + randString + "-" + Long.toString(System.currentTimeMillis());
	}

	/* getByteString()
	¦ = separator between RESOURCE File entries
	$ = separator between STATE fields
	€ = separator between list entries of ROOMs
	ƒ = separator between ROOM fields
	± = separator between list entries of VOLUMEs
	° = separator between VOLUME fields
	‡ = separator between list entries of OBJECTs/ENEMYs
	§ = separator between OBJECT/ENEMY fields
	¶ = separator between list entries of ITEMs/SPELLs
	| = separator between ITEM/SPELL fields
	ß = separator between list entries of PREFIX/SUFFIX
	µ = separator between PREFIX/SUFFIX fields
	· = separator between PREFIX/SUFFIX array members
 */
}
