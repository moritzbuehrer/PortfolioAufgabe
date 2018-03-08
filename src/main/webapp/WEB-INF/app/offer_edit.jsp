<%-- 
    Copyright © 2018 Dennis Schulmeister-Zimolong

    E-Mail: dhbw@windows3.de
    Webseite: https://www.wpvs.de/

    Dieser Quellcode ist lizenziert unter einer
    Creative Commons Namensnennung 4.0 International Lizenz.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        <c:choose>
            <c:when test="${edit}">
                Angebot bearbeiten
            </c:when>
            <c:otherwise>
                Angebot anlegen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/offer_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/offers/"/>">Übersicht</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="offer_creator">Ersteller:</label>
                <div class="side-by-side">
                    <input type="text" name="offer_creator" value="${offer_form.values["offer_creator"][0]}" readonly="readonly">
                </div>

                <label for="offer_category">Kategorie:</label>
                <div class="side-by-side">
                    <select name="offer_category">
                        <option value="">Keine Kategorie</option>

                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}" ${offer_form.values["offer_category"][0] == category.id ? 'selected' : ''}>
                                <c:out value="${category.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="offer_date_of_creation">
                    Erstellt am:
                    <span class="required">*</span>
                </label>
                
                <div class="side-by-side">
                    <input type="text" name="offer_date_of_creation" readonly="readonly" value="${offer_form.values["offer_date_of_creation"][0]}">
                </div>
                
                <label for="offer_type">
                    Typ:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="offer_type">
                        <c:forEach items="${types}" var="type">
                            <option value="${type}" ${offer_form.values["offer_type"][0] == type ? 'selected' : ''}>
                                <c:out value="${type.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="offer_title">
                    Titel:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="offer_title" value="${offer_form.values["offer_title"][0]}">
                </div>

                <label for="offer_description">
                    Beschreibung:
                </label>
                <div class="side-by-side">
                    <textarea name="offer_description"><c:out value="${offer_form.values['offer_description'][0]}"/></textarea>
                </div>
                
                <label for="offer_price">
                    Preis:
                </label>
                <div class="side-by-side">
                    <input name="offer_price" value="${offer_form.values['offer_price'][0]}"/>
                </div>
                
                <label for="offer_type">
                    Preistyp:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="offer_type_of_price">
                        <c:forEach items="${price_types}" var="type">
                            <option value="${type}" ${offer_form.values["offer_type_of_price"][0] == type ? 'selected' : ''}>
                                <c:out value="${type.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <%-- Button zum Abschicken --%>
                <div class="side-by-side">
                    <button class="icon-pencil" type="submit" name="action" value="save">
                        Sichern
                    </button>

                    <c:if test="${edit}">
                        <button class="icon-trash" type="submit" name="action" value="delete">
                            Löschen
                        </button>
                    </c:if>
                </div>
            </div>

            <%-- Fehlermeldungen --%>
            <c:if test="${!empty offer_form.errors}">
                <ul class="errors">
                    <c:forEach items="${offer_form.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>