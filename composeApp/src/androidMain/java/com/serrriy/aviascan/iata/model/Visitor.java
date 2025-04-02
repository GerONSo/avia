package com.serrriy.aviascan.iata.model;

import com.serrriy.aviascan.iata.specs.Element;

public interface Visitor<E extends Exception>
{
    void onElement(Element e, CharSequence value) throws E;
}
