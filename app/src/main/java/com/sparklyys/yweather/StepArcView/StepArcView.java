package com.sparklyys.yweather.StepArcView;



        import android.animation.ValueAnimator;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Matrix;
        import android.graphics.Paint;
        import android.graphics.PointF;
        import android.graphics.Rect;
        import android.graphics.RectF;
        import android.graphics.SweepGradient;
        import android.graphics.Typeface;
        import android.os.Build;
        import android.support.annotation.RequiresApi;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.View;

        import com.sparklyys.yweather.R;


/**
 * Created by DylanAndroid on 2016/5/26.
 * 显示步数的圆弧
 */
public class StepArcView extends View {
    private final String TAG = "StepArcView";

    /**
     * 圆弧的宽度
     */
    private float borderWidth = 38f;
    /**
     * 最外层圆弧的宽度
     */
    private float borderWidthw = 3f;
    /**
     * 最外层圆弧和宽圆弧的间距
     */
    private float marginWidth = 10f;
    /**
     * 画步数的数值的字体大小
     */
    private float numberTextSize = 0;
    /**
     * 步数
     */
    private String stepNumber = "0";
    /**
     * 开始绘制圆弧的角度
     */
    private float startAngle = 90;
    /**
     * 终点对应的角度和起始点对应的角度的夹角
     */
    private float angleLength = 360;
    /**
     * 所要绘制的当前步数的红色圆弧终点到起点的夹角
     */
    private float currentAngleLength = 0;
    /**
     * 动画时长
     */
    private int animationLength = 5000;

    private PointF point;
    private float centerX;
    private Bitmap bitmap;
    private int totalStepNum;
    private  String air;

    /**
     * 要绘制的三角形指示器的半径
     **/
    private float triangleRadius;
    private float clockRadius;

    public StepArcView(Context context) {
        super(context);
        init();

    }


