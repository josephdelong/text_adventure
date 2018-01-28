package game.asset.room;

import game.asset.GameAsset;
import game.asset.actor.GameEnemy;
import game.asset.effect.GameEffect;
import game.asset.item.GameItem;
import game.asset.object.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameVolume A class that represents a 3D volume of GameRoom interior
 * @author Joseph DeLong
 */
public class GameVolume extends GameAsset {
	private int xPos;
	private int yPos;
	private int zPos;
	private boolean canTraverse;//if the Player can pass into this volume
	private byte passableDirs;//0b00_00_00: represents the Z+Z-_Y+Y-_X+X- boolean values for whether the volume is passable in the indicated direction
	private boolean isIncline;
	private byte inclineDir;//0b00_00 - 0b10_10: represents the Y+Y-_X+X- indicators of whether the ground is sloped UP in the indicated direction
	private Map<String,List<GameObject>> objects;
	private Map<String,List<GameEnemy>> enemies;
	private Map<String,List<GameItem>> items;
	private Map<String,List<GameEffect>> effects;//fog, light source?, water, hazardous terrain, traps, triggers?

	/**
	 * GameVolume() Constructor used for CREATION MODE //might not ever use
	 */
	public GameVolume() {
		this.setAssetType(GameAsset.assetType_ROOM_VOLUME);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setXPos(0);
		this.setYPos(0);
		this.setZPos(0);
		this.setCanTraverse(false);
		this.setPassableDirs((byte)0b00_00_00);
		this.setIsIncline(false);
		this.setInclineDir((byte)0b00_00);
		this.setObjects(null);
		this.setEnemies(null);
		this.setItems(null);
		this.setEffects(null);
	}

	/**
	 * GameVolume(int,int,int,boolean,boolean[],boolean,int,List<GameObject>,List<GameEnemy>,List<GameItem>)
	 */
	public GameVolume(
			int xPos, int yPos, int zPos, boolean canTraverse, byte passableDirs, boolean isIncline, byte inclineDir, Map<String,List<GameObject>> objects, 
			Map<String,List<GameEnemy>> enemies, Map<String,List<GameItem>> items, Map<String,List<GameEffect>> effects) {
		this.setAssetType(GameAsset.assetType_OBJECT);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setXPos(xPos);
		this.setYPos(yPos);
		this.setZPos(zPos);;
		this.setCanTraverse(canTraverse);
		this.setPassableDirs(passableDirs);
		this.setIsIncline(isIncline);
		this.setInclineDir(inclineDir);
		this.setObjects(objects);
		this.setEnemies(enemies);
		this.setItems(items);
		this.setEffects(effects);
	}

