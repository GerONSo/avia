package com.serrriy.aviascan.iata.specs;

public enum SelecteeIndicator
{
    NOT_APPLICABLE (" ", "Not applicable to that flight"),
    SELECTEE       ("1", "selectee"),
    NOT_SELECTEE   ("0", "not selectee"),
    UNKNOWN        ("", "<unknown>");

    private final String value;
    private final String description;

    SelecteeIndicator(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public static SelecteeIndicator parse(CharSequence s)
    {
        for (SelecteeIndicator indicator : values())
        {
            if (indicator.getValue().equals(s))
            {
                return indicator;
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
