package com.example.snake;

import com.example.snake.slice.MainAbilitySlice;
import com.example.snake.snake.SnakeComponent;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;

public class MainAbility extends Ability implements IAbilityContinuation {
    private SnakeComponent snakeComponent;
    private Button btnContinue;
    @Override
    public void onStart(Intent intent) {
        requestPermissionsFromUser(new String[]{
                "ohos.permission.DISTRIBUTED_DATASYNC",
                "ohos.permission.servicebus.ACCESS_SERVICE",
                "com.huawei.hwddmp.servicebus.BIND_SERVICE"
        },0);
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
//        btnContinue = (Button) findComponentById(ResourceTable.Id_btn);
//        btnContinue.setClickedListener(new Component.ClickedListener() {
//            @Override
//            public void onClick(Component component) {
//                continueAbility(getAvailableDeviceIds().get(0));
//            }
//        });
//        requestPermission();
    }

    /**
     * 流转，申请权限
     */
    private void requestPermission() {
        String[] permission = {
                "ohos.permission.DISTRIBUTED_DATASYNC",
                "ohos.permission.GET_DISTRIBUTED_DEVICE_INFO"};
        List<String> applyPermissions = new ArrayList<>();
        for (String element : permission) {
            if (verifySelfPermission(element) != 0) {
                if (canRequestPermission(element)) {
                    applyPermissions.add(element);
                } else {
                }
            } else {
            }
        }
        requestPermissionsFromUser(new String[]{
                "ohos.permission.DISTRIBUTED_DATASYNC",
                "ohos.permission.servicebus.ACCESS_SERVICE",
                "com.huawei.hwddmp.servicebus.BIND_SERVICE"
        },0);
    }

    @Override
    public boolean onStartContinuation() {
        return true;
    }

    @Override
    public boolean onSaveData(IntentParams intentParams) {
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams intentParams) {
        return true;
    }

    @Override
    public void onCompleteContinuation(int i) {
        terminateAbility();
    }
    /**
     * 获得所有已经连接的所有设备ID
     * @return 设备ID列表
     */
    public static List<String> getAvailableDeviceIds() {
        // 获得DeviceInfo列表，包含了已经连接的所有设备信息
        List<DeviceInfo> deviceInfoList = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        // 如果DeviceInfo列表为空则返回
        if (deviceInfoList == null || deviceInfoList.size() == 0) {
            return null;
        }
        // 遍历DeviceInfo列表，获得所有的设备ID
        List<String> deviceIds = new ArrayList<>();
        for (DeviceInfo deviceInfo : deviceInfoList) {
            deviceIds.add(deviceInfo.getDeviceId());
        }
        // 返回所有的设备ID
        return deviceIds;
    }
}
