package game.asset.actor;

import game.asset.GameAsset;
import game.asset.GameDiplomacy;
import game.asset.item.GameItem;
import game.asset.spell.GameSpell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameEnemy Class that represents a Non-Playable Character, which is generally an Enemy of the Player
 * @author Joseph DeLong
 */
public class GameEnemy extends GameActor {
	private static Map<String,GameEnemy> enemyTemplates;
	
	private int xpValue;
	private boolean isHostile;

	/**
	 * GamePlayer() Constructor used for TEMPLATE CREATION MODE. Creates an empty GamePlayer TEMPLATE
	 */
	public GameEnemy() {
		this.setAssetType(GameAsset.assetType_ACTOR_ENEMY);//this.getClass().getName()
		this.setUid(GameAsset.generateUid(this.getAssetType()));
		
		this.setActorType(GameActor.actorType_ENEMY);
		this.setName("");
		this.setDescription("");
		this.setLevel(0);
		this.setActorRace("HUMAN");//GameActor.race_HUMAN
		this.setActorClass("FIGHTER");//GameActor.class_FIGHTER
		this.setAge(20);
		this.setGender("MALE");//GameActor.gender_MALE
		this.setHeight(60);//5' 10"
		this.setWeight(180);
		this.setEyeDesc("BROWN");//GameActor.eyeColor_BROWN
		this.setHairDesc("BLACK");//GameActor.hairColor_BLACK
		this.setSkinDesc("DARK BROWN");//GameActor.skinColor_DARK_BROWN
		this.setInitiative(0);
		this.setSpeed(6);//30ft of movement per round
		this.setSTR(10);
		this.setDEX(10);
		this.setCON(10);
		this.setINT(10);
		this.setWIS(10);
		this.setCHA(10);
		this.setSTR_mod(0);
		this.setDEX_mod(0);
		this.setCON_mod(0);
		this.setINT_mod(0);
		this.setWIS_mod(0);
		this.setCHA_mod(0);
		this.setFORT_save(1);
		this.setRFLX_save(1);
		this.setWILL_save(1);
		this.setHitDie(8);
		this.setSize(0);//SMALL=-1/MEDIUM=0/LARGE=1
		this.setCarryWeight(100);//10*STR
		this.setCurHp(10);
		this.setMaxHp(10);
		this.setCurSp(10);
		this.setMaxSp(10);
		this.setAttackBonusMelee(0);
		this.setAttackBonusRanged(0);
		this.setAttackBonusMagic(0);
		this.setArmorClass(10);
		this.setDamageReduction(0);
		this.setMagicResistance(0);
		this.setInventory(new ArrayList<GameItem>());
		this.setSpellBook(new ArrayList<GameSpell>());
		this.setLanguages(new ArrayList<String>());
		this.setDiplomacy(new ArrayList<GameDiplomacy>());
		this.setRoomIndex(0);
		this.setVolumeIndex(0);

		this.setEquipSlot_BACK(null);
		this.setEquipSlot_BELT(null);
		this.setEquipSlot_BOOTS(null);
		this.setEquipSlot_CUIRASS(null);
		this.setEquipSlot_GAUNTLETS(null);
		this.setEquipSlot_GREAVES(null);
		this.setEquipSlot_HELM(null);
		this.setEquipSlot_MAIN_HAND(null);
		this.setEquipSlot_OFF_HAND(null);
		this.setEquipSlot_TWO_HAND(null);
		this.setEquipSlot_PANTS(null);
		this.setEquipSlot_SHIRT(null);
		this.setEquipSlot_SHOES(null);
		this.setEquipSlot_RING(null);
		this.setEquipSlot_AMULET(null);
		this.setEquipSlot_EARRING(null);
		this.setEquipSlot_CLOAK(null);

		this.setXpValue(0);
		this.setIsHostile(true);
	}

