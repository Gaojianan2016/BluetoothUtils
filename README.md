# BluetoothUtils
蓝牙工具[![](https://jitpack.io/v/Gaojianan2016/BluetoothUtils.svg)](https://jitpack.io/#Gaojianan2016/BluetoothUtils)

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}


dependencies {
    implementation 'com.github.Gaojianan2016:BluetoothUtils:1.0.1'
}
```

# 基本使用

记得在清单文件加入权限
```
    <!-- 蓝牙权限 -->
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <!-- 5.0之后蓝牙需要获取gps权限 -->
    <uses-feature android:name="android.hardware.location.gps" />
    <!-- 6.0之后蓝牙需要位置权限 记得用6.0后的权限访问 -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

```
package com.gjn.bluetoothutils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gjn.bluetoothlibrary.BluethoothReceiver;
import com.gjn.bluetoothlibrary.BluetoothManager;
import com.gjn.easydialoglibrary.EasyDFragmentManager;
import com.gjn.easydialoglibrary.NormalDFragment;
import com.gjn.permissionlibrary.PermissionCallBack;
import com.gjn.permissionlibrary.PermissionUtils;
import com.gjn.universaladapterlibrary.BaseRecyclerAdapter;
import com.gjn.universaladapterlibrary.RecyclerViewHolder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv1, rv2;
    Button btn1, btn2, btn3, btn4, btn5, btn6;
    NormalDFragment loading;
    EasyDFragmentManager dFragmentManager;
    BluetoothManager bluetoothManager;

    BaseRecyclerAdapter<BluetoothDevice> adapter1, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        rv1 = findViewById(R.id.rv1);
        rv2 = findViewById(R.id.rv2);

        init();
        click();
    }

    private void init() {
        dFragmentManager = new EasyDFragmentManager(this);
        loading = dFragmentManager.getMiddleLoadingDialog();
        bluetoothManager = new BluetoothManager(this, new BluethoothReceiver.SimpleReceiverListener() {
            @Override
            public void stateChange() {
                if (bluetoothManager.getScanMode() != BluetoothAdapter.SCAN_MODE_NONE) {
                    startSearch();
                }
            }

            @Override
            public void discoveryStart(BluetoothDevice device) {
                dFragmentManager.showOnlyDialog(loading);
            }

            @Override
            public void discoveryFinish(BluetoothDevice device) {
                dFragmentManager.dismissDialog(loading);
            }

            @Override
            public void found(BluetoothDevice device, List<BluetoothDevice> devices) {
                adapter2.setData(devices);
            }

            @Override
            public void bondStateChange(BluetoothDevice device) {
                updata();
            }
        });
        //已配对
        adapter1 = new BaseRecyclerAdapter<BluetoothDevice>(this, R.layout.item_bluetooth,
                bluetoothManager.getBondedDevices()) {
            @Override
            public void bindData(RecyclerViewHolder holder, BluetoothDevice device, int i) {
                holder.setTextViewText(R.id.tv_type_iblue, BluetoothManager.getDeviceTypeStr(device));
                holder.setTextViewText(R.id.tv_name_iblue, BluetoothManager.getDeviceName(device));
                holder.setTextViewText(R.id.tv_state_iblue, BluetoothManager.getDeviceStateStr(device));
            }
        };
        //未配对
        adapter2 = new BaseRecyclerAdapter<BluetoothDevice>(this, R.layout.item_bluetooth,
                bluetoothManager.getAllDevices()) {
            @Override
            public void bindData(RecyclerViewHolder holder, BluetoothDevice device, int i) {
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    holder.setVisibility(R.id.ll_iblue, View.GONE);
                } else {
                    holder.setVisibility(R.id.ll_iblue, View.VISIBLE);
                }
                holder.setTextViewText(R.id.tv_type_iblue, BluetoothManager.getDeviceTypeStr(device));
                holder.setTextViewText(R.id.tv_name_iblue, BluetoothManager.getDeviceName(device));
                holder.setTextViewText(R.id.tv_state_iblue, BluetoothManager.getDeviceStateStr(device));
            }
        };

        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv1.setAdapter(adapter1);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setAdapter(adapter2);

        adapter1.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                final BluetoothDevice device = adapter1.getItem(i);
                dFragmentManager.showEasyNormalDialog("取消配对 " + BluetoothManager.getDeviceName(device),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BluetoothManager.removeBond(device);
                            }
                        });
            }
        });

        adapter2.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                final BluetoothDevice device = adapter2.getItem(i);
                dFragmentManager.showEasyNormalDialog("请求配对 " + BluetoothManager.getDeviceName(device),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BluetoothManager.createBond(device);
                            }
                        });
            }
        });


        if (PermissionUtils.requestPermissions(this, 0x88, PermissionUtils.PERMISSION_ACCESS_COARSE_LOCATION)) {
            startSearch();
        }
    }

    private void startSearch() {
        bluetoothManager.startSearch();
    }

    private void updata() {
        adapter1.setData(bluetoothManager.getBondedDevices());
        adapter2.setData(bluetoothManager.getAllDevices());
    }

    private void click() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.openBluetooth();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.openBluetooth(false);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.openBluetooth(true);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.changeModeConnectableDiscoverable();
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothManager.cancelSearch();
                bluetoothManager.closeBluetooth();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionCallBack() {
            @Override
            public void onSuccess(int code) {
                startSearch();
            }

            @Override
            public void onFail(int code) {
                Log.e("-s-", "onFail");

            }

            @Override
            public void onRetry(int code) {
                Log.e("-s-", "onRetry");

            }

            @Override
            public void onSetting(int code) {
                Log.e("-s-", "onSetting");
            }
        });
    }

    @Override
    protected void onDestroy() {
        bluetoothManager.unRegisterReceiver();
        super.onDestroy();
    }
}
```