    public StepArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        point = new PointF();
        /**
         * 通过这个拿到一个资源图片对象
         */
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_top);
        setBorderWidth(dpToPx(12));
        setBorderWidthw(1);
        setMarginWidth(3);
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getBorderWidthw() {
        return borderWidthw;
    }

    public void setBorderWidthw(float borderWidthw) {
        this.borderWidthw = borderWidthw;
    }

    public float getMarginWidth() {
        return marginWidth;
    }

    public void setMarginWidth(float marginWidth) {
        this.marginWidth = marginWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**中心点的x坐标*/
        centerX = (getWidth()) / 2;
        /**指定圆弧的外轮廓矩形区域*/
        RectF rectFw = new RectF(0 + borderWidthw, borderWidthw, 2 * centerX - borderWidthw, 2 * centerX - borderWidthw);

        /**指定圆弧的外轮廓矩形区域*/
        RectF rectF = new RectF(0 + borderWidth + marginWidth, borderWidth + marginWidth, 2 * centerX - borderWidth - marginWidth, 2 * centerX - borderWidth - marginWidth);

        drawArcBig(canvas, rectFw);
        /**【第一步】绘制整体的灰色圆弧*/
        drawArcYellow(canvas, rectF);
        /**【第二步】绘制当前进度的渐变色圆弧*/
        drawArcRed(canvas, rectF);
        /**【第三步】绘制当前进度的蓝色数字*/
        drawTextNumber(canvas, centerX);
        /**【第四步】绘制"步数"的蓝色数字*/
        drawTextStepString(canvas, centerX);
        /**【第五步】绘制"步数"进度标尺的三角形*/
        drawBitmap(canvas);
        /**【第六步】绘制"步数"进度标尺类似于钟表线隔*/
        drawLine(canvas);
    }

    /**
     * 0.绘制最外层的蓝色圆弧
     *
     * @param canvas 画笔
     * @param rectF  参考的矩形
     */
    private void drawArcBig(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        /** 默认画笔颜色，黄色 */
        paint.setColor(getResources().getColor(R.color.start_color));
        /** 结合处为圆弧*/
        paint.setStrokeJoin(Paint.Join.ROUND);
        /** 设置画笔的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形*/
        paint.setStrokeCap(Paint.Cap.ROUND);
        /** 设置画笔的填充样式 Paint.Style.FILL  :填充内部;Paint.Style.FILL_AND_STROKE  ：填充内部和描边;  Paint.Style.STROKE  ：仅描边*/
        paint.setStyle(Paint.Style.STROKE);
        /**抗锯齿功能*/
        paint.setAntiAlias(true);
        /**设置画笔宽度*/
        paint.setStrokeWidth(borderWidthw);

        /**绘制圆弧的方法
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)//画弧，
         参数一是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二是起始角(度)在电弧的开始，圆弧起始角度，单位为度。
         参数三圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         参数四是如果这是true(真)的话,在绘制圆弧时将圆心包括在内，通常用来绘制扇形；如果它是false(假)这将是一个弧线,
         参数五是Paint对象；
         */
        canvas.drawArc(rectF, startAngle, angleLength, false, paint);

    }

    /**
     * 1.绘制总步数的黄色圆弧
     *
     * @param canvas 画笔
     * @param rectF  参考的矩形
     */
    private void drawArcYellow(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        /** 默认画笔颜色，黄色 */
        paint.setColor(getResources().getColor(R.color.main_background));
        /** 结合处为圆弧*/
        paint.setStrokeJoin(Paint.Join.ROUND);
        /** 设置画笔的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形*/
        paint.setStrokeCap(Paint.Cap.ROUND);
        /** 设置画笔的填充样式 Paint.Style.FILL  :填充内部;Paint.Style.FILL_AND_STROKE  ：填充内部和描边;  Paint.Style.STROKE  ：仅描边*/
        paint.setStyle(Paint.Style.STROKE);
        /**抗锯齿功能*/
        paint.setAntiAlias(true);
        /**设置画笔宽度*/
        paint.setStrokeWidth(borderWidth);

        /**绘制圆弧的方法
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)//画弧，
         参数一是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二是起始角(度)在电弧的开始，圆弧起始角度，单位为度。
         参数三圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         参数四是如果这是true(真)的话,在绘制圆弧时将圆心包括在内，通常用来绘制扇形；如果它是false(假)这将是一个弧线,
         参数五是Paint对象；
         */
        canvas.drawArc(rectF, startAngle, angleLength, false, paint);

    }

    /**
     * 2.绘制当前步数的红色圆弧
     */
    private void drawArcRed(Canvas canvas, RectF rectF) {
        Paint paintCurrent = new Paint();
        paintCurrent.setStrokeJoin(Paint.Join.ROUND);
//        paintCurrent.setStrokeCap(Paint.Cap.SQUARE);//圆角弧度
        paintCurrent.setStyle(Paint.Style.STROKE);//设置填充样式
        paintCurrent.setAntiAlias(true);//抗锯齿功能
        paintCurrent.setStrokeWidth(borderWidth);//设置画笔宽度
        /**
         * 设置圆形渐变
         * 【第一个参数】：中心点x坐标
         * 【第二个参数】：中心点y坐标
         * 【第三个参数】：渐变的颜色数组
         * 【第四个参数】：渐变的颜色数组对应的相对位置
         */
        paintCurrent.setShader(new SweepGradient(centerX, centerX,
                new int[]{getResources().getColor(R.color.color1),
                        getResources().getColor(R.color.color2),
                        getResources().getColor(R.color.color3),
                        getResources().getColor(R.color.color4)},
                null));

        canvas.drawArc(rectF, startAngle, currentAngleLength, false, paintCurrent);
    }

    /**
     * 3.圆环中心的步数
     */
    private void drawTextNumber(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setTextSize(numberTextSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        vTextPaint.setTypeface(font);//字体风格
        vTextPaint.setColor(getResources().getColor(R.color.main_background));
        vTextPaint.setTextSize(150);
        Rect bounds_Number = new Rect();
        vTextPaint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        canvas.drawText(stepNumber, centerX, getHeight() / 2 + bounds_Number.height() / 2, vTextPaint);

    }

    /**
     * 4.圆环中心[步数]的文字
     */
    private void drawTextStepString(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(dpToPx(13));
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setColor(getResources().getColor(R.color.secondly_text_white));

        String stepString = "空气质量:" + air;
        Rect bounds = new Rect();
        vTextPaint.getTextBounds(stepString, 0, stepString.length(), bounds);
        canvas.drawText(stepString, centerX, getHeight() / 2 + bounds.height() + getFontHeight(numberTextSize), vTextPaint);
        canvas.save();
        stepString = "Pm2.5";
        //   今天步数
        bounds = new Rect();
        vTextPaint.getTextBounds(stepString, 0, stepString.length(), bounds);
        canvas.drawText(stepString, centerX, getHeight() / 2 - getFontHeight(numberTextSize), vTextPaint);

    }

    /**
     * 5.画三角形
     *
     * @param canvas
     */
    private void drawBitmap(Canvas canvas) {
        // 定义矩阵对象
        Matrix matrix = new Matrix();
        // 参数为正则向右旋转
        matrix.preRotate(startAngle + currentAngleLength + 90, point.x, point.x);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);//这里不管怎么设置都不影响最终图像
        canvas.drawBitmap(dstbmp, point.x - dstbmp.getWidth() / 2, point.y - dstbmp.getHeight() / 2, mBitmapPaint);
    }

    /**
     * 6. 画钟表线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(dpToPx(2));
        mPaint.setColor(getResources().getColor(R.color.start_color));
        /**要绘制的表盘线的总数**/
        int count = 60;
        /**要绘制的表盘每个间隔线条之间的夹角**/
        int avgAngle = (360 / (count - 1));

        /**要绘制的表盘线条长度**/
        int lineLength = dpToPx(6);
        /**要绘制的表盘的最长的半径**/
        clockRadius = centerX - marginWidth - borderWidthw - borderWidth - bitmap.getHeight() - marginWidth - lineLength;
        /**起始点**/
        PointF point1 = new PointF();
        /**终止点**/
        PointF point2 = new PointF();
        for (int i = 0; i < count; i++) {
            int angle = avgAngle * i;
            /**起始点坐标**/
            point1.x = centerX + (float) Math.cos(angle * (Math.PI / 180)) * clockRadius;
            point1.y = centerX + (float) Math.sin(angle * (Math.PI / 180)) * clockRadius;

            /**终止点坐标**/
            point2.x = centerX + (float) Math.cos(angle * (Math.PI / 180)) * (clockRadius - lineLength);
            point2.y = centerX + (float) Math.sin(angle * (Math.PI / 180)) * (clockRadius - lineLength);

            /**画线**/
            canvas.drawLine(point1.x, point1.y, point2.x, point2.y, mPaint);
        }
    }

    /**
     * 获取当前步数的数字的高度
     *
     * @param fontSize 字体大小
     * @return 字体高度
     */
    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Rect bounds_Number = new Rect();
        paint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        return bounds_Number.height();
    }

    /**
     * dp 转换成px
     *
     * @param dip
     * @return
     */

    private int dpToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 所走的步数进度
     *
     * @param totalStepNum  设置的步数
     * @param currentCounts 所走步数
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void setCurrentCount(int totalStepNum, int currentCounts ,String air) {
        this.totalStepNum = totalStepNum;
        this.air=air;
        stepNumber = currentCounts + "";
        setTextSize(currentCounts);
        /**如果当前走的步数超过总步数则圆弧还是270度，不能成为园*/
        if (currentCounts > totalStepNum) {
            currentCounts = totalStepNum;
        }

        /**所走步数占用总共步数的百分比*/
        float scale = (float) currentCounts / totalStepNum;
        /**换算成弧度最后要到达的角度的长度-->弧长*/
        currentAngleLength = scale * angleLength;
        /**开始执行动画*/
        setAnimation(0, currentAngleLength, animationLength);
    }

    /**
     * 为进度设置动画
     * ValueAnimator是整个属性动画机制当中最核心的一个类，属性动画的运行机制是通过不断地对值进行操作来实现的，
     * 而初始值和结束值之间的动画过渡就是由ValueAnimator这个类来负责计算的。
     * 它的内部使用一种时间循环的机制来计算值与值之间的动画过渡，
     * 我们只需要将初始值和结束值提供给ValueAnimator，并且告诉它动画所需运行的时长，
     * 那么ValueAnimator就会自动帮我们完成从初始值平滑地过渡到结束值这样的效果。
     *
     * @param last
     * @param current
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void setAnimation(float last, float current, int length) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngleLength);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**每次要绘制的圆弧角度**/
                currentAngleLength = (float) animation.getAnimatedValue();
                Log.d(TAG, "currentAngleLength=" + currentAngleLength);
                triangleRadius = centerX - marginWidth - borderWidthw - borderWidth - bitmap.getWidth();
                /**要绘制的三角形指示器的x坐标**/
                point.x = (float) (centerX + triangleRadius * Math.cos((startAngle + currentAngleLength) * Math.PI / 180));
                /**要绘制的三角形指示器的y坐标**/
                point.y = (float) (centerX + triangleRadius * Math.sin((startAngle + currentAngleLength) * Math.PI / 180));
                Log.d(TAG, centerX + "");
                Log.d(TAG, point + "");
                invalidate();
            }
        });
        progressAnimator.start();
    }

    /**
     * 设置文本大小,防止步数特别大之后放不下，将字体大小动态设置
     *
     * @param num
     */
    public void setTextSize(int num) {
        String s = String.valueOf(num);
        int length = s.length();
        if (length <= 4) {
            numberTextSize = dpToPx(40);
        } else if (length > 4 && length <= 6) {
            numberTextSize = dpToPx(30);
        } else if (length > 6 && length <= 8) {
            numberTextSize = dpToPx(25);
        } else if (length > 8) {
            numberTextSize = dpToPx(20);
        }
    }

}
