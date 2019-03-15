package com.gjn.bluetoothlibrary;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author gjn
 * @time 2019/3/12 17:49
 */

public class BluetoothManager {
    private static final String TAG = "BluetoothManager";

    public final static int REQUEST_ENABLE = 0x261;
    private final static int DEFAULT_TIME = 120;

    private BluethoothReceiver bluethoothReceiver;
    private BluetoothAdapter bluetoothAdapter;
    private OnReceiveListener receiverListener;
    private Activity activity;

    public BluetoothManager(Activity activity, OnReceiveListener receiverListener) {
        this.activity = activity;
        this.receiverListener = receiverListener;
        init();
        initReceiver();
    }

    public static String getDeviceStateStr(BluetoothDevice device) {
        String str;
        switch (device.getBondState()) {
            case BluetoothDevice.BOND_BONDED:
                str = "已配对";
                break;
            case BluetoothDevice.BOND_BONDING:
                str = "配对中";
                break;
            case BluetoothDevice.BOND_NONE:
            default:
                str = "未配对";
        }
        return str;
    }

    public static String getDeviceTypeStr(BluetoothDevice device) {
        String str;
        switch (device.getType()) {
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                str = "传统蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_LE:
                str = "低功耗蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                str = "双模蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
            default:
                str = "未知";
        }
        return str;
    }

    public static String getDeviceName(BluetoothDevice device) {
        String str;
        if (!TextUtils.isEmpty(device.getName())) {
            str = device.getName();
        } else {
            str = device.getAddress();
        }
        return str;
    }

    public static boolean isEqual(BluetoothDevice device, BluetoothDevice device2) {
        return Objects.equals(device.getAddress(), device2.getAddress());
    }

    public static boolean hasDev(List<BluetoothDevice> devices, BluetoothDevice device) {
        if (devices != null && devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                if (isEqual(device, bluetoothDevice)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void printLog(BluetoothDevice device) {
        Log.i(TAG, "{Name=" + device.getName() + "," +
                "Address=" + device.getAddress() + "," +
                "BondState=" + getDeviceStateStr(device) + "," +
                "Type=" + getDeviceTypeStr(device) +
                "}");
    }

    public static boolean createBond(BluetoothDevice device) {
        //未连接
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            return device.createBond();
        }
        return false;
    }

    public static boolean removeBond(BluetoothDevice device) {
        //已连接
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            try {
                Method method = BluetoothDevice.class.getMethod("removeBond");
                return (boolean) method.invoke(device);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                Log.e(TAG, "取消配对失败, " + e.getMessage());
            }
        }
        return false;
    }

    private void init() {
        bluethoothReceiver = new BluethoothReceiver(receiverListener);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(activity, "设备不存在蓝牙！", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "设备不存在蓝牙！");
        }
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        //找到设备的广播
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //搜索完成的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //蓝牙状态改变的广播
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //开始扫描的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //配对状态改变
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        activity.registerReceiver(bluethoothReceiver, filter);
    }

    public void unRegisterReceiver() {
        activity.unregisterReceiver(bluethoothReceiver);
    }

    public void openBluetooth(boolean isDiscoverable) {
        if (isDiscoverable) {
            openBluetooth(DEFAULT_TIME);
        } else {
            openBluetooth(0);
        }
    }

    public void openBluetooth(int timeout) {
        Intent intent;
        if (!isEnabled()) {
            if (timeout <= 0) {
                //单独打开蓝牙
                intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            } else {
                //打开蓝牙并且自己蓝牙可见 默认是120秒
                intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                //设置持续时间
                if (timeout != DEFAULT_TIME) {
                    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, timeout);
                }
            }
            activity.startActivityForResult(intent, REQUEST_ENABLE);
        }
    }

    public void openBluetooth() {
        bluetoothAdapter.enable();
    }

    public void closeBluetooth() {
        if (isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    public String getScanModeStr() {
        String str;
        switch (bluetoothAdapter.getScanMode()) {
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                str = "可连接";
                break;
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                str = "可见可连接";
                break;
            case BluetoothAdapter.SCAN_MODE_NONE:
            default:
                str = "不可用";
        }
        return str;
    }

    public int getScanMode() {
        return bluetoothAdapter.getScanMode();
    }

    public boolean isDiscovering() {
        return bluetoothAdapter.isDiscovering();
    }

    public void startSearch() {
        cancelSearch();
        bluetoothAdapter.startDiscovery();
    }

    public void cancelSearch() {
        if (isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    public boolean changeModeConnectable() {
        try {
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class);
            return (boolean) setScanMode.invoke(bluetoothAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e(TAG, "改变连接模式失败, " + e.getMessage());
        }
        return false;
    }

    public boolean changeModeConnectableDiscoverable() {
        return changeModeConnectableDiscoverable(getDiscoverableTimeout());
    }

    public boolean changeModeConnectableDiscoverable(int tiemout) {
        try {
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            return (boolean) setScanMode.invoke(bluetoothAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, tiemout);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e(TAG, "改变可见连接模式失败, " + e.getMessage());
        }
        return false;
    }

    public int getDiscoverableTimeout() {
        try {
            Method getDiscoverableTimeout = BluetoothAdapter.class.getMethod("getDiscoverableTimeout");
            return (int) getDiscoverableTimeout.invoke(bluetoothAdapter);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e(TAG, "获取默认可见时间失败, " + e.getMessage());
        }
        return 0;
    }

    public void setDiscoverableTimeout(int timeout) {
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.invoke(bluetoothAdapter, timeout);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e(TAG, "设置默认可见时间失败, " + e.getMessage());
        }
    }

    public boolean isEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public List<BluetoothDevice> getBondedDevices() {
        List<BluetoothDevice> devices = new ArrayList<>();
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            devices.add(device);
        }
        return devices;
    }

    public List<BluetoothDevice> getAllDevices() {
        return bluethoothReceiver.getDevices();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }
}
