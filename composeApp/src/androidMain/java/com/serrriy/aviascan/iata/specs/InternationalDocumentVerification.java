package com.serrriy.aviascan.iata.specs;

public enum InternationalDocumentVerification
{
    NA           (" ", "Not applicable to flight"),
    NOT_REQUIRED ("0", "Travel document verification not required"),
    REQUIRED     ("1", "Travel document verification required"),
    PERFORMED    ("2", "Travel document verification performed"),
    UNKNOWN      ("", "<unknown>");

    private final String value;
    private final String description;

    InternationalDocumentVerification(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public static InternationalDocumentVerification parse(CharSequence s)
    {
        for (InternationalDocumentVerification verification : values())
        {
            if (verification.getValue().equals(s))
            {
                return verification;
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
