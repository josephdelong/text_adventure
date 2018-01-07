package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.GameItem;
import game.GameItemPrefix;
import game.GameItemSuffix;
import game.GameRoom;
import game.GameVolume;

public class Tester {

	protected static GameRoom room = null;
	
	public static void main(String[] args) {
//		for(int i = 0; i < 100; i++) {
//			System.out.println(rollDie(20,0,true));
//		}
/*		int length = 100;
		int width = 100;
		int height = 100;
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
					if(x == length && y == width && z == height) {
						Map<String,List<GameEnemy>> allEnemies = new HashMap<String,List<GameEnemy>>();
						GameEnemy testEnemy = new GameEnemy("practicedummy", "A straw-filled burlap sack affixed to a wooden facsimile of a humanoid, with a big red target painted on it.", -1, -1, "null", 0, null, 0, 0);
						List<GameEnemy> enemies = new ArrayList<GameEnemy>();
						enemies.add(testEnemy);
						allEnemies.put(testEnemy.name,enemies);
						interior.add(new GameVolume(x,y,z,true,passableDirs,false,(byte)0b00_00,null,allEnemies,null));
					} else {
						interior.add(new GameVolume(x,y,z,true,passableDirs,false,(byte)0b00_00,null,null,null));
					}
				}
			}
		}
		room = new GameRoom(1,"A dark and dank octagonal crypt. There is a large stone fountain in the center of the room. You can hear the sound of running water coming from it. The EAST and WEST walls of the room each house an arched DOOR, both of which seem to be securely latched. The NORTH wall is covered in moth-eaten tapestries depicting a romanticized version of an ancient battle. The SOUTH wall is obscured by rows of stacked boxes and crates, all rotten and soggy. There is a sputtering torch mounted on a stand near the WEST DOOR.",length,width,height,interior);
		int targetX = 55;
		int targetY = 90;
		int targetZ = 25;
		List<Integer> trajectoryPath = getTraversalPath(room.interior.get(0),room.interior.get(((targetZ - 1) * room.width * room.length) + ((targetY - 1) * room.length) + (targetX - 1)));
		System.out.println("Trajectory of \"straight-line\" path from [1,1,1] to [" + targetX + "," + targetY + "," + targetZ + "] is:");
		for(int i: trajectoryPath) {
			System.out.println(i);
		}
		System.out.println("GameVolumes indicated by calculated trajectory path:");
		List<GameVolume> traveledVolumes = new ArrayList<GameVolume>();
		for(int i: trajectoryPath) {
			traveledVolumes.add(room.interior.get(i));
		}
		for(GameVolume g: traveledVolumes) {
			System.out.println("[" + g.xPos + "," + g.yPos + "," + g.zPos + "]");
		}
		System.out.println("");*/
/*
		for(byte z = 0b00; z <= 0b10; z++) {
			for(byte y = 0b00; y <= 0b10; y++) {
				for(byte x = 0b00; x <= 0b10; x++) {
					System.out.println(x|y << 2|z << 4);
				}
			}
		}

		//for an axis of movement, a 0 indicates NEGATIVE movement, a 1 indicates NO movement, and a 2 represents POSITIVE movement
		//to compute what direction to move (byte value), we determine the -/0/+ movement per axis, and bit shift accordingly
		//for the X axis, we left bit shift by 0 (x << 0) for consistency's sake only, as this does nothing in practice
		//for the Y axis, we left bit shift by 2 (y << 2)
		//for the Z axis, we left bit shift by 4 (z << 4)
		//then we can combine these byte values into a combined value using the bitwise or operator |
		System.out.println("[-X,-Y,-Z] (SOUTHWEST & DOWN): " + (0 << 0 | 0 << 2 | 0 << 4));//0
		System.out.println("[0X,-Y,-Z] (SOUTH & DOWN): " + (1 << 0 | 0 << 2 | 0 << 4));//1
		System.out.println("[+X,-Y,-Z] (SOUTHEAST & DOWN): " + (2 << 0 | 0 << 2 | 0 << 4));//2
		
		System.out.println("[-X,0Y,-Z] (WEST & DOWN): " + (0 << 0 | 1 << 2 | 0 << 4));//4
		System.out.println("[0X,0Y,-Z] (DOWN): " + (1 << 0 | 1 << 2 | 0 << 4));//5
		System.out.println("[+X,0Y,-Z] (EAST & DOWN): " + (2 << 0 | 1 << 2 | 0 << 4));//6
		
		System.out.println("[-X,+Y,-Z] (NORTHWEST & DOWN): " + (0 << 0 | 2 << 2 | 0 << 4));//8
		System.out.println("[0X,+Y,-Z] (NORTH & DOWN): " + (1 << 0 | 2 << 2 | 0 << 4));//9
		System.out.println("[+X,+Y,-Z] (NORTHEAST & DOWN): " + (2 << 0 | 2 << 2 | 0 << 4));//10
		
		System.out.println("[-X,-Y,0Z] (SOUTHWEST): " + (0 << 0 | 0 << 2 | 1 << 4));//16
		System.out.println("[0X,-Y,0Z] (SOUTH): " + (1 << 0 | 0 << 2 | 1 << 4));//17
		System.out.println("[+X,-Y,0Z] (SOUTHEAST): " + (2 << 0 | 0 << 2 | 1 << 4));//18
		
		System.out.println("[-X,0Y,0Z] (WEST): " + (0 << 0 | 1 << 2 | 1 << 4));//20
		System.out.println("[0X,0Y,0Z] (NO MOVEMENT): " + (1 << 0 | 1 << 2 | 1 << 4));//21
		System.out.println("[+X,0Y,0Z] (EAST): " + (2 << 0 | 1 << 2 | 1 << 4));//22
		
		System.out.println("[-X,+Y,0Z] (NORTHWEST): " + (0 << 0 | 2 << 2 | 1 << 4));//24
		System.out.println("[0X,+Y,0Z] (NORTH): " + (1 << 0 | 2 << 2 | 1 << 4));//25
		System.out.println("[+X,+Y,0Z] (NORTHEAST): " + (2 << 0 | 2 << 2 | 1 << 4));//26
		
		System.out.println("[-X,-Y,+Z] (SOUTHWEST & UP): " + (0 << 0 | 0 << 2 | 2 << 4));//32
		System.out.println("[0X,-Y,+Z] (SOUTH & UP): " + (1 << 0 | 0 << 2 | 2 << 4));//33
		System.out.println("[+X,-Y,+Z] (SOUTHEAST & UP): " + (2 << 0 | 0 << 2 | 2 << 4));//34
		
		System.out.println("[-X,0Y,+Z] (WEST & UP): " + (0 << 0 | 1 << 2 | 2 << 4));//36
		System.out.println("[0X,0Y,+Z] (UP): " + (1 << 0 | 1 << 2 | 2 << 4));//37
		System.out.println("[+X,0Y,+Z] (EAST & UP): " + (2 << 0 | 1 << 2 | 2 << 4));//38
		
		System.out.println("[-X,+Y,+Z] (NORTHWEST & UP): " + (0 << 0 | 2 << 2 | 2 << 4));//40
		System.out.println("[0X,+Y,+Z] (NORTH & UP): " + (1 << 0 | 2 << 2 | 2 << 4));//41
		System.out.println("[+X,+Y,+Z] (NORTHEAST & UP): " + (2 << 0 | 2 << 2 | 2 << 4));//42
*/
		String name = "Skeleton";
		int currentHp = 5;
		int maxHp= 10;
		double distance = 1.732;
		byte direction = 0b10_10_01;
		System.out.println(String.format("%s: %d/%dHP - %.2f meters, %s",name.toUpperCase(),currentHp,maxHp,distance,GameState.getDirectionName(direction)));
		System.out.println("");

/*
		Prefixes
			JAGGED (itemType_WEAPON)(attackType_MELEE)(damageType_SLASH) = chance to apply BLEED condition 
			VENEMOUS (itemType_WEAPON)(attackType_ANY)(damageType_ANY) = chance to apply POISON condition
			MIGHTY (itemType_WEAPON)(attackType_MELEE)(damageType_BLUNT) = chance to apply STUN condition
			WOUNDING (itemType_WEAPON)(attackType_ANY)(damageType_PIERCE) = chance to apply FATIGUE condition
			MYSTIC (itemType_WEAPON)(attackType_MAGIC)(damageType_MAGIC) = chance to cast EXTRA spell effect
			BLESSED (itemType_WEAPON)(attackType_ANY)(damageType_ANY) = chance attack will do DOUBLE damage
			CURSED (itemType_WEAPON)(attackType_ANY(damageType_ANY) = chance attack will do HALF damage (or FAIL?)
			
			POTENT (itemType_WEAPON) = applies positive damage multiplier * rarityLevel
			LETHARGIC (itemType_WEAPON) = applies negative damage multiplier * rarityLevel
			DURABLE (itemType_ANY) = applies positive durability multiplier * rarityLevel
			BRITTLE (itemType_ANY) = applies negative durability multiplier * rarityLevel
			ARCANE (itemType_ANY) = increased SP & maxSP * rarityLevel
			SANGUINE (itemType_ANY) = increased HP & maxHP * rarityLevel
			LARGE (itemType_WEAPON) = increased REACH / RANGE, itemWeight & damageDealt
			SMALL (itemType_WEAPON) = decreased REACH / RANGE, itemWeight & damageDealt
			
			GUARDING (itemType_ANY) = increased damageReduction * rarityLevel
			SHIELDING (itemType_ANY) = increased magicResistance * rarityLevel
			SMITING (itemType_WEAPON)(attackType_MELEE) = increased attackBonusMelee * rarityLevel
			SEEKING (itemType_WEAPON)(attackType_RANGED) = increased attackBonusRanged * rarityLevel
			SCRYING (itemType_WEAPON)(attackType_MAGIC) = increased attackBonusMagic * rarityLevel
		Suffixes
			FROST (itemType_WEAPON) = deals bonus damageType_COLD * rarityLevel
			FIRE (itemType_WEAPON) = deals bonus damageType_FIRE * rarityLevel
			LIGHTNING (itemType_WEAPON) = deals bonus damageType_ELECTRIC * rarityLevel
			FORCE (itemType_WEAPON) = deals bonus damage_FORCE * rarityLevel
			INSANITY (itemType_WEAPON) = deals bonus damage_PSYCHIC * rarityLevel
			GRAVITY (itemType_WEAPON) = deals bonus damage_CRUSHING * rarityLevel
			HEALING (itemType_ANY) = heals wielder damage_HEALING * rarityLevel on attack/use
			
			FORTUNE (itemType_ANY) = increased LOOT chance * rarityLevel
			THE PAUPER (itemType_ANY) = decreased LOOT chance * rarityLevel
			THE VAMPIRE (itemType_WEAPON) = chance to apply LIFE_DRAIN * rarityLevel
			ABSORPTION (itemType_WORN) = chance damageType_MAGIC taken restores SP * rarityLevel
			DOOM (itemType_WEAPON) = chance to instaKill * rarityLevel
			
			PROTECTION (itemType_ANY) = increased armorClass * rarityLevel
			RESTORATION (itemType_ANY) = increased lifeRegen * rarityLevel
			
			THE BULL (itemType_ANY) = bonus to abilityType_STR * rarityLevel (BRAWN)
			THE BOAR (itemType_ANY) = bonus to abilityType_CON * rarityLevel (VIGOR)
			THE HAWK (itemType_ANY) = bonus to abilityType_DEX * rarityLevel (SPEED)
			THE CROW (itemType_ANY) = bonus to abilityType_INT * rarityLevel (LOGIC)
			THE WOLF (itemType_ANY) = bonus to abilityType_WIS * rarityLevel (SENSE)
			THE SEAL (itemType_ANY) = bonus to abilityType_CHA * rarityLevel (CHARM)
			
			attackType_MELEE = 0;
			attackType_RANGED = 1;
			attackType_THROWN = 2;
			attackType_MAGIC = 3;
			
			damageType_BLUDGEON = 0;
			damageType_SLASH = 1;
			damageType_PIERCE = 2;
			damageType_MAGIC = 3;
		*/
//		String[] itemNameBase = new String[] {"Dagger","Quarterstaff","Shortsword","Longsword","Wand","Shortbow","Longbow","Mace","Cudgel","Warhammer","Axe","Broadaxe","Waraxe","Glaive","Halberd","Pike","Buckler","Shield","Towershield","Necklace","Ring","Cuirass","Greaves","Gloves","Gauntlets","Boots","Cape","Pants","Wraps","Helm","Crown","Belt","Quiver","Shirt"};
//		String[] itemPrefix = new String[] {"Ardent","Stout","Powerful","Durable","Steadfast","Quick","Blessed","Cursed","Enduring","Stalwart","Nimble","Daring","Sylvan","Potent","Enervating","Guarding","Observant","Watchful","Greedy","Lucky","Punishing","Devastating","Crushing","Shocking","Burning","Freezing","Draining","Poisoning","Sharp"};
//		String[] itemSuffix = new String[] {"Protection","Restoration","the Vampire","Doom","Darkness","Light","the Elements","Fortune","Brawn","Health","Energy","Plenty","Destruction","Pain","Cowardice","Fear","Malice","Sickness","Heroism","Skill","Ability","Absorption","Amelioration","Awesome"};
//		
//		for(int i = 0; i < 100; i++) {
//			String itemName = "";
//			if(Math.random() > 0.75d) {
//				itemName += itemPrefix[(int)(Math.random() * itemPrefix.length)] + " ";
//				if(Math.random() > 0.95d) {
//					itemName += itemPrefix[(int)(Math.random() * itemPrefix.length)] + " ";
//				}
//			}
//			itemName += itemNameBase[(int)(Math.random() * itemNameBase.length)];
//			if(Math.random() > 0.75) {
//				itemName += " of ";
//				itemName += itemSuffix[(int)(Math.random() * itemSuffix.length)];
//				if(Math.random() > 0.95) {
//					itemName += " and " + itemSuffix[(int)(Math.random() * itemSuffix.length)];
//				}
//			}
//			System.out.println(itemName);
//		}
//		System.out.println("");
		
		List<GameItemPrefix> testPrefixes = new ArrayList<GameItemPrefix>();
		GameItemPrefix prefixDurable = new GameItemPrefix("Durable",GameItem.itemType_ARMOR,-1,-1,new String[]{"maxDurability","tempDurability"},new String[]{"X","X"},new double[]{2,2},new double[]{15,15},50.0);
		testPrefixes.add(prefixDurable);
		GameItemPrefix prefixCrushing = new GameItemPrefix("Crushing",GameItem.itemType_WEAPON,GameItem.attackType_MELEE,GameItem.damageType_BLUDGEON,new String[]{"damageDealt"},new String[]{"damage_CRUSH"},new double[]{5},new double[]{10},50.0);
		testPrefixes.add(prefixCrushing);
		GameItemPrefix prefixShocking = new GameItemPrefix("Potent",GameItem.itemType_WEAPON,-1,-1,new String[]{"damageDealt"},new String[]{"X"},new double[]{2},new double[]{2},50.0);
		testPrefixes.add(prefixShocking);
		GameItemPrefix prefixBurning = new GameItemPrefix("Burning",GameItem.itemType_WEAPON,-1,-1,new String[]{"damageDealt"},new String[]{"damage_FIRE"},new double[]{5},new double[]{10},50.0);
		testPrefixes.add(prefixBurning);
		GameItemPrefix prefixFreezing = new GameItemPrefix("Freezing",GameItem.itemType_WEAPON,-1,-1,new String[]{"damageDealt"},new String[]{"damage_COLD"},new double[]{5},new double[]{10},50.0);
		testPrefixes.add(prefixFreezing);
		GameItemPrefix prefixSharp = new GameItemPrefix("Sharp",GameItem.itemType_WEAPON,1,-1,new String[]{"damageDealt"},new String[]{"damage_SLASH"},new double[]{5},new double[]{10},50.0);
		testPrefixes.add(prefixSharp);
		GameItemPrefix prefixLarge = new GameItemPrefix("Large",GameItem.itemType_WEAPON,-1,-1,new String[]{"minRange","maxRange","reach","weight","damageDealt"},new String[]{"X","X","X","X","X"},new double[]{2,2,2,2,2},new double[]{2,2,2,2,2},75.0);
		testPrefixes.add(prefixLarge);//increased REACH / RANGE, itemWeight & damageDealt
		GameItemPrefix prefixSmall = new GameItemPrefix("Small",GameItem.itemType_WEAPON,-1,-1,new String[]{"minRange","maxRange","reach","weight","damageDealt"},new String[]{"/","/","/","/","/"},new double[]{2,2,2,2,2},new double[]{2,2,2,2,2},25.0);
		testPrefixes.add(prefixSmall);
		
		List<GameItemSuffix> testSuffixes = new ArrayList<GameItemSuffix>();
		GameItemSuffix suffixHealth = new GameItemSuffix("Health","itemType_ANY",-1,-1,new String[]{"maxHp","tempHp"},new String[]{"+","+"},new double[]{5,5},new double[]{25,25},50.0);
		testSuffixes.add(suffixHealth);
		GameItemSuffix suffixEnergy = new GameItemSuffix("Energy","itemType_ANY",-1,-1,new String[]{"maxSp","tempSp"},new String[]{"+","+"},new double[]{5,5},new double[]{25,25},50.0);
		testSuffixes.add(suffixEnergy);
		GameItemSuffix suffixProtection = new GameItemSuffix("Protection","itemType_ANY",-1,-1,new String[]{"armorClass"},new String[]{"+"},new double[]{5},new double[]{10},50.0);
		testSuffixes.add(suffixProtection);
		GameItemSuffix suffixFortune = new GameItemSuffix("Fortune","itemType_ANY",-1,-1,new String[]{"ATTRIBUTE","ATTRIBUTE"},new String[]{"goldFind","lootFind"},new double[]{10,1},new double[]{50,5},100.0);
		testSuffixes.add(suffixFortune);
		GameItemSuffix suffixVampire = new GameItemSuffix("the Vampire",GameItem.itemType_WEAPON,-1,-1,new String[]{"CONDITION"},new String[]{"lifeSteal"},new double[]{5},new double[]{10},50.0);
		testSuffixes.add(suffixVampire);
		GameItemSuffix suffixRestoration = new GameItemSuffix("Restoration",GameItem.itemType_ARMOR,-1,-1,new String[]{"CONDITION?"},new String[]{"healthRegen"},new double[]{5},new double[]{10},50.0);
		testSuffixes.add(suffixRestoration);
		
		List<GameItem> testItems = new ArrayList<GameItem>();
		GameItem dagger = new GameItem(new GameItem(new GameItem("Dagger",1.0,5.0,1,-1,-1,"null","A small, quick steel blade suitable for dispatching foes in close quarters. Also useful as a letter opener.",4,GameItem.MAIN_HAND,false),0,0,0,0,0,0,0,0,true,10,10,GameItem.attackType_MELEE,0,1,0,GameItem.damageType_PIERCE),GameItem.itemType_WEAPON,1,4,1.0,new GameItemPrefix[]{},new GameItemSuffix[]{});
		testItems.add(dagger);
		GameItem longBow = new GameItem(new GameItem(new GameItem("Longbow",5.0,7.5,1,-1,-1,"null","A stout bow that stands about as tall as the person wielding it. Although this weapon is large and cumbersome, if used skillfully, it can hit targets up to 300 feet away.",6,GameItem.MAIN_HAND,false),0,0,0,0,0,0,1,0,true,30,30,GameItem.attackType_RANGED,6,60,0,GameItem.damageType_PIERCE),GameItem.itemType_WEAPON,1,8,1.0,new GameItemPrefix[]{},new GameItemSuffix[]{});
		testItems.add(longBow);
		GameItem mace = new GameItem(new GameItem(new GameItem("Mace",5.0,3.25,1,-1,-1,"null","A solid steel weight affixed to the top of a sturdy wooden handle, with hemishperical protrusions across the surface. This weapon is meant for smashing and bashing and so it doesn't require a light touch.",8,GameItem.MAIN_HAND,false),0,0,0,0,0,1,0,0,true,50,50,GameItem.attackType_MELEE,0,1,0,GameItem.damageType_BLUDGEON),GameItem.itemType_WEAPON,1,8,1.0,new GameItemPrefix[]{},new GameItemSuffix[]{});
		testItems.add(mace);
		GameItem wand = new GameItem(new GameItem(new GameItem("Wand",1.25,15.0,1,-1,-1,"null","A slender rod of wood, finely finished with a magical gem in the pommel. This weapon is capable of having Spells imprinted on it, and once properly attuned, casting the Spell(s) which have been imprinted.",0,GameItem.MAIN_HAND,false),0,0,0,0,0,0,0,1,true,25,25,GameItem.attackType_MAGIC,0,20,0,GameItem.damageType_MAGIC),GameItem.itemType_WEAPON,0,0,1.5,new GameItemPrefix[]{},new GameItemSuffix[]{});
		testItems.add(wand);
		GameItem warhammer = new GameItem(new GameItem(new GameItem("Warhammer",15.0,7.5,1,-1,-1,"null","A mighty mallet of wood and steel, with a 2-handed haft. This weapon is suitable for breaking bones and bruising flesh.",10,GameItem.MAIN_HAND,false),0,0,0,0,0,0,0,0,true,50,50,GameItem.attackType_MELEE,0,1,0,GameItem.damageType_BLUDGEON),GameItem.itemType_WEAPON,1,10,1.25,new GameItemPrefix[]{},new GameItemSuffix[]{});
		testItems.add(warhammer);

//		GameItem dagger = new GameItem("itemType_WEAPON","Dagger","MAIN_HAND",0,0,0,0,0,0,0,0,true,25,25,0,0,0,1,1,1,5,1.0,new GameItemPrefix[]{},new GameItemSuffix[]{});
//		testItems.add(dagger);
//		GameItem longbow = new GameItem("itemType_WEAPON","LongBow","MAIN_HAND",0,0,0,0,0,0,1,0,true,25,25,1,2,20,0,2,3,13,1.0,new GameItemPrefix[]{},new GameItemSuffix[]{});
//		testItems.add(longbow);
//		GameItem mace = new GameItem("itemType_WEAPON","Mace","MAIN_HAND",0,0,0,0,0,1,0,0,true,25,25,0,0,0,1,0,1,5,1.0,new GameItemPrefix[]{},new GameItemSuffix[]{});
//		testItems.add(mace);
//		GameItem wand = new GameItem("itemType_WEAPON","Wand","MAIN_HAND",0,0,0,0,0,0,0,1,true,25,25,3,0,5,1,3,1,5,1.0,new GameItemPrefix[]{},new GameItemSuffix[]{});
//		testItems.add(wand);
//		GameItem warhammer = new GameItem("itemType_WEAPON","Warhammer","MAIN_HAND",0,0,0,0,0,1,0,0,true,25,25,0,0,0,1,0,1,5,1.0,new GameItemPrefix[]{},new GameItemSuffix[]{});
//		testItems.add(warhammer);

//		for(GameItem i: testItems) {
//			List<GameItemPrefix> prefixes = i.getApplicablePrefixes(i,testPrefixes);
//			List<GameItemSuffix> suffixes = i.getApplicableSuffixes(i,testSuffixes);
//			System.out.println("Item:\n\t" + i.name);
//			System.out.println("Applicable Prefixes:");
//			for(GameItemPrefix p: prefixes) {
//				System.out.println("\t" + p.name);
//			}
//			System.out.println("Applicable Suffixes:");
//			for(GameItemSuffix s: suffixes) {
//				System.out.println("\t" + s.name);
//			}
//			System.out.println();
//		}
		
		for(int i = 0; i < testItems.size(); i++) {
			GameItem item = testItems.get(i);
			if(Math.random() > 0.75d) {
				item = GameItem.addRandomPrefix(item,1,testPrefixes);
				if(Math.random() > 0.95d) {
					item = GameItem.addRandomPrefix(item,1,testPrefixes);
				}
			}
			if(Math.random() > 0.75) {
				item = GameItem.addRandomSuffix(item,1,testSuffixes);
				if(Math.random() > 0.95) {
					item = GameItem.addRandomSuffix(item,1,testSuffixes);
				}
			}
			testItems.set(i,item);
		}
		
		for(GameItem i: testItems) {
			System.out.println(i.printStats());
		}
		
	}
	
	
	
}
