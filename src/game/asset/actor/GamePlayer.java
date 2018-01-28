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
 * GameNpc Class that represents a connected Player's Character
 * @author Joseph DeLong
 */
public class GamePlayer extends GameActor {
	private static Map<String,GamePlayer> allPlayers;
	
	private int curXp;
	private int deathSavingThrow_SUCCESS;
	private int deathSavingThrow_FAILURE;
	private int numDeaths;

	/**
	 * GamePlayer() Constructor used for TEMPLATE CREATION MODE. Creates an empty GamePlayer TEMPLATE
	 */
	public GamePlayer() {
		this.setAssetType(GameAsset.assetType_ACTOR_PLAYER);//this.getClass().getName()
		this.setUid("");//userName + characterName //GameAsset.generateUid(this)
		
		this.setActorType(GameActor.actorType_PLAYER);
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

		this.setCurXp(0);
		this.setDeathSavingThrow_SUCCESS(0);
		this.setDeathSavingThrow_FAILURE(0);
		this.setNumDeaths(0);
	}

	/**
	 * GamePlayer(ALL_INPUT_PARAMS)
	 */
	public GamePlayer(
			String uid, String name, String description, int level, String actorRace, String actorClass, int age, String gender, int height, int weight, 
			String eyeDesc, String hairDesc, String skinDesc, int initiative, int speed, int STR, int DEX, int CON, int INT,int WIS, int CHA, int STR_mod, 
			int DEX_mod, int CON_mod, int INT_mod, int WIS_mod, int CHA_mod, int FORT_save, int RFLX_save, int WILL_save, int hitDie, int size, 
			int carryWeight, int curHp, int maxHp, int curSp, int maxSp, int attackBonusMelee, int attackBonusRanged, int attackBonusMagic, int armorClass, 
			int damageReduction, int magicResistance, List<GameItem> inventory, List<GameSpell> spellBook, List<String> languages, 
			List<GameDiplomacy> diplomacy, int roomIndex, int volumeIndex, GameItem equipSlot_BACK, GameItem equipSlot_BELT, GameItem equipSlot_BOOTS, 
			GameItem equipSlot_CUIRASS, GameItem equipSlot_GAUNTLETS, GameItem equipSlot_GREAVES, GameItem equipSlot_HELM, GameItem equipSlot_MAIN_HAND, 
			GameItem equipSlot_OFF_HAND, GameItem equipSlot_TWO_HAND, GameItem equipSlot_PANTS, GameItem equipSlot_SHIRT, GameItem equipSlot_SHOES, 
			GameItem equipSlot_RING, GameItem equipSlot_AMULET, GameItem equipSlot_EARRING, GameItem equipSlot_CLOAK, int curXp, int deathSavingThrow_SUCCESS, 
			int deathSavingThrow_FAILURE, int numDeaths
		) {
		this.setAssetType(GameAsset.assetType_ACTOR_PLAYER);//this.getClass().getName()
		this.setUid(uid);
		
		this.setActorType(GameActor.actorType_PLAYER);
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

		this.setCurXp(curXp);
		this.setDeathSavingThrow_SUCCESS(deathSavingThrow_SUCCESS);
		this.setDeathSavingThrow_FAILURE(deathSavingThrow_FAILURE);
		this.setNumDeaths(numDeaths);
	}
	
	public static Map<String,GamePlayer> getAllPlayers() {return GamePlayer.allPlayers;}

	public int getCurXp() {return curXp;}
	public int getDeathSavingThrow_SUCCESS() {return deathSavingThrow_SUCCESS;}
	public int getDeathSavingThrow_FAILURE() {return deathSavingThrow_FAILURE;}
	public int getNumDeaths() {return numDeaths;}

	public void setCurXp(int curXp) {this.curXp = curXp;}
	public void setDeathSavingThrow_SUCCESS(int deathSavingThrow_SUCCESS) {this.deathSavingThrow_SUCCESS = deathSavingThrow_SUCCESS;}
	public void setDeathSavingThrow_FAILURE(int deathSavingThrow_FAILURE) {this.deathSavingThrow_FAILURE = deathSavingThrow_FAILURE;}
	public void setNumDeaths(int numDeaths) {this.numDeaths = numDeaths;}

