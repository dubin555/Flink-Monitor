package data;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Gson POJO class for /jobs
 *
 * {
 * "jobs-running": [],
 * "jobs-finished": ["7684be6004e4e955c2a558a9bc463f65","49306f94d0920216b636e8dd503a6409"],
 * "jobs-cancelled":[],
 * "jobs-failed":[]
 * }
 */
public class Jobs {

    @SerializedName("jobs-running")
    public String[] jobsRunning;

    @SerializedName("jobs-finished")
    public String[] jobsFinished;

    @SerializedName("jobs-cancelled")
    public String[] jobsCancelled;

    @SerializedName("jobs-failed")
    public String[] jobsFailed;

    @Override
    public String toString() {
        return "Jobs{" +
                "jobsRunning=" + Arrays.toString(jobsRunning) +
                ", jobsFinished=" + Arrays.toString(jobsFinished) +
                ", jobsCancelled=" + Arrays.toString(jobsCancelled) +
                ", jobsFailed=" + Arrays.toString(jobsFailed) +
                '}';
    }
}
