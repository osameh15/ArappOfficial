package ir.arapp.arappofficial.Data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class UserData implements Parcelable {

    protected UserData(Parcel in) {
        name = in.readString();
        province = in.readString();
        city = in.readString();
        notification_receiver = in.readInt();
        avatar = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", notification_receiver='" + notification_receiver + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    @Nullable
    @SerializedName("name")
    private String name;
    @SerializedName("province")
    @Nullable
    private String province;
    @SerializedName("city")
    @Nullable
    private String city;
    @SerializedName("notification_receiver")
    private int notification_receiver;
    @SerializedName("avatar")
    @Nullable
    private String avatar;

    public UserData(@Nullable String name, @Nullable String province, @Nullable String city, @Nullable int notification_receiver, @Nullable String avatar) {
        this.name = name;
        this.province = province;
        this.city = city;
        this.notification_receiver = notification_receiver;
        this.avatar = avatar;
    }

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getProvince() {
        return province;
    }

    public void setProvince(@Nullable String province) {
        this.province = province;
    }

    @Nullable
    public String getCity() {
        return city;
    }

    public void setCity(@Nullable String city) {
        this.city = city;
    }

    @Nullable
    public int getNotification_receiver() {
        return notification_receiver;
    }

    public void setNotification_receiver(@Nullable int notification_receiver) {
        this.notification_receiver = notification_receiver;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(province);
        parcel.writeString(city);
        parcel.writeInt(notification_receiver);
        parcel.writeString(avatar);
    }
}
