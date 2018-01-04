package data;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Gson POJO class for /jobs/jobid/exceptions
 *
 * {
 * "root-exception": "java.io.IOException: File already exists:/tmp/abzs/2\n\tat org.apache.flink.core.fs.local.LocalFileSystem. ...",
 * "all-exceptions": [ {
 * "exception": "java.io.IOException: File already exists:/tmp/abzs/1\n\tat org.apache.flink...",
 * "task": "DataSink (CsvOutputFormat (path: /tmp/abzs, delimiter:  )) (1/2)",
 * "location": "localhost:49220"
 * }, {
 * "exception": "java.io.IOException: File already exists:/tmp/abzs/2\n\tat org.apache.flink...",
 * "task": "DataSink (CsvOutputFormat (path: /tmp/abzs, delimiter:  )) (2/2)",
 * "location": "localhost:49220"
 * } ],
 * "truncated":false
 * }
 */
public class JobExceptions {

    @SerializedName("root-exception")
    public String rootException;

    @SerializedName("all-exceptions")
    public String[] allExceptions;

    @SerializedName("truncated")
    public boolean truncated;

    @Override
    public String toString() {
        return "JobExceptions{" +
                "rootException='" + rootException + '\'' +
                ", allExceptions=" + Arrays.toString(allExceptions) +
                ", truncated=" + truncated +
                '}';
    }
}
