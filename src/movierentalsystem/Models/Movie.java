/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierentalsystem.Models;

import java.sql.Blob;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 *
 * @author Eli Núñez
 */
public class Movie {
    public final StringProperty ID, title, director, writers, releaseDate, runningTime, rated, genre, cast, status;
    Blob image;
    
    public String getID() {
        return ID.get();
    }

    public void setID(String id) {
        ID.set(id);
    }
    
    public StringProperty IDProperty() {
        return ID;
    }

    public String getTitle() {
        return title.get();
    }    

    public void setTitle(String t) {
        title.set(t);
    }
    
    public StringProperty titleProperty() {
        return title;
    }
    
    public String getDirector() {
        return director.get();
    }    

    public void setDirector(String dir) {
        director.set(dir);
    }
    
    public StringProperty directorProperty() {
        return director;
    }
    
    public String getWriters() {
        return writers.get();
    }    

    public void setWriters(String wri) {
        writers.set(wri);
    }
    
    public StringProperty writersProperty() {
        return writers;
    }
    
    public String getReleaseDate() {
        return releaseDate.get();
    }    

    public void setReleaseDate(String wri) {
        releaseDate.set(wri);
    }
    
    public StringProperty releaseDateProperty() {
        return releaseDate;
    }
    
    public String getRunningTime() {
        return runningTime.get();
    }    

    public void setRunningTime(String rt) {
        runningTime.set(rt);
    }
    
    public StringProperty runningTimeProperty() {
        return runningTime;
    }
    
    public String getRated() {
        return rated.get();
    }    

    public void setRated(String rd) {
        rated.set(rd);
    }
    
    public StringProperty ratedProperty() {
        return rated;
    }
    
    public String getGenre() {
        return genre.get();
    }    

    public void setGenre(String gr) {
        genre.set(gr);
    }
    
    public StringProperty genreProperty() {
        return genre;
    }
    
    public String getCast() {
        return cast.get();
    }    

    public void setCast(String ca) {
        cast.set(ca);
    }
    
    public StringProperty castProperty() {
        return cast;
    }
    
    public String getStatus() {
        return status.get();
    }    

    public void setStatus(String st) {
        status.set(st);
    }
    
    public StringProperty statusProperty() {
        return status;
    }
    
    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
     
    public Movie(String ID, String title, String director, String writers, String releaseDate, String runningTime, String rated, String genre, String cast, String status, Blob image) {
        this.ID = new SimpleStringProperty(ID);
        this.title = new SimpleStringProperty(title);
        this.director = new SimpleStringProperty(director);
        this.writers = new SimpleStringProperty(writers);
        this.releaseDate = new SimpleStringProperty(releaseDate);
        this.runningTime = new SimpleStringProperty(runningTime);
        this.rated = new SimpleStringProperty(rated);
        this.genre = new SimpleStringProperty(genre);
        this.cast = new SimpleStringProperty(cast);
        this.status = new SimpleStringProperty(status);
        this.image = image;
    }
}
