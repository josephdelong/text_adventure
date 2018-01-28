package game;

import game.asset.GameAsset;
import game.asset.GameDiplomacy;
import game.asset.GameItemModifier;
import game.asset.actor.GameActor;
import game.asset.actor.GameEnemy;
import game.asset.effect.GameEffect;
import game.asset.item.GameItem;
import game.asset.item.GameItemConsumable;
import game.asset.item.GameItemEquip;
import game.asset.item.GameItemUse;
import game.asset.item.GameItemWield;
import game.asset.item.GameItemWorn;
import game.asset.item.GameKey;
import game.asset.object.GameObject;
import game.asset.room.GameRoom;
import game.asset.room.GameVolume;
import game.asset.spell.GameSpell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GameState A class that stores the current state of the Game
 * @author Joseph DeLong
 */
public class GameState {
	private static List<GameActor> players;
	private static Map<Integer,GameRoom> visitedRooms;
	private static boolean inCombat;
	private static GameCombat activeCombat;

	public static List<GameActor> getPlayers() {return players;}
	public static Map<Integer, GameRoom> getVisitedRooms() {return visitedRooms;}
	public static boolean isInCombat() {return inCombat;}
	public static GameCombat getActiveCombat() {return activeCombat;}

	public static void setPlayers(List<GameActor> players) {GameState.players = players;}
	public static void setVisitedRooms(Map<Integer, GameRoom> visitedRooms) {GameState.visitedRooms = visitedRooms;}
	public static void setInCombat(boolean inCombat) {GameState.inCombat = inCombat;}
	public static void setActiveCombat(GameCombat activeCombat) {GameState.activeCombat = activeCombat;}

	/**
	 * initiateCombat(GameRoom) For the specified GameRoom, create a new Combat Encounter including all hostile entities
	 * @param room The GameRoom in which the Combat Encounter will take place
	 * @return A GameCombat, set up with all initially involved GameActors, including their Initiative order
	 */
	public GameCombat initiateCombat(GameRoom room) {
		activeCombat = new GameCombat(room);
		
		return null;
	}

