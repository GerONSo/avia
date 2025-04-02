package com.serrriy.aviascan.iata;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * This class provides static factory methods for creating {@link Calendar} instances in UTC.
 */
public final class UTCCalendarFactory
{
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    private UTCCalendarFactory()
    {
    }

    public static Calendar getInstanceForDayOfYear(int dayOfYear)
    {
        return getInstanceForDayOfYear(dayOfYear, Calendar.getInstance(UTC));
    }

    /**
     * Creates {@link Calendar} instance in UTC from the given <code>dayOfYear</code>
     *
     * @param dayOfYear the day of year
     * @return the new {@link Calendar} instance in UTC
     */
    static Calendar getInstanceForDayOfYear(int dayOfYear, Calendar utc)
    {
        // getting current date in UTC
        int utcDayOfYear = utc.get(Calendar.DAY_OF_YEAR);
        Calendar c = (Calendar) utc.clone();

        if (dayOfYear < utcDayOfYear)
        {
            // given dayOfYear must be in the following year
            c.roll(Calendar.YEAR, 1);
        }
        else if (utcDayOfYear == 1 && (dayOfYear == 365 || dayOfYear == 366))
        {
            // check for edge case at the end of year
            // when given dayOfYear still in previous year but server time in new year
            // this way trying to compensate for lack of time zone information

            // given dayOfYear must still be in the previous year
            c.roll(Calendar.YEAR, -1);
        }


        c.set(Calendar.DAY_OF_YEAR, dayOfYear);
        // truncating the calendar, leaving only date part
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        return c;
    }
}