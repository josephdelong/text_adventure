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
 * GameNpc Class that represents a Non-Playable Character, which is not strictly an Enemy of the Player
 * @author Joseph DeLong
 */
public class GameNpc extends GameActor {
	protected static Map<String,GameNpc> npcTemplates;
	
	private boolean isQuestGiver;
	private List<String> questTree;
	private boolean isMerchant;
	private String merchantType;
	private List<GameItem> merchantInv;
	private int restockFrequency;
	private int lastRestockTime;
	private int coinReserve;

	/**
	 * GameNpc() Constructor used for TEMPLATE CREATION MODE. Creates an empty GameNpc TEMPLATE
	 */
	public GameNpc() {
		this.setAssetType(GameAsset.assetType_ACTOR_NPC);//this.getClass().getName()
		this.setUid(GameAsset.generateUid(this.getAssetType()));
		
		this.setActorType(GameActor.actorType_NPC);
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

		this.setIsQuestGiver(false);
		this.setQuestTree(new ArrayList<String>());
		this.setIsMerchant(false);
		this.setMerchantType("null");
		this.setMerchantInv(new ArrayList<GameItem>());
		this.setRestockFrequency(0);//how often, in GameDays, this Npc will re-stock their shop inventory
		this.setLastRestockTime(0);
		this.setCoinReserve(0);
	}

	/**
	 * GameNpc(ALL_INPUT_PARAMS)
	 */
	public GameNpc(
			String uid, String name, String description, int level, String actorRace, String actorClass, int age, String gender, int height, int weight, 
			String eyeDesc, String hairDesc, String skinDesc, int initiative, int speed, int STR, int DEX, int CON, int INT,int WIS, int CHA, int STR_mod, 
			int DEX_mod, int CON_mod, int INT_mod, int WIS_mod, int CHA_mod, int FORT_save, int RFLX_save, int WILL_save, int hitDie, int size, 
			int carryWeight, int curHp, int maxHp, int curSp, int maxSp, int attackBonusMelee, int attackBonusRanged, int attackBonusMagic, int armorClass, 
			int damageReduction, int magicResistance, List<GameItem> inventory, List<GameSpell> spellBook, List<String> languages, 
			List<GameDiplomacy> diplomacy, int roomIndex, int volumeIndex, GameItem equipSlot_BACK, GameItem equipSlot_BELT, GameItem equipSlot_BOOTS, 
			GameItem equipSlot_CUIRASS, GameItem equipSlot_GAUNTLETS, GameItem equipSlot_GREAVES, GameItem equipSlot_HELM, GameItem equipSlot_MAIN_HAND, 
			GameItem equipSlot_OFF_HAND, GameItem equipSlot_TWO_HAND, GameItem equipSlot_PANTS, GameItem equipSlot_SHIRT, GameItem equipSlot_SHOES, 
			GameItem equipSlot_RING, GameItem equipSlot_AMULET, GameItem equipSlot_EARRING, GameItem equipSlot_CLOAK, boolean isQuestGiver, 
			List<String> questTree, boolean isMerchant, String merchantType, List<GameItem> merchantInv, int restockFrequency, int lastRestockTime, 
			int coinReserve
		) {
		this.setAssetType(GameAsset.assetType_ACTOR_NPC);//this.getClass().getName()
		this.setUid(uid);
		
		this.setActorType(GameActor.actorType_NPC);
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

		this.setIsQuestGiver(isQuestGiver);
		this.setQuestTree(questTree);
		this.setIsMerchant(isMerchant);
		this.setMerchantType(merchantType);
		this.setMerchantInv(merchantInv);
		this.setRestockFrequency(restockFrequency);
		this.setLastRestockTime(lastRestockTime);
		this.setCoinReserve(coinReserve);
	}

