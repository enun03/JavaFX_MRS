/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Eli Núñez
 */
public class Customer {
    public final StringProperty ID, firstName, lastName, address, city, zipCode, phone;
    
    public String getID() {
        return ID.get();
    }

    public void setID(String id) {
        ID.set(id);
    }
    
    public StringProperty IDProperty() {
        return ID;
    }

    public String getFirstName() {
        return firstName.get();
    }    

    public void setFirstName(String fn) {
        firstName.set(fn);
    }
    
    public StringProperty firstNameProperty() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName.get();
    }    

    public void setLastName(String ln) {
        lastName.set(ln);
    }
    
    public StringProperty lastNameProperty() {
        return lastName;
    }
    
    public String getAddress() {
        return address.get();
    }    

    public void setAddress(String addr) {
        address.set(addr);
    }
    
    public StringProperty addressProperty() {
        return address;
    }
    
    public String getCity() {
        return city.get();
    }    

    public void setCity(String ct) {
        city.set(ct);
    }
    
    public StringProperty cityProperty() {
        return city;
    }
    
    public String getZipCode() {
        return zipCode.get();
    }    

    public void setZipCode(String zc) {
        zipCode.set(zc);
    }
    
    public StringProperty zipCodeProperty() {
        return zipCode;
    }
    
    public String getPhone() {
        return phone.get();
    }    

    public void setPhone(String value) {
        phone.set(value);
    }
    
    public StringProperty phoneProperty() {
        return phone;
    }
     
    public Customer(String ID, String firstName, String lastName, String address, String city, String zipCode, String phone) {
        this.ID = new SimpleStringProperty(ID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.zipCode = new SimpleStringProperty(zipCode);
        this.phone = new SimpleStringProperty(phone);
    }
}
