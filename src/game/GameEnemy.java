package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameEnemy Class that represents an Enemy in the Game
 * @author Joseph DeLong
 */
public class GameEnemy {
	protected static Map<String,GameEnemy> enemies;

	protected String name;
	protected String description;
	protected int currentHp;
	protected int maxHp;
	protected String heldItem;
	protected int xpValue;
	protected List<GameItem> inventory;
	protected int armorClass;
	protected int damageReduction;
	
	//TODO: implement onDeath()

	/**
	 * GameEnemy() Constructor used for CREATION MODE
	 */
	protected GameEnemy() {
		this.name = "";
		this.description = "";
		this.currentHp = -1;
		this.maxHp = -1;
		this.heldItem = null;
		this.xpValue = 0;
		this.inventory = new ArrayList<GameItem>();
		this.armorClass = 0;
		this.damageReduction = 0;
	}

	/**
	 * GameEnemy(String,String,int,int,String,int,List<GameItem>) Creates a new Enemy with the specified stats
	 * @param name Name of the Enemy
	 * @param description A detailed description of the Enemy
	 * @param currentHp The Enemy's current Hit Points
	 * @param maxHp The Enemy's maximum Hit Points
	 * @param heldItem The Enemy's currently held Item
	 * @param xpValue How much eXperience Points will be rewarded the player if this Enemy is defeated
	 * @param inventory The current inventory of this Enemy
	 * @param armorClass The AC of this Enemy
	 * @param damageReduction
	 */
	protected GameEnemy(String name, String description, int currentHp, int maxHp, String heldItem, int xpValue, List<GameItem> inventory, int armorClass, int damageReduction) {
		this.name = name;
		this.description = description;
		this.currentHp = currentHp;
		this.maxHp = maxHp;
		this.heldItem = heldItem;
		this.xpValue = xpValue;
		if(inventory == null) {
			this.inventory = new ArrayList<GameItem>();
		} else {
			this.inventory = inventory;
		}
		this.armorClass = armorClass;
		this.damageReduction = damageReduction;
		/*
		if(spellBook == null) {
			this.spellBook = new ArrayList<GameSpell>();
		} else {
			this.spellBook = spellBook;
		}
		 */
		GameEnemy.add(this);
	}

	/**
	 * add(GameEnemy) Adds the specified GameEnemy to the available Enemies in the Game, if not already present
	 * @param enemy
	 */
	protected static void add(GameEnemy enemy) {
		if(enemies == null) {
			enemies = new HashMap<String,GameEnemy>();
		}
		if(!enemies.containsKey(enemy.name)) {
			enemies.put(enemy.name.toLowerCase(),enemy);
		}
	}

	/**
	 * Look up the GameEnemy by its Name in the list of current Game Enemies
	 * @param enemyName The Name of the Enemy to find
	 * @return Game Enemy identified by the specified name, if found
	 */
	protected static GameEnemy lookup(String enemyName) {
		if(enemies == null || enemies.isEmpty()) {
			return null;
		}
		return enemies.get(enemyName);
	}

	/**
	 * getByteString() Returns a String representation of the Enemy and its properties
	 * @return
	 */
	protected String getByteString() {
		String returnString = this.name + "§" + this.description + "§" + this.currentHp + "§" + this.maxHp + "§" + this.heldItem + "§" + this.xpValue + "§";
		if(this.inventory != null && this.inventory.size() > 0) {
			List<GameItem> invItems = this.inventory;
			for(int i = 0; i < invItems.size(); i++) {
				returnString += invItems.get(i).getByteString();
				if(i < invItems.size() - 1) {
					returnString += "¶";
				}
			}
		} else {
			returnString += "null";
		}
		returnString += "§" + this.armorClass + "§" + this.damageReduction;
		return returnString;
	}

	/**
	 * getBytes() Returns a representation of the Enemy in an array of bytes
	 * @return
	 */
	protected byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameEnemy
	 * @param bytes The byte array to read
	 * @return GameEnemy containing the data parsed from the byte array
	 */
	protected static GameEnemy parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] enemyData = s.split("§");
		if(enemyData.length != 9) {
			return null;
		}
		String [] invData = enemyData[6].split("¶");
		List<GameItem> inv = new ArrayList<GameItem>();
		for(String itemData: invData) {
			GameItem item = GameItem.parseBytes(itemData.getBytes());
			if(item != null) {
				inv.add(item);
			}
		}
		return new GameEnemy(
				enemyData[0],//name
				enemyData[1],//description
				Integer.parseInt(enemyData[2]),//currentHp
				Integer.parseInt(enemyData[3]),//maxHp
				enemyData[4],//heldItem
				Integer.parseInt(enemyData[5]),//xpValue
				inv,//inventory
				Integer.parseInt(enemyData[7]),//armorClass
				Integer.parseInt(enemyData[8])//damageReduction
		);
	}
}
