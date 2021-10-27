package com.example.snake;

import com.example.snake.slice.TimeAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;

import java.util.ArrayList;
import java.util.List;

public class TimeAbility extends Ability implements IAbilityContinuation {
    @Override
    public void onStart(Intent intent) {
        requestPermissionsFromUser(new String[]{
                "ohos.permission.DISTRIBUTED_DATASYNC",
                "ohos.permission.servicebus.ACCESS_SERVICE",
                "com.huawei.hwddmp.servicebus.BIND_SERVICE"
        },0);
        super.onStart(intent);
        super.setMainRoute(TimeAbilitySlice.class.getName());
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

    }
}
