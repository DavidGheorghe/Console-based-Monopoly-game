import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import Players.*;
import Properties.Property;

public class Game {
	
	// returns a random number between 1 and 6
	public static int dieNumber() {
		Random rand = new Random();
		int result = rand.nextInt(6);
		
		return result + 1;
	}
	
	static Board board = new Board();
	static Board boardInventory = new Board();
	static ArrayList<Player> players = new ArrayList<>();
	static ArrayList<String> colors = new ArrayList<>(Arrays.asList("Red", "Green", "Blue", "Brown"));
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args){
		System.out.println("Number of players: ");
		
		
		int nbOfPlayers;
		
		//check if the number of players is between 2 and 4 because the maximum number of player is 4 and minimum is 2
		do {
			nbOfPlayers = Integer.valueOf(sc.nextLine());
			if(nbOfPlayers > 4 || nbOfPlayers < 2) {
				System.out.println("The number of players must be between 2 and 4. Choose a number between 2 and 4.");
			}
		}while(nbOfPlayers > 4 || nbOfPlayers < 2);
		
		
		//each player chooses a color
		for(int i = 0; i < nbOfPlayers; i++) {
			System.out.println("Choose a player: ");
			
			printColors(colors);
			
			String choice;
			do {
				choice = sc.nextLine();
				if(!colors.contains(choice)) {
					System.out.println(choice + " is not available. Choose another color.");
				}
			}while(!colors.contains(choice));
			
			switch(choice) {
			case "Red":
				RedPlayer rp = new RedPlayer();
				colors.remove(choice);
				players.add(rp);
				break;
			case "Green":
				GreenPlayer gp = new GreenPlayer();
				colors.remove(choice);
				players.add(gp);
				break;
			case "Blue":
				BluePlayer blp = new BluePlayer();
				colors.remove(choice);
				players.add(blp);
				break;
			case "Brown":
				BrownPlayer brp = new BrownPlayer();
				colors.remove(choice);
				players.add(brp);
				break;
			}
		}
		
		for(Player pl: players) {
			System.out.println(pl);
		}
		
		startGame(players);
		sc.close();
	}
	
	public static void printColors(ArrayList<String> colors) {
		for(String color: colors) {
			System.out.print(color + " ");
		}
	}

	
	public static void startGame(ArrayList<Player> players) {	
		while(!gameOver()) {
			
			for(Player player: players) {
				
				System.out.println();
				System.out.println("-------------------");
				System.out.println("Player " + player.getName() + " turn.");	
				System.out.println();
				displayInventory(player);
				System.out.println(player.getName() + " balance: " + player.getBalance());
				System.out.println();
				
				boolean doubleDice = true;
				int nbOfDoubles = 0;//when a player gives 3 doubles in a row he goes to jail
				
				do {
				int diceNumber1 = dieNumber();
				int diceNumber2 = dieNumber();
				int diceNumber = diceNumber1 + diceNumber2;
				
				if(diceNumber1 != diceNumber2) {
					doubleDice = false;
					nbOfDoubles++;
				}
				
				//handle jail
				//the player has the option to pay a fine of 50 and get out of jail in first 2 rounds
				//if the players choose to not pay then he can get out of jail if he throws a double
				//if the dice is not a double then the games continue
				//in 3rd round in jail, if the dice is not a double then he must pay a 50 fine and continue
				if(player.isInJail()) {
					System.out.println("Player " + player.getName() + " is in jail.");
					player.setRoundsInJail(player.getRoundsInJail() + 1);
					if(player.getRoundsInJail() < 3) {
						System.out.println("Would you like to pay a fine of 50 to get out of jail? Yes/No");
						String userChoice = sc.nextLine();
						if(userChoice.equals("Yes")) {
							player.payJailTax();
							player.setInJail(false);
							System.out.println("Dice number: " + diceNumber1 + " " + diceNumber2);
						}else {
							System.out.println("Dice number: " + diceNumber1 + " " + diceNumber2);
							if(diceNumber1 == diceNumber2) {
								player.setInJail(false);
								//if the dice is a double the game continues as usual
							}else {
								continue;//if the dice is not a double then the player can't do anything while he is in jail
							}
						}
					}else {
						//3rd round in jail
						player.setInJail(false);
						player.setRoundsInJail(0);
						System.out.println("3rd round in jail.");
						System.out.println("Dice number: " + diceNumber1 + " " + diceNumber2);
						if(diceNumber1 == diceNumber2) {
							//if the dice is a double the game continues as usual
						}else {
							player.payJailTax();							
						}
					}

				}else {
					System.out.println("Dice number: " + diceNumber1 + " " + diceNumber2);
				}

				String previousSpaceName = board.getNameOfSpace(player.getCurrentPosition());
				int previousSpacePrice = board.getPriceOfSpace(player.getCurrentPosition());
				int previousPosition = player.getCurrentPosition();
				System.out.println("Current space: " + previousSpaceName + " index: " + player.getCurrentPosition() + 
						", price: " + previousSpacePrice);
								
				player.goForward(diceNumber);
				
				//get start bonus if the player did not land on start
				if(previousPosition + diceNumber > 40 && player.getCurrentPosition() != 0) {
					player.getStartBonus();
				}
				
				int currentSpacePrice = board.getPriceOfSpace(player.getCurrentPosition());
				String currentSpaceName = board.getNameOfSpace(player.getCurrentPosition());
				int currentSpaceRent = board.getRentSpace(player.getCurrentPosition());
				
				System.out.println("Go to: " + currentSpaceName + " index: " + player.getCurrentPosition() + 
					", price: " + currentSpacePrice);
				System.out.println();
				
				String userChoice;
				Property currentProperty = new Property(currentSpaceName, currentSpacePrice, currentSpaceRent);
				
				//check if the player owns the property
				if(player.getInventory().contains(currentProperty) || currentSpaceName.equals("JAIL") || currentSpaceName.equals("FREE PARKING")) {
					continue;
				}
				//check if the space is a property
				else if(isProperty(currentSpaceName)) {
					//check if the property is available, if yes then offer option to buy it
					if(boardInventory.propertyAvailabilty(currentProperty)) {
						System.out.println(currentSpaceName + "  is available. Would you like to buy it? Yes/No");
						userChoice = sc.nextLine();
						if(userChoice.equals("Yes") || userChoice.equals("yes")) {
							player.buyProperty(currentProperty);
							boardInventory.removeProperty(currentProperty);
						}
					}else {
						//pay rent
						//implement rent for stations and utilities properties
						System.out.println(currentProperty.getName() + " is owned by " + getPlayer(currentProperty));
						int rent = currentProperty.getRent();
						
						
						if(getNumberOfStationsOwned(getPlayer(currentProperty)) > 0) {
							//if the property is a station then the rent is 25 * number of stations owned by that player
							player.payRent(rent * getNumberOfStationsOwned(getPlayer(currentProperty)));
							getPlayer(currentProperty).recieveRentPay(rent * getNumberOfStationsOwned(getPlayer(currentProperty)));
						}else if(getNumberOfStationsOwned(player) > 0) {
							//if the player owns one utility then pay rent 4 times more than the number of dice
							//if the player owns both utilities then pay rent 10 times more than the number of dice
							if(getNumberOfStationsOwned(player) == 1) {
								
								player.payRent(4 * diceNumber);
								getPlayer(currentProperty).recieveRentPay(4 * diceNumber);
							}else {
								player.payRent(10 * diceNumber);
								getPlayer(currentProperty).recieveRentPay(10 * diceNumber);
							}
						}else {
							//if the property is not a station or a utility(electric or water) then pay rent 10 times more in order to finish the game faster xd
							player.payRent(rent * 10);
							getPlayer(currentProperty).recieveRentPay(currentProperty.getRent() * 10);
						}
						
					}
					
				}else if(currentSpaceName.equals("START")) {
					player.getStartBonus();
				}else if(currentSpaceName.equals("CHANCE") || currentSpaceName.equals("COMMUNITY CHEST")) {
					//implement a class or two with chance and community chest cards
				}else if(currentSpaceName.equals("INCOME TAX")) {
					player.payIncomeTax();
				}else if(currentSpaceName.equals("SUPER TAX")) {
					player.paySuperTax();
				}else if(currentSpaceName.equals("GO TO JAIL")) {
					player.goToJail();
				}
				
				displayInventory(player);
				System.out.println(player.getName() + " balance: " + player.getBalance());
				System.out.println();
				}while(doubleDice && nbOfDoubles < 3 && player.isInJail() == false);
			
				if(nbOfDoubles == 3) {
					player.goToJail();
				}
				System.out.println("-------------------");
				System.out.println();
			}	
		}
	}
	
	/**
	 * @param pl
	 * the method display the inventory of player @param pl
	 */
	public static void displayInventory(Player pl) {
		System.out.print(pl.getName() + " inventory: ");
		for(Property pr: pl.getInventory()) {
			System.out.print(pr.getName() + "; ");
		}

		System.out.println();
	}
		
	/**
	 * @param pr
	 * @return the player who owns the property pr
	 */
	public static Player getPlayer(Property pr) {
		Player ply = new Player();
		for(Player pl: players) {
			if(pl.getInventory().contains(pr)) {
				ply = pl;
			}
		}
		return ply;
	}
	
	/**
	 * @param pl
	 * @return the number of stations owned by the player pl
	 */
	public static int getNumberOfStationsOwned(Player pl) {
		int nb = 0;
		for(Property pr: pl.getInventory()) {
			if(pr.getName().equals("KINGS CROSS STATION") || pr.getName().equals("MARYLEBONE STATION") || 
					pr.getName().equals("FENCHURCH ST. STATION") || pr.getName().equals("LIVERPOOL ST. STATION")) {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @param pl
	 * @return the number of utilities(electric and water property) owned by player pl
	 */
	public static int getNumberOfUtilitiesOwned(Player pl) {
		int nb = 0;
		for(Property pr: pl.getInventory()) {
			if(pr.getName().equals("ELECTRIC COMPANY") || pr.getName().equals("WATER WORKS")) {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @param spaceName
	 * @return true if the space with the name @param spaceName is a property, false otherwise
	 */
	public static boolean isProperty(String spaceName) {
		return !(spaceName.equals("START") || spaceName.equals("COMMUNITY CHEST") ||
				spaceName.equals("INCOME TAX") || spaceName.equals("CHANCE") ||
				spaceName.equals("JAIL") || spaceName.equals("FREE PARKING") ||
				spaceName.equals("GO TO JAIL") || spaceName.equals("SUPER TAX"));
	}

	/**
	 * @return true if the game is over, false otherwise
	 */
	public static boolean gameOver() {
		// a player wins when the other players does not have money or properties
		int nbOfPlayersInGame = players.size();
		
		for(Player pl: players) {
			if(nbOfPlayersInGame == 1) {
				return true;
			}
			if(pl.getBalance() <= 0 && pl.getInventory().isEmpty()) {
				nbOfPlayersInGame--;
			}
		}
		if(nbOfPlayersInGame == 1) {
			return true;
		}
		return false;
	}
}
