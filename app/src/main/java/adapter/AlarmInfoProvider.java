package adapter;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gwelldemo.R;

import entity.AlarmInfo;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by USER on 2017/4/26.
 */

public class AlarmInfoProvider extends ItemViewBinder<AlarmInfo, AlarmInfoProvider.ViewHolder> {
    private final static String infoFormat = "ID: %s\n编号：%d\n路径：%s";
    public final static String localPathFormat = "/test/%d.jpg";
    private AlarmInfoProvider.OnItemClickListner listner;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_alarminfo, parent, false);
        return new AlarmInfoProvider.ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final AlarmInfo info) {
        String infoText = String.format(infoFormat, info.getSrcId(), info.getId(), info.getImagePath());
        holder.txInfo.setText(infoText);
        holder.btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.onItemClick(holder.getAdapterPosition(), info);
                }
            }
        });
        Glide.with(holder.txInfo.getContext())
                .load(getLocalImagePath(info))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivAlarmPicture);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txInfo;
        private final ImageView ivAlarmPicture;
        private final Button btnGet;

        public ViewHolder(View itemView) {
            super(itemView);
            txInfo = (TextView) itemView.findViewById(R.id.tx_infos);
            ivAlarmPicture = (ImageView) itemView.findViewById(R.id.iv_alarmImage);
            btnGet = (Button) itemView.findViewById(R.id.btn_getimage);
        }
    }

    //报警时设备传的路径
    private String getImagePath(AlarmInfo info) {
        return info.getAlarmCapDir() + "01.jpg";
    }

    //手机本地保存路径
    private String getLocalImagePath(AlarmInfo info) {
        String path = String.format(localPathFormat, info.getId());
        return Environment.getExternalStorageDirectory().getPath() + path;
    }

    public interface OnItemClickListner {
        void onItemClick(int position, AlarmInfo info);
    }

    public void setOnItemClickListner(AlarmInfoProvider.OnItemClickListner listner) {
        this.listner = listner;
    }
}
