package Players;
import Properties.Property;

public interface IPlayer {
	
	public void goForward(int nbOfSpaces);
	public void buyProperty(Property pr);
	public void payIncomeTax();
	public void paySuperTax();
	public void getStartBonus();
	public void goToJail();
	public void payJailTax();
	public void payRent(int rent);
	public void recieveRentPay(int rent);
}
