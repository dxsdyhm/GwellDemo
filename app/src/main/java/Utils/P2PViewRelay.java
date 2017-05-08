package Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gwelldemo.R;

import static com.gwelldemo.R.string.test;

/**
 * Created by dansesshou on 17/4/23.
 */

public class P2PViewRelay extends RelativeLayout {

    private ScreenShotImageView ivScreenShotView;
    private Context mContext;
    public P2PViewRelay(Context context) {
        this(context,null);
    }

    public P2PViewRelay(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public P2PViewRelay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
    }

    public void showScreenShot(String Path){
        if(ivScreenShotView==null){
            ivScreenShotView=new ScreenShotImageView(mContext,Path);
        }
        RelativeLayout.LayoutParams parems=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ivScreenShotView.setLayoutParams(parems);
        ivScreenShotView.setPath(Path);

        P2PViewRelay.this.addView(ivScreenShotView);

    }
}
