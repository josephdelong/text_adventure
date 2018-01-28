package game.asset.actor;

import game.asset.GameAsset;
import game.asset.GameDiplomacy;
import game.asset.item.GameItem;
import game.asset.object.GameObject;
import game.asset.room.GameRoom;
import game.asset.room.GameVolume;
import game.asset.spell.GameSpell;
import game.util.GameUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * GameActor Class that represents an Actor in the Game (This can be an NPC, a Player, or an Enemy, etc.)
 * @author Joseph DeLong
 */
public abstract class GameActor extends GameAsset {
	private String actorType;//player,enemy,npc
	private String name;
	private String description;
	private int level;
	private String actorRace;
	private String actorClass;
	private int age;
	private String gender;
	private int height;
	private int weight;
	private String eyeDesc;
	private String hairDesc;
	private String skinDesc;
	private int initiative;
	private int speed;
	private int STR;
	private int DEX;
	private int CON;
	private int INT;
	private int WIS;
	private int CHA;
	private int STR_mod;
	private int DEX_mod;
	private int CON_mod;
	private int INT_mod;
	private int WIS_mod;
	private int CHA_mod;
	private int FORT_save;
	private int RFLX_save;
	private int WILL_save;
	private int hitDie;
	private int size;
	private int carryWeight;
	private int curHp;
	private int maxHp;
	private int curSp;
	private int maxSp;
	private int attackBonusMelee;
	private int attackBonusRanged;
	private int attackBonusMagic;
	private int armorClass;
	private int damageReduction;
	private int magicResistance;
	private List<GameItem> inventory;
	private List<GameSpell> spellBook;
	private List<String> languages;
	private List<GameDiplomacy> diplomacy;
	private int roomIndex;
	private int volumeIndex;
	
	private GameItem equipSlot_BACK;
	private GameItem equipSlot_BELT;
	private GameItem equipSlot_BOOTS;
	private GameItem equipSlot_CUIRASS;
	private GameItem equipSlot_GAUNTLETS;
	private GameItem equipSlot_GREAVES;
	private GameItem equipSlot_HELM;
	private GameItem equipSlot_MAIN_HAND;
	private GameItem equipSlot_OFF_HAND;
	private GameItem equipSlot_TWO_HAND;
	private GameItem equipSlot_PANTS;
	private GameItem equipSlot_SHIRT;
	private GameItem equipSlot_SHOES;
	private GameItem equipSlot_RING;
	private GameItem equipSlot_AMULET;
	private GameItem equipSlot_EARRING;
	private GameItem equipSlot_CLOAK;

	public static final String actorType_ENEMY = "enemy";
	public static final String actorType_NPC = "npc";
	public static final String actorType_OTHER = "other";
	public static final String actorType_PLAYER = "player";

	public static final String race_HUMAN = "HUMAN";
	public static final String race_ELF = "ELF";
	public static final String race_DWARF = "DWARF";
	public static final String race_HALFLING = "HALFLING";
	public static final String race_ORC = "ORC";
	public static final String race_GOBLIN = "GOBLIN";
	public static final String race_UNDEAD = "UNDEAD";
	public static final String race_BEASTMAN = "BEASTMAN";
	public static final String race_DEMON = "DEMON";
	public static final String race_CELESTIAL = "CELESTIAL";
	public static final String race_DRAGON = "DRAGON";

	public static final String class_FIGHTER = "FIGHTER";
	public static final String class_RANGER = "RANGER";
	public static final String class_MAGE = "MAGE";

	public static final String[] namesMale = new String[]{
		"Armon","Baldhus","Chardin","Dergin","Elterre","Fendus","Geldorn","Heimlen","Irsok","Janlus","Khelden","Leifkur","Mentfel",
		"Nortow","Oorfet","Pujdeer","Quorval","Renveld","Schelbo","Terpael","Ungsveid","Verdbog","Wulgen","Xhelzbar","Ypdral","Zutken"
	};
	public static final String[] namesFemale = new String[]{
		"Arwyn","Baelune","Charris","Dinnae","Elmha","Feirwin","Gelda","Heimda","Irlen","Jeinla","Khelta","Leinfer","Maenfil",
		"Nolar","Orfae","Pujveir","Qoorvae","Rennva","Shalbi","Tynpal","Unsvael","Valboc","Walgre","Xhensper","Yptil","Zutvonn"
	};
	public static final String[] namesOther = new String[] {
		"Arnel","Beilar","Charvik","Duuren","Elrid","Falmet","Gildens","Haervol","Irmend","Julnus","Khelmar","Leitrel","Marden",
		"Norfeir","Ourfil","Pujwaar","Quorven","Renval","Scholva","Tulnear","Unsbeir","Voorbyr","Wilgir","Xhulsvel","Ypgrat","Zutbour"
	};

