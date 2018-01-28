package game.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameDiplomacy Class that represents the disposition of a GameActor (or group thereof) to another
 * @author Joseph DeLong
 *
 */
public class GameDiplomacy extends GameAsset {
	private static Map<String,GameDiplomacy> diplomacyTemplates;
	
	private String primaryFaction;
	private String secondaryFaction;
	private int disposition;
	private boolean isTemporary;
	private int startTime;
	private int endTime;
	private boolean isRamped;

	/**
	 * GameDiplomacy() Constructor used for TEMPLATE creation
	 */
	public GameDiplomacy() {
		this.setAssetType("GameDiplomacy");
		this.setUid("null-null");
		
		this.setPrimaryFaction("");
		this.setSecondaryFaction("");
		this.setDisposition(0);
		this.setIsTemporary(false);
		this.setStartTime(0);
		this.setEndTime(0);
		this.setIsRamped(false);
	}

	/**
	 * GameDiplomacy(String,String,String,String,int,boolean,int,int,boolean)
	 * @param primaryFaction The Primary Faction to which the disposition applies (This Faction HOLDs the disposition value toward the Secondary Faction)
	 * @param secondaryFaction The Secondary Faction to which the disposition applies (This Faction is regarded with the disposition value by the Primary Faction)
	 * @param disposition The disposition toward the indicated Faction (or Individual), relative to "Normal"
	 * @param isTemporary Whether the disposition value is temporary or permanent
	 * @param startTime The time when the disposition value was initially set
	 * @param endTime The time when the disposition indicated will revert to a "Normal" value
	 * @param isRamped Whether the decay of disposition to "Normal" should be gradual or all at once
	 */
	public GameDiplomacy(String assetType, String uid, String primaryFaction, String secondaryFaction, int disposition, boolean isTemporary, int startTime, 
			int endTime, boolean isRamped) {
		this.setAssetType(assetType);
		this.setUid(uid);
		
		this.setPrimaryFaction(primaryFaction);
		this.setSecondaryFaction(secondaryFaction);
		this.setDisposition(disposition);
		this.setIsTemporary(isTemporary);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setIsRamped(isRamped);
		
		this.setUid(this.getKeyName());
	}

	/**
	 * GameDiplomacy(GameDiplomacy) Given a base GameDiplomacy, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the original's values directly, but are still a value-for-value direct copy of the original data.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameActor.lookupTemplate(actorName)</b></code> in order to create in-game
	 * instances of the Actor TEMPLATE. This will prevent the INSTANCEs from overwriting the TEMPLATEs (which is undesired).
	 * @param diplomacyTemplate The GameDiplomacy TEMPLATE to make an INSTANCE copy of
	 */
	public GameDiplomacy(GameDiplomacy diplomacyTemplate) {
		this.setAssetType("GameDiplomacy");
		this.setUid(GameAsset.generateUid(this.getAssetType()));
		
		this.setPrimaryFaction(new String(diplomacyTemplate.getPrimaryFaction()));
		this.setSecondaryFaction(new String(diplomacyTemplate.getSecondaryFaction()));
		this.setDisposition(Integer.valueOf(diplomacyTemplate.getDisposition()));
		this.setIsTemporary(Boolean.valueOf(diplomacyTemplate.isTemporary()));
		this.setStartTime(Integer.valueOf(diplomacyTemplate.getStartTime()));
		this.setEndTime(Integer.valueOf(diplomacyTemplate.getEndTime()));
		this.setIsRamped(Boolean.valueOf(diplomacyTemplate.isRamped()));
		
		this.setUid(new String(this.getPrimaryFaction().toLowerCase() + "-" + this.getSecondaryFaction().toLowerCase()));
	}

	public static Map<String,GameDiplomacy> getDiplomacyTemplates() {return GameDiplomacy.diplomacyTemplates;}

	public String getPrimaryFaction() {return this.primaryFaction;}
	public String getSecondaryFaction() {return this.secondaryFaction;}
	public int getDisposition() {return this.disposition;}
	public boolean isTemporary() {return this.isTemporary;}
	public int getStartTime() {return this.startTime;}
	public int getEndTime() {return this.endTime;}
	public boolean isRamped() {return this.isRamped;}

	public void setPrimaryFaction(String primaryFaction) {this.primaryFaction = primaryFaction;}
	public void setSecondaryFaction(String secondaryFaction) {this.secondaryFaction = secondaryFaction;}
	public void setDisposition(int disposition) {this.disposition = disposition;}
	public void setIsTemporary(boolean isTemporary) {this.isTemporary = isTemporary;}
	public void setStartTime(int startTime) {this.startTime = startTime;}
	public void setEndTime(int endTime) {this.endTime = endTime;}
	public void setIsRamped(boolean isRamped) {this.isRamped = isRamped;}

