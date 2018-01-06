package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		Tester tester = new Tester();
		
		List<itemPrefixClass> testPrefixes = new ArrayList<itemPrefixClass>();
		itemPrefixClass prefixDurable = tester.new itemPrefixClass("Durable","itemType_ARMOR",-1,-1,new String[]{"maxDurability","tempDurability"},new String[]{"X","X"},new double[]{2,2},new double[]{15,15},50.0);
		testPrefixes.add(prefixDurable);
		itemPrefixClass prefixCrushing = tester.new itemPrefixClass("Crushing","itemType_WEAPON",0,0,new String[]{"damageDealt"},new String[]{"damage_CRUSH"},new double[]{5},new double[]{10},50.0);
		testPrefixes.add(prefixCrushing);
		itemPrefixClass prefixShocking = tester.new itemPrefixClass("Potent","itemType_WEAPON",-1,-1,new String[]{"damageDealt"},new String[]{"X"},new double[]{2},new double[]{2},50.0);
		testPrefixes.add(prefixShocking);
		itemPrefixClass prefixBurning = tester.new itemPrefixClass("Burning","itemType_WEAPON",-1,-1,new String[]{"damageDealt"},new String[]{"damage_FIRE"},new double[]{5},new double[]{10},50.0);
		testPrefixes.add(prefixBurning);
		itemPrefixClass prefixFreezing = tester.new itemPrefixClass("Freezing","itemType_WEAPON",-1,-1,new String[]{"damageDealt"},new String[]{"damage_COLD"},new double[]{5},new double[]{10},50.0);
		testPrefixes.add(prefixFreezing);
		itemPrefixClass prefixSharp = tester.new itemPrefixClass("Sharp","itemType_WEAPON",1,-1,new String[]{"damageDealt"},new String[]{"damage_SLASH"},new double[]{5},new double[]{10},50.0);
		testPrefixes.add(prefixSharp);
		itemPrefixClass prefixLarge = tester.new itemPrefixClass("Large","itemType_WEAPON",-1,-1,new String[]{"minRange","maxRange","reach","weight","damageDealt"},new String[]{"X","X","X","X","X"},new double[]{2,2,2,2,2},new double[]{2,2,2,2,2},75.0);
		testPrefixes.add(prefixLarge);//increased REACH / RANGE, itemWeight & damageDealt
		itemPrefixClass prefixSmall = tester.new itemPrefixClass("Small","itemType_WEAPON",-1,-1,new String[]{"minRange","maxRange","reach","weight","damageDealt"},new String[]{"/","/","/","/","/"},new double[]{2,2,2,2,2},new double[]{2,2,2,2,2},25.0);
		testPrefixes.add(prefixSmall);
		
		List<itemSuffixClass> testSuffixes = new ArrayList<itemSuffixClass>();
		itemSuffixClass suffixHealth = tester.new itemSuffixClass("Health","itemType_ANY",-1,-1,new String[]{"maxHp","tempHp"},new String[]{"+","+"},new double[]{5,5},new double[]{25,25},50.0);
		testSuffixes.add(suffixHealth);
		itemSuffixClass suffixEnergy = tester.new itemSuffixClass("Energy","itemType_ANY",-1,-1,new String[]{"maxSp","tempSp"},new String[]{"+","+"},new double[]{5,5},new double[]{25,25},50.0);
		testSuffixes.add(suffixEnergy);
		itemSuffixClass suffixProtection = tester.new itemSuffixClass("Protection","itemType_ANY",-1,-1,new String[]{"armorClass"},new String[]{"+"},new double[]{5},new double[]{10},50.0);
		testSuffixes.add(suffixProtection);
		itemSuffixClass suffixFortune = tester.new itemSuffixClass("Fortune","itemType_ANY",-1,-1,new String[]{"ATTRIBUTE","ATTRIBUTE"},new String[]{"goldFind","lootFind"},new double[]{10,1},new double[]{50,5},100.0);
		testSuffixes.add(suffixFortune);
		itemSuffixClass suffixVampire = tester.new itemSuffixClass("the Vampire","itemType_WEAPON",-1,-1,new String[]{"CONDITION"},new String[]{"lifeSteal"},new double[]{5},new double[]{10},50.0);
		testSuffixes.add(suffixVampire);
		itemSuffixClass suffixRestoration = tester.new itemSuffixClass("Restoration","itemType_ARMOR",-1,-1,new String[]{"CONDITION?"},new String[]{"healthRegen"},new double[]{5},new double[]{10},50.0);
		testSuffixes.add(suffixRestoration);
		
		List<itemClass> testItems = new ArrayList<itemClass>();
		itemClass dagger = tester.new itemClass("itemType_WEAPON","Dagger","MAIN_HAND",0,0,0,0,0,0,0,0,true,25,25,0,0,0,1,1,1,5,1.0,new itemPrefixClass[]{},new itemSuffixClass[]{});
		testItems.add(dagger);
		itemClass longbow = tester.new itemClass("itemType_WEAPON","LongBow","MAIN_HAND",0,0,0,0,0,0,1,0,true,25,25,1,2,20,0,2,3,13,1.0,new itemPrefixClass[]{},new itemSuffixClass[]{});
		testItems.add(longbow);
		itemClass mace = tester.new itemClass("itemType_WEAPON","Mace","MAIN_HAND",0,0,0,0,0,1,0,0,true,25,25,0,0,0,1,0,1,5,1.0,new itemPrefixClass[]{},new itemSuffixClass[]{});
		testItems.add(mace);
		itemClass wand = tester.new itemClass("itemType_WEAPON","Wand","MAIN_HAND",0,0,0,0,0,0,0,1,true,25,25,3,0,5,1,3,1,5,1.0,new itemPrefixClass[]{},new itemSuffixClass[]{});
		testItems.add(wand);
		itemClass warhammer = tester.new itemClass("itemType_WEAPON","Warhammer","MAIN_HAND",0,0,0,0,0,1,0,0,true,25,25,0,0,0,1,0,1,5,1.0,new itemPrefixClass[]{},new itemSuffixClass[]{});
		testItems.add(warhammer);

