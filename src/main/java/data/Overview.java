package data;

import com.google.gson.annotations.SerializedName;

/**
 * Gson POJO class for /overview
 *
 * {
 * "taskmanagers": 17,
 * "slots-total": 68,
 * "slots-available": 68,
 * "jobs-running": 0,
 * "jobs-finished": 3,
 * "jobs-cancelled": 1,
 * "jobs-failed": 0
 * }
 */
public class Overview {

    @SerializedName("taskmanagers")
    public String taskManagers;

    @SerializedName("slots-total")
    public String slotsTotal;

    @SerializedName("slots-available")
    public String slotsAvailable;

    @SerializedName("jobs-running")
    public String jobsRunning;

    @SerializedName("jobs-finished")
    public String jobsFinished;

    @SerializedName("jobs-cancelled")
    public String jobsCancelled;

    @SerializedName("jobs-failed")
    public String jobsFailed;

    @Override
    public String toString() {
        return "Overview{" +
                "taskManagers='" + taskManagers + '\'' +
                ", slotsTotal='" + slotsTotal + '\'' +
                ", slotsAvailable='" + slotsAvailable + '\'' +
                ", jobsRunning='" + jobsRunning + '\'' +
                ", jobsFinished='" + jobsFinished + '\'' +
                ", jobsCancelled='" + jobsCancelled + '\'' +
                ", jobsFailed='" + jobsFailed + '\'' +
                '}';
    }
}
