package Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.gwelldemo.R;

import es.dmoral.toasty.Toasty;

/**
 * Created by USER on 2017/3/23.
 */

public class ToastUtils {
    public static void ShowSuccess(Context context, String text, int time,boolean withIcon){
        Toasty.success(context, text, time, withIcon).show();
    }

    public static void ShowError(Context context, String text, int time,boolean withIcon){
        Toasty.error(context, text, time, withIcon).show();
    }

    public static void ShowNormoal(Context context, String text, int time,Drawable Icon){
        Toasty.normal(context, text, time, Icon).show();
    }
}
