package game.test;

import game.asset.room.GameRoom;
import game.util.GameUtility;

public class Tester {

	protected static GameRoom room = null;
	
	public static void main(String[] args) {
//		for(int i = 0; i < 100; i++) {
//			System.out.println(rollDie(20,0,true));
//		}
/*
		String name = "Skeleton";
		int currentHp = 5;
		int maxHp= 10;
		double distance = 1.732;
		byte direction = 0b10_10_01;
		System.out.println(String.format("%s: %d/%dHP - %.2f meters, %s",name.toUpperCase(),currentHp,maxHp,distance,GameState.getDirectionName(direction)));
		System.out.println("");
*/
		
//		for(int i = 0; i < 10; i++) {
//			System.out.println("Masculine (Random): " + GameUtility.generateName(Double.valueOf(Math.random() * 3).intValue() + 1,'m'));
//			System.out.println("Masculine (Selected): " + GameState.namesMale[Double.valueOf(Math.random() * GameState.namesMale.length).intValue()]);
//			System.out.println("Feminine (Random): " + GameUtility.generateName(Double.valueOf(Math.random() * 3).intValue() + 1,'f'));
//			System.out.println("Feminine (Selected): " + GameState.namesFemale[Double.valueOf(Math.random() * GameState.namesFemale.length).intValue()]);
//			System.out.println("Other (Random): " + GameUtility.generateName(Double.valueOf(Math.random() * 3).intValue() + 1,'o'));
//			System.out.println("Other (Selected): " + GameState.namesOther[Double.valueOf(Math.random() * GameState.namesOther.length).intValue()]);
//			System.out.println("Full Random: " + GameUtility.generateName(Double.valueOf(Math.random() * 7).intValue() + 2));
//		}
		
		int modifier = GameUtility.rollDie(10,0,false) + GameUtility.rollDie(10,0,false);
		int height = 58 + modifier;
		int weight = 120 + ((GameUtility.rollDie(4,0,false) + GameUtility.rollDie(4,0,false)) * modifier);
//		System.out.println("(Rolled) Stats for your " + gameState.playerRace.toUpperCase() + " " + gameState.gender.toUpperCase() + ": ");
		System.out.println(String.format("HEIGHT: %1$sft. %2$sin. | WEIGHT: %3$slbs.",height / 12,height % 12,weight));
		
//		try {
//			GameUtility.clearScreen();
//		} catch (InterruptedException | IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("DID THIS CLEAR THE SCREEN?");
		
		//%[argument_index$][flags][width][.precision]conversion
//		System.out.println(String.format("%-40s| %s %,.2f¢","Selected Items:","COIN:",1578854.521));//A155[¢]
//		System.out.println(String.format("[%02d]%-35.35s ",5,"A LONGER ITEM NAME"));
//		System.out.println(String.format("[%02d]%-25.25s:%8.2f¢ ",12,"NOT QUITE AS LONG OF A NAME AS THE LAST ONE I DON'T THINK",142.154787));//128[Ç]135[ç],155[¢]

//		System.out.println(GameUtility.getRequiredXpForNextLevel(15));
//		System.out.println(GameUtility.getRequiredXpForNextLevel(1));
//		System.out.println(GameUtility.getRequiredXpForNextLevel(6));
//		System.out.println(GameUtility.getRequiredXpForNextLevel(232));
//		System.out.println(GameUtility.getRequiredXpForNextLevel(233));
		
		System.out.println(String.format("%4.4s%s%s%s%d%s%d%s%4.2f%s%s",125,": ","ENEMY_NAME",", ",15,"/",25,"HP - ",173.5214d," meters, ",GameUtility.getDirectionName((byte)0b01_11_00)));
		int numOfEnemyTypeInRoom = 50;
		int dynLen = GameUtility.getPrintedLength(numOfEnemyTypeInRoom);
		System.out.println(dynLen);
		System.out.println(String.format("[%" + dynLen + "." + dynLen + "s]: %s, %d/%dHP - %.2f meters, %s",1,"ENEMY_NAME",15,25,173.5214d,GameUtility.getDirectionName((byte)0b01_11_00)));
		
		System.out.println(String.format("[ #]%1$-21.21s xQt:   Price¢ [ #]%1$-21.21s xQt:   Price¢ ","Item Name"));
		System.out.println();
		
	}
	

	
}