	/**
	 * GameEnemy(ALL_INPUT_PARAMS)
	 */
	public GameEnemy(
			String assetType, String uid, String actorType, String name, String description, int level, String actorRace, String actorClass, int age, String gender, 
			int height, int weight, String eyeDesc, String hairDesc, String skinDesc, int initiative, int speed, int STR, int DEX, int CON, int INT,int WIS, int CHA, 
			int STR_mod, int DEX_mod, int CON_mod, int INT_mod, int WIS_mod, int CHA_mod, int FORT_save, int RFLX_save, int WILL_save, int hitDie, int size, 
			int carryWeight, int curHp, int maxHp, int curSp, int maxSp, int attackBonusMelee, int attackBonusRanged, int attackBonusMagic, int armorClass, 
			int damageReduction, int magicResistance, List<GameItem> inventory, List<GameSpell> spellBook, List<String> languages, 
			List<GameDiplomacy> diplomacy, int roomIndex, int volumeIndex, GameItem equipSlot_BACK, GameItem equipSlot_BELT, GameItem equipSlot_BOOTS, 
			GameItem equipSlot_CUIRASS, GameItem equipSlot_GAUNTLETS, GameItem equipSlot_GREAVES, GameItem equipSlot_HELM, GameItem equipSlot_MAIN_HAND, 
			GameItem equipSlot_OFF_HAND, GameItem equipSlot_TWO_HAND, GameItem equipSlot_PANTS, GameItem equipSlot_SHIRT, GameItem equipSlot_SHOES, 
			GameItem equipSlot_RING, GameItem equipSlot_AMULET, GameItem equipSlot_EARRING, GameItem equipSlot_CLOAK, int xpValue, boolean isHostile
		) {
		this.setAssetType(assetType);//this.getClass().getName()
		this.setUid(uid);
		
		this.setActorType(actorType);
		this.setName(name);
		this.setDescription(description);
		this.setLevel(level);
		this.setActorRace(actorRace);//GameActor.race_HUMAN
		this.setActorClass(actorClass);//GameActor.class_FIGHTER
		this.setAge(age);
		this.setGender(gender);//GameActor.gender_MALE
		this.setHeight(height);//5' 10"
		this.setWeight(weight);
		this.setEyeDesc(eyeDesc);//GameActor.eyeColor_BROWN
		this.setHairDesc(hairDesc);//GameActor.hairColor_BLACK
		this.setSkinDesc(skinDesc);//GameActor.skinColor_DARK_BROWN
		this.setInitiative(initiative);
		this.setSpeed(speed);//30ft of movement per round
		this.setSTR(STR);
		this.setDEX(DEX);
		this.setCON(CON);
		this.setINT(INT);
		this.setWIS(WIS);
		this.setCHA(CHA);
		this.setSTR_mod(STR_mod);
		this.setDEX_mod(DEX_mod);
		this.setCON_mod(CON_mod);
		this.setINT_mod(INT_mod);
		this.setWIS_mod(WIS_mod);
		this.setCHA_mod(CHA_mod);
		this.setFORT_save(FORT_save);
		this.setRFLX_save(RFLX_save);
		this.setWILL_save(WILL_save);
		this.setHitDie(hitDie);
		this.setSize(size);//SMALL=-1/MEDIUM=0/LARGE=1
		this.setCarryWeight(carryWeight);//10*STR
		this.setCurHp(curHp);
		this.setMaxHp(maxHp);
		this.setCurSp(curSp);
		this.setMaxSp(maxSp);
		this.setAttackBonusMelee(attackBonusMelee);
		this.setAttackBonusRanged(attackBonusRanged);
		this.setAttackBonusMagic(attackBonusMagic);
		this.setArmorClass(armorClass);
		this.setDamageReduction(damageReduction);
		this.setMagicResistance(magicResistance);
		this.setInventory(inventory);
		this.setSpellBook(spellBook);
		this.setLanguages(languages);
		this.setDiplomacy(diplomacy);
		this.setRoomIndex(roomIndex);
		this.setVolumeIndex(volumeIndex);

		this.setEquipSlot_BACK(equipSlot_BACK);
		this.setEquipSlot_BELT(equipSlot_BELT);
		this.setEquipSlot_BOOTS(equipSlot_BOOTS);
		this.setEquipSlot_CUIRASS(equipSlot_CUIRASS);
		this.setEquipSlot_GAUNTLETS(equipSlot_GAUNTLETS);
		this.setEquipSlot_GREAVES(equipSlot_GREAVES);
		this.setEquipSlot_HELM(equipSlot_HELM);
		this.setEquipSlot_MAIN_HAND(equipSlot_MAIN_HAND);
		this.setEquipSlot_OFF_HAND(equipSlot_OFF_HAND);
		this.setEquipSlot_TWO_HAND(equipSlot_TWO_HAND);
		this.setEquipSlot_PANTS(equipSlot_PANTS);
		this.setEquipSlot_SHIRT(equipSlot_SHIRT);
		this.setEquipSlot_SHOES(equipSlot_SHOES);
		this.setEquipSlot_RING(equipSlot_RING);
		this.setEquipSlot_AMULET(equipSlot_AMULET);
		this.setEquipSlot_EARRING(equipSlot_EARRING);
		this.setEquipSlot_CLOAK(equipSlot_CLOAK);

		this.setXpValue(xpValue);
		this.setIsHostile(isHostile);
	}