	public String getActorType() {return this.actorType;}
	public String getName() {return this.name;}
	public String getDescription() {return this.description;}
	public int getLevel() {return this.level;}
	public String getActorRace() {return this.actorRace;}
	public String getActorClass() {return this.actorClass;}
	public int getAge() {return this.age;}
	public String getGender() {return this.gender;}
	public int getHeight() {return this.height;}
	public int getWeight() {return this.weight;}
	public String getEyeDesc() {return this.eyeDesc;}
	public String getHairDesc() {return this.hairDesc;}
	public String getSkinDesc() {return this.skinDesc;}
	public int getInitiative() {return this.initiative;}
	public int getSpeed() {return this.speed;}
	public int getSTR() {return this.STR;}
	public int getDEX() {return this.DEX;}
	public int getCON() {return this.CON;}
	public int getINT() {return this.INT;}
	public int getWIS() {return this.WIS;}
	public int getCHA() {return this.CHA;}
	public int getSTR_mod() {return this.STR_mod;}
	public int getDEX_mod() {return this.DEX_mod;}
	public int getCON_mod() {return this.CON_mod;}
	public int getINT_mod() {return this.INT_mod;}
	public int getWIS_mod() {return this.WIS_mod;}
	public int getCHA_mod() {return this.CHA_mod;}
	public int getFORT_save() {return this.FORT_save;}
	public int getRFLX_save() {return this.RFLX_save;}
	public int getWILL_save() {return this.WILL_save;}
	public int getHitDie() {return this.hitDie;}
	public int getSize() {return this.size;}
	public int getCarryWeight() {return this.carryWeight;}
	public int getCurHp() {return this.curHp;}
	public int getMaxHp() {return this.maxHp;}
	public int getCurSp() {return this.curSp;}
	public int getMaxSp() {return this.maxSp;}
	public int getAttackBonusMelee() {return this.attackBonusMelee;}
	public int getAttackBonusRanged() {return this.attackBonusRanged;}
	public int getAttackBonusMagic() {return this.attackBonusMagic;}
	public int getArmorClass() {return this.armorClass;}
	public int getDamageReduction() {return this.damageReduction;}
	public int getMagicResistance() {return this.magicResistance;}
	public List<GameItem> getInventory() {return this.inventory;}
	public List<GameSpell> getSpellBook() {return this.spellBook;}
	public List<String> getLanguages() {return this.languages;}
	public List<GameDiplomacy> getDiplomacy() {return this.diplomacy;}
	public int getRoomIndex() {return this.roomIndex;}
	public int getVolumeIndex() {return this.volumeIndex;}
	public GameItem getEquipSlot_BACK() {return this.equipSlot_BACK;}
	public GameItem getEquipSlot_BELT() {return this.equipSlot_BELT;}
	public GameItem getEquipSlot_BOOTS() {return this.equipSlot_BOOTS;}
	public GameItem getEquipSlot_CUIRASS() {return this.equipSlot_CUIRASS;}
	public GameItem getEquipSlot_GAUNTLETS() {return this.equipSlot_GAUNTLETS;}
	public GameItem getEquipSlot_GREAVES() {return this.equipSlot_GREAVES;}
	public GameItem getEquipSlot_HELM() {return this.equipSlot_HELM;}
	public GameItem getEquipSlot_MAIN_HAND() {return this.equipSlot_MAIN_HAND;}
	public GameItem getEquipSlot_OFF_HAND() {return this.equipSlot_OFF_HAND;}
	public GameItem getEquipSlot_TWO_HAND() {return this.equipSlot_TWO_HAND;}
	public GameItem getEquipSlot_PANTS() {return this.equipSlot_PANTS;}
	public GameItem getEquipSlot_SHIRT() {return this.equipSlot_SHIRT;}
	public GameItem getEquipSlot_SHOES() {return this.equipSlot_SHOES;}
	public GameItem getEquipSlot_RING() {return this.equipSlot_RING;}
	public GameItem getEquipSlot_AMULET() {return this.equipSlot_AMULET;}
	public GameItem getEquipSlot_EARRING() {return this.equipSlot_EARRING;}
	public GameItem getEquipSlot_CLOAK() {return this.equipSlot_CLOAK;}

