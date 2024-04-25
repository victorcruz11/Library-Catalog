package main;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Book {		//represents a book in the system
    private int bookID;
    private String bookTitle;		//these store the books properties
    private String bookAuthor;
    private String bookGenre;
    private LocalDate lastBorrowedDate;		//date of when the book was last borrowed
    private boolean isBorrowed;		//indicates if the book is currently borrowed


    public Book(int bookID, String bookTitle, String bookAuthor, String bookGenre, LocalDate lastBorrowedDate, boolean isBorrowed) {		//this initializes a new Book with given properties
        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookGenre = bookGenre;
        this.lastBorrowedDate = lastBorrowedDate;
        this.isBorrowed = isBorrowed;
    }
    
    //overdue after 30 days
    private static final float EXTRA_DAY_FEE = 1.33f;		//this is a fee for every day overdue
    private static final float ORIGIN_FEE = 5.0f;		//base fee 
    private static final int BEFORE_OVERDUE_QTY = 31;		//quantity of days before its overdue

    
    //the following will be getters and setters methods for book's properties
    public int getId() {		//gets the ID
        return bookID;
    }

    public void setId(int bookID) {		//sets the ID
        this.bookID = bookID;
    }

    public String getTitle() {		//gets the book's title
        return bookTitle;
    }

    public void setTitle(String bookTitle) {		//sets the book title
        this.bookTitle = bookTitle;
    }

    public String getAuthor() {		//gets the authors name
        return bookAuthor;
    }

    public void setAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getGenre() {		//gets the books category
        return bookGenre;
    }

    public void setGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    public LocalDate getLastCheckOut() {
        return lastBorrowedDate;
    }

    public void setLastCheckOut(LocalDate lastBorrowedDate) {
        this.lastBorrowedDate = lastBorrowedDate;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }
    
    public boolean isCheckedOut() {
        return isBorrowed;
    }

    public void setCheckedOut(boolean checkedOut) {
        isBorrowed = checkedOut;
    }

    @Override
    public String toString() {
        return String.format("%s BY %s", getTitle().toUpperCase(), getAuthor().toUpperCase());
    }
 // this calculates and returns any overdue fees for the book
    public float calculateFees() {
        if (!isBorrowed || lastBorrowedDate == null) {
            return 0;
        }
// calculate how many days the book is overdue
        long daysOverdue = ChronoUnit.DAYS.between(lastBorrowedDate, LocalDate.now()) - BEFORE_OVERDUE_QTY;
        return daysOverdue > 0 ? ORIGIN_FEE + (daysOverdue * EXTRA_DAY_FEE) : 0;
    }

}