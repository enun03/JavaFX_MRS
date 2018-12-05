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
public class User {
    public final StringProperty ID, custID, roleID, userName, password, registeredDate;
    
    public String getID() {
        return ID.get();
    }

    public void setID(String id) {
        ID.set(id);
    }
    
    public StringProperty IDProperty() {
        return ID;
    }

    public String getCustID() {
        return custID.get();
    }    

    public void setCustID(String cid) {
        custID.set(cid);
    }
    
    public StringProperty CustIDProperty() {
        return custID;
    }
    
    public String getRoleID() {
        return roleID.get();
    }    

    public void setRoleID(String rid) {
        roleID.set(rid);
    }
    
    public StringProperty RoleIDProperty() {
        return roleID;
    }
    
    public String getUserName() {
        return userName.get();
    }    

    public void setUserName(String un) {
        userName.set(un);
    }
    
    public StringProperty UserNameProperty() {
        return userName;
    }
    
    public String getPassword() {
        return password.get();
    }    

    public void setPassword(String pass) {
        password.set(pass);
    }
    
    public StringProperty PasswordProperty() {
        return password;
    }
    
    public String getRegisteredDate() {
        return registeredDate.get();
    }    

    public void setRegisteredDate(String rd) {
        registeredDate.set(rd);
    }
    
    public StringProperty RegisteredDateProperty() {
        return registeredDate;
    }
    
     
    public User(String ID, String custID, String roleID, String userName, String password, String registeredDate) {
        this.ID = new SimpleStringProperty(ID);
        this.custID = new SimpleStringProperty(custID);
        this.roleID = new SimpleStringProperty(roleID);
        this.userName = new SimpleStringProperty(userName);
        this.password = new SimpleStringProperty(password);
        this.registeredDate = new SimpleStringProperty(registeredDate);
    }
}