	public void setActorType(String actorType) {this.actorType = actorType;}
	public void setName(String name) {this.name = name;}
	public void setDescription(String description) {this.description = description;}
	public void setLevel(int level) {this.level = level;}
	public void setActorRace(String actorRace) {this.actorRace = actorRace;}
	public void setActorClass(String actorClass) {this.actorClass = actorClass;}
	public void setAge(int age) {this.age = age;}
	public void setGender(String gender) {this.gender = gender;}
	public void setHeight(int height) {this.height = height;}
	public void setWeight(int weight) {this.weight = weight;}
	public void setEyeDesc(String eyeDesc) {this.eyeDesc = eyeDesc;}
	public void setHairDesc(String hairDesc) {this.hairDesc = hairDesc;}
	public void setSkinDesc(String skinDesc) {this.skinDesc = skinDesc;}
	public void setInitiative(int initiative) {this.initiative = initiative;}
	public void setSpeed(int speed) {this.speed = speed;}
	public void setSTR(int STR) {this.STR = STR;}
	public void setDEX(int DEX) {this.DEX = DEX;}
	public void setCON(int CON) {this.CON = CON;}
	public void setINT(int INT) {this.INT = INT;}
	public void setWIS(int WIS) {this.WIS = WIS;}
	public void setCHA(int CHA) {this.CHA = CHA;}
	public void setSTR_mod(int STR_mod) {this.STR_mod = STR_mod;}
	public void setDEX_mod(int DEX_mod) {this.DEX_mod = DEX_mod;}
	public void setCON_mod(int CON_mod) {this.CON_mod = CON_mod;}
	public void setINT_mod(int INT_mod) {this.INT_mod = INT_mod;}
	public void setWIS_mod(int WIS_mod) {this.WIS_mod = WIS_mod;}
	public void setCHA_mod(int CHA_mod) {this.CHA_mod = CHA_mod;}
	public void setFORT_save(int FORT_save) {this.FORT_save = FORT_save;}
	public void setRFLX_save(int RFLX_save) {this.RFLX_save = RFLX_save;}
	public void setWILL_save(int WILL_save) {this.WILL_save = WILL_save;}
	public void setHitDie(int hitDie) {this.hitDie = hitDie;}
	public void setSize(int size) {this.size = size;}
	public void setCarryWeight(int carryWeight) {this.carryWeight = carryWeight;}
	public void setCurHp(int curHp) {this.curHp = curHp;}
	public void setMaxHp(int maxHp) {this.maxHp = maxHp;}
	public void setCurSp(int curSp) {this.curSp = curSp;}
	public void setMaxSp(int maxSp) {this.maxSp = maxSp;}
	public void setAttackBonusMelee(int attackBonusMelee) {this.attackBonusMelee = attackBonusMelee;}
	public void setAttackBonusRanged(int attackBonusRanged) {this.attackBonusRanged = attackBonusRanged;}
	public void setAttackBonusMagic(int attackBonusMagic) {this.attackBonusMagic = attackBonusMagic;}
	public void setArmorClass(int armorClass) {this.armorClass = armorClass;}
	public void setDamageReduction(int damageReduction) {this.damageReduction = damageReduction;}
	public void setMagicResistance(int magicResistance) {this.magicResistance = magicResistance;}
	public void setInventory(List<GameItem> inventory) {this.inventory = inventory;}
	public void setSpellBook(List<GameSpell> spellBook) {this.spellBook = spellBook;}
	public void setLanguages(List<String> languages) {this.languages = languages;}
	public void setDiplomacy(List<GameDiplomacy> diplomacy) {this.diplomacy = diplomacy;}
	public void setRoomIndex(int roomIndex) {this.roomIndex = roomIndex;}
	public void setVolumeIndex(int volumeIndex) {this.volumeIndex = volumeIndex;}
	public void setEquipSlot_BACK(GameItem equipSlot_BACK) {this.equipSlot_BACK = equipSlot_BACK;}
	public void setEquipSlot_BELT(GameItem equipSlot_BELT) {this.equipSlot_BELT = equipSlot_BELT;}
	public void setEquipSlot_BOOTS(GameItem equipSlot_BOOTS) {this.equipSlot_BOOTS = equipSlot_BOOTS;}
	public void setEquipSlot_CUIRASS(GameItem equipSlot_CUIRASS) {this.equipSlot_CUIRASS = equipSlot_CUIRASS;}
	public void setEquipSlot_GAUNTLETS(GameItem equipSlot_GAUNTLETS) {this.equipSlot_GAUNTLETS = equipSlot_GAUNTLETS;}
	public void setEquipSlot_GREAVES(GameItem equipSlot_GREAVES) {this.equipSlot_GREAVES = equipSlot_GREAVES;}
	public void setEquipSlot_HELM(GameItem equipSlot_HELM) {this.equipSlot_HELM = equipSlot_HELM;}
	public void setEquipSlot_MAIN_HAND(GameItem equipSlot_MAIN_HAND) {this.equipSlot_MAIN_HAND = equipSlot_MAIN_HAND;}
	public void setEquipSlot_OFF_HAND(GameItem equipSlot_OFF_HAND) {this.equipSlot_OFF_HAND = equipSlot_OFF_HAND;}
	public void setEquipSlot_TWO_HAND(GameItem equipSlot_TWO_HAND) {this.equipSlot_TWO_HAND = equipSlot_TWO_HAND;}
	public void setEquipSlot_PANTS(GameItem equipSlot_PANTS) {this.equipSlot_PANTS = equipSlot_PANTS;}
	public void setEquipSlot_SHIRT(GameItem equipSlot_SHIRT) {this.equipSlot_SHIRT = equipSlot_SHIRT;}
	public void setEquipSlot_SHOES(GameItem equipSlot_SHOES) {this.equipSlot_SHOES = equipSlot_SHOES;}
	public void setEquipSlot_RING(GameItem equipSlot_RING) {this.equipSlot_RING = equipSlot_RING;}
	public void setEquipSlot_AMULET(GameItem equipSlot_AMULET) {this.equipSlot_AMULET = equipSlot_AMULET;}
	public void setEquipSlot_EARRING(GameItem equipSlot_EARRING) {this.equipSlot_EARRING = equipSlot_EARRING;}
	public void setEquipSlot_CLOAK(GameItem equipSlot_CLOAK) {this.equipSlot_CLOAK = equipSlot_CLOAK;}

