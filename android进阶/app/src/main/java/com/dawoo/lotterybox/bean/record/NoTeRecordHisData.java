package com.dawoo.lotterybox.bean.record;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

/**
 * Created by benson on 18-2-18.
 */

public class NoTeRecordHisData implements Parcelable {
    private int error;
    private String code;
    private String message;
    private List<NoteRecordHis> data;
    private Count extend;

    public Count getExtend() {
        return extend;
    }

    public void setExtend(Count extend) {
        this.extend = extend;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NoteRecordHis> getData() {
        return data;
    }

    public void setData(List<NoteRecordHis> data) {
        this.data = data;
    }


    public static class Count implements Parcelable {
        private int totalCount;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.totalCount);
        }

        public Count() {
        }

        protected Count(Parcel in) {
            this.totalCount = in.readInt();
        }

        public static final Creator<Count> CREATOR = new Creator<Count>() {
            @Override
            public Count createFromParcel(Parcel source) {
                return new Count(source);
            }

            @Override
            public Count[] newArray(int size) {
                return new Count[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.error);
        dest.writeString(this.code);
        dest.writeString(this.message);
        dest.writeTypedList(this.data);
        dest.writeParcelable(this.extend, flags);
    }

    public NoTeRecordHisData() {
    }

    protected NoTeRecordHisData(Parcel in) {
        this.error = in.readInt();
        this.code = in.readString();
        this.message = in.readString();
        this.data = in.createTypedArrayList(NoteRecordHis.CREATOR);
        this.extend = in.readParcelable(Count.class.getClassLoader());
    }

    public static final Parcelable.Creator<NoTeRecordHisData> CREATOR = new Parcelable.Creator<NoTeRecordHisData>() {
        @Override
        public NoTeRecordHisData createFromParcel(Parcel source) {
            return new NoTeRecordHisData(source);
        }

        @Override
        public NoTeRecordHisData[] newArray(int size) {
            return new NoTeRecordHisData[size];
        }
    };
}
