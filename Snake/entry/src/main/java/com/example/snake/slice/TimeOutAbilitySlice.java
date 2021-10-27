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
import ohos.agp.components.TextField;

import java.util.ArrayList;
import java.util.List;

public class TimeOutAbilitySlice extends AbilitySlice {
    private Text text;
    private Button button;
    private Button btn_okhttp;
    private TextField textField;
    private String score;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_time_out);
        findViews();
        //获取分数
        if(intent != null) {
            score = intent.getStringParam("score");
            System.out.println(score);
            if (score == null) {
                score = "0";
            }
            text.setText("当前得分:" + score);
        }
        //给返回按钮设置点击事件监听器
        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent1 = new Intent();
                Operation operation1 = new Intent.OperationBuilder().withDeviceId("").withBundleName("com.example.snake").withAbilityName("com.example.snake.MainAbility").build();
                intent1.setOperation(operation1);
                startAbility(intent1);
            }
        });
        btn_okhttp.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                String url3 = "http://121.196.41.49:8080/HarmonySnake/user/save";
                ObjectMapper objectMapper = new ObjectMapper();
                User user = new User();
                user.setMaxScore(Integer.parseInt(score));
                user.setName(textField.getText());
                try {
                    String json = objectMapper.writeValueAsString(user);
                    ZZRHttp.postJson(url3, json, new ZZRCallBack.CallBackString() {
                        @Override
                        public void onFailure(int i, String s) {
                            System.out.println("登录失败");
                        }
                        @Override
                        public void onResponse(String s) {
                            System.out.println("登录成功");
                        }
                    });
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                //跳转到排名页面
                Intent intent = new Intent();
                present(new RankAbilitySlice(),intent);

            }
        });

    }

    private void  findViews(){
        text = (Text) findComponentById(ResourceTable.Id_score_time);
        button = (Button) findComponentById(ResourceTable.Id_time_back_btn);
        textField = (TextField)findComponentById(ResourceTable.Id_user_info);
        btn_okhttp = (Button)findComponentById(ResourceTable.Id_time_okhttp);
    }

    //获取排名信息
    private  void getRand(){
        String url2 = "http://192.168.137.1:8080/HarmonySnake/user/all";
        ZZRHttp.get(url2, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                System.out.println("errorMessagemll" + s);
            }
            @Override
            public void onResponse(String s) {
                System.out.println("response=======mll:" + s);
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class,User.class);
                try {
                    List<User> list = objectMapper.readValue(s, javaType);
                    System.out.println("mll" + list);
                    //跳转到排名页面
                    Intent intent = new Intent();
                    intent.setParam("fName",list.get(0).getName());//第一名
                    intent.setParam("sName",list.get(1).getName());//第二名
                    intent.setParam("tName",list.get(2).getName());//第三名
                    intent.setParam("cName",textField.getText());//当前用户名
                    present(new RankAbilitySlice(),intent);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
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