	/**
	 * GameEnemy(GameEnemy) Given a base GameEnemy TEMPLATE, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE data.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameEnemy.lookupTemplate(enemyName)</b></code> in order to create in-game
	 * instances of the GameEnemy TEMPLATE (stored in <code>GameEnemy.enemyTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param enemyTemplate The GameEnemy TEMPLATE to make an INSTANCE copy of
	 */
	public GameEnemy(GameEnemy enemyTemplate) {
		this.setAssetType(GameAsset.assetType_ACTOR_ENEMY);//this.getClass().getName()
		this.setUid(GameAsset.generateUid(this.getAssetType()));//create a new UID for this INSTANCE
		
		this.setActorType(GameActor.actorType_ENEMY);
		this.setName(new String(enemyTemplate.getName()));
		this.setDescription(new String(enemyTemplate.getDescription()));
		this.setLevel(Integer.valueOf(enemyTemplate.getLevel()));
		this.setActorRace(new String(enemyTemplate.getActorRace()));//GameActor.race_HUMAN
		this.setActorClass(new String(enemyTemplate.getActorClass()));//GameActor.class_FIGHTER
		this.setAge(Integer.valueOf(enemyTemplate.getAge()));
		this.setGender(new String(enemyTemplate.getGender()));//GameActor.gender_MALE
		this.setHeight(Integer.valueOf(enemyTemplate.getHeight()));//5' 10"
		this.setWeight(Integer.valueOf(enemyTemplate.getWeight()));
		this.setEyeDesc(new String(enemyTemplate.getEyeDesc()));//GameActor.eyeColor_BROWN
		this.setHairDesc(new String(enemyTemplate.getHairDesc()));//GameActor.hairColor_BLACK
		this.setSkinDesc(new String(enemyTemplate.getSkinDesc()));//GameActor.skinColor_DARK_BROWN
		this.setInitiative(Integer.valueOf(enemyTemplate.getInitiative()));
		this.setSpeed(Integer.valueOf(enemyTemplate.getSpeed()));//30ft of movement per round
		this.setSTR(Integer.valueOf(enemyTemplate.getSTR()));
		this.setDEX(Integer.valueOf(enemyTemplate.getDEX()));
		this.setCON(Integer.valueOf(enemyTemplate.getCON()));
		this.setINT(Integer.valueOf(enemyTemplate.getINT()));
		this.setWIS(Integer.valueOf(enemyTemplate.getWIS()));
		this.setCHA(Integer.valueOf(enemyTemplate.getCHA()));
		this.setSTR_mod(Integer.valueOf(enemyTemplate.getSTR_mod()));
		this.setDEX_mod(Integer.valueOf(enemyTemplate.getDEX_mod()));
		this.setCON_mod(Integer.valueOf(enemyTemplate.getCON_mod()));
		this.setINT_mod(Integer.valueOf(enemyTemplate.getINT_mod()));
		this.setWIS_mod(Integer.valueOf(enemyTemplate.getWIS_mod()));
		this.setCHA_mod(Integer.valueOf(enemyTemplate.getCHA_mod()));
		this.setFORT_save(Integer.valueOf(enemyTemplate.getFORT_save()));
		this.setRFLX_save(Integer.valueOf(enemyTemplate.getRFLX_save()));
		this.setWILL_save(Integer.valueOf(enemyTemplate.getWILL_save()));
		this.setHitDie(Integer.valueOf(enemyTemplate.getHitDie()));
		this.setSize(Integer.valueOf(enemyTemplate.getSize()));//SMALL=-1/MEDIUM=0/LARGE=1
		this.setCarryWeight(Integer.valueOf(enemyTemplate.getCarryWeight()));//10*STR
		this.setCurHp(Integer.valueOf(enemyTemplate.getCurHp()));
		this.setMaxHp(Integer.valueOf(enemyTemplate.getMaxHp()));
		this.setCurSp(Integer.valueOf(enemyTemplate.getCurSp()));
		this.setMaxSp(Integer.valueOf(enemyTemplate.getMaxSp()));
		this.setAttackBonusMelee(Integer.valueOf(enemyTemplate.getAttackBonusMelee()));
		this.setAttackBonusRanged(Integer.valueOf(enemyTemplate.getAttackBonusRanged()));
		this.setAttackBonusMagic(Integer.valueOf(enemyTemplate.getAttackBonusMagic()));
		this.setArmorClass(Integer.valueOf(enemyTemplate.getArmorClass()));
		this.setDamageReduction(Integer.valueOf(enemyTemplate.getDamageReduction()));
		this.setMagicResistance(Integer.valueOf(enemyTemplate.getMagicResistance()));
		List<GameItem> inventory = null;
		List<GameItem> templateInventory = enemyTemplate.getInventory();
		if(templateInventory != null && templateInventory.size() > 0) {
			inventory = new ArrayList<GameItem>();
			for(GameItem item: templateInventory) {
				inventory.add(new GameItem(item));
			}
		} else {
			inventory = new ArrayList<GameItem>();
		}
		this.setInventory(inventory);
		List<GameSpell> spellBook = null;
		List<GameSpell> templateSpellBook = enemyTemplate.getSpellBook();
		if(templateSpellBook != null && templateSpellBook.size() > 0) {
			spellBook = new ArrayList<GameSpell>();
			for(GameSpell spell: templateSpellBook) {
				spellBook.add(new GameSpell(spell));
			}
		} else {
			spellBook = new ArrayList<GameSpell>();
		}
		this.setSpellBook(spellBook);
		List<String> languages = null;
		List<String> templateLanguages = enemyTemplate.getLanguages();
		if(templateLanguages != null && templateLanguages.size() > 0) {
			languages = new ArrayList<String>();
			for(String s: templateLanguages) {
				languages.add(new String(s));
			}
		} else {
			languages = new ArrayList<String>();
		}
		this.setLanguages(languages);
		List<GameDiplomacy> diplomacy = null;
		List<GameDiplomacy> templateDiplomacy = enemyTemplate.getDiplomacy();
		if(templateDiplomacy != null && templateDiplomacy.size() > 0) {
			diplomacy = new ArrayList<GameDiplomacy>();
			for(GameDiplomacy d: templateDiplomacy) {
				diplomacy.add(new GameDiplomacy(d));
			}
		} else {
			diplomacy = new ArrayList<GameDiplomacy>();
		}
		this.setDiplomacy(diplomacy);
		this.setRoomIndex(Integer.valueOf(enemyTemplate.getRoomIndex()));
		this.setVolumeIndex(Integer.valueOf(enemyTemplate.getVolumeIndex()));

		this.setEquipSlot_BACK(new GameItem(enemyTemplate.getEquipSlot_BACK()));
		this.setEquipSlot_BELT(new GameItem(enemyTemplate.getEquipSlot_BELT()));
		this.setEquipSlot_BOOTS(new GameItem(enemyTemplate.getEquipSlot_BOOTS()));
		this.setEquipSlot_CUIRASS(new GameItem(enemyTemplate.getEquipSlot_CUIRASS()));
		this.setEquipSlot_GAUNTLETS(new GameItem(enemyTemplate.getEquipSlot_GAUNTLETS()));
		this.setEquipSlot_GREAVES(new GameItem(enemyTemplate.getEquipSlot_GREAVES()));
		this.setEquipSlot_HELM(new GameItem(enemyTemplate.getEquipSlot_HELM()));
		this.setEquipSlot_MAIN_HAND(new GameItem(enemyTemplate.getEquipSlot_MAIN_HAND()));
		this.setEquipSlot_OFF_HAND(new GameItem(enemyTemplate.getEquipSlot_OFF_HAND()));
		this.setEquipSlot_TWO_HAND(new GameItem(enemyTemplate.getEquipSlot_TWO_HAND()));
		this.setEquipSlot_PANTS(new GameItem(enemyTemplate.getEquipSlot_PANTS()));
		this.setEquipSlot_SHIRT(new GameItem(enemyTemplate.getEquipSlot_SHIRT()));
		this.setEquipSlot_SHOES(new GameItem(enemyTemplate.getEquipSlot_SHOES()));
		this.setEquipSlot_RING(new GameItem(enemyTemplate.getEquipSlot_RING()));
		this.setEquipSlot_AMULET(new GameItem(enemyTemplate.getEquipSlot_AMULET()));
		this.setEquipSlot_EARRING(new GameItem(enemyTemplate.getEquipSlot_EARRING()));
		this.setEquipSlot_CLOAK(new GameItem(enemyTemplate.getEquipSlot_CLOAK()));

		this.setXpValue(Integer.valueOf(enemyTemplate.getXpValue()));
		this.setIsHostile(Boolean.valueOf(enemyTemplate.isHostile()));
	}

