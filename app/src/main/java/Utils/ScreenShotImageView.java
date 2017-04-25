package Utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gwelldemo.R;
import com.hitomi.cslibrary.CrazyShadow;


/**
 * Created by dansesshou on 17/4/23.
 */

public class ScreenShotImageView extends ImageView {
    private long DeleteTime=5*1000L;
    private ScreenShotImageView view;
    private String path;
    private Context mContext;
    Paint paint=new Paint();
    private CrazyShadow wrapCrazyShadow0;

    private Handler removeHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(view!=null&&view.getParent() instanceof ViewGroup){
                ViewGroup parent= (ViewGroup) view.getParent();
                parent.removeView(view);
            }else{
                throw new IllegalArgumentException("ScreenShotImageView's Parent must be ViewGroup");
            }


            return false;
        }
    });

    private LoadListner listner;
    public ScreenShotImageView(Context context) {
        super(context);
        view=this;
        mContext=context;
    }

    public ScreenShotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScreenShotImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public ScreenShotImageView(Context context,String path){
        super(context);
        view=this;
        this.path=path;
        initData();

    }

    private void initData(){
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(3);

        //setBackgroundResource(R.drawable.border);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取控件需要重新绘制的区域
        Rect rect=canvas.getClipBounds();
        rect.bottom--;
        rect.right--;
        canvas.drawRect(rect, paint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        Animater();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        //this.setImageResource(R.mipmap.ic_launcher);
        Glide.with(mContext)
                .load(path)
                //.transform(new GlideStokenTransform(context,2,context.getResources().getColor(R.color.white)))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if(listner!=null){
                            listner.onLoadSuccess(false);
                        }
                        Log.e("dxsTest","onException:"+e.toString());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if(listner!=null){
                            listner.onLoadSuccess(true);
                        }
                        //Animater();
                        return false;
                    }
                })
                .into(view);
        Log.e("dxsTest","ScreenShotImageView:"+path);
    }

    @Override
    protected void onDetachedFromWindow() {
        if(removeHandler!=null){
            removeHandler.removeCallbacksAndMessages(null);
        }
        super.onDetachedFromWindow();
    }

    private void AnimalStar(){
        final AlphaAnimation alphaAnimation=new AlphaAnimation(0.1f,1.0f);
        alphaAnimation.setDuration(100);

        final ScaleAnimation animation =new ScaleAnimation(1.0f,0.3f,
                1.0f,0.3f,
                Animation.RELATIVE_TO_SELF,0.1f,
                Animation.RELATIVE_TO_SELF,0.9f);

        animation.setDuration(800);
        animation.setFillAfter(true);


        AnimationSet set=new AnimationSet(false);
        set.setFillAfter(true);
        set.addAnimation(alphaAnimation);
        set.addAnimation(animation);
        this.setAnimation(set);
        set.start();
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setColorFilter(Color.WHITE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(removeHandler!=null){
                    removeHandler.sendEmptyMessageDelayed(0,DeleteTime);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void Animater(){
        ObjectAnimator alpha1=ObjectAnimator.ofFloat(this, "alpha", 0,1.0f);
        alpha1.setDuration(800);

        ObjectAnimator animator2=ObjectAnimator.ofFloat(this, "scaleX", 1.0f,0.25f);
        ObjectAnimator animator3=ObjectAnimator.ofFloat(this, "scaleY", 1.0f,0.25f);
        this.setPivotX(0.1f);
        this.setPivotY(0.1f);
        animator2.setDuration(1000);
        animator3.setDuration(1000);

        ObjectAnimator alpha2=ObjectAnimator.ofFloat(this, "alpha", 1.0f,0.0f);
        alpha2.setDuration(300);

        AnimatorSet set=new AnimatorSet();
        set.play(alpha1);
        set.play(animator2).with(animator3);
        set.play(alpha2).after(DeleteTime);
        set.start();

    }

    private class GlideStokenTransform extends BitmapTransformation {

        private Paint mBorderPaint;
        private float mBorderWidth;

        public GlideStokenTransform(Context context) {
            super(context);
        }

        public GlideStokenTransform(BitmapPool bitmapPool) {
            super(bitmapPool);
        }

        public GlideStokenTransform(Context context, int borderWidth, int borderColor){
            super(context);

            mBorderWidth=borderWidth;

            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return addStroken(pool,toTransform);
        }

        private Bitmap addStroken(BitmapPool pool, Bitmap toTransform) {
            if (pool == null) return null;

            Bitmap result = pool.get(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(toTransform.getWidth(), toTransform.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, toTransform.getWidth(), toTransform.getHeight());
            canvas.drawRect(rectF, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    /**
     * 加载是否成功
     */
    public interface LoadListner{
        void onLoadSuccess(boolean isSuccess);
    }

    public void setLoadListner(LoadListner listner){
        this.listner=listner;
    }
}
