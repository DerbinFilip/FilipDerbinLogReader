import java.util.Arrays;
import java.util.List;

public class LogsName {
    public String errorLog = "ERROR";
    public String warnLog = "WARN";
    public String infoLog = "INFO";
    public String fatalLog = "FATAL";
    public String debugLog = "DEBUG";
    public List<String> listOfLogs = Arrays.asList(errorLog, warnLog, infoLog, fatalLog, debugLog);
    int numberOfWARN, numberOfINFO, numberOfEROR, numberOfFATAL, numberOfDEBUG;

    public int getNumberOfWARN() {
        return numberOfWARN;
    }

    public void setNumberOfWARN(int numberOfWARN) {
        this.numberOfWARN = numberOfWARN;
    }

    public int getNumberOfINFO() {
        return numberOfINFO;
    }

    public void setNumberOfINFO(int numberOfINFO) {
        this.numberOfINFO = numberOfINFO;
    }

    public int getNumberOfEROR() {
        return numberOfEROR;
    }

    public void setNumberOfEROR(int numberOfEROR) {
        this.numberOfEROR = numberOfEROR;
    }

    public int getNumberOfFATAL() {
        return numberOfFATAL;
    }

    public void setNumberOfFATAL(int numberOfFATAL) {
        this.numberOfFATAL = numberOfFATAL;
    }

    public int getNumberOfDEBUG() {
        return numberOfDEBUG;
    }

    public void setNumberOfDEBUG(int numberOfDEBUG) {
        this.numberOfDEBUG = numberOfDEBUG;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getWarnLog() {
        return warnLog;
    }

    public void setWarnLog(String warnLog) {
        this.warnLog = warnLog;
    }

    public String getInfoLog() {
        return infoLog;
    }

    public void setInfoLog(String infoLog) {
        this.infoLog = infoLog;
    }

    public String getFatalLog() {
        return fatalLog;
    }

    public void setFatalLog(String fatalLog) {
        this.fatalLog = fatalLog;
    }

    public String getDebugLog() {
        return debugLog;
    }

    public void setDebugLog(String debugLog) {
        this.debugLog = debugLog;
    }
}
