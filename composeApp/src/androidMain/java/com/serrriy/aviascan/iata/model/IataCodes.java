package com.serrriy.aviascan.iata.model;

import static com.serrriy.aviascan.iata.specs.Element.Type.COND;
import static com.serrriy.aviascan.iata.specs.Element.Type.MAN;
import static com.serrriy.aviascan.iata.specs.Element.Type.NONE;
import static com.serrriy.aviascan.iata.specs.Element.Type.SEC;
import static com.serrriy.aviascan.iata.specs.Occurrence.R;
import static com.serrriy.aviascan.iata.specs.Occurrence.U;

import com.serrriy.aviascan.iata.specs.Element;
import com.serrriy.aviascan.iata.specs.Occurrence;

import java.util.Map;

public final class IataCodes
{
    private IataCodes() { /* ... */ }

    public static <E extends Exception> void walk(IataCode iataCode, Visitor<E> visitor) throws E
    {
        visit(MAN, U, iataCode.getElements(), visitor);
        visit(MAN, R, iataCode.getFirstFlightSegment().getElements(), visitor);
        visit(COND, U, iataCode.getElements(), visitor);
        for (FlightSegment segment : iataCode.getFlightSegments())
        {
            visit(MAN, R, segment.getElements(), visitor);
            visit(COND, R, segment.getElements(), visitor);
            visit(NONE, R, segment.getElements(), visitor);
        }
        visit(SEC, U, iataCode.getElements(), visitor);
    }

    private static <E extends Exception> void visit(Element.Type type, Occurrence occurrence,
                                                    Map<Element, CharSequence> elements, Visitor<E> visitor) throws E
    {
        for (Element e : Element.values())
        {
            if (e.getType().equals(type) && e.getOccurrence().equals(occurrence))
            {
                CharSequence value = elements.get(e);
                if (value != null)
                {
                    visitor.onElement(e, value);
                }
            }
        }
    }
}
