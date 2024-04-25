package main;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import data_structures.ArrayList;
import data_structures.DoublyLinkedList;
import data_structures.SinglyLinkedList;
import interfaces.FilterFunction;
import interfaces.List;


public class LibraryCatalog {
    
    // List to store library books
    private ArrayList<Book> bookList;
    // List to store library clientList
    private ArrayList<User> clientList;

    public LibraryCatalog() throws IOException { 	//initializes library catalog
        this.bookList = loadBooksFromData();
        this.clientList = loadClientsFromData();
    }
    
    private ArrayList<Book> loadBooksFromData() throws IOException {
        ArrayList<Book> books = new ArrayList<>();
        
        try (BufferedReader bookFile = new BufferedReader(new FileReader("data/catalog.csv"))) {
            
            bookFile.readLine();

            String line;
            while ((line = bookFile.readLine()) != null) {
                String[] data = line.split(",");
                
                int id = Integer.parseInt(data[0]);
                String title = data[1];
                String author = data[2];
                String genre = data[3];
                LocalDate lastCheckoutDate = LocalDate.parse(data[4]);
                boolean isCheckedOut = Boolean.parseBoolean(data[5]);

                books.add(new Book(id, title, author, genre, lastCheckoutDate, isCheckedOut));
            }
        }

        return books;
    }


    private ArrayList<User> loadClientsFromData() throws IOException {
        ArrayList<User> clientList = new ArrayList<>();
        
        try (BufferedReader userFile = new BufferedReader(new FileReader("data/user.csv"))) {
            
            userFile.readLine(); // Read the header

            String line;
            while ((line = userFile.readLine()) != null) {
                String[] userData = line.split(",");
                
                if (userData.length < 3) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }
                
                int id = Integer.parseInt(userData[0].trim());
                String fullName = userData[1].trim();
                
                ArrayList<Book> checkedOutBooks = new ArrayList<>();
                String[] bookIds = userData[2].replace("{", "").replace("}", "").split(" ");
                for (String bookId : bookIds) {
                    if (!bookId.trim().isEmpty()) {
                        checkedOutBooks.add(getBookById(Integer.parseInt(bookId.trim())));
                    }
                }
                
                clientList.add(new User(id, fullName, checkedOutBooks));
            }
        }

        return clientList;
    }


    public ArrayList<Book> getCatalog() {
        return bookList;
    }

    public ArrayList<User> getUsers() {
        return clientList;
    }

    public void addBook(String title, String author, String genre) {
        int id = bookList.size() + 1;
        LocalDate currentDate = LocalDate.parse("2023-09-15");
        Book newBook = new Book(id, title, author, genre, currentDate, false);
        bookList.add(newBook);
    }

    public void removeBook(int id) {
        Book bookToRemove = getBookById(id);
        if (bookToRemove != null) {
            bookList.remove(bookToRemove);
        }
    }

    public boolean checkOutBook(int id) {
        Book book = getBookById(id);
        
        if (book == null) {
            return false;
        }

        if (!book.isCheckedOut()) {
            book.setLastCheckOut(LocalDate.parse("2023-09-15"));
            book.setCheckedOut(true);
            return true;
        }

        return false;
    }


    public boolean returnBook(int id) {
        Book book = getBookById(id);
        
        if (book == null) {
            return false;
        }
        
        if (book.isCheckedOut()) {
            book.setCheckedOut(false);
            return true;
        }

        return false;
    }


    public boolean getBookAvailability(int id) {
        Book book = getBookById(id);
        return book != null && !book.isCheckedOut();
    }

    public int bookCount(String title) {
        int count = 0;
        for (Book book : bookList) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                count++;
            }
        }
        return count;
    }
    

    private Book getBookById(int id) {
        for (Book book : bookList) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }
    public ArrayList<Book> getBookCatalog() {
        return bookList;
    }
    public void generateReport() throws IOException {
        StringBuilder output = new StringBuilder("\t\t\t\tREPORT\n\n");
        output.append("\t\tSUMMARY OF BOOKS\n");
        output.append("GENRE\t\t\t\t\t\tAMOUNT\n");

        // Count the number of books per genre
        long adventureCount = 0;
        long fictionCount = 0;
        long classicsCount = 0;
        long mysteryCount = 0;
        long sciFiCount = 0;   

        for (Book book : bookList) {
            switch (book.getGenre()) {
                case "Adventure":
                    adventureCount++;
                    break;
                    
                case "Fiction":
                    fictionCount++;
                    break;
                    
                case "Classics":
                    classicsCount++;
                    break;
                    
                case "Science Fiction":
                    sciFiCount++;
                    break;
                    
                case "Mystery":
                    mysteryCount++;
                    break;
            }
        }

        // This section prints the amount of books per category
        output.append("Adventure\t\t\t\t\t" + adventureCount + "\n");
        output.append("Fiction\t\t\t\t\t\t" + fictionCount + "\n");
        output.append("Classics\t\t\t\t\t" + classicsCount + "\n");
        output.append("Mystery\t\t\t\t\t\t" + mysteryCount + "\n");
        output.append("Science Fiction\t\t\t\t\t" + sciFiCount + "\n");
        output.append("====================================================\n");
        output.append("\t\t\tTOTAL AMOUNT OF BOOKS\t" + bookList.size() + "\n\n");

        output.append("\t\t\tBOOKS CURRENTLY CHECKED OUT\n\n");

        for (Book book : bookList) {
            if (book.isCheckedOut()) {
                output.append(book.toString() + "\n");
            }
        }

        long checkedOutCount = 0;
        for (Book book : bookList) {
            if (book.isCheckedOut()) {
                checkedOutCount++;
            }
        }

        // This part prints the books that are currently checked out
        output.append("====================================================\n");
        output.append("\t\t\tTOTAL AMOUNT OF BOOKS\t" + checkedOutCount + "\n\n");

        output.append("\n\n\t\tUSERS THAT OWE BOOK FEES\n\n");

        for (User user : clientList) {
            float totalFees = user.getTotalFeesOwed();
            if (totalFees > 0) {
                output.append(String.format("%s\t\t\t\t\t$%.2f\n", user.getFullName(), totalFees));
            }
        }

        float totalDue = 0;
        for (User user : clientList) {
            totalDue += user.getTotalFeesOwed();
        }

        output.append("====================================================\n");
        output.append("\t\t\t\tTOTAL DUE\t$" + String.format("%.2f", totalDue) + "\n\n\n");

        System.out.println(output);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("report/report.txt"))) {
            bw.write(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/*
	 * BONUS Methods
	 * 
	 * You are not required to implement these, but they can be useful for
	 * other parts of the project.
	 */
    public List<Book> searchForBook(FilterFunction<Book> func) {
		return null;
	}
	
	public List<User> searchForUsers(FilterFunction<User> func) {
		return null;
	}
	


}