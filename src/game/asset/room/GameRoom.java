package game.asset.room;

import game.asset.GameAsset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameRoom Class that represents a Room in the Game
 * @author Joseph DeLong
 */
public class GameRoom extends GameAsset {
	private static Map<Integer,GameRoom> roomTemplates;

	protected int roomId;
	protected String description;
	protected int length;
	protected int width;
	protected int height;
	protected List<GameVolume> interior;

	/**
	 * GameRoom() Constructor used for CREATION MODE
	 */
	public GameRoom() {
		this.setAssetType(GameAsset.assetType_ROOM);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setRoomId(-1);
		this.setDescription("");
		this.setLength(0);
		this.setWidth(0);
		this.setHeight(0);
		this.setInterior(new ArrayList<GameVolume>());
	}

	/**
	 * GameRoom(String,String,int,String,Map<String,GameItem>,Map<String,GameObject>,Map<String,GameEnemy>,Map<String,String>) Construct a GameRoom TEMPLATE with the given parameters
	 */
	public GameRoom(String assetType, String uid, int roomId, String description, int length, int width, int height, List<GameVolume> interior) {
		this.setAssetType(assetType);
		this.setUid(uid);

		this.setRoomId(roomId);
		this.setDescription(description);
		this.setLength(length);
		this.setWidth(width);
		this.setHeight(height);
		if(interior == null) {
			this.setInterior(new ArrayList<GameVolume>());
		} else {
			this.setInterior(interior);
		}
	}

	/**
	 * GameRoom(GameRoom) Given a base GameRoom, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the template's values directly, but are still a value-for-value direct copy of the template.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameRoom.lookup(roomId)</b></code> in order to create in-game
	 * instances of the Room Template (stored in <code>GameRoom.roomTemplates</code>). This will prevent the Instances from overwriting the
	 * Templates (which is undesired).
	 * @param roomTemplate The GameRoom TEMPLATE to make an INSTANCE copy of
	 */
	public GameRoom(GameRoom roomTemplate) {
		this.setAssetType(GameAsset.assetType_ROOM);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.roomId = Integer.valueOf(roomTemplate.roomId);
		this.description = new String(roomTemplate.description);
		this.length = Integer.valueOf(roomTemplate.length);
		this.width = Integer.valueOf(roomTemplate.width);
		this.height = Integer.valueOf(roomTemplate.height);
		List<GameVolume> interior = null;
		if(roomTemplate.interior != null && !roomTemplate.interior.isEmpty()) {
			interior = new ArrayList<GameVolume>();
			for(GameVolume volume: roomTemplate.interior) {
				interior.add(new GameVolume(volume));
			}
		} else {
			interior = makeRoomVolume(roomTemplate.length,roomTemplate.width,roomTemplate.height);
		}
		this.interior = interior;
	}

	public static Map<Integer, GameRoom> getRoomTemplates() {return roomTemplates;}

	public int getRoomId() {return this.roomId;}
	public String getDescription() {return this.description;}
	public int getLength() {return this.length;}
	public int getWidth() {return this.width;}
	public int getHeight() {return this.height;}
	public List<GameVolume> getInterior() {return this.interior;}

	public void setRoomId(int roomId) {this.roomId = roomId;}
	public void setDescription(String description) {this.description = description;}
	public void setLength(int length) {this.length = length;}
	public void setWidth(int width) {this.width = width;}
	public void setHeight(int height) {this.height = height;}
	public void setInterior(List<GameVolume> interior) {this.interior = interior;}

	/**
	 * add(int,String) Adds the specified GameRoom TEMPLATE to the list of available GameRoom TEMPLATEs, if not already available
	 * @param room The GameRoom TEMPLATE to be added
	 */
	public static void add(GameRoom room) {
		if(roomTemplates == null) {
			roomTemplates = new HashMap<Integer,GameRoom>();
		}
		if(!roomTemplates.containsKey(room.getRoomId())) {
			roomTemplates.put(room.getRoomId(),room);
		}
	}

