package game;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * CommandLineInterface A Command Line Interface for use with various text-based adventure games, designed by yours truly
 * @author Joseph DeLong
 */
public class CommandLineInterface {

	private static boolean isRunning = true;
	private static int saveGameSlot = 0;
	private static Scanner sc;
	private static String userInput = "";
	private static File[] saveGames;
	private static InputStream fileIn;
	private static OutputStream fileOut;
	private static GameState gameState;

	private static String errorLogFilePath = "./log/error.log";
	private static String userInputCommandsLogFile = "./log/user_input.log";

	private static List<String> inputCommands;
	
	private static boolean inCreationMode = false;

	/**
	 * main(String[]) Main method for running / testing the game
	 * @param args Any userInput parameters to the runtime of this Class
	 */
	public static void main(String[] args) {
		try {
			Instant start = Instant.now();
			loadTemplates();//load all persisted GameEnemy, GameItem, GameObject, GameRoom, GameSpell
			File saveGameFilePath = new File("./saveGames");//load previously SAVEd Games
			if(saveGameFilePath.exists() && saveGameFilePath.isDirectory()) {
				File[] tempFiles = new File[] {null,null,null,null,null,null,null,null,null,null};
				File[] saveGameFiles = saveGameFilePath.listFiles();
				for(int i = 0; i < saveGameFiles.length; i++) {
					String fileIndex = saveGameFiles[i].getName();
					int index = Integer.parseInt(fileIndex.substring(fileIndex.lastIndexOf('_') + 1));
					tempFiles[index - 1] = new File(saveGameFiles[i].getPath() + "/saveGame.sav");
				}
				if(tempFiles != null && tempFiles.length > 0 && tempFiles.length <= 10) {
					saveGames = tempFiles;
				} else {
					System.out.println("Error loading Save Game slots!");
				}
			} else {
				System.out.println("Save Game slots not yet set up. Skipping...");
			}
			inputCommands = new ArrayList<String>();
			sc = null;
			if(args.length > 0) {//command line parameters passed in
				System.setErr(new PrintStream(errorLogFilePath));//TODO: add checks for "-e String"?
				//detect which is -i "String" and which is -o "String"
				List<String> capturedArgs = new ArrayList<String>();
				for(String s: args) {
					capturedArgs.add(s.toLowerCase());//TODO: handle filepaths with spaces!
				}
				int inputIndex = capturedArgs.indexOf("-i");
				int outputIndex = capturedArgs.indexOf("-o");
				if(inputIndex >= 0 && inputIndex < capturedArgs.size() - 1 && !capturedArgs.get(inputIndex + 1).equals("-o") && !capturedArgs.get(inputIndex + 1).equals("-i")) {//there is an input file specified
					File testInFile = new File(capturedArgs.get(inputIndex + 1));
					if(testInFile.exists() && testInFile.isFile() && testInFile.canRead()) {
						fileIn = new FileInputStream(capturedArgs.get(inputIndex + 1));
						System.setIn(fileIn);//get first specified input file
					}
				}
				if(outputIndex >= 0 && outputIndex < capturedArgs.size() - 1 && !capturedArgs.get(outputIndex + 1).equals("-o") && !capturedArgs.get(outputIndex + 1).equals("-i")) {//there is an output file specified
					File testOutFile = new File(capturedArgs.get(outputIndex + 1));
					if(testOutFile.exists() && testOutFile.isFile() && testOutFile.canWrite()) {
						System.setOut(new PrintStream(capturedArgs.get(outputIndex + 1)));//get first specified output file
					}
				}
				if(fileIn != null) {//set Scanner depending on if -i parameter is set
					sc = new Scanner(fileIn);
				} else {
					sc = new Scanner(System.in);
				}
			} else {//no command line parameters passed in
				System.setErr(new PrintStream(errorLogFilePath));
				sc = new Scanner(System.in);//use STD_IN (keyboard) as source of commands
			}
			System.out.println(intro());
			while(isRunning) {
				System.out.print("[]>");
				userInput = sanitize(sc.nextLine());
				inputCommands.add(userInput);//log user input for export, I guess
				//TODO: break each COMMAND into its own class, with do() and help() methods
				if(userInput.matches("commands\\s*.*")) {
					System.out.println(displayHelp("commands"));
				} else if(userInput.matches("\\?.*")) {
					System.out.println(displayHelp());
				} else if(userInput.matches("help\\s*.*")) {
					if(userInput.matches("help\\s*")) {//no context given
						System.out.println(displayHelp());//display default help screen
					} else {//specific command indicated
						String[] context = userInput.split("\\s+");
						System.out.println(displayHelp(context[1]));//discard all userInput after the first contextual token
					}
				} else if(userInput.matches("about\\s*.*")) {
					System.out.println(about());
				} else if(userInput.matches("exit\\s*.*")) {
					System.out.println("Do you really want to EXIT? All unSaved progress will be lost!");
					userInput = sanitize(sc.nextLine());
					inputCommands.add(userInput);//log user input for export, I guess
					if(userInput.matches("y\\s*.*")) {
						System.out.println("Exiting the Game.");
						exit();
					} else {
						System.out.println("Not Exiting!\n");
					}
				} else if(userInput.matches("load\\s*.*")) {
					if(saveGames == null || saveGames.length == 0) {//no Saved Games to Load
						System.out.println("No Saved Games to LOAD. Type NEW to start a New Game.");
					} else {
						if(userInput.matches("load\\s*")) {//no Save Game slot indicated
							if(saveGameSlot == 0) {//no Save Game slot active
								System.out.println("No Save Game slot is active. Unable to LOAD.");
							} else {
								System.out.print("LOAD the Saved Game in current slot [" + saveGameSlot + "]? ");
								userInput = sanitize(sc.nextLine());
								inputCommands.add(userInput);//log user input for export, I guess
								if(userInput.matches("y\\s*.*")) {
									loadGame(saveGameSlot);
								}
							}
						} else {//get Save Game slot indicated
							String[] context = userInput.split("\\s+");
							if(context[1].matches("\\d+")) {//context is a valid number
								int tempSaveGameSlot = Integer.parseInt(context[1]);
								if(tempSaveGameSlot > 0 && tempSaveGameSlot <= 10) {
									loadGame(tempSaveGameSlot);
								} else {//indicated Save Game slot is not between 1 and 10
									System.out.println("Not a valid Save Game slot: " + tempSaveGameSlot);
								}
							} else {//indicated Save Game slot is not a number
								System.out.println("Not a valid Save Game slot: " + context[1]);
							}
						}
					}
				} else if(userInput.matches("new\\s*.*")) {
					if(saveGameSlot == 0) {//no current Game Loaded
						newGame();
					} else {//there is a Game already Loaded
						System.out.print("Start a New Game? Unsaved progress in current Game will be lost");
						userInput = sanitize(sc.nextLine());
						inputCommands.add(userInput);//log user input for export, I guess
						if(userInput.matches("y\\s*.*")) {
							newGame();
						} else {
							System.out.println("Returning to current Game!");
						}
					}
				} else if(userInput.matches("options\\s*.*")) {
					options();
				} else if(userInput.matches("save\\s*.*")) {
					if(userInput.equals("save")) {
						if(saveGameSlot == 0) {//no Save Game slot active
							saveGame();
						} else {////there is a Save Game slot active
							System.out.print("Overwrite current Game in slot (" + saveGameSlot + ")? ");
							userInput = sanitize(sc.nextLine());
							inputCommands.add(userInput);//log user input for export, I guess
							if(userInput.matches("y\\s*.*")) {
								saveGame(saveGameSlot);
							}
						}
					} else {
						String[] context = userInput.split("\\s+");
						if(context.length > 1) {//got a context
							if(context[1].matches("\\d+")) {//context is a valid number
								int tempSaveGameSlot = Integer.parseInt(context[1]);
								if(tempSaveGameSlot > 0 && tempSaveGameSlot <= 10) {
									saveGame(tempSaveGameSlot);
								} else {//indicated Save Game slot is not between 1 and 10
									System.out.println("Not a valid Save Game slot: " + tempSaveGameSlot);
								}
							} else {//indicated Save Game slot is not a number
								System.out.println("Not a valid Save Game slot: " + context[1]);
							}
						} else {//need a context
							System.out.println("What SAVE GAME slot would you like to SAVE to?");
							userInput = sanitize(sc.nextLine());
							if(userInput.matches("\\d+")) {//valid number
								int saveSlot = Integer.parseInt(userInput);
								if(saveSlot > 0 && saveSlot <= 10) {
									if(saveGames != null && saveGames[saveSlot - 1].exists()) {
										boolean confirmation = true;
										while(confirmation) {
											System.out.println("Do you want to OVERWRITE the SAVE file in slot " + saveSlot + "? (Y/N)");
											userInput = sanitize(sc.nextLine());
											if(userInput.matches("y")) {
												saveGame(saveSlot);
											} else if(userInput.matches("n")) {
												System.out.println("Will not OVERWRITE the SAVE file in slot " + saveSlot + ".");
											}
										}
									} else {
										saveGame(saveSlot);
									}
								} else {
									System.out.println("Invalid SAVE GAME slot: " + saveSlot);
								}
							} else {
								System.out.println("Bad value for SAVE GAME slot: " + userInput.toUpperCase());
							}
						}
					}
				} else if(userInput.matches("test\\s*.*")) {
					String[] context = userInput.split("\\s+");
					if(context.length > 1) {
						test(context[1]);//ignore all context after the first whitespace character. Fix this to recognize ' and " for grouping fileNames with whitespace
					} else {
						test(null);
					}
				} else if(userInput.matches("look\\s*.*")) {
					if(userInput.equals("look") || userInput.matches("look\\s+(at|for){1}\\s*")) {
						System.out.println("What do you want to LOOK at?");
						String context = sanitize(sc.nextLine());
						inputCommands.add(context);//log user input for export, I guess
						if(context.length() > 0) {
							System.out.println(look(context));
						} else {
							System.out.println("You LOOK at nothing in particular.");
						}
					} else if(userInput.matches("look\\s+(at|for)?\\s*.+")) {
						String[] tokens = userInput.split("look(\\s+at|\\s+for)*\\s+");
						if(tokens.length > 1) {
							System.out.println(look(tokens[1]));
						}
					}
				} else if(userInput.matches("take\\s*.*")) {
					if(userInput.equals("take")) {
						System.out.println("What do you want to TAKE?");
						String context = sanitize(sc.nextLine());
						inputCommands.add(context);//log user input for export, I guess
						if(context.length() > 0) {
							System.out.println(take(context));
						} else {
							System.out.println("You decide not to touch anything.");
						}
					} else if(userInput.matches("take\\s+.+")) {
						String[] tokens = userInput.split("\\s+");
						System.out.println(take(tokens[1]));
					}
				} else if(userInput.matches("drop\\s*.*")) {
					if(userInput.equals("drop")) {
						System.out.println("What do you want to DROP?");
						String context = sanitize(sc.nextLine());
						inputCommands.add(context);//log user input for export, I guess
						if(context.length() > 0) {
							System.out.println(drop(context));
						} else {
							System.out.println("You decide to hold onto your spells for now.");
						}
					} else if(userInput.matches("drop\\s+.+")) {
						String[] tokens = userInput.split("\\s+");
						System.out.println(drop(tokens[1]));
					}
				} else if(userInput.matches("equip\\s*.*")) {
					if(userInput.equals("equip")) {
						System.out.println("What do you want to EQUIP?");
						String context = sanitize(sc.nextLine());
						inputCommands.add(context);//log user input for export, I guess
						if(context.length() > 0) {
							System.out.println(equip(context));
						} else {
							System.out.println("You keep your current equipment as-is.");
						}
					} else if(userInput.matches("equip\\s+.+")) {
						String[] tokens = userInput.split("\\s+");
						System.out.println(equip(tokens[1]));
					}
				} else if(userInput.matches("unequip\\s*.*")) {
					if(userInput.equals("unequip")) {
						System.out.println("What do you want to UNEQUIP?");
						String context = sanitize(sc.nextLine());
						inputCommands.add(context);//log user input for export, I guess
						if(context.length() > 0) {
							System.out.println(unEquip(context));
						} else {
							System.out.println("You keep your current equipment as-is.");
						}
					} else if(userInput.matches("unequip\\s+.+")) {
						String[] tokens = userInput.split("\\s+");
						System.out.println(unEquip(tokens[1]));
					}
				} else if(userInput.matches("move\\s*.*")) {
					//MOVE, MOVE DIRECTION, MOVE OBJECT
					if(userInput.equals("move")) {
						System.out.println("In what DIRECTION would you like to MOVE?");
						System.out.println("     [ EAST | WEST | NORTH | SOUTH ]     ");
						String context = sanitize(sc.nextLine());
						inputCommands.add(context);//log user input for export, I guess
						if(context.length() > 0) {
							System.out.println(move(context));
						} else {
							System.out.println("You stay where you are.");
						}
					} else if(userInput.matches("move\\s+.+")) {
						String[] tokens = userInput.split("\\s+");
						System.out.println(move(tokens[1]));
					}
				} else if(userInput.matches("attack\\s*.*")) {
					//ATTACK, ATTACK TARGET, ATTACK TARGET WITH
					if(userInput.equals("attack")) {
						System.out.println("What do you want to ATTACK?");
						String context = sanitize(sc.nextLine());
						if(context != null) {
							System.out.println(attack(context));
						}
					} else if(userInput.matches("attack\\s+\\w+\\s+(with\\s+)?\\w+")) {//ATTACK TARGET WITH ITEM
						String[] context = userInput.split("\\s+");
						if(context.length > 3) {
							System.out.println(attack(context[1],context[3]));
						} else {
							System.out.println(attack(context[1]));
						}
					} else {
						String[] context = userInput.split("\\s+");
						if(context.length > 1) {
							System.out.println(attack(context[1]));
						}
					}
				} else if(userInput.matches("defend\\s*.*")) {
					//DEFEND, DEFEND TARGET, DEFEND TARGET WITH, DEFEND AGAINST, DEFEND AGAINST WITH
					
				} else if(userInput.matches("open\\s*.*")) {
					//OPEN, OPEN DOOR, OPEN CONTAINER, OPEN INVENTORY
					if(userInput.equals("open")) {
						System.out.println("What would you like to OPEN?");
						userInput = sanitize(sc.nextLine());
						inputCommands.add(userInput);
						if(userInput.matches("\\w+")) {//process context
							System.out.println(open(userInput));
						} else {
							System.out.println("You can't figure out how to OPEN " + userInput.toUpperCase() + ".");
						}
					} else if(userInput.matches("open\\s+(inv(entory)?)")) {
						if(gameState.inInventory) {//already in the inventory
							System.out.println("You are already looking in your inventory.");
							System.out.println(showInventory());
						} else {//open player inventory
							System.out.println("You open your bags and look inside.");
							gameState.inInventory = true;
							System.out.println(showInventory());
						}
					} else if(userInput.matches("open\\s+\\w+")) {
						String[] context = userInput.split("open\\s+");
						if(context != null && context.length > 1) {
							System.out.println(open(context[1]));
						} else {
							System.out.println("You decide not to OPEN anything after all.");
						}
					} else {
						System.out.println("You don't know how to " + userInput.toUpperCase() + ".");
					}
				} else if(userInput.matches("close\\s*.*")) {
					//CLOSE, CLOSE DOOR, CLOSE CONTAINER, CLOSE INVENTORY
					if(userInput.equals("close")) {
						System.out.println("What would you like to CLOSE?");
						userInput = sanitize(sc.nextLine());
						inputCommands.add(userInput);
						if(userInput.matches("\\w+")) {//process context
							System.out.println(close(userInput));
						} else {
							System.out.println("You can't figure out how to CLOSE " + userInput.toUpperCase() + ".");
						}
					} else if(userInput.matches("close\\s+(inv(entory)?)")) {
						if(gameState.inInventory) {
							gameState.inInventory = false;
							System.out.println("You close your bags.");
						}
					} else if(userInput.matches("close\\s+\\w+")) {
						String[] context = userInput.split("close\\s");
						if(context != null && context.length > 1) {
							System.out.println(close(context[1]));
						} else {
							System.out.println("You decide not to CLOSE anything after all.");
						}
					} else {
						System.out.println("You don't know how to CLOSE a " + userInput.toUpperCase() + ".");
					}
				} else if(userInput.matches("use\\s*.*")) {
					//USE, USE ITEM, USE ITEM ON
				} else if(userInput.matches("throw\\s*.*")) {
					//THROW, THROW ITEM, THROW ITEM AT TARGET
				} else if(userInput.matches("climb\\s*.*")) {
					//CLIMB, CLIMB OBJECT, CLIMB DIRECTION
				} else if(userInput.matches("run\\s*.*")) {
					//RUN, RUN DIRECTION, RUN AWAY
				} else if(userInput.matches("jump\\s*.*")) {
					//JUMP, JUMP ON OBJECT, JUMP OVER OBJECT
				} else if(userInput.matches("listen\\s*.*")) {
					//LISTEN, LISTEN TO SOURCE, LISTEN FOR SOUND
				} else if(userInput.matches("touch\\s*.*")) {
					//TOUCH, TOUCH OBJECT, TOUCH ITEM, TOUCH TARGET, TOUCH SELF (naughty!)
				} else if(userInput.matches("smell\\s*.*")) {
					//SMELL, SMELL OBJECT, SMELL ITEM, SMELL ROOM, SMELL TARGET, SMELL SELF (hygiene)
				} else if(userInput.matches("taste\\s*.*")) {
					//TASTE, TASTE ITEM, TASTE OBJECT, TASTE TARGET (weird...)
				} else if(userInput.matches("think\\s*.*")) {
					//THINK, THINK ABOUT
				} else if(userInput.matches("rest\\s*.*")) {
					//REST, REST DURATION, REST ON OBJECT
					
				} else if(userInput.matches("cast\\s*.*")) {
					//CAST, CAST SPELL, CAST SPELL TARGET
					if(userInput.equals("cast")) {
						System.out.println("What SPELL would you like to CAST?");
						String spellName = sanitize(sc.nextLine());
						inputCommands.add(spellName);
						if(spellName.matches("\\w+.*")) {//process context
							String[] context = spellName.split("\\s+(at|on)?");
							if(context.length > 0) {//got a try at a SPELL name
								if(context.length > 1) {//got a SPELL name + a try at a TARGET
									System.out.println(castSpell(context[0],context[1]));//try to CAST SPELL TARGET
								} else {//need a TARGET
									System.out.println("On what TARGET would you like to CAST " + context[0].toUpperCase() + "?");
									String target = sanitize(sc.nextLine());
									inputCommands.add(target);
									if(context[0].matches("\\w+") && target.matches("\\w+")) {
										System.out.println(castSpell(context[0],target));
									} else {//no TARGET
										if(!GameSpell.spells.containsKey(context[0])) {//valid GameSpell
											System.out.println("You decide not to CAST " + context[0].toUpperCase() + " after all");
										} else {
											System.out.println("You're not even sure that " + userInput.toUpperCase() + " is a real SPELL.");
										}
									}
								}
							} else {//no context
								System.out.println("You decide not to CAST a SPELL after all.");
							}
						} else {
							System.out.println("You're not even sure that " + spellName.toUpperCase() + " is a real SPELL.");
						}
					} else if(userInput.matches("cast\\s+\\w+")) {//got SPELL
						String[] context = userInput.split("\\s+(at|on)?");
						System.out.println("On what TARGET would you like to CAST " + context[1].toUpperCase() + "?");
						String target = sanitize(sc.nextLine());
						inputCommands.add(target);
						if(context[1].matches("\\w+") && target.matches("\\w+")) {
							System.out.println(castSpell(context[1],target));
						} else {//no TARGET
							if(!GameSpell.spells.containsKey(context[1])) {//valid GameSpell
								System.out.println("You decide not to CAST " + context[1].toUpperCase() + " after all");
							} else {
								System.out.println("You're not even sure that " + userInput.toUpperCase() + " is a real SPELL.");
							}
						}
					} else if(userInput.matches("cast\\s+\\w+\\s+\\w+.*")) {//got SPELL + TARGET
						String[] context = userInput.split("\\s+");
						if(context.length > 2) {
							if(context[2].matches("(at|on)?")) {
								if(context.length > 3) {
									System.out.println(castSpell(context[1],context[3]));
								} else {//get TARGET
									System.out.println("On what TARGET would you like to CAST " + context[1].toUpperCase() + "?");
									String target = sanitize(sc.nextLine());
									inputCommands.add(target);
									if(context[1].matches("\\w+") && target.matches("\\w+")) {
										System.out.println(castSpell(context[1],target));
									} else {//no SPELL name
										if(!GameSpell.spells.containsKey(context[1])) {//valid GameSpell
											System.out.println("You decide not to CAST " + context[1].toUpperCase() + " after all");
										} else {
											System.out.println("You're not even sure that " + userInput.toUpperCase() + " is a real SPELL.");
										}
									}
								}
							} else {
								System.out.println(castSpell(context[1],context[2]));
							}
						}
					}
				} else if(userInput.matches("debug\\s*.*")) {
					if(userInput.equals("debug")) {
						System.out.println(debug());
					} else {
						String[] context = userInput.split("\\s+");
						if(context.length > 1) {
							System.out.println(debug(context[1]));
						}
					}
				} else if(userInput.matches("create.*")) {
					System.out.println("Are you sure you want to switch to CREATION mode? You will lose all unSAVEd progress.");
					userInput = sanitize(sc.nextLine());
					inputCommands.add(userInput);
					if(userInput.matches("y.*")) {
						creationMode();//enter CREATION mode
					} else {
						System.out.println("Returning to the GAME.");
					}
				} else {
					System.out.println("Command '" + userInput + "' not recognized.\nType HELP for more info.");
				}//TODO: implement the use of ranged attacks [bows & arrows; wands]: AIM / SHOOT / TARGET
			}
			cleanUp();
			Instant end = Instant.now();
			System.out.println("Start: " + start + "\nEnd: " + end + "\nDifference: " + (end.toEpochMilli() - start.toEpochMilli()) + " milliseconds");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Exception" + e.getClass() + " thrown. Exiting System!");
			System.out.println("Exception cause: " + e.getCause());
			System.out.println("Exception message: " + e.getMessage());
			System.out.println("Exception stacktrace: ");
			e.printStackTrace();
		} finally {
//			cleanUp();
			System.exit(0);
		}
	}

	/**
	 * sanitize(String) Sanitizes user input from the command line interface
	 * @param userInput Text input from the user
	 * @return Sanitized version of the input user string
	 */
	private static String sanitize(String input) {
		return input.trim().toLowerCase().replaceAll("[^A-Za-z0-9_\\?\\-/\\.\\s]*", "");//remove all characters other than those specified in this range
		// maybe check for bad user userInput here as well?
	}

	/**
	 * cleanUp() Clean up method, run when exiting
	 * @throws IOException 
	 */
	private static void cleanUp() throws IOException {
		sc.close();
		if(fileIn != null) fileIn.close();
		saveTemplates();
		fileOut = new FileOutputStream(userInputCommandsLogFile);
		for(String s: inputCommands) {
			fileOut.write((s + "\n").getBytes());
		}
		if(fileOut != null)
		fileOut.close();
	}

	/**
	 * debug() Displays the values of all variables in the current GameState
	 * @return String representation of all GameState variables
	 */
	private static String debug() {
		String debugString = "";
		debugString += "isRunning: " + isRunning + "\n";
		debugString += "saveGameSlot: " + saveGameSlot + "\n";
		debugString += "saveGames:\n";
		if(saveGames != null && saveGames.length > 0) {
			for(int i = 0; i < saveGames.length; i++) {
				if(saveGames[i] != null) {
					debugString += "\t" + saveGames[i].getPath() + "\n";
				}
			}
		}
		debugString += "GameState:\n";
		if(gameState != null) {
			debugString += "\tcurrentHp: " + gameState.currentHp + "\n";
			debugString += "\tmaxHp: " + gameState.maxHp + "\n";
			debugString += "\tcurrentSp: " + gameState.currentSp + "\n";
			debugString += "\tmaxSp: " + gameState.maxSp + "\n";
			debugString += "\tcurrentXp: " + gameState.currentXp + "\n";
			debugString += "\tcurrentLevel: " + gameState.currentLevel + "\n";
			debugString += "\tarmorClass: " + gameState.armorClass + "\n";
			debugString += "\tdamageReduction: " + gameState.damageReduction + "\n";
			debugString += "\tmagicResistance: " + gameState.magicResistance + "\n";
			debugString += "\tinitiative: " + gameState.initiative + "\n";
			debugString += "\tattackBonusMelee: " + gameState.attackBonusMelee + "\n";
			debugString += "\tattackBonusRanged: " + gameState.attackBonusRanged + "\n";
			debugString += "\tattackBonusMagic: " + gameState.attackBonusMagic + "\n";
			debugString += "\tcurrentRoomId: " + gameState.currentRoomId + "\n";
			debugString += "\troomVolumeIndex: " + gameState.roomVolumeIndex + "\n";
			debugString += "\tcurrentWeight: " + gameState.currentWeight + "\n";
			debugString += "\theldItem: " + gameState.heldItem.toUpperCase() + "\n";
			debugString += "\tmaxWeight: " + gameState.maxWeight + "\n";
			debugString += "\tinInventory: " + gameState.inInventory + "\n";
			debugString += "\tinventory:\n";
			for(GameItem i: gameState.inventory.values()) {
				debugString += "\t\t" + i.name.toUpperCase() + ": " + i.description + " - " + i.weight + " lb\n";
			}
			debugString += "\tvisitedRooms:\n";
			for(GameRoom r: gameState.visitedRooms.values()) {
				debugString += "\t\tRoom #" + r.roomId + " - " + r.description + "\n";
				for(GameVolume v: r.interior) {
					if(v.items != null && !v.items.isEmpty()) {
						debugString += "\t\t\tItems:\n";
						for(List<GameItem> l: v.items.values()) {
							for(GameItem i: l) {
								debugString += "\t\t\t\t" + i.name.toUpperCase() + ": " + i.description + " - " + i.weight + " lb\n";
							}
						}
					}
					if(v.objects != null && !v.objects.isEmpty()) {
						debugString += "\t\t\tObjects:\n";
						for(List<GameObject> l: v.objects.values()) {
							for(GameObject o: l) {
								debugString += "\t\t\t\t" + o.name.toUpperCase() + ": " + o.description + "\n";
							}
						}
					}
					if(v.enemies != null && !v.enemies.isEmpty()) {
						debugString += "\t\t\tEnemies:\n";
						for(List<GameEnemy> l: v.enemies.values()) {
							for(GameEnemy e: l) {
								debugString += "\t\t\t\t" + e.name.toUpperCase() + ": " + e.description + " - HP: " + e.currentHp + "\n";
							}
						}
					}
				}
			}
			debugString += "\tspellBook:\n";
			for(GameSpell s: gameState.spellbook.values()) {
				debugString += "\t\t" + s.name.toUpperCase() + ": " + s.description + " | Spell Level: " + s.spellLevel + " | SP Cost: " + s.spellPointCost + "\n";
			}
		} else {
			debugString += "No Game Loaded";
		}
		return debugString;
	}

