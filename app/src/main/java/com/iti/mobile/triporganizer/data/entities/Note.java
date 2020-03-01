package com.iti.mobile.triporganizer.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "notes", indices = {@Index(value = {"message"}, unique = true)}, foreignKeys = @ForeignKey(entity = Trip.class,  parentColumns = "id", childColumns = "tripId", onDelete = CASCADE))
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String message;
    @ColumnInfo(name = "tripId", index = true)
    private long tripId;
    private boolean status;

    public Note() {
    }

    @Ignore
    public Note(String message, long tripId, boolean status) {
        this.message = message;
        this.tripId = tripId;
        this.status = status;
    }

    protected Note(Parcel in) {
        id = in.readLong();
        message = in.readString();
        tripId = in.readLong();
        status = in.readByte() != 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Note note = (Note) obj;
        if (note != null && (tripId != note.getTripId() || !message.equals(note.getMessage())
                || status != note.getStatus())) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(message);
        dest.writeLong(tripId);
        dest.writeByte((byte) (status ? 1 : 0));
    }
}
