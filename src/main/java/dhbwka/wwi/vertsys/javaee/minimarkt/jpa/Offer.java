/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.minimarkt.jpa;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Eine zu erledigende Aufgabe.
 */
@Entity
public class Offer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "task_ids")
    @TableGenerator(name = "task_ids", initialValue = 0, allocationSize = 50)
    private long id;

    @ManyToOne
    @NotNull(message = "Das Angebot muss einem Ersteller zugewiesen sein.")
    private User creator;

    @ManyToOne
    private Category category;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private TypeOfOffer typeOfOffer;

    @Column(length = 50)
    @NotNull(message = "Der Titel darf nicht leer sein.")
    @Size(min = 1, max = 50, message = "Der Titel muss zwischen ein und 50 Zeichen lang sein.")
    private String title;

    @Lob
    @NotNull
    private String description;

    @NotNull(message = "Das Erstelldatum darf nicht leer sein.")
    private Date dateOfCreation;

    @NotNull
    private double price;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TypeOfPrice typeOfPrice;

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Offer() {
    }

    public Offer(User owner, Category category, String shortText, String longText, Date dueDate, Time dueTime) {
        this.creator = owner;
        this.category = category;
        this.title = shortText;
        this.description = longText;
        this.dateOfCreation = dueDate;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * @return the typeOfOffer
     */
    public TypeOfOffer getTypeOfOffer() {
        return typeOfOffer;
    }

    /**
     * @param typeOfOffer the typeOfOffer to set
     */
    public void setTypeOfOffer(TypeOfOffer typeOfOffer) {
        this.typeOfOffer = typeOfOffer;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the typeOfPrice
     */
    public TypeOfPrice getTypeOfPrice() {
        return typeOfPrice;
    }

    /**
     * @param typeOfPrice the typeOfPrice to set
     */
    public void setTypeOfPrice(TypeOfPrice typeOfPrice) {
        this.typeOfPrice = typeOfPrice;
    }

    //</editor-fold>
    
}
