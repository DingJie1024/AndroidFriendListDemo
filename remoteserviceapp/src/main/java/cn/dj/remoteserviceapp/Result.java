package cn.dj.remoteserviceapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable {

    private String paySuccess;
    private String noneUser;
    private String errorPassword;
    private String noEnoughBalance;

    public Result(String paySuccess, String noneUser, String errorPassword, String noEnoughBalance) {
        this.paySuccess = paySuccess;
        this.noneUser = noneUser;
        this.errorPassword = errorPassword;
        this.noEnoughBalance = noEnoughBalance;
    }

    public String getPaySuccess() {
        return paySuccess;
    }

    public String getNoneUser() {
        return noneUser;
    }

    public String getErrorPassword() {
        return errorPassword;
    }

    public String getNoEnoughBalance() {
        return noEnoughBalance;
    }

    protected Result(Parcel in) {
        paySuccess = in.readString();
        noneUser = in.readString();
        errorPassword = in.readString();
        noEnoughBalance = in.readString();
    }



    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paySuccess);
        dest.writeString(noneUser);
        dest.writeString(errorPassword);
        dest.writeString(noEnoughBalance);
    }
}
