package de.jeezy_land.recieveroutetable;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class LogData implements Parcelable {
    private int id;
    private int routenTyp;
    private int reiseZeit;
    private String eintragsDatum;

    private LogData() {
        this.id = 0;
        this.routenTyp = 0;
        this.reiseZeit = 0;
        this.eintragsDatum = "";
    }
    public LogData(int id, int routenTyp, int reiseZeit, String eintragsdatumDatum) {
        this();
        this.id = id;
        this.routenTyp = routenTyp;
        this.reiseZeit = reiseZeit;
        this.eintragsDatum = eintragsdatumDatum;
    }

    protected LogData(Parcel in) {
        this();
        id = in.readInt();
        routenTyp = in.readInt();
        reiseZeit = in.readInt();
        eintragsDatum = in.readString();
    }

    public static final Creator<LogData> CREATOR = new Creator<LogData>() {
        @Override
        public LogData createFromParcel(Parcel in) {
            return new LogData(in);
        }

        @Override
        public LogData[] newArray(int size) {
            return new LogData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoutenTyp() {
        return routenTyp;
    }

    public void setRoutenTyp(int routenTyp) {
        this.routenTyp = routenTyp;
    }

    public int getReiseZeit() {
        return reiseZeit;
    }

    public void setReiseZeit(int reiseZeit) {
        this.reiseZeit = reiseZeit;
    }

    public String getEintragsdatumDatum() {
        return eintragsDatum;
    }

    public void setEintragsdatumDatum(String eintragsdatumDatum) {
        this.eintragsDatum = eintragsdatumDatum;
    }

    @Override
    public String toString() {
        return "Route " + routenTyp + " brauchte " + this.reiseZeit/60 + " min am " + this.eintragsDatum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(routenTyp);
        dest.writeInt(reiseZeit);

        dest.writeString(eintragsDatum);
    }
}
