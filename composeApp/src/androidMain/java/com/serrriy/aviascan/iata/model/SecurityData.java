package com.serrriy.aviascan.iata.model;

import java.util.Objects;

public final class SecurityData
{
    private final String type;
    private final String data;

    public SecurityData(String type, String data)
    {
        this.type = type;
        this.data = data;
    }

    public String getType()
    {
        return type;
    }

    public String getData()
    {
        return data;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof SecurityData))
        {
            return false;
        }
        SecurityData that = (SecurityData) o;
        return Objects.equals(getType(), that.getType()) &&
            Objects.equals(getData(), that.getData());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getType(), getData());
    }
}
