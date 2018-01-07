package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameState A class that stores the current state of the Game
 * @author Joseph DeLong
 */
public class GameState {
	/* MIGHT do these...
	protected String name;
	protected String class;
	protected String race;
	protected String alignment;//use a Tuple here? <x,y>
	protected String deity;
	protected int size;//Small=-1,Medium=0,Large=1
	protected int age;
	protected String gender;
	protected int height;//in inches (50 = 4'2", 99 = 8'3")
	protected double weight;//in Imperial lbs. Might just be an int
	protected String eyeDesc;//color,size,special features, etc.
	protected String hairDesc//^^;
	protected String skinDesc//^^ minus size;
	protected int speed;//how many feet per round you may move at normal speed
	protected int hitDie;//(([rand(1,hitDie)]/hitDie) + abilityMod_CON) Hp gained per player level
	protected int deathSavingThrow_SUCCESS;
	protected int deathSavingThrow_FAILURE;
	//do I want to do a DnD-like thing?
	protected int abilityBase_STR;
	protected int abilityBase_DEX;
	protected int abilityBase_CON;
	protected int abilityBase_INT;
	protected int abilityBase_WIS;
	protected int abilityBase_CHA;
	protected int abilityMod_STR;
	//etc
	protected int reflex;//DEX saving throw
	protected int fortitude;//CON saving throw
	protected int willpower;//WIS saving throw
	 */
	protected int currentXp;
	protected int currentLevel;
	protected int currentHp;
	protected int maxHp;
	protected String heldItem;
	protected Map<String,GameItem> inventory;
	protected double currentWeight;
	protected double maxWeight;
	protected int currentRoomId;
	protected boolean inInventory;
	protected Map<Integer,GameRoom> visitedRooms;
	protected int roomVolumeIndex;
	//additional parameters
	protected int armorClass;
	protected int damageReduction;
	protected int magicResistance;
	protected int initiative;
	protected Map<String,GameSpell> spellbook;
	protected int currentSp;//current Spell Points
	protected int maxSp;//max Spell Points
	protected int attackBonusMelee;
	protected int attackBonusRanged;
	protected int attackBonusMagic;//probably won't differentiate this after all, but it's a neat idea

	protected static GameItem equipSlot_BACK;
	protected static GameItem equipSlot_BELT;
	protected static GameItem equipSlot_BOOTS;
	protected static GameItem equipSlot_CUIRASS;
	protected static GameItem equipSlot_GAUNTLETS;
	protected static GameItem equipSlot_GREAVES;
	protected static GameItem equipSlot_HELM;
	protected static GameItem equipSlot_MAIN_HAND;
	protected static GameItem equipSlot_OFF_HAND;
	protected static GameItem equipSlot_PANTS;
	protected static GameItem equipSlot_SHIRT;
	protected static GameItem equipSlot_SHOES;
	protected static GameItem equipSlot_RING;
	protected static GameItem equipSlot_AMULET;
	protected static GameItem equipSlot_EARRING;
	protected static GameItem equipSlot_CLOAK;