	/**
	 * readFile(File) Translates the specified File into a 64-bit encoded byte array, to be written to disk
	 * @param f The File to read
	 * @return byte[] containing the contents of the File f
	 * @throws IOException If the File can't be found, accessed, or read
	 */
	private static byte[] readFile(File f) throws IOException {
		List<Byte> allBytes = new ArrayList<Byte>();
		fileIn = new FileInputStream(f);
		DataInputStream in = new DataInputStream(new BufferedInputStream(fileIn));
		while(in.available() != 0) {
			allBytes.add(in.readByte());
		}
		byte[] byteArray = new byte[allBytes.size()];
		for(int i = 0; i < allBytes.size(); i++) {
			byteArray[i] = allBytes.get(i);
		}
		in.close();
		fileIn.close();
		return byteArray;
	}

	/**
	 * compress(byte[]) Compresses an array of bytes<br>
	 * Compression / Decompression methods are not my original work. These were developed and published<br>
	 * BY: Ralf Quebbemann<br>
	 * AT: https://dzone.com/articles/how-compress-and-uncompress
	 * @param data The byte array to compress
	 * @return A compressed version of the input byte array
	 * @throws IOException
	 */
	private static byte[] compress(byte[] data) throws IOException {
		Deflater deflater = new Deflater();
		deflater.setLevel(Deflater.BEST_COMPRESSION);//Added to maximize the compression (save the most disk space possible)
		deflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
				data.length);
		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer); // returns the generated code... index
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
//		System.out.println("Original: " + data.length);
//		System.out.println("Compressed: " + output.length);
		return output;
	}

	/**
	 * decompress(byte[]) Decompresses an array of bytes<br>
	 * Compression / Decompression methods are not my original work. These were developed and published<br>
	 * BY: Ralf Quebbemann<br>
	 * AT: https://dzone.com/articles/how-compress-and-uncompress
	 * @param data The byte array to decompress
	 * @return A decompressed version of the input byte array
	 * @throws IOException
	 * @throws DataFormatException
	 */
	private static byte[] decompress(byte[] data) throws IOException, DataFormatException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
				data.length);
		byte[] buffer = new byte[1024];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
