package com.example.snake.snake;

import com.example.snake.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.global.resource.NotExistException;


import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;


public class SnakeComponent extends Component implements Component.ScrolledListener {
    public static final String TAG = "SnakeView";

    private int mWidth = 2200;  //view的宽
    private int mHeight = 800; //View的高
    private static final int sXOffset = 0;
    private static final int sYOffset = 0; // X坐标和Y坐标的偏移量，可以修改来缩小游戏范围

    private int BOXWIDTH = 30; //食物的边长，蛇体的宽度
    private Random mRandom = new Random(); //用于产生随机数
    private Point mFoodPosition; //食物的位置
    private boolean mIsFoodDone; //食物是否已经被吃掉

    private ArrayList<Point> mSnakeList;  //蛇体可以看做是很多食物组成的
    private Paint mSnakePaint;  //用于画蛇的画笔
    private int mSnakeDirection = 0; //蛇体运动的方向
    private final int UP = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;
    private boolean firstFlag ;

    private Paint mBgPaint;//游戏背景画笔
    private Paint mFoodPaint;//食物画笔
    private Paint paint;
    private int snakeFood; // 记录当前食物的位置，横坐标*100+纵坐标
    private LinkedList<Integer> snakeArray; // 记录蛇的位置信息，每一节的坐标和食物的一致，横坐标*100+纵坐标
    private DrawTask task = new DrawTask() {
        @Override
        public void onDraw(Component component, Canvas canvas) {
            try {
                drawBg(canvas, mBgPaint);  //画背景
//                if(mIsFoodDone){
                    drawFood(canvas, mFoodPaint);//画食物
//                }
                drawSnake(canvas, mSnakePaint); //画蛇
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotExistException e) {
                e.printStackTrace();
            }
        }

    };


