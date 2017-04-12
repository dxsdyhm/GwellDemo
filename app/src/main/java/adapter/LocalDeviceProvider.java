package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwelldemo.R;

import entity.LocalDevice;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by dansesshou on 17/2/17.
 */

public class LocalDeviceProvider extends ItemViewProvider<LocalDevice,LocalDeviceProvider.ViewHolder> {
    private OnItemClickListner listner;


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root=inflater.inflate(R.layout.item_localdevice,parent,false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final LocalDevice localDevice) {
        holder.txID.setText(localDevice.getID());
        holder.txIP.setText(localDevice.getIP());
        holder.txVersion.setText(localDevice.getVersion());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner!=null){
                    listner.onItemClick(holder.getAdapterPosition(),localDevice);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private  TextView txID;
        private TextView txIP;
        private TextView txVersion;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txID= (TextView) itemView.findViewById(R.id.tx_deviceid);
            this.txIP= (TextView) itemView.findViewById(R.id.tx_deviceip);
            this.txVersion= (TextView) itemView.findViewById(R.id.tx_device_type);
        }
    }

    public interface OnItemClickListner{
        void onItemClick(int position,LocalDevice localDevice);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }
}
