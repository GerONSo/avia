package com.serrriy.aviascan.iata.print;

import com.serrriy.aviascan.iata.specs.Element;

public interface Formatter
{
    CharSequence formatElement(Element element, CharSequence value);
}