	/**
	 * attack(GameAsset)
	 * @param target
	 * @return
	 */
	public String attack(GameAsset target) {
		String attackString = "";
		
		return attackString;
	}

	/**
	 * castSpell(GameAsset)
	 * @param target
	 * @return
	 */
	public String castSpell(GameAsset target) {
		String castString = "";
		
		return castString;
	}

	/**
	 * getCarriedWeight() Returns the total weight carried by this GameActor
	 * @return The total weight of all GameItems this GameActor is carrying
	 */
	public int getCarriedWeight() {
		int weight = 0;
		List<GameItem> inventory = this.getInventory();
		if(inventory != null && inventory.size() > 0) {
			for(GameItem item: inventory) {
				weight += item.getTotalWeight();
			}
		}
		return weight;
	}

	/**
	 * canCarry(GameItem) Returns a boolean representing whether this GameActor has enough carryWeight free to pick up the specified GameItem
	 * @param item The GameItem INSTANCE to check carryCapacity against
	 * @return Boolean representing whether this GameActor has sufficient carryCapacity to pick up the specified GameItem
	 */
	public boolean canCarry(GameItem item) {
		return this.getCarryWeight() >= this.getCarriedWeight() + item.getTotalWeight();
	}

	/**
	 * canSee(GameAsset,int)
	 * @param target
	 * @param targetVolumeIndex
	 * @return
	 */
	public boolean canSee(GameAsset target, int targetVolumeIndex) {
		return false;
	}