	/**
	 * getKeyName(GameDiplomacy) Returns the PrimaryFaction-SecondaryFaction pairing that represents this GameDiplomacy
	 * @return A Strign representing the PrimaryFaction-SecondaryFaction pairing of this GameDiplomacy
	 */
	public String getKeyName() {
		return this.getPrimaryFaction().toLowerCase() + "-" + this.getSecondaryFaction().toLowerCase();
	}

	/**
	 * getDiplomacyTemplatesForFaction(String) Return a Map containing all GameDiplomacy TEMPLATEs relevant to the specified Faction
	 * @param primaryFactionName The name of the Primary Faction to return all related GameDiplomacy TEMPLATEs for
	 * @return A Map containing all GameDiplomacy TEMPLATEs associated to the specified Primary Faction
	 */
	public static Map<String,GameDiplomacy> getDiplomacyTemplatesForFaction(String primaryFactionName) {
		Map<String,GameDiplomacy> relevantDiplomacyTemplates = new HashMap<String,GameDiplomacy>();;
		List<GameDiplomacy> diplomacyTemplates = new ArrayList<GameDiplomacy>();
		diplomacyTemplates.addAll(GameDiplomacy.getDiplomacyTemplates().values());
		for(GameDiplomacy diplomacy: diplomacyTemplates) {
			if(diplomacy.getPrimaryFaction().equals(primaryFactionName.toLowerCase())) {
				relevantDiplomacyTemplates.put(diplomacy.getKeyName(),diplomacy);
			}
		}
		return relevantDiplomacyTemplates;
	}

	/**
	 * add(GameDiplomacy) Adds the specified GameDiplomacy to the List of all Enemy TEMPLATEs in the Game, if not already present
	 * @param diplomacy The GameDiplomacy to add
	 */
	public static void add(GameDiplomacy diplomacy) {
		if(diplomacyTemplates == null) {
			diplomacyTemplates = new HashMap<String,GameDiplomacy>();
		}
		if(!diplomacyTemplates.containsKey(diplomacy.getUid())) {
			diplomacyTemplates.put(diplomacy.getUid(),diplomacy);
		}
	}

	/**
	 * lookupTemplate(String) Look up the GameDiplomacy by its Name in the list of current GameDiplomacy TEMPLATEs
	 * @param diplomacyUid The (unique) identifier of the GameDiplomacy TEMPLATE which is to be returned
	 * @return GameDiplomacy TEMPLATE specified by the (unique) identifier, if found in the current list of GameDiplomacy
	 */
	public static GameDiplomacy lookupTemplate(String diplomacyUid) {
		if(diplomacyTemplates == null || diplomacyTemplates.isEmpty()) {
			return null;
		}
		return diplomacyTemplates.get(diplomacyUid.toLowerCase());
	}

	/**
	 * getBytes() Returns a representation of the GameDiplomacy in an array of bytes
	 * @return byte[] containing the data of this GameDiplomacy
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * getByteString() Converts an instance of GameDiplomacy into a byte[] so it can be written to disk
	 * @return String containing the data of this GameDiplomacy
	 */
	public String getByteString() {
		String dataString = this.getAssetType() + "|" + this.getUid() + "|" + this.getPrimaryFaction() + "|" + this.getSecondaryFaction() 
				+ "|" + this.getDisposition() + "|" + this.isTemporary() + "|" + this.getStartTime() + "|" + this.getEndTime() + "|" + this.isRamped();
		return dataString;
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameDiplomacy
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameDiplomacy contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameDiplomacy containing the data parsed from the byte array
	 */
	public static GameDiplomacy parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] diplomacyData = s.split("|");
		GameDiplomacy diplomacy = null;
		if(diplomacyData != null && diplomacyData.length == 9 && diplomacyData[0].equals("GameDiplomacy")) {//GameAsset(2) + GameDiplomacy(7)
			diplomacy = new GameDiplomacy();
			diplomacy.setAssetType(diplomacyData[0]);//assetType
			diplomacy.setUid(diplomacyData[1]);//uid
			diplomacy.setPrimaryFaction(diplomacyData[2]);//primaryFaction
			diplomacy.setSecondaryFaction(diplomacyData[3]);//secondaryFaction
			diplomacy.setDisposition(Integer.parseInt(diplomacyData[4]));//disposition
			diplomacy.setIsTemporary(Boolean.parseBoolean(diplomacyData[5]));//isTemporary
			diplomacy.setStartTime(Integer.parseInt(diplomacyData[6]));//startTime
			diplomacy.setEndTime(Integer.parseInt(diplomacyData[7]));//endTime
			diplomacy.setIsRamped(Boolean.parseBoolean(diplomacyData[8]));//isRamped
			if(isTemplate) {
				GameDiplomacy.add(diplomacy);
			}
		}
		return diplomacy;
	}
}
