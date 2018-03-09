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
        Übersicht
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/offer_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/offer/new/"/>">Angebot erstellen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/categories/"/>">Kategorien bearbeiten</a>
        </div>
        
        <div class="menuitem">
            <a href="<c:url value="/signup/edit"/>">Benutzerdaten bearbeiten</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_text" value="${param.search_text}" placeholder="Beschreibung"/>

            <select name="search_category">
                <option value="">Alle Kategorien</option>

                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}" ${param.search_category == category.id ? 'selected' : ''}>
                        <c:out value="${category.name}" />
                    </option>
                </c:forEach>
            </select>

            <select name="search_type">
                <option value="">Alle Typen</option>

                <c:forEach items="${types}" var="type">
                    <option value="${type}" ${param.search_type == type ? 'selected' : ''}>
                        <c:out value="${type.label}"/>
                    </option>
                </c:forEach>
            </select>

            <button class="icon-search" type="submit">
                Suchen
            </button>
        </form>

        <%-- Gefundene Aufgaben --%>
        <c:choose>
            <c:when test="${empty offers}">
                <p>
                    Es wurden keine Aufgaben gefunden. 🐈
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbwka.wwi.vertsys.javaee.minimarkt.web.WebUtils"/>
                
                <table>
                    <thead>
                        <tr>
                            <th>Bezeichnung</th>
                            <th>Kategorie</th>
                            <th>Benutzer</th>
                            <th>Angebotstyp</th>
                            <th>Preis</th>
                            <th>Preistyp</th>
                            <th>Datum</th>
                        </tr>
                    </thead>
                    <c:forEach items="${offers}" var="offer">
                        <tr>
                            <td>
                                <a href="<c:url value="/app/offer/${offer.id}/"/>">
                                    <c:out value="${offer.title}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${offer.category.name}"/>
                            </td>
                            <td>
                                <c:out value="${offer.creator.username}"/>
                            </td>
                            <td>
                                <c:out value="${offer.typeOfOffer.label}"/>
                            </td>
                            <td>
                                <c:out value="${offer.price}"/>
                            </td>
                            <td>
                                <c:out value="${offer.typeOfPrice.label}"/>
                            </td>
                            <td>
                                <c:out value="${utils.formatDate(offer.dateOfCreation)}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>