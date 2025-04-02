package com.serrriy.aviascan.iata.specs;

public enum BaggageUnit
{
    PIECES  ("PC", "pieces"),
    KILOS   ("K", "kilos"),
    POUNDS  ("L", "pounds"),
    UNKNOWN ("K", "<unknown unit>");

    private final String value;
    private final String description;

    BaggageUnit(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public static BaggageUnit parse(CharSequence s)
    {
        for (BaggageUnit unit : values())
        {
            if (unit.getValue().equals(s))
            {
                return unit;
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
