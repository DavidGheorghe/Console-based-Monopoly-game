package Properties;

public class Property{
	
	private String name;
	private int price;// all prices will be millions i.e. if a property costs 12 this means the property costs 12 millions
	private boolean available;
	private int rent;
	
	public Property(String name, int price, int rent) {
		this.name = name;
		this.price = price;
		this.rent = rent;
		this.available = true;
	}
	
	public boolean isAvailable() {
		return this.available;
	}
	
	public void setAvailabilty(boolean av) {
		this.available = av;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getRent() {
		return rent;
	}

	public void setRent(int rent) {
		this.rent = rent;
	}
	public String toString() {
		return "Name: " + this.name + " price: " + this.price;
	}
	
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(getClass() != o.getClass() || o == null) {
			return false;
		}
		
		Property pr = (Property)o;
		
		return this.name.equals(pr.getName()) && this.price == pr.getPrice();
	}
	
	
}
