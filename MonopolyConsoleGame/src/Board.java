import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Properties.Property;

public class Board {
	private ArrayList<Property> board;
	public Board() {
		this.board = new ArrayList<>(40); // an array list with capacity of 40 because a monopoly board has 40 spaces

		//using a CSV format, the names and price of spaces will be read from a file named 'Board.txt'  
		try {
			File file = new File("src/Board.txt");
			Scanner fileInput = new Scanner(file);
			
			while(fileInput.hasNextLine()) {
				
				String line = fileInput.nextLine();
				String[] values = line.split(",");
				
				board.add(new Property(values[0], Integer.valueOf(values[1]), Integer.valueOf(values[2])));
			}
			
			fileInput.close();
		}catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String getNameOfSpace(int i) {
		return this.board.get(i).getName();
	}
	
	public boolean propertyAvailabilty(Property pr) {
		return board.contains(pr);
	}
	
	public void removeProperty(Property pr) {
		board.remove(pr);
		return;
	}
	
	public int getPriceOfSpace(int i) {
		return this.board.get(i).getPrice();
	}
	
	public int getRentSpace(int i) {
		return this.board.get(i).getRent();
	}
	
}
