/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Models;

/**
 *
 * @author Eli Núñez
 */
public class DataTableModel {
    String id, value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataTableModel(String id, String value) {
        this.id = id;
        this.value = value;
    }
    
}
