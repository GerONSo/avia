package com.serrriy.aviascan.iata.specs;

public enum DocumentType
{
    BOARDING_PASS     ("B", "Boarding Pass"),
    ITINERARY_RECEIPT ("I", "Itinerary Receipt"),
    UNKNOWN           ("", "<unknown>");

    private final String value;
    private final String description;

    DocumentType(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public static DocumentType parse(CharSequence s)
    {
        for (DocumentType documentType : values())
        {
            if (documentType.getValue().equals(s))
            {
                return documentType;
            }
        }
        return UNKNOWN;
    }

    public String getValue()
    {
        return value;
    }

    public String getDescription()
    {
        return description;
    }
}
