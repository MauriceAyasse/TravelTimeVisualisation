package de.jeezy_land.recieveroutetable;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LogDataHandler implements Parcelable {
    private List<LogData> listLog;

    protected LogDataHandler(Parcel in) {
        this();
        in.readTypedList(listLog, LogData.CREATOR);
    }

    public static final Creator<LogDataHandler> CREATOR = new Creator<LogDataHandler>() {
        @Override
        public LogDataHandler createFromParcel(Parcel in) {
            return new LogDataHandler(in);
        }

        @Override
        public LogDataHandler[] newArray(int size) {
            return new LogDataHandler[size];
        }
    };

    public List<String> getLoggedDays() {
        List<String> loggedDays = new ArrayList<>();

        if (this.listLog != null) {
            String datum = listLog.get(0).getEintragsdatumDatum().substring(0, 10);
            int zaehler = 1;
            for (int i = 0; listLog.size() > i; i++) {
                LogData logEintrag = listLog.get(i);
                String leDatum = logEintrag.getEintragsdatumDatum().substring(0, 10);
                zaehler++;
                if (!leDatum.equals(datum) || listLog.size() - 1 == i) {
                    loggedDays.add(zaehler + " Einträge am " + datum);
                    datum = leDatum;
                    zaehler = 0;
                }
            }
        }
        return loggedDays;
    }

    public List<LogData> getLoggedDay(String datum) {
        List<LogData> logDataDay = new ArrayList<>();
        for (LogData day : listLog) {
            if (day.getEintragsdatumDatum().substring(0, 10).equals(datum))
                logDataDay.add(day);
        }
        return logDataDay;
    }

    public List<LogData> getLoggedDay(String datum, int stepps, int route) {
        List<LogData> logDataDay = new ArrayList<>();
        List<LogData> logDataRoute = getListLog(route);
        for (int i = 0; logDataRoute.size() > i; i += stepps) {
            LogData day = logDataRoute.get(i);
            if (day.getEintragsdatumDatum().substring(0, 10).equals(datum)) {
                logDataDay.add(day);
            }
        }
        return logDataDay;
    }

    private LogDataHandler() {
        listLog = new ArrayList<>();
    }

    public LogDataHandler(List<LogData> listLog) {
        this();
        if (listLog != null) {
            this.listLog = listLog;
        }

    }

    public List<LogData> getListLog() {
        return listLog;
    }

    public List<LogData> getListLog(int route) {
        List<LogData> logDataRoute = new ArrayList<>();
        for (LogData logData : listLog) {
            if (logData.getRoutenTyp() == route) {
                logDataRoute.add(logData);
            }
        }
        return logDataRoute;
    }

    public void setListLog(List<LogData> listLog) {
        this.listLog = listLog;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
         dest.writeTypedList(listLog);
    }
}
