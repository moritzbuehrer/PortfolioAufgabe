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

import dhbwka.wwi.vertsys.javaee.minimarkt.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.minimarkt.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.minimarkt.jpa.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet für die Startseite /index.html. Hier wird der Anwender einfach auf
 * die Übersichtsseite weitergeleitet. Falls er noch nicht eingeloggt ist,
 * sorgt der Applikationsserver von alleine dafür, zunächst die Loginseite
 * anzuzeigen.
 */
@WebServlet(urlPatterns = {"/app/user/edit/"})
public class EditUser extends HttpServlet {
    
    @EJB
    ValidationBean validationBean;
            
    @EJB
    UserBean userBean;
    
    /**
     * GET-Anfrage: Seite anzeigen
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        
        request.setAttribute("name", this.userBean.getCurrentUser().getName());
        request.setAttribute("adresse", this.userBean.getCurrentUser().getAdresse());
        request.setAttribute("stadt", this.userBean.getCurrentUser().getStadt());
        request.setAttribute("plz", this.userBean.getCurrentUser().getPlz());
        request.setAttribute("tel", this.userBean.getCurrentUser().getTel());
        request.setAttribute("email", this.userBean.getCurrentUser().getEmail());
        
        request.getRequestDispatcher("/WEB-INF/app/edit_user.jsp").forward(request, response);
        
        HttpSession session = request.getSession();
        session.removeAttribute("edit_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<String> errors = new ArrayList<>();
        
        // Formulareingaben auslesen
        request.setCharacterEncoding("utf-8");
        
        String name = request.getParameter("edit_name");
        String adresse = request.getParameter("edit_adresse");
        String stadt = request.getParameter("edit_stadt");
        String plz = request.getParameter("edit_plz");
        String tel = request.getParameter("edit_tel");
        String email = request.getParameter("edit_email");
        
        User user = this.userBean.getCurrentUser();
        
        if(!name.isEmpty()){
            user.setName(name);   
        }else{
            errors.add("Name darf nicht leer sein");
        }
        if(!adresse.isEmpty()){
            user.setAdresse(adresse);  
        }else{
            errors.add("Adresse darf nicht leer sein");
        }
        if(!stadt.isEmpty()){
            user.setStadt(stadt);  
        }else{
            errors.add("Stadt darf nicht leer sein");
        }
        if(!plz.isEmpty()){
            user.setPlz(plz);  
        }else{
            errors.add("Postleitzahl darf nicht leer sein");
        }
        if(!tel.isEmpty()){
            user.setTel(tel);  
        }else{
            errors.add("Telefonnummer darf nicht leer sein");
        }
        if(!email.isEmpty()){
            user.setEmail(email);  
        }else{
            errors.add("Email darf nicht leer sein");
        }
        
        this.validationBean.validate(user, errors);
        
        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            this.userBean.update(user);
            response.sendRedirect(WebUtils.appUrl(request, "/app/offers/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("edit_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
        
    }
    
    
}