	/**
	 * GameVolume(GameVolume) Given a base GameVolume, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the template's values directly, but are still a value-for-value direct copy of the template.<br>
	 * <br>
	 * NOTE: This constructor supports the GameRoom(GameRoom) constructor, as it makes a 1-to-1 INSTANCE copy of a specified
	 * GameVolume, so that the parent GameRoom's interior volume won't be changed in any way by an INSTANCE of the Room in-game.
	 * @param volumeTemplate The GameVolume TEMPLATE to make an INSTANCE copy of
	 */
	public GameVolume(GameVolume volumeTemplate) {
		this.setAssetType(GameAsset.assetType_OBJECT);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setXPos(Integer.valueOf(volumeTemplate.getXPos()));
		this.setYPos(Integer.valueOf(volumeTemplate.getYPos()));
		this.setZPos(Integer.valueOf(volumeTemplate.getXPos()));
		this.setCanTraverse(Boolean.valueOf(volumeTemplate.canTraverse()));
		this.setPassableDirs(Byte.valueOf(volumeTemplate.getPassableDirs()));
		this.setIsIncline(Boolean.valueOf(volumeTemplate.isIncline()));
		this.setInclineDir(Byte.valueOf(volumeTemplate.getInclineDir()));
		Map<String,List<GameObject>> objects = null;
		if(volumeTemplate.getObjects() != null && !volumeTemplate.getObjects().isEmpty()) {
			objects = new HashMap<String,List<GameObject>>();
			for(List<GameObject> list: volumeTemplate.getObjects().values()) {
				String key = "";
				List<GameObject> objList = new ArrayList<GameObject>();
				for(GameObject object: list) {
					if(key == "") {
						key = object.getName().toLowerCase();
					}
					objList.add(new GameObject(object));
				}
				objects.put(key,objList);
			}
		} else {
			objects = new HashMap<String,List<GameObject>>();
		}
		this.setObjects(objects);
		Map<String,List<GameEnemy>> enemies = null;
		if(volumeTemplate.getEnemies() != null && !volumeTemplate.getEnemies().isEmpty()) {
			enemies = new HashMap<String,List<GameEnemy>>();
			for(List<GameEnemy> list: volumeTemplate.getEnemies().values()) {
				String key = "";
				List<GameEnemy> enemyList = new ArrayList<GameEnemy>();
				for(GameEnemy enemy: list) {
					if(key == "") {
						key = enemy.getName().toLowerCase();
					}
					enemyList.add(new GameEnemy(enemy));
				}
				enemies.put(key,enemyList);
			}
		} else {
			enemies = new HashMap<String,List<GameEnemy>>();
		}
		this.setEnemies(enemies);
		Map<String,List<GameItem>> items = null;
		if(volumeTemplate.getItems() != null && !volumeTemplate.getItems().isEmpty()) {
			items = new HashMap<String,List<GameItem>>();
			for(List<GameItem> list: volumeTemplate.getItems().values()) {
				String key = "";
				List<GameItem> itemList = new ArrayList<GameItem>();
				for(GameItem item: list) {
					if(key == "") {
						key = item.getName().toLowerCase();
					}
					itemList.add(new GameItem(item));
				}
				items.put(key,itemList);
			}
		} else {
			items = new HashMap<String,List<GameItem>>();
		}
		this.setItems(items);
		Map<String,List<GameEffect>> effects = null;
		if(volumeTemplate.getEffects() != null && !volumeTemplate.getEffects().isEmpty()) {
			effects = new HashMap<String,List<GameEffect>>();
			for(List<GameEffect> list: volumeTemplate.getEffects().values()) {
				String key = "";
				List<GameEffect> effectList = new ArrayList<GameEffect>();
				for(GameEffect effect: list) {
					if(key == "") {
						key = effect.getName().toLowerCase();
					}
					effectList.add(new GameEffect(effect));
				}
				effects.put(key,effectList);
			}
		} else {
			effects = new HashMap<String,List<GameEffect>>();
		}
		this.setEffects(effects);
	}

	public int getXPos() {return this.xPos;}
	public int getYPos() {return this.yPos;}
	public int getZPos() {return this.zPos;}
	public boolean canTraverse() {return this.canTraverse;}
	public byte getPassableDirs() {return this.passableDirs;}
	public boolean isIncline() {return this.isIncline;}
	public byte getInclineDir() {return this.inclineDir;}
	public Map<String, List<GameObject>> getObjects() {return this.objects;}
	public Map<String, List<GameEnemy>> getEnemies() {return this.enemies;}
	public Map<String, List<GameItem>> getItems() {return this.items;}
	public Map<String, List<GameEffect>> getEffects() {return this.effects;}

	public void setXPos(int xPos) {this.xPos = xPos;}
	public void setYPos(int yPos) {this.yPos = yPos;}
	public void setZPos(int zPos) {this.zPos = zPos;}
	public void setCanTraverse(boolean canTraverse) {this.canTraverse = canTraverse;}
	public void setPassableDirs(byte passableDirs) {this.passableDirs = passableDirs;}
	public void setIsIncline(boolean isIncline) {this.isIncline = isIncline;}
	public void setInclineDir(byte inclineDir) {this.inclineDir = inclineDir;}
	public void setObjects(Map<String, List<GameObject>> objects) {this.objects = objects;}
	public void setEnemies(Map<String, List<GameEnemy>> enemies) {this.enemies = enemies;}
	public void setItems(Map<String, List<GameItem>> items) {this.items = items;}
	public void setEffects(Map<String, List<GameEffect>> effects) {this.effects = effects;}

	/**
	 * containsObject(String)
	 * @param name The name of the GameObject to search for in this GameRoom
	 * @return Whether the specified GameObject is present in this GameRoom
	 */
	public boolean containsObject(String name) {
		return this.getObjects() != null && !this.getObjects().isEmpty() && this.getObjects().containsKey(name);
	}

	/**
	 * containsEnemy(String)
	 * @param name The name of the GameEnemy to search for in this GameRoom
	 * @return Whether the specified GameEnemy is present in this GameRoom
	 */
	public boolean containsEnemy(String name) {
		return this.getEnemies() != null && !this.getEnemies().isEmpty() && this.getEnemies().containsKey(name);
	}

	/**
	 * containsItem(String)
	 * @param name The name of the GameItem to search for in this GameRoom
	 * @return Whether the specified GameItem is present in this GameRoom
	 */
	public boolean containsItem(String name) {
		return this.getItems() != null && !this.getItems().isEmpty() && this.getItems().containsKey(name);
	}

