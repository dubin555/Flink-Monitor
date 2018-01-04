package data;

import com.google.gson.annotations.SerializedName;

/**
 * Gson POJO class for /config
 *
 * {
 * "refresh-interval": 3000,
 * "timezone-offset": 3600000,
 * "timezone-name": "Central European Time",
 * "flink-version": "1.3.2",
 * "flink-revision": "8124545 @ 16.09.2015 @ 15:38:42 CEST"
 * }
 */
public class Config {

    @SerializedName("refresh-interval")
    public String refreshInterval;

    @SerializedName("timezone-offset")
    public String timezoneOffset;

    @SerializedName("timezone-name")
    public String timezoneName;

    @SerializedName("flink-version")
    public String flinkVersion;

    @Override
    public String toString() {
        return "Config{" +
                "refreshInterval='" + refreshInterval + '\'' +
                ", timezoneOffset='" + timezoneOffset + '\'' +
                ", timezoneName='" + timezoneName + '\'' +
                ", flinkVersion='" + flinkVersion + '\'' +
                '}';
    }
}