	/**
	 * GameNpc(GameNpc) Given a base GameNpc TEMPLATE, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE data.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameNpc.lookupTemplate(npcName)</b></code> in order to create in-game
	 * instances of the GameNpc TEMPLATE (stored in <code>GameNpc.npcTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param npcTemplate The GameNpc TEMPLATE to make an INSTANCE copy of
	 */
	public GameNpc(GameNpc npcTemplate) {
		this.setAssetType(GameAsset.assetType_ACTOR_NPC);//this.getClass().getName()
		this.setUid(GameAsset.generateUid(this.getAssetType()));//create a new UID for this INSTANCE
		
		this.setActorType(GameActor.actorType_NPC);
		this.setName(new String(npcTemplate.getName()));
		this.setDescription(new String(npcTemplate.getDescription()));
		this.setLevel(Integer.valueOf(npcTemplate.getLevel()));
		this.setActorRace(new String(npcTemplate.getActorRace()));//GameActor.race_HUMAN
		this.setActorClass(new String(npcTemplate.getActorClass()));//GameActor.class_FIGHTER
		this.setAge(Integer.valueOf(npcTemplate.getAge()));
		this.setGender(new String(npcTemplate.getGender()));//GameActor.gender_MALE
		this.setHeight(Integer.valueOf(npcTemplate.getHeight()));//5' 10"
		this.setWeight(Integer.valueOf(npcTemplate.getWeight()));
		this.setEyeDesc(new String(npcTemplate.getEyeDesc()));//GameActor.eyeColor_BROWN
		this.setHairDesc(new String(npcTemplate.getHairDesc()));//GameActor.hairColor_BLACK
		this.setSkinDesc(new String(npcTemplate.getSkinDesc()));//GameActor.skinColor_DARK_BROWN
		this.setInitiative(Integer.valueOf(npcTemplate.getInitiative()));
		this.setSpeed(Integer.valueOf(npcTemplate.getSpeed()));//30ft of movement per round
		this.setSTR(Integer.valueOf(npcTemplate.getSTR()));
		this.setDEX(Integer.valueOf(npcTemplate.getDEX()));
		this.setCON(Integer.valueOf(npcTemplate.getCON()));
		this.setINT(Integer.valueOf(npcTemplate.getINT()));
		this.setWIS(Integer.valueOf(npcTemplate.getWIS()));
		this.setCHA(Integer.valueOf(npcTemplate.getCHA()));
		this.setSTR_mod(Integer.valueOf(npcTemplate.getSTR_mod()));
		this.setDEX_mod(Integer.valueOf(npcTemplate.getDEX_mod()));
		this.setCON_mod(Integer.valueOf(npcTemplate.getCON_mod()));
		this.setINT_mod(Integer.valueOf(npcTemplate.getINT_mod()));
		this.setWIS_mod(Integer.valueOf(npcTemplate.getWIS_mod()));
		this.setCHA_mod(Integer.valueOf(npcTemplate.getCHA_mod()));
		this.setFORT_save(Integer.valueOf(npcTemplate.getFORT_save()));
		this.setRFLX_save(Integer.valueOf(npcTemplate.getRFLX_save()));
		this.setWILL_save(Integer.valueOf(npcTemplate.getWILL_save()));
		this.setHitDie(Integer.valueOf(npcTemplate.getHitDie()));
		this.setSize(Integer.valueOf(npcTemplate.getSize()));//SMALL=-1/MEDIUM=0/LARGE=1
		this.setCarryWeight(Integer.valueOf(npcTemplate.getCarryWeight()));//10*STR
		this.setCurHp(Integer.valueOf(npcTemplate.getCurHp()));
		this.setMaxHp(Integer.valueOf(npcTemplate.getMaxHp()));
		this.setCurSp(Integer.valueOf(npcTemplate.getCurSp()));
		this.setMaxSp(Integer.valueOf(npcTemplate.getMaxSp()));
		this.setAttackBonusMelee(Integer.valueOf(npcTemplate.getAttackBonusMelee()));
		this.setAttackBonusRanged(Integer.valueOf(npcTemplate.getAttackBonusRanged()));
		this.setAttackBonusMagic(Integer.valueOf(npcTemplate.getAttackBonusMagic()));
		this.setArmorClass(Integer.valueOf(npcTemplate.getArmorClass()));
		this.setDamageReduction(Integer.valueOf(npcTemplate.getDamageReduction()));
		this.setMagicResistance(Integer.valueOf(npcTemplate.getMagicResistance()));
		List<GameItem> inventory = null;
		List<GameItem> templateInventory = npcTemplate.getInventory();
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
		List<GameSpell> templateSpellBook = npcTemplate.getSpellBook();
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
		List<String> templateLanguages = npcTemplate.getLanguages();
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
		List<GameDiplomacy> templateDiplomacy = npcTemplate.getDiplomacy();
		if(templateDiplomacy != null && templateDiplomacy.size() > 0) {
			diplomacy = new ArrayList<GameDiplomacy>();
			for(GameDiplomacy d: templateDiplomacy) {
				diplomacy.add(new GameDiplomacy(d));
			}
		} else {
			diplomacy = new ArrayList<GameDiplomacy>();
		}
		this.setDiplomacy(diplomacy);
		this.setRoomIndex(Integer.valueOf(npcTemplate.getRoomIndex()));
		this.setVolumeIndex(Integer.valueOf(npcTemplate.getVolumeIndex()));

		this.setEquipSlot_BACK(new GameItem(npcTemplate.getEquipSlot_BACK()));
		this.setEquipSlot_BELT(new GameItem(npcTemplate.getEquipSlot_BELT()));
		this.setEquipSlot_BOOTS(new GameItem(npcTemplate.getEquipSlot_BOOTS()));
		this.setEquipSlot_CUIRASS(new GameItem(npcTemplate.getEquipSlot_CUIRASS()));
		this.setEquipSlot_GAUNTLETS(new GameItem(npcTemplate.getEquipSlot_GAUNTLETS()));
		this.setEquipSlot_GREAVES(new GameItem(npcTemplate.getEquipSlot_GREAVES()));
		this.setEquipSlot_HELM(new GameItem(npcTemplate.getEquipSlot_HELM()));
		this.setEquipSlot_MAIN_HAND(new GameItem(npcTemplate.getEquipSlot_MAIN_HAND()));
		this.setEquipSlot_OFF_HAND(new GameItem(npcTemplate.getEquipSlot_OFF_HAND()));
		this.setEquipSlot_TWO_HAND(new GameItem(npcTemplate.getEquipSlot_TWO_HAND()));
		this.setEquipSlot_PANTS(new GameItem(npcTemplate.getEquipSlot_PANTS()));
		this.setEquipSlot_SHIRT(new GameItem(npcTemplate.getEquipSlot_SHIRT()));
		this.setEquipSlot_SHOES(new GameItem(npcTemplate.getEquipSlot_SHOES()));
		this.setEquipSlot_RING(new GameItem(npcTemplate.getEquipSlot_RING()));
		this.setEquipSlot_AMULET(new GameItem(npcTemplate.getEquipSlot_AMULET()));
		this.setEquipSlot_EARRING(new GameItem(npcTemplate.getEquipSlot_EARRING()));
		this.setEquipSlot_CLOAK(new GameItem(npcTemplate.getEquipSlot_CLOAK()));

		this.setIsQuestGiver(Boolean.valueOf(npcTemplate.isQuestGiver()));
		List<String> questTree = null;
		List<String> templateQuests = npcTemplate.getQuestTree();
		if(templateQuests != null && templateQuests.size() > 0) {
			questTree = new ArrayList<String>();
			for(String s: templateQuests) {
				questTree.add(new String(s));
			}
		} else {
			questTree = new ArrayList<String>();
		}
		this.setQuestTree(questTree);
		this.setIsMerchant(Boolean.valueOf(npcTemplate.isMerchant()));
		this.setMerchantType(new String(npcTemplate.getMerchantType()));
		List<GameItem> merchantInv = null;
		List<GameItem> templateMerchantInv = npcTemplate.getMerchantInv();
		if(templateMerchantInv != null && templateMerchantInv.size() > 0) {
			merchantInv = new ArrayList<GameItem>();
			for(GameItem item: templateMerchantInv) {
				merchantInv.add(new GameItem(item));
			}
		} else {
			merchantInv = new ArrayList<GameItem>();
		}
		this.setMerchantInv(merchantInv);
		this.setRestockFrequency(Integer.valueOf(npcTemplate.getRestockFrequency()));
		this.setLastRestockTime(Integer.valueOf(npcTemplate.getLastRestockTime()));
		this.setCoinReserve(Integer.valueOf(npcTemplate.getCoinReserve()));
	}