	/**
	 * containsEffect(String)
	 * @param name The name of the GameEffect to search for in this GameRoom
	 * @return Whether the specified GameEffect is present in this GameRoom
	 */
	public boolean containsEffect(String name) {
		return this.getEffects() != null && !this.getEffects().isEmpty() && this.getEffects().containsKey(name);
	}

	/**
	 * addObject(GameObject) Add the specified GameObject to the GameVolume's Objects
	 * @param object The GameObject to add
	 */
	public void addObject(GameObject object) {
		Map<String,List<GameObject>> volumeObjects = this.getObjects();
		if(volumeObjects == null) {
			volumeObjects = new HashMap<String,List<GameObject>>();
		}
		if(volumeObjects.containsKey(object.getName())) {
			List<GameObject> objects = volumeObjects.get(object.getName());
			objects.add(object);
			volumeObjects.put(object.getName(),objects);
		} else {
			List<GameObject> objects = new ArrayList<GameObject>();
			objects.add(object);
			volumeObjects.put(object.getName(),objects);
		}
		this.setObjects(volumeObjects);
	}

	/**
	 * addEnemy(GameEnemy) Add the specified GameEnemy to the GameVolume's Enemies
	 * @param enemy The GameEnemy to add
	 */
	public void addEnemy(GameEnemy enemy) {
		Map<String,List<GameEnemy>> volumeEnemies = this.getEnemies();
		if(volumeEnemies == null) {
			volumeEnemies = new HashMap<String,List<GameEnemy>>();
		}
		if(volumeEnemies.containsKey(enemy.getName())) {
			List<GameEnemy> enemies = volumeEnemies.get(enemy.getName());
			enemies.add(enemy);
			volumeEnemies.replace(enemy.getName(),enemies);
		} else {
			List<GameEnemy> enemies = new ArrayList<GameEnemy>();
			enemies.add(enemy);
			volumeEnemies.put(enemy.getName(),enemies);
		}
		this.setEnemies(volumeEnemies);
	}

	/**
	 * addItem(GameItem) Add the specified GameItem to the GameVolume's Items
	 * @param item The GameItem to add
	 */
	public void addItem(GameItem item) {
		Map<String,List<GameItem>> volumeItems = this.getItems();
		if(volumeItems == null) {
			volumeItems = new HashMap<String,List<GameItem>>();
		}
		if(volumeItems.containsKey(item.getName())) {
			List<GameItem> items = volumeItems.get(item.getName());
			items.add(item);
			volumeItems.replace(item.getName(),items);
		} else {
			List<GameItem> items = new ArrayList<GameItem>();
			items.add(item);
			volumeItems.put(item.getName(),items);
		}
		this.setItems(volumeItems);
	}

	/**
	 * addEffect(GameEffect) Add the specified GameEffect to the GameVolume's Effects
	 * @param effect The GameEffect to add
	 */
	public void addEffect(GameEffect effect) {
		Map<String,List<GameEffect>> volumeEffects = this.getEffects();
		if(volumeEffects == null) {
			volumeEffects = new HashMap<String,List<GameEffect>>();
		}
		if(volumeEffects.containsKey(effect.getName())) {
			List<GameEffect> items = volumeEffects.get(effect.getName());
			items.add(effect);
			volumeEffects.replace(effect.getName(),items);
		} else {
			List<GameEffect> items = new ArrayList<GameEffect>();
			items.add(effect);
			volumeEffects.put(effect.getName(),items);
		}
		this.setEffects(volumeEffects);
	}

