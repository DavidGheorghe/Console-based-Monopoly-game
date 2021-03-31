package Players;

import Properties.Property;

public class RedPlayer extends Player{
	
	private String name;
	
	public RedPlayer() {
		this.name = "Red";
	}
	
	@Override
	public void buyProperty(Property pr) {
		if(super.getBalance() - pr.getPrice() < 0) {
			System.out.println("Player " + this.getName() + " does not have enough money.");
			return;
		}
		super.getInventory().add(pr);
		super.setBalance(super.getBalance() - pr.getPrice());
	}
	@Override
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.name;
	}

}