	/**
	 * getLineOfSightVolumeIndexes(GameAsset,int)
	 * @param target
	 * @param targetVolumeIndex
	 * @return
	 */
	public List<Integer> getLineOfSightVolumeIndexes(GameAsset target, int targetVolumeIndex) {
		return null;
	}

	/**
	 * canNavigateTo(int)
	 * @param targetVolumeIndex
	 * @return
	 */
	public boolean canNavigateTo(int targetVolumeIndex) {
		return false;
	}

	/**
	 * moveTo(int)
	 * @param targetVolumeIndex
	 */
	public void moveTo(int targetVolumeIndex) {
		
	}

	/**
	 * getDistanceToTargetSquared(int)
	 * @param targetVolumeIndex
	 * @return
	 */
	public int getDistanceToTargetSquared(int targetVolumeIndex) {
		return 0;
	}

	/**
	 * getTraversalPath(GameVolume,GameVolume) Returns a list of GameVolumes which lie along the calculated traversal path. The traversal path should be a relatively straight line between the 2 GameVolume parameters.
	 * @param from The GameVolume from which to start traversal
	 * @param to The GameVolume to which to find a traversal path
	 * @return A list of GameVolumes that lie along the traversal path
	 */
	public static List<GameVolume> getTraversalPath(GameRoom room, GameVolume from, GameVolume to) {
		List<GameVolume> traversalVolumes = new ArrayList<GameVolume>();
		if(from.getXPos() == to.getXPos() && from.getYPos() == to.getYPos() && from.getZPos() == to.getZPos()) {
			return traversalVolumes;//NO TRAVEL DIRECTION
		}
//		GameRoom room = this.visitedRooms.get(this.currentRoomId);
		GameVolume step = from;
		traversalVolumes.add(step);
		int xDistance = to.getXPos() - from.getXPos();
		int yDistance = to.getYPos() - from.getYPos();
		int zDistance = to.getZPos() - from.getZPos();
		int totalSteps = Math.max(Math.abs(xDistance),Math.max(Math.abs(yDistance),Math.abs(zDistance)));
		int stepIndex = 0;
		while(stepIndex < totalSteps) {//while not at destination yet
			int nextTraversalDirection = GameUtility.getTraversalDirection(step,to,totalSteps,stepIndex,xDistance,yDistance,zDistance);
			step = GameUtility.getNextVolumeInDirection(room,step,nextTraversalDirection);
			traversalVolumes.add(step);
//			traversalPathIndexes.add(((step.zPos - 1) * room.width * room.length) + ((step.yPos - 1) * room.length) + (step.xPos - 1));
			stepIndex++;
		}
		return traversalVolumes;
	}

	/**
	 * getTraversalPathIndexes(GameVolume,GameVolume) Returns a list of Integers representing the Array indexes of the GameVolumes which lie along the calculated traversal path. The traversal path should be a relatively straight line between the 2 GameVolumes.
	 * @param from The GameVolume from which to start traversal
	 * @param to The GameVolume to which to find a traversal path
	 * @return A list of Integers representing the array indexes of GameVolumes that lie along the traversal path
	 */
	public static List<Integer> getTraversalPathIndexes(GameRoom room, GameVolume from, GameVolume to) {
		List<Integer> traversalPathIndexes = new ArrayList<Integer>();
		if(from.getXPos() == to.getXPos() && from.getYPos() == to.getYPos() && from.getZPos() == to.getZPos()) {
			return traversalPathIndexes;//NO TRAVEL DIRECTION
		}
//		GameRoom room = this.visitedRooms.get(this.currentRoomId);
		GameVolume step = from;
		int xDistance = to.getXPos() - from.getXPos();
		int yDistance = to.getYPos() - from.getYPos();
		int zDistance = to.getZPos() - from.getZPos();
		int totalSteps = Math.max(Math.abs(xDistance),Math.max(Math.abs(yDistance),Math.abs(zDistance)));
		int stepIndex = 0;
		while(stepIndex < totalSteps) {//while not at destination yet
			int nextTraversalDirection = GameUtility.getTraversalDirection(step,to,totalSteps,stepIndex,xDistance,yDistance,zDistance);
			step = GameUtility.getNextVolumeInDirection(room,step,nextTraversalDirection);
			traversalPathIndexes.add(((step.getZPos() - 1) * room.getWidth() * room.getLength()) + ((step.getYPos() - 1) * room.getLength()) + (step.getXPos() - 1));
			stepIndex++;
		}
		return traversalPathIndexes;
	}