    public SnakeComponent(Context context) {
        super(context);
        mSnakeList = new ArrayList<Point>();
        Point point = new Point();
        mSnakeList.add(point);
        mSnakePaint = new Paint();
        //蛇的颜色
        mSnakePaint.setColor(Color.GREEN);
        mSnakePaint.setStyle(Paint.Style.FILLANDSTROKE_STYLE);
        //蛇初始位置
        mSnakeList.add(new Point(100, 130));
        firstFlag = true;

        mSnakeDirection = RIGHT;
        mIsFoodDone = true;
        mFoodPosition = new Point();

        //食物的颜色
        mFoodPaint = new Paint();
        mFoodPaint.setColor(Color.CYAN);
        mFoodPaint.setStyle(Paint.Style.FILL_STYLE);

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.YELLOW);
        mBgPaint.setStyle(Paint.Style.FILL_STYLE);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);   //初始化各种画笔
        addDrawTask(task);
    }

    @Override
    public void addDrawTask(DrawTask task) {
        super.addDrawTask(task);
        task.onDraw(this, mCanvasForTaskOverContent);
    }

    //画背景 这里通过sXOffset, sYOffset可以实现对蛇活动区域的限制
    private void drawBg(Canvas canvas, Paint paint) {
        //设置背景颜色
        paint.setColor(Color.BLUE);
//        RectFloat rect = new RectFloat(sXOffset, sYOffset, mWidth - sXOffset, mHeight - sYOffset);
//        canvas.drawRect(rect, paint);
    }

    /**
     * 画蛇
     *
     * @param canvas
     * @param paint
     * @throws IOException
     * @throws NotExistException
     */
    private void drawSnake(Canvas canvas, Paint paint) throws IOException, NotExistException {
        RectFloat rect = null;
        Point point0 = mSnakeList.get(0);
        InputStream inputStream = null;
        ImageSource.SourceOptions sourceOptions = null;
        ImageSource imageSource = null;
        ImageSource.DecodingOptions decodingOptions = null;
        PixelMap pixelMap = null;
        PixelMapHolder pixelMapHolder = null;
        switch (mSnakeDirection) {
            case UP:
                //画蛇头
                rect = new RectFloat(point0.getPointX(), point0.getPointY(), point0.getPointX() + BOXWIDTH, point0.getPointY() + BOXWIDTH);
                //获取图片输入流
                inputStream = getContext().getResourceManager().getResource(ResourceTable.Media_head_top);//向上的蛇头
                sourceOptions = new ImageSource.SourceOptions();
                //设置格式
                sourceOptions.formatHint = "image/png";
                //获取图片资源
                imageSource = ImageSource.create(inputStream, sourceOptions);
                //创建DecodingOptions对象用来创建PixeMap对象
                decodingOptions = new ImageSource.DecodingOptions();
                //获取PixeMap对象
                pixelMap = imageSource.createPixelmap(decodingOptions);
                //获取PixelMapHolder对象
                pixelMapHolder = new PixelMapHolder(pixelMap);
                //把蛇头画在画布上
                canvas.drawPixelMapHolderRect(pixelMapHolder, rect, paint);
                //画蛇身体
                for (int i = 1; i < mSnakeList.size(); i++) {
                    Point point = mSnakeList.get(i);
                    RectFloat rect1 = new RectFloat(point.getPointX(), point.getPointY(), point.getPointX() + BOXWIDTH, point.getPointY() + BOXWIDTH);
                    //获取图片输入流
                    InputStream inputStream1;
                    if (i == mSnakeList.size() - 1) {
                        inputStream1 = getContext().getResourceManager().getResource(ResourceTable.Media_tail_top);//尾巴
                    } else {
                        inputStream1 = getContext().getResourceManager().getResource(ResourceTable.Media_body_top);//身体
                    }
                    ImageSource.SourceOptions sourceOptions1 = new ImageSource.SourceOptions();
                    //设置格式
                    sourceOptions.formatHint = "image/png";
                    //获取图片资源
                    ImageSource imageSource1 = ImageSource.create(inputStream1, sourceOptions1);
                    //创建DecodingOptions对象用来创建PixeMap对象
                    ImageSource.DecodingOptions decodingOptions1 = new ImageSource.DecodingOptions();
                    //获取PixeMap对象
                    PixelMap pixelMap1 = imageSource1.createPixelmap(decodingOptions1);
                    //获取PixelMapHolder对象
                    PixelMapHolder pixelMapHolder1 = new PixelMapHolder(pixelMap1);
                    //把蛇头画在画布上
                    canvas.drawPixelMapHolderRect(pixelMapHolder1, rect1, paint);
                }
                break;
            case DOWN:
                //画蛇头
                rect = new RectFloat(point0.getPointX(), point0.getPointY(), point0.getPointX() + BOXWIDTH, point0.getPointY() + BOXWIDTH);
                //获取图片输入流
                inputStream = getContext().getResourceManager().getResource(ResourceTable.Media_head_down);
                sourceOptions = new ImageSource.SourceOptions();
                //设置格式
                sourceOptions.formatHint = "image/png";
                //获取图片资源
                imageSource = ImageSource.create(inputStream, sourceOptions);
                //创建DecodingOptions对象用来创建PixeMap对象
                decodingOptions = new ImageSource.DecodingOptions();
                //获取PixeMap对象
                pixelMap = imageSource.createPixelmap(decodingOptions);
                //获取PixelMapHolder对象
                pixelMapHolder = new PixelMapHolder(pixelMap);
                //把蛇头画在画布上
                canvas.drawPixelMapHolderRect(pixelMapHolder, rect, paint);
                //画蛇身体
                for (int i = 1; i < mSnakeList.size(); i++) {
                    Point point = mSnakeList.get(i);
                    RectFloat rect1 = new RectFloat(point.getPointX(), point.getPointY(), point.getPointX() + BOXWIDTH, point.getPointY() + BOXWIDTH);
                    //获取图片输入流
                    InputStream inputStream1;
                    if (i == mSnakeList.size() - 1) {
                        inputStream1 = getContext().getResourceManager().getResource(ResourceTable.Media_tail_down);//尾巴
                    } else {
                        inputStream1 = getContext().getResourceManager().getResource(ResourceTable.Media_body_down);//身体
                    }
                    ImageSource.SourceOptions sourceOptions1 = new ImageSource.SourceOptions();
                    //设置格式
                    sourceOptions.formatHint = "image/png";
                    //获取图片资源
                    ImageSource imageSource1 = ImageSource.create(inputStream1, sourceOptions1);
                    //创建DecodingOptions对象用来创建PixeMap对象
                    ImageSource.DecodingOptions decodingOptions1 = new ImageSource.DecodingOptions();
                    //获取PixeMap对象
                    PixelMap pixelMap1 = imageSource1.createPixelmap(decodingOptions1);
                    //获取PixelMapHolder对象
                    PixelMapHolder pixelMapHolder1 = new PixelMapHolder(pixelMap1);
                    //把蛇头画在画布上
                    canvas.drawPixelMapHolderRect(pixelMapHolder1, rect1, paint);
                }
                break;
            case LEFT:
                //画蛇头
                rect = new RectFloat(point0.getPointX(), point0.getPointY(), point0.getPointX() + BOXWIDTH, point0.getPointY() + BOXWIDTH);
                //获取图片输入流
                inputStream = getContext().getResourceManager().getResource(ResourceTable.Media_head_left);//向左的蛇头
                sourceOptions = new ImageSource.SourceOptions();
                //设置格式
                sourceOptions.formatHint = "image/png";
                //获取图片资源
                imageSource = ImageSource.create(inputStream, sourceOptions);
                //创建DecodingOptions对象用来创建PixeMap对象
                decodingOptions = new ImageSource.DecodingOptions();
                //获取PixeMap对象
                pixelMap = imageSource.createPixelmap(decodingOptions);
                //获取PixelMapHolder对象
                pixelMapHolder = new PixelMapHolder(pixelMap);
                //把蛇头画在画布上
                canvas.drawPixelMapHolderRect(pixelMapHolder, rect, paint);
                //画蛇身体
                for (int i = 1; i < mSnakeList.size(); i++) {
                    Point point = mSnakeList.get(i);
                    RectFloat rect1 = new RectFloat(point.getPointX(), point.getPointY(), point.getPointX() + BOXWIDTH, point.getPointY() + BOXWIDTH);
                    //获取图片输入流
                    InputStream inputStream1;
                    if (i == mSnakeList.size() - 1) {
                        inputStream1 = getContext().getResourceManager().getResource(ResourceTable.Media_tail_left);//尾巴
                    } else {
                        inputStream1 = getContext().getResourceManager().getResource(ResourceTable.Media_body_left);//身体
                    }
                    ImageSource.SourceOptions sourceOptions1 = new ImageSource.SourceOptions();
                    //设置格式
                    sourceOptions.formatHint = "image/png";
                    //获取图片资源
                    ImageSource imageSource1 = ImageSource.create(inputStream1, sourceOptions1);
                    //创建DecodingOptions对象用来创建PixeMap对象
                    ImageSource.DecodingOptions decodingOptions1 = new ImageSource.DecodingOptions();
                    //获取PixeMap对象
                    PixelMap pixelMap1 = imageSource1.createPixelmap(decodingOptions1);
                    //获取PixelMapHolder对象
                    PixelMapHolder pixelMapHolder1 = new PixelMapHolder(pixelMap1);
                    //把蛇头画在画布上
                    canvas.drawPixelMapHolderRect(pixelMapHolder1, rect1, paint);
                }
                break;
            case RIGHT:
                //画蛇头
                rect = new RectFloat(point0.getPointX(), point0.getPointY(), point0.getPointX() + BOXWIDTH, point0.getPointY() + BOXWIDTH);
                //获取图片输入流
                inputStream = getContext().getResourceManager().getResource(ResourceTable.Media_head_right);//向上的蛇头
                sourceOptions = new ImageSource.SourceOptions();
                //设置格式
                sourceOptions.formatHint = "image/png";
                //获取图片资源
                imageSource = ImageSource.create(inputStream, sourceOptions);
                //创建DecodingOptions对象用来创建PixeMap对象
                decodingOptions = new ImageSource.DecodingOptions();
                //获取PixeMap对象
                pixelMap = imageSource.createPixelmap(decodingOptions);
                //获取PixelMapHolder对象
                pixelMapHolder = new PixelMapHolder(pixelMap);
                //把蛇头画在画布上
                canvas.drawPixelMapHolderRect(pixelMapHolder, rect, paint);
                //画蛇身体
                for (int i = 1; i < mSnakeList.size(); i++) {
                    Point point = mSnakeList.get(i);
                    RectFloat rect1 = new RectFloat(point.getPointX(), point.getPointY(), point.getPointX() + BOXWIDTH, point.getPointY() + BOXWIDTH);
                    //获取图片输入流
                    InputStream inputStream1;
                    if (i == mSnakeList.size() - 1) {
                        inputStream1 = getContext().getResourceManager().getResource(ResourceTable.Media_tail_right);//尾巴
                    } else {
                        inputStream1 = getContext().getResourceManager().getResource(ResourceTable.Media_body_right);//身体
                    }
                    ImageSource.SourceOptions sourceOptions1 = new ImageSource.SourceOptions();
                    //设置格式
                    sourceOptions.formatHint = "image/png";
                    //获取图片资源
                    ImageSource imageSource1 = ImageSource.create(inputStream1, sourceOptions1);
                    //创建DecodingOptions对象用来创建PixeMap对象
                    ImageSource.DecodingOptions decodingOptions1 = new ImageSource.DecodingOptions();
                    //获取PixeMap对象
                    PixelMap pixelMap1 = imageSource1.createPixelmap(decodingOptions1);
                    //获取PixelMapHolder对象
                    PixelMapHolder pixelMapHolder1 = new PixelMapHolder(pixelMap1);
                    //把蛇头画在画布上
                    canvas.drawPixelMapHolderRect(pixelMapHolder1, rect1, paint);
                }
                break;
            default:
                break;
        }
        //蛇移动，更新list为下一次刷新做准备
        snakeMove(mSnakeList, mSnakeDirection);
        if (isFoodEaten()) {  //如果吃了食物，长度加1
            mIsFoodDone = true;
        } else {    //如果没有吃食物，由于前进时加了一个 这里删除尾巴，出现移动的效果
            mSnakeList.remove(mSnakeList.size() - 1);
        }
    }

    public void snakeMove(ArrayList<Point> list, int direction) {
        //Log.e(TAG," snakeMove ArrayList = " + list.toString());
        Point orighead = list.get(0);
        Point newhead = new Point();
        //蛇前进，实现原理就是头加尾减，若吃到食物，头加尾不减
        switch (direction) {
            case UP:
                newhead.position[0] = orighead.getPointXToInt();
                newhead.position[1] = orighead.getPointYToInt() - BOXWIDTH;
                break;
            case DOWN:
                newhead.position[0] = orighead.getPointXToInt();
                newhead.position[1] = orighead.getPointYToInt() + BOXWIDTH;
                break;
            case LEFT:
                newhead.position[0] = orighead.getPointXToInt() - BOXWIDTH;
                newhead.position[1] = orighead.getPointYToInt();
                break;
            case RIGHT:
                newhead.position[0] = orighead.getPointXToInt() + BOXWIDTH;
                newhead.position[1] = orighead.getPointYToInt();
                break;
            default:
                break;
        }
        adjustHead(newhead);
        list.add(0, newhead);
    }

    /**
     * 画食物
     *
     * @param canvas
     * @param paint
     * @throws IOException
     * @throws NotExistException
     */
    private void drawFood(Canvas canvas, Paint paint) throws IOException, NotExistException {
        RectFloat food =null;
        PixelMapHolder pixelMapHolder = null;
        int i = 2;
        System.out.println("mll1");
        if (mIsFoodDone) {  //只在前一个食物被吃掉的情况下才产生食物
            System.out.println("mll2");
            int x = mRandom.nextInt(mWidth - 2 * sXOffset - BOXWIDTH) + sXOffset;
            int y = mRandom.nextInt(mHeight - 2 * sYOffset - BOXWIDTH) + sYOffset;
            mFoodPosition = new Point(new Float(x), new Float(y));
            mIsFoodDone = false;
            System.out.println("mllsize"+mSnakeList.size());
            if ((mSnakeList.size() - 2) % 5 == 0) {
                BOXWIDTH += 7;
            }
            Random r = new Random();
            i = r.nextInt(3);
//            //把食物画在画布上
//            canvas.drawPixelMapHolderRect(pixelMapHolder, food, paint);
//            System.out.println("mll食物"+food);
        }
        //获取图片输入流
        int[] array = new int[5];
        array[0] = ResourceTable.Media_food1;
        array[1] = ResourceTable.Media_food2;
        array[2] = ResourceTable.Media_food3;

        InputStream inputStream = getContext().getResourceManager().getResource(array[i]);
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        //设置格式
        sourceOptions.formatHint = "image/png";
        //获取图片资源
        ImageSource imageSource = ImageSource.create(inputStream, sourceOptions);
        //创建DecodingOptions对象用来创建PixeMap对象
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        //获取PixeMap对象
        PixelMap pixelMap = imageSource.createPixelmap(decodingOptions);
        //获取PixelMapHolder对象
        pixelMapHolder = new PixelMapHolder(pixelMap);
        food = new RectFloat(mFoodPosition.getPointXToInt(), mFoodPosition.getPointYToInt(), mFoodPosition.getPointXToInt() + BOXWIDTH, mFoodPosition.getPointYToInt() + BOXWIDTH);
        //把食物画在画布上
        canvas.drawPixelMapHolderRect(pixelMapHolder, food, paint);
    }

    //边界判断
    private boolean isOutBound(Point point) {
        if (point.getPointXToInt() < sXOffset || point.getPointXToInt() > mWidth - sXOffset) return true;
        if (point.getPointYToInt() < sYOffset || point.getPointYToInt() > mHeight - sYOffset) return true;
        return false;
    }

    //出了边界，重新回来
    private void adjustHead(Point point) {
        //Log.e(TAG, "checkBound = " + isOutBound(point));
        if (isOutBound(point)) {
            if (mSnakeDirection == UP) point.position[1] = mHeight - sYOffset - BOXWIDTH;
            if (mSnakeDirection == DOWN) point.position[1] = sYOffset;
            if (mSnakeDirection == LEFT) point.position[0] = mWidth - sYOffset - BOXWIDTH;
            if (mSnakeDirection == RIGHT) point.position[0] = sXOffset;
        }
    }

    //判断食物是否可以被吃
    private boolean isFoodEaten() {
        if (!mIsFoodDone) {
            Rect foodrect = new Rect(mFoodPosition.getPointXToInt(), mFoodPosition.getPointYToInt(), mFoodPosition.getPointXToInt() + BOXWIDTH, mFoodPosition.getPointYToInt() + BOXWIDTH);
            Point head = mSnakeList.get(0);
            Rect headrect = new Rect(head.getPointXToInt(), head.getPointYToInt(), head.getPointXToInt() + BOXWIDTH, head.getPointYToInt() + BOXWIDTH);
            return foodrect.getIntersectRect(headrect);
        }
        return false;
    }

    @Override
    public void onContentScrolled(Component component, int i, int i1, int i2, int i3) {
        mWidth = i;
        mHeight = i1;
    }

    /**
     * 设置蛇的运动方向
     *
     * @param mSnakeDirection
     */
    public void setmSnakeDirection(int mSnakeDirection) {
        this.mSnakeDirection = mSnakeDirection;
    }

    public int getmSnakeDirection() {
        return mSnakeDirection;
    }

    public ArrayList<Point> getmSnakeList() {
        return mSnakeList;
    }


}
