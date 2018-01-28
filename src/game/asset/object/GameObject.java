package game.asset.object;

import game.asset.GameAsset;
import game.asset.item.GameItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameObject Represents an interactive Object in the Game, such as a Container, Door, Switch, Lever, etc.
 * @author Joseph DeLong
 */
public class GameObject extends GameAsset {
	private static Map<String,GameObject> objectTemplates;

	private String objectType;
	private String name;
	private String description;
	private boolean isContainer;
	private boolean isOpen;
	private boolean isLocked;
	private List<GameItem> contents;
	private boolean isMovable;
	private boolean isBreakable;
	private int currentHp;
	private int maxHp;

	public static final String objectType_OBJECT = "OBJECT";
	public static final String objectType_ACTIVATOR = "ACTIVATOR";
	public static final String objectType_CONTAINER = "CONTAINER";
	public static final String objctsType_PORTAL = "PORTAL";

	/**
	 * Constructor used for TEMPLATE CREATION MODE
	 */
	public GameObject() {
		this.setAssetType(GameAsset.assetType_OBJECT);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setObjectType(GameObject.objectType_OBJECT);
		this.setName("");
		this.setDescription("");
		this.setIsContainer(false);
		this.setIsOpen(false);
		this.setIsLocked(false);
		this.setContents(new ArrayList<GameItem>());
		this.setIsMovable(false);
		this.setIsBreakable(false);
		this.setCurrentHp(0);
		this.setMaxHp(0);
	}

	/**
	 * GameObject(String,String,String,String,boolean,boolean,List<GameItem>,boolean,boolean,int,int) Constructs a GameObject with the values specified
	 */
	public GameObject(String assetType, String uid, String objectType, String name, String description, boolean isContainer, boolean isOpen, boolean isLocked, List<GameItem> contents, boolean isMovable, boolean isBreakable, int currentHp, int maxHp) {
		this.setAssetType(assetType);
		this.setUid(uid);

		this.setObjectType(objectType);
		this.setName(name);
		this.setDescription(description);
		this.setIsContainer(isContainer);
		this.setIsOpen(isOpen);
		this.setIsLocked(isLocked);
		if(contents == null) {
			this.setContents(new ArrayList<GameItem>());
		} else {
			this.setContents(contents);
		}
		this.setIsMovable(isMovable);
		this.setIsBreakable(isBreakable);
		this.setCurrentHp(currentHp);
		this.setMaxHp(maxHp);
	}

	/**
	 * GameObject(GameObject) Given a base GameObject, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the template's values directly, but are still a value-for-value direct copy of the template.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameObject.lookup(objectName)</b></code> in order to create in-game
	 * instances of the Object Template (stored in <code>GameObject.objectTemplates</code>). This will prevent the Instances from overwriting the
	 * Templates (which is undesired).
	 * @param objectTemplate The GameObject TEMPLATE to make an INSTANCE copy of
	 */
	public GameObject(GameObject objectTemplate) {
		this.setAssetType(new String(objectTemplate.getAssetType()));
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setObjectType(new String(objectTemplate.getObjectType()));
		this.setName(new String(objectTemplate.getName()));
		this.setDescription(new String(objectTemplate.getDescription()));
		this.setIsContainer(Boolean.valueOf(objectTemplate.isContainer()));
		this.setIsOpen(Boolean.valueOf(objectTemplate.isOpen()));
		this.setIsLocked(Boolean.valueOf(objectTemplate.isLocked()));
		List<GameItem> contents = null;
		if(objectTemplate.getContents() != null && objectTemplate.getContents().size() > 0) {
			contents = new ArrayList<GameItem>();
			for(GameItem item: objectTemplate.getContents()) {
				contents.add(new GameItem(item));
			}
		} else {
			contents = new ArrayList<GameItem>();
		}
		this.setContents(contents);
		this.setIsMovable(Boolean.valueOf(objectTemplate.isMovable()));
		this.setIsBreakable(Boolean.valueOf(objectTemplate.isBreakable()));
		this.setCurrentHp(Integer.valueOf(objectTemplate.getCurrentHp()));
		this.setMaxHp(Integer.valueOf(objectTemplate.getMaxHp()));
	}

	public static Map<String, GameObject> getObjectTemplates() {return objectTemplates;}
	
