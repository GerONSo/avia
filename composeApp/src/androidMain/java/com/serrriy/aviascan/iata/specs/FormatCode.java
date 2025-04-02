package com.serrriy.aviascan.iata.specs;

public enum FormatCode
{
    SINGLE   ("S", "Single"),
    MULTIPLE ("M", "Multiple"),
    UNKNOWN  ("", "<unknown>");

    private final String value;
    private final String description;

    FormatCode(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public String getValue()
    {
        return value;
    }

    public String getDescription()
    {
        return description;
    }

    public static FormatCode parse(CharSequence s)
    {
        for (FormatCode code : values())
        {
            if (code.getValue().equals(s))
            {
                return code;
            }
        }
        return UNKNOWN;
    }
}
