package vn.edu.vgu.jupiter.eventbean_http;

public class FileTooLargeAlert {
    String time;
    String timeZone;
    long failuresCount;

    public FileTooLargeAlert(String time, String timeZone, long failuresCount) {
        this.time = time;
        this.timeZone = timeZone;
        this.failuresCount = failuresCount;
    }

    public String getTime() {
        return time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public long getFailuresCount() {
        return failuresCount;
    }
}
