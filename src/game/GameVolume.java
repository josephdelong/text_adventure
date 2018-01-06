package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameVolume A class that represents a 3D volume of GameRoom interior
 * @author Joseph DeLong
 */
public class GameVolume {
	protected int xPos;
	protected int yPos;
	protected int zPos;
	protected boolean canTraverse;//if the Player can pass into this volume
	protected byte passableDirs;//0b00_00_00: represents the Z+Z-_Y+Y-_X+X- boolean values for whether the volume is passable in the indicated direction
	protected boolean isIncline;
	protected byte inclineDir;//0b00_00 - 0b10_10: represents the Y+Y-_X+X- indicators of whether the ground is sloped UP in the indicated direction
	protected Map<String,List<GameObject>> objects;
	protected Map<String,List<GameEnemy>> enemies;
	protected Map<String,List<GameItem>> items;
//	protected Map<String,List<GameEffect>> effects;//fog, light source?, water, hazardous terrain, traps, triggers?

	/**
	 * GameVolume() Constructor used for CREATION MODE //might not ever use
	 */
	protected GameVolume() {
		this.xPos = 0;
		this.yPos = 0;
		this.zPos = 0;
		this.canTraverse = false;
		this.passableDirs = 0b00_00_00;
		this.isIncline = false;
		this.inclineDir = 0b00_00;
		this.objects = null;
		this.enemies = null;
		this.items = null;
//		this.effects = null;
	}

