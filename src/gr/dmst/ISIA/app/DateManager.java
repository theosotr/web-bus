package gr.dmst.ISIA.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class defines the desirable format of a date.
 *
 * @author Thodoris Sotiropoulos, Konstantinos Karakatsanis
 */
public class DateManager {
	/** Desirable format of a date */
	private String formattedDate;

	/**
	 * Constructor that initializes the format of a day according to the parameter.
	 *
	 * @param format format of date.
	 */
	public DateManager(String format) {
		/* Date object represents a date */
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format, 
				Locale.ENGLISH);
		this.formattedDate = dateFormat.format(date);
	}

	/**
	 * Get desirable format of date.
	 *
	 * @return desirable format.
	 */
	public String getFormattedDate() {
		return this.formattedDate;
	}

	/**
	 * Convert hour into the format stored in database. For example, if time is
	 * 02:00pm, so the current hour is "02", the corresponding hour stored to
	 * database is 26.
	 *
	 * @return hour stored to database.
	 */
	public int convertHourToDatabaseFormat() {
		switch(this.formattedDate) {
		case "00":
			return 24;
		case "01":
			return 25;
		case "02":
			return 26;
		case "03":
			return 27;
		case "04":
			return 28;
		case "05":
			return 29;
		case "06":
			return 30;
		case "07":
			return 31;
		default:
			return Integer.parseInt(this.formattedDate);
		}
	}

	/**
	 * Turns hour stored to the database into the normal format. For example,
	 * if time got from database is "26:03:00", it is turned into "02:03:00".
	 *
	 * @param time stored to database.
	 * @return normal format of time.
	 */
	public static String convertTimeIntoNormalFormat(String time) {
		String hour = time.substring(0, 2);
		switch(hour) {
		case "24":
			return "00" + time.substring(2);
		case "25":
			return "01" + time.substring(2);
		case "26":
			return "02" + time.substring(2);
		case "27":
			return "03" + time.substring(2);
		case "28":
			return "04" + time.substring(2);
		case "29":
			return "05" + time.substring(2);
		case "30":
			return "06" + time.substring(2);
		case "31":
			return "07" + time.substring(2);
		default:
			return time;
		
		}
	}

	/**
	 * Checks if time1 is before time2.
	 *
	 * @param time1
	 * @param time2
	 * @return total minutes passed from time1 and time2/
	 */
	public static int getDifference(String time1, String time2) {
		final int MINUTES_OF_HOUR = 60;
		String[] departureTime1 = time1.split(":");
		String[] departureTime2 = time2.split(":");
		int hour1 = Integer.parseInt(departureTime1[0]) * MINUTES_OF_HOUR +
				Integer.parseInt(departureTime1[1]);
		int hour2 = Integer.parseInt(departureTime2[0]) * MINUTES_OF_HOUR +
				Integer.parseInt(departureTime2[1]);
		return hour2 - hour1;
	}

	/**
	 * Returns the total time passed from time1 to time2.
	 *
	 * For example 1hour and 40 minutes.
	 *
	 * @param time1
	 * @param time2
	 * @return total time passed from time1 to time2
	 */
	public static String findDuration(String time1, String time2) {
		StringBuilder duration;
		String[] departureTime1 = time1.split(":");
		String[] departureTime2 = time2.split(":");
		int hour1 = Integer.parseInt(departureTime1[0]);
		int hour2 = Integer.parseInt(departureTime2[0]);
		int minute1 = Integer.parseInt(departureTime1[1]);
		int minute2 = Integer.parseInt(departureTime2[1]);
		if ((hour2 > hour1) && (minute2 > minute1))
			duration = new StringBuilder().append(hour2 - hour1).append(" ώρα")
					.append(" και ").append(minute2 - minute1).append(" λεπτά");
		else if ((hour2 > hour1) && (minute2 == minute1))
			duration = new StringBuilder().append(hour2 - hour1).append(" ώρα");
		else if ((hour2 > hour1) && (minute2 < minute1))
			duration = new StringBuilder().append(60 + (minute2 - minute1))
					.append(" λεπτά");
		else
			duration = new StringBuilder().append(minute2 - minute1)
					.append(" ").append("λεπτά");
		return String.valueOf(duration);
	}

	/**
	 * An algorithm that calculates the total minutes which have passed between
	 * two times, specified by the parameters.
	 *
	 * If the returned value is negative that means that time which is defined
	 * by the first and second parameter (hour and minutes) is posterior of
	 * time defined by the third and fourth parameter.
	 *
	 * If the returned value is positive that means that time which is defined
	 * by the first and second parameter (hour and minute ) is anterior of
	 * time defined by the third and fourth parameter.
	 *
	 * Note: Time is described by two arguments. The first one is the hour of
	 * time and the second one is the minutes of time. For example, if we want
	 * to compare time 4:34PM and 2:12AM then the first argument it would be
	 * 4, the second 34, the third 14, and the last one 12.
	 *
	 * @param hourA hour of first time.
	 * @param minutesA minute of first time.
	 * @param hourB hour of second time.
	 * @param minutesB minutes of second time.
	 * @return int, total minutes. If negative first time is posterior to second
	 * time, otherwise the opposite.
	 */
	public static Integer calculateTimeOfNextArrivals(int hourA, int minutesA,
			int hourB, int minutesB) {
		final int MINUTES_OF_HOUR = 60;
		if (hourB < hourA)
			return - ((calculateTimeOfNextArrivals(hourB,
					minutesB, hourA, minutesA)));
		else if (hourB - hourA >= 2)
			return MINUTES_OF_HOUR + calculateTimeOfNextArrivals(hourA,
					minutesA, hourB - 1, minutesB);
		else if (hourB - hourA == 1) {
			if (minutesB > minutesA)
				return MINUTES_OF_HOUR + calculateTimeOfNextArrivals(hourA,
						minutesA, hourB - 1, minutesB);
			else if (minutesB == minutesA)
				return MINUTES_OF_HOUR;
			else
				return (minutesB + (MINUTES_OF_HOUR - minutesA));
		} else
			return minutesB - minutesA;
	}
}