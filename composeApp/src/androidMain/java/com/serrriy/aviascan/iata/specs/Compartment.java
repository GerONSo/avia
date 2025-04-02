package com.serrriy.aviascan.iata.specs;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *     <b>RESOLUTION 728:</b> CODE DESIGNATORS FOR PASSENGER TICKET AND BAGGAGE CHECK
 * </p>
 */
public enum Compartment
{
    SUPERSONIC("Supersonic", "R"),
    FIRST_CLASS_PREMIUM("First Class Premium", "P"),
    FIRST_CLASS("First Class", "F"),
    FIRST_CLASS_DISCOUNTED("First Class Discounted", "A"),

    // Business
    BUSINESS_CLASS_PREMIUM("Business Class Premium", "J"),
    BUSINESS_CLASS("Business Class", "C"),
    BUSINESS_CLASS_DISCOUNTED("Business Class Discounted", "D", "I", "Z"),

    // Economy
    ECONOMY_PREMIUM("Economy/Coach Premium", "W"),
    ECONOMY("Economy/Coach", "S", "Y"),
    ECONOMY_DISCOUNTED("Economy/Coach Discounted", "B", "H", "K", "L", "M", "N", "Q", "T", "X"),

    // Unknown
    UNKNOWN("<unknown>");

    private final Set<String> codes;
    private final String description;

    Compartment(String description, String...codes)
    {
        this.description = description;
        this.codes = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(codes)));
    }

    public static Compartment parse(CharSequence s)
    {
        for (Compartment compartment : values())
        {
            if (compartment.codes.contains(s))
            {
                return compartment;
            }
        }
        return UNKNOWN;
    }

    public String getDescription()
    {
        return description;
    }
}