	public static Map<String,GameNpc> getNpcTemplates() {return GameNpc.npcTemplates;}

	public boolean isQuestGiver() {return isQuestGiver;}
	public List<String> getQuestTree() {return questTree;}
	public boolean isMerchant() {return isMerchant;}
	public String getMerchantType() {return merchantType;}
	public List<GameItem> getMerchantInv() {return merchantInv;}
	public int getRestockFrequency() {return restockFrequency;}
	public int getLastRestockTime() {return lastRestockTime;}
	public int getCoinReserve() {return coinReserve;}

	public void setIsQuestGiver(boolean isQuestGiver) {this.isQuestGiver = isQuestGiver;}
	public void setQuestTree(List<String> questTree) {this.questTree = questTree;}
	public void setIsMerchant(boolean isMerchant) {this.isMerchant = isMerchant;}
	public void setMerchantType(String merchantType) {this.merchantType = merchantType;}
	public void setMerchantInv(List<GameItem> merchantInv) {this.merchantInv = merchantInv;}
	public void setRestockFrequency(int restockFrequency) {this.restockFrequency = restockFrequency;}
	public void setLastRestockTime(int lastRestockTime) {this.lastRestockTime = lastRestockTime;}
	public void setCoinReserve(int coinReserve) {this.coinReserve = coinReserve;}