	/**
	 * canAttack(String,String,String,String)
	 * @param room The GameRoom in which the FROM and TO GameVolumes exist
	 * @param startVolume The GameVolume in which the player is located
	 * @param targetVolume The GameVolume in which the target is located
	 * @param targetIndex The index of the ArrayList in which the target is stored (if multiple of the same Entity template are in the same GameVolume)
	 * @param targetType The type of Game entity you are trying to attack
	 * @param targetName The name of the Entity to attack (Enemy, Object, Item, Spell, Self, etc.)
	 * @param method The method by which to attack (Melee, Ranged, Thrown, Spell, etc.)
	 * @param implement The name of the implement with which to attack the Entity
	 * @return Success or Failure of the Attack attempt
	 */
	public static boolean canAttack(GameRoom room, GameVolume startVolume, GameVolume targetVolume, int targetIndex, String targetType, String targetName, String method, String implement) {
		boolean canAttack = false;
		double targetDistance = GameUtility.distanceSquaredBetween(startVolume,targetVolume);
		GameItem item = GameItem.lookupTemplate(implement);//use of LOOKUP might be okay here
		if(item == null) {//implement not a valid GameItem (fists, for example)
			if(targetDistance > 3) {//target is > 1 GameVolume away in any direction
				return false;
			}
		}
//		List<Integer> attackVectorIndices = getTraversalPath(startVolume,targetVolume);
		List<GameVolume> traversalVolumes = getTraversalPath(room, startVolume,targetVolume);
		List<Byte> traversalDirections = new ArrayList<Byte>();
		GameVolume start = traversalVolumes.get(0);
		GameVolume end = traversalVolumes.get(traversalVolumes.size() - 1);
		for(int i = 0; i < traversalVolumes.size() - 1; i++) {
			GameVolume from = traversalVolumes.get(i);
			GameVolume to = traversalVolumes.get(i + 1);
			traversalDirections.add(GameUtility.getTraversalDirection(from,to,traversalVolumes.size() - 1,i,end.getXPos() - start.getXPos(),end.getYPos() - start.getYPos(),end.getZPos() - start.getZPos()));
		}
		switch (targetType) {
		case "enemy"://attack an ENEMY
			GameEnemy targetEnemy = targetVolume.getEnemies().get(targetName).get(targetIndex);
			
			break;
		case "object"://attack an OBJECT
			GameObject targetObject = targetVolume.getObjects().get(targetName).get(targetIndex);
			
			break;
		case "item"://attack an ITEM
			GameItem targetItem = targetVolume.getItems().get(targetName).get(targetIndex);
			
			break;
		case "spell"://attack a SPELL
			
			break;
		case "self"://attack SELF? okay...
			canAttack = true;
			break;
		default://unknown target
			canAttack = false;
			break;
		}
		return canAttack;
	}

	/**
	 * canTraverse(int,int,int,int) Determines whether the Player can enter or exit the GameVolume indicated by x,y,z position, can be passed through.
	 * @param xPos The X coordinate of the indicated volume. Should be > 0.
	 * @param yPos The Y coordinate of the indicated volume. Should be > 0.
	 * @param zPos The Z coordinate of the indicated volume. Should be > 0.
	 * @param direction The direction of traversal through the indicated volume.
	 * @return An indication of whether the Player can traverse through the indicated volume in the indicated direction.
	 */
	public static boolean canTraverse(int xPos, int yPos, int zPos, byte direction) {
//		if(xPos > 0 && yPos > 0 && zPos > 0 && direction >= 0 && direction <= 42 && this.visitedRooms.get(this.currentRoomId).interior != null && this.visitedRooms.get(this.currentRoomId).interior.size() >= (xPos * yPos * zPos)) {
//			GameRoom room = this.visitedRooms.get(this.currentRoomId);
//			GameVolume volume = this.visitedRooms.get(this.currentRoomId).interior.get(((zPos - 1) * room.width * room.length) + ((yPos - 1) * room.length) + (xPos - 1));
//			boolean canPass = false;
//			byte passableDirs = volume.passableDirs;
//			//TODO: FINISH THIS METHOD!
//			return volume.canTraverse && canPass;
//		}
		return true;
	}

