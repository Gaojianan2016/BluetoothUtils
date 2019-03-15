package com.gjn.bluetoothlibrary;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * @author gjn
 * @time 2019/3/13 10:16
 */

public interface OnReceiveListener {
    void stateChange();

    void discoveryStart(BluetoothDevice device);

    void discoveryFinish(BluetoothDevice device);

    void found(BluetoothDevice device, List<BluetoothDevice> devices);

    void bondStateChange(BluetoothDevice device);
}
