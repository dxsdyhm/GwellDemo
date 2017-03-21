package Utils;

import com.hwangjr.rxbus.Bus;

/**
 * Created by USER on 2017/3/21.
 */

public class RxBus {
    private static Bus sBus;

    public static synchronized Bus get() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }
}
