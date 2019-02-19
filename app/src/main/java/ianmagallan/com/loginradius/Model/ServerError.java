package ianmagallan.com.loginradius.Model;

import com.google.gson.annotations.SerializedName;

public class ServerError {
    @SerializedName("Description")
    public String mDescription;

    @SerializedName("Message")
    public String mMessage;

    @SerializedName("ErrorCode")
    public int mErrorCode;
}