	public static Map<String,GameEnemy> getEnemyTemplates() {return GameEnemy.enemyTemplates;}
	
	public int getXpValue() {return this.xpValue;}
	public boolean isHostile() {return this.isHostile;}

	public void setXpValue(int xpValue) {this.xpValue = xpValue;}
	public void setIsHostile(boolean isHostile) {this.isHostile = isHostile;}

	/**
	 * add(GameEnemy) Adds the specified GameEnemy to the List of all Enemy TEMPLATEs in the Game, if not already present
	 * @param enemy The GameEnemy to add
	 */
	public static void add(GameEnemy enemy) {
		if(enemyTemplates == null) {
			enemyTemplates = new HashMap<String,GameEnemy>();
		}
		if(!enemyTemplates.containsKey(enemy.getUid())) {
			enemyTemplates.put(enemy.getName().toLowerCase(),enemy);
		}
	}

	/**
	 * lookupTemplate(String) Look up the GameEnemy by its Name in the list of current GameEnemy TEMPLATEs
	 * @param enemyName The (unique) name of the GameEnemy TEMPLATE which is to be returned
	 * @return GameEnemy TEMPLATE specified by the (unique) name, if found in the current list of GameEnemy
	 */
	public static GameEnemy lookupTemplate(String enemyName) {
		if(enemyTemplates == null || enemyTemplates.isEmpty()) {
			return null;
		}
		return enemyTemplates.get(enemyName.toLowerCase());
	}

