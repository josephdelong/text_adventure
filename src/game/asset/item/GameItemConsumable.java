package game.asset.item;

import game.asset.GameAsset;
import game.asset.GameItemModifier;
import game.asset.effect.GameEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * GameItemConsumable Class that represents a GameItem with limited uses that a GameActor may consume for some benefit
 * @author Joseph DeLong
 */
public class GameItemConsumable extends GameItemUse {
	private List<GameEffect> effects;

	/**
	 * GameItemConsumable() Constructor used for TEMPLATE creation
	 */
	public GameItemConsumable() {
		super();
		this.setAssetType(GameAsset.assetType_ITEM_CONSUMABLE);
		this.setUid(GameAsset.generateUid(this.getAssetType()));
		
		this.setItemType(GameItem.itemType_CONSUMABLE);

		this.setEffects(new ArrayList<GameEffect>());
	}

	/**
	 * GameItemConsumable(String,String,int,int,int,int,String,int,int,List<GameItemModifier>,List<GameEffect>)
	 */
	public GameItemConsumable(
			String assetType, String uid, String itemType, String name, String description, int itemValue, int itemWeight, int quantity, int rarity, String owner, 
			int curUses, int maxUses, List<GameItemModifier> modifiers, List<GameEffect> effects) {
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

		this.setEffects(effects);
	}

	/**
	 * GameItemConsumable(GameItemConsumable) Given a base GameItemConsumable, this method will create an INSTANCE copy, so that the copy's data fields 
	 * reference none of the TEMPLATE's values directly, but are still a value-for-value direct copy of the TEMPLATE.<br>
	 * <br>
	 * NOTE: This constructor should be used in conjunction with <code><b>GameItem.lookupTemplate(itemName)</b></code> in order to create in-game
	 * INSTANCEs of the GameItem TEMPLATE (stored in <code>GameItem.itemTemplates</code>). This will prevent the INSTANCEs from overwriting the
	 * TEMPLATEs (which is undesired).
	 * @param itemTemplate The GameItemConsumable TEMPLATE to make an INSTANCE copy of
	 */
	public GameItemConsumable(GameItemConsumable itemTemplate) {
		this.setAssetType(GameAsset.assetType_ITEM_CONSUMABLE);
		this.setUid(GameAsset.generateUid(this.getAssetType()));

		this.setItemType(GameItem.itemType_CONSUMABLE);
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

		List<GameEffect> templateEffects = itemTemplate.getEffects();
		List<GameEffect> effects = null;
		if(templateEffects != null && templateEffects.size() > 0) {
			effects = new ArrayList<GameEffect>();
			for(GameEffect effect: templateEffects) {
				switch (effect.getEffectType()) {
				case GameEffect.effectType_CHANCE:
					effects.add(new GameEffect(effect));//Chance
					break;
				case GameEffect.effectType_ENVIRONMENT:
					effects.add(new GameEffect(effect));//Environment
					break;
				case GameEffect.effectType_SPELL:
					effects.add(new GameEffect(effect));//Spell
					break;
				case GameEffect.effectType_TRAP:
					effects.add(new GameEffect(effect));//Trap
					break;
				default:
					break;
				}
			}
		} else {
			effects = new ArrayList<GameEffect>();
		}
		this.setEffects(effects);
	}

	public List<GameEffect> getEffects() {return effects;}

	public void setEffects(List<GameEffect> effects) {this.effects = effects;}

	/**
	 * getByteString() Converts the current GameItemConsumable into a String so it can be written to disk
	 * @return String containing the data of this GameItemConsumable
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
		dataString += "|";
		List<GameEffect> effects = this.getEffects();
		if(effects != null && !effects.isEmpty()) {
			for(int i = 0; i < effects.size(); i++) {
				dataString += effects.get(i).getByteString();
				if(i < effects.size() - 1) {
					dataString += "ß";
				}
			}
		}
		return dataString;
	}

	/**
	 * getBytes() Returns a representation of the GameItemConsumable in an array of bytes
	 * @return
	 */
	public byte[] getBytes() {
		return this.getByteString().getBytes();
	}

	/**
	 * parseBytes(byte[]) Reads an array of bytes and translates the data into a GameItem
	 * @param bytes The byte array to read
	 * @param isTemplate Whether the data of GameItemConsumable contained within the btye[] should be processed as a TEMPLATE or INSTANCE thereof
	 * @return GameItem containing the data parsed from the byte array
	 */
	public static GameItem parseBytes(byte[] bytes, boolean isTemplate) {
		String s = new String(bytes);
		String[] itemData = s.split("\\|");
		GameItemConsumable item = null;
		if(itemData != null && itemData.length == 14 && itemData[0].equals("GameItemConsumable")) {//GameAsset(2) + GameItem(8) + GameItemUse(3) + GameItemConsumable(1)
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
			item = new GameItemConsumable();
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
			item.setEffects(effects);//effects
			if(isTemplate) {
				GameItem.add(item);
			}
		}
		return item;
	}
}