	/**
	 * add(GamePlayer) Adds the specified GamePlayer to the List of all Players in the Game, if not already present
	 * @param player The GamePlayer to add
	 */
	public static void add(GamePlayer player) {
		if(allPlayers == null) {
			allPlayers = new HashMap<String,GamePlayer>();
		}
		if(!allPlayers.containsKey(player.getUid())) {
			allPlayers.put(player.getUid().toLowerCase(),player);
		}
	}

	/**
	 * lookupPlayer(String) Look up the GamePlayer by its UID in the list of current GamePlayers
	 * @param playerUid The (unique) identifier of the Player which is to be returned
	 * @return GamePlayer specified by the UID, if found in the current list of GamePlayers
	 */
	public static GamePlayer lookupPlayer(String playerUid) {
		if(allPlayers == null || allPlayers.isEmpty()) {
			return null;
		}
		return allPlayers.get(playerUid.toLowerCase());
	}

	/**
	 * getBytes() Returns a representation of the GamePlayer in an array of bytes
	 * @return byte[] containing the data of this GamePlayer
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * getByteString() Converts an instance of GamePlayer into a byte[] so it can be written to disk
	 * @return String containing the data of this GamePlayer
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

		dataString += "§" + this.getCurXp() + "§" + this.getDeathSavingThrow_SUCCESS() + "§" + this.getDeathSavingThrow_FAILURE() + "§" + this.getNumDeaths();
		return dataString;
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GamePlayer
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GamePlayer contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GamePlayer containing the data parsed from the byte array
	 */
	public static GamePlayer parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] playerData = s.split("§");
		GamePlayer player = null;
		if(playerData != null && playerData.length == 72 && playerData[0].equals("GamePlayer")) {//GameAsset(2)+GameActor(49)+GameNpc(4)
			String[] inventoryData = playerData[45].split("¶");
			List<GameItem> inventory = new ArrayList<GameItem>();
			for(String itemData: inventoryData) {
				GameItem item = GameItem.parseBytes(itemData.getBytes(),false);
				if(item != null) {
					inventory.add(item);
				}
			}
			String[] spellBookData = playerData[46].split("¶");
			List<GameSpell> spellBook = new ArrayList<GameSpell>();
			for(String spellData: spellBookData) {
				GameSpell spell = GameSpell.parseBytes(spellData.getBytes(),false);
				if(spell != null) {
					spellBook.add(spell);
				}
			}
			String[] languageData = playerData[47].split("¶");
			List<String> languages = new ArrayList<String>();
			for(String language: languageData) {
				if(language != null && language.length() > 0) {
					languages.add(language);
				}
			}
			String[] diplomacyData = playerData[48].split("¶");
			List<GameDiplomacy> diplomacy = new ArrayList<GameDiplomacy>();
			for(String disposition: diplomacyData) {
				GameDiplomacy diplomacyItem = GameDiplomacy.parseBytes(disposition.getBytes(),isTemplate);
				if(diplomacyItem != null) {
					diplomacy.add(diplomacyItem);
				}
			}
			player = new GamePlayer();
			player.setAssetType(playerData[0]);//assetType
			player.setUid(playerData[1]);//uid//userName + characterName
			player.setActorType(playerData[2]);//actorType
			player.setName(playerData[3]);//name
			player.setDescription(playerData[4]);//description
			player.setLevel(Integer.parseInt(playerData[5]));//level
			player.setActorRace(playerData[6]);//actorRace
			player.setActorClass(playerData[7]);//actorClass
			player.setAge(Integer.parseInt(playerData[8]));//age
			player.setGender(playerData[9]);//gender
			player.setHeight(Integer.parseInt(playerData[10]));//height
			player.setWeight(Integer.parseInt(playerData[11]));//weight
			player.setEyeDesc(playerData[12]);//eyeDesc
			player.setHairDesc(playerData[13]);//hairDesc
			player.setSkinDesc(playerData[14]);//skinDesc
			player.setInitiative(Integer.parseInt(playerData[15]));//initiative
			player.setSpeed(Integer.parseInt(playerData[16]));//speed
			player.setSTR(Integer.parseInt(playerData[17]));//STR
			player.setDEX(Integer.parseInt(playerData[18]));//DEX
			player.setCON(Integer.parseInt(playerData[19]));//CON
			player.setINT(Integer.parseInt(playerData[20]));//INT
			player.setWIS(Integer.parseInt(playerData[21]));//WIS
			player.setCHA(Integer.parseInt(playerData[22]));//CHA
			player.setSTR_mod(Integer.parseInt(playerData[23]));//STR_mod
			player.setDEX_mod(Integer.parseInt(playerData[24]));//DEX_mod
			player.setCON_mod(Integer.parseInt(playerData[25]));//CON_mod
			player.setINT_mod(Integer.parseInt(playerData[26]));//INT_mod
			player.setWIS_mod(Integer.parseInt(playerData[27]));//WIS_mod
			player.setCHA_mod(Integer.parseInt(playerData[28]));//CHA_mod
			player.setFORT_save(Integer.parseInt(playerData[29]));//FORT_save
			player.setRFLX_save(Integer.parseInt(playerData[30]));//RFLX_save
			player.setWILL_save(Integer.parseInt(playerData[31]));//WILL_save
			player.setHitDie(Integer.parseInt(playerData[32]));//hitDie
			player.setSize(Integer.parseInt(playerData[33]));//size
			player.setCarryWeight(Integer.parseInt(playerData[34]));//carryWeight
			player.setCurHp(Integer.parseInt(playerData[35]));//curHp
			player.setMaxHp(Integer.parseInt(playerData[36]));//maxHp
			player.setCurSp(Integer.parseInt(playerData[37]));//curSp
			player.setMaxSp(Integer.parseInt(playerData[38]));//maxSp
			player.setAttackBonusMelee(Integer.parseInt(playerData[39]));//attackBonusMelee
			player.setAttackBonusRanged(Integer.parseInt(playerData[40]));//attackBonusRanged
			player.setAttackBonusMagic(Integer.parseInt(playerData[41]));//attackBonusMagic
			player.setArmorClass(Integer.parseInt(playerData[42]));//armorClass
			player.setDamageReduction(Integer.parseInt(playerData[43]));//damageReduction
			player.setMagicResistance(Integer.parseInt(playerData[44]));//magicResistance
			player.setInventory(inventory);//inventory
			player.setSpellBook(spellBook);//spellBook
			player.setLanguages(languages);//languages
			player.setDiplomacy(diplomacy);//diplomacy
			player.setRoomIndex(Integer.parseInt(playerData[49]));//roomIndex
			player.setVolumeIndex(Integer.parseInt(playerData[50]));//volumeIndex

			player.setEquipSlot_BACK(GameItem.parseBytes(playerData[51].getBytes(),false));
			player.setEquipSlot_BELT(GameItem.parseBytes(playerData[52].getBytes(),false));
			player.setEquipSlot_BOOTS(GameItem.parseBytes(playerData[53].getBytes(),false));
			player.setEquipSlot_CUIRASS(GameItem.parseBytes(playerData[54].getBytes(),false));
			player.setEquipSlot_GAUNTLETS(GameItem.parseBytes(playerData[55].getBytes(),false));
			player.setEquipSlot_GREAVES(GameItem.parseBytes(playerData[56].getBytes(),false));
			player.setEquipSlot_HELM(GameItem.parseBytes(playerData[57].getBytes(),false));
			player.setEquipSlot_MAIN_HAND(GameItem.parseBytes(playerData[58].getBytes(),false));
			player.setEquipSlot_OFF_HAND(GameItem.parseBytes(playerData[59].getBytes(),false));
			player.setEquipSlot_TWO_HAND(GameItem.parseBytes(playerData[60].getBytes(),false));
			player.setEquipSlot_PANTS(GameItem.parseBytes(playerData[61].getBytes(),false));
			player.setEquipSlot_SHIRT(GameItem.parseBytes(playerData[62].getBytes(),false));
			player.setEquipSlot_SHOES(GameItem.parseBytes(playerData[63].getBytes(),false));
			player.setEquipSlot_RING(GameItem.parseBytes(playerData[64].getBytes(),false));
			player.setEquipSlot_AMULET(GameItem.parseBytes(playerData[65].getBytes(),false));
			player.setEquipSlot_EARRING(GameItem.parseBytes(playerData[66].getBytes(),false));
			player.setEquipSlot_CLOAK(GameItem.parseBytes(playerData[67].getBytes(),false));

			player.setCurXp(Integer.parseInt(playerData[68]));//curXp
			player.setDeathSavingThrow_SUCCESS(Integer.parseInt(playerData[69]));//deathSavingThrow_SUCCESS
			player.setDeathSavingThrow_FAILURE(Integer.parseInt(playerData[70]));//deathSavingThrow_FAILURE
			player.setNumDeaths(Integer.parseInt(playerData[71]));//numDeaths
			if(isTemplate) {
				GamePlayer.add(player);
			}
		}
		return player;
	}
}
