package com.serrriy.aviascan.iata.specs;

public enum Occurrence
{
    U("Unique"),
    R("Repeated");

    private final String description;

    Occurrence(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}