	/**
	 * GameVolume(int,int,int,boolean,boolean[],boolean,,int,List<GameObject>,List<GameEnemy>,List<GameItem>)
	 * @param xPos The X coordinate of this volume within the GameRoom
	 * @param yPos The Y coordinate of this volume within the GameRoom
	 * @param zPos The Z coordinate of this volume within the GameRoom
	 * @param canTraverse Whether this volume can be passed through
	 * @param passableDirs The directions that an actor can pass into or out form this volume
	 * @param isIncline Whether this volume has an inclined (sloped) floor
	 * @param inclineDir The direction towards which the incline slopes UP
	 * @param objects Any Game Objects which are in this volume of space
	 * @param enemies Any Game Enemies which are in this volume of space
	 * @param items Any Game Items which are in this volume of space
	 * @param effects Any Game Effects which are in this volume of space
	 */
	protected GameVolume(int xPos, int yPos, int zPos, boolean canTraverse, byte passableDirs, boolean isIncline, byte inclineDir, Map<String,List<GameObject>> objects, Map<String,List<GameEnemy>> enemies, Map<String,List<GameItem>> items/*, Map<String,List<GameEffect>> effects*/) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.canTraverse = canTraverse;
		this.passableDirs = passableDirs;
		this.isIncline = isIncline;
		this.inclineDir = inclineDir;
		this.objects = objects;
		this.enemies = enemies;
		this.items = items;
//		this.effects = effects;
	}

	/**
	 * containsObject(String)
	 * @param name The name of the GameObject to search for in this GameRoom
	 * @return Whether the specified GameObject is present in this GameRoom
	 */
	protected boolean containsObject(String name) {
		return this.objects != null && !this.objects.isEmpty() && this.objects.containsKey(name);
	}

	/**
	 * containsEnemy(String)
	 * @param name The name of the GameEnemy to search for in this GameRoom
	 * @return Whether the specified GameEnemy is present in this GameRoom
	 */
	protected boolean containsEnemy(String name) {
		return this.enemies != null && !this.enemies.isEmpty() && this.enemies.containsKey(name);
	}

	/**
	 * containsItem(String)
	 * @param name The name of the GameItem to search for in this GameRoom
	 * @return Whether the specified GameItem is present in this GameRoom
	 */
	protected boolean containsItem(String name) {
		return this.items != null && !this.items.isEmpty() && this.items.containsKey(name);
	}

	/**
	 * addObject(GameObject) Add the specified GameObject to the GameVolume's Objects
	 * @param object The GameObject to add
	 */
	protected void addObject(GameObject object) {
		if(this.objects == null) {
			this.objects = new HashMap<String,List<GameObject>>();
		}
		if(this.objects.containsKey(object.name)) {
			List<GameObject> objects = this.objects.get(object.name);
			objects.add(object);
			this.objects.put(object.name,objects);
		} else {
			List<GameObject> objects = new ArrayList<GameObject>();
			objects.add(object);
			this.objects.put(object.name,objects);
		}
	}

	/**
	 * addEnemy(GameEnemy) Add the specified GameEnemy to the GameVolume's Enemies
	 * @param enemy The GameEnemy to add
	 */
	protected void addEnemy(GameEnemy enemy) {
		if(this.enemies == null) {
			this.enemies = new HashMap<String,List<GameEnemy>>();
		}
		if(this.enemies.containsKey(enemy.name)) {
			List<GameEnemy> enemies = this.enemies.get(enemy.name);
			enemies.add(enemy);
			this.enemies.replace(enemy.name,enemies);
		} else {
			List<GameEnemy> enemies = new ArrayList<GameEnemy>();
			enemies.add(enemy);
			this.enemies.put(enemy.name,enemies);
		}
	}

	/**
	 * addItem(GameItem) Add the specified GameItem to the GameVolume's Items
	 * @param item The GameItem to add
	 */
	protected void addItem(GameItem item) {
		if(this.items == null) {
			this.items = new HashMap<String,List<GameItem>>();
		}
		if(this.items.containsKey(item.name)) {
			List<GameItem> items = this.items.get(item.name);
			items.add(item);
			this.items.replace(item.name,items);
		} else {
			List<GameItem> items = new ArrayList<GameItem>();
			items.add(item);
			this.items.put(item.name,items);
		}
	}

	/**
	 * Returns a String representation of the Volume & its properties
	 * @return
	 */
	protected String getByteString() {
		String returnString = this.xPos + "°" + this.yPos + "°" + this.zPos + "°" + this.canTraverse + "°" + this.passableDirs + "°" + this.isIncline + "°" + inclineDir + "°";
		if(this.objects != null && this.objects.size() > 0) {
			List<GameObject> roomObjects = new ArrayList<GameObject>();
			for(List<GameObject> l: this.objects.values()) {
				roomObjects.addAll(l);
			}
			for(int i = 0; i < roomObjects.size(); i++) {
				if(i == roomObjects.size() - 1) {
					returnString += roomObjects.get(i).getByteString();
				} else {
					returnString += roomObjects.get(i).getByteString() + "‡";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "°";
		if(this.enemies != null && this.enemies.size() > 0) {
			List<GameEnemy> roomEnemies = new ArrayList<GameEnemy>();
			for(List<GameEnemy> l: this.enemies.values()) {
				roomEnemies.addAll(l);
			}
			for(int i = 0; i < roomEnemies.size(); i++) {
				if(i == roomEnemies.size() - 1) {
					returnString += roomEnemies.get(i).getByteString();
				} else {
					returnString += roomEnemies.get(i).getByteString() + "‡";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "°";
		if(this.items != null && this.items.size() > 0) {
			List<GameItem> roomItems = new ArrayList<GameItem>();
			for(List<GameItem> l: this.items.values()) {
				roomItems.addAll(l);
			}
			for(int i = 0; i < roomItems.size(); i++) {
				if(i == roomItems.size() - 1) {
					returnString += roomItems.get(i).getByteString();
				} else {
					returnString += roomItems.get(i).getByteString() + "¶";
				}
			}
		} else {
			returnString += "null";
		}
		return returnString;
	}

	/**
	 * getBytes() Returns a representation of the Volume in an array of bytes
	 * @return
	 */
	protected byte[] getBytes() {
		String returnString = this.getByteString();
		return returnString.getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameVolume
	 * @param bytesThe byte array to read
	 * @return GameVolume containing the data parsed from the byte array
	 */
	protected static GameVolume parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] spaceData = s.split("°");
		if(spaceData.length != 10) {
			return null;
		}
		String[] spaceObjects = spaceData[7].split("‡");
		String[] spaceEnemies = spaceData[8].split("‡");
		String[] spaceItems = spaceData[9].split("¶");
		Map<String,List<GameObject>> allObjects = new HashMap<String,List<GameObject>>();
		for(String objectData: spaceObjects) {
			GameObject object = GameObject.parseBytes(objectData.getBytes());
			if(object != null) {
				List<GameObject> objects = allObjects.get(object.name);
				if(objects == null) {
					objects = new ArrayList<GameObject>();
				}
				objects.add(object);
				allObjects.put(object.name,objects);
			}
		}
		Map<String,List<GameEnemy>> allEnemies = new HashMap<String,List<GameEnemy>>();
		for(String enemyData: spaceEnemies) {
			GameEnemy enemy = GameEnemy.parseBytes(enemyData.getBytes());
			if(enemy != null) {
				List<GameEnemy> enemies = allEnemies.get(enemy.name);
				if(enemies == null) {
					enemies = new ArrayList<GameEnemy>();
				}
				enemies.add(enemy);
				allEnemies.put(enemy.name,enemies);
			}
		}
		Map<String,List<GameItem>> allItems = new HashMap<String,List<GameItem>>();
		for(String itemData: spaceItems) {
			GameItem item = GameItem.parseBytes(itemData.getBytes());
			if(item != null) {
				List<GameItem> items = allItems.get(item.name);
				if(items == null) {
					items = new ArrayList<GameItem>();
				}
				items.add(item);
				allItems.put(item.name,items);
			}
		}
		return new GameVolume(
				Integer.parseInt(spaceData[0]),//xPos
				Integer.parseInt(spaceData[1]),//yPos
				Integer.parseInt(spaceData[2]),//zPos
				Boolean.parseBoolean(spaceData[3]),//canTraverse
				Byte.parseByte(spaceData[4]),//passableDirs
				Boolean.parseBoolean(spaceData[5]),//isIncline
				Byte.parseByte(spaceData[6]),//inclineDir
				allObjects,//objects
				allEnemies,//enemies
				allItems//items
		);
	}
}
