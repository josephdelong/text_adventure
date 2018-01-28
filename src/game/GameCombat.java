package game;

import game.asset.actor.GameEnemy;
import game.asset.room.GameRoom;
import game.asset.room.GameVolume;
import game.util.GameUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * GameItem A class that represents a Combat Encounter in the Game
 * @author Joseph DeLong
 */
public class GameCombat {

	//queue,queueIndex,numRounds,actionLog
	protected List<GameCombatQueue> initiativeQueue;
	protected int queueIndex;
	protected int numRounds;
	protected List<String> actionLog;
	protected List<GameEnemy> enemies;
	protected List<List<Integer>> initiativeRolls;//Outer List index = Initiative roll; Inner List = List of indexes corresponding to positions in the GameEnemy array
	protected int currentRoundNumber;
	
	protected static final int combatAction_FREE = 0;
	protected static final int combatAction_ATTACK = 1;
	protected static final int combatAction_MOVE = 2;
	protected static final int combatAction_DEFEND = 3;

	/**
	 * GameCombat(GameRoom) Sets up a Combat Encounter for the Player in the given GameRoom
	 * @param room The GameRoom in which the Combat encounter will take place
	 */
	protected GameCombat(GameRoom room) {
		initiativeQueue = new ArrayList<GameCombatQueue>();
		enemies = new ArrayList<GameEnemy>();
		initiativeRolls = new ArrayList<List<Integer>>(20);
		currentRoundNumber = 1;
		int enemyIndex = 0;
		//Set up Initiative for every hostile GameEnemy in the GameRoom
		for(GameVolume v: room.getInterior()) {
			for(List<GameEnemy> l: v.getEnemies().values()) {
				for(GameEnemy e: l) {
					if(e.isHostile()) {
						enemies.add(e);
						int initiative = GameUtility.rollDie(20,e.getInitiative(),false);
						List<Integer> rolls = initiativeRolls.get(initiative - 1);
						if(rolls == null) {
							rolls = new ArrayList<Integer>();
						}
						rolls.add(enemyIndex);
						initiativeRolls.set(initiative - 1,rolls);
						enemyIndex++;
					}
				}
			}
		}
		//all set up?
	}

	/**
	 * addCombatant(GameEnemy) Adds the specified GameEnemy to the Combat Encounter. This would occur if an Enemy which was not initially hostile became hostile, or if the Enemy was summoned, or otherwise entered the room & was hostile to the player.
	 * @param enemy The Enemy to add to Combat
	 */
	protected void addCombatant(GameEnemy enemy) {
		enemies.add(enemy);
		List<Integer> enemyIndexes = initiativeRolls.get(0);//added enemies' Initiative will always be 0, so they go in the 0 index
		if(enemyIndexes == null) {
			enemyIndexes = new ArrayList<Integer>();
		}
		enemyIndexes.add(enemies.size() - 1);
		initiativeRolls.set(0,enemyIndexes);
	}

	/**
	 * removeCombatant(GameEnemy) Removes the specified GameEnemy from the Combat Encounter
	 * @param enemy The Enemy to remove from Combat
	 */
	protected void removeCombatant(GameEnemy enemy) {
		
	}

	/**
	 * combat(boolean) Processes the Rounds of the Combat Encounter, allowing each Actor to perform their CombatAction(s) in a Round-Robin style
	 * @param inCombat Boolean representing whether the cycle of Combat Encounter Rounds should be active
	 */
	protected void combat(boolean inCombat) {
		while(inCombat) {
			GameCombatQueue round = new GameCombatQueue();
			//process Combat Encounter Round...
			for(int i = initiativeRolls.size(); i > 0; i--) {
				List<Integer> enemyIndexes = initiativeRolls.get(i - 1);
				for(int enemyIndex: enemyIndexes) {
					GameEnemy enemy = this.enemies.get(enemyIndex);
					//process turn for enemy...
					if(enemy.isHostile()) {
						//...
					}
				}
			}
			initiativeQueue.add(round);
		}
	}

}
