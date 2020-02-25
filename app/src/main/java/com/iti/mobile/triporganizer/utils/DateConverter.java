package com.iti.mobile.triporganizer.utils;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public Date fromTimestamp(long value){
        return new Date(value);
    }

    @TypeConverter
    public long dateToTimestamp(Date date){
        return date.getTime();
    }
}
