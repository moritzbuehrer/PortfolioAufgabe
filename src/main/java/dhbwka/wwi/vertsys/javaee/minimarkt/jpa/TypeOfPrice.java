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

/**
 *
 * @author D067617
 */
public enum TypeOfPrice {
    FIXED, NEGOTIABLE;
    
    public String getLabel() {
        switch (this) {
            case FIXED:
                return "Festpreis";
            case NEGOTIABLE:
                return "Verhandlungsbasis";
            default:
                return this.toString();
        }
    }
}
