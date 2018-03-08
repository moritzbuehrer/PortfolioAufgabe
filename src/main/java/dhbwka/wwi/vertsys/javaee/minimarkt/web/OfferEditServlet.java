/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.minimarkt.web;

import dhbwka.wwi.vertsys.javaee.minimarkt.ejb.CategoryBean;
import dhbwka.wwi.vertsys.javaee.minimarkt.ejb.OfferBean;
import dhbwka.wwi.vertsys.javaee.minimarkt.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.minimarkt.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.Offer;
import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.OfferStatus;
import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.TypeOfOffer;
import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.TypeOfPrice;
import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.User;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/offer/*")
public class OfferEditServlet extends HttpServlet {

    @EJB
    OfferBean offerBean;

    @EJB
    CategoryBean categoryBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("types", TypeOfOffer.values());

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

        Offer offer = this.getRequestedOffer(request);
        request.setAttribute("edit", offer.getId() != 0);
                                
        if (session.getAttribute("offer_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("offer_form", this.createOfferForm(offer));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/offer_edit.jsp").forward(request, response);

        session.removeAttribute("offer_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        request.setCharacterEncoding("utf-8");
        
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "save":
                this.saveOffer(request, response);
                break;
            case "delete":
                this.deleteOffer(request, response);
                break;
        }
    }

    private void saveOffer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String offerCategory = request.getParameter("offer_category");
        String offerDateOfCreation = request.getParameter("offer_date_of_creation");
        String offerType = request.getParameter("offer_status");
        String offerTitle = request.getParameter("offer_title");
        String offerDescription = request.getParameter("offer_description");
        String offerPrice = request.getParameter("offer_price");
        String offerTypeOfPrice = request.getParameter("offer_type_of_price");

        Offer offer = this.getRequestedOffer(request);
        
        User user = userBean.getCurrentUser();
        
        if (!user.getUsername().equals(offer.getCreator().getUsername())) {
            errors.add("Nur der ersteller hat die Berechtigung ein Angebot zu bearbeiten.");
        }

        if (offerCategory != null && !offerCategory.trim().isEmpty()) {
            try {
                offer.setCategory(this.categoryBean.findById(Long.parseLong(offerCategory)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date dateOfCreation = WebUtils.parseDate(offerDateOfCreation);

        if (dateOfCreation != null) {
            offer.setDateOfCreation(dateOfCreation);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }

        try {
            offer.setTypeOfOffer(TypeOfOffer.valueOf(offerType));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Typ ist nicht vorhanden.");
        }
        
        try {
            offer.setTypeOfPrice(TypeOfPrice.valueOf(offerTypeOfPrice));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Preistyp ist nicht vorhanden.");
        }
        
        double price = 0.0;
        try {
            price = Double.parseDouble(offerPrice);
        } catch (Exception e) {
            errors.add("Parsen des Preises fehlgeschlagen.");
        }
        
        if (price < 0.0) {
            errors.add("Der Preis darf nicht negativ sein.");
        } 

        offer.setTitle(offerTitle);
        offer.setDescription(offerDescription);

        this.validationBean.validate(offer, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.offerBean.update(offer);
        }
        
        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/offers/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("offer_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    private void deleteOffer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Offer offer = this.getRequestedOffer(request);
        this.offerBean.delete(offer);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/offers/"));
    }

    private Offer getRequestedOffer(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Offer offer = new Offer();
        offer.setCreator(this.userBean.getCurrentUser());
        offer.setDateOfCreation(new Date(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String offerId = request.getPathInfo();

        if (offerId == null) {
            offerId = "";
        }

        offerId = offerId.substring(1);

        if (offerId.endsWith("/")) {
            offerId = offerId.substring(0, offerId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            offer = this.offerBean.findById(Long.parseLong(offerId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return offer;
    }

    private FormValues createOfferForm(Offer offer) {
        Map<String, String[]> values = new HashMap<>();

        values.put("offer_creator", new String[]{
            offer.getCreator().getUsername()
        });

        if (offer.getCategory() != null) {
            values.put("offer_category", new String[]{
                offer.getCategory().toString()
            });
        }

        values.put("offer_date_of_creation", new String[]{
            WebUtils.formatDate(offer.getDateOfCreation())
        });

        values.put("offer_type", new String[]{
            offer.getTypeOfOffer().toString()
        });

        values.put("offer_title", new String[]{
            offer.getTitle()
        });

        values.put("offer_description", new String[]{
            offer.getDescription()
        });
        
        values.put("offer_price", new String[]{
            Double.toString(offer.getPrice())
        });
        
        values.put("offer_type_of_price", new String[]{
            offer.getTypeOfPrice().toString()
        });
        

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
