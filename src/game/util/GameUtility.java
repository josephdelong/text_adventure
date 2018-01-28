package game.util;

import game.GameCombat;
import game.GameState;
import game.asset.GameAsset;
import game.asset.GameDiplomacy;
import game.asset.GameItemModifier;
import game.asset.actor.GameActor;
import game.asset.actor.GameEnemy;
import game.asset.actor.GameNpc;
import game.asset.actor.GamePlayer;
import game.asset.item.GameItem;
import game.asset.object.GameObject;
import game.asset.room.GameRoom;
import game.asset.room.GameVolume;
import game.asset.spell.GameSpell;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Class to hold all utility functions to be used across Game Asset Classes
 * @author Joseph DeLong
 */
public class GameUtility {

	/**
	 * compress(byte[]) Compresses an array of bytes<br>
	 * Compression / Decompression methods are not my original work. These were developed and published<br>
	 * BY: Ralf Quebbemann<br>
	 * AT: https://dzone.com/articles/how-compress-and-uncompress
	 * @param data The byte array to compress
	 * @return A compressed version of the input byte array
	 * @throws IOException
	 */
	public static byte[] compress(byte[] data) throws IOException {
		Deflater deflater = new Deflater();
		deflater.setLevel(Deflater.BEST_COMPRESSION);//Added to maximize the compression (save the most disk space possible)
		deflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer); // returns the generated code... index
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
//		System.out.println("Original: " + data.length);
//		System.out.println("Compressed: " + output.length);
		return output;
	}

	/**
	 * decompress(byte[]) Decompresses an array of bytes<br>
	 * Compression / Decompression methods are not my original work. These were developed and published<br>
	 * BY: Ralf Quebbemann<br>
	 * AT: https://dzone.com/articles/how-compress-and-uncompress
	 * @param data The byte array to decompress
	 * @return A decompressed version of the input byte array
	 * @throws IOException
	 * @throws DataFormatException
	 */
	public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