	public String getObjectType() {return this.objectType;}
	public String getName() {return this.name;}
	public String getDescription() {return this.description;}
	public boolean isContainer() {return this.isContainer;}
	public boolean isOpen() {return this.isOpen;}
	public boolean isLocked() {return this.isLocked;}
	public List<GameItem> getContents() {return this.contents;}
	public boolean isMovable() {return this.isMovable;}
	public boolean isBreakable() {return this.isBreakable;}
	public int getCurrentHp() {return this.currentHp;}
	public int getMaxHp() {return this.maxHp;}

	public void setObjectType(String objectType) {this.objectType = objectType;}
	public void setName(String name) {this.name = name;}
	public void setDescription(String description) {this.description = description;}
	public void setIsContainer(boolean isContainer) {this.isContainer = isContainer;}
	public void setIsOpen(boolean isOpen) {this.isOpen = isOpen;}
	public void setIsLocked(boolean isLocked) {this.isLocked = isLocked;}
	public void setContents(List<GameItem> contents) {this.contents = contents;}
	public void setIsMovable(boolean isMovable) {this.isMovable = isMovable;}
	public void setIsBreakable(boolean isBreakable) {this.isBreakable = isBreakable;}
	public void setCurrentHp(int currentHp) {this.currentHp = currentHp;}
	public void setMaxHp(int maxHp) {this.maxHp = maxHp;}

	/**
	 * add(GameObject) Adds the specified GameObject to the available Objects in the Game, if not already present
	 * @param object
	 */
	public static void add(GameObject object) {
		if(objectTemplates == null) {
			objectTemplates = new HashMap<String,GameObject>();
		}
		if(!objectTemplates.containsKey(object.name)) {
			objectTemplates.put(object.name.toLowerCase(),object);
		}
	}

	/**
	 * lookup(String) Returns the specified GameObject if it exists in the Game
	 * @param name Name of the Object to look up
	 * @return The GameObject, if found, that matches the specified name
	 */
	public static GameObject lookup(String name) {
		if(objectTemplates == null || objectTemplates.isEmpty()) {
			return null;
		}
		return objectTemplates.get(name.toLowerCase());
	}

	/**
	 * getBytes() Returns a representation of the Object in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * getByteString() Returns a String representation of the Object and its properties
	 * @return
	 */
	public String getByteString() {
		String returnString = this.getAssetType() + "§" + this.getUid() + "§" + this.getObjectType() + "§" + this.getName() + "§" + this.getDescription() 
				+ "§" + this.isContainer() + "§" + this.isOpen() + "§" + this.isLocked() + "§";
		if(this.getContents() != null && this.getContents().size() > 0) {
			List<GameItem> invItems = this.getContents();
			for(int i = 0; i < invItems.size(); i++) {
				returnString += invItems.get(i).getByteString();
				if(i < invItems.size() - 1) {
					returnString += "¶";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "§" + this.isMovable() + "§" + this.isBreakable() + "§" + this.getCurrentHp() + "§" + this.getMaxHp();
		return returnString;
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameObject
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameObject contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameObject containing the data parsed from the byte array
	 */
	public static GameObject parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] objectData = s.split("§");
		if(objectData != null && objectData.length != 13 && objectData[0].equals(GameAsset.assetType_OBJECT)) {
			return null;
		}
		String[] invData = objectData[8].split("¶");
		List<GameItem> contents = new ArrayList<GameItem>();
		for(String itemData: invData) {
			GameItem item = GameItem.parseBytes(itemData.getBytes(),false);
			if(item != null) {
				contents.add(item);
			}
		}
		GameObject object = new GameObject();
		object.setAssetType(objectData[0]);//assetType
		object.setUid(objectData[1]);//uid
		object.setObjectType(objectData[2]);//objectType
		object.setName(objectData[3]);//name
		object.setDescription(objectData[4]);//description
		object.setIsContainer(Boolean.parseBoolean(objectData[5]));//isContainer
		object.setIsOpen(Boolean.parseBoolean(objectData[6]));//isOpen
		object.setIsLocked(Boolean.parseBoolean(objectData[7]));//isLocked
		object.setContents(contents);//contents
		object.setIsMovable(Boolean.parseBoolean(objectData[9]));//isMovable
		object.setIsBreakable(Boolean.parseBoolean(objectData[10]));//isBreakable
		object.setCurrentHp(Integer.parseInt(objectData[11]));//currentHp
		object.setMaxHp(Integer.parseInt(objectData[12]));//maxHp
		return object;
	}
}
