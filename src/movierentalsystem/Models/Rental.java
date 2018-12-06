/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Models;

import java.sql.Date;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Eli Núñez
 */
public class Rental {
    public final StringProperty movieId, custId, firstName, lastName, movieTitle, rentedDate;
    //public final FloatProperty rate, totalCost;
    
//    public int getID() {
//        return ID.get();
//    }
//
//    public void setID(int id) {
//        ID.set(id);
//    }
//    
//    public IntegerProperty IDProperty() {
//        return ID;
//    }

    public String getMovieId() {
        return movieId.get();
    }

    public void setMovieId(String id) {
        movieId.set(id);
    }
    
    public StringProperty MovieIdProperty() {
        return movieId;
    }
    
    public String getMovieTitle() {
        return movieTitle.get();
    }

    public void setMovieTitle(String mt) {
        movieTitle.set(mt);
    }
    
    public StringProperty MovieTitleProperty() {
        return movieTitle;
    }
    
    public String getCustId() {
        return custId.get();
    }

    public void setCustId(String id) {
        custId.set(id);
    }
    
    public StringProperty CustIdProperty() {
        return custId;
    }
    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fn) {
        firstName.set(fn);
    }
    
    public StringProperty FirstNameProperty() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String ln) {
        lastName.set(ln);
    }
    
    public StringProperty LastNameProperty() {
        return lastName;
    }
    
    public String getRentedDate() {
        return rentedDate.get();
    }

    public void setRentedDate(String rd) {
        rentedDate.set(rd);
    }
    
    public StringProperty RentedDateProperty() {
        return rentedDate;
    }

    

//    public Date getReturnDate() {
//        return returnDate;
//    }
//
//    public void setReturnDate(Date returnDate) {
//        this.returnDate = returnDate;
//    }
//    
//    public int getDaysRented() {
//        return daysRented.get();
//    }
//
//    public void setDaysRented(int dr) {
//        daysRented.set(dr);
//    }
//    
//    public IntegerProperty DaysRentedProperty() {
//        return daysRented;
//    }
//    
//    public Float getRate() {
//        return rate.get();
//    }
//
//    public void setRate(Float r) {
//        rate.set(r);
//    }
//    
//    public FloatProperty RateProperty() {
//        return rate;
//    }
//    
//    public Float getTotalCost() {
//        return totalCost.get();
//    }
//
//    public void setTotalCost(Float tc) {
//        rate.set(tc);
//    }
//    
//    public FloatProperty TotalCostProperty() {
//        return totalCost;
//    }
    
    public Rental(String movieId, String movieTitle, String custId, String firstName, String lastName, String rentedDate) {
        this.movieId = new SimpleStringProperty(movieId);
        this.movieTitle = new SimpleStringProperty(movieTitle);
        this.custId = new SimpleStringProperty(custId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.rentedDate = new SimpleStringProperty(rentedDate);
    }
}
