package com.example.snake.slice;

import com.example.snake.ResourceTable;
import com.example.snake.snake.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import com.zzrv5.mylibrary.ZZRResponse;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;


public class MainAbilitySlice extends AbilitySlice implements IAbilityContinuation {
    private Button endless_btn;
    private Button time_btn;
    private Button btn_okhttp;
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        endless_btn = (Button) findComponentById(ResourceTable.Id_endless_btn);
        time_btn = (Button) findComponentById(ResourceTable.Id_time_btn);
        btn_okhttp = (Button)findComponentById(ResourceTable.Id_wander_btn);
        clickListener();
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

    class MyListener implements Component.ClickedListener {
        @Override
        public void onClick(Component component) {
            Intent intent = new Intent();
            switch (component.getId()) {
                case ResourceTable.Id_endless_btn:
                    System.out.println("????????????");
                    Operation operation1 = new Intent.OperationBuilder()//??????Operation
                            .withDeviceId("")//????????????
                            .withBundleName("com.example.snake")
                            .withAbilityName("com.example.snake.EndlessAbility")
                            .build();
                    intent.setOperation(operation1);
                    startAbility(intent);
                    break;
                case ResourceTable.Id_time_btn:
                    System.out.println("????????????");
                    Operation operation2 = new Intent.OperationBuilder()
                            .withDeviceId("")//????????????
                            .withBundleName("com.example.snake")
                            .withAbilityName("com.example.snake.TimeAbility")
                            .build();
                    intent.setOperation(operation2);
                    startAbility(intent);
                    break;
                case ResourceTable.Id_wander_btn:
                    System.out.println("mll"+getAvailableDeviceIds());
                    if(getAvailableDeviceIds()!=null){
                        continueAbility(getAvailableDeviceIds().get(0));
                    }

//
//                    String url = "https://zzrv5.github.io/test/zzr.html";
//                    String url1 = "http://192.168.1.102:8080/OkHttpServerDemo/helloServlet";
//                    String url2 = "http://192.168.137.1:8080/HarmonySnake/user/list";
//                    String url3 = "http://192.168.137.1:8080/HarmonySnake/user/save";
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    User user = new User();
//                    user.setMaxScore(102);user.setName("zzaxasca");
//                    try {
//                        String json = objectMapper.writeValueAsString(user);
//                        ZZRHttp.postJson(url3, json, new ZZRCallBack.CallBackString() {
//                            @Override
//                            public void onFailure(int i, String s) {
//                                System.out.println("????????????");
//                            }
//
//                            @Override
//                            public void onResponse(String s) {
//                                System.out.println("????????????");
//                            }
//                        });
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    }

//                    ZZRHttp.get(url2, new ZZRCallBack.CallBackString() {
//                        @Override
//                        public void onFailure(int i, String s) {
//                            //http??????????????????????????????????????????????????????;
////                            //????????????UI?????????,??????????????????????????????
//                            System.out.println("errorMessagemll"+s);
//                        }
//
//                        @Override
//                        public void onResponse(String s) {
//                            System.out.println("response=======mll:"+s);
//                            ObjectMapper objectMapper = new ObjectMapper();
//                            try {
//                                User user = objectMapper.readValue(s, User.class);
//                                System.out.println("mll"+user);
//                            } catch (JsonProcessingException e) {
//                                e.printStackTrace();
//                            }
////                            //http???????????????????????????????????????????????????;
////                            //????????????UI?????????????????????????????????????????????
//                        }
//                    });
                    break;
            }
        }
    }

    private void clickListener() {
        MyListener listener = new MyListener();
        endless_btn.setClickedListener(listener);
        time_btn.setClickedListener(listener);
        btn_okhttp.setClickedListener(listener);

    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onBackground() {
        super.onBackground();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    /**
     * ???????????????????????????????????????ID
     * @return ??????ID??????
     */
    public static List<String> getAvailableDeviceIds() {
        // ??????DeviceInfo???????????????????????????????????????????????????
        List<DeviceInfo> deviceInfoList = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        // ??????DeviceInfo?????????????????????
        if (deviceInfoList == null || deviceInfoList.size() == 0) {
            return null;
        }
        // ??????DeviceInfo??????????????????????????????ID
        List<String> deviceIds = new ArrayList<>();
        for (DeviceInfo deviceInfo : deviceInfoList) {
            deviceIds.add(deviceInfo.getDeviceId());
        }
        // ?????????????????????ID
        return deviceIds;
    }
}
