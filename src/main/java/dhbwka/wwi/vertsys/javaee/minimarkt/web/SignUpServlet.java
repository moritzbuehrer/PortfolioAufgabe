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

import dhbwka.wwi.vertsys.javaee.minimarkt.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.minimarkt.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet für die Registrierungsseite. Hier kann sich ein neuer Benutzer
 * registrieren. Anschließend wird der auf die Startseite weitergeleitet.
 */
@WebServlet(urlPatterns = {"/signup/*"})
public class SignUpServlet extends HttpServlet {
    
    @EJB
    ValidationBean validationBean;
            
    @EJB
    UserBean userBean;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (request.getPathInfo().contains("edit")) {
            request.setAttribute("edit", true);
            
            User user = userBean.getCurrentUser();
            
            if (user != null) {
                FormValues formValues = this.createUserForm(user);

                HttpSession session = request.getSession();
                session.setAttribute("signup_form", formValues);
            }
            
        }
        
        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login/signup.jsp");
        dispatcher.forward(request, response);
        
        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("signup_form");
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Formulareingaben auslesen
        request.setCharacterEncoding("utf-8");
        
        String username     = request.getParameter("signup_username");
        String password1    = request.getParameter("signup_password1");
        String password2    = request.getParameter("signup_password2");
        String fullname     = request.getParameter("signup_fullname");
        String address      = request.getParameter("signup_address");
        String city         = request.getParameter("signup_city");
        String postalCode   = request.getParameter("signup_postal_code");
        String phoneNumber  = request.getParameter("signup_phone_number");
        String emailAddress = request.getParameter("signup_email_address");

        // Eingaben prüfen
        User user = new User(username, password1, fullname, address, postalCode, city, phoneNumber, emailAddress);
        List<String> errors = this.validationBean.validate(user);
        this.validationBean.validate(user.getPassword(), errors);
        
        if (!request.getPathInfo().contains("edit")) {
            if (password1 != null && password2 != null && !password1.equals(password2)) {
                errors.add("Die beiden Passwörter stimmen nicht überein.");
            }
            
            // Neuen Benutzer anlegen
            if (errors.isEmpty()) {
                try {
                    this.userBean.signup(username, password1, fullname, address, postalCode, city, phoneNumber, emailAddress);
                } catch (UserBean.UserAlreadyExistsException ex) {
                    errors.add(ex.getMessage());
                }
            }

            // Weiter zur nächsten Seite
            if (errors.isEmpty()) {
                // Keine Fehler: Startseite aufrufen
                request.login(username, password1);
                response.sendRedirect(WebUtils.appUrl(request, "/app/offers/"));
            } else {
                // Fehler: Formuler erneut anzeigen
                FormValues formValues = new FormValues();
                formValues.setValues(request.getParameterMap());
                formValues.setErrors(errors);

                HttpSession session = request.getSession();
                session.setAttribute("signup_form", formValues);

                response.sendRedirect(request.getRequestURI());
            }
        } else {
            this.userBean.updateData(fullname, address, city, phoneNumber, fullname, emailAddress);
            
            response.sendRedirect(request.getRequestURI());
        }
    }
    
    private FormValues createUserForm(User user) {
        Map<String, String[]> values = new HashMap<>();
        
        values.put("signup_fullname", new String[]{
            user.getFullname()
        });
        
        values.put("signup_address", new String[]{
            user.getAddress()
        });
        
        values.put("signup_city", new String[]{
            user.getCity()
        });
        
        values.put("signup_postal_code", new String[]{
            user.getPostalCode()
        });
        
        values.put("signup_phone_number", new String[]{
            user.getPhoneNumber()
        });
        
        values.put("signup_email_address", new String[]{
            user.getEmailAddress()
        });
        
        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }
}
