package com.serrriy.aviascan.iata.specs;

import java.util.regex.Pattern;

public enum DataType
{
    f                     ("[\\u0020-\\u007E]+"),
    N                     ("[0-9]+"),
    a                     ("[A-Z]+"),
    NNNa                  ("[0-9]{3}[A-Z]"),
    NNNN_a                ("[0-9]{4}[A-Z\\s]?"),
    NNNN_f                ("[0-9]{4}[\\u0020-\\u007E]?"),
    ANY                   (".*"),
    GREATER_THAN          (">"),
    CARET_OR_GREATER_THAN ("\\^|>"); // Old version of IATA spec used '>' to denote the start of security data

    private final Pattern typeRegex;

    DataType(String typeRegex)
    {
        this.typeRegex = Pattern.compile(typeRegex);
    }

    public boolean isValid(CharSequence s)
    {
        return typeRegex.matcher(s).matches();
    }

    @Override
    public String toString()
    {
        return String.format("%s('%s')", name(), typeRegex);
    }
}
