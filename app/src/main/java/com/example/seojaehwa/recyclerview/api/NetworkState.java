package com.example.seojaehwa.recyclerview.api;

import android.text.TextUtils;

public final class NetworkState {

    private final Status status;
    private final String msg;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    static {
        LOADED = new NetworkState(Status.SUCCESS, null);
        LOADING = new NetworkState(Status.RUNNING, null);
    }

    private NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static NetworkState error(String msg) {
        return new NetworkState(Status.FAILED, msg);
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "NetworkState: "
                + status.name()
                + (TextUtils.isEmpty(msg) ? "" : " > " + msg);
    }

    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED
    }
}
