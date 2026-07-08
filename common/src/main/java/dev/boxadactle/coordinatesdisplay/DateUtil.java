package dev.boxadactle.coordinatesdisplay;

import net.minecraft.client.resources.language.I18n;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Utility methods for formatting timestamps and converting Minecraft ticks to human-readable times.
 *
 * <p>In Minecraft, 24000 ticks == 24 hours (1 day). 1000 ticks == 1 hour. Tick 0 corresponds to 6:00
 * (sunrise). This class provides helpers to format day ticks as conventional clock times (e.g. "9:30 PM").
 */
public final class DateUtil {

    // Default date/time formatter used by formatEpochMillis
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    /**
     * Format an epoch millisecond timestamp using the system default zone and the default pattern.
     *
     * @param epochMillis epoch milliseconds
     * @return formatted date/time string
     */
    public static String formatEpochMillis(long epochMillis) {
        return formatEpochMillis(epochMillis, ZoneId.systemDefault(), DEFAULT_DATE_TIME_FORMATTER);
    }

    /**
     * Format an epoch millisecond timestamp using a specific zone and formatter.
     *
     * @param epochMillis epoch milliseconds
     * @param zone zone id to use
     * @param formatter formatter to apply
     * @return formatted date/time string
     */
    public static String formatEpochMillis(long epochMillis, ZoneId zone, DateTimeFormatter formatter) {
        Objects.requireNonNull(zone, "zone");
        Objects.requireNonNull(formatter, "formatter");

        Instant instant = Instant.ofEpochMilli(epochMillis);
        ZonedDateTime zdt = instant.atZone(zone);
        return formatter.format(zdt);
    }

    /**
     * Convert total Minecraft ticks to day ticks (0..23999). Handles negative values.
     *
     * @param totalTicks total ticks (may be negative or larger than 24000)
     * @return normalized day ticks in range [0, 24000)
     */
    public static long toDayTicks(long totalTicks) {
        long day = 24000L;
        long t = totalTicks % day;
        if (t < 0) t += day;
        return t;
    }

    /**
     * Return the in-game LocalTime corresponding to the provided day ticks.
     * <p>
     * Mapping used: 1000 ticks == 1 hour. Tick 0 == 06:00 (6 AM). The returned LocalTime is the
     * hour/minute/second representation of the in-game clock.
     *
     * @param dayTicks ticks within the day (any long, will be normalized)
     * @return LocalTime representing the in-game time (24-hour clock)
     */
    public static LocalTime minecraftDayTicksToLocalTime(long dayTicks) {
        long ticks = toDayTicks(dayTicks);

        // Each 1000 ticks equals one hour
        long hoursFromTicks = ticks / 1000L; // 0..23 (before adding offset)
        int hour24 = (int) ((hoursFromTicks + 6) % 24); // tick 0 => 6:00

        // remainder ticks within current Minecraft hour
        long remainder = ticks % 1000L; // 0..999

        // compute seconds within hour (3600 seconds per hour)
        long totalSecondsInHour = Math.round(remainder * (3600.0 / 1000.0));
        int minute = (int) ((totalSecondsInHour / 60) % 60);
        int second = (int) (totalSecondsInHour % 60);

        // rounding with the chosen calculation cannot produce 60 minutes here because remainder is 0..999
        // minute will therefore be in range 0..59

        return LocalTime.of(hour24, minute, second);
    }

    /**
     * Format day ticks into a human-friendly 12-hour clock string, e.g. "9:30 PM".
     *
     * @param dayTicks ticks within the day (any long, will be normalized)
     * @return formatted time like "9:30 PM"
     */
    public static String formatMinecraftDayTime(long dayTicks) {
        if (dayTicks == -1L) return I18n.get("hud.coordinatesdisplay.time.unknown");
        LocalTime t = minecraftDayTicksToLocalTime(dayTicks);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm a");
        return fmt.format(t);
    }

    /**
     * Format day ticks into a 24-hour clock string, e.g. "21:30".
     *
     * @param dayTicks ticks within the day (any long, will be normalized)
     * @return formatted time like "21:30"
     */
    public static String formatMinecraftDayTime24(long dayTicks) {
        if (dayTicks == -1L) return I18n.get("hud.coordinatesdisplay.time.unknown");
        LocalTime t = minecraftDayTicksToLocalTime(dayTicks);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        return fmt.format(t);
    }

    /**
     * Format total world ticks (may be >24000) into a human-friendly 12-hour clock string by
     * converting to day ticks first.
     *
     * @param totalTicks total world ticks
     * @return formatted time like "9:30 PM"
     */
    public static String formatMinecraftWorldTicks(long totalTicks) {
        return formatMinecraftDayTime(toDayTicks(totalTicks));
    }

    /**
     * Format total world ticks into a 24-hour clock string by converting to day ticks first.
     *
     * @param totalTicks total world ticks
     * @return formatted time like "21:30"
     */
    public static String formatMinecraftWorldTicks24(long totalTicks) {
        return formatMinecraftDayTime24(toDayTicks(totalTicks));
    }
}