	/**
	 * makeRoomVolume(int,int,int) Given a LENGTH, WIDTH, and HEIGHT (each between 1 and 100, inclusive), this method will create 
	 * a 3D volume of empty GameVolume cubes, with proper collision detection on all edge volumes
	 * @param length The LENGTH of the GameRoom interior, in units of GameVolume. Must be between 1 & 100, inclusive
	 * @param width The WIDTH of the GameRoom interior, in units of GameVolume. Must be between 1 & 100, inclusive
	 * @param height The HEIGHT of the GameRoom interior, in units of GameVolume. Must be between 1 & 100, inclusive
	 * @return A list of GameVolume, representing the interior volume of the GameRoom, or null if LENGTH, WIDTH, or HEIGHT are outside the
	 * numerical constraints listed above
	 */
	public static List<GameVolume> makeRoomVolume(int length, int width, int height) {
		if(length < 1 || length > 100 || width < 1 || width > 100 || height < 1 || height > 100) {
			return null;
		}
		List<GameVolume> interior = new ArrayList<GameVolume>();
		byte passableDirs = 0b11_11_11;//represents the Z+Z-_Y+Y-_X+X- indicators for whether the indicated direction is passable into or out of the center of the volume
		for(int z = 1; z <= height; z++) {
			for(int y = 1; y <= width; y++) {
				for(int x = 1; x <= length; x++) {
					//this setup assumes the room is larger than 1 GameVolume in every direction
					if(x == 1) {
						passableDirs &= 0b11_11_10;//X- direction not passable
					} else if(x == length) {
						passableDirs &= 0b11_11_01;//X+ direction not passable
					}
					if(y == 1) {
						passableDirs &= 0b11_10_11;//Y- direction not passable
					} else if(y == width) {
						passableDirs &= 0b11_01_11;//Y+ direction not passable
					}
					if(z == 1) {
						passableDirs &= 0b10_11_11;//Z- direction not passable
					} else if(z == height) {
						passableDirs &= 0b01_11_11;//Z+ direction not passable
					}
					interior.add(new GameVolume(x,y,z,true,passableDirs,false,(byte)0b00_00,null,null,null,null));
				}
			}
		}
		return interior;
	}

	/**
	 * lookup(int) Finds the GameRoom TEMPLATE by roomId, if exists
	 * @param roomId The ID of the Room TEMPLATE to look up
	 * @return The GameRoom TEMPLATE with the roomId specified
	 */
	public static GameRoom lookup(int roomId) {
		if(roomTemplates == null ||  roomTemplates.isEmpty()) {
			return null;
		}
		return roomTemplates.get(roomId);
	}

	/**
	 * contains(int) Indicated whether a GameRoom TEMPLATE with the specified Room Number exists
	 * @param id Number of the GameRoom TEMPLATE to look up
	 * @return Whether the indicated GameRoom TEMPLATE exists
	 */
	public static boolean contains(int id) {
		return GameRoom.roomTemplates != null && !GameRoom.roomTemplates.isEmpty() && GameRoom.roomTemplates.containsKey(id);
	}

	/**
	 * getAllVolumesContainingObject(String) Returns a List of GameVolumes which contain the specified GameObject
	 * @param name The name of the Object to look for
	 * @return List of GameVolumes which contain the specified GameObject, or an empty List if not found
	 */
	public List<GameVolume> getAllVolumesContainingObject(String name) {
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
	public List<GameVolume> getAllVolumesContainingEnemy(String name) {
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
	public List<GameVolume> getAllVolumesContainingItem(String name) {
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
	public boolean containsObject(String name) {
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
	public boolean containsEnemy(String name) {
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
	public boolean containsItem(String name) {
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
	public String getByteString() {
		String returnString = this.getAssetType() + "ƒ" + this.getUid() + "ƒ" + this.getRoomId() + "ƒ" + this.getDescription() + "ƒ" + this.getLength() 
				+ "ƒ" + this.getWidth() + "ƒ" + this.getHeight() + "ƒ";
		if(this.getInterior() != null && this.getInterior().size() > 0) {
			List<GameVolume> roomVolumes = this.getInterior();
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
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameRoom
	 * @param bytes The byte array to read
	 * @return GameRoom containing the data parsed from the byte array
	 */
	public static GameRoom parseBytes(byte[] bytes) {
		String s = new String(bytes);
		String[] roomData = s.split("ƒ");
		if(roomData.length != 8) {
			return null;
		}
		String[] roomVolumes = roomData[7].split("±");
		List<GameVolume> volumes = new ArrayList<GameVolume>();
		for(String volumeData: roomVolumes) {
			GameVolume volume = GameVolume.parseBytes(volumeData.getBytes());
			if(volume != null) {
				volumes.add(volume);
			}
		}
		GameRoom room = new GameRoom();
		room.setAssetType(roomData[0]);//assetType
		room.setUid(roomData[1]);//uid
		room.setRoomId(Integer.parseInt(roomData[2]));//roomId
		room.setDescription(roomData[3]);//description
		room.setLength(Integer.parseInt(roomData[4]));//length
		room.setWidth(Integer.parseInt(roomData[5]));//width
		room.setHeight(Integer.parseInt(roomData[6]));//height
		room.setInterior(volumes);//interior
		return room;
	}
}