	/**
	 * GameState() Sets the current Game State to default values
	 */
	protected GameState() {
		this.currentXp = 0;
		this.currentLevel = 1;
		this.currentHp = 10;
		this.maxHp = 10;
		this.heldItem = "lamp";
		Map<String,GameItem> inv = new HashMap<String,GameItem>();
		GameItem lamp = new GameItem("lamp",1,0,1,100,100,"null","A dim oil lamp. It gives off a warm glow up to 50 feet away.",1,GameItem.OFF_HAND,true);
		GameItem shirt = new GameItem("shirt",0.5,0,1,-1,-1,"null","A comfortable homespun hemp shirt. Suitable for all seasons.",0,GameItem.SHIRT,true);
		GameItem pants = new GameItem("pants",1.5,0,1,-1,-1,"null","A pair of well-worn homespun hemp trousers. They're quite comfortable and warm.",0,GameItem.PANTS,true);
		GameItem shoes = new GameItem("shoes",0.5,0,1,-1,-1,"null","A pair of common walking shoes, well suited for everyday use.",0,GameItem.SHOES,true);
		equipSlot_MAIN_HAND = lamp;
		equipSlot_PANTS = pants;
		equipSlot_SHIRT = shirt;
		equipSlot_SHOES = shoes;
		inv.put("lamp", lamp);
		inv.put("shirt", shirt);
		inv.put("pants", pants);
		inv.put("shoes", shoes);
		this.inventory = inv;
		this.maxWeight = 100;
		this.currentWeight = 3.5;//weight of currently carried items = (1 + 0.5 + 1.5 + 0.5)
		this.currentRoomId = 0;
		this.inInventory = false;
		this.visitedRooms = new HashMap<Integer,GameRoom>();
		this.roomVolumeIndex = 4;
		GameItem sword = new GameItem("sword",2.5,5,1,-1,-1,"null","A rusty iron sabre.",2,GameItem.MAIN_HAND,false);
		List<GameItem> skeletonInventory = new ArrayList<GameItem>();
		skeletonInventory.add(sword);
		GameEnemy skeleton = new GameEnemy("skeleton","A walking pile of bones, skull and all.",5,5,sword.name,10,skeletonInventory,10,0);
		Map<String,List<GameEnemy>> roomEnemies = new HashMap<String,List<GameEnemy>>();
		List<GameEnemy> enemies = new ArrayList<GameEnemy>();
		enemies.add(skeleton);
		roomEnemies.put(skeleton.name,enemies);
		List<GameVolume> roomVolumes = GameRoom.makeRoomVolume(3, 3, 1);
		GameVolume centerVolume = new GameVolume(2,2,1,true,(byte)0b11_11_11,false,(byte)0b00_00,null,roomEnemies,null);
		roomVolumes.set(4, centerVolume);
		GameRoom startRoom = new GameRoom(0,"This is a very spooky-looking room. Lots of cobwebs and dust and things like that.",3,3,1,roomVolumes);
		this.visitedRooms.put(startRoom.roomId, startRoom);
		//additional fields
		this.armorClass = 10;
		this.damageReduction = 0;
		this.magicResistance = 0;
		this.initiative = 0;
		Map<String,GameSpell> spells = new HashMap<String,GameSpell>();
		GameSpell firebolt = new GameSpell("firebolt",0,0,"Fires a small bolt of flame from your extended fingertip.",4,1,0,GameSpell.damage_FIRE);
		spells.put(firebolt.name, firebolt);
		//repeat for more Spells player should know from the start...
		this.spellbook = spells;
		this.currentSp = 10;
		this.maxSp = 10;
		this.attackBonusMelee = 0;
		this.attackBonusRanged = 0;
		this.attackBonusMagic = 0;
	}

	/**
	 * GameState(int,int,int,int,String,Map<String,GameItem>,double,double,int,boolean,Map<Integer,GameRoom>) Sets the current Game State to the passes in variables
	 * @param currentXp The Player's current eXperience Points
	 * @param currentLevel The Player's current Level
	 * @param currentHp The Player's current Hit Points
	 * @param maxHp The Player's maximum Hit Points
	 * @param heldItem The Player's currently held Item
	 * @param inventory The Player's current Inventory
	 * @param currentWeight The Player's current carry weight
	 * @param maxWeight The Player's maximum carry weight
	 * @param currentRoomId The current Room number the Player is in
	 * @param inInventory Indicates whether the Player is looking through their inventory
	 * @param visitedRooms All Rooms the Player has visited & their current contents
	 * @param roomVolumeIndex The index of the GameVolume in which the Player is currently located
	 */
	protected GameState(int currentXp, int currentLevel, int currentHp, int maxHp, String heldItem, Map<String,GameItem> inventory, double currentWeight, double maxWeight, int currentRoomId, boolean inInventory, Map<Integer,GameRoom> visitedRooms, int roomVolumeIndex) {
		this.currentXp = currentXp;
		this.currentLevel = currentLevel;
		this.currentHp = currentHp;
		this.maxHp = maxHp;
		this.heldItem = heldItem;
		this.inventory = inventory;
		this.currentWeight = currentWeight;
		this.maxWeight = maxWeight;
		this.currentRoomId = currentRoomId;
		this.inInventory = inInventory;
		this.visitedRooms = visitedRooms;
		this.roomVolumeIndex = roomVolumeIndex;
	}