	/**
	 * Returns a String representation of the Volume & its properties
	 * @return
	 */
	public String getByteString() {
		String returnString = this.getAssetType() + "°" + this.getUid() + "°" + this.getXPos() + "°" + this.getYPos() + "°" + this.getXPos() + "°" 
				+ this.canTraverse() + "°" + this.getPassableDirs() + "°" + this.isIncline() + "°" + this.getInclineDir() + "°";
		if(this.getObjects() != null && this.getObjects().size() > 0) {
			List<GameObject> roomObjects = new ArrayList<GameObject>();
			for(List<GameObject> l: this.getObjects().values()) {
				roomObjects.addAll(l);
			}
			for(int i = 0; i < roomObjects.size(); i++) {
				returnString += roomObjects.get(i).getByteString();
				if(i < roomObjects.size() - 1) {
					returnString += "‡";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "°";
		if(this.getEnemies() != null && this.getEnemies().size() > 0) {
			List<GameEnemy> roomEnemies = new ArrayList<GameEnemy>();
			for(List<GameEnemy> l: this.getEnemies().values()) {
				roomEnemies.addAll(l);
			}
			for(int i = 0; i < roomEnemies.size(); i++) {
				returnString += roomEnemies.get(i).getByteString();
				if(i < roomEnemies.size() - 1) {
					returnString += "‡";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "°";
		if(this.getItems() != null && this.getItems().size() > 0) {
			List<GameItem> roomItems = new ArrayList<GameItem>();
			for(List<GameItem> l: this.getItems().values()) {
				roomItems.addAll(l);
			}
			for(int i = 0; i < roomItems.size(); i++) {
				returnString += roomItems.get(i).getByteString();
				if(i < roomItems.size() - 1) {
					returnString += "¶";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "°";
		if(this.getEffects() != null && this.getEffects().size() > 0) {
			List<GameEffect> roomEffects = new ArrayList<GameEffect>();
			for(List<GameEffect> l: this.getEffects().values()) {
				roomEffects.addAll(l);
			}
			for(int i = 0; i < roomEffects.size(); i++) {
				returnString += roomEffects.get(i).getByteString();
				if(i < roomEffects.size() - 1) {
					returnString += "¶";
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
	public byte[] getBytes() {
		String returnString = this.getByteString();
		return returnString.getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameVolume
	 * @param bytesThe byte array to read
	 * @return GameVolume containing the data parsed from the byte array
	 */
	public static GameVolume parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] spaceData = s.split("°");
		if(spaceData.length != 13) {
			return null;
		}
		String[] spaceObjects = spaceData[9].split("‡");
		String[] spaceEnemies = spaceData[10].split("‡");
		String[] spaceItems = spaceData[11].split("¶");
		String[] spaceEffects = spaceData[12].split("¶");
		Map<String,List<GameObject>> allObjects = new HashMap<String,List<GameObject>>();
		for(String objectData: spaceObjects) {
			GameObject object = GameObject.parseBytes(objectData.getBytes(),false);
			if(object != null) {
				List<GameObject> objects = allObjects.get(object.getName());
				if(objects == null) {
					objects = new ArrayList<GameObject>();
				}
				objects.add(object);
				allObjects.put(object.getName(),objects);
			}
		}
		Map<String,List<GameEnemy>> allEnemies = new HashMap<String,List<GameEnemy>>();
		for(String enemyData: spaceEnemies) {
			GameEnemy enemy = GameEnemy.parseBytes(enemyData.getBytes(),false);
			if(enemy != null) {
				List<GameEnemy> enemies = allEnemies.get(enemy.getName());
				if(enemies == null) {
					enemies = new ArrayList<GameEnemy>();
				}
				enemies.add(enemy);
				allEnemies.put(enemy.getName(),enemies);
			}
		}
		Map<String,List<GameItem>> allItems = new HashMap<String,List<GameItem>>();
		for(String itemData: spaceItems) {
			GameItem item = GameItem.parseBytes(itemData.getBytes(),false);
			if(item != null) {
				List<GameItem> items = allItems.get(item.getName());
				if(items == null) {
					items = new ArrayList<GameItem>();
				}
				items.add(item);
				allItems.put(item.getName(),items);
			}
		}
		Map<String,List<GameEffect>> allEffects = new HashMap<String,List<GameEffect>>();
		for(String effectData: spaceEffects) {
			GameEffect effect = GameEffect.parseBytes(effectData.getBytes(),false);
			if(effect != null) {
				List<GameEffect> effects = allEffects.get(effect.getName());
				if(effects == null) {
					effects = new ArrayList<GameEffect>();
				}
				effects.add(effect);
				allEffects.put(effect.getName(),effects);
			}
		}
		GameVolume volume = new GameVolume();
		volume.setAssetType(spaceData[0]);//assetType
		volume.setUid(spaceData[1]);//uid
		volume.setXPos(Integer.parseInt(spaceData[2]));//xPos
		volume.setYPos(Integer.parseInt(spaceData[3]));//yPos
		volume.setZPos(Integer.parseInt(spaceData[4]));//zPos
		volume.setCanTraverse(Boolean.parseBoolean(spaceData[5]));//canTraverse
		volume.setPassableDirs(Byte.parseByte(spaceData[6]));//passableDirs
		volume.setIsIncline(Boolean.parseBoolean(spaceData[7]));//isIncline
		volume.setInclineDir(Byte.parseByte(spaceData[8]));//inclineDir
		volume.setObjects(allObjects);//objects
		volume.setEnemies(allEnemies);//enemies
		volume.setItems(allItems);//items
		volume.setEffects(allEffects);//effects
		return volume;
	}
}
