package ianmagallan.com.loginradius.Contract;

public interface BasicErrors {
    void onFailure(Throwable error, String errorCode);
}