	/**
	 * GameState(GameState,int,int,int,int,int,Map<String,GameSpell>,int,int,int,int,int) Constructor with additional parameters
	 * @param gameState The GameState to add these additional parameter values to
	 * @param armorClass The Player's current AC
	 * @param damageReduction The Player's current DR
	 * @param magicResistance The Player's current MR
	 * @param initiative The Player's current Initiative
	 * @param spellbook The Player's Spells
	 * @param currentSp The Player's current SP
	 * @param maxSp The Player's maximum SP
	 * @param attackBonusMelee The Player's current Melee Attack Bonus
	 * @param attackBonusRanged The Player's current Ranged Attack Bonus
	 * @param attackBonusMagic The Player's currenct Magic Attack Bonus
	 */
	protected GameState(GameState gameState, int armorClass, int damageReduction, int magicResistance, int initiative, Map<String,GameSpell> spellbook, int currentSp, int maxSp, int attackBonusMelee, int attackBonusRanged, int attackBonusMagic) {
		this.currentXp = gameState.currentXp;
		this.currentLevel = gameState.currentLevel;
		this.currentHp = gameState.currentHp;
		this.maxHp = gameState.maxHp;
		this.heldItem = gameState.heldItem;
		this.inventory = gameState.inventory;
		this.currentWeight = gameState.currentWeight;
		this.maxWeight = gameState.maxWeight;
		this.currentRoomId = gameState.currentRoomId;
		this.inInventory = gameState.inInventory;
		this.visitedRooms = gameState.visitedRooms;
		this.roomVolumeIndex = gameState.roomVolumeIndex;
		this.armorClass = armorClass;
		this.damageReduction = damageReduction;
		this.magicResistance = magicResistance;
		this.initiative = initiative;
		if(spellbook == null || spellbook.isEmpty()) {
			this.spellbook = new HashMap<String,GameSpell>();
		} else {
			this.spellbook = spellbook;
		}
		this.currentSp = currentSp;
		this.maxSp = maxSp;
		this.attackBonusMelee = attackBonusMelee;
		this.attackBonusRanged = attackBonusRanged;
		this.attackBonusMagic = attackBonusMagic;
	}

//	/**
//	 * pickUpItem(GameItem) Adds the specified GameItem to the Player's Inventory
//	 * @param item The GameItem to be picked up
//	 */
//	protected void pickUpItem(GameItem item) {
//		if(inventory.containsKey(item.name)) {//if Player already has at least 1 of the item
//			GameItem curItem = inventory.get(item.name);
//			if(curItem.maxUses > 0) {//if item is a use-based item
//				curItem.remainingUses = Math.min(curItem.maxUses, curItem.remainingUses + item.remainingUses);//refill the item's uses
//				inventory.put(item.name, curItem);//save over the old item with the updated item
//			} else {//item is quantity-based
//				curItem.quantity += item.quantity;//add quantity of new item to quantity of old item
//				inventory.put(item.name, curItem);//save over the old item with the updated item
//				currentWeight += item.getTotalWeight();
//			}
//		} else {//Player does not yet carry this item
//			inventory.put(item.name, item);//add new item to inventory
//		}
//		GameRoom room = GameRoom.lookup(currentRoomId);
//		Map<String,GameItem> roomItems = room.items;//get list of items in the current room
//		roomItems.remove(item);//remove the picked up item from item list
//		room.items = roomItems;//save the room's items
//		visitedRooms.replace(currentRoomId, room);
//	}