	/**
	 * traverse(int,int,int,int,int) Moves the Player in the specified Direction & the specified Distance, if able
	 * <br><br>
	 * NOTE: The 3-dimensional space inside the GameRoom is set up so that the coordinate [1,1,1] is at the left-most, down-most, bottom-most corner. That would look something like:<br>
	 * <br>
	 * <b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * FLOOR_1:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * FLOOR_2:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * FLOOR_3:</b><br>
	 * [1,3,1][2,3,1][3,3,1]&nbsp;&nbsp;&nbsp;&nbsp;
	 * [1,3,2][2,3,2][3,3,2]&nbsp;&nbsp;&nbsp;&nbsp;
	 * [1,3,3][2,3,3][3,3,3]<br>
	 * [1,2,1][2,2,1][3,2,1]&nbsp;&nbsp;&nbsp;&nbsp;
	 * [1,2,2][2,2,2][3,2,2]&nbsp;&nbsp;&nbsp;&nbsp;
	 * [1,2,3][2,2,3][3,2,3]<br>
	 * [1,1,1][2,1,1][3,1,1]&nbsp;&nbsp;&nbsp;&nbsp;
	 * [1,1,2][2,1,2][3,1,2]&nbsp;&nbsp;&nbsp;&nbsp;
	 * [1,1,3][2,1,3][3,1,3]<br>
	 * <br>
	 * In this example, coordinate [1,1,1] is in the SOUTHWEST corner of the LOWEST floor, and coordinate [3,3,3] is in the NORTHEAST corner of the HIGHEST floor.
	 * @param xPos The X coordinate of the indicated volume. Should be > 0.
	 * @param yPos The Y coordinate of the indicated volume. Should be > 0.
	 * @param zPos The Z coordinate of the indicated volume. Should be > 0.
	 * @param direction The direction of traversal through the indicated volume.
	 * @param distance The distance to be traveled, in units of GameVolume
	 */
	public static String traverse(GameRoom room, int xPos, int yPos, int zPos, byte direction, int distance) {
//		GameRoom room = this.visitedRooms.get(this.currentRoomId);
		String moveString = "";
		boolean canPath = true;
		int distanceLeft = distance;
		while(canPath && distanceLeft > 0) {//determine next coordinate to look up based on direction
			int newX = xPos;
			int newY = yPos;
			int newZ = zPos;
			int xDir = ((byte) direction & 0b00_00_11) >> 0;//extract the X axis direction using a bit mask
			int yDir = ((byte) direction & 0b00_11_00) >> 2;//extract the Y axis direction using a bit mask
			int zDir = ((byte) direction & 0b11_00_00) >> 4;//extract the Z axis direction using a bit mask
			if(xDir == 0) {//NEGATIVE movement on X axis
				newX--;
			} else if(xDir == 2) {//POSITIVE movement on X axis
				newX++;
			}
			if(yDir == 0) {//NEGATIVE movement on Y axis
				newY--;
			} else if(yDir == 2) {//POSITIVE movement on Y axis
				newY++;
			}
			if(zDir == 0) {//NEGATIVE movement on Z axis
				newZ--;
			} else if(zDir == 2) {//POSOTIVE movement on Z axis
				newZ++;
			}
			if(GameActor.canTraverse(newX, newY, newZ, direction)) {
				int roomVolumeIndex = ((newZ * room.getWidth() * room.getLength()) + (newY * room.getLength()) + newX);
				distanceLeft--;
			} else {
				canPath = false;
				moveString = "You move " + (distance - distanceLeft) + " meter(s) " + GameUtility.getDirectionName(direction) + ".";
			}
		}
		return moveString;
	}
}
