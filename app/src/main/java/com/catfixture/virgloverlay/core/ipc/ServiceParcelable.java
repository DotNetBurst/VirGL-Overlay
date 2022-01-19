package com.catfixture.virgloverlay.core.ipc;

import android.os.Parcel;
import android.os.Parcelable;

import com.catfixture.virgloverlay.core.IService;

public class ServiceParcelable implements Parcelable {
    public final int id;
    public final int state;
    public final int fd;
    public final String threadName;

    protected ServiceParcelable(Parcel in) {
        int[] ints = new int[3];
        in.readIntArray(ints);
        id = ints[0];
        state = ints[1];
        fd = ints[2];
        threadName = in.readString();
    }

    public ServiceParcelable(IService service) {
        this.id = service.GetId();
        this.state = service.GetServiceState();
        this.fd = service.GetFD();
        this.threadName = service.GetThreadName();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[] {id, state, fd});
        dest.writeString(threadName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceParcelable> CREATOR = new Creator<ServiceParcelable>() {
        @Override
        public ServiceParcelable createFromParcel(Parcel in) {
            return new ServiceParcelable(in);
        }

        @Override
        public ServiceParcelable[] newArray(int size) {
            return new ServiceParcelable[size];
        }
    };
}
