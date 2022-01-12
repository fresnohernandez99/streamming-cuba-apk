package cu.video.app.streamingcuba.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    enum class FormatType(val value: String) {
        YYYY_MM_DD_T_HH_MM_SS_SSS_Z("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    }

    enum class TimeZoneFormat(val timeZone: TimeZone) {
        DEFAULT(TimeZone.getDefault()),
        UTC(TimeZone.getTimeZone("UTC"))
    }

    fun format(
        inputDate: String,
        inFormat: FormatType,
        outFormat: FormatType,
        inTimeZoneFormat: TimeZoneFormat? = null,
        outTimeZoneFormat: TimeZoneFormat? = null
    ): String {
        val inputDateFormat = SimpleDateFormat(inFormat.value, Locale.getDefault())
        val outputDateFormat = SimpleDateFormat(outFormat.value, Locale.getDefault())

        if (inTimeZoneFormat != null) {
            inputDateFormat.timeZone = inTimeZoneFormat.timeZone
        }

        if (outTimeZoneFormat != null) {
            outputDateFormat.timeZone = outTimeZoneFormat.timeZone
        }

        val date = inputDateFormat.parse(inputDate)
        return outputDateFormat.format(date!!)
    }

    fun getCurrentDate(
        outFormat: FormatType,
        outTimeZoneFormat: TimeZoneFormat? = null
    ): String {
        val outputDateFormat = SimpleDateFormat(outFormat.value, Locale.ENGLISH)
        if (outTimeZoneFormat != null) {
            outputDateFormat.timeZone = outTimeZoneFormat.timeZone
        }
        val date = Calendar.getInstance()
        return outputDateFormat.format(date.time)
    }

    fun getDateString(
        inputDate: String,
        inFormat: FormatType,
        outFormat: FormatType,
        inTimeZoneFormat: TimeZoneFormat? = null,
    ): String {
        val inputDateFormat = SimpleDateFormat(inFormat.value, Locale.getDefault())
        val locale = Locale("es", "ES")
        if (inTimeZoneFormat != null) {
            inputDateFormat.timeZone = inTimeZoneFormat.timeZone
        }
        val outputDateFormat = SimpleDateFormat(outFormat.value, locale)
        val date = inputDateFormat.parse(inputDate)
        return outputDateFormat.format(date!!.time)
    }

    /**
     *
     */
    fun getDate(
        inputDate: String,
        inFormat: FormatType,
        inTimeZoneFormat: TimeZoneFormat? = null
    ): Date {
        val inputDateFormat = SimpleDateFormat(inFormat.value, Locale.getDefault())

        if (inTimeZoneFormat != null) {
            inputDateFormat.timeZone = inTimeZoneFormat.timeZone
        }
        return inputDateFormat.parse(inputDate)!!
    }

    fun getDay(dateIn: String): String {
        val startTime: Date =
            getDate(
                inputDate = dateIn,
                inFormat = FormatType.YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
                inTimeZoneFormat = TimeZoneFormat.UTC
            )
        val locale = Locale("es", "ES")
        val simpleDateformat = SimpleDateFormat("E", locale)
        val dayOfWeek = simpleDateformat.format(startTime)
        val calendar = Calendar.getInstance()
        calendar.time = startTime
        val day =
            dayOfWeek.toUpperCase(Locale.ROOT).replace(".", "") + " " + calendar.get(Calendar.DAY_OF_MONTH);

        return day;
    }

    fun getHour(
        dateIn: String,
        inTimeZoneFormat: TimeZoneFormat? = TimeZoneFormat.UTC
    ): String {

        val startTime: Date =
            getDate(
                inputDate = dateIn,
                inFormat = FormatType.YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
                inTimeZoneFormat = inTimeZoneFormat
            )

        val locale = Locale("es", "ES")
        val simpleDateFormatHour = SimpleDateFormat("HH:mm", locale)

        return simpleDateFormatHour.format(startTime);
    }

    fun getDay(dateIn: Date): String {
        val simpleDateFormat = SimpleDateFormat("d", Locale.ENGLISH)
        return simpleDateFormat.format(dateIn.time)
    }

    fun getDayFullName(dateIn: Date): String {
        val simpleDateFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        return simpleDateFormat.format(dateIn.time)
    }

    fun getMonth(dateIn: Date): String {
        val simpleDateFormat = SimpleDateFormat("m", Locale.ENGLISH)
        return simpleDateFormat.format(dateIn.time)
    }

    fun getYear(dateIn: Date): String {
        val simpleDateFormat = SimpleDateFormat("y", Locale.ENGLISH)
        return simpleDateFormat.format(dateIn.time)
    }

    fun getDateISOFormat(): String {
        val tz = TimeZone.getTimeZone("UTC")
        val df =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH) // Quoted "Z" to indicate UTC, no timezone offset
        df.timeZone = tz

        return df.format(Date())
    }

}