package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameObject Represents an interactive Object in the Game, such as a Container, Door, Switch, Lever, etc.
 * @author Joseph DeLong
 */
public class GameObject {
	protected static Map<String,GameObject> objects;

	protected String name;
	protected String description;
	protected boolean isContainer;
	protected boolean isOpen;
	protected boolean isLocked;
	protected List<GameItem> contents;
	protected boolean isMovable;
	protected boolean isBreakable;
	protected int currentHp;
	protected int maxHp;

	/**
	 * Constructor used for CREATION MODE
	 */
	protected GameObject() {
		this.name = "";
		this.description = "";
		this.isContainer = false;
		this.isOpen = false;
		this.isLocked = false;
		this.contents = new ArrayList<GameItem>();
		this.isMovable = false;
		this.isBreakable = false;
		this.currentHp = -1;
		this.maxHp = -1;
	}

	/**
	 * GameObject(String,String,boolean,boolean,List<GameItem>) Constructs an Instance of GameObject with the values specified
	 * @param name The name of the Object
	 * @param decription A detailed description of the Object
	 * @param isContainer Indicates whether the Object may contain Items
	 * @param isOpen Indicates whether the Object is open
	 * @param isLocked Indicates whether the Object is locked
	 * @param contents The List of Items inside this Object
	 * @param isMovable Indicates whether this Object may be moved by the player
	 * @param isBreakable Indicates whether this Object may be broken open
	 * @param currentHp How many Hit Points this Object has, if breakable
	 * @param maxHp The maximum total Hit Points this Object has, if breakable
	 */
	protected GameObject(String name, String description, boolean isContainer, boolean isOpen, boolean isLocked, List<GameItem> contents, boolean isMovable, boolean isBreakable, int currentHp, int maxHp) {
		this.name = name;
		this.description = description;
		this.isContainer = isContainer;
		this.isOpen = isOpen;
		this.isLocked = isLocked;
		if(contents == null) {
			this.contents = new ArrayList<GameItem>();
		} else {
			this.contents = contents;
		}
		this.isMovable = isMovable;
		this.isBreakable = isBreakable;
		this.currentHp = currentHp;
		this.maxHp = maxHp;
		GameObject.add(this);
	}

	/**
	 * add(GameObject) Adds the specified GameObject to the available Objects in the Game, if not already present
	 * @param object
	 */
	protected static void add(GameObject object) {
		if(objects == null) {
			objects = new HashMap<String,GameObject>();
		}
		if(!objects.containsKey(object.name)) {
			objects.put(object.name.toLowerCase(),object);
		}
	}

	/**
	 * breakObject() Destroys this GameObject, spilling any contents to the floor of the current Room
	 */
	protected void breakObject() {
		//TODO: implement this method?
	}

	/**
	 * lookup(String) Returns the specified GameObject if it exists in the Game
	 * @param name Name of the Object to look up
	 * @return The GameObject, if found, that matches the specified name
	 */
	protected static GameObject lookup(String name) {
		if(objects == null || objects.isEmpty()) {
			return null;
		}
		return objects.get(name);
	}

	/**
	 * getByteString() Returns a String representation of the Object and its properties
	 * @return
	 */
	protected String getByteString() {
		String returnString = this.name + "§" + this.description + "§" + this.isContainer + "§" + this.isOpen + "§" + this.isLocked + "§";
		if(this.contents != null && this.contents.size() > 0) {
			List<GameItem> invItems = this.contents;
			for(int i = 0; i < invItems.size(); i++) {
				if(i == invItems.size() - 1) {
					returnString += invItems.get(i).getByteString();
				} else {
					returnString += invItems.get(i).getByteString() + "¶";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "§" + this.isMovable + "§" + this.isBreakable + "§" + this.currentHp + "§" + this.maxHp;
		return returnString;
	}

	/**
	 * getBytes() Returns a representation of the Object in an array of bytes
	 * @return
	 */
	protected byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameObject
	 * @param bytes The byte array to read
	 * @return GameObject containing the data parsed from the byte array
	 */
	protected static GameObject parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] objectData = s.split("§");
		if(objectData.length != 10) {
			return null;
		}
		String[] invData = objectData[5].split("¶");
		List<GameItem> inv = new ArrayList<GameItem>();
		for(String itemData: invData) {
			GameItem item = GameItem.parseBytes(itemData.getBytes());
			if(item != null) {
				inv.add(item);
			}
		}
		return new GameObject(
				objectData[0],//name
				objectData[1],//description
				Boolean.parseBoolean(objectData[2]),//isContainer
				Boolean.parseBoolean(objectData[3]),//isOpen
				Boolean.parseBoolean(objectData[4]),//isLocked
				inv,//contents
				Boolean.parseBoolean(objectData[6]),//isMovable
				Boolean.parseBoolean(objectData[7]),//isBreakable
				Integer.parseInt(objectData[8]),//currentHp
				Integer.parseInt(objectData[9])//maxHp
		);
	}
}
