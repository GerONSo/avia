package com.serrriy.aviascan.iata.specs;

public enum FastTrack
{
    ELLIGIBLE     ("Y", "Elligible for this segment"),
    NOT_ELLIGIBLE ("N", "Not elligible for this segment"),
    NOT_QUALIFIED(" ", "Elligibility not qualified"),
    UNKNOWN       ("", "<unknown>");

    private final String value;
    private final String description;

    FastTrack(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public static FastTrack parse(CharSequence s)
    {
        for (FastTrack track : values())
        {
            if (track.getValue().equals(s))
            {
                return track;
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
