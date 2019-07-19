package com.gjn.bluetoothlibrary;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

/**
 * @author gjn
 * @time 2019/5/10 10:51
 */

public class BluetoothConstant {

    public static String getBluetoothType(BluetoothDevice device){
        int type = device.getType();
        String reslut;
        switch (type) {
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                reslut = "传统蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_LE:
                reslut = "低功耗蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                reslut = "双模蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
            default:
                reslut="未知";
        }
        return reslut;
    }

    public static String getDeviceState(BluetoothDevice device){
        int deviceState = device.getBondState();
        String reslut;
        switch (deviceState) {
            case BluetoothDevice.BOND_BONDED:
                reslut = "已配对";
                break;
            case BluetoothDevice.BOND_BONDING:
                reslut = "配对中";
                break;
            case BluetoothDevice.BOND_NONE:
            default:
                reslut="未配对";
        }
        return reslut;
    }

    public static String getStyleContent(BluetoothDevice device){
        int styleMajor = device.getBluetoothClass().getMajorDeviceClass();
        String reslut;
        switch (styleMajor) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO://音频设备
                reslut = "音配设备";
                break;
            case BluetoothClass.Device.Major.COMPUTER://电脑
                reslut = "电脑";
                break;
            case BluetoothClass.Device.Major.HEALTH://健康状况
                reslut = "健康状况";
                break;
            case BluetoothClass.Device.Major.IMAGING://镜像，映像
                reslut = "镜像";
                break;
            case BluetoothClass.Device.Major.MISC://麦克风
                reslut = "麦克风";
                break;
            case BluetoothClass.Device.Major.NETWORKING://网络
                reslut = "网络";
                break;
            case BluetoothClass.Device.Major.PERIPHERAL://外部设备
                reslut = "外部设备";
                break;
            case BluetoothClass.Device.Major.PHONE://电话
                reslut = "电话";
                break;
            case BluetoothClass.Device.Major.TOY://玩具
                reslut = "玩具";
                break;
            case BluetoothClass.Device.Major.UNCATEGORIZED://未知的
                reslut = "未知的";
                break;
            case BluetoothClass.Device.Major.WEARABLE://穿戴设备
                reslut = "穿戴设备";
                break;
            default:
                reslut = "其他";
        }
        return reslut;
    }

    public static String getDeviceType(BluetoothDevice device) {
        int deviceType =device.getBluetoothClass().getDeviceClass();
        String reslut;
        switch (deviceType) {
            case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER://录像机
                reslut = "录像机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                reslut = "车载设备";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                reslut = "蓝牙耳机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER:
                reslut = "扬声器";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE:
                reslut = "麦克风";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO:
                reslut = "打印机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX:
                reslut = "BOX";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED:
                reslut = "未知的";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VCR:
                reslut = "录像机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA:
                reslut = "照相机录像机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING:
                reslut = "conferencing";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
                reslut = "显示器和扬声器";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY:
                reslut = "游戏";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR:
                reslut = "显示器";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                reslut = "可穿戴设备";
                break;
            case BluetoothClass.Device.PHONE_CELLULAR:
                reslut = "手机";
                break;
            case BluetoothClass.Device.PHONE_CORDLESS:
                reslut = "无线电设备";
                break;
            case BluetoothClass.Device.PHONE_ISDN:
                reslut = "手机服务数据网";
                break;
            case BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY:
                reslut = "手机调节器";
                break;
            case BluetoothClass.Device.PHONE_SMART:
                reslut = "手机卫星";
                break;
            case BluetoothClass.Device.PHONE_UNCATEGORIZED:
                reslut = "未知手机";
                break;
            case BluetoothClass.Device.WEARABLE_GLASSES:
                reslut = "可穿戴眼睛";
                break;
            case BluetoothClass.Device.WEARABLE_HELMET:
                reslut = "可穿戴头盔";
                break;
            case BluetoothClass.Device.WEARABLE_JACKET:
                reslut = "可穿戴上衣";
                break;
            case BluetoothClass.Device.WEARABLE_PAGER:
                reslut = "客串点寻呼机";
                break;
            case BluetoothClass.Device.WEARABLE_UNCATEGORIZED:
                reslut = "未知的可穿戴设备";
                break;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                reslut = "手腕监听设备";
                break;
            case BluetoothClass.Device.TOY_CONTROLLER:
                reslut = "可穿戴设备";
                break;
            case BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE:
                reslut = "玩具doll_action_figure";
                break;
            case BluetoothClass.Device.TOY_GAME:
                reslut = "游戏";
                break;
            case BluetoothClass.Device.TOY_ROBOT:
                reslut = "玩具遥控器";
                break;
            case BluetoothClass.Device.TOY_UNCATEGORIZED:
                reslut = "玩具未知设备";
                break;
            case BluetoothClass.Device.TOY_VEHICLE:
                reslut = "vehicle";
                break;
            case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE:
                reslut = "健康状态-血压";
                break;
            case BluetoothClass.Device.HEALTH_DATA_DISPLAY:
                reslut = "健康状态数据";
                break;
            case BluetoothClass.Device.HEALTH_GLUCOSE:
                reslut = "健康状态葡萄糖";
                break;
            case BluetoothClass.Device.HEALTH_PULSE_OXIMETER:
                reslut = "健康状态脉搏血氧计";
                break;
            case BluetoothClass.Device.HEALTH_PULSE_RATE:
                reslut = "健康状态脉搏速率";
                break;
            case BluetoothClass.Device.HEALTH_THERMOMETER:
                reslut = "健康状态体温计";
                break;
            case BluetoothClass.Device.HEALTH_WEIGHING:
                reslut = "健康状态体重";
                break;
            case BluetoothClass.Device.HEALTH_UNCATEGORIZED:
                reslut = "未知健康状态设备";
                break;
            case BluetoothClass.Device.COMPUTER_DESKTOP:
                reslut = "电脑桌面";
                break;
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
                reslut = "手提电脑或Pad";
                break;
            case BluetoothClass.Device.COMPUTER_LAPTOP:
                reslut = "便携式电脑";
                break;
            case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA:
                reslut = "微型电脑";
                break;
            case BluetoothClass.Device.COMPUTER_SERVER:
                reslut = "电脑服务";
                break;
            case BluetoothClass.Device.COMPUTER_UNCATEGORIZED:
                reslut = "未知的电脑设备";
                break;
            case BluetoothClass.Device.COMPUTER_WEARABLE:
                reslut = "可穿戴的电脑";
                break;
            default:
                reslut="其他";
        }
        return reslut;
    }
}
