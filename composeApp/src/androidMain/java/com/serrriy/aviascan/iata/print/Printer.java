package com.serrriy.aviascan.iata.print;

import com.serrriy.aviascan.iata.model.Visitor;
import com.serrriy.aviascan.iata.specs.Element;

import java.io.IOException;
import java.io.Writer;

public final class Printer implements Visitor<IOException>
{
    private final Formatter formatter;
    private final Writer writer;

    public Printer(Formatter formatter, Writer writer)
    {
        this.formatter = formatter;
        this.writer = writer;
    }

    @Override
    public void onElement(Element element, CharSequence value) throws IOException
    {
        writer.write(formatter.formatElement(element, value).toString());
        writer.flush();
    }
}