	/**
	 * distanceBetween(GameVolume,GameVolume) Calculates the distance between the center of the FROM volume and the center of the TO volume, in increments of GameVolume
	 * @param from The GameVolume that represents the starting location
	 * @param to The GameVolume that represents the target location
	 * @return The distance between the FROM and the TO volumes, in double precision, or 0 if the 2 volumes are the same
	 */
	protected static double distanceBetween(GameVolume from, GameVolume to) {
		double distance = -1;
		if(from != null && to != null) {
			if(from.xPos == to.xPos && from.yPos == to.yPos && from.zPos == to.zPos) {//volumes are the same
				distance = 0;
			} else {
				distance = Math.sqrt(Math.pow(from.xPos - to.xPos, 2) + Math.pow(from.yPos - to.yPos, 2) + Math.pow(from.zPos - to.zPos, 2));
			}
		}
		return distance;
	}

	/**
	 * distanceSquaredBetween(GameVolume,GameVolume) Calculates the distance, squared, between the center of the FROM volume and the center of the TO volume, in increments of GameVolume. This method saves the processing power of calculating the SquareRoot of the Sum of xDistance + yDIstance + zDistance.
	 * @param from The GameVolume that represents the starting location
	 * @param to The GameVolume that represents the target location
	 * @return The distance, squared, between the FROM and the TO volumes, in double precision, or 0 if the 2 volumes are the same
	 */
	protected static double distanceSquaredBetween(GameVolume from, GameVolume to) {
		double distance = -1;
		if(from != null && to != null) {
			if(from.xPos == to.xPos && from.yPos == to.yPos && from.zPos == to.zPos) {//volumes are the same
				distance = 0;
			} else {
				distance = Math.pow(from.xPos - to.xPos, 2) + Math.pow(from.yPos - to.yPos, 2) + Math.pow(from.zPos - to.zPos, 2);
			}
		}
		return distance;
	}

