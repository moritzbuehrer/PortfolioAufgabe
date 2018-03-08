/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.minimarkt.ejb;

import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.Category;
import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.Offer;
import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.TypeOfOffer;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Einfache EJB mit den üblichen CRUD-Methoden für Aufgaben
 */
@Stateless
@RolesAllowed("minimarkt-app-user")
public class OfferBean extends EntityBean<Offer, Long> { 
   
    public OfferBean() {
        super(Offer.class);
    }
    
    /**
     * Alle Aufgaben eines Benutzers, nach Fälligkeit sortiert zurückliefern.
     * @param username Benutzername
     * @return Alle Aufgaben des Benutzers
     */
    public List<Offer> findByUsername(String username) {
        return em.createQuery("SELECT o FROM Offer o WHERE o.creator.username = :username ORDER BY o.dateOfCreation")
                 .setParameter("username", username)
                 .getResultList();
    }
    

    public List<Offer> search(String search, Category category, TypeOfOffer typeOfOffer) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Task t
        CriteriaQuery<Offer> query = cb.createQuery(Offer.class);
        Root<Offer> from = query.from(Offer.class);
        query.select(from);

        // ORDER BY dueDate, dueTime
        query.orderBy(cb.asc(from.get("dateOfCreation")));
        
        // WHERE t.shortText LIKE :search
        if (search != null && !search.trim().isEmpty()) {
            query.where(cb.like(from.get("title"), "%" + search + "%"));
        }
        
        if (category != null) {
            query.where(cb.equal(from.get("category"), category));
        }
        
        if (typeOfOffer != null) {
            query.where(cb.equal(from.get("typeOfOffer"), typeOfOffer));
        }
        
        return em.createQuery(query).getResultList();
    }
}