	/**
	 * getBytes() Returns a representation of the GameEnemy in an array of bytes
	 * @return byte[] containing the data of this GameEnemy
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * getByteString() Converts an instance of GameEnemy into a byte[] so it can be written to disk
	 * @return String containing the data of this GameEnemy
	 */
	public String getByteString() {
		String dataString = this.getAssetType() + "§" + this.getUid() + "§" + this.getActorType() + "§" + this.getName() + "§" + this.getDescription()
				+ "§" + this.getLevel()+ "§" + this.getActorRace()+ "§" + this.getActorClass()+ "§" + this.getAge()+ "§" + this.getGender()
				+ "§" + this.getHeight() + "§" + this.getWeight() + "§" + this.getEyeDesc() + "§" + this.getHairDesc() + "§" + this.getSkinDesc()
				+ "§" + this.getInitiative() + "§" + this.getSpeed() + "§" + this.getSTR() + "§" + this.getDEX() + "§" + this.getCON()
				+ "§" + this.getINT() + "§" + this.getWIS() + "§" + this.getCHA() + "§" + this.getSTR_mod() + "§" + this.getDEX_mod()
				+ "§" + this.getCON_mod() + "§" + this.getINT_mod() + "§" + this.getWIS_mod() + "§" + this.getCHA_mod() + "§" + this.getFORT_save()
				+ "§" + this.getRFLX_save() + "§" + this.getWILL_save() + "§" + this.getHitDie() + "§" + this.getSize() + "§" + this.getCarryWeight()
				+ "§" + this.getCurHp() + "§" + this.getMaxHp() + "§" + this.getCurSp() + "§" + this.getMaxSp() + "§" + this.getAttackBonusMelee()
				+ "§" + this.getAttackBonusRanged() + "§" + this.getAttackBonusMagic() + "§" + this.getArmorClass() + "§" + this.getDamageReduction()
				+ "§" + this.getMagicResistance();
		dataString += "§";
		List<GameItem> inventory = this.getInventory();
		if(inventory != null && inventory.size() > 0) {
			for(int i = 0; i < inventory.size(); i++) {
				dataString += inventory.get(i).getByteString();
				if(i < inventory.size() - 1) {
					dataString += "¶";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "§";
		List<GameSpell> spellBook = this.getSpellBook();
		if(spellBook != null && spellBook.size() > 0) {
			for(int i = 0; i < spellBook.size(); i++) {
				dataString += spellBook.get(i).getByteString();
				if(i < spellBook.size() - 1) {
					dataString += "¶";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "§";
		List<String> languages = this.getLanguages();
		if(languages != null && languages.size() > 0) {
			for(int i = 0; i < languages.size(); i++) {
				dataString += languages.get(i);
				if(i < languages.size() - 1) {
					dataString += "¶";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "§";
		List<GameDiplomacy> diplomacy = this.getDiplomacy();
		if(diplomacy != null && diplomacy.size() > 0) {
			for(int i = 0; i < diplomacy.size(); i++) {
				dataString += diplomacy.get(i).getByteString();
				if(i < diplomacy.size() - 1) {
					dataString += "¶";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "§" + this.getRoomIndex() + "§" + this.getVolumeIndex();

		dataString += "§" + this.getEquipSlot_BACK().getByteString();
		dataString += "§" + this.getEquipSlot_BELT().getByteString();
		dataString += "§" + this.getEquipSlot_BOOTS().getByteString();
		dataString += "§" + this.getEquipSlot_CUIRASS().getByteString();
		dataString += "§" + this.getEquipSlot_GAUNTLETS().getByteString();
		dataString += "§" + this.getEquipSlot_GREAVES().getByteString();
		dataString += "§" + this.getEquipSlot_HELM().getByteString();
		dataString += "§" + this.getEquipSlot_MAIN_HAND().getByteString();
		dataString += "§" + this.getEquipSlot_OFF_HAND().getByteString();
		dataString += "§" + this.getEquipSlot_TWO_HAND().getByteString();
		dataString += "§" + this.getEquipSlot_PANTS().getByteString();
		dataString += "§" + this.getEquipSlot_SHIRT().getByteString();
		dataString += "§" + this.getEquipSlot_SHOES().getByteString();
		dataString += "§" + this.getEquipSlot_RING().getByteString();
		dataString += "§" + this.getEquipSlot_AMULET().getByteString();
		dataString += "§" + this.getEquipSlot_EARRING().getByteString();
		dataString += "§" + this.getEquipSlot_CLOAK().getByteString();

		dataString += "§" + this.getXpValue() + "§" + this.isHostile();
		return dataString;
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameEnemy
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameEnemy contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameEnemy containing the data parsed from the byte array
	 */
	public static GameEnemy parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] enemyData = s.split("§");
		GameEnemy enemy = null;
		if(enemyData != null && enemyData.length == 70 && enemyData[0].equals("GameEnemy")) {//GameAsset(2)+GameActor(66)+GameNpc(2)
			String[] inventoryData = enemyData[45].split("¶");
			List<GameItem> inventory = new ArrayList<GameItem>();
			for(String itemData: inventoryData) {
				GameItem item = GameItem.parseBytes(itemData.getBytes(),false);
				if(item != null) {
					inventory.add(item);
				}
			}
			String[] spellBookData = enemyData[46].split("¶");
			List<GameSpell> spellBook = new ArrayList<GameSpell>();
			for(String spellData: spellBookData) {
				GameSpell spell = GameSpell.parseBytes(spellData.getBytes(),false);
				if(spell != null) {
					spellBook.add(spell);
				}
			}
			String[] languageData = enemyData[47].split("¶");
			List<String> languages = new ArrayList<String>();
			for(String language: languageData) {
				if(language != null && language.length() > 0) {
					languages.add(language);
				}
			}
			String[] diplomacyData = enemyData[48].split("¶");
			List<GameDiplomacy> diplomacy = new ArrayList<GameDiplomacy>();
			for(String disposition: diplomacyData) {
				GameDiplomacy diplomacyItem = GameDiplomacy.parseBytes(disposition.getBytes(),isTemplate);
				if(diplomacyItem != null) {
					diplomacy.add(diplomacyItem);
				}
			}
			enemy = new GameEnemy();
			enemy.setAssetType(enemyData[0]);//assetType
			enemy.setUid(enemyData[1]);//uid//userName + characterName
			enemy.setActorType(enemyData[2]);//actorType
			enemy.setName(enemyData[3]);//name
			enemy.setDescription(enemyData[4]);//description
			enemy.setLevel(Integer.parseInt(enemyData[5]));//level
			enemy.setActorRace(enemyData[6]);//actorRace
			enemy.setActorClass(enemyData[7]);//actorClass
			enemy.setAge(Integer.parseInt(enemyData[8]));//age
			enemy.setGender(enemyData[9]);//gender
			enemy.setHeight(Integer.parseInt(enemyData[10]));//height
			enemy.setWeight(Integer.parseInt(enemyData[11]));//weight
			enemy.setEyeDesc(enemyData[12]);//eyeDesc
			enemy.setHairDesc(enemyData[13]);//hairDesc
			enemy.setSkinDesc(enemyData[14]);//skinDesc
			enemy.setInitiative(Integer.parseInt(enemyData[15]));//initiative
			enemy.setSpeed(Integer.parseInt(enemyData[16]));//speed
			enemy.setSTR(Integer.parseInt(enemyData[17]));//STR
			enemy.setDEX(Integer.parseInt(enemyData[18]));//DEX
			enemy.setCON(Integer.parseInt(enemyData[19]));//CON
			enemy.setINT(Integer.parseInt(enemyData[20]));//INT
			enemy.setWIS(Integer.parseInt(enemyData[21]));//WIS
			enemy.setCHA(Integer.parseInt(enemyData[22]));//CHA
			enemy.setSTR_mod(Integer.parseInt(enemyData[23]));//STR_mod
			enemy.setDEX_mod(Integer.parseInt(enemyData[24]));//DEX_mod
			enemy.setCON_mod(Integer.parseInt(enemyData[25]));//CON_mod
			enemy.setINT_mod(Integer.parseInt(enemyData[26]));//INT_mod
			enemy.setWIS_mod(Integer.parseInt(enemyData[27]));//WIS_mod
			enemy.setCHA_mod(Integer.parseInt(enemyData[28]));//CHA_mod
			enemy.setFORT_save(Integer.parseInt(enemyData[29]));//FORT_save
			enemy.setRFLX_save(Integer.parseInt(enemyData[30]));//RFLX_save
			enemy.setWILL_save(Integer.parseInt(enemyData[31]));//WILL_save
			enemy.setHitDie(Integer.parseInt(enemyData[32]));//hitDie
			enemy.setSize(Integer.parseInt(enemyData[33]));//size
			enemy.setCarryWeight(Integer.parseInt(enemyData[34]));//carryWeight
			enemy.setCurHp(Integer.parseInt(enemyData[35]));//curHp
			enemy.setMaxHp(Integer.parseInt(enemyData[36]));//maxHp
			enemy.setCurSp(Integer.parseInt(enemyData[37]));//curSp
			enemy.setMaxSp(Integer.parseInt(enemyData[38]));//maxSp
			enemy.setAttackBonusMelee(Integer.parseInt(enemyData[39]));//attackBonusMelee
			enemy.setAttackBonusRanged(Integer.parseInt(enemyData[40]));//attackBonusRanged
			enemy.setAttackBonusMagic(Integer.parseInt(enemyData[41]));//attackBonusMagic
			enemy.setArmorClass(Integer.parseInt(enemyData[42]));//armorClass
			enemy.setDamageReduction(Integer.parseInt(enemyData[43]));//damageReduction
			enemy.setMagicResistance(Integer.parseInt(enemyData[44]));//magicResistance
			enemy.setInventory(inventory);//inventory
			enemy.setSpellBook(spellBook);//spellBook
			enemy.setLanguages(languages);//languages
			enemy.setDiplomacy(diplomacy);//diplomacy
			enemy.setRoomIndex(Integer.parseInt(enemyData[49]));//roomIndex
			enemy.setVolumeIndex(Integer.parseInt(enemyData[50]));//volumeIndex

			enemy.setEquipSlot_BACK(GameItem.parseBytes(enemyData[51].getBytes(),false));
			enemy.setEquipSlot_BELT(GameItem.parseBytes(enemyData[52].getBytes(),false));
			enemy.setEquipSlot_BOOTS(GameItem.parseBytes(enemyData[53].getBytes(),false));
			enemy.setEquipSlot_CUIRASS(GameItem.parseBytes(enemyData[54].getBytes(),false));
			enemy.setEquipSlot_GAUNTLETS(GameItem.parseBytes(enemyData[55].getBytes(),false));
			enemy.setEquipSlot_GREAVES(GameItem.parseBytes(enemyData[56].getBytes(),false));
			enemy.setEquipSlot_HELM(GameItem.parseBytes(enemyData[57].getBytes(),false));
			enemy.setEquipSlot_MAIN_HAND(GameItem.parseBytes(enemyData[58].getBytes(),false));
			enemy.setEquipSlot_OFF_HAND(GameItem.parseBytes(enemyData[59].getBytes(),false));
			enemy.setEquipSlot_TWO_HAND(GameItem.parseBytes(enemyData[60].getBytes(),false));
			enemy.setEquipSlot_PANTS(GameItem.parseBytes(enemyData[61].getBytes(),false));
			enemy.setEquipSlot_SHIRT(GameItem.parseBytes(enemyData[62].getBytes(),false));
			enemy.setEquipSlot_SHOES(GameItem.parseBytes(enemyData[63].getBytes(),false));
			enemy.setEquipSlot_RING(GameItem.parseBytes(enemyData[64].getBytes(),false));
			enemy.setEquipSlot_AMULET(GameItem.parseBytes(enemyData[65].getBytes(),false));
			enemy.setEquipSlot_EARRING(GameItem.parseBytes(enemyData[66].getBytes(),false));
			enemy.setEquipSlot_CLOAK(GameItem.parseBytes(enemyData[67].getBytes(),false));

			enemy.setXpValue(Integer.parseInt(enemyData[68]));//xpValue
			enemy.setIsHostile(Boolean.parseBoolean(enemyData[69]));//isHostile
			if(isTemplate) {
				GameEnemy.add(enemy);
			}
		}
		return enemy;
	}
}