//		for(itemClass i: testItems) {
//			List<itemPrefixClass> prefixes = tester.getApplicablePrefixes(i,testPrefixes);
//			List<itemSuffixClass> suffixes = tester.getApplicableSuffixes(i,testSuffixes);
//			System.out.println("Item:\n\t" + i.name);
//			System.out.println("Applicable Prefixes:");
//			for(itemPrefixClass p: prefixes) {
//				System.out.println("\t" + p.name);
//			}
//			System.out.println("Applicable Suffixes:");
//			for(itemSuffixClass s: suffixes) {
//				System.out.println("\t" + s.name);
//			}
//			System.out.println();
//		}
		
		for(int i = 0; i < testItems.size(); i++) {
			itemClass item = testItems.get(i);
			if(Math.random() > 0.75d) {
				item = tester.addRandomPrefix(item,1,testPrefixes);
				if(Math.random() > 0.95d) {
					item = tester.addRandomPrefix(item,1,testPrefixes);
				}
			}
			if(Math.random() > 0.75) {
				item = tester.addRandomSuffix(item,1,testSuffixes);
				if(Math.random() > 0.95) {
					item = tester.addRandomSuffix(item,1,testSuffixes);
				}
			}
			testItems.set(i,item);
		}
		
		for(itemClass i: testItems) {
			System.out.println(i.printStats());
		}
		
	}
	
	protected itemClass addRandomPrefix(itemClass item, int numNewPrefixes, List<itemPrefixClass> allPrefixes) {
		List<itemPrefixClass> filteredPrefixes = getApplicablePrefixes(item,allPrefixes);
		if(filteredPrefixes.size() > 1) {
			itemPrefixClass[] prefixes = item.prefixes;
			itemPrefixClass[] newPrefixes = new itemPrefixClass[prefixes.length + numNewPrefixes];
			for(int i = 0; i < prefixes.length; i++) {
				newPrefixes[i] = prefixes[i];
			}
			for(int i = prefixes.length; i < newPrefixes.length; i++) {
				newPrefixes[i] = filteredPrefixes.get((int)(Math.random() * filteredPrefixes.size()));
			}
			item.prefixes = newPrefixes;
		}
		return item;
	}
	
	protected itemClass addRandomSuffix(itemClass item, int numNewSuffixes, List<itemSuffixClass> allSuffixes) {
		List<itemSuffixClass> filteredSuffixes = getApplicableSuffixes(item,allSuffixes);
		if(filteredSuffixes.size() > 1) {
			itemSuffixClass[] suffixes = item.suffixes;
			itemSuffixClass[] newSuffixes = new itemSuffixClass[suffixes.length + numNewSuffixes];
			for(int i = 0; i < suffixes.length; i++) {
				newSuffixes[i] = suffixes[i];
			}
			for(int i = suffixes.length; i < newSuffixes.length; i++) {
				newSuffixes[i] = filteredSuffixes.get((int)(Math.random() * filteredSuffixes.size()));
			}
			item.suffixes = newSuffixes;
		}
		return item;
	}
	
	protected List<itemPrefixClass> getApplicablePrefixes(itemClass item, List<itemPrefixClass> allPrefixes) {
		List<itemPrefixClass> filteredPrefixes = new ArrayList<itemPrefixClass>();
		for(itemPrefixClass p: allPrefixes) {
			if(		(p.itemType.equals(item.itemType) || p.itemType.equals("itemType_ANY")) && 
					(p.attackType == item.attackType || p.attackType == -1) && 
					(p.damageType == item.damageType || p.damageType == -1)		) {
				filteredPrefixes.add(p);
			}
		}
		return filteredPrefixes;
	}
	
	protected List<itemSuffixClass> getApplicableSuffixes(itemClass item, List<itemSuffixClass> allSuffixes) {
		List<itemSuffixClass> filteredSuffixes = new ArrayList<itemSuffixClass>();
		for(itemSuffixClass s: allSuffixes) {
			if(		(s.itemType.equals(item.itemType) || s.itemType.equals("itemType_ANY")) && 
					(s.attackType == item.attackType || s.attackType == -1) && 
					(s.damageType == item.damageType || s.damageType == -1)		) {
				filteredSuffixes.add(s);
			}
		}
		return filteredSuffixes;
	}
	
	protected class itemClass {
		protected String itemType;
		protected String name;
		protected String equipType;
		protected int armorClass;
		protected int damageReduction;
		protected int magicResistance;
		protected int healthPoints;
		protected int spellPoints;
		protected int attackBonusMelee;
		protected int attackBonusRanged;
		protected int attackBonusMagic;
		protected boolean isBreakable;
		protected int curDurability;
		protected int maxDurability;
		protected int attackType;
		protected int minRange;
		protected int maxRange;
		protected int reachDistance;
		protected int damageType;
		protected int minDamage;
		protected int maxDamage;
		protected double rarityLevel;//a multiplier for any instances of prefix/suffix bonuses (so that they scale appropriately with base item rarity)
		protected itemPrefixClass[] prefixes;
		protected itemSuffixClass[] suffixes;
		
		protected itemClass(String itemType, String name, String equipType, int armorClass, int damageReduction, int magicResistance, int healthPoints, 
				int spellPoints, int attackBonusMelee, int attackBonusRanged, int attackBonusMagic, boolean isBreakable, int curDurability, 
				int maxDurability, int attackType, int minRange, int maxRange, int reachDistance, int damageType, int minDamage, int maxDamage, 
				double rarityLevel, itemPrefixClass[] prefixes, itemSuffixClass[] suffixes) {
			this.itemType = itemType;
			this.name = name;
			this.equipType = equipType;
			this.armorClass = armorClass;
			this.damageReduction = damageReduction;
			this.magicResistance = magicResistance;
			this.healthPoints = healthPoints;
			this.spellPoints = spellPoints;
			this.attackBonusMelee = attackBonusMelee;
			this.attackBonusRanged = attackBonusRanged;
			this.attackBonusMagic = attackBonusMagic;
			this.isBreakable = isBreakable;
			this.curDurability = curDurability;
			this.maxDurability = maxDurability;
			this.attackType = attackType;
			this.minRange = minRange;
			this.maxRange = maxRange;
			this.reachDistance = reachDistance;
			this.damageType = damageType;
			this.minDamage = minDamage;
			this.maxDamage = maxDamage;
			this.rarityLevel = rarityLevel;
			this.prefixes = prefixes;
			this.suffixes = suffixes;
		}
		
		protected String getFullItemName() {
			String itemName = "";
			for(itemPrefixClass p: this.prefixes) {
				itemName += p.name + " ";
			}
			itemName += this.name;
			if(this.suffixes != null && this.suffixes.length > 0) {
				itemName += " of ";
				for(int i = 0; i < this.suffixes.length; i++) {
					itemName += this.suffixes[i].name;
					if(i != this.suffixes.length - 1) {
						itemName += " and ";
					}
				}
			}
			return itemName;
		}
		
		protected String printStats() {
			String statString = "";
			statString += "FullName: " + this.getFullItemName() + "\n";
			statString += "BaseName: " + this.name + "\n";
			statString += "EquipType: " + this.equipType + "\n";
			statString += "ArmorClass: " + this.armorClass + "\n";
			statString += "DamageReduction: " + this.damageReduction + "\n";
			statString += "MagicResistance: " + this.magicResistance + "\n";
			statString += "HP: " + this.healthPoints + "\n";
			statString += "SP: " + this.spellPoints + "\n";
			statString += "MeleeAttackBonus: " + this.attackBonusMelee + "\n";
			statString += "RangedAttackBonus: " + this.attackBonusRanged + "\n";
			statString += "MagicAttackBonus: " + this.attackBonusMagic + "\n";
			statString += "Breakable: " + this.isBreakable + "\n";
			statString += "Durability: " + this.curDurability + "\n";
			statString += "MaxDurability: " + this.maxDurability + "\n";
			statString += "AttackType: " + this.attackType + "\n";
			statString += "MinRange: " + this.minRange + "\n";
			statString += "MaxRange: " + this.maxRange + "\n";
			statString += "Reach: " + this.reachDistance + "\n";
			statString += "DamageType: " + this.damageType + "\n";
			statString += "MinDamage: " + this.minDamage + "\n";
			statString += "MaxDamage: " + this.maxDamage + "\n";
			statString += "Rarity: " + this.rarityLevel + "\n";
			statString += "Prefixes:\n";
			for(itemPrefixClass p: prefixes) {
				statString += "\tName: " + p.name + "\n";
				statString += "\tItemType: " + p.itemType + "\n";
				statString += "\tAttackType: " + p.attackType + "\n";
				statString += "\tDamageType: " + p.damageType + "\n";
				statString += "\tBonusCategories:\n";
				for(String x: p.bonusCategories) {
					statString += "\t\t" + x + "\n";
				}
				statString += "\tBonusTypes:\n";
				for(String x: p.bonusTypes) {
					statString += "\t\t" + x + "\n";
				}
				statString += "\tMinBonuses:\n";
				for(double x: p.minBonuses) {
					statString += "\t\t" + x + "\n";
				}
				statString += "\tMaxBonuses:\n";
				for(double x: p.maxBonuses) {
					statString += "\t\t" + x + "\n";
				}
				statString += "\tBaseValueAdd: " + p.baseValueAdd + "\n";
			}
			statString += "Suffixes:\n";
			for(itemSuffixClass s: suffixes) {
				statString += "\tName: " + s.name + "\n";
				statString += "\tItemType: " + s.itemType + "\n";
				statString += "\tAttackType: " + s.attackType + "\n";
				statString += "\tDamageType: " + s.damageType + "\n";
				statString += "\tBonusCategories:\n";
				for(String x: s.bonusCategories) {
					statString += "\t\t" + x + "\n";
				}
				statString += "\tBonusTypes:\n";
				for(String x: s.bonusTypes) {
					statString += "\t\t" + x + "\n";
				}
				statString += "\tMinBonuses:\n";
				for(double x: s.minBonuses) {
					statString += "\t\t" + x + "\n";
				}
				statString += "\tMaxBonuses:\n";
				for(double x: s.maxBonuses) {
					statString += "\t\t" + x + "\n";
				}
				statString += "\tBaseValueAdd: " + s.baseValueAdd + "\n";
			}
			return statString;
		}
	}
	
	protected class itemPrefixClass {
		protected String name;//shorthand name of the unique combination of bonus stats
		protected String itemType;//controls what types of items this Prefix can be applied to
		protected int attackType;//if itemType != null, controls what types of Weapons this Prefix can be applied to
		protected int damageType;//if attackType != null, further filters what Weapons this Prefix can be applied to
		protected String[] bonusCategories;//the categories of the bonuses (Attribute,Damage,Protection,etc.)
		protected String[] bonusTypes;//the types of bonuses within the categories (sub-type)
		protected double[] minBonuses;//the minimum values for the bonuses (may be the same as maxBonuses)
		protected double[] maxBonuses;//the maximum values for the bonuses (may be the same as minBonuses)
		protected double baseValueAdd;//the base value added to an item when this Prefix is applied
		
		protected itemPrefixClass(String name, String itemType, int attackType, int damageType, String[] bonusCategories, String[] bonusTypes, double[] minBonuses, double[] maxBonuses, double baseValueAdd) {
			this.name = name;
			this.itemType = itemType;
			this.attackType = attackType;
			this.damageType = damageType;
			this.bonusCategories = bonusCategories;
			this.bonusTypes = bonusTypes;
			this.minBonuses = minBonuses;
			this.maxBonuses = maxBonuses;
			this.baseValueAdd = baseValueAdd;
		}
	}
	
	protected class itemSuffixClass {
		protected String name;
		protected String itemType;
		protected int attackType;
		protected int damageType;
		protected String[] bonusCategories;
		protected String[] bonusTypes;
		protected double[] minBonuses;
		protected double[] maxBonuses;
		protected double baseValueAdd;
		
		protected itemSuffixClass(String name, String itemType, int attackType, int damageType, String[] bonusCategories, String[] bonusTypes, double[] minBonuses, double[] maxBonuses, double baseValueAdd) {
			this.name = name;
			this.itemType = itemType;
			this.attackType = attackType;
			this.damageType = damageType;
			this.bonusCategories = bonusCategories;
			this.bonusTypes = bonusTypes;
			this.minBonuses = minBonuses;
			this.maxBonuses = maxBonuses;
			this.baseValueAdd = baseValueAdd;
		}
		
	}
	
	private static int rollDie(int dieVal, int bonus, boolean canCrit) {
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
	
	protected static List<Integer> getTraversalPath(GameVolume from, GameVolume to) {
		List<Integer> traversalPathIndexes = new ArrayList<Integer>();
		if(from.xPos == to.xPos && from.yPos == to.yPos && from.zPos == to.zPos) {
			return traversalPathIndexes;//NO TRAVEL DIRECTION
		}
		GameVolume step = from;
		int xDistance = to.xPos - from.xPos;
		int yDistance = to.yPos - from.yPos;
		int zDistance = to.zPos - from.zPos;
		int totalSteps = Math.max(Math.abs(xDistance),Math.max(Math.abs(yDistance),Math.abs(zDistance)));
		int stepIndex = 0;
		while(stepIndex < totalSteps) {//while not at destination yet
			int nextTraversalDirection = getTraversalDirection(step,to,totalSteps,stepIndex,xDistance,yDistance,zDistance);
			step = getNextVolumeInDirection(step,nextTraversalDirection);
			traversalPathIndexes.add(((step.zPos - 1) * room.width * room.length) + ((step.yPos - 1) * room.length) + (step.xPos - 1));
			stepIndex++;
		}
		return traversalPathIndexes;
	}
	
	protected static int getTraversalDirection(GameVolume from, GameVolume to, int totalSteps, int stepIndex, int xDistance, int yDistance, int zDistance) {
		int direction = -1;
		//IF the ratio of stepIndex:totalSteps IS EQUIVALENT TO the ratio of axisIndex:axisDistance THEN travel on that Axis
		boolean travelX = xDistance > 0 && ((stepIndex + 1) * xDistance / totalSteps) >= (1 + (stepIndex * xDistance / totalSteps));
		boolean travelY = yDistance > 0 && ((stepIndex + 1) * yDistance / totalSteps) >= (1 + (stepIndex * yDistance / totalSteps));
		boolean travelZ = zDistance > 0 && ((stepIndex + 1) * zDistance / totalSteps) >= (1 + (stepIndex * zDistance / totalSteps));
		String indicator = "";//get axes of concurrent movement
		if(travelX) indicator += "x";
		if(travelY) indicator += "y";
		if(travelZ) indicator += "z";
		direction = getTraversalDirection(indicator, xDistance, yDistance, zDistance);
		return direction;
	}
	
	protected static int getTraversalDirection(String directionIndicator, int xDistance, int yDistance, int zDistance) {
		int direction = (1 << 0 | 1 << 2 | 1 << 4);//21
		switch (directionIndicator) {
		case "xyz":
			if(xDistance > 0) {//traveling EAST
				if(yDistance > 0) {//traveling NORTH
					if(zDistance > 0) {//traveling UP
						direction = (2 << 0 | 2 << 2 | 2 << 4);//NORTHEAST & UP: 42
					} else if(zDistance < 0) {//traveling DOWN
						direction = (2 << 0 | 2 << 2 | 0 << 4);//NORTHEAST & DOWN: 10
					} else {//no Z axis travel
						direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XYZ axes simultaneously: 21
					}
				} else if(yDistance < 0) {//traveling SOUTH
					if(zDistance > 0) {//traveling UP
						direction = (2 << 0 | 0 << 2 | 2 << 4);//SOUTHEAST & UP: 34
					} else if(zDistance < 0) {//traveling DOWN
						direction = (2 << 0 | 0 << 2 | 0 << 4);//SOUTHEAST & DOWN: 2
					} else {//no Z axis travel
						direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XYZ axes simultaneously: 21
					}
				} else {//no Y axis travel
					direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XYZ axes simultaneously : 21
				}
			} else if(xDistance < 0) {//traveling WEST
				if(yDistance > 0) {//traveling NORTH
					if(zDistance > 0) {//traveling UP
						direction = (0 << 0 | 2 << 2 | 2 << 4);//NORTHWEST & UP: 40
					} else if(zDistance < 0) {//traveling DOWN
						direction = (2 << 0 | 2 << 2 | 0 << 4);//NORTHWEST & DOWN: 10
					} else {//no Z axis travel
						direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XYZ axes simultaneously: 21
					}
				} else if(yDistance < 0) {//traveling SOUTH
					if(zDistance > 0) {//traveling UP
						direction = (0 << 0 | 0 << 2 | 2 << 4);//SOUTHWEST & UP: 32
					} else if(zDistance < 0) {//traveling DOWN
						direction = (0 << 2 | 0 << 2 | 0 << 4);//SOUTHWEST & DOWN: 0
					} else {//no Z axis travel
						direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XYZ axes simultaneously: 21
					}
				} else {//no Y axis travel
					direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XYZ axes simultaneously: 21
				}
			} else {//no X axis travel
				direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XYZ axes simultaneously: 21
			}
			break;
		case "xy":
			if(xDistance > 0) {//traveling EAST
				if(yDistance > 0) {//traveling NORTH
					direction = (2 << 0 | 2 << 2 | 1 << 4);//NORTHEAST: 26
				} else if(yDistance < 0) {//traveling SOUTH
					direction = (2 << 0 | 0 << 2 | 1 << 4);//SOUTHEAST: 18
				} else {//no Y axis travel
					direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XY axes simultaneously: 21
				}
			} else if(xDistance < 0) {//traveling WEST
				if(yDistance > 0) {//traveling NORTH
					direction = (0 << 0 | 2 << 2 | 1 << 4);//NORTHWEST: 24
				} else if(yDistance < 0) {//traveling SOUTH
					direction = (0 << 0 | 0 << 2 | 1 << 4);//SOUTHWEST: 16
				} else {//no Y axis travel
					direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XY axes simultaneously: 21
				}
			} else {//no X axis travel
				direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XY axes simultaneously: 21
			}
			break;
		case "xz":
			if(xDistance > 0) {//traveling EAST
				if(zDistance > 0) {//traveling UP
					direction = (2 << 0 | 1 << 2 | 2 << 4);//EAST & UP: 38
				} else if(zDistance < 0) {//traveling DOWN
					direction = (2 << 0 | 1 << 2 | 0 << 4);//EAST & DOWN: 6
				} else {//no Z axis travel
					direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XZ axes simultaneously: 21
				}
			} else if(xDistance < 0) {//traveling WEST
				if(zDistance > 0) {//traveling UP
					direction = (0 << 0 | 1 << 2 | 2 << 4);//WEST & UP: 36
				} else if(zDistance < 0) {//traveling DOWN
					direction = (0 << 0 | 1 << 2 | 0 << 4);//WEST & DOWN: 4
				} else {//no Z axis travel
					direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XZ axes simultaneously: 21
				}
			} else {//no X axis travel
				direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN XZ axes simultaneously: 21
			}
			break;
		case "yz":
			if(yDistance > 0) {//traveling NORTH
				if(zDistance > 0) {//traveling UP
					direction = (1 << 0 | 2 << 2 | 2 << 4);//NORTH & UP: 41
				} else if(zDistance < 0) {//traveling DOWN
					direction = (1 << 0 | 2 << 2 | 0 << 4);//NORTH & DOWN: 9
				} else {//no Z axis travel
					direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN YZ axes simultaneously: 21
				}
			} else if(yDistance < 0) {//traveling SOUTH
				if(zDistance > 0) {//traveling UP
					direction = (1 << 0 | 0 << 2 | 2 << 4);//SOUTH & UP: 33
				} else if(zDistance < 0) {//traveling DOWN
					direction = (1 << 0 | 0 << 2 | 0 << 4);//SOUTH & DOWN: 1
				} else {//no Z axis travel
					direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN YZ axes simultaneously: 21
				}
			} else {//no Y axis travel
				direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN YZ axes simultaneously: 21
			}
			break;
		case "x":
			if(xDistance > 0) {//traveling EAST
				direction = (2 << 0 | 1 << 2 | 1 << 4);//EAST: 22
			} else if(xDistance < 0) {//traveling WEST
				direction = (0 << 0 | 1 << 2 | 1 << 4);//WEST: 20
			} else {//no X axis travel
				direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN X axis: 21
			}
			break;
		case "y":
			if(xDistance > 0) {//traveling NORTH
				direction = (1 << 0 | 2 << 2 | 1 << 4);//NORTH: 25
			} else if(xDistance < 0) {//traveling SOUTH
				direction = (1 << 0 | 0 << 2 | 1 << 4);//SOUTH: 17
			} else {//no Y axis travel
				direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN Y axis: 21
			}
			break;
		case "z":
			if(xDistance > 0) {//traveling UP
				direction = (1 << 0 | 1 << 2 | 2 << 4);//UP: 37
			} else if(xDistance < 0) {//traveling DOWN
				direction = (1 << 0 | 1 << 2 | 0 << 4);//DOWN: 5
			} else {//no Z axis travel
				direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T HAPPEN IF TRAVLING IN Z axis
			}
			break;
		default://NO DIRECTION
			direction = (1 << 0 | 1 << 2 | 1 << 4);//SHOULDN'T EVER HAPPEN: 21
			break;
		}
		return direction;
	}
	
	protected static GameVolume getNextVolumeInDirection(GameVolume from, int direction) {
		GameVolume nextVolume = null;
		int newX = from.xPos;
		int newY = from.yPos;
		int newZ = from.zPos;
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
		} else if(zDir == 2) {//POSOTIVE movement on Z axis
			newZ++;
		}
		if(newX < 0 || newX > room.length || newY < 0 || newY > room.width || newZ < 0 || newZ > room.height) {//index out of bounds
			nextVolume = null;
		} else {
			nextVolume = room.interior.get(((newZ - 1) * room.width * room.length) + ((newY - 1) * room.length) + (newX - 1));
		}
		return nextVolume;
	}
}
