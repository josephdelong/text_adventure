package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameRoom Class that represents a Room in the Game
 * @author Joseph DeLong
 */
public class GameRoom {
	protected static Map<Integer,GameRoom> rooms;

	protected int roomId;
	protected String description;
	protected int length;
	protected int width;
	protected int height;
	protected List<GameVolume> interior;

	/**
	 * GameRoom() Constructor used for CREATION MODE
	 */
	protected GameRoom() {
		this.roomId = -1;
		this.description = "";
		this.length = 0;
		this.width = 0;
		this.height = 0;
		this.interior = new ArrayList<GameVolume>();
	}

	/**
	 * GameRoom(int,String,Map<String,GameItem>,Map<String,GameObject>,Map<String,GameEnemy>,Map<String,String>) Construct a GameRoom with the given parameters
	 * @param roomId The identifier of this GameRoom
	 * @param description The long description of this GameRoom and its contents
	 * @param length The length of the GameRoom, in units of GameVolume
	 * @param width The width of the GameRoom, in units of GameVolume
	 * @param height The height of the GameRoom, in units of GameVolume
	 * @param interior A 3D array of GameVolume, representing the interior volume of this GameRoom
	 */
	protected GameRoom(int roomId, String description, int length, int width, int height, List<GameVolume> interior) {
		this.roomId = roomId;
		this.description = description;
		this.length = length;
		this.width = width;
		this.height = height;
		if(interior == null) {
			this.interior = new ArrayList<GameVolume>();
		} else {
			this.interior = interior;
		}
		GameRoom.add(roomId,this);
	}

	/**
	 * add(int,String) Adds the specified GameRoom to the list of available Game Rooms, if not already available
	 * @param roomId The ID of the Room to be added
	 * @param description The description of the Room to be added
	 */
	protected static void add(int roomId, GameRoom room) {
		if(rooms == null) {
			rooms = new HashMap<Integer,GameRoom>();
		}
		if(!rooms.containsKey(roomId)) {
			rooms.put(roomId,room);
		}
	}

	/**
	 * lookup(int) Finds the GameRoom by roomId, if exists
	 * @param roomId The ID of the room to look up
	 * @return The GameRoom with the roomId specified
	 */
	protected static GameRoom lookup(int roomId) {
		if(rooms == null ||  rooms.isEmpty()) {
			return null;
		}
		return rooms.get(roomId);
	}

	/**
	 * contains(int) Indicated whether a GameRoom with the specified Room Number exists
	 * @param id Number of the GameRoom to look up
	 * @return Whether the indicated GameRoom exists
	 */
	protected static boolean contains(int id) {
		return GameRoom.rooms != null && !GameRoom.rooms.isEmpty() && GameRoom.rooms.containsKey(id);
	}

	/**
	 * getAllVolumesContainingObject(String) Returns a List of GameVolumes which contain the specified GameObject
	 * @param name The name of the Object to look for
	 * @return List of GameVolumes which contain the specified GameObject, or an empty List if not found
	 */
	protected List<GameVolume> getAllVolumesContainingObject(String name) {
		List<GameVolume> volumes = new ArrayList<GameVolume>();
		for(GameVolume v: this.interior) {
			if(v.containsObject(name)) {
				volumes.add(v);
			}
		}
		return volumes;
	}

	/**
	 * getAllVolumesContainingEnemy(String) Returns a List of GameVolumes which contain the specified GameEnemy
	 * @param name The name of the Enemy to look for
	 * @return List of GameVolumes which contain the specified GameEnemy, or an empty List if not found
	 */
	protected List<GameVolume> getAllVolumesContainingEnemy(String name) {
		List<GameVolume> volumes = new ArrayList<GameVolume>();
		for(GameVolume v: this.interior) {
			if(v.containsEnemy(name)) {
				volumes.add(v);
			}
		}
		return volumes;
	}

	/**
	 * getAllVolumesContainingItem(String) Returns a List of GameVolumes which contain the specified GameItem
	 * @param name The name of the Item to look for
	 * @return List of GameVolumes which contain the specified GameItem, or an empty List if not found
	 */
	protected List<GameVolume> getAllVolumesContainingItem(String name) {
		List<GameVolume> volumes = new ArrayList<GameVolume>();
		for(GameVolume v: this.interior) {
			if(v.containsItem(name)) {
				volumes.add(v);
			}
		}
		return volumes;
	}

	/**
	 * containsObject(String) Determine if the named GameObject is in this GameRoom
	 * @param name The name of the Object to look for in this GameRoom
	 * @return Indicator of whether the specified GameObject is in this GameRoom
	 */
	protected boolean containsObject(String name) {
		for(GameVolume v: this.interior) {
			if(v.containsObject(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * containsEnemy(String) Determine if the named GameEnemy is in this GameRoom
	 * @param name The name of the Enemy to look for in this GameRoom
	 * @return Indicator of whether the specified GameEnemy is in this GameRoom
	 */
	protected boolean containsEnemy(String name) {
		for(GameVolume v: this.interior) {
			if(v.containsEnemy(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * containsItem(String) Determine if the named GameItem is in this GameRoom
	 * @param name The name of the Item to look for in this GameRoom
	 * @return Indicator of whether the specified GameItem is in this GameRoom
	 */
	protected boolean containsItem(String name) {
		for(GameVolume v: this.interior) {
			if(v.containsItem(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * getByteString() Returns a String representation of the Room and its properties
	 * @return
	 */
	protected String getByteString() {
		String returnString = this.roomId + "ƒ" + this.description + "ƒ" + this.length + "ƒ" + this.width + "ƒ" + this.height + "ƒ";
		if(this.interior != null && this.interior.size() > 0) {
			List<GameVolume> roomVolumes = this.interior;
			for(int i = 0; i < roomVolumes.size(); i++) {
				if(1 == roomVolumes.size() -1 ) {
					returnString += roomVolumes.get(i).getByteString();
				} else {
					returnString += roomVolumes.get(i).getByteString() + "±";
				}
			}
		} else {
			returnString += "null";
		}
		return returnString;
	}

	/**
	 * getBytes() Returns a representation of the Room in an array of bytes
	 * @return
	 */
	protected byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameRoom
	 * @param bytes The byte array to read
	 * @return GameRoom containing the data parsed from the byte array
	 */
	protected static GameRoom parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] roomData = s.split("ƒ");
		if(roomData.length != 6) {
			return null;
		}
		String[] roomVolumes = roomData[5].split("±");
		List<GameVolume> volumes = new ArrayList<GameVolume>();
		for(String volumeData: roomVolumes) {
			GameVolume volume = GameVolume.parseBytes(volumeData.getBytes());
			if(volume != null) {
				volumes.add(volume);
			}
		}
		return new GameRoom(
				Integer.parseInt(roomData[0]),//roomId
				roomData[1],//description
				Integer.parseInt(roomData[2]),//length
				Integer.parseInt(roomData[3]),//width
				Integer.parseInt(roomData[4]),//height
				volumes//interior
		);
	}
}
