package com.example.snake.slice;

import com.example.snake.ResourceTable;
import com.example.snake.snake.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;

import java.util.ArrayList;
import java.util.List;

public class RankAbilitySlice extends AbilitySlice {
    private Button back_btn;
    private Text fText;//第一名
    private Text sText;//第二名
    private Text tText;//第三名
    private Text cText;//当前用户排名
    private List<User> list;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_rank);
        findViews();
        back_btn = (Button)findComponentById(ResourceTable.Id_rank_back_btn);
        //获取分数
        getRand();
//        if(intent != null) {
//            List<User> userList = getRand();//获取排名信息
//            fText.setText("第一名: " + userList.get(0).getName() + " " + userList.get(0).getMaxScore());
//            sText.setText("第二名: " + userList.get(1).getName() + " " + userList.get(1).getMaxScore());
//            tText.setText("第三名: " + userList.get(2).getName() + " " + userList.get(2).getMaxScore());
//        }
        //给返回按钮设置点击事件监听器
        back_btn.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent1 = new Intent();
                Operation operation1 = new Intent.OperationBuilder().withDeviceId("").withBundleName("com.example.snake").withAbilityName("com.example.snake.MainAbility").build();
                intent1.setOperation(operation1);
                startAbility(intent1);
            }
        });
    }

    private void findViews() {
        fText = (Text) findComponentById(ResourceTable.Id_first_score);
        sText = (Text) findComponentById(ResourceTable.Id_second_score);
        tText = (Text) findComponentById(ResourceTable.Id_third_score);
    }

    //获取排名信息
    private  List<User> getRand(){
        String url2 = "http://121.196.41.49:8080/HarmonySnake/user/all";
        ZZRHttp.get(url2, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                System.out.println("errorMessagemll" + s);
            }
            @Override
            public void onResponse(String s) {
                System.out.println("response=======mll:" + s);
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, User.class);
                try {
                    list = objectMapper.readValue(s, javaType);
                    fText.setText("第一名: " + list.get(0).getName() + " " + list.get(0).getMaxScore());
                    sText.setText("第二名: " + list.get(1).getName() + " " + list.get(1).getMaxScore());
                    tText.setText("第三名: " + list.get(2).getName() + " " + list.get(2).getMaxScore());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
        return list;
    }
    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