	/**
	 * getTraversalPath(GameVolume,GameVolume) Returns a list of GameVolumes which lie along the calculated traversal path. The traversal path should be a relatively straight line between the 2 GameVolume parameters.
	 * @param from The GameVolume from which to start traversal
	 * @param to The GameVolume to which to find a traversal path
	 * @return A list of GameVolumes that lie along the traversal path
	 */
	protected List<GameVolume> getTraversalPath(GameVolume from, GameVolume to) {
		List<GameVolume> traversalVolumes = new ArrayList<GameVolume>();
		if(from.xPos == to.xPos && from.yPos == to.yPos && from.zPos == to.zPos) {
			return traversalVolumes;//NO TRAVEL DIRECTION
		}
//		GameRoom room = this.visitedRooms.get(this.currentRoomId);
		GameVolume step = from;
		traversalVolumes.add(step);
		int xDistance = to.xPos - from.xPos;
		int yDistance = to.yPos - from.yPos;
		int zDistance = to.zPos - from.zPos;
		int totalSteps = Math.max(Math.abs(xDistance),Math.max(Math.abs(yDistance),Math.abs(zDistance)));
		int stepIndex = 0;
		while(stepIndex < totalSteps) {//while not at destination yet
			int nextTraversalDirection = getTraversalDirection(step,to,totalSteps,stepIndex,xDistance,yDistance,zDistance);
			step = getNextVolumeInDirection(step,nextTraversalDirection);
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
	protected List<Integer> getTraversalPathIndexes(GameVolume from, GameVolume to) {
		List<Integer> traversalPathIndexes = new ArrayList<Integer>();
		if(from.xPos == to.xPos && from.yPos == to.yPos && from.zPos == to.zPos) {
			return traversalPathIndexes;//NO TRAVEL DIRECTION
		}
		GameRoom room = this.visitedRooms.get(this.currentRoomId);
		GameVolume step = from;
		int xDistance = to.xPos - from.xPos;
		int yDistance = to.yPos - from.yPos;
		int zDistance = to.zPos - from.zPos;
		int totalSteps = Math.max(Math.abs(xDistance),Math.max(Math.abs(yDistance),Math.abs(zDistance)));
		int stepIndex = 0;
		while(stepIndex < totalSteps) {//while not at destination yet
			int nextTraversalDirection = getTraversalDirection(step,to,totalSteps,stepIndex,xDistance,yDistance,zDistance);
			step = getNextVolumeInDirection(step,nextTraversalDirection);
			traversalPathIndexes.add(((step.zPos - 1) * room.width * room.length) + ((step.yPos - 1) * room.length) + (step.xPos - 1));
			stepIndex++;
		}
		return traversalPathIndexes;
	}

	/**
	 * getTraversalDirection(GameVolume,GameVolume,int,int,int,int,int) Given 2 GameVolumes within the same GameRoom, the number of 
	 * traversal steps already taken, and the total distance to travel on the X, Y, and Z axes, this method will return the next 
	 * direction of traversal from the indicated GameVolume
	 * @param from The GameVolume from which to begin calculating traversal direction
	 * @param to The GameVolume to which the traversal will navigate to
	 * @param totalSteps The total number of steps to occur from the original startign GameVolume to the ultimate end GameVolume
	 * @param stepIndex The current traversal index (how many steps have already been taken + 1)
	 * @param xDistance The total distance to travel on the X axis
	 * @param yDistance The total distance to travel on the Y axis
	 * @param zDistance The total distance to travel on the Z axis
	 * @return A byte representation of the next direction of traversal between the 2 indicated GameVolumes
	 */
	protected static byte getTraversalDirection(GameVolume from, GameVolume to, int totalSteps, int stepIndex, int xDistance, int yDistance, int zDistance) {
		byte direction = (1 << 0 | 1 << 2 | 1 << 4);
		//IF the ratio of stepIndex:totalSteps IS EQUIVALENT TO the ratio of axisIndex:axisDistance THEN travel on that Axis
		boolean travelX = xDistance > 0 && ((stepIndex + 1) * xDistance / totalSteps) >= (1 + (stepIndex * xDistance / totalSteps));
		boolean travelY = yDistance > 0 && ((stepIndex + 1) * yDistance / totalSteps) >= (1 + (stepIndex * yDistance / totalSteps));
		boolean travelZ = zDistance > 0 && ((stepIndex + 1) * zDistance / totalSteps) >= (1 + (stepIndex * zDistance / totalSteps));
		int xDir = 1;
		int yDir = 1;
		int zDir = 1;
		if(travelX && xDistance > 0) {
			xDir = 2;
		} else if(travelX && xDistance < 0) {
			xDir = 0;
		}
		if(travelY && yDistance > 0) {
			yDir = 2;
		} else if(travelY && yDistance < 0) {
			yDir = 0;
		}
		if(travelZ && zDistance > 0) {
			zDir = 2;
		} else if(travelZ && zDistance < 0) {
			zDir = 0;
		}
		direction = (byte) (xDir << 0 | yDir << 2 | zDir << 4);
		return direction;
	}

	/**
	 * getGeneralTraversalDirection(GameVolume,GameVolume) Given a START and END GameVolume, returns the general direction from START to END. 
	 * In practice, this method will return a direction which represents whether there will be any movement on the X, Y, and Z axes and whether
	 * that movement will be NEGATIVE or POSITIVE. (A value of 0b00 represents NEGATIVE movement. A value of 0b01 represents NO movement, and
	 * a value of 0b10 represents POSITIVE movement. Each of these values are bit-shifted into a representation of 0bZZ_YY_XX value.
	 * @param from The GameVolume from which to START traversal
	 * @param to The GameVolume in which to END traversal
	 * @return Byte representation of the general direction of traversal between the START and END GameVolumes
	 */
	protected static byte getGeneralTraversalDirection(GameVolume from, GameVolume to) {
		byte direction = 0b11_11_11;//ZZ_YY_XX
		int xDistance = to.xPos - from.xPos;
		int yDistance = to.yPos - from.yPos;
		int zDistance = to.zPos - from.zPos;
		if(xDistance < 0) {
			direction &= 0b11_11_00;
		} else if(xDistance > 0) {
			direction &= 0b11_11_10;
		} else if(xDistance == 0) {
			direction &= 0b11_11_01;
		}
		if(yDistance < 0) {
			direction &= 0b11_00_11;
		} else if(yDistance > 0) {
			direction &= 0b11_10_11;
		} else if(yDistance == 0) {
			direction &= 0b11_01_11;
		}
		if(zDistance < 0) {
			direction &= 0b00_11_11;
		} else if(zDistance > 0) {
			direction &= 0b10_11_11;
		} else if(zDistance == 0) {
			direction &= 0b01_11_11;
		}
		return direction;
	}

	/**
	 * getNextVolumeInDirection(GameVolume,int) Returns the GameVolume which is located in the specified direction from the specified GameVolume
	 * @param from The GameVolume to use as a baseline
	 * @param direction The direction in which to look for the next Volume
	 * @return The GameVolume which is adjacent to the specified GameVolume in the specified direction, or null if none is found
	 */
	protected GameVolume getNextVolumeInDirection(GameVolume from, int direction) {
		GameRoom room = this.visitedRooms.get(this.currentRoomId);
		GameVolume nextVolume = null;
		int newX = from.xPos;
		int newY = from.yPos;
		int newZ = from.zPos;
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
		if(newX < 0 || newX > room.length || newY < 0 || newY > room.width || newZ < 0 || newZ > room.height) {//index out of bounds
			nextVolume = null;
		} else {
			nextVolume = room.interior.get(((newZ - 1) * room.width * room.length) + ((newY - 1) * room.length) + (newX - 1));
		}
		return nextVolume;
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
	protected boolean canAttack(GameRoom room, GameVolume startVolume, GameVolume targetVolume, int targetIndex, String targetType, String targetName, String method, String implement) {
		boolean canAttack = false;
		double targetDistance = distanceSquaredBetween(startVolume,targetVolume);
		GameItem item = GameItem.lookup(implement);
		if(item == null) {//implement not a valid GameItem (fists, for example)
			if(targetDistance > 3) {//target is > 1 GameVolume away in any direction
				return false;
			}
		}
//		List<Integer> attackVectorIndices = getTraversalPath(startVolume,targetVolume);
		List<GameVolume> traversalVolumes = getTraversalPath(startVolume,targetVolume);
		List<Byte> traversalDirections = new ArrayList<Byte>();
		GameVolume start = traversalVolumes.get(0);
		GameVolume end = traversalVolumes.get(traversalVolumes.size() - 1);
		for(int i = 0; i < traversalVolumes.size() - 1; i++) {
			GameVolume from = traversalVolumes.get(i);
			GameVolume to = traversalVolumes.get(i + 1);
			traversalDirections.add(getTraversalDirection(from,to,traversalVolumes.size() - 1,i,end.xPos - start.xPos,end.yPos - start.yPos,end.zPos - start.zPos));
		}
		switch (targetType) {
		case "enemy"://attack an ENEMY
			GameEnemy targetEnemy = targetVolume.enemies.get(targetName).get(targetIndex);
			
			break;
		case "object"://attack an OBJECT
			GameObject targetObject = targetVolume.objects.get(targetName).get(targetIndex);
			
			break;
		case "item"://attack an ITEM
			GameItem targetItem = targetVolume.items.get(targetName).get(targetIndex);
			
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
	 * canCarry Checks to see if the weight of an item (or stack of items) can be carried by the Player
	 * @param itemWeight Weight of the item (or stack of items) which the Player wants to pick up
	 * @return True or False
	 */
	protected boolean canCarry(double itemWeight) {
		return itemWeight + this.currentWeight <= this.maxWeight;
	}

	/**
	 * canTraverse(int,int,int,int) Determines whether the Player can enter or exit the GameVolume indicated by x,y,z position, can be passed through.
	 * @param xPos The X coordinate of the indicated volume. Should be > 0.
	 * @param yPos The Y coordinate of the indicated volume. Should be > 0.
	 * @param zPos The Z coordinate of the indicated volume. Should be > 0.
	 * @param direction The direction of traversal through the indicated volume.
	 * @return An indication of whether the Player can traverse through the indicated volume in the indicated direction.
	 */
	protected boolean canTraverse(int xPos, int yPos, int zPos, byte direction) {
		if(xPos > 0 && yPos > 0 && zPos > 0 && direction >= 0 && direction <= 42 && this.visitedRooms.get(this.currentRoomId).interior != null && this.visitedRooms.get(this.currentRoomId).interior.size() >= (xPos * yPos * zPos)) {
			GameRoom room = this.visitedRooms.get(this.currentRoomId);
			GameVolume volume = this.visitedRooms.get(this.currentRoomId).interior.get(((zPos - 1) * room.width * room.length) + ((yPos - 1) * room.length) + (xPos - 1));
			boolean canPass = false;
			byte passableDirs = volume.passableDirs;
			//TODO: FINISH THIS METHOD!
			return volume.canTraverse && canPass;
		}
		return false;
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
	protected String traverse(int xPos, int yPos, int zPos, byte direction, int distance) {
		GameRoom room = this.visitedRooms.get(this.currentRoomId);
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
			if(this.canTraverse(newX, newY, newZ, direction)) {
				this.roomVolumeIndex = ((newZ * room.width * room.length) + (newY * room.length) + newX);
				distanceLeft--;
			} else {
				canPath = false;
				moveString = "You move " + (distance - distanceLeft) + " meter(s) " + getDirectionName(direction) + ".";
			}
		}
		return moveString;
	}

	/**
	 * getDirectionName(byte) Returns the lexical representation of the indicated direction
	 * @param direction Byte representation of an [X,Y,Z] direction
	 * @return String containing the lexical representation of the given direction
	 */
	protected static String getDirectionName(byte direction) {
		String moveDir = "";
		int xDir = ((byte) direction & 0b00_00_11) >> 0;//extract the X axis direction using a bit mask
		int yDir = ((byte) direction & 0b00_11_00) >> 2;//extract the Y axis direction using a bit mask
		int zDir = ((byte) direction & 0b11_00_00) >> 4;//extract the Z axis direction using a bit mask
		if(yDir == 0) {
			moveDir = "SOUTH";
		} else if(yDir == 2) {
			moveDir = "NORTH";
		}
		if(xDir != 1) {
			if(xDir == 0) {
				moveDir += "WEST";
			} else if(xDir == 2) {
				moveDir += "EAST";
			}
		}
		if(zDir != 1) {
			if(moveDir.length() > 0) {
				moveDir += " & ";
			}
			if(zDir == 0) {
				moveDir += "DOWN";
			} else if(zDir == 2) {
				moveDir += "UP";
			}
		}
		return moveDir;
	}

	/**
	 * getBytes() Converts the current GameState into a byte[] so it can be written to disk
	 * @return byte[] containing the current GameState
	 */
	protected byte[] getBytes() {
	/*
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
		String returnString = this.currentXp + "$" + this.currentLevel + "$" + this.currentHp + "$" + this.maxHp + "$" + this.heldItem + "$";
		if(this.inventory != null && this.inventory.size() > 0) {
			List<GameItem> inventoryItems = new ArrayList<GameItem>();
			inventoryItems.addAll(this.inventory.values());
			for(int i = 0; i < inventoryItems.size(); i++) {
				returnString += inventoryItems.get(i).getByteString();
				if(i < inventoryItems.size() - 1) {
					returnString += "¶";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "$" + this.currentWeight + "$" + this.maxWeight + "$" + this.currentRoomId + "$" + this.inInventory + "$";
		if(this.visitedRooms != null && this.visitedRooms.size() > 0) {
			List<GameRoom> seenRooms = new ArrayList<GameRoom>();
			seenRooms.addAll(this.visitedRooms.values());
			for(int i = 0; i < seenRooms.size(); i++) {
				returnString += seenRooms.get(i).getByteString();
				if(i < seenRooms.size() - 1) {
					returnString += "€";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "$" + this.roomVolumeIndex + "$" + this.armorClass + "$" + this.damageReduction + "$" + this.magicResistance + "$" + this.initiative + "$";
		if(this.spellbook != null && this.spellbook.size() > 0) {
			List<GameSpell> playerSpells = new ArrayList<GameSpell>();
			playerSpells.addAll(this.spellbook.values());
			for(int i = 0; i < playerSpells.size(); i++) {
				returnString += playerSpells.get(i).getByteString();
				if(i < playerSpells.size() - 1) {
					returnString += "¶";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "$" + this.currentSp + "$" + this.maxSp + "$" + this.attackBonusMelee + "$" + this.attackBonusRanged + "$" + this.attackBonusMagic;
		return returnString.getBytes();
	}

	/**
	 * parseBytes(byte[]) Parses an array of bytes and translates them into a GameState to be loaded
	 * @param bytes The byte array containing the loaded GameState
	 * @return GameState contained in the byte array
	 */
	protected static GameState parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] stateData = s.split("\\$");
		GameState gameState = null;
		if(stateData.length <= 22) {
			if(stateData.length < 12) {
				return gameState;
			} else {
				String[] inventory = stateData[5].split("¶");
				String[] visitedRooms = stateData[10].split("€");
				Map<String,GameItem> inv = new HashMap<String,GameItem>();
				for(String itemData: inventory) {
					GameItem item = GameItem.parseBytes(itemData.getBytes());
					if(item != null) {
						inv.put(item.name, item);
					}
				}
				Map<Integer,GameRoom> rooms = new HashMap<Integer,GameRoom>();
				for(String roomData: visitedRooms) {
					GameRoom room = GameRoom.parseBytes(roomData.getBytes());
					if(room != null) {
						rooms.put(room.roomId, room);
					}
				}
				gameState = new GameState(
						Integer.parseInt(stateData[0]),//currentXp
						Integer.parseInt(stateData[1]),//currentLevel
						Integer.parseInt(stateData[2]),//currentHp
						Integer.parseInt(stateData[3]),//maxHp
						stateData[4],//heldItem
						inv,//inventory
						Double.parseDouble(stateData[6]),//currentWeight
						Double.parseDouble(stateData[7]),//maxWeight
						Integer.parseInt(stateData[8]),//currentRoomId
						Boolean.parseBoolean(stateData[9]),//inInventory
						rooms,//visitedRooms
						Integer.parseInt(stateData[11])//roomVolumeIndex
				);
			}
			if(stateData.length == 12) {
				return gameState;
			}
			String[] playerSpells = stateData[16].split("¶");
			Map<String,GameSpell> spells = new HashMap<String,GameSpell>();
			for(String spellData: playerSpells) {
				GameSpell spell = GameSpell.parseBytes(spellData.getBytes());
				if(spell != null) {
					spells.put(spell.name,spell);
				}
			}
			gameState = new GameState(
				gameState,
				Integer.parseInt(stateData[12]),//armorClass
				Integer.parseInt(stateData[13]),//damageReduction
				Integer.parseInt(stateData[14]),//magicResistance
				Integer.parseInt(stateData[15]),//initiative
				spells,//spellBook
				Integer.parseInt(stateData[17]),//currentSp
				Integer.parseInt(stateData[18]),//maxSp
				Integer.parseInt(stateData[19]),//attackBonusMelee
				Integer.parseInt(stateData[20]),//attackBonusRanged
				Integer.parseInt(stateData[21])//attackBonusMagic
			);
		}
		return gameState;
	}
}
