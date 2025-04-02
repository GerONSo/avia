package com.serrriy.aviascan.iata.specs;

public enum CheckinSource
{
    WEB            ("W", "Web"),
    AIRPORT_KIOSK  ("K", "Airport Kiosk"),
    OFF_SITE_KIOSK ("R", "Remote or Off Site Kiosk"),
    MOBILE_DEVICE  ("M", "Mobile Device"),
    AIRPORT_AGENT  ("O", "Airport Agent"),
    TOWN_AGENT     ("T", "Town Agent"),
    THIRD_PARTY    ("V", "Third Party Vendor"),
    UNKNOWN        ("", "<unknown>");

    private final String value;
    private final String description;

    CheckinSource(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public static CheckinSource parse(CharSequence s)
    {
        for (CheckinSource source : values())
        {
            if (source.getValue().equals(s))
            {
                return source;
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
