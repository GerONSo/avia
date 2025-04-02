package com.serrriy.aviascan.iata.specs;

public enum PassIssuanceSource
{
    WEB                    ("W", "Web Printed"),
    AIRPORT_KIOSK_PRINTED  ("K", "Airport Kiosk Printed"),
    TRANSFER_KIOSK_PRINTED ("X", "Transfer Kiosk Printed"),
    OFF_SITE_KIOSK_PRINTED ("R", "Remote or Off Site Kiosk Printed"),
    MOBILE_DEVICE_PRINTED  ("M", "Mobile Device Printed"),
    AIRPORT_AGENT_PRINTED  ("O", "Airport Agent Printed"),
    TOWN_AGENT_PRINTED     ("T", "Town Agent Printed"),
    THIRD_PARTY_PRINTED    ("V", "Third Party Vendor Printed"),
    UNABLE_TO_SUPPORT      (" ", "Unable to support"),
    UNKNOWN                ("", "<unknown>");

    private final String value;
    private final String description;

    PassIssuanceSource(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public static PassIssuanceSource parse(CharSequence s)
    {
        for (PassIssuanceSource source : values())
        {
            if (source.getValue().equals(s))
            {
                return source;
            }
        }
        return UNKNOWN;
    }

    public String getDescription()
    {
        return description;
    }

    public String getValue()
    {
        return value;
    }
}
