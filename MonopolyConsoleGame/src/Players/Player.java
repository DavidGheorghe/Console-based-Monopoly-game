package Players;
import java.util.ArrayList;

import Properties.Property;

public class Player implements IPlayer{
	
	private String name;
	private int currentPosition;
	private ArrayList<Property> inventory;
	private int balance;
	private boolean isInJail;
	private int roundsInJail;
	
	public Player() {
		this.currentPosition = 0;
		this.inventory = new ArrayList<>();
		this.balance = 1500;
		this.isInJail = false;
		this.roundsInJail = 0;
	}
	
	@Override
	public void goForward(int nbOfSpaces) {
		
		if(this.currentPosition + nbOfSpaces > 39) {
			this.currentPosition = (this.currentPosition + nbOfSpaces) - 40;
			return;
		}
		
		this.currentPosition = this.currentPosition + nbOfSpaces;
	}

	@Override
	public void buyProperty(Property pr) {
		if(this.balance - pr.getPrice() < 0) {
			System.out.println("Not enough money.");
			return;
		}
		this.inventory.add(pr);
		this.balance = this.balance - pr.getPrice();
	}
	
	@Override
	public void payIncomeTax() {
		this.balance = this.balance - 200;
	}
	@Override
	public void paySuperTax() {
		this.balance = this.balance - 100;
	}
	@Override
	public void getStartBonus() {
		this.balance = this.balance + 200;
	}
	@Override
	public void goToJail() {
		setCurrentPosition(10);
		setInJail(true);
	}
	@Override
	public void payJailTax() {
		this.balance = this.balance - 50;
	}
	@Override
	public void payRent(int rent) {
		this.balance = this.balance - rent;
	}
	@Override
	public void recieveRentPay(int rent) {
		this.balance = this.balance + rent;
	}

	public String getName() {
		return this.name;
	}
	
	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public ArrayList<Property> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Property> inventory) {
		this.inventory = inventory;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public boolean isInJail() {
		return isInJail;
	}

	public void setInJail(boolean isInJail) {
		this.isInJail = isInJail;
	}

	public int getRoundsInJail() {
		return roundsInJail;
	}

	public void setRoundsInJail(int roundsInJail) {
		this.roundsInJail = roundsInJail;
	}

}
