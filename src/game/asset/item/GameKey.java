package game.asset.item;

import game.asset.GameAsset;
import game.asset.GameItemModifier;
import game.asset.effect.GameEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * GameKey Class that represents a GameItem with acts as a Key that can lock and unlock one or more GameObject
 * @author Joseph DeLong
 */
public class GameKey extends GameItemUse {
	private boolean isPaired;
	private String pairedObject;

	/**
	 * GameKey() Constructor used for TEMPLATE creation
	 */
	public GameKey() {
		super();
		this.setAssetType(GameAsset.assetType_ITEM_KEY);
		this.setUid(GameAsset.generateUid(this.getAssetType()));
		
		this.setItemType(GameItem.itemType_KEY);

		this.setIsPaired(false);
		this.setPairedObject("null");
	}

	/**
	 * GameKey(String,String,String,String,String,int,int,int,int,String,int,int,List<GameItemModifier>,boolean,String)
	 */
	public GameKey(String assetType, String uid, String itemType, String name, String description, int itemValue, int itemWeight, int quantity, int rarity, 
			String owner, int curUses, int maxUses, List<GameItemModifier> modifiers, boolean isPaired, String pairedObject) {
		this.setAssetType(assetType);
		this.setUid(uid);

		this.setItemType(itemType);
		this.setName(name);
		this.setDescription(description);
		this.setItemValue(itemValue);
		this.setItemWeight(itemWeight);
		this.setQuantity(quantity);
		this.setRarity(rarity);
		this.setOwner(owner);

		this.setCurUses(curUses);
		this.setMaxUses(maxUses);
		this.setModifiers(modifiers);

		this.setIsPaired(isPaired);
		this.setPairedObject(pairedObject);
	}

	/**
	 * GameKey(GameKey) Given a base GameKey, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameItem.lookupTemplate(itemName)</b></code> in order to create in-game
	 * INSTANCEs of the GameItem TEMPLATE (stored in <code>GameItem.itemTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param itemTemplate The GameKey TEMPLATE to make an INSTANCE copy of
	 */
	public GameKey(GameKey itemTemplate) {
		this.setAssetType(GameAsset.assetType_ITEM_KEY);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType(GameItem.itemType_KEY);
		this.setName(new String(itemTemplate.getName()));
		this.setDescription(new String(itemTemplate.getDescription()));
		this.setItemValue(Integer.valueOf(itemTemplate.getItemValue()));
		this.setItemWeight(Integer.valueOf(itemTemplate.getItemWeight()));
		this.setQuantity(Integer.valueOf(itemTemplate.getQuantity()));
		this.setRarity(Integer.valueOf(itemTemplate.getRarity()));
		this.setOwner(new String(itemTemplate.getOwner()));

		this.setCurUses(Integer.valueOf(itemTemplate.getCurUses()));
		this.setMaxUses(Integer.valueOf(itemTemplate.getMaxUses()));
		List<GameItemModifier> templateModifiers = itemTemplate.getModifiers();
		List<GameItemModifier> modifiers = null;
		if(templateModifiers != null && templateModifiers.size() > 0) {
			modifiers = new ArrayList<GameItemModifier>();
			for(GameItemModifier modifier: templateModifiers) {
				modifiers.add(new GameItemModifier(modifier));
			}
		} else {
			modifiers = new ArrayList<GameItemModifier>();
		}
		this.setModifiers(modifiers);

		this.setIsPaired(Boolean.valueOf(itemTemplate.isPaired()));
		this.setPairedObject(new String(itemTemplate.getPairedObject()));
	}

	public boolean isPaired() {return this.isPaired;}
	public String getPairedObject() {return this.pairedObject;}

	public void setIsPaired(boolean isPaired) {this.isPaired = isPaired;}
	public void setPairedObject(String pairedObject) {this.pairedObject = pairedObject;}

	/**
	 * getByteString() Converts the current GameKey into a String so it can be written to disk
	 * @return String containing the data of this GameKey
	 */
	public String getByteString() {
		String dataString = this.getAssetType() + "|" + this.getUid() + "|" + this.getItemType() + "|" + this.getName() + "|" + this.getDescription() 
				+ "|" + this.getItemValue() + "|" + this.getItemWeight() + "|" + this.getQuantity() + "|" + this.getRarity() + "|" + this.getOwner()
				+ "|" + this.getCurUses() + "|" + this.getMaxUses() + "|";
		List<GameItemModifier> modifiers = this.getModifiers();
		if(modifiers != null && !modifiers.isEmpty()) {
			for(int i = 0; i < modifiers.size(); i++) {
				dataString += modifiers.get(i).getByteString();
				if(i < modifiers.size() - 1) {
					dataString += "ß";
				}
			}
		} else {
			dataString += "null";
		}
		dataString += "|" + this.isPaired() + "|" + this.getPairedObject();
		return dataString;
	}

	/**
	 * getBytes() Returns a representation of the GameKey in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameKey
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameKey contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameKey containing the data parsed from the byte array
	 */
	public static GameItem parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] itemData = s.split("\\|");
		GameKey item = null;
		if(itemData != null && itemData.length == 15 && itemData[0].equals("GameKey")) {//GameAsset(2) + GameItem(8) + GameItemUse(3) + GameKey(2)
			String[] modifierData = itemData[12].split("ß");
			List<GameItemModifier> modifiers = new ArrayList<GameItemModifier>();
			for(String modData: modifierData) {
				GameItemModifier modifier = GameItemModifier.parseBytes(modData.getBytes(),false);
				if(item != null) {
					modifiers.add(modifier);
				}
			}
			String[] effectsData = itemData[13].split("ß");
			List<GameEffect> effects = new ArrayList<GameEffect>();
			for(String effectData: effectsData) {
				GameEffect effect = GameEffect.parseBytes(effectData.getBytes(),false);
				if(effect != null) {
					effects.add(effect);
				}
			}
			item = new GameKey();
			item.setAssetType(itemData[0]);//assetType
			item.setUid(itemData[1]);//uid
			item.setItemType(itemData[2]);//itemType
			item.setName(itemData[3]);//name
			item.setDescription(itemData[4]);//description
			item.setItemValue(Integer.parseInt(itemData[5]));//itemValue
			item.setItemWeight(Integer.parseInt(itemData[6]));//itemWeight
			item.setQuantity(Integer.parseInt(itemData[7]));//quantity
			item.setRarity(Integer.parseInt(itemData[8]));//rarity
			item.setOwner(itemData[9]);//owner
			item.setCurUses(Integer.parseInt(itemData[10]));//curUses
			item.setMaxUses(Integer.parseInt(itemData[11]));//maxUses
			item.setModifiers(modifiers);//modifiers
			item.setIsPaired(Boolean.parseBoolean(itemData[13]));//isPaired
			item.setPairedObject(itemData[14]);//pairedObject
			if(isTemplate) {
				GameItem.add(item);
			}
		}
		return item;
	}
}