	/**
	 * add(GameNpc) Adds the specified GameNpc TEMPLATE to the available Npc TEMPLATEs in the Game, if not already present
	 * @param npc The Npc TEMPLATE to add
	 */
	public static void add(GameNpc npc) {
		if(npcTemplates == null) {
			npcTemplates = new HashMap<String,GameNpc>();
		}
		if(!npcTemplates.containsKey(npc.getName())) {
			npcTemplates.put(npc.getName().toLowerCase(),npc);
		}
	}

	/**
	 * lookupTemplate(String)Look up the GameNpc TEMPLATE by its Name in the list of current Game NPC TEMPLATEs
	 * @param npcName The (unique) name of the NPC TEMPLATE which is to be returned
	 * @return GameNpc TEMPLATE specified by the (unique) npcName, if found in the current list of NPC TEMPLATEs available
	 */
	public static GameNpc lookupTemplate(String npcName) {
		if(npcTemplates == null || npcTemplates.isEmpty()) {
			return null;
		}
		return npcTemplates.get(npcName.toLowerCase());
	}

	/**
	 * getBytes() Returns a representation of the GameNpc in an array of bytes
	 * @return byte[] containing the data of this GameNpc
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * getByteString() Converts an instance of GameNpc into a byte[] so it can be written to disk (either as a TEMPLATE or in a Save File)
	 * @return String containing the data of this GameNpc
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

		dataString += "§" + this.isQuestGiver() + "§" + this.getQuestTree() + "§" + this.isMerchant() + "§" + this.getMerchantType() + "§";
		List<GameItem> merchantInventory = this.getMerchantInv();
		if(merchantInventory != null && merchantInventory.size() > 0) {
			for(int i = 0; i < merchantInventory.size(); i++) {
				dataString += merchantInventory.get(i).getByteString();
				if(i < merchantInventory.size() - 1) {
					dataString += "¶";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "§" + this.getRestockFrequency() + "§" + this.getLastRestockTime() + "§" + this.getCoinReserve();
		return dataString;
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameNpc
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameNpc contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameAsset containing the data parsed from the byte array
	 */
	public static GameNpc parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] npcData = s.split("§");
		GameNpc npc = null;
		if(npcData != null && npcData.length == 76 && npcData[0].equals("GameNpc")) {//GameAsset(2)+GameActor(49)+GameNpc(8)
			String[] inventoryData = npcData[45].split("¶");
			List<GameItem> inventory = new ArrayList<GameItem>();
			for(String itemData: inventoryData) {
				GameItem item = GameItem.parseBytes(itemData.getBytes(),isTemplate);
				if(item != null) {
					inventory.add(item);
				}
			}
			String[] spellBookData = npcData[46].split("¶");
			List<GameSpell> spellBook = new ArrayList<GameSpell>();
			for(String spellData: spellBookData) {
				GameSpell spell = GameSpell.parseBytes(spellData.getBytes(),isTemplate);
				if(spell != null) {
					spellBook.add(spell);
				}
			}
			String[] languageData = npcData[47].split("¶");
			List<String> languages = new ArrayList<String>();
			for(String language: languageData) {
				if(language != null && language.length() > 0) {
					languages.add(language);
				}
			}
			String[] diplomacyData = npcData[48].split("¶");
			List<GameDiplomacy> diplomacy = new ArrayList<GameDiplomacy>();
			for(String disposition: diplomacyData) {
				GameDiplomacy diplomacyItem = GameDiplomacy.parseBytes(disposition.getBytes(),isTemplate);
				if(diplomacyItem != null) {
					diplomacy.add(diplomacyItem);
				}
			}
			String[] questTreeData = npcData[69].split("¶");
			List<String> questTree = new ArrayList<String>();
			for(String questData: questTreeData) {
				String quest = questData;//GameQuest.parseBytes(questData.getBytes(),isTemplate);
				if(quest != null) {
					questTree.add(quest);
				}
			}
			String[] merchantInvData = npcData[72].split("¶");
			List<GameItem> merchantInv = new ArrayList<GameItem>();
			for(String itemData: merchantInvData) {
				GameItem item = GameItem.parseBytes(itemData.getBytes(),isTemplate);
				if(item != null) {
					merchantInv.add(item);
				}
			}
			npc = new GameNpc();
			npc.setAssetType(npcData[0]);//assetType
			npc.setUid(npcData[1]);//uid
			npc.setActorType(npcData[2]);//actorType
			npc.setName(npcData[3]);//name
			npc.setDescription(npcData[4]);//description
			npc.setLevel(Integer.parseInt(npcData[5]));//level
			npc.setActorRace(npcData[6]);//actorRace
			npc.setActorClass(npcData[7]);//actorClass
			npc.setAge(Integer.parseInt(npcData[8]));//age
			npc.setGender(npcData[9]);//gender
			npc.setHeight(Integer.parseInt(npcData[10]));//height
			npc.setWeight(Integer.parseInt(npcData[11]));//weight
			npc.setEyeDesc(npcData[12]);//eyeDesc
			npc.setHairDesc(npcData[13]);//hairDesc
			npc.setSkinDesc(npcData[14]);//skinDesc
			npc.setInitiative(Integer.parseInt(npcData[15]));//initiative
			npc.setSpeed(Integer.parseInt(npcData[16]));//speed
			npc.setSTR(Integer.parseInt(npcData[17]));//STR
			npc.setDEX(Integer.parseInt(npcData[18]));//DEX
			npc.setCON(Integer.parseInt(npcData[19]));//CON
			npc.setINT(Integer.parseInt(npcData[20]));//INT
			npc.setWIS(Integer.parseInt(npcData[21]));//WIS
			npc.setCHA(Integer.parseInt(npcData[22]));//CHA
			npc.setSTR_mod(Integer.parseInt(npcData[23]));//STR_mod
			npc.setDEX_mod(Integer.parseInt(npcData[24]));//DEX_mod
			npc.setCON_mod(Integer.parseInt(npcData[25]));//CON_mod
			npc.setINT_mod(Integer.parseInt(npcData[26]));//INT_mod
			npc.setWIS_mod(Integer.parseInt(npcData[27]));//WIS_mod
			npc.setCHA_mod(Integer.parseInt(npcData[28]));//CHA_mod
			npc.setFORT_save(Integer.parseInt(npcData[29]));//FORT_save
			npc.setRFLX_save(Integer.parseInt(npcData[30]));//RFLX_save
			npc.setWILL_save(Integer.parseInt(npcData[31]));//WILL_save
			npc.setHitDie(Integer.parseInt(npcData[32]));//hitDie
			npc.setSize(Integer.parseInt(npcData[33]));//size
			npc.setCarryWeight(Integer.parseInt(npcData[34]));//carryWeight
			npc.setCurHp(Integer.parseInt(npcData[35]));//curHp
			npc.setMaxHp(Integer.parseInt(npcData[36]));//maxHp
			npc.setCurSp(Integer.parseInt(npcData[37]));//curSp
			npc.setMaxSp(Integer.parseInt(npcData[38]));//maxSp
			npc.setAttackBonusMelee(Integer.parseInt(npcData[39]));//attackBonusMelee
			npc.setAttackBonusRanged(Integer.parseInt(npcData[40]));//attackBonusRanged
			npc.setAttackBonusMagic(Integer.parseInt(npcData[41]));//attackBonusMagic
			npc.setArmorClass(Integer.parseInt(npcData[42]));//armorClass
			npc.setDamageReduction(Integer.parseInt(npcData[43]));//damageReduction
			npc.setMagicResistance(Integer.parseInt(npcData[44]));//magicResistance
			npc.setInventory(inventory);//inventory
			npc.setSpellBook(spellBook);//spellBook
			npc.setLanguages(languages);//languages
			npc.setDiplomacy(diplomacy);//diplomacy
			npc.setRoomIndex(Integer.parseInt(npcData[49]));//roomIndex
			npc.setVolumeIndex(Integer.parseInt(npcData[50]));//volumeIndex

			npc.setEquipSlot_BACK(GameItem.parseBytes(npcData[51].getBytes(),false));
			npc.setEquipSlot_BELT(GameItem.parseBytes(npcData[52].getBytes(),false));
			npc.setEquipSlot_BOOTS(GameItem.parseBytes(npcData[53].getBytes(),false));
			npc.setEquipSlot_CUIRASS(GameItem.parseBytes(npcData[54].getBytes(),false));
			npc.setEquipSlot_GAUNTLETS(GameItem.parseBytes(npcData[55].getBytes(),false));
			npc.setEquipSlot_GREAVES(GameItem.parseBytes(npcData[56].getBytes(),false));
			npc.setEquipSlot_HELM(GameItem.parseBytes(npcData[57].getBytes(),false));
			npc.setEquipSlot_MAIN_HAND(GameItem.parseBytes(npcData[58].getBytes(),false));
			npc.setEquipSlot_OFF_HAND(GameItem.parseBytes(npcData[59].getBytes(),false));
			npc.setEquipSlot_TWO_HAND(GameItem.parseBytes(npcData[60].getBytes(),false));
			npc.setEquipSlot_PANTS(GameItem.parseBytes(npcData[61].getBytes(),false));
			npc.setEquipSlot_SHIRT(GameItem.parseBytes(npcData[62].getBytes(),false));
			npc.setEquipSlot_SHOES(GameItem.parseBytes(npcData[63].getBytes(),false));
			npc.setEquipSlot_RING(GameItem.parseBytes(npcData[64].getBytes(),false));
			npc.setEquipSlot_AMULET(GameItem.parseBytes(npcData[65].getBytes(),false));
			npc.setEquipSlot_EARRING(GameItem.parseBytes(npcData[66].getBytes(),false));
			npc.setEquipSlot_CLOAK(GameItem.parseBytes(npcData[67].getBytes(),false));

			npc.setIsQuestGiver(Boolean.parseBoolean(npcData[68]));//isQuestGiver
			npc.setQuestTree(questTree);//questTree
			npc.setIsMerchant(Boolean.parseBoolean(npcData[70]));//isMerchant
			npc.setMerchantType(npcData[71]);//merchantType
			npc.setMerchantInv(merchantInv);//merchantInv
			npc.setRestockFrequency(Integer.parseInt(npcData[73]));//restockFrequency
			npc.setLastRestockTime(Integer.parseInt(npcData[74]));//lastRestockTime
			npc.setCoinReserve(Integer.parseInt(npcData[75]));//coinReserve
			if(isTemplate) {
				GameNpc.add(npc);
			}
		}
		return npc;
	}
}
