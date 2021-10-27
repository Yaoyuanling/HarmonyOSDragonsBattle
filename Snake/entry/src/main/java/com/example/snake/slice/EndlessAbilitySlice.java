package com.example.snake.slice;

import com.example.snake.snake.SnakeComponent;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

public class EndlessAbilitySlice extends AbilitySlice  implements IAbilityContinuation {
    private DirectionalLayout myLayout = new DirectionalLayout(this);
    private DirectionalLayout inLayout = new DirectionalLayout(this);
    private SnakeComponent snakeComponent;
    private MyEventHandler handler;
    private boolean flag;
    private final int CLICK_START = 1;
    private final int CLICK_STOP = 2;
    private final int CLICK_BACK = 3;
    private final int CLICK_TRANSFORM = 4;
    private Button btnStart;
    private Button btnStop;
    private Button backBtn;
    private Button btnTransform;
    private Text text;
    private int score = 0;
    private float startX, startY, distanceX, distanceY;//滑动相关属性
    private int rote = 600;//初始速度
    private int i = 1;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        DirectionalLayout.LayoutConfig config = new DirectionalLayout.LayoutConfig(MATCH_PARENT, MATCH_PARENT);
        config.setMargins(0, 30, 0, 0);
        myLayout.setWidth(MATCH_PARENT);
        myLayout.setHeight(MATCH_PARENT);
        myLayout.setOrientation(Component.VERTICAL);
        inLayout.setWidth(MATCH_PARENT);
        inLayout.setHeight(MATCH_CONTENT);
        inLayout.setOrientation(Component.HORIZONTAL);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(255, 117, 244));
        myLayout.setBackground(element);

        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(new RgbColor(62, 188, 202));

        findViews();

