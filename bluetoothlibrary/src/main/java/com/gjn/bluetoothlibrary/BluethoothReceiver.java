package com.gjn.bluetoothlibrary;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gjn
 * @time 2019/3/12 17:50
 */

public class BluethoothReceiver extends BroadcastReceiver {

    private OnReceiveListener onReceiveListener;
    //用于保存获取到全部设备  （已去重）
    private List<BluetoothDevice> devices;

    public BluethoothReceiver(OnReceiveListener onReceiveListener) {
        this.onReceiveListener = onReceiveListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                //蓝牙状态改变
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    if (onReceiveListener != null) {
                        onReceiveListener.stateChange();
                    }
                    break;
                //开始扫描
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    devices = new ArrayList<>();
                    if (onReceiveListener != null) {
                        onReceiveListener.discoveryStart(bluetoothDevice);
                    }
                    break;
                //搜索完成
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    if (onReceiveListener != null) {
                        onReceiveListener.discoveryFinish(bluetoothDevice);
                    }
                    break;
                //找到设备
                case BluetoothDevice.ACTION_FOUND:
                    if (!BluetoothManager.hasDev(devices, bluetoothDevice)) {
                        devices.add(bluetoothDevice);
                    }
                    if (onReceiveListener != null) {
                        onReceiveListener.found(bluetoothDevice, devices);
                    }
                    break;
                //配对状态改变
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    for (int i = 0; i < devices.size(); i++) {
                        if (BluetoothManager.isEqual(bluetoothDevice, devices.get(i))) {
                            devices.set(i, bluetoothDevice);
                        }
                    }
                    if (onReceiveListener != null) {
                        onReceiveListener.bondStateChange(bluetoothDevice);
                    }
                    break;
                default:
            }
        }
    }

    public List<BluetoothDevice> getDevices(){
        return devices;
    }

    public static abstract class SimpleReceiverListener implements OnReceiveListener {

        @Override
        public void stateChange() {
        }

        @Override
        public void discoveryStart(BluetoothDevice device) {
        }

        @Override
        public void discoveryFinish(BluetoothDevice device) {
        }
    }
}
