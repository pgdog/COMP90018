package com.example.comp90018.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.comp90018.R;

public class SideIndexBar extends View {
    private float indexHeight;
    private float indexWidth;
    private int indexColor;
    private float indexTextSize;
    private float hilightCircleRadius;
    private Paint paintDefault;
    private Paint paintHilight;

    public static final String[] indexs={"A","B","C","D","E","F","G","H","I","J","K","L","M","N",
        "O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};

    private String currentTouchIndex ="";
    private String lastTouchIndex ="";

    private final int default_value_index_color=0;
    private final int default_value_index_text_size=14;

    public SideIndexBar(Context context, @Nullable AttributeSet attrs){
        super(context,attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.side_index_bar);
        this.indexColor=typedArray.getColor(R.styleable.side_index_bar_index_color,default_value_index_color);
        this.indexTextSize=typedArray.getDimension(R.styleable.side_index_bar_index_text_size,default_value_index_text_size);
        this.hilightCircleRadius =this.indexTextSize/2;
        typedArray.recycle();

        this.paintDefault =new Paint();
        paintDefault.setAntiAlias(true);
        paintDefault.setTextSize(indexTextSize);
        paintDefault.setColor(indexColor);
        this.paintHilight=new Paint();
        paintHilight.setAntiAlias(true);
        paintHilight.setColor(context.getColor(R.color.colorBottomNavIconChecked));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Calculate the size
        int width = (int)(hilightCircleRadius*2);//"M" should be the largest size
        int height=MeasureSpec.getSize(heightMeasureSpec);
        this.indexHeight=(height-getPaddingTop()-getPaddingBottom())/indexs.length;
        this.indexWidth=width;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw each index
        for(int i=0;i<indexs.length;i++){
            //Calculate the distance of x
            int x=(int)(this.indexWidth/2- paintDefault.measureText(indexs[i])/2);
            //Calculate the baseline of the index
            int indexCenter=(int) (this.indexHeight/2+this.indexHeight*i + getPaddingTop());
            Paint.FontMetrics fontMetrics = paintDefault.getFontMetrics();
            int dy=(int)((fontMetrics.bottom - fontMetrics.top)/2-fontMetrics.bottom);
            int y=indexCenter+dy;
            //draw
            if(currentTouchIndex.equals(indexs[i])){
                paintDefault.setColor(Color.WHITE);
                canvas.drawCircle(indexWidth/2,indexCenter, hilightCircleRadius,paintHilight);
                canvas.drawText(currentTouchIndex,x,y,paintDefault);
            }else{
                paintDefault.setColor(this.indexColor);
                canvas.drawText(indexs[i],x,y, paintDefault);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //Calculate the height and show the index
                float currentHeight=event.getY();
                int position=(int)((currentHeight-getPaddingTop())/this.indexHeight);
                if(position>=0 && position<indexs.length) {
                    this.currentTouchIndex = indexs[position];
                }
                if(indexTouchListener!=null){
                    indexTouchListener.touch(currentTouchIndex);
                }
                if(!currentTouchIndex.equals(lastTouchIndex)){
                    lastTouchIndex=currentTouchIndex;
                    //Redraw
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                currentTouchIndex="";

                if(indexTouchListener!=null){
                    indexTouchListener.unTouch();
                }
                invalidate();
                break;
        }
        return true;
    }

    private IndexTouchListener indexTouchListener;
    public void setOnIndexTouchListener(IndexTouchListener listener){
        this.indexTouchListener=listener;
    }
    public interface IndexTouchListener{
        public void touch(String index);
        public void unTouch();
    }
}