//        firstFlag = true;
        snakeComponent = new SnakeComponent(this);
        //设置按钮布局
        drawButtons();//画按钮
        clickListener();//点击事件
        snakeOnTouchEvent();//滑动事件
        EventRunner eventRunner = EventRunner.current();
        //事件处理实现类
        handler = new MyEventHandler(eventRunner);
        snakeComponent.setLayoutConfig(config);
        snakeComponent.setWidth(MATCH_PARENT);
        snakeComponent.setHeight(MATCH_PARENT);
        snakeComponent.setBackground(shapeElement);
        myLayout.addComponent(snakeComponent);

        super.setUIContent(myLayout);
    }

    private void findViews() {
        btnStart = new Button(this);
        btnStop = new Button(this);
        backBtn = new Button(this);
        btnTransform = new Button(this);
        text = new Text(this);
    }

    private void clickListener() {
        MyListener listener = new MyListener();
        btnStart.setClickedListener(listener);
        btnStop.setClickedListener(listener);
        backBtn.setClickedListener(listener);
        btnTransform.setClickedListener(listener);
        btnStart.setId(CLICK_START);
        btnStop.setId(CLICK_STOP);
        backBtn.setId(CLICK_BACK);
        btnTransform.setId(CLICK_TRANSFORM);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }



    class MyListener implements Component.ClickedListener {

        @Override
        public void onClick(Component component) {
            switch (component.getId()) {
                case CLICK_TRANSFORM:
                    //数据流转
                    if(getAvailableDeviceIds()!=null){
                        continueAbility(getAvailableDeviceIds().get(0));
                    }
                    System.out.println("没有"+getAvailableDeviceIds());
                    break;
                case CLICK_START:
                    flag = true;
                    new Thread() {
                        @Override
                        public void run() {
                            while (flag) {
                                try {
                                    //给当前线程加锁
                                    Thread.sleep(rote);
                                    System.out.println("当前速度:" + rote);
                                    score = snakeComponent.getmSnakeList().size() - 2;
                                    handler.sendEvent(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                    break;
                case CLICK_STOP:
                    flag = false;
                    System.out.println("结束");
                    break;
                case CLICK_BACK:
                    flag = false;
                    //返回主页面
                    Intent intent = new Intent();
                    Operation operation1 = new Intent.OperationBuilder().withDeviceId("").withBundleName("com.example.snake").withAbilityName("com.example.snake.MainAbility").build();
                    intent.setOperation(operation1);
                    startAbility(intent);
            }
        }
    }

    class MyEventHandler extends EventHandler {

        public MyEventHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event == null) {
                return;
            }
            int eventId = event.eventId;
            switch (eventId) {
                case 1:
                    if (snakeComponent != null) {
                        snakeComponent.invalidate();
                    }
                    if (score == i * 5) {//设置一个加速规则
                        rote = rote - 1 * 50;
                        i++;
                    }
                    text.setText("分数:" + score);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 滑动监听事件
     *
     * @return
     */
    public void snakeOnTouchEvent() {
        snakeComponent.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                MmiPoint point = touchEvent.getPointerScreenPosition(0);
                switch (touchEvent.getAction()) {
                    case TouchEvent.PRIMARY_POINT_DOWN:
                        startX = point.getX();
                        startY = point.getY();
                        break;
                    case TouchEvent.PRIMARY_POINT_UP:
                        distanceX = point.getX() - startX;
                        distanceY = point.getY() - startY;
                        break;
                }
                if (snakeComponent != null) {
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {
                        if (distanceX > 200 && snakeComponent.getmSnakeDirection() != 3) {
                            //右移
                            snakeComponent.setmSnakeDirection(4);
                        } else if (distanceX < -200 && snakeComponent.getmSnakeDirection() != 4) {
                            //左移
                            snakeComponent.setmSnakeDirection(3);
                        }
                    } else if (Math.abs(distanceX) < Math.abs(distanceY)) {
                        if (distanceY > 200 && snakeComponent.getmSnakeDirection() != 1) {
                            //下移
                            snakeComponent.setmSnakeDirection(2);
                        } else if (distanceY < -200 && snakeComponent.getmSnakeDirection() != 2) {
                            //上移
                            snakeComponent.setmSnakeDirection(1);
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 画按钮
     */
    public void drawButtons() {
        ShapeElement background0 = new ShapeElement();
        background0.setRgbColor(new RgbColor(117, 217, 255));
        background0.setCornerRadius(120);

        text.setText("分数:0");
        text.setTextSize(70);
        text.setMarginLeft(50);
        text.setMarginTop(20);
        text.setBackground(background0);

        btnStart.setText("开始");
        btnStart.setTextSize(70);
        btnStart.setWidth(200);
        btnStart.setMarginLeft(300);
        btnStart.setMarginTop(20);
        btnStart.setBackground(background0);

        btnStop.setText("暂停");
        btnStop.setWidth(200);
        btnStop.setTextSize(70);
        btnStop.setMarginLeft(300);
        btnStop.setMarginTop(20);
        btnStop.setBackground(background0);



        backBtn.setText("返回");
        backBtn.setTextSize(70);
        backBtn.setWidth(200);
        backBtn.setMarginLeft(300);
        backBtn.setMarginTop(20);
        backBtn.setBackground(background0);

        btnTransform.setText("流转");
        btnTransform.setTextSize(70);
        btnTransform.setMarginTop(20);
        btnTransform.setMarginLeft(150);
        btnTransform.setBackground(background0);

        btnStart.setId(CLICK_START);
        btnStop.setId(CLICK_STOP);
        backBtn.setId(CLICK_BACK);
        inLayout.addComponent(text);
        inLayout.addComponent(btnStart);
        inLayout.addComponent(btnStop);
        inLayout.addComponent(backBtn);
        inLayout.addComponent(btnTransform);

        myLayout.addComponent(inLayout);
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
            System.out.println("没有当前设备");
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

//    public boolean isFirstFlag() {
//        return firstFlag;
//    }
}
