package main;

import data_structures.ArrayList;
import interfaces.List;

public class User {		//represents a user in the system
    private int userID;		//every user in unique
    private String completeName;		//complete name of the user
    private ArrayList<Book> borrowedBooksList;		//what books the user has borrowed

    public User(int userID, String completeName, ArrayList<Book> borrowedBooksList) {
    	// Constructor: Initializes a new User with given ID, name, and list of borrowed books.
        this.userID = userID;
        this.completeName = completeName;
        this.borrowedBooksList = borrowedBooksList;
    }

    public int getId() {		//this returns the users unique ID
        return userID;
    }

    public void setId(int userID) {		//sets a new ID for every user
        this.userID = userID;
    }

    public String getFullName() {		//returns the user's full name
        return completeName;
    }

    public void setFullName(String completeName) {
        this.completeName = completeName;
    }

    public ArrayList<Book> getCheckedOutList() {		//an array that returns the list of books the user has borrowed
        return borrowedBooksList;
    }

    public void setCheckedOutBooks(ArrayList<Book> borrowedBooksList) {		//this sets a new list of borrowed books for the user.
        this.borrowedBooksList = borrowedBooksList;
    }
    public float calculateTotalDueFees() {		//this calculates the total fees owed by the user for all borrowed books.
        float total = 0;
        for (int i = 0; i < borrowedBooksList.size(); i++) {
            Book book = borrowedBooksList.get(i);
            total += book.calculateFees();
        }
        return total;
    }


    public String getName() {		// its a method to return the user's full name.
        return getFullName();
    }


public float getTotalFeesOwed() {		//it calculates and returns the total fees owed by the user.
    float totalFees = 0.0f;
    for (Book book : borrowedBooksList) {
        totalFees += book.calculateFees();
    }
    return totalFees;

}
}


