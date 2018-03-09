<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="base_url" value="<%=request.getContextPath()%>" />

<template:base>
    <jsp:attribute name="title">
        Benutzer bearbeiten
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/login.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/offers/"/>">Übersicht</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <div class="container">
            <form method="post" class="stacked">
                <div class="column">
                    <%-- CSRF-Token --%>
                    <input type="hidden" name="csrf_token" value="${csrf_token}">
        
                    <div>
                        <h1> Anschrift </h1>
                    </div>
                                        <%-- Eingabefelder --%>
                    <label for="edit_name">
                        Vor- und Nachname:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="edit_name" value="${signup_form.values["edit_name"][0]}">
                    </div>
                    
                    <label for="edit_anschrift">
                        Straße und Hausnummer:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="edit_adresse" value="${signup_form.values["edit_adresse"][0]}">
                    </div>
                    
                    <label for="edit_plz" for ="edit_stadt">
                        Postleitzahl und Ort:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="number" name="signup_plz" value="${signup_form.values["edit_plz"][0]}">
                        <input type="text" name="signup_stadt" value="${signup_form.values["edit_stadt"][0]}">
                    </div>
                    
                    <div>
                        <h1> Kontaktdaten </h1>
                    </div>
                    
                    <label for="edit_tel">
                        Telefonnummer:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="number" name="edit_tel" value="${signup_form.values["edit_tel"][0]}">
                    </div>
                    
                    <label for="signup_email">
                        E-Mail:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="edit_email" value="${signup_form.values["edit_email"][0]}">
                    </div>


                    <%-- Button zum Abschicken --%>
                    <div class="side-by-side">
                        <button class="icon-pencil" type="submit">
                            Speichern
                        </button>
                    </div>
                </div>

                <%-- Fehlermeldungen --%>
                <c:if test="${!empty signup_form.errors}">
                    <ul class="errors">
                        <c:forEach items="${signup_form.errors}" var="error">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </c:if>
            </form>
        </div>
    </jsp:attribute>
</template:base>