	/**
	 * initDefaultResources() 
	 */
	public static void initDefaultResources() {
		//base GameAssets needed to play the Game
		//GameEnemy
		if(GameEnemy.getEnemyTemplates() == null || GameEnemy.getEnemyTemplates().size() == 0) {
			GameEnemy skeleton = new GameEnemy(GameAsset.assetType_ACTOR_ENEMY, GameAsset.generateUid("GameEnemy"),GameActor.actorType_ENEMY,"skeleton",
					"A walking pile of bones, skull and all.",1,GameActor.race_UNDEAD,GameActor.class_FIGHTER,0,"null",60,100,"null","null","null",
					0,6,10,10,10,10,10,10,0,0,0,0,0,0,0,0,0,6,0,100,10,10,10,10,0,0,0,10,0,0,new ArrayList<GameItem>(),new ArrayList<GameSpell>(),
					new ArrayList<String>(),new ArrayList<GameDiplomacy>(),-1,-1,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
					null,null,null,10,true);
			GameEnemy.add(skeleton);
			GameEnemy zombie = new GameEnemy(GameAsset.assetType_ACTOR_ENEMY,GameAsset.generateUid("GameEnemy"),GameActor.actorType_ENEMY,"zombie",
					"A shambling, rotting corpse, artificially animated by necromantic magic.",1,GameActor.race_UNDEAD,GameActor.class_FIGHTER,0,"null",
					60,120,"null","null","null",0,6,10,10,10,10,10,10,0,0,0,0,0,0,0,0,0,6,0,100,15,15,5,5,0,0,0,10,0,0,new ArrayList<GameItem>(),
					new ArrayList<GameSpell>(),new ArrayList<String>(),new ArrayList<GameDiplomacy>(),-1,-1,null,null,null,null,null,null,null,null,null,
					null,null,null,null,null,null,null,null,15,true);
			GameEnemy.add(zombie);
		}
		//GameItem
		if(GameItem.getItemTemplates() == null || GameItem.getItemTemplates().size() == 0) {
//			GameItem template = new GameItem(GameAsset.assetType_ITEM,"uid","itemType","name","description",0/*itemValue*/,0/*itemWeight*/,0/*quantity*/,0/*rarity*/,"owner");
//			GameItem use = new GameItemUse(GameAsset.assetType_ITEM_USE,"uid",GameItem.itemType_USE,"name","description",0/*itemValue*/,0/*itemWeight*/,0/*quantity*/,0/*rarity*/,"owner",0/*curUses*/,0/*maxUses*/,null/*modifiers*/);
//			GameItem consumable = new GameItemConsumable(GameAsset.assetType_ITEM_CONSUMABLE,"uid",GameItem.itemType_CONSUMABLE,"name","description",0/*itemValue*/,0/*itemWeight*/,0/*quantity*/,0/*rarity*/,"owner",0/*curUses*/,0/*maxUses*/,null/*modifiers*/,null/*effects*/);
//			GameItem key = new GameKey(GameAsset.assetType_ITEM_KEY,"uid",GameItem.itemType_KEY,"name","description",0/*itemValue*/,0/*itemWeight*/,0/*quantity*/,0/*rarity*/,"owner",0/*curUses*/,0/*maxUses*/,null/*modifiers*/,false/*isPaired*/,"pairedObject");
//			GameItem equip = new GameItemEquip(GameAsset.assetType_ITEM_EQUIP,"uid",GameItem.itemType_EQUIP,"name","description",0/*itemValue*/,0/*itemWeight*/,0/*quantity*/,0/*rarity*/,"owner","equipSlot",false/*isEquipped*/,null/*modifiers*/);
//			GameItem wield = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid","itemType","name","description",0/*itemValue*/,0/*itemWeight*/,0/*quantity*/,0/*rarity*/,"owner","equipSlot",false/*isEquipped*/,null/*modifiers*/,-1/*attackType*/,-1/*damageType*/,0/*damageDie*/,0/*numDie*/,0/*bonusDamage*/,0/*minRange*/,0/*maxRange*/,0/*reach*/);
//			GameItem worn = new GameItemWorn(GameAsset.assetType_ITEM_WORN,"uid","itemType","name","description",0/*itemValue*/,0/*itemWeight*/,0/*quantity*/,0/*rarity*/,"owner","equipSlot",false/*isEquipped*/,null/*modifiers*/,0/*damageReduction*/,0/*magicResistance*/,0/*armorClass*/);

			GameItem sword = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"sword","A rusty iron sabre.",5/*itemValue*/,2/*itemWeight*/,1/*quantity*/,0/*rarity*/,"null",GameItem.MAIN_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MELEE,GameItem.damageType_SLASH,6/*damageDie*/,1/*numDie*/,0/*bonusDamage*/,0/*minRange*/,1/*maxRange*/,0/*reach*/);
			GameItem.add(sword);
			GameItem shortSword = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"short sword","A medium-sized blade, for piercing through gaps in an enemy's defenses.",10/*itemValue*/,2/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.MAIN_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MELEE,GameItem.damageType_PIERCE,6/*damageDie*/,1/*numDie*/,0/*bonusDamage*/,0/*minRange*/,1/*maxRange*/,0/*reach*/);
			GameItem.add(shortSword);
			GameItem longSword = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"long sword","A long, straight, steel blade, with fullers for bloodletting should you pierce through an opponent's defenses.",15/*itemValue*/,3/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.MAIN_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MELEE,GameItem.damageType_SLASH,8/*damageDie*/,1/*numDie*/,0/*bonusDamage*/,0/*minRange*/,1/*maxRange*/,0/*reach*/);
			GameItem.add(longSword);
			GameItem greatSword = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"gread sword","A blade about three-fifths as long as the weilder is tall, supported by a longer two-handed grip. This sword is specially made for hewing through thick armor.",50/*itemValue*/,6/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.TWO_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MELEE,GameItem.damageType_SLASH,6/*damageDie*/,2/*numDie*/,0/*bonusDamage*/,0/*minRange*/,1/*maxRange*/,0/*reach*/);
			GameItem.add(greatSword);
			GameItem dagger = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"dagger","A small, quick steel blade suitable for dispatching foes in close quarters. Also useful as a letter opener.",5/*itemValue*/,1/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.MAIN_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MELEE,GameItem.damageType_PIERCE,4/*damageDie*/,1/*numDie*/,0/*bonusDamage*/,0/*minRange*/,1/*maxRange*/,0/*reach*/);
			GameItem.add(dagger);
			GameItem shortStaff = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"short staff","A stout rod of intricately carved Ash, about 3 feet in length, with adornments of beads, feathers, and other small arcane reagants. There are dimply pulsing magic runes across the head of the staff.",10/*itemValue*/,4/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.MAIN_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MELEE,GameItem.damageType_BLUDGEON,6/*damageDie*/,1/*numDie*/,0/*bonusDamage*/,0/*minRange*/,1/*maxRange*/,0/*reach*/);
			GameItem.add(shortStaff);
			GameItem longStaff = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"long staff","A strong rod of Oak, about 6 feet in length, with arcane runes and small magic crystals inset into the surface of the staff's upper third. The runes and gems pulsate with a faint magic glow.",50/*itemValue*/,6/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.TWO_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MELEE,GameItem.damageType_BLUDGEON,8/*damageDie*/,1/*numDie*/,0/*bonusDamage*/,0/*minRange*/,1/*maxRange*/,0/*reach*/);
			GameItem.add(longStaff);
			GameItem shortBow = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"short bow","A fast and mobile bow, which can hit targets up to 150 feet away.",25/*itemValue*/,2/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.TWO_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_RANGED,GameItem.damageType_PIERCE,6/*damageDie*/,1/*numDie*/,0/*bonusDamage*/,3/*minRange*/,30/*maxRange*/,0/*reach*/);
			GameItem.add(shortBow);
			GameItem longBow = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"long bow","A stout bow that stands about as tall as the person wielding it. Although this weapon is large and cumbersome, if used skillfully, it can hit targets up to 300 feet away.",50/*itemValue*/,8/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.TWO_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_RANGED,GameItem.damageType_PIERCE,8/*damageDie*/,1/*numDie*/,0/*bonusDamage*/,6/*minRange*/,60/*maxRange*/,0/*reach*/);
			GameItem.add(longBow);
			GameItem mace = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"mace","A solid steel weight affixed to the top of a sturdy wooden handle, with hemishperical protrusions across the surface. This weapon is meant for smashing and bashing and so it doesn't require a light touch.",5/*itemValue*/,4/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.MAIN_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MELEE,GameItem.damageType_BLUDGEON,6/*damageDie*/,1/*numDie*/,0/*bonusDamage*/,0/*minRange*/,1/*maxRange*/,0/*reach*/);
			GameItem.add(mace);
			GameItem maul = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"maul","A mighty mallet of wood and steel, with a 2-handed haft. This weapon is suitable for breaking bones and bruising flesh.",10/*itemValue*/,10/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.TWO_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MELEE,GameItem.damageType_BLUDGEON,6/*damageDie*/,2/*numDie*/,0/*bonusDamage*/,0/*minRange*/,1/*maxRange*/,0/*reach*/);
			GameItem.add(maul);
			GameItem wand = new GameItemWield(GameAsset.assetType_ITEM_WIELD,"uid",GameItem.itemType_WEAPON,"wand","A slender rod of wood, finely finished with a magical gem in the pommel. This weapon is capable of having Spells imprinted on it, and once properly attuned, casting the Spell(s) which have been imprinted.",15/*itemValue*/,1/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.MAIN_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),GameItem.attackType_MAGIC,GameItem.damageType_MAGIC,0/*damageDie*/,0/*numDie*/,0/*bonusDamage*/,0/*minRange*/,20/*maxRange*/,0/*reach*/);
			GameItem.add(wand);

			GameItem fullPlate = new GameItemWorn(GameAsset.assetType_ITEM_WORN,"uid",GameItem.itemType_ARMOR,"full plate","Full Plate armor consists of shaped, interlocking metal plates to cover the entire body. A suit of plate includes gauntlets, heavy leather boots, a visored helmet, and thick layers of padding underneath the armor. Buckles and straps distribute the weight over the body.",1500/*itemValue*/,65/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.CUIRASS,false/*isEquipped*/,new ArrayList<GameItemModifier>(),0/*damageReduction*/,0/*magicResistance*/,8/*armorClass*/);
			GameItem.add(fullPlate);
			GameItem chainMail = new GameItemWorn(GameAsset.assetType_ITEM_WORN,"uid",GameItem.itemType_ARMOR,"chain mail","Made of interlocking metal rings, Chain Mail includes a layer of quilted fabric worn underneath the mail to prevent chafing and to cushion the impact of blows. The suit includes gauntlets.",75/*itemValue*/,55/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.CUIRASS,false/*isEquipped*/,new ArrayList<GameItemModifier>(),0/*damageReduction*/,0/*magicResistance*/,6/*armorClass*/);
			GameItem.add(chainMail);
			GameItem leatherArmor = new GameItemWorn(GameAsset.assetType_ITEM_WORN,"uid",GameItem.itemType_ARMOR,"leather armor","The Breastplate and shoulder protectors of this armor are made of leather that has been stiffened by being boiled in oil. The rest of the armor is made of softer and more flexible materials.",10/*itemValue*/,10/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.CUIRASS,false/*isEquipped*/,new ArrayList<GameItemModifier>(),0/*damageReduction*/,0/*magicResistance*/,2/*armorClass*/);
			GameItem.add(leatherArmor);
			GameItem studdedArmor = new GameItemWorn(GameAsset.assetType_ITEM_WORN,"uid",GameItem.itemType_ARMOR,"studded armor","Made from tough but flexible leather, studded leather is reinforced with close-set rivets or spikes.",45/*itemValue*/,13/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.CUIRASS,false/*isEquipped*/,new ArrayList<GameItemModifier>(),0/*damageReduction*/,0/*magicResistance*/,3/*armorClass*/);
			GameItem.add(studdedArmor);
			GameItem buckler = new GameItemWorn(GameAsset.assetType_ITEM_WORN,"uid",GameItem.itemType_ARMOR,"buckler","A small, round, wooden shield with a row of iron studs across the front. It has a sturdy leather strap on the back to hold.",10/*itemValue*/,5/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.OFF_HAND,false/*isEquipped*/,new ArrayList<GameItemModifier>(),0/*damageReduction*/,0/*magicResistance*/,2/*armorClass*/);
			GameItem.add(buckler);

			GameItem shirt = new GameItemEquip(GameAsset.assetType_ITEM_EQUIP,"uid",GameItem.itemType_CLOTHING,"shirt","A comfortable homespun hemp shirt. Suitable for all seasons.",0/*itemValue*/,1/*itemWeight*/,1/*quantity*/,0/*rarity*/,"null",GameItem.SHIRT,false/*isEquipped*/,new ArrayList<GameItemModifier>());
			GameItem.add(shirt);
			GameItem pants = new GameItemEquip(GameAsset.assetType_ITEM_EQUIP,"uid",GameItem.itemType_CLOTHING,"pants","A pair of well-worn homespun hemp trousers. They're quite comfortable and warm.",0/*itemValue*/,2/*itemWeight*/,1/*quantity*/,0/*rarity*/,"null",GameItem.PANTS,false/*isEquipped*/,new ArrayList<GameItemModifier>());
			GameItem.add(pants);
			GameItem shoes = new GameItemEquip(GameAsset.assetType_ITEM_EQUIP,"uid",GameItem.itemType_CLOTHING,"shoes","A pair of common walking shoes, well suited for everyday use.",0/*itemValue*/,1/*itemWeight*/,1/*quantity*/,0/*rarity*/,"null",GameItem.SHOES,false/*isEquipped*/,new ArrayList<GameItemModifier>());
			GameItem.add(shoes);
			GameItem cape = new GameItemEquip(GameAsset.assetType_ITEM_EQUIP,"uid",GameItem.itemType_CLOTHING,"cape","A dark brown velvet cape, embroidered along the edges with a simple floral pattern. It has a shiny brass button and a braided cord at the collar to fasten it securely, but comfortably, around your neck.",15/*itemValue*/,3/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.CLOAK,false/*isEquipped*/,new ArrayList<GameItemModifier>());
			GameItem.add(cape);
			GameItem robe = new GameItemEquip(GameAsset.assetType_ITEM_EQUIP,"uid",GameItem.itemType_CLOTHING,"robe","A luxurious silken robe, with long-flowing arms, the interiors of which are lined with a series of pockets for storage of and quick access to magical reagents.",50/*itemValue*/,4/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.CUIRASS,false/*isEquipped*/,new ArrayList<GameItemModifier>());
			GameItem.add(robe);
			GameItem linenWrap = new GameItemEquip(GameAsset.assetType_ITEM_EQUIP,"uid",GameItem.itemType_CLOTHING,"linen wrap","A single, long swath of Linen cloth, which wraps around the wearer's body in a comfortable & fully covering way.",10/*itemValue*/,5/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.CUIRASS,false/*isEquipped*/,new ArrayList<GameItemModifier>());
			GameItem.add(linenWrap);
			GameItem necklace = new GameItemEquip(GameAsset.assetType_ITEM_EQUIP,"uid",GameItem.itemType_TRINKET,"necklace","An ornate decorative pendant, hung on a braided black cord.",50/*itemValue*/,1/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.AMULET,false/*isEquipped*/,new ArrayList<GameItemModifier>());
			GameItem.add(necklace);
			GameItem ring = new GameItemEquip(GameAsset.assetType_ITEM_EQUIP,"uid",GameItem.itemType_TRINKET,"ring","A simple loop of metal, which fits snugly around your index finger.",25/*itemValue*/,0/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",GameItem.RING,false/*isEquipped*/,new ArrayList<GameItemModifier>());
			GameItem.add(ring);

			GameItem lamp = new GameItemUse(GameAsset.assetType_ITEM_USE,"uid",GameItem.itemType_USE,"lamp","A dim oil lamp. It gives off a warm glow up to 50 feet away.",0/*itemValue*/,2/*itemWeight*/,1/*quantity*/,0/*rarity*/,"null",100/*curUses*/,100/*maxUses*/,new ArrayList<GameItemModifier>());
			GameItem.add(lamp);

			GameItem lockpick = new GameKey(GameAsset.assetType_ITEM_KEY,"uid",GameItem.itemType_KEY,"lockpick","A thin, flexible, metal tool, small enough to insert into the keyhole of a lock. This single-use key substitute can be used to unlock all but the most secure keylocks.",5/*itemValue*/,0/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",1/*curUses*/,1/*maxUses*/,new ArrayList<GameItemModifier>(),false/*isPaired*/,"null");
			GameItem.add(lockpick);

			GameItem potion = new GameItemConsumable(GameAsset.assetType_ITEM_CONSUMABLE,"uid",GameItem.itemType_CONSUMABLE,"potion","A small, corked glass bottle, containing a gently swirling tincture. A paper label is affixed to the bottle, with the name and known effects of the potion.",10/*itemValue*/,1/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",1/*curUses*/,1/*maxUses*/,new ArrayList<GameItemModifier>(),new ArrayList<GameEffect>());
			GameItem.add(potion);
			GameItem bandage = new GameItemConsumable(GameAsset.assetType_ITEM_CONSUMABLE,"uid",GameItem.itemType_CONSUMABLE,"bandage","A small roll of absorbent cloth, for wrapping tightly around cuts and abrasions. It is treated with a soothing ointment to numb pain and speed the healing process.",5/*itemValue*/,0/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null",1/*curUses*/,1/*maxUses*/,new ArrayList<GameItemModifier>(),new ArrayList<GameEffect>());
			GameItem.add(bandage);

			GameItem coin = new GameItem(GameAsset.assetType_ITEM,"uid","ITEM","coin","The currency of the land. Each coin is stamped in the likeness of some long-dead monarch, and associated with a monetary value.",1/*itemValue*/,0/*itemWeight*/,1/*quantity*/,0/*rarity*/,"null");
			GameItem.add(coin);
			GameItem gemstone = new GameItem(GameAsset.assetType_ITEM,"uid","ITEM","gemstone","A rough, but sizable chunk of unidentifiable gemstone. If refined, this shiny stone could be worth a fair sum of coin.",10/*itemValue*/,0/*itemWeight*/,1/*quantity*/,1/*rarity*/,"null");
			GameItem.add(gemstone);
		}
		//GameItemModifier (prefix)
		if(GameItemModifier.getModifierTemplates() == null || GameItemModifier.getModifierTemplates().size() == 0) {
			GameItemModifier prefixJagged = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"jagged",GameItem.itemType_WEAPON,GameItem.attackType_MELEE,GameItem.damageType_SLASH,new String[]{"condition"},new String[]{"GameActor.condition_BLEED"},new int[]{15},new int[]{30},100);
			GameItemModifier.add(prefixJagged);
			GameItemModifier prefixVenemous = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"venemous",GameItem.itemType_WEAPON,-1,-1,new String[]{"condition"},new String[]{"GameActor.condition_POISON"},new int[]{15},new int[]{30},100);
			GameItemModifier.add(prefixVenemous);
			GameItemModifier prefixMighty = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"mighty",GameItem.itemType_WEAPON,GameItem.attackType_MELEE,GameItem.damageType_BLUDGEON,new String[]{"condition"},new String[]{"GameActor.condition_STUN"},new int[]{15},new int[]{30},100);
			GameItemModifier.add(prefixMighty);
			GameItemModifier prefixWounding = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"wounding",GameItem.itemType_WEAPON,-1,GameItem.damageType_PIERCE,new String[]{"condition"},new String[]{"GameActor.condition_FATIGUE"},new int[]{15},new int[]{30},100);
			GameItemModifier.add(prefixWounding);
			GameItemModifier prefixMystic = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"mystic",GameItem.itemType_WEAPON,GameItem.attackType_MAGIC,GameItem.damageType_MAGIC,new String[]{"weaponEffect"},new String[]{"extraAttack"},new int[]{1},new int[]{1},100);
			GameItemModifier.add(prefixMystic);
			GameItemModifier prefixBlessed = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"blessed",GameItem.itemType_WEAPON,-1,-1,new String[]{"damageDealt"},new String[]{"X"},new int[]{2},new int[]{2},100);
			GameItemModifier.add(prefixBlessed);
			GameItemModifier prefixCursed = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"cursed",GameItem.itemType_WEAPON,-1,-1,new String[]{"damageDealt"},new String[]{"/"},new int[]{2},new int[]{2},100);
			GameItemModifier.add(prefixCursed);

			GameItemModifier prefixPotent = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"potent",GameItem.itemType_WEAPON,-1,-1,new String[]{"damageDealt"},new String[]{"X"},new int[]{1},new int[]{2},25);
			GameItemModifier.add(prefixPotent);
			GameItemModifier prefixLethargic = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"lethargic",GameItem.itemType_WEAPON,-1,-1,new String[]{"damageDealt"},new String[]{"/"},new int[]{1},new int[]{2},25);
			GameItemModifier.add(prefixLethargic);
			GameItemModifier prefixDurable = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"durable","ANY",-1,-1,new String[]{"curDurability","maxDurability"},new String[]{"X","X"},new int[]{1,1},new int[]{2,2},25);
			GameItemModifier.add(prefixDurable);
			GameItemModifier prefixBrittle = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"brittle","ANY",-1,-1,new String[]{"curDurability","maxDurability"},new String[]{"/","/"},new int[]{1,1},new int[]{2,2},25);
			GameItemModifier.add(prefixBrittle);
			GameItemModifier prefixArcane = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"arcane","ANY",-1,-1,new String[]{"curSp","maxSp"},new String[]{"+","+"},new int[]{10,20},new int[]{10,20},25);
			GameItemModifier.add(prefixArcane);
			GameItemModifier prefixSanguine = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"sanguine","ANY",-1,-1,new String[]{"curHp","maxHp"},new String[]{"+","+"},new int[]{10,20},new int[]{10,20},25);
			GameItemModifier.add(prefixSanguine);
			GameItemModifier prefixLarge = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"large",GameItem.itemType_WEAPON,-1,-1,new String[]{"minRange","maxRange","reach","weight","damageDealt"},new String[]{"X","X","X","X","X"},new int[]{2,2,2,2,2},new int[]{2,2,2,2,2},75);
			GameItemModifier.add(prefixLarge);
			GameItemModifier prefixSmall = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"small",GameItem.itemType_WEAPON,-1,-1,new String[]{"minRange","maxRange","reach","weight","damageDealt"},new String[]{"/","/","/","/","/"},new int[]{2,2,2,2,2},new int[]{2,2,2,2,2},25);
			GameItemModifier.add(prefixSmall);

			GameItemModifier prefixGuarding = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"guarding","ANY",-1,-1,new String[]{"damageReduction"},new String[]{"+"},new int[]{1},new int[]{5},100);
			GameItemModifier.add(prefixGuarding);
			GameItemModifier prefixShrouding = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"shrouding","ANY",-1,-1,new String[]{"magicResistance"},new String[]{"+"},new int[]{1},new int[]{5},100);
			GameItemModifier.add(prefixShrouding);
			GameItemModifier prefixSmiting = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"smiting",GameItem.itemType_WEAPON,GameItem.attackType_MELEE,-1,new String[]{"attackBonusMelee"},new String[]{"+"},new int[]{1},new int[]{5},100);
			GameItemModifier.add(prefixSmiting);
			GameItemModifier prefixSeeking = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"seeking",GameItem.itemType_WEAPON,GameItem.attackType_RANGED,-1,new String[]{"attackBonusRanged"},new String[]{"+"},new int[]{1},new int[]{5},100);
			GameItemModifier.add(prefixSeeking);
			GameItemModifier prefixScrying = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_PREFIX,"scrying",GameItem.itemType_WEAPON,GameItem.attackType_MAGIC,-1,new String[]{"attackBonusMagic"},new String[]{"+"},new int[]{1},new int[]{5},100);
			GameItemModifier.add(prefixScrying);
		//GameItemModifier (suffix)
			GameItemModifier suffixProtection = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"protection","ANY",-1,-1,new String[]{"armorClass"},new String[]{"+"},new int[]{5},new int[]{10},50);
			GameItemModifier.add(suffixProtection);
			GameItemModifier suffixFortune = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"fortune","ANY",-1,-1,new String[]{"ATTRIBUTE","ATTRIBUTE"},new String[]{"goldFind","lootFind"},new int[]{10,1},new int[]{50,5},100);
			GameItemModifier.add(suffixFortune);
			GameItemModifier suffixPauper = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"the pauper","ANY",-1,-1,new String[]{"ATTRIBUTE","ATTRIBUTE"},new String[]{"goldFind","lootFind"},new int[]{-10,-1},new int[]{-50,-5},100);
			GameItemModifier.add(suffixPauper);
			GameItemModifier suffixVampire = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"the vampire",GameItem.itemType_WEAPON,-1,-1,new String[]{"CONDITION"},new String[]{"lifeSteal"},new int[]{5},new int[]{10},50);
			GameItemModifier.add(suffixVampire);
			GameItemModifier suffixRestoration = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"restoration",GameItem.itemType_ARMOR,-1,-1,new String[]{"ATTRIBUTE?"},new String[]{"healthRegen"},new int[]{5},new int[]{10},50);
			GameItemModifier.add(suffixRestoration);
			GameItemModifier suffixHealth = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"health",GameItem.itemType_CONSUMABLE,-1,-1,new String[]{"currentHp"},new String[]{"+"},new int[]{5},new int[]{15},15);
			GameItemModifier.add(suffixHealth);
			GameItemModifier suffixEnergy = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"energy",GameItem.itemType_CONSUMABLE,-1,-1,new String[]{"currentSp"},new String[]{"+"},new int[]{5},new int[]{15},15);
			GameItemModifier.add(suffixEnergy);
			GameItemModifier suffixDoom = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"doom",GameItem.itemType_WEAPON,-1,-1,new String[]{"condition"},new String[]{"GameActor.condition_INSTA_KILL"},new int[]{1},new int[]{15},250);
			GameItemModifier.add(suffixDoom);

			GameItemModifier suffixFrost = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"frost",GameItem.itemType_WEAPON,-1,-1,new String[]{"applyTarget"},new String[]{GameSpell.damage_COLD},new int[]{5},new int[]{15},50);
			GameItemModifier.add(suffixFrost);
			GameItemModifier suffixFire = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"fire",GameItem.itemType_WEAPON,-1,-1,new String[]{"applyTarget"},new String[]{GameSpell.damage_FIRE},new int[]{5},new int[]{15},50);
			GameItemModifier.add(suffixFire);
			GameItemModifier suffixLightning = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"lightning",GameItem.itemType_WEAPON,-1,-1,new String[]{"applyTarget"},new String[]{GameSpell.damage_ELECTRIC},new int[]{5},new int[]{15},50);
			GameItemModifier.add(suffixLightning);
			GameItemModifier suffixForce = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"force",GameItem.itemType_WEAPON,-1,-1,new String[]{"applyTarget"},new String[]{GameSpell.damage_FORCE},new int[]{5},new int[]{15},50);
			GameItemModifier.add(suffixForce);
			GameItemModifier suffixInsanity = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"insanity",GameItem.itemType_WEAPON,-1,-1,new String[]{"applyTarget"},new String[]{GameSpell.damage_PSYCHIC},new int[]{5},new int[]{15},50);
			GameItemModifier.add(suffixInsanity);
			GameItemModifier suffixGravity = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"gravity",GameItem.itemType_WEAPON,-1,-1,new String[]{"applyTarget"},new String[]{GameSpell.damage_CRUSHING},new int[]{5},new int[]{15},50);
			GameItemModifier.add(suffixGravity);
			GameItemModifier suffixHealing = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"healing",GameItem.itemType_WEAPON,-1,-1,new String[]{"applySelf"},new String[]{GameSpell.damage_HEALING},new int[]{5},new int[]{15},100);
			GameItemModifier.add(suffixHealing);

			GameItemModifier suffixBull = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"the bull","ANY",-1,-1,new String[]{"ability_STR"},new String[]{"+"},new int[]{1},new int[]{5},100);//(BRAWN)
			GameItemModifier.add(suffixBull);
			GameItemModifier suffixBoar = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"the boar","ANY",-1,-1,new String[]{"ability_DEX"},new String[]{"+"},new int[]{1},new int[]{5},100);//(VIGOR)
			GameItemModifier.add(suffixBoar);
			GameItemModifier suffixHawk = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"the hawk","ANY",-1,-1,new String[]{"ability_CON"},new String[]{"+"},new int[]{1},new int[]{5},100);//(SPEED)(FLEET)(QUICK)(DODGE)
			GameItemModifier.add(suffixHawk);
			GameItemModifier suffixCrow = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"the crow","ANY",-1,-1,new String[]{"ability_INT"},new String[]{"+"},new int[]{1},new int[]{5},100);//(LOGIC)
			GameItemModifier.add(suffixCrow);
			GameItemModifier suffixWolf = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"the wolf","ANY",-1,-1,new String[]{"ability_WIS"},new String[]{"+"},new int[]{1},new int[]{5},100);//(SENSE)
			GameItemModifier.add(suffixWolf);
			GameItemModifier suffixSeal = new GameItemModifier(GameAsset.assetType_ITEM_MODIFIER,GameAsset.generateUid(GameAsset.assetType_ITEM_MODIFIER),GameItemModifier.modifierType_SUFFIX,"the seal","ANY",-1,-1,new String[]{"ability_CHA"},new String[]{"+"},new int[]{1},new int[]{5},100);//(CHARM)
			GameItemModifier.add(suffixSeal);
		}
		//GameObject
		if(GameObject.getObjectTemplates() == null || GameObject.getObjectTemplates().size() == 0) {
			GameObject chest = new GameObject(GameAsset.assetType_OBJECT_CONTAINER,GameAsset.generateUid(GameAsset.assetType_OBJECT_CONTAINER),GameObject.objectType_CONTAINER,"chest","A rough wooden chest, with an old iron lock, two leather straps for handles, and cast iron hinges. It looks heavy.",true,false,true,new ArrayList<GameItem>(),true,true,50,50);
			GameObject.add(chest);
		}
		//GameRoom
		if(GameRoom.getRoomTemplates() == null || GameRoom.getRoomTemplates().size() == 0) {
			GameRoom defaultRoom = new GameRoom(GameAsset.assetType_ROOM,"uid",0,"This is a very spooky-looking room. Lots of cobwebs and dust and things like that.",3,3,1,GameRoom.makeRoomVolume(3,3,1));
			GameRoom.add(defaultRoom);
			List<GameVolume> octagonalRoomInterior = new ArrayList<GameVolume>();
			//TODO: set up the interior so that the diagonals are 7x7 & the sides are 10x1 -> 5 units high, with pillars, objects, items, enemies, etc.
			GameRoom octagonalCrypt10 = new GameRoom(GameAsset.assetType_ROOM,"uid",1,"A dark and dank octagonal crypt. There is a large stone fountain in the center of the room. You can hear the sound of running water coming from it. The EAST and WEST walls of the room each house an arched DOOR, both of which seem to be securely latched. The NORTH wall is covered in moth-eaten tapestries depicting a romanticized version of an ancient battle. The SOUTH wall is obscured by rows of stacked boxes and crates, all rotten and soggy. There is a sputtering torch mounted on a stand near the WEST DOOR.",24,24,5,GameRoom.makeRoomVolume(24,24,5));
			GameRoom.add(octagonalCrypt10);
		}
		//GameSpell
		if(GameSpell.getSpellTemplates() == null || GameSpell.getSpellTemplates().size() == 0) {
			GameSpell lightning = new GameSpell(GameAsset.assetType_SPELL,"uid","lightning","An arc of electricity leaps from your hand to shock and stun an enemy.",2,3,false,0,null);
			GameSpell.add(lightning);
			GameSpell fireBolt = new GameSpell(GameAsset.assetType_SPELL,"uid","fire bolt","A small bolt of flame shoots from your extended fingertip.",0,0,false,0,null);
			GameSpell.add(fireBolt);
			GameSpell thunder = new GameSpell(GameAsset.assetType_SPELL,"uid","thunder","A thunderclap erupts from your mouth as you utter the incantation, buffeting an enemy with a shockwave of sound.",2,4,false,0,null);
			GameSpell.add(thunder);
		}
	}
}
