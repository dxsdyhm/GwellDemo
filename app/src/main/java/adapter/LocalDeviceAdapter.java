package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwelldemo.R;

import java.util.List;

import entity.Device;

/**
 * Created by Administrator on 2017/4/11.
 */

public class LocalDeviceAdapter extends RecyclerView.Adapter<LocalDeviceAdapter.ViewHolder> {
    private Context context;
    private List<Device> devices;
    private OnItemClickListener listner;

    public LocalDeviceAdapter(Context context, List<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_localdevice, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Device localDevice = devices.get(position);
        holder.txID.setText(localDevice.getId() + "");
        holder.txIP.setText(localDevice.getIP() + "");
        holder.txVersion.setText(localDevice.getType()+"\n"+localDevice.getSubType());
        if (listner != null) {
            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.onClick(view, devices.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txID;
        private TextView txIP;
        private TextView txVersion;
        private LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txID = (TextView) itemView.findViewById(R.id.tx_deviceid);
            this.txIP = (TextView) itemView.findViewById(R.id.tx_deviceip);
            this.txVersion = (TextView) itemView.findViewById(R.id.tx_deviceversion);
            this.llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
        }
    }

    /**
     * item单击监听器
     */
    public interface OnItemClickListener {
        void onClick(View view, Device device);
    }

    public void setOnItemClickLinstener(OnItemClickListener linstener) {
        this.listner = linstener;
    }

}
