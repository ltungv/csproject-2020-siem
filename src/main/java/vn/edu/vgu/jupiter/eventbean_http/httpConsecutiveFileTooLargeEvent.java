package vn.edu.vgu.jupiter.eventbean_http;

public class httpConsecutiveFileTooLargeEvent {
    String time;
    String timeZone;
    String returnObjSize;

    public httpConsecutiveFileTooLargeEvent(String time, String timeZone, String returnObjSize) {
        this.time = time;
        this.timeZone = timeZone;
        this.returnObjSize = returnObjSize;
    }

    public String getTime() {
        return time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getReturnObjSize() {
        return returnObjSize;
    }
}