//		System.out.println("Original: " + data.length);
//		System.out.println("Uncompressed: " + output.length);
		return output;
	}

	/**
	 * loadTemplates Initializes the Game World. Loads into memory all persisted GameEnemy, GameItem, GameObject, GameRoom, GameSpell
	 * @throws IOException If the File is not found, not readable, etc.
	 * @throws DataFormatException 
	 */
	public static void loadTemplates() throws IOException, DataFormatException {
		File resourceFilePath = new File("./res");
		if(resourceFilePath.exists() && resourceFilePath.isDirectory()) {
			File[] resourceFiles = resourceFilePath.listFiles();
			for(File f: resourceFiles) {
				byte[] fileData = null;
				fileData = decompress(readFile(f));
				fileData = Base64.getDecoder().decode(fileData);
				String[] assetData = new String(fileData).split("¦");
				switch (f.getName()) {
				case "GameEnemy":
					for(String s: assetData) {
						GameEnemy.parseBytes(s.getBytes(),true);
					}
					break;
				case "GameItem":
					for(String s: assetData) {
						GameItem.parseBytes(s.getBytes(),true);
					}
					break;
				case "GameItemModifier":
					for(String s: assetData) {
						GameItemModifier.parseBytes(s.getBytes(),true);
					}
					break;
				case "GameNpc":
					for(String s: assetData) {
						GameNpc.parseBytes(s.getBytes(),true);
					}
					break;
				case "GamePlayer":
					for(String s: assetData) {
						GamePlayer.parseBytes(s.getBytes(),true);
					}
					break;
				case "GameObject":
					for(String s: assetData) {
						GameObject.parseBytes(s.getBytes(),true);
					}
					break;
				case "GameRoom":
					for(String s: assetData) {
						GameRoom.parseBytes(s.getBytes()/*,true*/);
					}
					break;
				case "GameSpell":
					for(String s: assetData) {
						GameSpell.parseBytes(s.getBytes(),true);
					}
					break;
				default:
					System.err.println("Resource File not recognized: " + f.getName());
					break;
				}
			}
		} else {
			System.out.println("No resources to load. Skipping...");
		}	
	}

	/**
	 * saveTemplates() Saves all in-memory resources to their respective resourceFile
	 * @throws IOException If file is not found, is unreadable, etc.
	 */
	public static void saveTemplates() throws IOException {
		FileOutputStream fileOut;
		Map<String,GameEnemy> enemyTemplates = GameEnemy.getEnemyTemplates();
		if(enemyTemplates != null && !enemyTemplates.isEmpty()) {
			List<GameEnemy> enemies = new ArrayList<GameEnemy>();
			enemies.addAll(enemyTemplates.values());
			String dataString = "";
			for(int i = 0; i < enemies.size(); i++) {
				dataString += enemies.get(i).getByteString();
				if(i != enemies.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameEnemy");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		Map<String,GameItem> itemTemplates = GameItem.getItemTemplates();
		if(itemTemplates != null && !itemTemplates.isEmpty()) {
			List<GameItem> items = new ArrayList<GameItem>();
			items.addAll(itemTemplates.values());
			String dataString = "";
			for(int i = 0; i < items.size(); i++) {
				dataString += items.get(i).getByteString();
				if(i != items.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameItem");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		Map<String,GameItemModifier> modifierTemplates = GameItemModifier.getModifierTemplates();
		if(modifierTemplates != null && !modifierTemplates.isEmpty()) {
			List<GameItemModifier> prefixes = new ArrayList<GameItemModifier>();
			prefixes.addAll(modifierTemplates.values());
			String dataString = "";
			for(int i = 0; i < prefixes.size(); i++) {
				dataString += prefixes.get(i).getByteString();
				if(i != prefixes.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameItemModifier");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		Map<String,GameNpc> npcTemplates = GameNpc.getNpcTemplates();
		if(npcTemplates != null && !npcTemplates.isEmpty()) {
			List<GameNpc> npcs = new ArrayList<GameNpc>();
			npcs.addAll(npcTemplates.values());
			String dataString = "";
			for(int i = 0; i < npcs.size(); i++) {
				dataString += npcs.get(i).getByteString();
				if(i != npcs.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameNpc");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		Map<String,GamePlayer> allPlayers = GamePlayer.getAllPlayers();
		if(allPlayers != null && !allPlayers.isEmpty()) {
			List<GamePlayer> players = new ArrayList<GamePlayer>();
			players.addAll(allPlayers.values());
			String dataString = "";
			for(int i = 0; i < players.size(); i++) {
				dataString += players.get(i).getByteString();
				if(i != players.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GamePlayer");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		Map<String,GameObject> objectTemplates = GameObject.getObjectTemplates();
		if(objectTemplates != null && !objectTemplates.isEmpty()) {
			List<GameObject> objects = new ArrayList<GameObject>();
			objects.addAll(objectTemplates.values());
			String dataString = "";
			for(int i = 0; i < objects.size(); i++) {
				dataString += objects.get(i).getByteString();
				if(i != objects.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameObject");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		Map<Integer,GameRoom> roomTemplates = GameRoom.getRoomTemplates();
		if(roomTemplates != null && !roomTemplates.isEmpty()) {
			List<GameRoom> rooms = new ArrayList<GameRoom>();
			rooms.addAll(roomTemplates.values());
			String dataString = "";
			for(int i = 0; i < rooms.size(); i++) {
				dataString += rooms.get(i).getByteString();
				if(i != rooms.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameRoom");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		Map<String,GameSpell> spellTemplates = GameSpell.getSpellTemplates();
		if(spellTemplates != null && !spellTemplates.isEmpty()) {
			List<GameSpell> spells = new ArrayList<GameSpell>();
			spells.addAll(spellTemplates.values());
			String dataString = "";
			for(int i = 0; i < spells.size(); i++) {
				dataString += spells.get(i).getByteString();
				if(i != spells.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameSpell");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
	}

	/**
	 * readFile(File) Translates the specified File into a 64-bit encoded byte array, to be written to disk
	 * @param f The File to read
	 * @return byte[] containing the contents of the File f
	 * @throws IOException If the File can't be found, accessed, or read
	 */
	public static byte[] readFile(File f) throws IOException {
		List<Byte> allBytes = new ArrayList<Byte>();
		FileInputStream fileIn = new FileInputStream(f);
		DataInputStream in = new DataInputStream(new BufferedInputStream(fileIn));
		while(in.available() != 0) {
			allBytes.add(in.readByte());
		}
		byte[] byteArray = new byte[allBytes.size()];
		for(int i = 0; i < allBytes.size(); i++) {
			byteArray[i] = allBytes.get(i);
		}
		in.close();
		fileIn.close();
		return byteArray;
	}

	/**
	 * clearScreen() Clear the console contents (https://stackoverflow.com/questions/2979383/java-clear-the-console)
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void clearScreen() throws InterruptedException, IOException {
//		System.out.print("\033[H\033[2J");//no idea what this does code-wise
//		System.out.flush();
		final String osName = System.getProperty("os.name");
		if(osName.contains("Windows")) {//https://stackoverflow.com/questions/2979383/java-clear-the-console
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} else {
			Runtime.getRuntime().exec("clear");
		}
	}

	/**
	 * rollDie(int,int,boolean) Returns the result of a Die + the roller's bonus, if any
	 * @param dieVal The die to be rolled
	 * @param bonus Any bonus to this roll
	 * @param canCrit Indicates whether the check rolled against can be a CRITICAL roll.
	 * @return Integer value from 1 to [die] representing the result of the d20 roll. A CRIT_FAIL is indicated by a -1. A CRIT_SUCCESS is indicated by a 
	 */
	public static int rollDie(int dieVal, int bonus, boolean canCrit) {
		int roll = Double.valueOf(Math.random() * dieVal).intValue() + 1;//roll the die
		if(canCrit && roll == 1) {
			roll = -1;//CRIT_FAIL
		} else if(canCrit && roll == dieVal) {
			roll = 999;//CRIT_SUCCESS
		} else {//normal roll
			roll += bonus;
		}
		return roll;
	}

	/**
	 * getRequiredXpForNextLevel(int) Given an int value representing the Player's current level, this method calculates the XP needed (in TOTAL) to level up
	 * @param playerLevel The Player's current level
	 * @return An int representing the Player's next level XP threshold, or 0 if a negative number is passed in
	 * <br><br>
	 * NOTE: this method will experience (32-bit) Integer overflow if playerLevel > 232
	 */
	public static int getRequiredXpForNextLevel(int playerLevel) {
		int xp = 0;
		for(int i = 0; i <= playerLevel + 1; i++) {
			for(int x = 0; x <= i; x++) {
				xp += x;
			}
		}
		return 1000 * xp;
	}

	/**
	 * getPrintedLength(int) Given an integer value, this method returns its printed length (in number of characters)
	 * @param input The integer value to get printed length for
	 * @return An integer representing the printed length of the input integer value
	 */
	public static int getPrintedLength(int input) {
		return ("" + input).length();
	}

	/**
	 * distanceBetween(GameVolume,GameVolume) Calculates the distance between the center of the FROM volume and the center of the TO volume, in increments of GameVolume
	 * @param from The GameVolume that represents the starting location
	 * @param to The GameVolume that represents the target location
	 * @return The distance between the FROM and the TO volumes, in double precision, or 0 if the two volumes are the same
	 */
	public static double distanceBetween(GameVolume from, GameVolume to) {
		double distance = -1;
		if(from != null && to != null) {
			if(from.getXPos() == to.getXPos() && from.getYPos() == to.getYPos() && from.getZPos() == to.getZPos()) {//volumes are the same
				distance = 0;
			} else {
				distance = Math.sqrt(Math.pow(from.getXPos() - to.getXPos(), 2) + Math.pow(from.getYPos() - to.getYPos(), 2) + Math.pow(from.getZPos() - to.getZPos(), 2));
			}
		}
		return distance;
	}

	/**
	 * distanceSquaredBetween(GameVolume,GameVolume) Calculates the distance, squared, between the center of the FROM volume and the center of the TO volume, in increments of GameVolume. This method saves the processing power of calculating the SquareRoot of the Sum of xDistance + yDIstance + zDistance.
	 * @param from The GameVolume that represents the starting location
	 * @param to The GameVolume that represents the target location
	 * @return The distance, squared, between the FROM and the TO volumes, in double precision, or 0 if the two volumes are the same
	 */
	public static int distanceSquaredBetween(GameVolume from, GameVolume to) {
		double distance = -1;
		if(from != null && to != null) {
			if(from.getXPos() == to.getXPos() && from.getYPos() == to.getYPos() && from.getZPos() == to.getZPos()) {//volumes are the same
				distance = 0;
			} else {
				distance = Math.pow(from.getXPos() - to.getZPos(), 2) + Math.pow(from.getYPos() - to.getYPos(), 2) + Math.pow(from.getZPos() - to.getZPos(), 2);
			}
		}
		return Double.valueOf(distance).intValue();
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
	public static byte getTraversalDirection(GameVolume from, GameVolume to, int totalSteps, int stepIndex, int xDistance, int yDistance, int zDistance) {
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
	public static byte getGeneralTraversalDirection(GameVolume from, GameVolume to) {
		byte direction = 0b11_11_11;//ZZ_YY_XX
		int xDistance = to.getXPos() - from.getXPos();
		int yDistance = to.getYPos() - from.getYPos();
		int zDistance = to.getZPos() - from.getZPos();
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
	public static GameVolume getNextVolumeInDirection(GameRoom room, GameVolume from, int direction) {
		GameVolume nextVolume = null;
		int newX = from.getXPos();
		int newY = from.getYPos();
		int newZ = from.getZPos();
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
		} else if(zDir == 2) {//POSITIVE movement on Z axis
			newZ++;
		}
		if(newX <= 0 || newX > room.getLength() || newY <= 0 || newY > room.getWidth() || newZ <= 0 || newZ > room.getHeight()) {//index out of bounds
			nextVolume = null;
		} else {
			nextVolume = room.getInterior().get(((newZ - 1) * room.getWidth() * room.getLength()) + ((newY - 1) * room.getLength()) + (newX - 1));
		}
		return nextVolume;
	}

	/**
	 * getDirectionName(byte) Returns the lexical representation of the indicated direction
	 * @param direction Byte representation of an [X,Y,Z] direction
	 * @return String containing the lexical representation of the given direction
	 */
	public static String getDirectionName(byte direction) {
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

//	/**
//	 * generateName(int,char) Given an int representing the number of syllables, and a character representing a gender (m,f,o), this method returns a randomly generated (single) name
//	 * @param numSyllables How many syllables the name should be
//	 * @param gender What gender to simulate (MALE | FEMALE | OTHER)
//	 * @return A randomly-generated name (to be used for an Actor in the Game) that meets the syllable and gender specifications
//	 */
//	public static String generateName(int numSyllables, char gender) {
//		String name = "";
//		String[] hardConsonants = new String[]{
//			"B","BC","BD","BG","BK","BQ","BR","BT","BV","BZ",
//			"C","CC","CD","CF","CJ","CK","CL","CR","CT","CZ",
//			"D","DB","DD","DG","DK","DP","DR","DT","DV","DZ",
//			"G","GL","GR",
//			"J",
//			"K","KD","KK","KL","KR","KT",
//			"LD","LK","LT","LV","LZ",
//			"MD","MG","MP","MR","MT",
//			"PD","PL","PR",
//			"R","RB","RD","RG","RT","RZ",
//			"SC","SCHV","SG","SK","SS","ST","SV","SZ",
//			"T",
//			"V","VK","VT","VV",
//			"X",
//			"YB","YD","YK","YT","YV",
//			"Z","ZB","ZD","ZK","ZSK","ZT","ZW","ZZ","ZZK","ZZT"
//		};
//		String[] softConsonants = new String[]{
//			"BF","BH","BJ","BL","BM","BN","BS","BW",
//			"CH","CM","CN","CP","CS","CV","CW",
//			"DH","DJ","DL","DM","DN","DS","DW",
//			"F","FL","FR",
//			"H",
//			"JH",
//			"KF","KH","KV",
//			"L","LL","LM","LN","LS","LW",
//			"M","MH","ML","MM","MN","MS",
//			"NG",
//			"P","PF","PH","PS",
//			"RF","RJ","RL","RM","RN","RP","RS","RV",
//			"S","SCH","SCHW","SF","SH","SL","SM","SN","SP","SR","SW",
//			"VF",
//			"W","WH","WL",
//			"XH",
//			"Y","YS",
//			"ZH","ZS"
//		};
//		String[] hardVowels = new String[]{
//			"A","AA","AI","AO",
//			"EE","EA","EU",
//			"II",
//			"O","OA",
//			"UU",
//			"Y","YI"
//		};
//		String[] softVowels = new String[]{
//			"AE","AU",
//			"E","EI",
//			"I","IE",
//			"OE","OI","OU",
//			"YA","YE","YU"
//		};
//		for(int i = 0; i < numSyllables; i++) {
////			String hardC = hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
////			String softC = softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
////			String hardV = hardVowels[Double.valueOf(Math.random() * hardVowels.length).intValue()];
////			String softV = softVowels[Double.valueOf(Math.random() * softVowels.length).intValue()];
//			if(gender == 'm' || gender == 'M') {//pattern = < { [HC*(75)|SC*(25)] + [HV*(50)|SV*(50)] }* (75) > | < { [HC*(50)|SC*(50)] + [HV*(75)|SV*(25)] } * (25) > /// END = [HC*(50)|SC*(50)] * (50)
//				if(Math.random() > .25d) {//75% chance of [HC*(75)|SC*(25)] + [HV*(50)|SV*(50)]
//					if(Math.random() > .25d) {//75% chance of hard consonant group
//						name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//					} else {//25% chance of soft consonant group
//						name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//					}
//					if(Math.random()> .5d) {//50% chance of soft vowel group
//						name += softVowels[Double.valueOf(Math.random() * softVowels.length).intValue()];
//					} else {//50% chance of hard vowel group
//						name += hardVowels[Double.valueOf(Math.random() * hardVowels.length).intValue()];
//					}
//				} else {//25% chance of [HC*(50)|SC*(50)] + [HV*(75)|SV*(25)]
//					if(Math.random() > .5d) {//50% chance of hard consonant group
//						name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//					} else {//50% chance of soft consonant group
//						name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//					}
//					if(Math.random()> .25d) {//75% chance of hard Vowel group
//						name += hardVowels[Double.valueOf(Math.random() * hardVowels.length).intValue()];
//					} else {//50% chance of soft Vowel group
//						name += softVowels[Double.valueOf(Math.random() * softVowels.length).intValue()];
//					}
//				}
//			} else if(gender == 'f' || gender == 'F') {//pattern = < { [HC*(75)|SC*(25)] + [HV*(50)|SV*(50)] }* (25) > | < { [HC*(50)|SC*(50)] + [HV*(25)|SV*(75)] } * (75) > /// END = [HC*(50)|SC*(50)] * (50)
//				if(Math.random() > .25d) {//75% chance of [HC*(50)|SC*(50)] + [HV*(25)|SV*(75)]
//					if(Math.random() > .5d) {//50% chance of hard consonant group
//						name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//					} else {//50% chance of soft consonant group
//						name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//					}
//					if(Math.random()> .25d) {//25% chance of soft vowel group
//						name += softVowels[Double.valueOf(Math.random() * softVowels.length).intValue()];
//					} else {//75% chance of hard vowel group
//						name += hardVowels[Double.valueOf(Math.random() * hardVowels.length).intValue()];
//					}
//				} else {//25% chance of [HC*(75)|SC*(25)] + [HV*(50)|SV*(50)]
//					if(Math.random() > .25d) {//75% chance of hard consonant group
//						name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//					} else {//25% chance of soft consonant group
//						name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//					}
//					if(Math.random()> .50d) {//50% chance of soft vowel group
//						name += softVowels[Double.valueOf(Math.random() * softVowels.length).intValue()];
//					} else {//50% chance of hard vowel group
//						name += hardVowels[Double.valueOf(Math.random() * hardVowels.length).intValue()];
//					}
//				}
//			} else {//pattern = { [HC*(50)|SC*(50)] + [HV*(50)|SV*(50)] } /// END = [HC*(50)|SC*(50)] * (50)
//				if(Math.random() > .5d) {//50% chance of hard consonant group
//					name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//				} else {//50% chance of soft consonant group
//					name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//				}
//				if(Math.random() > .5d) {//50% chance of hard vowel group
//					name += hardVowels[Double.valueOf(Math.random() * hardVowels.length).intValue()];
//				} else {//50% chance of soft vowel group
//					name += softVowels[Double.valueOf(Math.random() * softVowels.length).intValue()];
//				}
//			}
//		}
//		if(Math.random() > .50d) {
//			if(Math.random() > .50d) {//50% chance name will end in a consonant group
//				name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//			} else {
//				name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//			}
//		}
//		return name;
//	}
//
//	public static String generateName(int numSyllables) {
//		String name = "";
//		char[] hardConsonants = new char[]{'B','C','D','G','J','K','P','Q','T','V','X','Y','Z'};
//		char[] softConsonants = new char[]{'F','H','L','M','N','R','S','W'};
//		char[] vowels = new char[]{'A','E','I','O','U','Y'};
//		for(int i = 0; i < numSyllables; i++) {
//			if(i == 1) {//1st syllable of name
//				if(Math.random() > .4d) {//60% chance name will start with a consonant
//					if(Math.random() > .50d) {//50% chance of hard consonant
//						name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//					} else {//50% chance of soft consonant
//						name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//					}
//					if(Math.random() > .10d) {//90% chance of vowel
//						name += vowels[Double.valueOf(Math.random() * vowels.length).intValue()];
//					} else {//10% chance of another consonant
//						if(Math.random() > .50d) {//50% chance of hard consonant
//							name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//						} else {//50% chance of soft consonant
//							name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//						}
//					}
//				} else {//40% chance name will start with a vowel
//					name += vowels[Double.valueOf(Math.random() * vowels.length).intValue()];
//					if(Math.random() > .25d) {//75% chance of consonant
//						if(Math.random() > .50d) {//50% chance of hard consonant
//							name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//						} else {//50% chance of soft consonant
//							name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//						}
//					} else {//25% chance of another vowel
//						name += vowels[Double.valueOf(Math.random() * vowels.length).intValue()];
//					}
//				}
//			} else {//syllable in middle of name
//				if(Math.random() > .65d) {//65% chance of consonant
//					if(Math.random() > .50d) {//50% chance of hard consonant
//						name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//					} else {//50% chance of soft consonant
//						name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//					}
//				} else {//35% chance of vowel
//					name += vowels[Double.valueOf(Math.random() * vowels.length).intValue()];
//				}
//			}
//		}
//		if(Math.random() > .50d) {//50% chance name will end in a consonant group
//			if(Math.random() > .50d) {//50% chance of hard consonant
//				name += hardConsonants[Double.valueOf(Math.random() * hardConsonants.length).intValue()];
//			} else {//50% chance of soft consonant
//				name += softConsonants[Double.valueOf(Math.random() * softConsonants.length).intValue()];
//			}
//		}
//		return name;
//	}
}
