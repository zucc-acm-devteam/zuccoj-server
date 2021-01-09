package top.kealine.zuccoj.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JudgehostStatus {
    private String judgehostName;
    private String heartbeatTime;
    private int status;

    public static final String JUDGEHOST_HEARTBEAT_KEY_PREFIX = "ZUCCOJ::JUDGEHOST::HEARTBEAT::";
    public static final SimpleDateFormat formatter;

    static {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static class StatusCode {
        public static final int OFFLINE = -1;
        public static final int UNKNOWN = 0;
        public static final int ONLINE = 1;
        public static final int BUSY = 2;

        private static final int SECOND_UNIT = 1000;
        private static final int MAX_ONLINE_LIMIT = 20 * SECOND_UNIT;
        private static final int MAX_BUSY_LIMIT = 60 * SECOND_UNIT;

        public static int of(long msgTime, long nowTime) {
            if (msgTime > nowTime) {
                return UNKNOWN;
            } else {
                long past = nowTime - msgTime;
                if (past < MAX_ONLINE_LIMIT) {
                    return ONLINE;
                } else if (past < MAX_BUSY_LIMIT) {
                    return BUSY;
                } else {
                    return OFFLINE;
                }
            }
        }
    }

    public JudgehostStatus(String judgehostName, String heartbeatMsg) {
        this.judgehostName = judgehostName;
        try {
            long msgTime = Long.parseLong(heartbeatMsg);
            this.heartbeatTime = formatter.format(new Date(msgTime));
            this.status = StatusCode.of(msgTime, System.currentTimeMillis());
        } catch (NumberFormatException e) {
            this.status = StatusCode.UNKNOWN;
            this.heartbeatTime = "0000-00-00 00:00:00";
        }
    }

    public String getJudgehostName() {
        return judgehostName;
    }

    public void setJudgehostName(String judgehostName) {
        this.judgehostName = judgehostName;
    }

    public String getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(String heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