//		System.out.println("Original: " + data.length);
//		System.out.println("Uncompressed: " + output.length);
		return output;
	}

	/**
	 * debug(String) Displays debug information for the specified context. A valid context can be Game_Enemy, GameItem, GameObject, GameRoom, GameSpell, GameState
	 * @param context
	 */
	private static String debug(String context) {
		String debugString = "";
		if(context == "GameState") {
			debugString = debug();
		} else {
			switch (context) {
			case "enemy": case "gameenemy": case "game_enemy":
				if(GameEnemy.enemies != null && !GameEnemy.enemies.isEmpty()) {
					for(GameEnemy e: GameEnemy.enemies.values()) {
						debugString += e.name.toUpperCase() + ": " + e.description + " - HP: " + e.currentHp + "\n";
					}
				}
				break;
			case "item": case "gameitem": case "game_item":
				if(GameItem.items != null && !GameItem.items.isEmpty()) {	
					for(GameItem i: GameItem.items.values()) {
						debugString += i.name.toUpperCase() + ": " + i.description + " - EquipSlot: " + i.equipSlot + "\n";
					}
				}
				break;
			case "object": case "gameobject": case "game_object":
				if(GameObject.objects != null && !GameObject.objects.isEmpty()) {
					for(GameObject o: GameObject.objects.values()) {
						debugString += o.name.toUpperCase() + ": " + o.description + " - HP: " + o.currentHp + "\n";
					}
				}
				break;
			case "room": case "gameroom": case "game_room":
				if(GameRoom.rooms != null && !GameRoom.rooms.isEmpty()) {
					for(GameRoom r: GameRoom.rooms.values()) {
						debugString += r.roomId + ": " + r.description + "\n";
					}
				}
				break;
			case "spell": case "gamespell": case "game_spell":
				if(GameSpell.spells != null && !GameSpell.spells.isEmpty()) {
					for(GameSpell s: GameSpell.spells.values()) {
						debugString += s.name.toUpperCase() + ": " + s.description + " - SP Cost: " + s.spellPointCost + "\n";
					}
				}
				break;
			default:
				break;
			}
		}
		return debugString;
	}

	/**
	 * loadTemplates Initializes the Game World. Loads into memory all persisted GameEnemy, GameItem, GameObject, GameRoom, GameSpell
	 * @throws IOException If the File is not found, not readable, etc.
	 * @throws DataFormatException 
	 */
	private static void loadTemplates() throws IOException, DataFormatException {
		File resourceFilePath = new File("./res");
		if(resourceFilePath.exists() && resourceFilePath.isDirectory()) {
			File[] resourceFiles = resourceFilePath.listFiles();
			for(File f: resourceFiles) {
				byte[] fileData = null;
				switch (f.getName()) {
				case "GameEnemy":
					fileData = decompress(readFile(f));
					fileData = Base64.getDecoder().decode(fileData);
					String[] enemyData = new String(fileData).split("¦");
					for(String s: enemyData) {
						GameEnemy.parseBytes(s.getBytes());
					}
					break;
				case "GameItem":
					fileData = decompress(readFile(f));
					fileData = Base64.getDecoder().decode(fileData);
					String[] itemData = new String(fileData).split("¦");
					for(String s: itemData) {
						GameItem.parseBytes(s.getBytes());
					}
					break;
				case "GameObject":
					fileData = decompress(readFile(f));
					fileData = Base64.getDecoder().decode(fileData);
					String[] objectData = new String(fileData).split("¦");
					for(String s: objectData) {
						GameObject.parseBytes(s.getBytes());
					}
					break;
				case "GameRoom":
					fileData = decompress(readFile(f));
					fileData = Base64.getDecoder().decode(fileData);
					String[] roomData = new String(fileData).split("¦");
					for(String s: roomData) {
						GameRoom.parseBytes(s.getBytes());
					}
					break;
				case "GameSpell":
					fileData = decompress(readFile(f));
					fileData = Base64.getDecoder().decode(fileData);
					String[] spellData = new String(fileData).split("¦");
					for(String s: spellData) {
						GameSpell.parseBytes(s.getBytes());
					}
					break;
				default:
					System.out.println("Resource File not recognized: " + f.getName());
					break;
				}
			}
		} else {
			System.out.println("No resources to load. Skipping...");
		}	
	}

	/**
	 * saveTemplates() Saves all in-memory resources to their respective resourceFile
	 * @throws IOException If file is not found, is unreadable, etc.
	 */
	private static void saveTemplates() throws IOException {
		if(GameEnemy.enemies != null && !GameEnemy.enemies.isEmpty()) {
			List<GameEnemy> enemies = new ArrayList<GameEnemy>();
			enemies.addAll(GameEnemy.enemies.values());
			String dataString = "";
			for(int i = 0; i < enemies.size(); i++) {
				dataString += enemies.get(i).getByteString();
				if(i != enemies.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameEnemy");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		if(GameItem.items != null && !GameItem.items.isEmpty()) {
			List<GameItem> items = new ArrayList<GameItem>();
			items.addAll(GameItem.items.values());
			String dataString = "";
			for(int i = 0; i < items.size(); i++) {
				dataString += items.get(i).getByteString();
				if(i != items.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameItem");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		if(GameObject.objects != null && !GameObject.objects.isEmpty()) {
			List<GameObject> objects = new ArrayList<GameObject>();
			objects.addAll(GameObject.objects.values());
			String dataString = "";
			for(int i = 0; i < objects.size(); i++) {
				dataString += objects.get(i).getByteString();
				if(i != objects.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameObject");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		if(GameRoom.rooms != null && !GameRoom.rooms.isEmpty()) {
			List<GameRoom> rooms = new ArrayList<GameRoom>();
			rooms.addAll(GameRoom.rooms.values());
			String dataString = "";
			for(int i = 0; i < rooms.size(); i++) {
				dataString += rooms.get(i).getByteString();
				if(i != rooms.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameRoom");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
		if(GameSpell.spells != null && !GameSpell.spells.isEmpty()) {
			List<GameSpell> spells = new ArrayList<GameSpell>();
			spells.addAll(GameSpell.spells.values());
			String dataString = "";
			for(int i = 0; i < spells.size(); i++) {
				dataString += spells.get(i).getByteString();
				if(i != spells.size() - 1) {
					dataString += "¦";//separate individual entries
				}
			}
			fileOut = new FileOutputStream("./res/GameSpell");
			fileOut.write(compress(Base64.getEncoder().encode(dataString.getBytes())));
			fileOut.close();
		}
	}

	/**
	 * creationMode() Allows the user to create Game resources
	 */
	private static void creationMode() {
		System.out.println("-- ENTERING CREATION MODE --");
		inCreationMode =  true;
		String createInput;
		while (inCreationMode) {
			System.out.println("Options:\t[N]ew\t[E]dit\t[C]opy\te[X]it");
			createInput = sanitize(sc.nextLine());
			if(createInput.matches("\\w")) {
				switch (createInput) {
				case "x"://EXIT
					System.out.println("-- EXITING CREATION MODE --");
					inCreationMode = false;
					break;
				case "n"://NEW
					contentMode(false);
					break;
				case "e"://EDIT
					contentMode(true);
					break;
				case "c"://COPY
					System.out.println("COPY not yet implemented");
					break;
				default:
					System.out.println("Bad choice: " + createInput.toUpperCase());
					break;
				}
			} else {
				System.out.println("Bad input: " + createInput.toUpperCase());
			}
		}
	}

	/**
	 * contentMode() Allows the User to CREATE or EDIT Game resources
	 * @param isEdit Indicates whether to EDIT existing content
	 */
	private static void contentMode(boolean isEdit) {
		if(isEdit) {
			System.out.println("-- ENTERING CONTENT MODIFICATION MODE --");
		} else {
			System.out.println("-- ENTERING CONTENT CREATION MODE --");
		}
		boolean inContentMode = true;
		String contentString;
		while (inContentMode) {
			System.out.println("Options:\t[R]oom\t[O]bject\t[E]nemy\t[I]tem\t[S]pell\te[X]it");
			contentString = sanitize(sc.nextLine());
			if(contentString.matches("\\w")) {
				switch (contentString) {
				case "x"://EXIT
					if(isEdit) {
						System.out.println("-- EXITING CONTENT MODIFICATION MODE --");
					} else {
						System.out.println("-- EXITING CONTENT CREATION MODE --");
					}
					inContentMode = false;
					break;
				case "r"://ROOM
					roomMode(isEdit);
					break;
				case "o"://OBJECT
					objectMode(isEdit);
					break;
				case "e"://ENEMY
					enemyMode(isEdit);
					break;
				case "i"://ITEM
					itemMode(isEdit);
					break;
				case "s"://SPELL
					spellMode(isEdit);
					break;
				default:
					System.out.println("Bad choice: " + contentString.toUpperCase());
					break;
				}
			} else {
				System.out.println("Bad input: " + contentString.toUpperCase());
			}
		}
	}

	/**
	 * roomMode(boolean) Allows the User to Create or Edit GameRooms
	 * @param isEdit Indicates whether to EDIT existing content
	 */
	private static void roomMode(boolean isEdit) {
		if(isEdit) {
			System.out.println("-- ENTERING GameRoom MODIFICATION MODE --");
		} else {
			System.out.println("-- ENTERING GameRoom CREATION MODE --");
		}
		boolean inRoomMode = true;
		String contentString;
		GameRoom room = null;
		if(isEdit) {//EDIT ROOM mode
			if(GameRoom.rooms == null || GameRoom.rooms.isEmpty()) {
				inRoomMode = false;
				System.out.println("No saved GameRooms to EDIT");
			} else {
				boolean doLookup = true;
				while (doLookup == true && room == null) {
					System.out.println("Enter the ROOM NUMBER of the GameRoom you wish to EDIT (or X to EXIT):");
					contentString = sanitize(sc.nextLine());
					if(contentString.equals("x")) {
						doLookup = false;
						inRoomMode = false;
						System.out.println("-- EXITING GameRoom MODIFICATION MODE --");
					} else if(contentString.matches("\\d+.*")) {//DIGITS only
						String[] tokens = contentString.split("\\s+");
						room = GameRoom.lookup(Integer.parseInt(tokens[0]));
						if(room == null) {
							System.out.println("GameRoom #" + contentString + " not found");
						}
					} else {
						System.out.println("Bad value for GameRoom #: " + contentString.toUpperCase());
					}
				}
				if(room != null) {
					System.out.println("Loading GameRoom #" + room.roomId);
				}
			}
		} else {//NEW ROOM mode
			room = new GameRoom();
		}
		while (inRoomMode) {
			System.out.println("Edit:\t[R]oomId\t[D]escription\t[V]olume\te[X]it");
			contentString = sanitize(sc.nextLine());
			if(contentString.matches("\\w")) {
				switch (contentString) {
				case "x"://EXIT
					if(isEdit) {
						inRoomMode = false;
						System.out.println("-- EXITING GameRoom MODIFICATION MODE --");
					} else {
						if(GameRoom.rooms != null && GameRoom.rooms.containsKey(room.roomId)) {
							System.out.println("GameRoom #" + room.roomId + " already exists. OVERWRITE existing? (Y/N)");
							String doOverwrite = sanitize(sc.nextLine());
							if(doOverwrite.equals("y")) {
								inRoomMode = false;
							} else {
								System.out.println("Will not OVERWRITE. Change the ROOM NUMBER of this GameRoom to SAVE.");
							}
						} else {
							System.out.println("-- EXITING GameRoom CREATION MODE --");
							inRoomMode = false;
						}
					}
					break;
				case "r"://roomId
					if(isEdit) {
						System.out.println("Edit of ROOM NUMBER for saved GameRoom not allowed");
					} else {
						System.out.println("Enter a new ROOM NUMBER:");
						contentString = sanitize(sc.nextLine());
						if(contentString.matches("\\d+")) {
							int roomId = Integer.parseInt(contentString);
							if(GameRoom.rooms != null) {
								if(GameRoom.rooms.containsKey(roomId)) {
									System.out.println("GameRoom with ROOM NUMBER " + roomId + " already exists. Choose another number.");
								} else {
									room.roomId = roomId;
								}
							} else {
								room.roomId = roomId;
							}
						} else {
							System.out.println("Bad input: " + contentString.toUpperCase());
						}
					}
					break;
				case "d"://description
					System.out.println("Current ROOM Description:");
					System.out.println(room.description);
					System.out.println("Enter new ROOM Description:");
					contentString = sc.nextLine();//preserve the Description exactly as entered
					room.description = contentString;
					break;
				case "v"://volume
					System.out.println("-- Entering Room VOLUME Mode --");
					boolean inRoomStructureMode = true;
					while (inRoomStructureMode) {
						System.out.println("Options:\t[L]ength\t[W]idth\t[H]eight\t[C]ontents\te[X]it");
						contentString = sanitize(sc.nextLine());
						switch (contentString) {
						case "x"://EXIT
							inRoomStructureMode = false;
							break;
						case "l"://LENGTH
							boolean modifyXDimension = true;
							if(room.interior.size() > 0) {//interior already set up
								contentString = "";
								while(modifyXDimension && !contentString.matches("y|n")) {
									System.out.println("VOLUME of the ROOM has already been defined. Clear ROOM VOLUME (and contents)? (Y/N)");
									contentString = sanitize(sc.nextLine());
									if(contentString.equals("y")) {
										contentString = "";
										while(modifyXDimension) {
											System.out.println("Enter a new LENGTH for the ROOM (or X to EXIT):");
											contentString = sanitize(sc.nextLine());
											if(contentString.equals("x")) {
												modifyXDimension = false;
											} else if(contentString.matches("\\d+")) {
												int newDimension = Integer.parseInt(contentString);
												if(newDimension > 0 && newDimension <= 100) {
													room.length = newDimension;
													System.out.println("Room LENGTH set to " + newDimension);
													modifyXDimension = false;
												} else {
													System.out.println("Value out of range for LENGTH: " + newDimension);
												}
											} else {
												System.out.println("Bad input: " + contentString.toUpperCase());
											}
										}
										room.interior = makeRoomVolume(room.length, room.width, room.height);
										System.out.println("ROOM VOLUME [" + room.length + "," + room.width + "," + room.height + "] initialized");
									} else if(contentString.equals("n")) {
										System.out.println("ROOM VOLUME (and contents) unchanged");
									} else {
										System.out.println("Bad input: " + contentString.toUpperCase());
									}
								}
							} else {//need at least 1 more dimension to calculate volume
								System.out.println("Specify new LENGTH (X axis) for the ROOM (max 100):");
								contentString = sanitize(sc.nextLine());
								if(contentString.matches("\\d+")) {
									int newDimension = Integer.parseInt(contentString);
									if(newDimension > 0 && newDimension <= 100) {//don't want to go too crazy with ROOM interior sizes
										room.length = Integer.parseInt(contentString);
										System.out.println("ROOM LENGTH set to " + contentString);
									} else {
										System.out.println("Invalid LENGTH: " + contentString);
									}
									if(room.width > 0 && room.height > 0) {//room VOLUME is valid
										room.interior = makeRoomVolume(room.length, room.width, room.height);
										System.out.println("LENGTH, WIDTH, & HEIGHT have been set. You may now set up the CONTENTS of the ROOM.");
									} else {
										if(room.width == 0) {
											System.out.println("ROOM WIDTH not yet set");
										}
										if(room.height == 0) {
											System.out.println("ROOM HEIGHT not yet set");
										}
									}
								} else {
									System.out.println("Bad value for LENGTH: " + contentString.toUpperCase());
								}
							}
							break;
						case "w"://WIDTH
							boolean modifyYDimension = true;
							if(room.interior.size() > 0) {//interior already set up
								contentString = "";
								while(modifyYDimension && !contentString.matches("y|n")) {
									System.out.println("VOLUME of the ROOM has already been defined. Clear ROOM VOLUME (and contents)? (Y/N)");
									contentString = sanitize(sc.nextLine());
									if(contentString.equals("y")) {
										contentString = "";
										while(modifyYDimension) {
											System.out.println("Enter a new WIDTH for the ROOM (or X to EXIT):");
											contentString = sanitize(sc.nextLine());
											if(contentString.equals("x")) {
												modifyYDimension = false;
											} else if(contentString.matches("\\d+")) {
												int newDimension = Integer.parseInt(contentString);
												if(newDimension > 0 && newDimension <= 100) {
													room.width = newDimension;
													System.out.println("Room WIDTH set to " + newDimension);
													modifyYDimension = false;
												} else {
													System.out.println("Value out of range for WIDTH: " + newDimension);
												}
											} else {
												System.out.println("Bad input: " + contentString.toUpperCase());
											}
										}
										room.interior = makeRoomVolume(room.length, room.width, room.height);
										System.out.println("ROOM VOLUME [" + room.length + "," + room.width + "," + room.height + "] initialized");
									} else if(contentString.equals("n")) {
										System.out.println("ROOM VOLUME (and contents) unchanged");
									} else {
										System.out.println("Bad input: " + contentString.toUpperCase());
									}
								}
							} else {//need at least 1 more dimension to calculate volume
								System.out.println("Specify new WIDTH (X axis) for the ROOM (max 100):");
								contentString = sanitize(sc.nextLine());
								if(contentString.matches("\\d+")) {
									int newDimension = Integer.parseInt(contentString);
									if(newDimension > 0 && newDimension <= 100) {//don't want to go too crazy with ROOM interior sizes
										room.width = Integer.parseInt(contentString);
										System.out.println("ROOM WIDTH set to " + contentString);
									} else {
										System.out.println("Invalid WIDTH: " + contentString);
									}
									if(room.length > 0 && room.height > 0) {//room VOLUME is valid
										room.interior = makeRoomVolume(room.length, room.width, room.height);
										System.out.println("LENGTH, WIDTH, & HEIGHT have been set. You may now set up the CONTENTS of the ROOM.");
									} else {
										if(room.length == 0) {
											System.out.println("ROOM LENGTH not yet set");
										}
										if(room.height == 0) {
											System.out.println("ROOM HEIGHT not yet set");
										}
									}
								} else {
									System.out.println("Bad value for WIDTH: " + contentString.toUpperCase());
								}
							}
							break;
						case "h"://HEIGHT
							boolean modifyZDimension = true;
							if(room.interior.size() > 0) {//interior already set up
								contentString = "";
								while(modifyZDimension && !contentString.matches("y|n")) {
									System.out.println("VOLUME of the ROOM has already been defined. Clear ROOM VOLUME (and contents)? (Y/N)");
									contentString = sanitize(sc.nextLine());
									if(contentString.equals("y")) {
										contentString = "";
										while(modifyZDimension) {
											System.out.println("Enter a new HEIGHT for the ROOM (or X to EXIT):");
											contentString = sanitize(sc.nextLine());
											if(contentString.equals("x")) {
												modifyZDimension = false;
											} else if(contentString.matches("\\d+")) {
												int newDimension = Integer.parseInt(contentString);
												if(newDimension > 0 && newDimension <= 100) {
													room.height = newDimension;
													System.out.println("Room HEIGHT set to " + newDimension);
													modifyZDimension = false;
												} else {
													System.out.println("Value out of range for HEIGHT: " + newDimension);
												}
											} else {
												System.out.println("Bad input: " + contentString.toUpperCase());
											}
										}
										room.interior = makeRoomVolume(room.length, room.width, room.height);
										System.out.println("ROOM VOLUME [" + room.length + "," + room.width + "," + room.height + "] initialized");
									} else if(contentString.equals("n")) {
										System.out.println("ROOM VOLUME (and contents) unchanged");
									} else {
										System.out.println("Bad input: " + contentString.toUpperCase());
									}
								}
							} else {//need at least 1 more dimension to calculate volume
								System.out.println("Specify new HEIGHT (X axis) for the ROOM (max 100):");
								contentString = sanitize(sc.nextLine());
								if(contentString.matches("\\d+")) {
									int newDimension = Integer.parseInt(contentString);
									if(newDimension > 0 && newDimension <= 100) {//don't want to go too crazy with ROOM interior sizes
										room.height = Integer.parseInt(contentString);
										System.out.println("ROOM HEIGHT set to " + contentString);
									} else {
										System.out.println("Invalid HEIGHT: " + contentString);
									}
									if(room.length > 0 && room.width > 0) {//room VOLUME is valid
										room.interior = makeRoomVolume(room.length, room.width, room.height);
										System.out.println("LENGTH, WIDTH, & HEIGHT have been set. You may now set up the CONTENTS of the ROOM.");
									} else {
										if(room.length == 0) {
											System.out.println("ROOM LENGTH not yet set");
										}
										if(room.width == 0) {
											System.out.println("ROOM WIDTH not yet set");
										}
									}
								} else {
									System.out.println("Bad value for HEIGHT: " + contentString.toUpperCase());
								}
							}
							break;
						case "c"://CONTENTS
							System.out.println("-- Entering ROOM CONTENT Mode --");
							boolean inRoomStructureContentMode = true;
							while (inRoomStructureContentMode) {
								if(room.interior == null || room.interior.isEmpty()) {
									inRoomStructureContentMode = false;
									System.out.println("You must first set the LENGTH, WIDTH, and HEIGHT of this ROOM");
								}
								//get X,Y,Z coordinates
								System.out.println("Which X,Y,Z coordinate do you wish to edit the CONTENT of?");
								boolean inXCoordinateSelectionMode = true;
								boolean inYCoordinateSelectionMode = false;
								boolean inZCoordinateSelectionMode = false;
								int xPos = 0;
								int yPos = 0;
								int zPos = 0;
								boolean gotVolume = false;
								GameVolume volumeToEdit = null;
								while (inXCoordinateSelectionMode && xPos == 0) {
									System.out.println("Enter an X coordinate (or X to EXIT):");
									contentString = sanitize(sc.nextLine());
									if(contentString.equals("x")) {
										inXCoordinateSelectionMode = false;
										xPos = 0;
									} else if(contentString.matches("\\d+")) {
										xPos = Integer.parseInt(contentString);
										if(room.length >= xPos && xPos > 0) {
											System.out.println("X coordinate set to " + xPos + ".");
											inYCoordinateSelectionMode = true;
											while (inYCoordinateSelectionMode && yPos == 0) {
												System.out.println("Enter a Y coordinate (or X to EXIT):");
												contentString = sanitize(sc.nextLine());
												if(contentString.equals("x")) {
													inYCoordinateSelectionMode = false;
													yPos = 0;
													xPos = 0;
												} else if(contentString.matches("\\d+")) {
													yPos = Integer.parseInt(contentString);
													if(room.width >= yPos && yPos > 0) {
														System.out.println("Y coordinate set to " + yPos + ".");
														inZCoordinateSelectionMode = true;
														while (inZCoordinateSelectionMode && zPos == 0) {
															System.out.println("Enter a Z coordinate (or X to EXIT):");
															contentString = sanitize(sc.nextLine());
															if(contentString.equals("x")) {
																inZCoordinateSelectionMode = false;
																zPos = 0;
																yPos = 0;
															} else if(contentString.matches("\\d+")) {
																zPos = Integer.parseInt(contentString);
																if(room.height >= zPos && zPos > 0) {
																	System.out.println("Z coordinate set to " + zPos + ".");
																	//index = (xPos * width * height) + (yPos * height) + zPos, WHERE x ~ length && y ~ width && z ~ height
																	volumeToEdit = room.interior.get(((zPos - 1) * room.width * room.length) + ((yPos - 1) * room.length) + (xPos - 1));
																	gotVolume = true;
																} else {
																	System.out.println("Z coordinate of " + zPos + " is out of range.");
																}
															}
														}
													} else {
														System.out.println("Y coordinate of " + yPos + " is out of range.");
													}
												}
											}
										} else {
											System.out.println("X coordinate of " + xPos + " is out of range.");
											xPos = 0;
										}
									}
								}
								if(gotVolume = true) {
									System.out.println("-- ROOM VOLUME " + xPos + "," + yPos + "," + zPos + " --");
								}
								while (gotVolume && volumeToEdit != null) {
									System.out.println("Options:\t[E]nemies\t[I]tems\t[O]bjects\te[X]it");
									contentString = sanitize(sc.nextLine());
									switch (contentString) {
									case "x":
										gotVolume = false;
//										volumeToEdit = null;
										inRoomStructureContentMode = false;
										break;
									case "i"://items
										System.out.println("-- Entering Room ITEM Mode --");
										boolean inRoomItemMode = true;
										while (inRoomItemMode) {
											System.out.println("Options:\t[A]dd\t[R]emove\t[Q]uantity\te[X]it");
											contentString = sanitize(sc.nextLine());
											switch (contentString) {
											case "x"://EXIT
												inRoomItemMode = false;
												break;
											case "a"://ADD
												boolean isAdd = true;
												while (isAdd) {
													System.out.println("Enter an ITEM name to ADD it to this ROOM (or X to EXIT):");
													String addString = sanitize(sc.nextLine());
													if(addString.matches("\\w+")) {
														if(addString.equals("x")) {
															isAdd = false;
														} else if(GameItem.items != null && GameItem.items.containsKey(addString)) {//valid GameItem
															volumeToEdit.addItem(GameItem.lookup(addString));
															System.out.println(addString.toUpperCase() + " added to the ROOM");
														} else {//invalid GameItem
															System.out.println("GameItem with name " + addString.toUpperCase() + " not found");
														}
													} else {
														System.out.println("Bad input: " + addString.toUpperCase());
													}
												}
												break;
											case "r"://REMOVE
												boolean isRemove = true;
												while (isRemove) {
													System.out.println("Enter an ITEM name to REMOVE it from this ROOM (or X to EXIT:");
													String removeString = sanitize(sc.nextLine());
													if(removeString.matches("\\w+")) {
														if(removeString.equals("x")) {
															isRemove = false;
														} else if(GameItem.items != null && GameItem.items.containsKey(removeString)) {//valid GameItem
															if(!volumeToEdit.containsItem(removeString)) {
																System.out.println("There is no " + removeString.toUpperCase() + " to REMOVE from this ROOM");
															} else {
																volumeToEdit.items.remove(removeString);
																System.out.println(removeString.toUpperCase() + " removed from the ROOM");
															}
														} else {//invalid GameItem
															System.out.println("GameItem with name " + removeString.toUpperCase() + " not found");
														}
													} else {
														System.out.println("Bad input: " + removeString.toUpperCase());
													}
												}
												break;
											case "q"://QUANTITY
												boolean isQuantity = true;
												while (isQuantity) {
													System.out.println("Enter an ITEM name to change its QUANTITY in the ROOM (or X to EXIT):");
													String quantityString = sanitize(sc.nextLine());
													if(quantityString.matches("\\w+")) {
														if(quantityString.equals("x")) {
															isQuantity = false;
														} else if(GameItem.items != null && GameItem.items.containsKey(quantityString)) {//valid GameItem
															if(!volumeToEdit.containsItem(quantityString)) {
																System.out.println("There is no " + quantityString.toUpperCase() + " to adjust QUANITTY for in this ROOM");
															} else {
																GameItem item = volumeToEdit.items.get(quantityString).get(0);//TODO: reconsider how to do this
																if(item.maxUses > 0) {//use-based item
																	System.out.println(quantityString + " is not a QUANTITY-based item");
																} else {//quantity-based item
																	String quantity = "";
																	while(!quantity.matches("\\d+")) {
																		System.out.println("Current QUANTITY of " + quantityString.toUpperCase() + "in the ROOM: " + item.quantity);
																		System.out.println("Enter new QUANTITY:");
																		quantity = sanitize(sc.nextLine());
																	}
																	item.quantity = Integer.parseInt(quantity);
																	System.out.println("QUANTITY of " + quantityString.toUpperCase() + " in the ROOM set to " + quantity);
																}
															}
														} else {//invalid GameItem
															System.out.println("GameItem with name " + quantityString.toUpperCase() + " not found");
														}
													} else {
														System.out.println("Bad input: " + quantityString.toUpperCase());
													}
												}
												break;
											default:
												System.out.println("Bad input: " + contentString.toUpperCase());
												break;
											}
										}
										System.out.println("-- Leaving Room ITEM Mode --");
										break;
									case "o"://objects
										System.out.println("-- Entering Room OBJECT Mode --");
										boolean inRoomObjectMode = true;
										while (inRoomObjectMode) {
											System.out.println("Options:\t[A]dd\t[R]emove\te[X]it");
											contentString = sanitize(sc.nextLine());
											switch (contentString) {
											case "x"://EXIT
												inRoomObjectMode = false;
												break;
											case "a"://ADD
												boolean isAdd = true;
												while (isAdd) {
													System.out.println("Enter an OBJECT name to ADD it to this ROOM (or X to EXIT):");
													String addString = sanitize(sc.nextLine());
													if(addString.matches("\\w+")) {
														if(addString.equals("x")) {
															isAdd = false;
														} else if(GameObject.objects != null && GameObject.objects.containsKey(addString)) {//valid GameObject
															volumeToEdit.addObject(GameObject.lookup(addString));
															System.out.println(addString.toUpperCase() + " added to the ROOM");
														} else {//invalid GameObject
															System.out.println("GameObject with name " + addString.toUpperCase() + " not found");
														}
													} else {
														System.out.println("Bad input: " + addString.toUpperCase());
													}
												}
												break;
											case "r"://REMOVE
												boolean isRemove = true;
												while (isRemove) {
													System.out.println("Enter an OBJECT name to REMOVE it from this ROOM (or X to EXIT:");
													String removeString = sanitize(sc.nextLine());
													if(removeString.matches("\\w+")) {
														if(removeString.equals("x")) {
															isRemove = false;
														} else if(GameObject.objects != null && GameObject.objects.containsKey(removeString)) {//valid GameObject
															if(!volumeToEdit.containsObject(removeString)) {
																System.out.println("There is no " + removeString.toUpperCase() + " to REMOVE from this ROOM");
															} else {
																volumeToEdit.objects.remove(removeString);
																System.out.println(removeString.toUpperCase() + " removed from the ROOM");
															}
														} else {//invalid GameObject
															System.out.println("GameObject with name " + removeString.toUpperCase() + " not found");
														}
													} else {
														System.out.println("Bad input: " + removeString.toUpperCase());
													}
												}
												break;
											case "q"://QUANTITY
												//
												break;
											default:
												System.out.println("Bad input: " + contentString.toUpperCase());
												break;
											}
										}
										System.out.println("-- Leaving Room OBJECT Mode --");
										break;
									case "e"://enemies
										System.out.println("-- Entering Room ENEMY Mode --");
										boolean inRoomEnemyMode = true;
										while (inRoomEnemyMode) {
											System.out.println("Options:\t[A]dd\t[R]emove\te[X]it");
											contentString = sanitize(sc.nextLine());
											switch (contentString) {
											case "x"://EXIT
												inRoomEnemyMode = false;
												break;
											case "a"://ADD
												boolean isAdd = true;
												while (isAdd) {
													System.out.println("Enter an ENEMY name to ADD it to this ROOM (or X to EXIT):");
													String addString = sanitize(sc.nextLine());
													if(addString.matches("\\w+")) {
														if(addString.equals("x")) {
															isAdd = false;
														} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(addString)) {//valid GameEnemy
															volumeToEdit.addEnemy(GameEnemy.lookup(addString));
															System.out.println(addString.toUpperCase() + " added to the ROOM");
														} else {//invalid GameEnemy
															System.out.println("GameEnemy with name " + addString.toUpperCase() + " not found");
														}
													} else {
														System.out.println("Bad input: " + addString.toUpperCase());
													}
												}
												break;
											case "r"://REMOVE
												boolean isRemove = true;
												while (isRemove) {
													System.out.println("Enter an ENEMY name to REMOVE it from this ROOM (or X to EXIT:");
													String removeString = sanitize(sc.nextLine());
													if(removeString.matches("\\w+")) {
														if(removeString.equals("x")) {
															isRemove = false;
														} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(removeString)) {//valid GameEnemy
															if(!volumeToEdit.containsEnemy(removeString)) {
																System.out.println("There is no " + removeString.toUpperCase() + " to REMOVE from this ROOM");
															} else {
																volumeToEdit.enemies.remove(removeString);
																System.out.println(removeString.toUpperCase() + " removed from the ROOM");
															}
														} else {//invalid GameEnemy
															System.out.println("GameEnemy with name " + removeString.toUpperCase() + " not found");
														}
													} else {
														System.out.println("Bad input: " + removeString.toUpperCase());
													}
												}
												break;
											case "q"://QUANTITY
												//TODO: figure out how to have more than 1 Enemy of the same TYPE in the same ROOM...differentiate between the instances somehow...
												break;
											default:
												System.out.println("Bad input: " + contentString.toUpperCase());
												break;
											}
										}
										System.out.println("-- Leaving Room ENEMY Mode --");
										break;
									default:
										System.out.println("Bad input: " + contentString.toUpperCase());
										break;
									}
								}
								if(volumeToEdit != null) {
									String confirmString = null;
									while(confirmString == null || (!confirmString.equals("y") && !confirmString.equals("n"))) {
										System.out.println("Save changes? Y/N");
										confirmString = sanitize(sc.nextLine());
										if(confirmString.equals("y")) {
											room.interior.set(((volumeToEdit.zPos - 1) * room.width * room.length) + ((volumeToEdit.yPos - 1) * room.length) + (volumeToEdit.xPos - 1), volumeToEdit);
											System.out.println("GameVolume [" + volumeToEdit.xPos + "," + volumeToEdit.yPos + "," + volumeToEdit.zPos + "] saved");
										} else if(confirmString.equals("n")) {
											System.out.println("Changes DISCARDED");
										}
									}
								}
							}
							System.out.println("-- Leaving ROOM CONTENT Mode --");
							break;
						default:
							System.out.println("Bad input: " + contentString.toUpperCase());
							break;
						}
					}
					System.out.println("-- Leaving Room VOLUME Mode --");
					break;
				default:
					System.out.println("Bad input: " + contentString.toUpperCase());
					break;
				}
			} else {
				System.out.println("Bad input: " + contentString.toUpperCase());
			}
		}
		if(room != null) {
			String confirmString = null;
			while(confirmString == null || (!confirmString.equals("y") && !confirmString.equals("n"))) {
				System.out.println("Save changes? Y/N");
				confirmString = sanitize(sc.nextLine());
				if(confirmString.equals("y")) {
					if(isEdit) {
						GameRoom.rooms.replace(room.roomId, room);
						System.out.println("GameRoom #" + room.roomId + " saved");
					} else {
						if(GameRoom.rooms != null) {
							if(GameRoom.rooms.containsKey(room.roomId)) {
								GameRoom.rooms.replace(room.roomId, room);
								System.out.println("GameRoom #" + room.roomId + " saved");
							} else {
								GameRoom.add(room.roomId, room);
								System.out.println("GameRoom #" + room.roomId + " saved");
							}
						} else {
							GameRoom.rooms = new HashMap<Integer,GameRoom>();
							GameRoom.add(room.roomId,room);
							System.out.println("GameRoom #" + room.roomId + " saved");
						}
					}
				} else if(confirmString.equals("n")) {
					System.out.println("Changes DISCARDED");
				}
			}
		}
	}

	/**
	 * objectMode(boolean) Allows the User to Create or Edit GameObjects
	 * @param isEdit Indicates whether to EDIT existing content
	 */
	private static void objectMode(boolean isEdit) {
		if(isEdit) {
			System.out.println("-- ENTERING GameObject MODIFICATION MODE --");
		} else {
			System.out.println("-- ENTERING GameObject CREATION MODE --");
		}
		boolean inObjectMode = true;
		String contentString;
		GameObject object = null;
		if(isEdit) {//EDIT OBJECT mode
			if(GameObject.objects == null || GameObject.objects.isEmpty()) {
				inObjectMode = false;
				System.out.println("No saved GameObjects to EDIT");
			} else {
				boolean doLookup = true;
				while (doLookup == true && object == null) {
					System.out.println("Enter the name of the GameObject you wish to EDIT (or X to EXIT):");
					contentString = sanitize(sc.nextLine());
					if(contentString.equals("x")) {
						doLookup = false;
						inObjectMode = false;
						System.out.println("-- EXITING GameObject MODIFICATION MODE --");
					} else if(contentString.matches("\\w+.*")) {
						String[] tokens = contentString.split("\\s+");
						object = GameObject.lookup(tokens[0]);
					}
					if(object == null) {
						System.out.println("GameObject " + contentString + " not found");
					}
				}
				System.out.println("Loading GameObject " + object.name.toUpperCase());
			}
		} else {//NEW OBJECT mode
			object = new GameObject();
		}
		while (inObjectMode) {
			System.out.println("Edit:\t[N]ame\t[D]escription\t[C]ontainer\t[O]pen\t[L]ocked\t[I]tems\t[M]ovable\t[B]reakable\tCurrent [H]P\tMax H[P]\te[X]it");
			contentString = sanitize(sc.nextLine());
			if(contentString.matches("\\w+")) {
				switch (contentString) {
				case "x"://EXIT
					if(isEdit) {
						inObjectMode = false;
						System.out.println("-- EXITING GameObject MODIFICATION MODE --");
					} else {
						if(GameObject.objects != null && GameObject.objects.containsKey(object.name)) {
							System.out.println("GameObject " + object.name.toUpperCase() + " already exists. OVERWRITE existing? (Y/N)");
							String doOverwrite = sanitize(sc.nextLine());
							if(doOverwrite.equals("y")) {
								inObjectMode = false;
							} else {
								System.out.println("Will not OVERWRITE. Change the NAME of this GameObject to SAVE.");
							}
						} else {
							System.out.println("-- EXITING GameObject CREATION MODE --");
							inObjectMode = false;
						}
					}
					break;
				case "n"://name
					System.out.println("Current OBJECT Name:");
					System.out.println(object.name);
					System.out.println("Enter new OBJECT Name:");
					contentString = sc.nextLine();//preserve the Name exactly as entered
					if(isEdit) {
						System.out.println("Currently unable to EDIT the name of an OBJECT after it is SAVEd");
					} else {
						object.name = contentString.toLowerCase();//TODO: manage multi-word names for OBJECTs, etc.
						System.out.println("OBJECT name set to " + contentString);
					}
					break;
				case "d"://description
					System.out.println("Current Description:");
					System.out.println(object.description);
					System.out.println("Enter new Description:");
					contentString = sc.nextLine();//preserve the Description exactly as entered
					object.description = contentString;
					System.out.println("Description set to \"" + contentString + "\"");
					break;
				case "c"://isContainer
					System.out.println("Is Container?:");
					System.out.println(object.isContainer);
					System.out.println("Should be Container?:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("(t(rue)?|f(alse)?)")) {
						boolean isContainer = Boolean.parseBoolean(contentString);
						object.isContainer = isContainer;
						System.out.println("Is Container set to " + isContainer);
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "o"://isOpen
					System.out.println("Is Opoen?:");
					System.out.println(object.isOpen);
					System.out.println("Should be Open?:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("(t(rue)?|f(alse)?)")) {
						boolean isOpen = Boolean.parseBoolean(contentString);
						object.isOpen = isOpen;
						System.out.println("Is Open set to " + isOpen);
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "l"://isLocked
					System.out.println("Is Locked?:");
					System.out.println(object.isLocked);
					System.out.println("Should be Locked?:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("(t(rue)?|f(alse)?)")) {
						boolean isLocked = Boolean.parseBoolean(contentString);
						object.isLocked = isLocked;
						System.out.println("Is Locked set to " + isLocked);
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "i"://contents
					System.out.println("-- Entering OBJECT ITEMs Mode --");
					boolean inObjectItemMode = true;
					while (inObjectItemMode) {
						System.out.println("Options:\t[A]dd\t[R]emove\t[Q]uantity\te[X]it");
						contentString = sanitize(sc.nextLine());
						switch (contentString) {
						case "x"://EXIT
							inObjectItemMode = false;
							break;
						case "a"://ADD
							boolean isAdd = true;
							while (isAdd) {
								System.out.println("Enter an ITEM name to ADD it to this OBJECT (or X to EXIT):");
								String addString = sanitize(sc.nextLine());
								if(addString.matches("\\w+")) {
									if(addString.equals("x")) {
										isAdd = false;
									} else if(GameItem.items != null && GameItem.items.containsKey(addString)) {//valid GameItem
										GameItem item = GameItem.lookup(addString);
										if(object.contents != null) {
											if(object.contents.contains(item)) {
												System.out.println(addString.toUpperCase() + " already present. Use QUANTITY option to change quantity of Item.");
											} else {
												object.contents.add(item);
												System.out.println(addString.toUpperCase() + " added to the OBJECT");
											}
										}
									} else {//invalid GameItem
										System.out.println("GameItem with name " + addString.toUpperCase() + " not found");
									}
								} else {
									System.out.println("Bad input: " + addString.toUpperCase());
								}
							}
							break;
						case "r"://REMOVE
							boolean isRemove = true;
							while (isRemove) {
								System.out.println("Enter an ITEM name to REMOVE it from this OBJECT (or X to EXIT:");
								String removeString = sanitize(sc.nextLine());
								if(removeString.matches("\\w+")) {
									if(removeString.equals("x")) {
										isRemove = false;
									} else if(GameItem.items != null && GameItem.items.containsKey(removeString)) {//valid GameItem
										GameItem item = GameItem.lookup(removeString);
										if(object.contents == null || !object.contents.contains(item)) {
											System.out.println("There is no " + removeString.toUpperCase() + " to REMOVE from this OBJECT");
										} else {
											object.contents.remove(item);
											System.out.println(removeString.toUpperCase() + " removed from the OBJECT");
										}
									} else {//invalid GameItem
										System.out.println("GameItem with name " + removeString.toUpperCase() + " not found");
									}
								} else {
									System.out.println("Bad input: " + removeString.toUpperCase());
								}
							}
							break;
						case "q"://QUANTITY
							boolean isQuantity = true;
							while (isQuantity) {
								System.out.println("Enter an ITEM name to change its QUANTITY in the OBJECT (or X to EXIT):");
								String quantityString = sanitize(sc.nextLine());
								if(quantityString.matches("\\w+")) {
									if(quantityString.equals("x")) {
										isQuantity = false;
									} else if(GameItem.items != null && GameItem.items.containsKey(quantityString)) {//valid GameItem
										GameItem item = GameItem.lookup(quantityString);
										if(object.contents == null || !object.contents.contains(item)) {
											System.out.println("There is no " + quantityString.toUpperCase() + " to adjust QUANITTY for in this OBJECT");
										} else {
											if(item.maxUses > 0) {//use-based item
												System.out.println(quantityString + " is not a QUANTITY-based item");
											} else {//quantity-based item
												String quantity = "";
												while(!quantity.matches("\\d+")) {
													System.out.println("Current QUANTITY of " + quantityString.toUpperCase() + "in the OBJECT: " + item.quantity);
													System.out.println("Enter new QUANTITY:");
													quantity = sanitize(sc.nextLine());
												}
												item.quantity = Integer.parseInt(quantity);
												System.out.println("QUANTITY of " + quantityString.toUpperCase() + " in the OBJECT set to " + quantity);
											}
										}
									} else {//invalid GameItem
										System.out.println("GameItem with name " + quantityString.toUpperCase() + " not found");
									}
								} else {
									System.out.println("Bad input: " + quantityString.toUpperCase());
								}
							}
							break;
						default:
							System.out.println("Bad input: " + contentString.toUpperCase());
							break;
						}
					}
					System.out.println("-- Leaving OBJECT ITEMs Mode --");
					break;
				case "m"://isMovable
					System.out.println("Is Moveable?:");
					System.out.println(object.isMovable);
					System.out.println("Should be Moveable?:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("(t(rue)?|f(alse)?)")) {
						boolean isMoveable = Boolean.parseBoolean(contentString);
						object.isMovable = isMoveable;
						System.out.println("Is Moveable set to " + isMoveable);
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "b"://isBreakable
					System.out.println("Is Breakable?:");
					System.out.println(object.isBreakable);
					System.out.println("Enter new Breakability:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("(t(rue)?|f(alse)?)")) {
						boolean isBreakable = Boolean.parseBoolean(contentString);
						object.isBreakable = isBreakable;
						System.out.println("Breakability set to " + isBreakable);
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "h"://currentHp
					System.out.println("Current HP:");
					System.out.println(object.currentHp);
					System.out.println("Enter new HP:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int currentHp = Integer.parseInt(contentString);
						if(currentHp >= 0) {
							object.currentHp = currentHp;
							System.out.println("HP set to " + currentHp);
						} else {
							System.out.println("Value not valid: " + currentHp);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "p"://maxHp
					System.out.println("Max HP:");
					System.out.println(object.maxHp);
					System.out.println("Enter new Max HP:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int maxHp = Integer.parseInt(contentString);
						if(maxHp >= 0) {
							object.maxHp = maxHp;
							System.out.println("Max HP set to " + maxHp);
						} else {
							System.out.println("Value not valid: " + maxHp);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				default:
					System.out.println("Bad input: " + contentString.toUpperCase());
					break;
				}
			} else {
				System.out.println("Bad input: " + contentString.toUpperCase());
			}
		}
		if(object != null) {
			String confirmString = null;
			while(confirmString == null || (!confirmString.equals("y") && !confirmString.equals("n"))) {
				System.out.println("Save changes? Y/N");
				confirmString = sanitize(sc.nextLine());
				if(confirmString.equals("y")) {
					if(isEdit) {
						GameObject.objects.replace(object.name, object);
						System.out.println("GameObject " + object.name.toUpperCase() + " saved");
					} else {
						if(GameObject.objects != null) {
							if(GameObject.objects.containsKey(object.name)) {
								System.out.println("GameObject " + object.name.toUpperCase() + " saved");
								GameObject.objects.replace(object.name, object);
							} else {
								GameObject.add(object);
								System.out.println("GameObject " + object.name.toUpperCase() + " saved");
							}
						} else {
							GameObject.objects = new HashMap<String,GameObject>();
							GameObject.add(object);
							System.out.println("GameObject " + object.name.toUpperCase() + " saved");
						}
					}
				} else if(confirmString.equals("n")) {
					System.out.println("Changes DISCARDED");
				}
			}
		}
	}

	/**
	 * enemyMode(boolean) Allows the User to Create or Edit GameEnemies
	 * @param isEdit Indicates whether to EDIT existing content
	 */
	private static void enemyMode(boolean isEdit) {
		if(isEdit) {
			System.out.println("-- ENTERING GameEnemy MODIFICATION MODE --");
		} else {
			System.out.println("-- ENTERING GameEnemy CREATION MODE --");
		}
		boolean inEnemyMode = true;
		String contentString;
		GameEnemy enemy = null;
		if(isEdit) {//EDIT ENEMY mode
			if(GameEnemy.enemies == null || GameEnemy.enemies.isEmpty()) {
				inEnemyMode = false;
				System.out.println("No saved GameEnemies to EDIT");
			} else {
				boolean doLookup = true;
				while (doLookup == true && enemy == null) {
					System.out.println("Enter the name of the GameEnemy you wish to EDIT (or X to EXIT):");
					contentString = sanitize(sc.nextLine());
					if(contentString.equals("x")) {
						doLookup = false;
						inEnemyMode = false;
						System.out.println("-- EXITING GameEnemy MODIFICATION MODE --");
					} else if(contentString.matches("\\w+.*")) {
						String[] tokens = contentString.split("\\s+");
						enemy = GameEnemy.lookup(tokens[0]);
					}
					if(enemy == null) {
						System.out.println("GameEnemy " + contentString + " not found");
					}
				}
				System.out.println("Loading GameEnemy " + enemy.name.toUpperCase());
			}
		} else {//NEW ENEMY mode
			enemy = new GameEnemy();
		}
		while (inEnemyMode) {
			System.out.println("Edit:\t[N]ame\t[D]escription\t[C]urrent HP\t[M]ax HP\t[H]eld Item\tXP [V]alue\t[I]tems\t[A]rmor Class\tDamage [R]eduction\te[X]it");
			contentString = sanitize(sc.nextLine());
			if(contentString.matches("\\w+")) {
				switch (contentString) {
				case "x"://EXIT
					if(isEdit) {
						inEnemyMode = false;
						System.out.println("-- EXITING GameEnemy MODIFICATION MODE --");
					} else {
						if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(enemy.name)) {
							System.out.println("GameEnemy " + enemy.name.toUpperCase() + " already exists. OVERWRITE existing? (Y/N)");
							String doOverwrite = sanitize(sc.nextLine());
							if(doOverwrite.equals("y")) {
								inEnemyMode = false;
							} else {
								System.out.println("Will not OVERWRITE. Change the NAME of this GameEnemy to SAVE.");
							}
						} else {
							System.out.println("-- EXITING GameEnemy CREATION MODE --");
							inEnemyMode = false;
						}
					}
					break;
				case "n"://name
					System.out.println("Current ENEMY Name:");
					System.out.println(enemy.name);
					System.out.println("Enter new ENEMY Name:");
					contentString = sc.nextLine();//preserve the Name exactly as entered
					if(isEdit) {
						System.out.println("Currently unable to EDIT the name of an ENEMY after it is SAVEd");
					} else {
						enemy.name = contentString.toLowerCase();//TODO: manage multi-word names for ENEMIES, etc.
						System.out.println("ENEMY name set to " + contentString);
					}
					break;
				case "d"://description
					System.out.println("Current Description:");
					System.out.println(enemy.description);
					System.out.println("Enter new Description:");
					contentString = sc.nextLine();//preserve the Description exactly as entered
					enemy.description = contentString;
					System.out.println("Description set to \"" + contentString + "\"");
					break;
				case "c"://currentHp
					System.out.println("Current HP:");
					System.out.println(enemy.currentHp);
					System.out.println("Enter new HP:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int currentHp = Integer.parseInt(contentString);
						if(currentHp >= 0) {
							enemy.currentHp = currentHp;
							System.out.println("HP set to " + currentHp);
						} else {
							System.out.println("Value not valid: " + currentHp);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "m"://maxHp
					System.out.println("Max HP:");
					System.out.println(enemy.maxHp);
					System.out.println("Enter new Max HP:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int maxHp = Integer.parseInt(contentString);
						if(maxHp >= 0) {
							enemy.maxHp = maxHp;
							System.out.println("Max HP set to " + maxHp);
						} else {
							System.out.println("Value not valid: " + maxHp);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "h"://heldItem
					System.out.println("Current Held Item:");
					System.out.println(enemy.heldItem);
					System.out.println("Enter new Held Item:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\w+.*")) {
						String[] tokens = contentString.split("\\s+");
						if(tokens.length > 0) {
							if(GameItem.items != null && GameItem.items.containsKey(tokens[0])) {//TODO: change this to containsKey(contentString) after fixing up multi-word names
								GameItem item = GameItem.lookup(tokens[0]);
								if(enemy.inventory.contains(item)) {
									enemy.heldItem = tokens[0];
									System.out.println("Held Item set to " + tokens[0].toUpperCase());
								} else {
									System.out.println(tokens[0].toUpperCase() + " not present in GameEnemy inventory");
								}
							} else {
								System.out.println(tokens[0].toUpperCase() + " is not a valid GameItem");
							}
						} else {
							System.out.println("Value not valid: " + tokens[0].toUpperCase());
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "v"://xpValue
					System.out.println("Current XP Value:");
					System.out.println(enemy.xpValue);
					System.out.println("Enter new XP Value:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int xpValue = Integer.parseInt(contentString);
						if(xpValue >= 0) {
							enemy.xpValue = xpValue;
							System.out.println("XP Value set to " + xpValue);
						} else {
							System.out.println("Value not valid: " + xpValue);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "i"://inventory
					System.out.println("-- Entering ENEMY ITEMs Mode --");
					boolean inObjectItemMode = true;
					while (inObjectItemMode) {
						System.out.println("Options:\t[A]dd\t[R]emove\t[Q]uantity\te[X]it");
						contentString = sanitize(sc.nextLine());
						switch (contentString) {
						case "x"://EXIT
							inObjectItemMode = false;
							break;
						case "a"://ADD
							boolean isAdd = true;
							while (isAdd) {
								System.out.println("Enter an ITEM name to ADD it to this ENEMY (or X to EXIT):");
								String addString = sanitize(sc.nextLine());
								if(addString.matches("\\w+")) {
									if(addString.equals("x")) {
										isAdd = false;
									} else if(GameItem.items != null && GameItem.items.containsKey(addString)) {//valid GameItem
										GameItem item = GameItem.lookup(addString);
										if(enemy.inventory != null) {
											if(enemy.inventory.contains(item)) {
												System.out.println(addString.toUpperCase() + " already present. Use QUANTITY option to change quantity of Item.");
											} else {
												enemy.inventory.add(item);
												System.out.println(addString.toUpperCase() + " added to the ENEMY");
											}
										}
									} else {//invalid GameItem
										System.out.println("GameItem with name " + addString.toUpperCase() + " not found");
									}
								} else {
									System.out.println("Bad input: " + addString.toUpperCase());
								}
							}
							break;
						case "r"://REMOVE
							boolean isRemove = true;
							while (isRemove) {
								System.out.println("Enter an ITEM name to REMOVE it from this ENEMY (or X to EXIT:");
								String removeString = sanitize(sc.nextLine());
								if(removeString.matches("\\w+")) {
									if(removeString.equals("x")) {
										isRemove = false;
									} else if(GameItem.items != null && GameItem.items.containsKey(removeString)) {//valid GameItem
										GameItem item = GameItem.lookup(removeString);
										if(enemy.inventory == null || !enemy.inventory.contains(item)) {
											System.out.println("There is no " + removeString.toUpperCase() + " to REMOVE from this ENEMY");
										} else {
											enemy.inventory.remove(item);
											System.out.println(removeString.toUpperCase() + " removed from the ENEMY");
										}
									} else {//invalid GameItem
										System.out.println("GameItem with name " + removeString.toUpperCase() + " not found");
									}
								} else {
									System.out.println("Bad input: " + removeString.toUpperCase());
								}
							}
							break;
						case "q"://QUANTITY
							boolean isQuantity = true;
							while (isQuantity) {
								System.out.println("Enter an ITEM name to change its QUANTITY in the ENEMY (or X to EXIT):");
								String quantityString = sanitize(sc.nextLine());
								if(quantityString.matches("\\w+")) {
									if(quantityString.equals("x")) {
										isQuantity = false;
									} else if(GameItem.items != null && GameItem.items.containsKey(quantityString)) {//valid GameItem
										GameItem item = GameItem.lookup(quantityString);
										if(enemy.inventory == null || !enemy.inventory.contains(item)) {
											System.out.println("There is no " + quantityString.toUpperCase() + " to adjust QUANITTY for in this ENEMY");
										} else {
											if(item.maxUses > 0) {//use-based item
												System.out.println(quantityString + " is not a QUANTITY-based item");
											} else {//quantity-based item
												String quantity = "";
												while(!quantity.matches("\\d+")) {
													System.out.println("Current QUANTITY of " + quantityString.toUpperCase() + "in the ENEMY: " + item.quantity);
													System.out.println("Enter new QUANTITY:");
													quantity = sanitize(sc.nextLine());
												}
												item.quantity = Integer.parseInt(quantity);
												System.out.println("QUANTITY of " + quantityString.toUpperCase() + " in the ENEMY set to " + quantity);
											}
										}
									} else {//invalid GameItem
										System.out.println("GameItem with name " + quantityString.toUpperCase() + " not found");
									}
								} else {
									System.out.println("Bad input: " + quantityString.toUpperCase());
								}
							}
							break;
						default:
							System.out.println("Bad input: " + contentString.toUpperCase());
							break;
						}
					}
					System.out.println("-- Leaving ENEMY ITEMs Mode --");
					break;
				case "a"://armorClass
					System.out.println("Current Armor Class:");
					System.out.println(enemy.armorClass);
					System.out.println("Enter new Armor Class:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int armorClass = Integer.parseInt(contentString);
						if(armorClass >= 0) {
							enemy.armorClass = armorClass;
							System.out.println("Armor Class set to " + armorClass);
						} else {
							System.out.println("Value not valid: " + armorClass);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "r"://damageReduction
					System.out.println("Current Damage Reduction:");
					System.out.println(enemy.damageReduction);
					System.out.println("Enter new Damage Reduction:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int damageReduction = Integer.parseInt(contentString);
						if(damageReduction >= 0) {
							enemy.damageReduction = damageReduction;
							System.out.println("Damage Reduction set to " + damageReduction);
						} else {
							System.out.println("Value not valid: " + damageReduction);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				default:
					System.out.println("Bad input: " + contentString.toUpperCase());
					break;
				}
			} else {
				System.out.println("Bad input: " + contentString.toUpperCase());
			}
		}
		if(enemy != null) {
			String confirmString = null;
			while(confirmString == null || (!confirmString.equals("y") && !confirmString.equals("n"))) {
				System.out.println("Save changes? Y/N");
				confirmString = sanitize(sc.nextLine());
				if(confirmString.equals("y")) {
					if(isEdit) {
						GameEnemy.enemies.replace(enemy.name, enemy);
						System.out.println("GameEnemy " + enemy.name.toUpperCase() + " saved");
					} else {
						if(GameEnemy.enemies != null) {
							if(GameEnemy.enemies.containsKey(enemy.name)) {
								System.out.println("GameEnemy " + enemy.name.toUpperCase() + " saved");
								GameEnemy.enemies.replace(enemy.name, enemy);
							} else {
								GameEnemy.add(enemy);
								System.out.println("GameEnemy " + enemy.name.toUpperCase() + " saved");
							}
						} else {
							GameEnemy.enemies = new HashMap<String,GameEnemy>();
							GameEnemy.add(enemy);
							System.out.println("GameEnemy " + enemy.name.toUpperCase() + " saved");
						}
					}
				} else if(confirmString.equals("n")) {
					System.out.println("Changes DISCARDED");
				}
			}
		}
	}

	/**
	 * itemMode(boolean) Allows the User to Create or Edit GameItems
	 * @param isEdit Indicates whether to EDIT existing content
	 */
	private static void itemMode(boolean isEdit) {
		if(isEdit) {
			System.out.println("-- ENTERING GameItem MODIFICATION MODE --");
		} else {
			System.out.println("-- ENTERING GameItem CREATION MODE --");
		}
		boolean inItemMode = true;
		String contentString;
		GameItem item = null;
		if(isEdit) {//EDIT ITEM mode
			if(GameItem.items == null || GameItem.items.isEmpty()) {
				inItemMode = false;
				System.out.println("No saved GameItems to EDIT");
			} else {
				boolean doLookup = true;
				while (doLookup == true && item == null) {
					System.out.println("Enter the name of the GameItem you wish to EDIT (or X to EXIT):");
					contentString = sanitize(sc.nextLine());
					if(contentString.equals("x")) {
						doLookup = false;
						inItemMode = false;
						System.out.println("-- EXITING GameItem MODIFICATION MODE --");
					} else if(contentString.matches("\\w+.*")) {
						String[] tokens = contentString.split("\\s+");
						item = GameItem.lookup(tokens[0]);
					}
					if(item == null) {
						System.out.println("GameItem " + contentString + " not found");
					}
				}
				System.out.println("Loading GameItem " + item.name.toUpperCase());
			}
		} else {//NEW ITEM mode
			item = new GameItem();
		}
		while (inItemMode) {
			System.out.println("Edit:\t[N]ame\t[W]eight\t[V]alue\t[Q]uantity\t[R]emaining [U]ses\t[M]ax [U]ses\t[O]wner\t[D]escription\t[DMG]\t[E]quip [S]lot"
					+ "\t[EQ]uipped\n\t[AC]\t[DR]\t[M]agic [R]esistance\t[HP]\t[SP]\tAttac[K] [B]onus (Melee)\tAttac[K] Bonus ([R]anged)\tAttac[K] Bonus ([M]agic)"
					+ "\n\t[BR]eakable\t[C]urrent [D]urability\t[M]ax [D]urability\t[A]ttack [T]ype\tMi[N] [R]ange\tMa[X] [R]ange\t[R]each [D]istance"
					+ "\t[D]amage [T]ype\te[X]it");
			contentString = sanitize(sc.nextLine());
			if(contentString.matches("\\w+")) {
				switch (contentString) {
				case "x"://EXIT
					if(isEdit) {
						inItemMode = false;
						System.out.println("-- EXITING GameItem MODIFICATION MODE --");
					} else {
						if(GameItem.items != null && GameItem.items.containsKey(item.name)) {
							System.out.println("GameItem " + item.name.toUpperCase() + " already exists. OVERWRITE existing? (Y/N)");
							String doOverwrite = sanitize(sc.nextLine());
							if(doOverwrite.equals("y")) {
								inItemMode = false;
							} else {
								System.out.println("Will not OVERWRITE. Change the NAME of this GameItem to SAVE.");
							}
						} else {
							System.out.println("-- EXITING GameItem CREATION MODE --");
							inItemMode = false;
						}
					}
					break;
				case "n"://name
					System.out.println("Current ITEM Name:");
					System.out.println(item.name);
					System.out.println("Enter new ITEM Name:");
					contentString = sc.nextLine();//preserve the Name exactly as entered
					if(isEdit) {
						System.out.println("Currently unable to EDIT the name of an ITEM after it is SAVEd");
					} else {
						item.name = contentString.toLowerCase();//TODO: manage multi-word names for ITEMs, etc.
						System.out.println("ITEM name set to " + contentString);
					}
					break;
				case "w"://weight
					System.out.println("Current Weight:");
					System.out.println(item.weight);
					System.out.println("Enter new Weight:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+(\\.\\d+)?")) {
						double weight = Double.parseDouble(contentString);
						if(weight >= 0) {
							item.weight = weight;
							System.out.println("Weight set to " + weight);
						} else {
							System.out.println("Value not valid: " + weight);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "v"://value
					System.out.println("Current Value:");
					System.out.println(item.value);
					System.out.println("Enter new Value:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+(\\.\\d+)?")) {
						double value = Double.parseDouble(contentString);
						if(value >= 0) {
							item.value = value;
							System.out.println("Value set to " + value);
						} else {
							System.out.println("Value not valid: " + value);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "q"://quantity
					System.out.println("Current Quantity:");
					System.out.println(item.quantity);
					System.out.println("Enter new Quantity:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int quantity = Integer.parseInt(contentString);
						if(quantity >= 0) {
							item.quantity = quantity;
							System.out.println("Quantity set to " + quantity);
						} else {
							System.out.println("Value not valid: " + quantity);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "ru"://remainingUses
					System.out.println("Current Remaining Uses:");
					System.out.println(item.remainingUses);
					System.out.println("Enter new Remaining Uses:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("(-)?\\d+")) {
						int remainingUses = Integer.parseInt(contentString);
						if(remainingUses > -2) {
							item.remainingUses = remainingUses;
							System.out.println("Remaining Uses set to " + remainingUses);
						} else {
							System.out.println("Value not valid: " + remainingUses);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "mu"://maxUses
					System.out.println("Current Max Uses:");
					System.out.println(item.maxUses);
					System.out.println("Enter new Max Uses:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("(-)?\\d+")) {
						int maxUses = Integer.parseInt(contentString);
						if(maxUses > -2) {
							item.maxUses = maxUses;
							System.out.println("Max Uses set to " + maxUses);
						} else {
							System.out.println("Value not valid: " + maxUses);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "o"://owner
					System.out.println("Current Owner:");
					System.out.println(item.owner);
					System.out.println("Enter new Owner:");
					contentString = sc.nextLine();//don't sanitize this String?
					if(contentString.matches("\\w+")) {
						item.owner = contentString;
						System.out.println("Owner set to " + contentString);
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "d"://description
					System.out.println("Current Description:");
					System.out.println(item.description);
					System.out.println("Enter new Description:");
					contentString = sc.nextLine();//preserve the Description exactly as entered
					item.description = contentString;
					System.out.println("Description set to \"" + contentString + "\"");
					break;
				case "dmg"://damageDealt
					System.out.println("Current Damage:");
					System.out.println(item.damageDealt);
					System.out.println("Enter new Damage:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int damageDealt = Integer.parseInt(contentString);
						if(damageDealt >= 0) {
							item.damageDealt = damageDealt;
							System.out.println("Damage set to " + damageDealt);
						} else {
							System.out.println("Value not valid: " + damageDealt);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "es"://equipSlot
					List<String> equipSlots = new ArrayList<String>();
					equipSlots.add(GameItem.AMULET);equipSlots.add(GameItem.BACK);equipSlots.add(GameItem.BELT);equipSlots.add(GameItem.BOOTS);
					equipSlots.add(GameItem.CLOAK);equipSlots.add(GameItem.CUIRASS);equipSlots.add(GameItem.EARRING);equipSlots.add(GameItem.GAUNTLETS);
					equipSlots.add(GameItem.GREAVES);equipSlots.add(GameItem.HELM);equipSlots.add(GameItem.MAIN_HAND);equipSlots.add(GameItem.OFF_HAND);
					equipSlots.add(GameItem.PANTS);equipSlots.add(GameItem.RING);equipSlots.add(GameItem.SHIRT);equipSlots.add(GameItem.SHOES);
					System.out.println("Current Equip Slot:");
					System.out.println(item.equipSlot);
					System.out.println("Enter new Equip Slot:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\w+")) {
						if(equipSlots.contains(contentString.toUpperCase())) {
							item.equipSlot = contentString.toUpperCase();
							System.out.println("EquipSlot set to " + contentString.toUpperCase());
						} else {
							System.out.println("Value not valid: " + contentString.toUpperCase());
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "eq"://isEquipped
					System.out.println("By default, all ITEMs are unequipped");
					break;
				case "ac"://armorClass
					System.out.println("Current Armor Class:");
					System.out.println(item.armorClass);
					System.out.println("Enter new Armor Class:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int armorClass = Integer.parseInt(contentString);
						if(armorClass >= 0) {
							item.armorClass = armorClass;
							System.out.println("Armor Class set to " + armorClass);
						} else {
							System.out.println("Value not valid: " + armorClass);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "dr"://damageReduction
					System.out.println("Current Damage Reduction:");
					System.out.println(item.damageReduction);
					System.out.println("Enter new Damage Reduction:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int damageReduction = Integer.parseInt(contentString);
						if(damageReduction >= 0) {
							item.damageReduction = damageReduction;
							System.out.println("Damage Reduction set to " + damageReduction);
						} else {
							System.out.println("Value not valid: " + damageReduction);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "mr"://magicResistance
					System.out.println("Current Magic Resistance:");
					System.out.println(item.magicResistance);
					System.out.println("Enter new Magic Resistance:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int magicResistance = Integer.parseInt(contentString);
						if(magicResistance >= 0) {
							item.magicResistance = magicResistance;
							System.out.println("Magic Resistance set to " + magicResistance);
						} else {
							System.out.println("Value not valid: " + magicResistance);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "hp"://healthPoints
					System.out.println("Current Health Points (Bonus):");
					System.out.println(item.healthPoints);
					System.out.println("Enter new Health Points (Bonus):");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int healthPoints = Integer.parseInt(contentString);
						if(healthPoints >= 0) {
							item.healthPoints = healthPoints;
							System.out.println("Health Points (Bonus) set to " + healthPoints);
						} else {
							System.out.println("Value not valid: " + healthPoints);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "sp"://spellPoints
					System.out.println("Current Spell Points (Bonus):");
					System.out.println(item.spellPoints);
					System.out.println("Enter new Spell Points (Bonus):");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int spellPoints = Integer.parseInt(contentString);
						if(spellPoints >= 0) {
							item.spellPoints = spellPoints;
							System.out.println("Spell Points (Bonus) set to " + spellPoints);
						} else {
							System.out.println("Value not valid: " + spellPoints);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "kb"://attackBonusMelee
					System.out.println("Current Melee Attack Bonus:");
					System.out.println(item.attackBonusMelee);
					System.out.println("Enter new Melee Attack Bonus:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int attackBonusMelee = Integer.parseInt(contentString);
						if(attackBonusMelee >= 0) {
							item.attackBonusMelee = attackBonusMelee;
							System.out.println("Melee Attack Bonus set to " + attackBonusMelee);
						} else {
							System.out.println("Value not valid: " + attackBonusMelee);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "kr"://attackBonusRanged
					System.out.println("Current Ranged Attack Bonus:");
					System.out.println(item.attackBonusRanged);
					System.out.println("Enter new Ranged Attack Bonus:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int attackBonusRanged = Integer.parseInt(contentString);
						if(attackBonusRanged >= 0) {
							item.attackBonusRanged = attackBonusRanged;
							System.out.println("Ranged Attack Bonus set to " + attackBonusRanged);
						} else {
							System.out.println("Value not valid: " + attackBonusRanged);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "km"://attackBonusMagic
					System.out.println("Current Magic Attack Bonus:");
					System.out.println(item.attackBonusMagic);
					System.out.println("Enter new Magic Attack Bonus:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int attackBonusMagic = Integer.parseInt(contentString);
						if(attackBonusMagic >= 0) {
							item.attackBonusMagic = attackBonusMagic;
							System.out.println("Magic Attack Bonus set to " + attackBonusMagic);
						} else {
							System.out.println("Value not valid: " + attackBonusMagic);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "br"://isBreakable
					System.out.println("Current Breakability:");
					System.out.println(item.isBreakable);
					System.out.println("Enter new Breakability:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("(t(rue)?|f(alse)?)")) {
						boolean isBreakable = Boolean.parseBoolean(contentString);
						item.isBreakable = isBreakable;
						System.out.println("Breakability set to " + isBreakable);
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "cd"://curDurability
					System.out.println("Current Durability:");
					System.out.println(item.curDurability);
					System.out.println("Enter new Durability:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int curDurability = Integer.parseInt(contentString);
						if(curDurability >= 0) {
							item.curDurability = curDurability;
							System.out.println("Durability set to " + curDurability);
						} else {
							System.out.println("Value not valid: " + curDurability);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "md"://maxDurability
					System.out.println("Max Durability:");
					System.out.println(item.maxDurability);
					System.out.println("Enter new Max Durability:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int maxDurability = Integer.parseInt(contentString);
						if(maxDurability >= 0) {
							item.maxDurability = maxDurability;
							System.out.println("Max Durability set to " + maxDurability);
						} else {
							System.out.println("Value not valid: " + maxDurability);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "at"://attackType
					System.out.println("Attack Type (0 = MELEE | 1 = RANGED | 2 = THROWN | 3 = MAGIC):");
					System.out.println(item.attackType);
					System.out.println("Enter new Attack Type (0 = MELEE | 1 = RANGED | 2 = THROWN | 3 = MAGIC):");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int attackType = Integer.parseInt(contentString);
						if(attackType >= 0 && attackType < 4) {
							item.attackType = attackType;
							System.out.println("Attack Type set to " + attackType);
						} else {
							System.out.println("Value not valid: " + attackType);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "nr"://minRange
					System.out.println("Min Range:");
					System.out.println(item.minRange);
					System.out.println("Enter new Min Range:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int minRange = Integer.parseInt(contentString);
						if(minRange >= 0) {
							item.minRange = minRange;
							System.out.println("Min Range set to " + minRange);
						} else {
							System.out.println("Value not valid: " + minRange);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "xr"://maxRange
					System.out.println("Max Range:");
					System.out.println(item.maxRange);
					System.out.println("Enter new Max Range:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int maxRange = Integer.parseInt(contentString);
						if(maxRange >= 0) {
							item.maxRange = maxRange;
							System.out.println("Max Range set to " + maxRange);
						} else {
							System.out.println("Value not valid: " + maxRange);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "rd"://reachDistance
					System.out.println("Reach Distance:");
					System.out.println(item.reachDistance);
					System.out.println("Enter new Reach Distance:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int reachDistance = Integer.parseInt(contentString);
						if(reachDistance >= 0) {
							item.reachDistance = reachDistance;
							System.out.println("Reach Distance set to " + reachDistance);
						} else {
							System.out.println("Value not valid: " + reachDistance);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					 break;
				case "dt"://damageType
					System.out.println("Damage Type (0 = BLUDGEON | 1 = SLASH | 2 = PIERCE | 3 = MAGIC):");
					System.out.println(item.damageType);
					System.out.println("Enter new Damage Type (0 = BLUDGEON | 1 = SLASH | 2 = PIERCE | 3 = MAGIC):");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int damageType = Integer.parseInt(contentString);
						if(damageType >= 0 && damageType < 4) {
							item.damageType = damageType;
							System.out.println("Damage Type set to " + damageType);
						} else {
							System.out.println("Value not valid: " + damageType);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				default:
					System.out.println("Bad input: " + contentString.toUpperCase());
					break;
				}
			} else {
				System.out.println("Bad input: " + contentString.toUpperCase());
			}
		}
		if(item != null) {
			String confirmString = null;
			while(confirmString == null || (!confirmString.equals("y") && !confirmString.equals("n"))) {
				System.out.println("Save changes? Y/N");
				confirmString = sanitize(sc.nextLine());
				if(confirmString.equals("y")) {
					if(isEdit) {
						GameItem.items.replace(item.name, item);
						System.out.println("GameItem " + item.name.toUpperCase() + " saved");
					} else {
						if(GameItem.items != null) {
							if(GameItem.items.containsKey(item.name)) {
								System.out.println("GameItem " + item.name.toUpperCase() + " saved");
								GameItem.items.replace(item.name, item);
							} else {
								GameItem.add(item);
								System.out.println("GameItem " + item.name.toUpperCase() + " saved");
							}
						} else {
							GameItem.items = new HashMap<String,GameItem>();
							GameItem.add(item);
							System.out.println("GameItem " + item.name.toUpperCase() + " saved");
						}
					}
				} else if(confirmString.equals("n")) {
					System.out.println("Changes DISCARDED");
				}
			}
		}
	}

	/**
	 * spellMode(boolean) Allows the User to Create or Edit GameSpells
	 * @param isEdit Indicates whether to EDIT existing content
	 */
	private static void spellMode(boolean isEdit) {
		if(isEdit) {
			System.out.println("-- ENTERING GameSpell MODIFICATION MODE --");
		} else {
			System.out.println("-- ENTERING GameSpell CREATION MODE --");
		}
		boolean inSpellMode = true;
		String contentString;
		GameSpell spell = null;
		if(isEdit) {//EDIT SPELL mode
			if(GameSpell.spells == null || GameSpell.spells.isEmpty()) {
				inSpellMode = false;
				System.out.println("No saved GameSpells to EDIT");
			} else {
				boolean doLookup = true;
				while (doLookup == true && spell == null) {
					System.out.println("Enter the name of the GameSpell you wish to EDIT (or X to EXIT):");
					contentString = sanitize(sc.nextLine());
					if(contentString.equals("x")) {
						doLookup = false;
						inSpellMode = false;
						System.out.println("-- EXITING GameSpell MODIFICATION MODE --");
					} else if(contentString.matches("\\w+.*")) {
						String[] tokens = contentString.split("\\s+");
						spell = GameSpell.lookup(tokens[0]);
					}
					if(spell == null) {
						System.out.println("GameSpell" + contentString + " not found");
					}
				}
				System.out.println("Loading GameSpell" + spell.name.toUpperCase());
			}
		} else {//NEW SPELL mode
			spell = new GameSpell();
		}
		while (inSpellMode) {
			System.out.println("Edit:\t[N]ame\t[L]evel\t[C]ost\td[E]scription\t[D]ie\t[Q]uantity\t[B]onus\t[T]ype\te[X]it");
			contentString = sanitize(sc.nextLine());
			if(contentString.matches("\\w")) {
				switch (contentString) {
				case "x"://EXIT
					if(isEdit) {
						inSpellMode = false;
						System.out.println("-- EXITING GameSpell MODIFICATION MODE --");
					} else {
						if(GameSpell.spells != null && GameSpell.spells.containsKey(spell.name)) {
							System.out.println("GameSpell " + spell.name.toUpperCase() + " already exists. OVERWRITE existing? (Y/N)");
							String doOverwrite = sanitize(sc.nextLine());
							if(doOverwrite.equals("y")) {
								inSpellMode = false;
							} else {
								System.out.println("Will not OVERWRITE. Change the NAME of this GameSpell to SAVE.");
							}
						} else {
							System.out.println("-- EXITING GameSpell CREATION MODE --");
							inSpellMode = false;
						}
					}
					break;
				case "n"://name
					System.out.println("Current SPELL Name:");
					System.out.println(spell.name);
					System.out.println("Enter new SPELL Name:");
					contentString = sc.nextLine();//preserve the Name exactly as entered
					if(isEdit) {
						System.out.println("Currently unable to EDIT the name of a SPELL after it is SAVEd");
					} else {
						spell.name = contentString.toLowerCase();
					}
					break;
				case "l"://spellLevel
					System.out.println("Current SPELL Level:");
					System.out.println(spell.spellLevel);
					System.out.println("Enter new SPELL Level:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int spellLevel = Integer.parseInt(contentString);
						if(spellLevel > 0 && spellLevel < 10) {
							spell.spellLevel = spellLevel;
							System.out.println("SPELL" + spell.name + " level set to " + spellLevel);
						} else {
							System.out.println("Spell Level out of bounds: " + spellLevel);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "c"://spellPointCost
					System.out.println("Current SP Cost:");
					System.out.println(spell.spellPointCost);
					System.out.println("Enter new SP Cost:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int spellPointCost = Integer.parseInt(contentString);
						if(spellPointCost >= 0 && spellPointCost <= 10) {
							spell.spellPointCost = spellPointCost;
							System.out.println("SPELL" + spell.name + " SP Cost set to " + spellPointCost);
						} else {
							System.out.println("SP Cost out of bounds: " + spellPointCost);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "e"://description
					System.out.println("Current SPELL Description:");
					System.out.println(spell.description);
					System.out.println("Enter new SPELL Description:");
					contentString = sc.nextLine();//preserve the Description exactly as entered
					spell.description = contentString;
					break;
				case "d"://damageDie
					List<Integer> diceValues = new ArrayList<Integer>();
					diceValues.add(2);diceValues.add(3);diceValues.add(4);diceValues.add(6);
					diceValues.add(8);diceValues.add(10);diceValues.add(12);diceValues.add(20);
					System.out.println("Current Damage Die:");
					System.out.println(spell.damageDie);
					System.out.println("Enter new Damage Die:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int damageDie = Integer.parseInt(contentString);
						if(diceValues.contains(damageDie)) {
							spell.damageDie = damageDie;
							System.out.println("SPELL" + spell.name + " Damage Die set to " + damageDie);
						} else {
							System.out.println("Not a valid Damage Die: " + damageDie);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "q"://numDie
					System.out.println("Current Number of Damage Dice to roll:");
					System.out.println(spell.numDie);
					System.out.println("Enter new Number of Damage Dice to roll:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int numDie = Integer.parseInt(contentString);
						if(numDie > 0) {
							spell.numDie = numDie;
							System.out.println("Number of Damage Die to roll set to " + numDie);
						} else {
							System.out.println("Not a valid number of Dice: " + numDie);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "b"://bonusDamage
					System.out.println("Current Bonus Damage amount:");
					System.out.println(spell.numDie);
					System.out.println("Enter new Bonus Damage amount:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\d+")) {
						int bonusDamage = Integer.parseInt(contentString);
						if(bonusDamage > 0) {
							spell.bonusDamage = bonusDamage;
							System.out.println("Bonus Damage set to " + bonusDamage);
						} else {
							System.out.println("Bonus Damage value not valid: " + bonusDamage);
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				case "t"://damageType
					List<String> damageTypes = new ArrayList<String>();
					damageTypes.add(GameSpell.damage_COLD);damageTypes.add(GameSpell.damage_CRUSHING);damageTypes.add(GameSpell.damage_ELECTRIC);
					damageTypes.add(GameSpell.damage_FIRE);damageTypes.add(GameSpell.damage_FORCE);damageTypes.add(GameSpell.damage_HEALING);
					damageTypes.add(GameSpell.damage_PSYCHIC);
					System.out.println("Current Damage Type:");
					System.out.println(spell.damageType);
					System.out.println("Enter new Damage Type:");
					contentString = sanitize(sc.nextLine());
					if(contentString.matches("\\w+")) {
						if(damageTypes.contains(contentString.toUpperCase())) {
							spell.damageType = contentString.toUpperCase();
							System.out.println("Damage Type set to " + contentString.toUpperCase());
						} else {
							System.out.println("Damage Type not valid: " + contentString.toUpperCase());
						}
					} else {
						System.out.println("Bad input: " + contentString.toUpperCase());
					}
					break;
				default:
					System.out.println("Bad input: " + contentString.toUpperCase());
					break;
				}
			} else {
				System.out.println("Bad input: " + contentString.toUpperCase());
			}
		}
		if(spell != null) {
			String confirmString = null;
			while(confirmString == null || (!confirmString.equals("y") && !confirmString.equals("n"))) {
				System.out.println("Save changes? Y/N");
				confirmString = sanitize(sc.nextLine());
				if(confirmString.equals("y")) {
					if(isEdit) {
						GameSpell.spells.replace(spell.name, spell);
						System.out.println("GameSpell " + spell.name.toUpperCase() + " saved");
					} else {
						if(GameSpell.spells != null) {
							if(GameSpell.spells.containsKey(spell.name)) {
								System.out.println("GameSpell " + spell.name.toUpperCase() + " saved");
								GameSpell.spells.replace(spell.name, spell);
							} else {
								GameSpell.add(spell);
								System.out.println("GameSpell " + spell.name.toUpperCase() + " saved");
							}
						} else {
							GameSpell.spells = new HashMap<String,GameSpell>();
							GameSpell.add(spell);
							System.out.println("GameSpell " + spell.name.toUpperCase() + " saved");
						}
					}
				} else if(confirmString.equals("n")) {
					System.out.println("Changes DISCARDED");
				}
			}
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
	private static List<GameVolume> makeRoomVolume(int length, int width, int height) {
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
					interior.add(new GameVolume(x,y,z,true,passableDirs,false,(byte)0b00_00,null,null,null));
				}
			}
		}
		return interior;
	}

	/**
	 * intro() Displays an Intro screen for the User
	 * @return String containing the contents of the Intro screen
	 */
	private static String intro() {
		String introString = "AaaaAAaaaABbbbBBbbbBCcccCCcccCDdddDDdddDEeeeEEeeeEFfffFFfffFGgggGGgggGHhhhHHhhhH\n";//80 character width for DOS-esque console look
		introString = "-- ABOUT -- || -- TEXT ADVENTURE GAME -- || ";
		introString += about();
		introString += "\nType ? for HELP";
		return introString;
	}

	/**
	 * about() Returns game or utility specific information
	 * @return Game-specific information
	 */
	private static String about() {
		String aboutString = "";
		aboutString += "-- ABOUT --\n";
		aboutString += "This Game (currently unnamed) is being developed by\n";
		aboutString += "JOSEPH DELONG (joseph.arthur.delong@gmail.com)\n";
		aboutString += "Current Version: 0.0.0.1";
		return aboutString;
	}

	/**
	 * attack(String) Attempts to attack the indicated target
	 * @param object The target of the attack
	 * @return The outcome of the attack attempt
	 */
	private static String attack(String object) {
		String attackString = "";
		if(gameState.inInventory) {//if Player is looking at their inventory
			if(gameState.inventory.containsKey(object)) {//if the inventory contains the object specified
				attackString = "You should treat your things with more respect! CLOSE your INVENTORY to ATTACK a valid target.";
			}
		} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(object)) {//if the object specified is a GameEnemy
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsEnemy(object)) {//if the current Room contains the Enemy
				int attackRoll = rollDie(20,gameState.attackBonusMelee,true);
				GameRoom room = gameState.visitedRooms.get(gameState.currentRoomId);
				GameVolume thisVolume = room.interior.get(gameState.roomVolumeIndex);//get the GameVolume the player is in
				List<GameVolume> volumes = room.getAllVolumesContainingEnemy(object);
				Map<String,GameEnemy> foundEnemies = new HashMap<String,GameEnemy>();//set up temporary storage for all Enemies that match attack criteria
				for(GameVolume v: volumes) {//for each GameVolume that has at least 1 of the specified GameEnemy in it
					for(int i = 0; i < v.enemies.get(object).size(); i++) {//for each of the specified GameEnemy inside the GameVolume
						GameEnemy e = v.enemies.get(object).get(i);//get the individual GameEnemy and...
						foundEnemies.put("" + ((v.zPos - 1) * room.width * room.length) + ((v.yPos - 1) * room.length) + (v.xPos - 1) + "-" + i, e);//map it to the GameVolume (index) & the index in the List where it was found
					}
				}
				//Ask which Enemy to target, listing distance and direction from player
				GameEnemy targetEnemy = null;
				int volumeIndex = -1;
				int enemyIndex = -1;
				GameVolume targetVolume = null;
				if(foundEnemies.size() > 1) {//more than 1 GameEnemy of the specified type in the GameRoom
					boolean gotEnemy = false;
					List<String> mapKeys = new ArrayList<String>();
					mapKeys.addAll(foundEnemies.keySet());
					while(!gotEnemy) {//while no individual GameEnemy is specified as TARGET
						System.out.println("Choose which ENEMY, by it's ID, you want to ATTACK (or X to EXIT):");//ask which Enemy to target
						System.out.println(" ID |Enemy Details");
						for(int i = 0; i < mapKeys.size(); i++) {//for each mapping...
							String s = mapKeys.get(i);
							int vIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameEnemy
							GameVolume v = room.interior.get(vIndex);//get the GameVolume the indicated GameEnemy is in
							double distance = GameState.distanceBetween(thisVolume,v);//calculate how far from the player the specified GameEnemy is
							byte direction = GameState.getGeneralTraversalDirection(thisVolume,v);//get the general direction in which the GameEnemy is, in relation to the player
							GameEnemy e = foundEnemies.get(mapKeys.get(i));//get the GameEnemy specified
							System.out.println(String.format("%-4d%s%s%s%d%s%d%s%.2f%s%s",i,": ",e.name.toUpperCase(),", ",e.currentHp,"/",e.maxHp,"HP - ",distance," meters, ",GameState.getDirectionName(direction)));
						}
						String input = sanitize(sc.nextLine());
						if(input.equals("x")) {
							attackString = "ATTACK cancelled.";
							return attackString;
						} else if(input.matches("//d")) {
							int index = Integer.parseInt(input);
							List<GameEnemy> list = new ArrayList<GameEnemy>();
							list.addAll(foundEnemies.values());
							if(index > 0 && index <= list.size()) {
								targetEnemy = list.get(index - 1);
								String s = mapKeys.get(index - 1);
								volumeIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameEnemy
								targetVolume = room.interior.get(volumeIndex);
								enemyIndex = Integer.parseInt(s.substring(s.indexOf('-') - 1));//get the List index corresponding to the indicated GameEnemy
								gotEnemy = true;//TODO: add check for DISTANCE
							} else {
								System.out.println("Invalid index: " + input);
							}
						} else {
							System.out.println("Invalid index: " + input);
						}
					}
				} else {//only 1 GameEnemy in the GameRoom of specified type
					targetEnemy = foundEnemies.values().iterator().next();//get first (and only) enemy in List
				}
				if(GameState.equipSlot_MAIN_HAND != null) {//if the Player has an ITEM equipped
					if(attackRoll == -1) {//CRIT_FAIL
						attackString = "You swing wildly at " + object.toUpperCase() + ", but manage to DROP your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase() + " on the ground.";//TODO: change to GameItem.onFail
						//System.out.println(drop(GameState.equipSlot_MAIN_HAND.name));//drop item without the extra text notification
						GameItem droppedItem = GameState.equipSlot_MAIN_HAND;
						droppedItem.isEquipped = false;
						thisVolume.addItem(droppedItem);
						room.interior.set(gameState.roomVolumeIndex,thisVolume);
						GameState.equipSlot_MAIN_HAND = null;
						gameState.currentWeight -= droppedItem.getTotalWeight();
						gameState.visitedRooms.replace(gameState.currentRoomId,room);
					} else if(attackRoll == 999) {//CRIT_SUCCESS
						targetEnemy.currentHp -= ((GameState.equipSlot_MAIN_HAND.damageDealt * 2) - targetEnemy.damageReduction);
						attackString = "You bring down your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase() + " on " + object.toUpperCase() + " with a mighty blow.";//TODO: change to GameItem.onCrit
						if(targetEnemy.currentHp <= 0) {//enemy is slain
							//TODO: implement degrees of dead-ness maybe?
							attackString += "\n" + object.toUpperCase() + " falls down dead.";
							gameState.currentXp += targetEnemy.xpValue;
							if(targetEnemy.inventory != null && !targetEnemy.inventory.isEmpty()) {
								attackString += " It dropped:";
								for(GameItem enemyItem: targetEnemy.inventory) {
									attackString += "\n\t" + enemyItem.name.toUpperCase();
									targetVolume.addItem(enemyItem);
								}
							}
							List<GameEnemy> volumeEnemies = targetVolume.enemies.get(object);
							volumeEnemies.remove(enemyIndex);
							if(volumeEnemies.isEmpty()) {
								targetVolume.enemies.remove(object);
							} else {
								targetVolume.enemies.put(object,volumeEnemies);
							}
							//TODO: add CORPSE of GameEnemy as a GameObject? Maybe...
						} else {
							List<GameEnemy> volumeEnemies = targetVolume.enemies.get(object);
							volumeEnemies.set(enemyIndex,targetEnemy);
							targetVolume.enemies.replace(object,volumeEnemies);
						}
						room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1),targetVolume);
						gameState.visitedRooms.replace(gameState.currentRoomId,room);
					} else if(attackRoll > targetEnemy.armorClass) {//the attack hits
						targetEnemy.currentHp -= ((GameState.equipSlot_MAIN_HAND.damageDealt * 2) - targetEnemy.damageReduction);
						attackString = "You strike " + object.toUpperCase() + " with a well-placed blow of your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase() + ".";
						if(targetEnemy.currentHp <= 0) {//enemy is slain
							//TODO: implement degrees of dead-ness maybe
							attackString += "\n" + object.toUpperCase() + " falls down dead.";
							gameState.currentXp += targetEnemy.xpValue;
							if(targetEnemy.inventory != null && !targetEnemy.inventory.isEmpty()) {
								attackString += " It dropped:";
								for(GameItem enemyItem: targetEnemy.inventory) {
									attackString += "\n\t" + enemyItem.name.toUpperCase();
									targetVolume.addItem(enemyItem);
								}
							}
							List<GameEnemy> volumeEnemies = targetVolume.enemies.get(object);
							volumeEnemies.remove(enemyIndex);
							if(volumeEnemies.isEmpty()) {
								targetVolume.enemies.remove(object);
							} else {
								targetVolume.enemies.put(object,volumeEnemies);
							}
							//TODO: add CORPSE of GameEnemy as a GameObject? Maybe...
						} else {
							List<GameEnemy> volumeEnemies = targetVolume.enemies.get(object);
							volumeEnemies.set(enemyIndex,targetEnemy);
							targetVolume.enemies.replace(object,volumeEnemies);
						}
						room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1),targetVolume);
						gameState.visitedRooms.replace(gameState.currentRoomId,room);
					} else {//the attack does not hit
						attackString = "You swing your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase() + " at " + object.toUpperCase();
						int missInterval = (targetEnemy.armorClass - attackRoll) % targetEnemy.armorClass;
						if(missInterval <= targetEnemy.armorClass * 0.25) {//not a terrible miss
							attackString += ", but " + object.toUpperCase() + " sidesteps the blow.";
						} else if(missInterval <= targetEnemy.armorClass * 0.5) {//moderate miss
							attackString += ", but the blow glances off harmlessly.";
						} else if(missInterval <= targetEnemy.armorClass * 0.75) {//pretty bad miss
							attackString += ", but they are just out of reach.";
						} else {//incredibly bad miss
							attackString += ", but you hit only air.";
						}
					}
				} else {//Player has no ITEM equipped
					if(attackRoll == -1) {//CRIT_FAIL
						attackString = "Your swing wildly with your FIST at " + object.toUpperCase() + ", but manage only an awkward slap.";
					} else if(attackRoll == 999) {//CRIT_SUCCESS
						attackString = "You bring down your FIST on " + object.toUpperCase() + " with a mighty blow, but do no damage.";
					} else if(attackRoll > targetEnemy.armorClass) {//the attack hits
						attackString = "You strike " + object.toUpperCase() + " with a well-placed punch, but do no damage.";
					} else {//the attack does not hit
						attackString = "You swing your FIST at " + object.toUpperCase();
						int missInterval = (targetEnemy.armorClass - attackRoll) % targetEnemy.armorClass;
						if(missInterval <= targetEnemy.armorClass * 0.25) {//not a terrible miss
							attackString += ", but " + object.toUpperCase() + " sidesteps the blow.";
						} else if(missInterval <= targetEnemy.armorClass * 0.5) {//moderate miss
							attackString += ", but the blow glances off harmlessly.";
						} else if(missInterval <= targetEnemy.armorClass * 0.75) {//pretty bad miss
							attackString += ", but they are just out of reach.";
						} else {//incredibly bad miss
							attackString += ", but you hit only air.";
						}
					}
				}
			} else {//Enemy is not in the Room
				attackString = "You don't see any " + object.toUpperCase() + " to ATTACK.";
			}
		} else if(GameObject.objects != null && GameObject.objects.containsKey(object)) {//if the object is a GameObject
			if(GameRoom.lookup(gameState.currentRoomId).containsObject(object)) {//if the current Room contains the Object
				//no need to roll for attack on an Object
				GameRoom room = gameState.visitedRooms.get(gameState.currentRoomId);
				GameVolume thisVolume = room.interior.get(gameState.roomVolumeIndex);//get the GameVolume the player is in
				List<GameVolume> volumes = room.getAllVolumesContainingEnemy(object);
				Map<String,GameObject> foundObjects = new HashMap<String,GameObject>();//set up temporary storage for all Objects that match attack criteria
				for(GameVolume v: volumes) {//for each GameVolume that has at least 1 of the specified GameObject in it
					for(int i = 0; i < v.objects.get(object).size(); i++) {//for each of the specified GameObject inside the GameVolume
						GameObject o = v.objects.get(object).get(i);//get the individual GameEnemy and...
						foundObjects.put("" + ((v.zPos - 1) * room.width * room.length) + ((v.yPos - 1) * room.length) + (v.xPos - 1) + "-" + i, o);//map it to the GameVolume (index) & the index in the List where it was found
					}
				}
				//Ask which Object to target, listing distance and direction from player
				GameObject targetObject = null;
				int volumeIndex = -1;
				int objectIndex = -1;
				GameVolume targetVolume = null;
				if(foundObjects.size() > 1) {//more than 1 GameObject of the specified type in the GameRoom
					boolean gotObject = false;
					List<String> mapKeys = new ArrayList<String>();
					mapKeys.addAll(foundObjects.keySet());
					while(!gotObject) {//while no individual GameObject is specified as TARGET
						System.out.println("Choose which OBJECT, by it's ID, you want to ATTACK (or X to EXIT):");//ask which Object to target
						System.out.println(" ID |Object Details");
						for(int i = 0; i < mapKeys.size(); i++) {//for each mapping...
							String s = mapKeys.get(i);
							int vIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameObject
							GameVolume v = room.interior.get(vIndex);//get the GameVolume the indicated GameObject is in
							double distance = GameState.distanceBetween(thisVolume,v);//calculate how far from the player the specified GameObject is
							byte direction = GameState.getGeneralTraversalDirection(thisVolume,v);//get the general direction in which the GameObject is, in relation to the player
							GameObject o = foundObjects.get(mapKeys.get(i));//get the GameObject specified
							System.out.println(String.format("%-4d%s%s%s%d%s%d%s%.2f%s%s",i,": ",o.name.toUpperCase(),", ",o.currentHp,"/",o.maxHp,"HP - ",distance," meters, ",GameState.getDirectionName(direction)));
						}
						String input = sanitize(sc.nextLine());
						if(input.equals("x")) {
							attackString = "ATTACK cancelled.";
							return attackString;
						} else if(input.matches("//d")) {
							int index = Integer.parseInt(input);
							List<GameObject> list = new ArrayList<GameObject>();
							list.addAll(foundObjects.values());
							if(index > 0 && index <= list.size()) {
								targetObject = list.get(index - 1);
								String s = mapKeys.get(index - 1);
								volumeIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameObject
								targetVolume = room.interior.get(volumeIndex);
								objectIndex = Integer.parseInt(s.substring(s.indexOf('-') - 1));//get the List index corresponding to the indicated GameObject
								gotObject = true;
							} else {
								System.out.println("Invalid index: " + input);
							}
						} else {
							System.out.println("Invalid index: " + input);
						}
					}
				} else {//only 1 GameObject in the GameRoom of specified type
					targetObject = foundObjects.values().iterator().next();//get first (and only) object in List
				}
				if(targetObject.isBreakable) {//object can be broken
					if(GameState.equipSlot_MAIN_HAND.damageDealt >= targetObject.currentHp) {//object is broken
						attackString = "You smash " + object.toUpperCase() + " to pieces with your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase() + ".";
//						targetObject.breakObject();//THIS DOES NOTHING RIGHT NOW
						if(targetObject.contents != null && !targetObject.contents.isEmpty()) {
							attackString += " It contained:";
							for(GameItem objectItem: targetObject.contents) {
								attackString += "\n\t" + objectItem.name.toUpperCase();
								targetVolume.addItem(objectItem);
							}
						}
						List<GameObject> volumeObjects = targetVolume.objects.get(object);
						volumeObjects.remove(objectIndex);
						if(volumeObjects.isEmpty()) {
							targetVolume.objects.remove(object);
						} else {
							targetVolume.objects.put(object,volumeObjects);
						}
					} else {//object is damaged, but not broken
						attackString = "You hit " + object.toUpperCase() + " with your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase();
						targetObject.currentHp -= GameState.equipSlot_MAIN_HAND.damageDealt;
						List<GameObject> volumeObjects = targetVolume.objects.get(object);
						volumeObjects.set(objectIndex,targetObject);
						targetVolume.objects.replace(object,volumeObjects);
					}
					//re-save object to the ROOM
					room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1),targetVolume);
					gameState.visitedRooms.replace(gameState.currentRoomId,room);
				} else {//object not breakable
					attackString = "You hit " + object.toUpperCase() + " with your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase() + ", but it makes no visible mark.";
				}
			} else {//the Object is not in the Room
				attackString = "You don't see any " + object.toUpperCase() + " to ATTACK.";
			}
		} else if(GameItem.items != null && GameItem.items.containsKey(object)) {//if the object specified is a GameItem
			if(GameRoom.lookup(gameState.currentRoomId).containsItem(object)) {//if the current Room contains the Item
				//no need to roll for attack on an Item
				GameRoom room = gameState.visitedRooms.get(gameState.currentRoomId);
				GameVolume thisVolume = room.interior.get(gameState.roomVolumeIndex);//get the GameVolume the player is in
				List<GameVolume> volumes = room.getAllVolumesContainingEnemy(object);
				Map<String,GameItem> foundItems = new HashMap<String,GameItem>();//set up temporary storage for all Items that match attack criteria
				for(GameVolume v: volumes) {//for each GameVolume that has at least 1 of the specified GameItem in it
					for(int i = 0; i < v.items.get(object).size(); i++) {//for each of the specified GameItem inside the GameVolume
						GameItem it = v.items.get(object).get(i);//get the individual GameItem and...
						foundItems.put("" + ((v.zPos - 1) * room.width * room.length) + ((v.yPos - 1) * room.length) + (v.xPos - 1) + "-" + i, it);//map it to the GameVolume (index) & the index in the List where it was found
					}
				}
				//Ask which Item to target, listing distance and direction from player
				GameItem targetItem = null;
				int volumeIndex = -1;
				int itemIndex = -1;
				GameVolume targetVolume = null;
				if(foundItems.size() > 1) {//more than 1 GameItem of the specified type in the GameRoom
					boolean gotItem = false;
					List<String> mapKeys = new ArrayList<String>();
					mapKeys.addAll(foundItems.keySet());
					while(!gotItem) {//while no individual GameItem is specified as TARGET
						System.out.println("Choose which ITEM, by it's ID, you want to ATTACK (or X to EXIT):");//ask which Item to target
						System.out.println(" ID |Item Details");
						for(int i = 0; i < mapKeys.size(); i++) {//for each mapping...
							String s = mapKeys.get(i);
							int vIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameItem
							GameVolume v = room.interior.get(vIndex);//get the GameVolume the indicated GameItem is in
							double distance = GameState.distanceBetween(thisVolume,v);//calculate how far from the player the specified GameItem is
							byte direction = GameState.getGeneralTraversalDirection(thisVolume,v);//get the general direction in which the GameItem is, in relation to the player
							GameItem it = foundItems.get(mapKeys.get(i));//get the GameEnemy specified
							System.out.println(String.format("%-4d%s%s%s%d%s%d%s%.2f%s%s",i,": ",it.name.toUpperCase(),", ",it.curDurability,"/",it.maxDurability,"HP - ",distance," meters, ",GameState.getDirectionName(direction)));
						}
						String input = sanitize(sc.nextLine());
						if(input.equals("x")) {
							attackString = "ATTACK cancelled.";
							return attackString;
						} else if(input.matches("//d")) {
							int index = Integer.parseInt(input);
							List<GameItem> list = new ArrayList<GameItem>();
							list.addAll(foundItems.values());
							if(index > 0 && index <= list.size()) {
								targetItem = list.get(index - 1);
								String s = mapKeys.get(index - 1);
								volumeIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameItem
								targetVolume = room.interior.get(volumeIndex);
								itemIndex = Integer.parseInt(s.substring(s.indexOf('-') - 1));//get the List index corresponding to the indicated GameItem
								gotItem = true;
							} else {
								System.out.println("Invalid index: " + input);
							}
						} else {
							System.out.println("Invalid index: " + input);
						}
					}
				} else {//only 1 GameItem in the GameRoom of specified type
					targetItem = foundItems.values().iterator().next();//get first (and only) item in List
				}
				if(GameState.equipSlot_MAIN_HAND.isBreakable) {//Item can be broken
					int damageDealt = GameState.equipSlot_MAIN_HAND.damageDealt - targetItem.damageReduction;
					if(targetItem.curDurability <= damageDealt) {//break it!
						attackString = "You smash the " + object.toUpperCase() + " with your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase() + ".";
						targetItem.curDurability -= damageDealt;
//						attackItem.breakItem();//TODO: determine if this method is worthwhile. Introduce a BROKEN version of the Item?
						List<GameItem> volumeItems = targetVolume.items.get(object);
						volumeItems.remove(itemIndex);
						if(volumeItems.isEmpty()) {
							targetVolume.items.remove(object);
						} else {
							targetVolume.items.put(object,volumeItems);
						}
					} else {//just damage it
						targetItem.curDurability -= damageDealt;
						attackString = "You hit the " + object.toUpperCase() + " with your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase() + ", damaging it.";
						List<GameItem> volumeItems = targetVolume.items.get(object);
						volumeItems.set(itemIndex,targetItem);
						targetVolume.items.replace(object,volumeItems);
					}
					//re-save the Item to the room
					room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1),targetVolume);
					gameState.visitedRooms.replace(gameState.currentRoomId, room);
				} else {//Item can't be broken
					attackString = "You hit the " + object.toUpperCase() + " with your " + GameState.equipSlot_MAIN_HAND.name.toUpperCase() + ", but cause no damage.";
				}
			} else {//the Item is not in the Room
				attackString = "You don't see any " + object.toUpperCase() + " to ATTACK.";
			}
		} else if(GameSpell.spells != null && GameSpell.spells.containsKey(object)) {//if the object is a GameSpell
			if(gameState.spellbook.containsKey(object)) {
				attackString = "Try to CAST " + object.toUpperCase() + " instead of ATTACKing it.";
			} else {
				attackString = "You can't figure out how to ATTACK a " + object.toUpperCase() + ".";
			}
		} else {//object not found in Game
			attackString = object.toUpperCase() + "? Never heard of it.";
		}
		return attackString;
	}

	/**
	 * attack(String,String) Attempt to ATTACK the specified TARGET with the specified implement
	 * @param target The name of the target of the attack attempt
	 * @param implement The name of the item used to attack the target
	 * @return The result of the attack attempt
	 */
	private static String attack(String target, String implement) {
		String attackString = "ATTACKing a specific target with a specific implement is not yet implemented.";
		//TODO: implement this method
		return attackString;
	}

	/**
	 * castSpell(String,String) Attempts to CAST the specified Spell on the specified Target
	 * @param spellName The name of the Spell to cast
	 * @param target The target of the Spell
	 * @return The narration of the Spellcasting attempt
	 */
	private static String castSpell(String spellName, String target) {
		String castString = "";
		if(gameState.inInventory) {//if Player is looking at their inventory
			castString = "You can't CAST any SPELL while rummaging through your Inventory.";
			//TODO: implement augmentation Spells? Item enchantments?
		} else {//not looking at inventory
			if(GameSpell.spells != null && GameSpell.spells.containsKey(spellName)) {//if a valid GameSpell
				if(gameState.spellbook.containsKey(spellName)) {//if Player knows the Spell
					if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(target)) {//if TARGET is a valid GameEnemy
						if(gameState.visitedRooms.get(gameState.currentRoomId).containsEnemy(target)) {//if the ENEMY is in the ROOM
							GameRoom room = gameState.visitedRooms.get(gameState.currentRoomId);
							GameVolume thisVolume = room.interior.get(gameState.roomVolumeIndex);//get the GameVolume the player is in
							List<GameVolume> volumes = room.getAllVolumesContainingEnemy(target);
							Map<String,GameEnemy> foundEnemies = new HashMap<String,GameEnemy>();//set up temporary storage for all Enemies that match attack criteria
							for(GameVolume v: volumes) {//for each GameVolume that has at least 1 of the specified GameEnemy in it
								for(int i = 0; i < v.enemies.get(target).size(); i++) {//for each of the specified GameEnemy inside the GameVolume
									GameEnemy e = v.enemies.get(target).get(i);//get the individual GameEnemy and...
									foundEnemies.put("" + ((v.zPos - 1) * room.width * room.length) + ((v.yPos - 1) * room.length) + (v.xPos - 1) + "-" + i, e);//map it to the GameVolume (index) & the index in the List where it was found
								}
							}
							//Ask which Enemy to target, listing distance and direction from player
							GameEnemy targetEnemy = null;
							int volumeIndex = -1;
							int enemyIndex = -1;
							GameVolume targetVolume = null;
							if(foundEnemies.size() > 1) {//more than 1 GameEnemy of the specified type in the GameRoom
								boolean gotEnemy = false;
								List<String> mapKeys = new ArrayList<String>();
								mapKeys.addAll(foundEnemies.keySet());
								while(!gotEnemy) {//while no individual GameEnemy is specified as TARGET
									System.out.println("Choose which ENEMY, by it's ID, you want to CAST " + spellName + " at (or X to EXIT):");//ask which Enemy to target
									System.out.println(" ID |Enemy Details");
									for(int i = 0; i < mapKeys.size(); i++) {//for each mapping...
										String s = mapKeys.get(i);
										int vIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameEnemy
										GameVolume v = room.interior.get(vIndex);//get the GameVolume the indicated GameEnemy is in
										double distance = GameState.distanceBetween(thisVolume,v);//calculate how far from the player the specified GameEnemy is
										byte direction = GameState.getGeneralTraversalDirection(thisVolume,v);//get the general direction in which the GameEnemy is, in relation to the player
										GameEnemy e = foundEnemies.get(mapKeys.get(i));//get the GameEnemy specified
										System.out.println(String.format("%-4d%s%s%s%d%s%d%s%.2f%s%s",i,": ",e.name.toUpperCase(),", ",e.currentHp,"/",e.maxHp,"HP - ",distance," meters, ",GameState.getDirectionName(direction)));
									}
									String input = sanitize(sc.nextLine());
									if(input.equals("x")) {
										castString = "CAST Spell cancelled.";
										return castString;
									} else if(input.matches("//d")) {
										int index = Integer.parseInt(input);
										List<GameEnemy> list = new ArrayList<GameEnemy>();
										list.addAll(foundEnemies.values());
										if(index > 0 && index <= list.size()) {
											targetEnemy = list.get(index - 1);
											String s = mapKeys.get(index - 1);
											volumeIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameEnemy
											targetVolume = room.interior.get(volumeIndex);
											enemyIndex = Integer.parseInt(s.substring(s.indexOf('-') - 1));//get the List index corresponding to the indicated GameEnemy
											gotEnemy = true;//TODO: add check for DISTANCE
										} else {
											System.out.println("Invalid index: " + input);
										}
									} else {
										System.out.println("Invalid index: " + input);
									}
								}
							} else {//only 1 GameEnemy in the GameRoom of specified type
								targetEnemy = foundEnemies.values().iterator().next();//get first (and only) enemy in List
							}
							GameSpell spell = gameState.spellbook.get(spellName);
							if(gameState.currentSp >= spell.spellPointCost) {//Player has sufficient SP to CAST the Spell
								int attackRoll = rollDie(20,gameState.attackBonusMagic,true);
								int damageDealt;
								if(attackRoll == -1) {//crit FAIL
									castString = "You attempt to CAST " + spellName.toUpperCase() + ", but the Spell fizzles and pops embarassingly.";
								} else if(attackRoll == 999) {//crit SUCCESS
									damageDealt = (GameSpell.calculateDamage(spellName) * 2) + spell.bonusDamage;
									castString = "You blast the " + target.toUpperCase() + " with a mighty " + spellName.toUpperCase() + " for " + damageDealt + " points of " + spell.damageType + ".";
									//TODO: implement GameSpell.onCast(int attackRoll)?
									targetEnemy.currentHp -= damageDealt;
								} else if(attackRoll >= targetEnemy.armorClass) {//spell hits
									damageDealt = GameSpell.calculateDamage(spellName) + spell.bonusDamage;
									castString = "Your " + spellName.toUpperCase() + " hits " + target.toUpperCase() + " for " + damageDealt + " points of " + spell.damageType + ".";
									targetEnemy.currentHp -= damageDealt;
								} else {//spell misses
									castString = "Your " + spellName.toUpperCase() + " whizzes by " + target.toUpperCase() + ".";
								}
								if(targetEnemy.currentHp <= 0) {//enemy is slain
									//TODO: implement degrees of dead-ness maybe?
									castString += "\n" + target.toUpperCase() + " falls down dead.";
									gameState.currentXp += targetEnemy.xpValue;
									if(targetEnemy.inventory != null && !targetEnemy.inventory.isEmpty()) {
										castString += " It dropped:";
										for(GameItem enemyItem: targetEnemy.inventory) {
											castString += "\n\t" + enemyItem.name.toUpperCase();
											targetVolume.addItem(enemyItem);
										}
									}
									List<GameEnemy> volumeEnemies = targetVolume.enemies.get(target);
									volumeEnemies.remove(enemyIndex);
									if(volumeEnemies.isEmpty()) {
										targetVolume.enemies.remove(target);
									} else {
										targetVolume.enemies.put(target,volumeEnemies);
									}
									//TODO: add CORPSE of GameEnemy as a GameObject? Maybe...
								} else {
									List<GameEnemy> volumeEnemies = targetVolume.enemies.get(target);
									volumeEnemies.set(enemyIndex,targetEnemy);
									targetVolume.enemies.replace(target,volumeEnemies);
								}
								room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1),targetVolume);
								gameState.visitedRooms.replace(gameState.currentRoomId,room);
							} else {//Player doesn's have enough SP to CAST the Spell
								castString = "You don't have enough SP to CAST " + spellName.toUpperCase() + ".";
							}
						} else {//ENEMY is not in the ROOM
							castString = "You don't see any " + target.toUpperCase() + " to CAST " + spellName.toUpperCase() + " on.";
						}
					} else if(GameObject.objects != null && GameObject.objects.containsKey(target)) {//if TARGET is a valid GameObject
						if(gameState.visitedRooms.get(gameState.currentRoomId).containsObject(target)) {//if the OBJECT is in the ROOM
							//no need to roll for attack on an Object
							GameRoom room = gameState.visitedRooms.get(gameState.currentRoomId);
							GameVolume thisVolume = room.interior.get(gameState.roomVolumeIndex);//get the GameVolume the player is in
							List<GameVolume> volumes = room.getAllVolumesContainingEnemy(target);
							Map<String,GameObject> foundObjects = new HashMap<String,GameObject>();//set up temporary storage for all Objects that match attack criteria
							for(GameVolume v: volumes) {//for each GameVolume that has at least 1 of the specified GameObject in it
								for(int i = 0; i < v.objects.get(target).size(); i++) {//for each of the specified GameObject inside the GameVolume
									GameObject o = v.objects.get(target).get(i);//get the individual GameEnemy and...
									foundObjects.put("" + ((v.zPos - 1) * room.width * room.length) + ((v.yPos - 1) * room.length) + (v.xPos - 1) + "-" + i, o);//map it to the GameVolume (index) & the index in the List where it was found
								}
							}
							//Ask which Object to target, listing distance and direction from player
							GameObject targetObject = null;
							int volumeIndex = -1;
							int objectIndex = -1;
							GameVolume targetVolume = null;
							if(foundObjects.size() > 1) {//more than 1 GameObject of the specified type in the GameRoom
								boolean gotObject = false;
								List<String> mapKeys = new ArrayList<String>();
								mapKeys.addAll(foundObjects.keySet());
								while(!gotObject) {//while no individual GameObject is specified as TARGET
									System.out.println("Choose which OBJECT, by it's ID, you want to CAST " + spellName + " at (or X to EXIT):");//ask which Object to target
									System.out.println(" ID |Object Details");
									for(int i = 0; i < mapKeys.size(); i++) {//for each mapping...
										String s = mapKeys.get(i);
										int vIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameObject
										GameVolume v = room.interior.get(vIndex);//get the GameVolume the indicated GameObject is in
										double distance = GameState.distanceBetween(thisVolume,v);//calculate how far from the player the specified GameObject is
										byte direction = GameState.getGeneralTraversalDirection(thisVolume,v);//get the general direction in which the GameObject is, in relation to the player
										GameObject o = foundObjects.get(mapKeys.get(i));//get the GameObject specified
										System.out.println(String.format("%-4d%s%s%s%d%s%d%s%.2f%s%s",i,": ",o.name.toUpperCase(),", ",o.currentHp,"/",o.maxHp,"HP - ",distance," meters, ",GameState.getDirectionName(direction)));
									}
									String input = sanitize(sc.nextLine());
									if(input.equals("x")) {
										castString = "CAST Spell cancelled.";
										return castString;
									} else if(input.matches("//d")) {
										int index = Integer.parseInt(input);
										List<GameObject> list = new ArrayList<GameObject>();
										list.addAll(foundObjects.values());
										if(index > 0 && index <= list.size()) {
											targetObject = list.get(index - 1);
											String s = mapKeys.get(index - 1);
											volumeIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameObject
											targetVolume = room.interior.get(volumeIndex);
											objectIndex = Integer.parseInt(s.substring(s.indexOf('-') - 1));//get the List index corresponding to the indicated GameObject
											gotObject = true;
										} else {
											System.out.println("Invalid index: " + input);
										}
									} else {
										System.out.println("Invalid index: " + input);
									}
								}
							} else {//only 1 GameObject in the GameRoom of specified type
								targetObject = foundObjects.values().iterator().next();//get first (and only) object in List
							}
							GameSpell spell = gameState.spellbook.get(spellName);
							if(gameState.currentSp >= spell.spellPointCost) {//Player has sufficient SP to CAST the Spell
								if(targetObject.isBreakable) {
									int attackRoll = rollDie(20,gameState.attackBonusMagic,true);
									int damageDealt;
									if(attackRoll == -1) {//crit FAIL
										castString = "You attempt to CAST " + spellName.toUpperCase() + ", but the Spell fizzles and pops embarassingly.";
									} else if(attackRoll == 999) {//crit SUCCESS
										damageDealt = (GameSpell.calculateDamage(spellName) * 2) + spell.bonusDamage;
										castString = "You blast the " + target.toUpperCase() + " with a mighty " + spellName.toUpperCase() + " for " + damageDealt + " points of " + spell.damageType + ".";
										targetObject.currentHp -= (GameSpell.calculateDamage(spellName) * 2) + spell.bonusDamage;
									} else {
										damageDealt = GameSpell.calculateDamage(spellName) + spell.bonusDamage;
										castString = "Your " + spellName.toUpperCase() + " hits " + target.toUpperCase() + " for " + damageDealt + " points of " + spell.damageType + ".";
										targetObject.currentHp -= GameSpell.calculateDamage(spellName) + spell.bonusDamage;
									}
									if(targetObject.currentHp <= 0) {//item is broken
										castString += "/nIt is destroyed in a magical puff of smoke.";
										if(targetObject.contents != null && !targetObject.contents.isEmpty()) {
											castString += " It contained:";
											for(GameItem objectItem: targetObject.contents) {
												castString += "\n\t" + objectItem.name.toUpperCase();
												targetVolume.addItem(objectItem);
											}
										}
										List<GameObject> volumeObjects = targetVolume.objects.get(target);
										volumeObjects.remove(objectIndex);
										if(volumeObjects.isEmpty()) {
											targetVolume.objects.remove(target);
										} else {
											targetVolume.objects.put(target,volumeObjects);
										}
									} else {
										List<GameObject> volumeObjects = targetVolume.objects.get(target);
										volumeObjects.set(objectIndex,targetObject);
										targetVolume.objects.replace(target,volumeObjects);
									}
									//re-save object to the ROOM
									room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1),targetVolume);
									gameState.visitedRooms.replace(gameState.currentRoomId,room);
								} else {
									castString = "You CAST " + spellName + " on the " + target.toUpperCase() + ", but cause no damage.";
								}
								gameState.currentSp -= spell.spellPointCost;
							} else {//player doesn't have enough SP to CAST the Spell
								castString = "You don't have enough SP to CAST " + spellName.toUpperCase() + ".";
							}
						} else {//OBJECT is not in the ROOM
							castString = "You don't see any " + target.toUpperCase() + " to CAST " + spellName.toUpperCase() + " on.";
						}
					} else if(GameItem.items != null && GameItem.items.containsKey(target)) {//if TARGET is a valid GameItem
						if(gameState.visitedRooms.get(gameState.currentRoomId).containsItem(target)) {//if the ITEM is in the ROOM
							//no need to roll for attack on an Item
							GameRoom room = gameState.visitedRooms.get(gameState.currentRoomId);
							GameVolume thisVolume = room.interior.get(gameState.roomVolumeIndex);//get the GameVolume the player is in
							List<GameVolume> volumes = room.getAllVolumesContainingEnemy(target);
							Map<String,GameItem> foundItems = new HashMap<String,GameItem>();//set up temporary storage for all Items that match attack criteria
							for(GameVolume v: volumes) {//for each GameVolume that has at least 1 of the specified GameItem in it
								for(int i = 0; i < v.items.get(target).size(); i++) {//for each of the specified GameItem inside the GameVolume
									GameItem it = v.items.get(target).get(i);//get the individual GameItem and...
									foundItems.put("" + ((v.zPos - 1) * room.width * room.length) + ((v.yPos - 1) * room.length) + (v.xPos - 1) + "-" + i, it);//map it to the GameVolume (index) & the index in the List where it was found
								}
							}
							//Ask which Item to target, listing distance and direction from player
							GameItem targetItem = null;
							int volumeIndex = -1;
							int itemIndex = -1;
							GameVolume targetVolume = null;
							if(foundItems.size() > 1) {//more than 1 GameItem of the specified type in the GameRoom
								boolean gotItem = false;
								List<String> mapKeys = new ArrayList<String>();
								mapKeys.addAll(foundItems.keySet());
								while(!gotItem) {//while no individual GameItem is specified as TARGET
									System.out.println("Choose which ITEM, by it's ID, you want to CAST " + spellName + " at (or X to EXIT):");//ask which Item to target
									System.out.println(" ID |Item Details");
									for(int i = 0; i < mapKeys.size(); i++) {//for each mapping...
										String s = mapKeys.get(i);
										int vIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameItem
										GameVolume v = room.interior.get(vIndex);//get the GameVolume the indicated GameItem is in
										double distance = GameState.distanceBetween(thisVolume,v);//calculate how far from the player the specified GameItem is
										byte direction = GameState.getGeneralTraversalDirection(thisVolume,v);//get the general direction in which the GameItem is, in relation to the player
										GameItem it = foundItems.get(mapKeys.get(i));//get the GameEnemy specified
										System.out.println(String.format("%-4d%s%s%s%d%s%d%s%.2f%s%s",i,": ",it.name.toUpperCase(),", ",it.curDurability,"/",it.maxDurability,"HP - ",distance," meters, ",GameState.getDirectionName(direction)));
									}
									String input = sanitize(sc.nextLine());
									if(input.equals("x")) {
										castString = "CAST Spell cancelled.";
										return castString;
									} else if(input.matches("//d")) {
										int index = Integer.parseInt(input);
										List<GameItem> list = new ArrayList<GameItem>();
										list.addAll(foundItems.values());
										if(index > 0 && index <= list.size()) {
											targetItem = list.get(index - 1);
											String s = mapKeys.get(index - 1);
											volumeIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameItem
											targetVolume = room.interior.get(volumeIndex);
											itemIndex = Integer.parseInt(s.substring(s.indexOf('-') - 1));//get the List index corresponding to the indicated GameItem
											gotItem = true;
										} else {
											System.out.println("Invalid index: " + input);
										}
									} else {
										System.out.println("Invalid index: " + input);
									}
								}
							} else {//only 1 GameItem in the GameRoom of specified type
								targetItem = foundItems.values().iterator().next();//get first (and only) item in List
							}
							GameSpell spell = gameState.spellbook.get(spellName);
							if(gameState.currentSp >= spell.spellPointCost) {//Player has sufficient SP to CAST the Spell
								if(targetItem.isBreakable) {
									int attackRoll = rollDie(20,gameState.attackBonusMagic,true);
									int damageDealt;
									if(attackRoll == -1) {//crit FAIL
										castString = "You attempt to CAST " + spellName.toUpperCase() + ", but the Spell fizzles and pops embarassingly.";
									} else if(attackRoll == 999) {//crit SUCCESS
										damageDealt = (GameSpell.calculateDamage(spellName) * 2) + spell.bonusDamage;
										castString = "You blast the " + target.toUpperCase() + " with a mighty " + spellName.toUpperCase() + " for " + damageDealt + " points of " + spell.damageType + ".";
										targetItem.curDurability -= (GameSpell.calculateDamage(spellName) * 2) + spell.bonusDamage;
									} else {
										damageDealt = GameSpell.calculateDamage(spellName) + spell.bonusDamage;
										castString = "Your " + spellName.toUpperCase() + " hits " + target.toUpperCase() + " for " + damageDealt + " points of " + spell.damageType + ".";
										targetItem.curDurability -= GameSpell.calculateDamage(spellName) + spell.bonusDamage;
									}
									if(targetItem.curDurability <= 0) {//item is broken
										castString += "/nIt is destroyed in a magical puff of smoke.";
										List<GameItem> volumeItems = targetVolume.items.get(target);
										volumeItems.remove(itemIndex);
										if(volumeItems.isEmpty()) {
											targetVolume.items.remove(target);
										} else {
											targetVolume.items.put(target,volumeItems);
										}
									} else {
										List<GameItem> volumeItems = targetVolume.items.get(target);
										volumeItems.set(itemIndex,targetItem);
										targetVolume.items.replace(target,volumeItems);
									}
									//re-save object to the ROOM
									room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1),targetVolume);
									gameState.visitedRooms.replace(gameState.currentRoomId,room);
								} else {
									castString = "You CAST " + spellName + " on the " + target.toUpperCase() + ", but cause no damage.";
								}
								gameState.currentSp -= spell.spellPointCost;
							} else {//player doesn't have enough SP to CAST the Spell
								castString = "You don't have enough SP to CAST " + spellName.toUpperCase() + ".";
							}
						} else {//ITEM is not in the ROOM
							castString = "You don't see any " + target.toUpperCase() + " to CAST " + spellName.toUpperCase() + " on.";
						}
					} else if(GameSpell.spells != null && GameSpell.spells.containsKey(target)) {//if TARGET is a valid GameSpell
						if(spellName == "counterspell") {
							castString = "If only COUNTER-SPELL were a real SPELL in this GAME.";
						} else {//can't target a SPELL!
							castString = "You can't CAST " + spellName.toUpperCase() + " against another SPELL.";
						}
					} else {//not a valid TARGET
						castString = target.toUpperCase() + " isn't a valid TARGET for " + spellName.toUpperCase() + ".";
					}
				} else {//Player doesn't know Spell
					castString = "You don't yet know how to CAST " + spellName.toUpperCase() + ".";
				}
			} else {//not a valid GameSpell
				castString = "You're not sure that " + spellName.toUpperCase() + " is even a real SPELL.";
			}
		}
		return castString;
	}

	/**
	 * close(String) Attempts to CLOSE the indicated object
	 * @param object The object the Player is attempting to CLOSE
	 * @return The result of the CLOSE attempt
	 */
	private static String close(String object) {
		String closeString = "";
		if(gameState.inInventory) {//if Player is looking at their inventory
			if(gameState.inventory.containsKey(object)) {//inventory has ITEM
				closeString = "You're not sure how to CLOSE your " + object.toUpperCase() + ".";
			} else {//inventory doesn't have ITEM
				closeString = "You don't have any " + object.toUpperCase() + " in your bags.";
			}
		} else {//not looking at inventory
			if(GameItem.items != null && GameItem.items.containsKey(object)) {//items
				if(gameState.visitedRooms.get(gameState.currentRoomId).containsItem(object)) {//ITEM is in the ROOM
					closeString = "You're not sure how to CLOSE a " + object.toUpperCase() + ".";
				} else {//item is not in ROOM
					closeString = "You don't see any " + object.toUpperCase() + " to CLOSE.";
				}
			} else if(GameObject.objects != null && GameObject.objects.containsKey(object)) {//objects
				if(gameState.visitedRooms.get(gameState.currentRoomId).containsObject(object)) {//OBJECT is in the ROOM
					GameRoom room = gameState.visitedRooms.get(gameState.currentRoomId);
					GameVolume thisVolume = room.interior.get(gameState.roomVolumeIndex);//get the GameVolume the player is in
					List<GameVolume> volumes = room.getAllVolumesContainingObject(object);
					Map<String,GameObject> foundObjects = new HashMap<String,GameObject>();//set up temporary storage for all Objects that match attack criteria
					for(GameVolume v: volumes) {//for each GameVolume that has at least 1 of the specified GameObject in it
						for(int i = 0; i < v.objects.get(object).size(); i++) {//for each of the specified GameObject inside the GameVolume
							GameObject o = v.objects.get(object).get(i);//get the individual GameObject and...
							foundObjects.put("" + ((v.zPos - 1) * room.width * room.length) + ((v.yPos - 1) * room.length) + (v.xPos - 1) + "-" + i, o);//map it to the GameVolume (index) & the index in the List where it was found
						}
					}
					//Ask which Object to target, listing distance and direction from player
					GameObject roomObject = null;
					int volumeIndex = -1;
					int objectIndex = -1;
					GameVolume targetVolume = null;
					if(foundObjects.size() > 1) {//more than 1 GameObject of the specified type in the GameRoom
						boolean gotObject = false;
						List<String> mapKeys = new ArrayList<String>();
						mapKeys.addAll(foundObjects.keySet());
						while(!gotObject) {//while no individual GameObject is specified as TARGET
							System.out.println("Choose which OBJECT, by it's ID, you want to CLOSE (or X to EXIT):");//ask which Object to target
							System.out.println(" ID |Object Details");
							for(int i = 0; i < mapKeys.size(); i++) {//for each mapping...
								String s = mapKeys.get(i);
								int vIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameObject
								GameVolume v = room.interior.get(vIndex);//get the GameVolume the indicated GameObject is in
								double distance = GameState.distanceBetween(thisVolume,v);//calculate how far from the player the specified GameObject is
								byte direction = GameState.getGeneralTraversalDirection(thisVolume,v);//get the general direction in which the GameObject is, in relation to the player
								GameObject o = foundObjects.get(mapKeys.get(i));//get the GameObject specified
								System.out.println(String.format("%-4d%s%s%s%d%s%d%s%.2f%s%s",i,": ",o.name.toUpperCase(),", ",o.currentHp,"/",o.maxHp,"HP - ",distance," meters, ",GameState.getDirectionName(direction)));
							}
							String input = sanitize(sc.nextLine());
							if(input.equals("x")) {
								closeString = "You decide not to CLOSE anything after all.";
								return closeString;
							} else if(input.matches("//d")) {
								int index = Integer.parseInt(input);
								List<GameObject> list = new ArrayList<GameObject>();
								list.addAll(foundObjects.values());
								if(index > 0 && index <= list.size()) {
									roomObject = list.get(index - 1);
									String s = mapKeys.get(index - 1);
									volumeIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameObject
									targetVolume = room.interior.get(volumeIndex);
									objectIndex = Integer.parseInt(s.substring(s.indexOf('-') - 1));//get the List index corresponding to the indicated GameObject
									gotObject = true;
								} else {
									System.out.println("Invalid index: " + input);
								}
							} else {
								System.out.println("Invalid index: " + input);
							}
						}
					} else {//only 1 GameObject in the GameRoom of specified type
						roomObject = foundObjects.values().iterator().next();//get first (and only) object in List
					}
					if(roomObject.isContainer) {
						if(roomObject.isOpen) {//if the Object is OPEN
							double distance = GameState.distanceBetween(thisVolume,targetVolume);//calculate how far from the player the specified GameObject is
							if(distance <= 1.75) {//Object is NO MORE than 1 GameVolume away in any direction
								roomObject.isOpen = false;
								List<GameObject> volumeObjects = targetVolume.objects.get(object);
								volumeObjects.set(objectIndex,roomObject);
								targetVolume.objects.replace(object,volumeObjects);
								room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1), targetVolume);
								gameState.visitedRooms.replace(gameState.currentRoomId, room);
								closeString = "You CLOSE the "+ object.toUpperCase() + ".";
							} else {
								closeString = object.toUpperCase() + " #" + objectIndex + " is too far away to CLOSE: " + String.format(".2f",distance) + " meters";
							}
						} else {//Object is CLOSEd
							closeString = "The " + object.toUpperCase() + " is already CLOSEd.";
						}
					} else {//not an CLOSE-able Object
						closeString = "You can't figure out how you might CLOSE a " + object.toUpperCase() + ".";
					}
				} else {//item is not in ROOM
					closeString = "You don't see any " + object.toUpperCase() + " to CLOSE.";
				}
			} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(object)) {//enemies
				if(gameState.visitedRooms.get(gameState.currentRoomId).containsEnemy(object)) {//Enemy is in this ROOM
					closeString = "You CLOSE your mind to the " + object.toUpperCase() + ", but it remains in the ROOM with you.";
				} else {//Enemy not in this ROOM
					closeString = "You CLOSE the chapter on the " + object.toUpperCase() + ", hoping to encounter no more.";
				}
			} else if(GameSpell.spells != null && GameSpell.spells.containsKey(object)) {//spells
				if(gameState.spellbook.containsKey(object)) {//Player knows this Spell
					closeString = "You CLOSE your mind to the " + object.toUpperCase() + " Spell.";
				} else {//Spell is unknown to Player
					closeString = "Even if you knew " + object.toUpperCase() + ", you wouldn't know how to CLOSE it.";
				}
			} else {
				closeString = object.toUpperCase() + "? Never heard of it.";
			}
		}
		return closeString;
	}

	/**
	 * drop(String) Attempts to drop the specified item, if carried
	 * @return The narration of the player dropping the specified item
	 */
	private static String drop(String object) {
		String dropString = "";
		if(object.equals("room")) {
			dropString = "It would be easier if the ROOM dropped YOU.";
		} else if(gameState.inInventory) {//if player is looking at their inventory
			if(gameState.inventory.containsKey(object)) {//if the player is carrying the specified object
				GameItem droppedItem = gameState.inventory.remove(object);
				droppedItem.isEquipped = false;
				GameRoom thisRoom = gameState.visitedRooms.get(gameState.currentRoomId);
				GameVolume thisVolume = thisRoom.interior.get(gameState.roomVolumeIndex);
				thisVolume.addItem(droppedItem);
				thisRoom.interior.set(gameState.roomVolumeIndex, thisVolume);
				gameState.visitedRooms.replace(gameState.currentRoomId, thisRoom);
				gameState.currentWeight -= droppedItem.getTotalWeight();
				dropString = "You drop the " + object.toUpperCase() + " on the ground.";
				if(gameState.heldItem == droppedItem.name) {
					gameState.heldItem = null;
					if(GameState.equipSlot_MAIN_HAND.equals(droppedItem)) {
						GameState.equipSlot_MAIN_HAND = null;
					}
					if(GameState.equipSlot_OFF_HAND.equals(droppedItem)) {
						GameState.equipSlot_OFF_HAND = null;
					}
					if(GameState.equipSlot_MAIN_HAND == null && GameState.equipSlot_OFF_HAND == null && gameState.heldItem == null) {
						dropString+= " Your hands are now empty.";
					}
				}
			}
		} else if(GameItem.items != null && GameItem.items.containsKey(object)) {//if the object is a valid game item
			if(gameState.heldItem == object) {//if the player is holding the item
				GameItem droppedItem = gameState.inventory.remove(object);
				droppedItem.isEquipped = false;
				GameRoom thisRoom = gameState.visitedRooms.get(gameState.currentRoomId);
				GameVolume thisVolume = thisRoom.interior.get(gameState.roomVolumeIndex);
				thisVolume.addItem(droppedItem);
				thisRoom.interior.set(gameState.roomVolumeIndex, thisVolume);
				gameState.visitedRooms.replace(gameState.currentRoomId, thisRoom);
				gameState.heldItem = null;
				gameState.currentWeight -= droppedItem.getTotalWeight();
				dropString = "You drop the " + object.toUpperCase() + " on the ground.\nYour hands are now empty.";
			} else if(gameState.inventory.containsKey(object)) {//if the player is otherwise carrying the item
				dropString = "OPEN your inventory to DROP the " + object + ".";
			}
		} else if(GameObject.objects != null && GameObject.objects.containsKey(object)) {//if the object is a valid game object
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsObject(object)) {//if the object is in the room
				dropString = "You can't even TAKE " + object.toUpperCase() + ", let alone DROP it.";
			} else {//object is not in the room
				dropString = "You don't see any " + object.toUpperCase() + " to DROP.";
			}
		} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(object)) {//if the object is a valid game enemy
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsEnemy(object)) {//if the enemy is in the room
				dropString = object.toUpperCase() + " looks like THEY could drop YOU. Careful, now!";
			} else {//enemy is not in the room
				dropString = "Talking about " + object.toUpperCase() + "behind their back, huh?\nThat's just rude.";
			}
		} else if(GameSpell.spells != null && GameSpell.spells.containsKey(object)) {//if the object is a valid game spell
			if(gameState.spellbook.containsKey(object)) {//if the Player knows the Spell
				dropString = "You try to DROP a " + object.toUpperCase() + ", but that's not how Spells work.";
			} else {
				dropString = "You don't know enough about " + object.toUpperCase() + " to DROP it on your foes.";
			}
		} else {//unknown object
			dropString = object.toUpperCase() + "? Never heard of it.";
		}
		return dropString;
	}

	/**
	 * equip(String) Attempts to equip the indicated object
	 * @return A String representing the success or failure of the equip attempt
	 */
	private static String equip(String object) {
		String equipString = "";
		if(object.equals("room")) {
			equipString = "How does one don a ROOM?";
		} else if(gameState.inInventory) {//if player is looking at their inventory
			if(gameState.inventory.containsKey(object)) {//if the player is carrying the specified object
				if(gameState.heldItem == object) {//if the player is literally HOLDING this object already
					equipString = "You already have the " + object + " equipped.";
				} else {//equip the specified object, if able
					GameItem itemToEquip = gameState.inventory.get(object);
					itemToEquip.isEquipped = true;
					switch (itemToEquip.equipSlot) {//figure out where the item should be equipped
					case GameItem.BACK:
						if(GameState.equipSlot_BACK != null) {//if another item is equipped here
							GameState.equipSlot_BACK.isEquipped = false;//unequip that item first
						}
						GameState.equipSlot_BACK = itemToEquip;//equip the new item
						//TODO: implement GameItem.onEquip()
						equipString = "You sling the " + object.toUpperCase() + " across your back.";
						break;
					case GameItem.BELT:
						if(GameState.equipSlot_BELT != null) {
							GameState.equipSlot_BELT.isEquipped = false;
						}
						GameState.equipSlot_BELT = itemToEquip;
						equipString = "You fasten " + object.toUpperCase() + " around your waist.";
						break;
					case GameItem.BOOTS:
						if(GameState.equipSlot_BOOTS != null) {
							GameState.equipSlot_BOOTS.isEquipped = false;
						}
						GameState.equipSlot_BOOTS = itemToEquip;
						equipString = "You strap the " + object.toUpperCase() + " around your ankles.";
						break;
					case GameItem.CUIRASS:
						if(GameState.equipSlot_CUIRASS != null) {
							GameState.equipSlot_CUIRASS.isEquipped = false;
						}
						GameState.equipSlot_CUIRASS = itemToEquip;
						equipString = "You strap the " + object.toUpperCase() + " around your torso.";
						break;
					case GameItem.GAUNTLETS:
						if(GameState.equipSlot_GAUNTLETS != null) {
							GameState.equipSlot_GAUNTLETS.isEquipped = false;
						}
						GameState.equipSlot_GAUNTLETS = itemToEquip;
						equipString = "You slip the " + object.toUpperCase() + " over your bare hands.";
						break;
					case GameItem.GREAVES:
						if(GameState.equipSlot_GREAVES != null) {
							GameState.equipSlot_GREAVES.isEquipped = false;
						}
						GameState.equipSlot_GREAVES = itemToEquip;
						equipString = "You strap the " + object.toUpperCase() + " around your legs.";
						break;
					case GameItem.HELM:
						if(GameState.equipSlot_HELM != null) {
							GameState.equipSlot_HELM.isEquipped = false;
						}
						GameState.equipSlot_HELM = itemToEquip;
						equipString = "You place the " + object.toUpperCase() + " on your head.";
						break;
					case GameItem.MAIN_HAND:
						if(GameState.equipSlot_MAIN_HAND != null) {
							GameState.equipSlot_MAIN_HAND.isEquipped = false;
						}
						GameState.equipSlot_MAIN_HAND = itemToEquip;
						equipString = "You hold the " + object.toUpperCase() + " firmly in your main hand.";
						break;
					case GameItem.OFF_HAND:
						if(GameState.equipSlot_OFF_HAND != null) {
							GameState.equipSlot_OFF_HAND.isEquipped = false;
						}
						GameState.equipSlot_OFF_HAND = itemToEquip;
						equipString = "You grip the " + object.toUpperCase() + " with your off hand.";
						break;
					case GameItem.PANTS:
						if(GameState.equipSlot_PANTS != null) {
							GameState.equipSlot_PANTS.isEquipped = false;
						}
						GameState.equipSlot_PANTS = itemToEquip;
						equipString = "You pull the " + object.toUpperCase() + " over your tattered skivvies.";
						break;
					case GameItem.SHIRT:
						if(GameState.equipSlot_SHIRT != null) {
							GameState.equipSlot_SHIRT.isEquipped = false;
						}
						GameState.equipSlot_SHIRT = itemToEquip;
						equipString = "You pull the " + object.toUpperCase() + " over your bare chest.";
						break;
					case GameItem.SHOES:
						if(GameState.equipSlot_SHOES != null) {
							GameState.equipSlot_SHOES.isEquipped = false;
						}
						GameState.equipSlot_SHOES = itemToEquip;
						equipString = "You slip the " + object.toUpperCase() + " over your bare feet.";
						break;
					case GameItem.RING:
						if(GameState.equipSlot_RING != null) {
							GameState.equipSlot_RING.isEquipped = false;
						}
						GameState.equipSlot_RING = itemToEquip;
						equipString = "You slip the " + object.toUpperCase() + " onto your index finger.";
						break;
					case GameItem.AMULET:
						if(GameState.equipSlot_AMULET != null) {
							GameState.equipSlot_AMULET.isEquipped = false;
						}
						GameState.equipSlot_AMULET = itemToEquip;
						equipString = "You tie the " + object.toUpperCase() + " around your neck.";
						break;
					case GameItem.EARRING:
						if(GameState.equipSlot_EARRING != null) {
							GameState.equipSlot_EARRING.isEquipped = false;
						}
						GameState.equipSlot_EARRING = itemToEquip;
						equipString = "You pin the " + object.toUpperCase() + " to your ear.";
						break;
					case GameItem.CLOAK:
						if(GameState.equipSlot_CLOAK != null) {
							GameState.equipSlot_CLOAK.isEquipped = false;
						}
						GameState.equipSlot_CLOAK = itemToEquip;
						equipString = "You wrap the " + object.toUpperCase() + " around your shoulders.";
						break;
					default:
						gameState.heldItem = object;
						equipString = "You take the " + object.toUpperCase() + " in your hands.";
						break;
					}
				}
			}
		} else if(GameItem.items != null && GameItem.items.containsKey(object)) {//if the object is a valid game item
			if(gameState.heldItem == object) {//if the player is holding the item
				equipString = "You are currently holding the " + object.toUpperCase();
			} else if(gameState.inventory.containsKey(object)){//object is in the player's inventory
				equipString = "It will take a moment to EQUIP your " + object.toUpperCase() + ".\nYou should OPEN your INVENTORY to do this.";
			} else if(GameRoom.lookup(gameState.currentRoomId).containsItem(object)) {//if the item is in the room
				equipString = "You need to TAKE the " + object.toUpperCase() + " before you can EQUIP it.";
			}
		} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(object.toUpperCase())) {//if the object is a valid game enemy
			if(GameRoom.lookup(gameState.currentRoomId).containsEnemy(object)) {//if the enemy is in the room
				equipString = "The " + object.toUpperCase() + " doesn't seem easily worn. Or willing.";
			} else {//enemy is not in the room
				equipString = "Are you asking for a " + object.toUpperCase() + " to hug you?";
			}
		} else if(GameObject.objects != null && GameObject.objects.containsKey(object)) {//object is a valid game object
			if(GameRoom.lookup(gameState.currentRoomId).containsObject(object)) {//if the object is in the room
				equipString = "Even if you could wear a " + object.toUpperCase() + ", why would you want to?";
			} else {
				equipString = "You don't see any " + object.toUpperCase() + " to EQUIP.";
			}
		} else if(GameSpell.spells != null && GameSpell.spells.containsKey(object)) {//object is a valid game spell
			if(gameState.spellbook.containsKey(object)) {//if Player knows the Spell
				equipString = "You ready your mind to unleash the " + object.toUpperCase() + " spell.";
			} else {
				equipString = "Even if you knew " + object.toUpperCase() + ", you couldn't wear it.";
			}
		} else {//unknown object
			equipString = object.toUpperCase() + "? Never heard of it.";
		}
		return equipString;
	}

	/**
	 * exit() Sets isRunning to false so the while() loop will exit
	 */
	private static void exit() {
		isRunning = false;
	}

	/**
	 * displayHelp() Displays the basic HELP menu for this program
	 * @return Basic Help menu text
	 */
	private static String displayHelp() {
		String helpString = "";
		helpString += "-- HELP MENU --\n";
		helpString += "How can I Help you today?\n";
		helpString += displayHelp("commands");
//		helpString += "Type HELP COMMANDS for a list of available COMMANDS.\n";
		return helpString;
	}

	/**
	 * displayHelp(String) Displays more in-depth help for the specified command / category
	 * @param category Name of the Command for which more in-depth help is requested
	 * @return String containing in-depth help for the specified COMMAND
	 */
	private static String displayHelp(String category) {
		String helpString = "";
		switch (category) {
		case "about":
			helpString += "-- HELP: ABOUT --\n";
			helpString += "This COMMAND shows you information about the Game.\n";
			break;
		case "commands":
			helpString += "-- HELP: IN-GAME COMMANDS --\n";
			helpString += "ATTACK  | Attempts to attack a target with held item\n";
			helpString += "DEFEND  | NOT YET IMPLEMENTED\n";//Attempts to defend against an incoming attack
			helpString += "CAST    | Attempts to cast a spell at a target\n";
			helpString += "CLIMB   | NOT YET IMPLEMENTED\n";//Attempts to climb up onto, over, or down from a target
			helpString += "CLOSE   | Attempts to close a container, door, or inventory\n";
			helpString += "DROP    | Drops a carried item into the game world\n";
			helpString += "EQUIP   | Equips a carried item\n";
			helpString += "JUMP    | NOT YET IMPLEMENTED\n";//Attempts to jump onto, over, or off from a target
			helpString += "LISTEN  | MIGHT NOT IMPLEMENT\n";//Listen for a sound or event
			helpString += "LOOK    | Inspects the indicated target in more detail\n";
			helpString += "MOVE    | NOT YET IMPLEMENTED\n";//Attempts to move in a direction\n";
			helpString += "OPEN    | Attempts to open a container, door, or inventory\n";
			helpString += "REST    | NOT YET IMPLEMENTED\n";//Rest to recover Health & Spell points
			helpString += "RUN     | NOT YET IMPLEMENTED\n";//Attempts to run in a direction
			helpString += "SMELL   | MIGHT NOT IMPLEMENT\n";//Smell a target
			helpString += "TAKE    | Attempts to take an item from the game world\n";
			helpString += "TASTE   | MIGHT NOT IMPLEMENT\n";//Taste a target
			helpString += "THINK   | MIGHT NOT IMPLEMENT\n";//Let your brain crunch the numbers for a bit
			helpString += "THROW   | NOT YET IMPLEMENTED\n";//Attempts to throw an item or object at a target
			helpString += "TOUCH   | MIGHT NOT IMPLEMENT\n";//Touch a target
			helpString += "UNEQUIP | Unequips a carried item\n";
			helpString += "USE     | NOT YET IMPLEMENTED\n";//Attempts to use an item on a target
			helpString += "\n";
			helpString += "-- HELP: MISC. COMMANDS --\n";
			helpString += "CREATE  | Allows you to create or edit Game resources\n";
			helpString += "\n";
			helpString += "-- HELP: GENERAL COMMANDS --\n";
			helpString += "ABOUT   | Show information about the Game\n";
			helpString += "COMMANDS| Show the list of available COMMANDS\n";
			helpString += "DEBUG   | Show current state of the Game & its variables\n";
			helpString += "EXIT    | Exit the System, losing any unSaved progress\n";
			helpString += "LOAD    | Load & Play a previously Saved Game\n";
			helpString += "NEW     | Start a New Game\n";
			helpString += "OPTIONS | Show the Options Menu\n";
			helpString += "SAVE    | Save the current Game\n";
			helpString += "TEST    | Load a File & Execute its COMMANDs one at a time\n";
			break;
		case "exit":
			helpString += "-- HELP: EXIT --\n";
			helpString += "This COMMAND will Exit the System. Any unSaved progress will\n";
			helpString += "be lost. You will be prompted to confirm Exit.\n";
			break;
		case "load":
			helpString += "-- HELP: LOAD --\n";
			helpString += "This COMMAND allows you to Load & Play a previously Saved Game\n";
			helpString += "You may specify a number between 1 and 10 to Load the Game in\n";
			helpString += "the corresponding Save Game slot. You may omit the number to\n";
			helpString += "Load the Save Game in the current Save Game slot.\n";
			helpString += "\n";
			helpString += "You will be prompted to confirm Loading a Saved Game.\n";
			helpString += "\n";
			helpString += "Syntax: LOAD [n] (where [n] is a number between 1 and 10)\n";
			break;
		case "new":
			helpString += "-- HELP: NEW --\n";
			helpString += "This COMMAND will start a New Game from the beginning.\n";
			break;
		case "options":
			helpString += "-- HELP: OPTIONS --\n";
			helpString += "This COMMAND will Show the Options Menu, where you can change\n";
			helpString += "various Game Settings.\n";
			break;
		case "save":
			helpString += "-- HELP: SAVE --\n";
			helpString += "This COMMAND will Save the current Game. You may specify a\n";
			helpString += "number between 1 and 10 to Save the Game to the corresponding\n";
			helpString += "Save Game slot. You may omit the number to Overwrite the\n";
			helpString += "Save Game in the current Save Game slot.\n";
			helpString += "\n";
			helpString += "Syntax: SAVE\n";
			helpString += "Syntax: SAVE [n] (where [n] is anumber between 1 and 10)\n";
			break;
		case "test":
			helpString += "-- HELP: TEST --\n";
			helpString += "This COMMAND allows you to Load a text file that contains a\n";
			helpString += "sequential list of COMMANDS. These COMMANDs will be parsed by\n";
			helpString += "the system, one after the other. Each COMMAND must be on its\n";
			helpString += "own line.";
			helpString += "\n";
			helpString += "Syntax: TEST \"full_file_path/fileName.extension\"\n";
			break;
		default:
			helpString += "Invalid COMMAND: '" + category + "'\n";
			helpString += "Type HELP COMMANDS for a list of available COMMANDS";
			break;
		}
		return helpString;
	}

	/**
	 * loadGame(int) Loads the Game Saved in the Save Game slot indicated
	 * @param saveSlot The Save Game slot to Load
	 * @throws IOException If the File in the specified Save Game slot can't be found, accessed, or read
	 * @throws DataFormatException 
	 */
	private static void loadGame(int saveSlot) throws IOException, DataFormatException {
		if(saveGames == null || saveGames.length == 0) {//no Saved Games
			System.out.println("No Saved Games to Load. Type NEW to start a New Game.");
		} else {//look for the Saved Game in specified slot
			if(saveSlot < 1 || saveSlot > 10) {//Save Game slot out of range
				System.out.println("Invalid Save Game slot: " + saveSlot + ".");
			} else {//Save Game slot is valid
				File f = saveGames[saveSlot - 1];
				if(f == null || !f.exists() || !f.isFile()) {//no File stored in Save Game slot
					System.out.println("No Game Saved in slot " + saveSlot + ".");
				} else {//got a Save Game File
					byte[] fileData = decompress(readFile(f));
					fileData = Base64.getDecoder().decode(fileData);
					GameState newGameState = GameState.parseBytes(fileData);
					if(newGameState == null) {
						System.out.println("Saved Game in slot [" + saveSlot + "] is corrupt. Cannot LOAD!");
					} else {
						gameState = newGameState;
						saveGameSlot = saveSlot;
						System.out.println("Saved Game in slot [" + saveSlot + "] loaded.");
						System.out.println(gameState.visitedRooms.get(gameState.currentRoomId).description);
					}
				}
			}
		}
	}

	/**
	 * look(String) Returns the description of the indicated Game object
	 * @param object Object the Player wants to observe more closely
	 * @return Description of the object the Player has indicated to observe
	 */
	private static String look(String object) {
		String lookString = "";
		if(object.matches("(the\\s+|this\\s+)*\\s*room")) {
			lookString = narrate("room",gameState.currentRoomId + "");
		} else if(object.matches("inv(entory)?\\s*.*")) {
			lookString = showInventory();
		} else if(gameState.inInventory) {
			if(gameState.inventory.containsKey(object)) {
				GameItem item = gameState.inventory.get(object);
				lookString += item.description;
				if(item.maxUses != -1) {//use-based item
					lookString += "\nIt has " + item.remainingUses + " uses left.";
				} else {//quantity-based item
					lookString += "\nYou have " + item.quantity + " in your inventory.";
				}
				if(item.isEquipped) {
					lookString += "\nYou currently have it equipped.";
				}
			} else {
				lookString = "You see no " + object.toUpperCase() + " in your inventory.";
			}
		} else if(GameItem.items != null && GameItem.items.containsKey(object)) {//item is in the game
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsItem(object)) {//item is in the room
				List<GameVolume> volumes = gameState.visitedRooms.get(gameState.currentRoomId).getAllVolumesContainingItem(object);
				if(volumes != null && volumes.size() > 0) {
					int count = 0;
					for(GameVolume v: volumes) {
						count += v.items.get(object).size();
					}
					lookString = GameItem.lookup(object).description + "\nThere are " + count + " in the ROOM.";
				}
			} else {
				lookString = "You see no " + object.toUpperCase() + " in the Room.";
			}
		} else if(GameObject.objects != null && GameObject.objects.containsKey(object)) {//object is in the game
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsObject(object)) {//object is in the room
				List<GameVolume> volumes = gameState.visitedRooms.get(gameState.currentRoomId).getAllVolumesContainingObject(object);
				if(volumes != null && volumes.size() > 0) {
					int count = 0;
					for(GameVolume v: volumes) {
						count += v.objects.get(object).size();
					}
					lookString = GameObject.lookup(object).description + "\nThere are " + count + " in the ROOM.";
				}
			} else {
				lookString = "You see no " + object.toUpperCase() + " in the Room.";
			}
		} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(object)) {//enemy is in the game
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsEnemy(object)) {//enemy is in the room
				List<GameVolume> volumes = gameState.visitedRooms.get(gameState.currentRoomId).getAllVolumesContainingEnemy(object);
				if(volumes != null && volumes.size() > 0) {
					int count = 0;
					for(GameVolume v: volumes) {
						count += v.enemies.get(object).size();
					}
					lookString = GameItem.lookup(object).description + "\nThere are " + count + " currently in the ROOM.";
				}
			} else {
				lookString = "You see no " + object.toUpperCase() + " in the Room.";
			}
		} else if(GameSpell.spells != null && GameSpell.spells.containsKey(object)) {//spell is in the game
			if(gameState.spellbook.containsKey(object)) {
				lookString = gameState.spellbook.get(object).description;
			} else {
				lookString = "You don't know enough about " + object.toUpperCase() + " to describe it.";
			}
		} else {//object not in the game
			lookString = "You see nothing around you that resembles a " + object.toUpperCase() + ".";
		}
		return lookString;
	}

	/**
	 * move(String) Attempts to move the Player in a direction if specified. May also be used to move an object in a direction if specified.
	 * @return A String representation of the move attempt
	 */
	private static String move(String context) {
		String moveString = "";
		if(context.matches("(into\\s+)*(the\\s+|this\\s+)*\\s*room")) {
			moveString = "As comfortable as this ROOM looks, you don't see yourself moving in just yet.";
		} else if(context.matches("\\w+")) {//only 1 word detected in context
			GameRoom room = gameState.visitedRooms.get(gameState.roomVolumeIndex);
			GameVolume currentVolume = room.interior.get(gameState.roomVolumeIndex);
			byte moveDir = (1 << 0 | 1 << 2 | 1 << 4);//21 (no movement on all axes)
			if(context.matches("w(est)?")) {//if direction, move player in that direction (affecting gameState.currentRoomId, gameState.roomVolumeIndex, etc.
				moveDir = (0 << 0 | 1 << 2 | 1 << 4);//20 - negative X axis movement
			} else if(context.matches("e(ast)?")) {
				moveDir = (2 << 0 | 1 << 2 | 1 << 4);//22 - positive X axis movement
			} else if(context.matches("n(orth)?")) {
				moveDir = (1 << 0 | 2 << 2 | 1 << 4);//25 - positive Y axis movement
			} else if(context.matches("s(outh)?")) {
				moveDir = (1 << 0 | 0 << 2 | 1 << 4);//17 - negative Y axis movement
//			} else if(context.matches("l(eft)?")) {
//				//should I even bother to figure out context of facingDirection()?
//			} else if(context.matches("r(ight)?")) {
//				//should I even bother to figure out context of facingDirection()?
			} else if(context.matches("u((p)?((ward)?(s)?)?)?")) {
				moveDir = (1 << 0 | 1 << 2 | 2 << 4);//37 - positive Z axis movement
			} else if(context.matches("d((own)?((ward)?(s)?)?)?")) {
				moveDir = (1 << 0 | 1 << 2 | 0 << 4);//5 - negative Z axis movement
			} else if(context.matches("northwest|nw")) {
				moveDir = (0 << 0 | 2 << 2 | 1 << 4);//24 - negative X axis movement, positive Y axis movement
			} else if(context.matches("northeast|ne")) {
				moveDir = (2 << 0 | 2 << 2 | 1 << 4);//26 - positive X axis movement, positive Y axis movement
			} else if(context.matches("southwest|sw")) {
				moveDir = (0 << 0 | 0 << 2 | 1 << 4);//16 - negative X axis movement, negative Y axis movement
			} else if(context.matches("southeast|se")) {
				moveDir = (2 << 0 | 0 << 2 | 1 << 4);//18 - positive X axis movement, negative Y axis movement
			} else if(GameObject.objects.containsKey(context)) {//trying to move an Object
				if(gameState.visitedRooms.get(gameState.currentRoomId).containsObject(context)) {//if the current Room contains the Object
					if(GameObject.objects.get(context).isMovable) {//if the player can physically move this Object
						moveString = "You must specify a DIRECTION to MOVE the " + context.toUpperCase() + ".";
					} else {//Object is NOT movable
						moveString = "The " + context.toUpperCase() + " seems to be immovable.";
					}
				} else {//Object not in the room
					moveString = "You don't see any " + context.toUpperCase() + " to MOVE.";
				}
			} else if(GameItem.items.containsKey(context)) {//trying to move an Item
				if(gameState.visitedRooms.get(gameState.currentRoomId).containsItem(context)) {//if the current Room contains the Item
					moveString = "Tou must specify a DIRECTION to MOVE the " + context.toUpperCase() + ".";
				} else {//Item not in the room
					moveString = "You don't see any " + context.toUpperCase() + " to MOVE.";
				}
			} else if(GameEnemy.enemies.containsKey(context)) {//trying to move an Enemy
				if(gameState.visitedRooms.get(gameState.currentRoomId).containsEnemy(context)) {//if the Enemy is in the Room
					moveString = "The " + context.toUpperCase() + " looks at you as if to say, \"No, *you* MOVE\"!";
				} else {//Enemy not in the Room
					moveString = "You can't tell the " + context.toUpperCase() + " to MOVE if they aren't here.";
				}
			} else {//unknown object
				moveString = context.toUpperCase() + "? Never heard of it.";
			}
			if(moveDir != 21) {//if got a good movement direction
				GameVolume nextVolume = gameState.getNextVolumeInDirection(currentVolume,moveDir);
				if(nextVolume != null) {
					if(gameState.canTraverse(nextVolume.xPos, nextVolume.yPos, nextVolume.zPos, moveDir)) {
//						gameState.roomVolumeIndex = (((nextVolume.zPos - 1) * room.width * room.length) + ((nextVolume.yPos - 1) * room.length) + (nextVolume.xPos - 1));
						moveString = gameState.traverse(nextVolume.xPos, nextVolume.yPos, nextVolume.zPos, moveDir, 1);
					} else {//player can't move in the direction indicated
						moveString = "You can't MOVE " + context.toUpperCase() + ".";
					}
				}
			} else {//no movement indicated
				moveString = "You don't MOVE anywhere.";
			}
		} else if(context.matches("\\w+\\s+\\w+")) {//more than 1 word detected in context
			String[] tokens = context.split("\\s+");
			//TODO: determine intent of tokens...implement this method
			//if direction,something else -> move player in direction...handled above already
			//if item/object,direction -> move item/object in direction (if able)
			//if item/object,something else -> shake your head and give up
			//if enemy,something else -> say something snarky
		}
		return moveString;
	}

	/**
	 * Narrate(String,String) Describe the specified object in more detail
	 * @param type Type of object to look up
	 * @param identifier Identifier of the object to look up
	 * @param id Id of the object to look up (if a room)
	 * @return String containing the narrative to be printed to the screen
	 */
	private static String narrate(String type, String identifier) {
		String narration = "";
		switch (type) {
		case "room":
			GameRoom room = gameState.visitedRooms.get(Integer.parseInt(identifier));
			narration += room.description;
			List<GameObject> allObjects = new ArrayList<GameObject>();
			for(GameVolume v: room.interior) {
				if(v.objects != null && !v.objects.isEmpty()) {
					for(List<GameObject> l: v.objects.values()) {
						allObjects.addAll(l);
					}
				}
			}
			if(!allObjects.isEmpty()) {
				narration += "\nIt contains these OBJECTS:";
				for(GameObject o : allObjects) {
					narration += "\n\t" + o.name.toUpperCase();
				}
			}
			List<GameItem> allItems = new ArrayList<GameItem>();
			for(GameVolume v: room.interior) {
				if(v.items != null && !v.items.isEmpty()) {
					for(List<GameItem> l: v.items.values()) {
						allItems.addAll(l);
					}
				}
			}
			if(!allItems.isEmpty()) {
				narration += "\nIt contains these ITEMS:";
				for(GameItem i: allItems) {
					narration += "\n\t" + i.name.toUpperCase();
				}
			}
			List<GameEnemy> allEnemies = new ArrayList<GameEnemy>();
			for(GameVolume v: room.interior) {
				if(v.enemies != null && !v.enemies.isEmpty()) {
					for(List<GameEnemy> l: v.enemies.values()) {
						allEnemies.addAll(l);
					}
				}
			}
			if(!allEnemies.isEmpty()) {
				narration += "\nIt contains these ENEMIES:";
				for(GameEnemy e: allEnemies) {
					narration += "\n\t" + e.name.toUpperCase();
				}
			}
			break;
		case "object":
			for(GameVolume v: gameState.visitedRooms.get(gameState.currentRoomId).getAllVolumesContainingObject(identifier)) {
				for(List<GameObject> l: v.objects.values()) {
					for(GameObject object: l) {
						narration += object.description;
						if(object.isContainer && object.contents != null && !object.contents.isEmpty()) {
							if(object.isLocked) {
								narration += " It is LOCKED.";
							} else {
								if(object.isOpen) {
									narration += " It contains these ITEMS:";
									for(GameItem i: object.contents) {
										narration += "\n\t" + i.name.toUpperCase();
									}
								} else {
									narration += " It is CLOSED.";
								}
							}
						}
					}
				}
			}
			break;
		case "item":
			if(gameState.inInventory) {
				GameItem item = gameState.inventory.get(identifier);
				if(item != null) {
					narration += item.description;
				}
			} else {
				for(GameVolume v: gameState.visitedRooms.get(gameState.currentRoomId).getAllVolumesContainingObject(identifier)) {
					for(List<GameItem> l: v.items.values()) {
						for(GameItem item: l) {
							narration += item.description;
						}
					}
				}
			}
			break;
		case "inventory":
			if(gameState.inventory != null && !gameState.inventory.isEmpty()) {
				narration += "Your inventory contains these ITEMS:";
				for(GameItem i: gameState.inventory.values()) {
					narration += "\n\t" + i.name.toUpperCase();
				}
			}
			break;
		case "enemy":
			for(GameVolume v: gameState.visitedRooms.get(gameState.currentRoomId).getAllVolumesContainingObject(identifier)) {
				for(List<GameEnemy> l: v.enemies.values()) {
					for(GameEnemy enemy: l) {
						narration += enemy.description;
						narration += "\nThe " + enemy.name.toUpperCase() + " has " + enemy.currentHp + "/" + enemy.maxHp + "HP.";
					}
				}
			}
			break;
		case "spell":
			GameSpell spell = gameState.spellbook.get(identifier);
			if(spell != null) {
				narration += spell.description;
			}
		default:
			narration += "You can't see any " + identifier + " from where you are.";
			break;
		}
		return narration;
	}

	/**
	 * newGame() Starts a New Game
	 */
	private static void newGame() {
		gameState = new GameState();
		//any more setup needed here?
		System.out.println(narrate("room",gameState.currentRoomId + ""));
	}

	/**
	 * open(String) Attempts to OPEN the specified object
	 * @param object The object which the Player is attempting to OPEN
	 * @return Result of the OPEN attempt
	 */
	private static String open(String object) {
		String openString = "";
		if(gameState.inInventory) {//if Player is looking at their inventory
			if(gameState.inventory.containsKey(object)) {//inventory has ITEM
				openString = "You're not sure how to OPEN your " + object.toUpperCase() + ".";
			} else {//inventory doesn't have ITEM
				openString = "You don't have any " + object.toUpperCase() + " in your bags.";
			}
		} else {//not looking at inventory
			if(GameItem.items != null && GameItem.items.containsKey(object)) {//items
				if(gameState.visitedRooms.get(gameState.currentRoomId).containsItem(object)) {//Item is in the Room
					openString = "You can't seem to figure out how to OPEN " + object.toUpperCase() + ".";
				} else {
					openString = "You see  no " + object.toUpperCase() + " to OPEN.";
				}
			} else if(GameObject.objects != null && GameObject.objects.containsKey(object)) {//objects
				if(gameState.visitedRooms.get(gameState.currentRoomId).containsObject(object)) {//Object is in the Room
					GameRoom room = gameState.visitedRooms.get(gameState.currentRoomId);
					GameVolume thisVolume = room.interior.get(gameState.roomVolumeIndex);//get the GameVolume the player is in
					List<GameVolume> volumes = room.getAllVolumesContainingObject(object);
					Map<String,GameObject> foundObjects = new HashMap<String,GameObject>();//set up temporary storage for all Objects that match attack criteria
					for(GameVolume v: volumes) {//for each GameVolume that has at least 1 of the specified GameObject in it
						for(int i = 0; i < v.objects.get(object).size(); i++) {//for each of the specified GameObject inside the GameVolume
							GameObject o = v.objects.get(object).get(i);//get the individual GameObject and...
							foundObjects.put("" + ((v.zPos - 1) * room.width * room.length) + ((v.yPos - 1) * room.length) + (v.xPos - 1) + "-" + i, o);//map it to the GameVolume (index) & the index in the List where it was found
						}
					}
					//Ask which Object to target, listing distance and direction from player
					GameObject roomObject = null;
					int volumeIndex = -1;
					int objectIndex = -1;
					GameVolume targetVolume = null;
					if(foundObjects.size() > 1) {//more than 1 GameObject of the specified type in the GameRoom
						boolean gotObject = false;
						List<String> mapKeys = new ArrayList<String>();
						mapKeys.addAll(foundObjects.keySet());
						while(!gotObject) {//while no individual GameObject is specified as TARGET
							System.out.println("Choose which OBJECT, by it's ID, you want to CLOSE (or X to EXIT):");//ask which Object to target
							System.out.println(" ID |Object Details");
							for(int i = 0; i < mapKeys.size(); i++) {//for each mapping...
								String s = mapKeys.get(i);
								int vIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameObject
								GameVolume v = room.interior.get(vIndex);//get the GameVolume the indicated GameObject is in
								double distance = GameState.distanceBetween(thisVolume,v);//calculate how far from the player the specified GameObject is
								byte direction = GameState.getGeneralTraversalDirection(thisVolume,v);//get the general direction in which the GameObject is, in relation to the player
								GameObject o = foundObjects.get(mapKeys.get(i));//get the GameObject specified
								System.out.println(String.format("%s: %d/%dHP - %.2f meters, %s",o.name.toUpperCase(),o.currentHp,o.maxHp,distance,GameState.getDirectionName(direction)));
							}
							String input = sanitize(sc.nextLine());
							if(input.equals("x")) {
								openString = "You decide not to CLOSE anything after all.";
								return openString;
							} else if(input.matches("//d")) {
								int index = Integer.parseInt(input);
								List<GameObject> list = new ArrayList<GameObject>();
								list.addAll(foundObjects.values());
								if(index > 0 && index <= list.size()) {
									roomObject = list.get(index - 1);
									String s = mapKeys.get(index - 1);
									volumeIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameObject
									targetVolume = room.interior.get(volumeIndex);
									objectIndex = Integer.parseInt(s.substring(s.indexOf('-') - 1));//get the List index corresponding to the indicated GameObject
									gotObject = true;
								} else {
									System.out.println("Invalid index: " + input);
								}
							} else {
								System.out.println("Invalid index: " + input);
							}
						}
					} else {//only 1 GameObject in the GameRoom of specified type
						roomObject = foundObjects.values().iterator().next();//get first (and only) object in List
					}
					double distance = GameState.distanceBetween(thisVolume,targetVolume);//calculate how far from the player the specified GameObject is
					if(roomObject.isContainer) {//if Object can be OPENed
						if(roomObject.isOpen) {//if Object is already OPEN
							openString = "The " + object.toUpperCase() + " is already OPEN.";
						} else {//OPEN the Object
							if(distance <= 1.75) {//Object is NO MORE than 1 GameVolume away in any direction
								roomObject.isOpen = false;
								List<GameObject> volumeObjects = targetVolume.objects.get(object);
								volumeObjects.set(objectIndex,roomObject);
								targetVolume.objects.replace(object,volumeObjects);
								room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1), targetVolume);
								gameState.visitedRooms.replace(gameState.currentRoomId, room);
								openString = "You OPEN the "+ object.toUpperCase() + ".";
							} else {
								openString = object.toUpperCase() + " #" + objectIndex + " is too far away to OPEN: " + String.format(".2f",distance) + " meters";
							}
						}
					} else {//Object can't be OPENed
						openString = object.toUpperCase() + " doesn't seem OPENable.";
					}
				} else {
					openString = "You see no " + object.toUpperCase() + " to OPEN.";
				}
			} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(object)) {//enemies
				if(gameState.visitedRooms.get(gameState.currentRoomId).containsEnemy(object)) {//Enemy is in the Room
					openString = "You feel the urge to OPEN UP to " + object.toUpperCase() + ", but they don't seem sympathetic.";
				} else {
					openString = "It's more likely that " + object.toUpperCase() + " would OPEN you!";
				}
			} else if(GameSpell.spells != null && GameSpell.spells.containsKey(object)) {//spells
				if(gameState.spellbook.containsKey(object)) {//Player knows the Spell
					openString = "You OPEN your mind to the " + object.toUpperCase() + " Spell.";
				} else {
					openString = "Even if you knew " + object.toUpperCase() + ", you wouldn't know how to OPEN it.";
				}
			} else {
				openString = object.toUpperCase() + "? Never heard of it.";
			}
		}
		return openString;
	}

	/**
	 * options() Shows the different Options available to the Player
	 */
	private static void options() {
		System.out.println("No options available yet. Come back later!");
	}

	/**
	 * rollDie(int,int,boolean) Returns the result of a Die + the roller's bonus, if any
	 * @param dieVal The die to be rolled
	 * @param bonus Any bonus to this roll
	 * @param canCrit Indicates whether the check rolled against can be a CRITICAL roll.
	 * @return Integer value from 1 to [die] representing the result of the d20 roll. A CRIT_FAIL is indicated by a -1. A CRIT_SUCCESS is indicated by a 
	 */
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

	/**
	 * saveGame() Saves the current Game in the current Save Game slot, if one is active
	 * @throws IOException If the specified File is not found on the FileSystem or if the File cannot be written to
	 */
	private static void saveGame() throws IOException {
		if(saveGames == null) {//all Save Game slots are empty
			saveGames = new File[10];
			File tempFile = new File("./saveGames/slot_01");
			tempFile.mkdirs();
			tempFile = new File(tempFile,"saveGame.sav");
			tempFile.createNewFile();
			saveGames[0] = tempFile;
			saveGameSlot = 1;
			saveGame(saveGameSlot);
		} else {//the Save Game slots are already set up
			for(int i = 0; i < saveGames.length; i++) {
				if(saveGames[i] == null || !saveGames[i].exists()) {//find first Save Game slot that is empty
					File tempFile;
					tempFile = new File("./saveGames/slot_" + String.format("%1$02d", i + 1));
					tempFile.mkdirs();
					tempFile = new File(tempFile,"saveGame.sav");
					tempFile.createNewFile();
					saveGames[i] = tempFile;
					saveGameSlot = i + 1;
					saveGame(saveGameSlot);
					break;
				}
			}
			if(saveGameSlot == 0) {//All slots are full
				System.out.println("All Save Game slots are in use. Specify a slot to Overwrite.\n");//let user try again with SAVE [x]
			}
		}
	}

	/**
	 * saveGame(int) Saves the current Game in the specified Save Game slot
	 * @param saveSlot The Save Game slot to Save the state of the current Game to
	 * @throws IOException If the specified File is not found on the FileSystem or if the File cannot be written to
	 */
	private static void saveGame(int saveSlot) throws IOException {
		if(saveGames == null) {//all Save Game slots are empty
			saveGames = new File[10];
		}
		if(saveGames[saveSlot - 1] == null) {
			File tempFile = null;
			tempFile = new File("./saveGames/slot_" + String.format("%1$02d", saveSlot));
			tempFile.mkdirs();
			tempFile = new File(tempFile,"saveGame.sav");
			tempFile.createNewFile();
			saveGames[saveSlot - 1] = tempFile;
			saveGameSlot = saveSlot;
		}
		fileOut = new FileOutputStream(saveGames[saveSlot - 1]);
		fileOut.write(compress(Base64.getEncoder().encode(gameState.getBytes())));
		fileOut.close();
		System.out.println("Game Saved to slot " + saveSlot + ".");
	}

	/**
	 * showInventory() Displays the current contents of the Player's inventory
	 * @return String representation of the Player's inventory
	 */
	private static String showInventory() {
		String invString = "«·•O•·•O•·•O•·•O•·•O•·•O•·•O•·»CURRENT INVENTORY«·•O•·•O•·•O•·•O•·•O•·•O•·•O•·»\n";
		List<GameItem> invItems = new ArrayList<GameItem>();
		invItems.addAll(gameState.inventory.values());
		for(int i = 0; i < invItems.size(); i++) {
			GameItem item = invItems.get(i);
			invString += String.format("%-20s",item.name.toUpperCase());//name of the item
			if(item.maxUses == -1) {//quantity-based item
				invString += String.format("%-10s", "Qty: " + item.quantity);
				invString += String.format("%-10s", "Wt: " + (item.weight * item.quantity));//show useful info for the item
			} else {//use-based item
				invString += String.format("%-10s", "Use: " + item.remainingUses);
				invString += String.format("%-10s", "Wt: " + item.weight);
			}
			if((i + 1) % 2 == 0) {//have 2 items per line
				invString +="\n";
			}
		}
		return invString;
	}

	/**
	 * take(String) Attempts to add the specified object to the Player's inventory
	 * @param object The object to be picked up
	 * @return String narration of success or failure to take the specified object
	 */
	private static String take(String object) {
		String takeString = "";
		if(object.equals("room")) {
			takeString = "You can't figure out how to fit a whole room in your pocket.";
		} else if(gameState.inInventory) {
			if(gameState.inventory.containsKey(object)) {
				takeString = "You already have " + object.toUpperCase() + ". Did you want to DROP " + object.toUpperCase() + " instead?";
			} else {
				takeString = "You can't seem to find " + object.toUpperCase() + " in your inventory.";
			}
		} else if(GameItem.items != null && GameItem.items.containsKey(object)) {//if the item is a valid GameItem
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsItem(object)) {//if the current room contains the specified item
				GameRoom room = gameState.visitedRooms.get(gameState.currentRoomId);
				GameVolume thisVolume = room.interior.get(gameState.roomVolumeIndex);//get the GameVolume the player is in
				List<GameVolume> volumes = room.getAllVolumesContainingItem(object);
				Map<String,GameItem> foundItems = new HashMap<String,GameItem>();//set up temporary storage for all Items that match attack criteria
				for(GameVolume v: volumes) {//for each GameVolume that has at least 1 of the specified GameItem in it
					for(int i = 0; i < v.items.get(object).size(); i++) {//for each of the specified GameItem inside the GameVolume
						GameItem targetItem = v.items.get(object).get(i);//get the individual GameItem and...
						foundItems.put("" + ((v.zPos - 1) * room.width * room.length) + ((v.yPos - 1) * room.length) + (v.xPos - 1) + "-" + i, targetItem);//map it to the GameVolume (index) & the index in the List where it was found
					}
				}
				//Ask which Item to target, listing distance and direction from player
				GameItem roomItem = null;
				int volumeIndex = -1;
				int itemIndex = -1;
				GameVolume targetVolume = null;
				if(foundItems.size() > 1) {//more than 1 GameItem of the specified type in the GameRoom
					boolean gotObject = false;
					List<String> mapKeys = new ArrayList<String>();
					mapKeys.addAll(foundItems.keySet());
					while(!gotObject) {//while no individual GameItem is specified as TARGET
						System.out.println("Choose which ITEM, by it's ID, you want to TAKE (or X to EXIT):");//ask which Item to target
						System.out.println(" ID |Item Details");
						for(int i = 0; i < mapKeys.size(); i++) {//for each mapping...
							String s = mapKeys.get(i);
							int vIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameItem
							GameVolume v = room.interior.get(vIndex);//get the GameVolume the indicated GameItem is in
							double distance = GameState.distanceBetween(thisVolume,v);//calculate how far from the player the specified GameItem is
							byte direction = GameState.getGeneralTraversalDirection(thisVolume,v);//get the general direction in which the GameItem is, in relation to the player
							GameItem targetItem = foundItems.get(mapKeys.get(i));//get the GameItem specified
							System.out.println(String.format("%-4d%s%s%s%d%s%d%s%.2f%s%s",i,": ",targetItem.name.toUpperCase(),", ",targetItem.curDurability,"/",targetItem.maxDurability,"HP - ",distance," meters, ",GameState.getDirectionName(direction)));
						}
						String input = sanitize(sc.nextLine());
						if(input.equals("x")) {
							takeString = "You decide not to TAKE anything after all.";
							return takeString;
						} else if(input.matches("//d")) {
							int index = Integer.parseInt(input);
							List<GameItem> list = new ArrayList<GameItem>();
							list.addAll(foundItems.values());
							if(index > 0 && index <= list.size()) {
								roomItem = list.get(index - 1);
								String s = mapKeys.get(index - 1);
								volumeIndex = Integer.parseInt(s.substring(0,s.indexOf('-')));//get the index of the GameVolume for the specified GameItem
								targetVolume = room.interior.get(volumeIndex);
								itemIndex = Integer.parseInt(s.substring(s.indexOf('-') - 1));//get the List index corresponding to the indicated GameItem
								gotObject = true;
							} else {
								System.out.println("Invalid index: " + input);
							}
						} else {
							System.out.println("Invalid index: " + input);
						}
					}
				} else {//only 1 GameItem in the GameRoom of specified type
					roomItem = foundItems.values().iterator().next();//get first (and only) Item in List
				}
				if(gameState.canCarry(roomItem.getTotalWeight())) {//player can carry this item
//					gameState.pickUpItem(item);
					double distance = GameState.distanceBetween(thisVolume,targetVolume);//calculate how far from the player the specified GameItem is
					if(distance <= 1.75) {//Item is NO MORE than 1 GameVolume away in any direction
						//take item
						if(gameState.inventory.containsKey(object)) {//if Player already has at least 1 of the item
							GameItem curItem = gameState.inventory.get(object);
							if(curItem.maxUses > 0) {//if item is a use-based item
								curItem.remainingUses = Math.min(curItem.maxUses, curItem.remainingUses + roomItem.remainingUses);//refill the item's uses
							} else {//item is quantity-based
								curItem.quantity += roomItem.quantity;//add quantity of new item to quantity of old item
								gameState.currentWeight += roomItem.getTotalWeight();
							}
							gameState.inventory.put(object, curItem);//add to / replace in inventory
						} else {//pick up the item
							gameState.inventory.put(object, roomItem);
							gameState.currentWeight += roomItem.getTotalWeight();
						}
						List<GameItem> volumeItems = targetVolume.items.get(object);
						volumeItems.set(itemIndex,roomItem);
						targetVolume.items.replace(object,volumeItems);
						room.interior.set(((targetVolume.zPos - 1) * room.width * room.length) + ((targetVolume.yPos - 1) * room.length) + (targetVolume.xPos - 1), targetVolume);
						gameState.visitedRooms.replace(gameState.currentRoomId, room);
						takeString = "You pick up the " + object.toUpperCase() + ".";
					} else {
						takeString = object.toUpperCase() + " #" + itemIndex + " is too far away to TAKE: " + String.format(".2f",distance) + " meters";
					}
				} else {//player can't carry item
					takeString = "Picking up " + object.toUpperCase() + " would over-encumber you.\nYou must DROP some items first.";
				}
			} else {//current room doesn't have this item
				takeString = "You see no " + object.toUpperCase() + " in the room to TAKE.";
			}
		} else if(GameObject.objects != null && GameObject.objects.containsKey(object)) {//if the object is part of the game
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsObject(object)) {//if the object is in the current room
				takeString = "You don't think you can TAKE the " + object.toUpperCase() + " with you.";
			} else {//object is not in the room
				takeString = "You see no " + object.toUpperCase() + " in the room to TAKE.";
			}
		} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(object)) {//if the enemy is part of the game
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsEnemy(object)) {//if the enemy is in the current room
				takeString = "The " + object.toUpperCase() + " won't fit in your pocket.";
			} else {//enemy not in this room
				takeString = "You don't see any " + object.toUpperCase() + " to pick up.";
			}
		} else if(GameSpell.spells != null && GameSpell.spells.containsKey(object)) {//if the spell is part of the game
			if(gameState.spellbook != null && gameState.spellbook.containsKey(object)) {//if the player knows the spell
				takeString = "You can't TAKE " + object.toUpperCase() + ", but you can try to CAST it.";
			} else {//player doesn't know the spell
				takeString = "That's not how you learn new Spells.";
			}
		} else {//object is not part of the game
			takeString = object.toUpperCase() + "? Never heard of it.";
		}
		return takeString;
	}

	/**
	 * test(String) Reads in and executes a text File containing Game COMMANDs 
	 * @param string The full Path of the File to read and execute
	 */
	private static void test(String string) {
		System.out.println("TESTing via loaded Files not yet implemented.");
	}

	/**
	 * unEquip(String) Attempts to unequip the indicated object if it is equipped
	 * @return A String representing the success or failure of the unequip attempt
	 */
	private static String unEquip(String object) {
		String unEquipString = "";
		if(object.equals("room")) {
			unEquipString = "How does one doff a whole ROOM?";
		} else if(gameState.inInventory) {//if player is looking at their inventory
			if(gameState.inventory.containsKey(object)) {//if the player is carrying the specified object
				if(gameState.heldItem == object) {//if the player is HOLDING this object
					gameState.heldItem = "";//stop holding the object
					unEquipString = "You put the " + object.toUpperCase() + " away.";//TODO: implement onUnEquip()
				} else {//unequip the specified object, if able
					GameItem itemToUnEquip = gameState.inventory.get(object);
					if(!itemToUnEquip.isEquipped) {//item isn't even equipped
						unEquipString = "You don't currently have " + object.toUpperCase() + " equipped.";
					} else {
						switch (itemToUnEquip.equipSlot) {//figure out what equipment slot the item is currently in
						case GameItem.BACK:
							GameState.equipSlot_BACK = null;//unequip the specified item
							unEquipString = "You slip the " + object.toUpperCase() + " from across your back";
							break;
						case GameItem.BELT:
							GameState.equipSlot_BELT = null;
							unEquipString = "You unclasp the " + object.toUpperCase() + " from your waist";
							break;
						case GameItem.BOOTS:
							GameState.equipSlot_BOOTS = null;
							unEquipString = "You unfasten the " + object.toUpperCase() + " from around your ankles";
							break;
						case GameItem.CUIRASS:
							GameState.equipSlot_CUIRASS = null;
							unEquipString = "You unbuckle the " + object.toUpperCase() + " from your torso";
							break;
						case GameItem.GAUNTLETS:
							GameState.equipSlot_GAUNTLETS = null;
							unEquipString = "You pull the " + object.toUpperCase() + " from your bare hands";
							break;
						case GameItem.GREAVES:
							GameState.equipSlot_GREAVES = null;
							unEquipString = "You unstrap the " + object.toUpperCase() + " from your legs";
							break;
						case GameItem.HELM:
							GameState.equipSlot_HELM = null;
							unEquipString = "You pull the " + object.toUpperCase() + " off your head";
							break;
						case GameItem.MAIN_HAND:
							GameState.equipSlot_MAIN_HAND = null;
							unEquipString = "You lower the " + object.toUpperCase() + " in your main hand";
							break;
						case GameItem.OFF_HAND:
							GameState.equipSlot_OFF_HAND = null;
							unEquipString = "You lower the " + object.toUpperCase() + " in your off hand";
							break;
						case GameItem.PANTS:
							GameState.equipSlot_PANTS = null;
							unEquipString = "You pull off the " + object.toUpperCase();
							break;
						case GameItem.SHIRT:
							GameState.equipSlot_SHIRT = null;
							unEquipString = "You pull the " + object.toUpperCase() + " over your head";
							break;
						case GameItem.SHOES:
							GameState.equipSlot_SHOES = null;
							unEquipString = "You kick off the " + object.toUpperCase() + " from your feet";
							break;
						case GameItem.RING:
							GameState.equipSlot_RING = null;
							unEquipString = "You slip the " + object.toUpperCase() + " from your index finger";
							break;
						case GameItem.AMULET:
							GameState.equipSlot_AMULET = null;
							unEquipString = "You untie the " + object.toUpperCase() + " from around your neck";
							break;
						case GameItem.EARRING:
							GameState.equipSlot_EARRING = null;
							unEquipString = "You unpin the " + object.toUpperCase() + " from yout ear";
							break;
						case GameItem.CLOAK:
							GameState.equipSlot_CLOAK = null;
							unEquipString = "You remove the " + object.toUpperCase() + " from around your shoulders";
							break;
						default:
							gameState.heldItem = null;
							unEquipString = "You take the " + object.toUpperCase();
							break;
						}
						unEquipString += " and put it in your bag.";
						itemToUnEquip.isEquipped = false;
						gameState.inventory.replace(object, itemToUnEquip);
					}
				}
			}
		} else if(GameItem.items != null && GameItem.items.containsKey(object)) {//if the object is a valid game item
			if(gameState.heldItem == object) {//if the player is holding the item
				unEquipString = "You put your " + object.toUpperCase() + " away.";
				gameState.heldItem = "";
			} else if(gameState.inventory.containsKey(object)) {//item is in player's inventory
				GameItem item = gameState.inventory.get(object);
				if(!item.isEquipped) {
					unEquipString = "You don't have " + object.toUpperCase() + " equipped.";
				} else {
					unEquipString = "It will take a while to UNEQUIP " + object.toUpperCase() + ". You should OPEN your inventory to do this.";
				}
			} else if(gameState.visitedRooms.get(gameState.currentRoomId).containsItem(object)) {//object is in the room
				unEquipString = "You don't even have the " + object.toUpperCase() + ". It's over there!";
			} else {//object is not in the room
				unEquipString = "There are no " + object.toUpperCase() + "s in sight.";
			}
		} else if(GameObject.objects != null && GameObject.objects.containsKey(object)) {//if the object is a valid game object
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsObject(object)) {//if the object is in the room
				unEquipString = "You can't EQUIP or UNEQUIP a " + object.toUpperCase() + ".";
			} else {//object is not in the room
				unEquipString = "You have no " + object.toUpperCase() + " to UNEQUIP.";
			}
		} else if(GameEnemy.enemies != null && GameEnemy.enemies.containsKey(object.toUpperCase())) {//if the object is a valid game enemy
			if(gameState.visitedRooms.get(gameState.currentRoomId).containsEnemy(object)) {//if the enemy is in the room
				unEquipString = "You can't EQUIP or UNEQUIP a " + object.toUpperCase() + ".";
			} else {//enemy is not in the room
				unEquipString = "Do you want the " + object.toUpperCase() + " to just go away?";
			}
		} else if(GameSpell.spells != null && GameSpell.spells.containsKey(object)) {//if the object is a valid game spell
			if(gameState.spellbook.containsKey(object)) {//if the Player knows the spell
				unEquipString = "You clear your mind of the knowledge of the " + object.toUpperCase() + " spell.";
			} else {//Player doesn't know the spell
				unEquipString = "Even if you knew " + object.toUpperCase() + ", you wouldn't be able to UNEQUIP it.";
			}
		} else {//unknown object
			unEquipString = object.toUpperCase() + "? Never heard of it.";
		}
		return unEquipString;
	}
}
