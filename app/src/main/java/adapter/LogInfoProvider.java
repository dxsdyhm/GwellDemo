package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.blankj.utilcode.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwelldemo.R;

import entity.LogInfo;
import entity.RecordFile;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by USER on 2017/5/3.
 */

public class LogInfoProvider extends ItemViewBinder<LogInfo, LogInfoProvider.ViewHolder> {
    @NonNull
    @Override
    protected LogInfoProvider.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_loginfo, parent, false);
        return new LogInfoProvider.ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LogInfo logInfo) {
        String Head=String.format("SrcID:%d---------RecTime:%s",logInfo.getSrcID(), TimeUtils.millis2String(logInfo.getRecTime()));
        holder.txHead.setText(Head);
        holder.txContent.setText(logInfo.getContStr());
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txHead,txContent;
        public ViewHolder(View itemView) {
            super(itemView);
            txHead= (TextView) itemView.findViewById(R.id.tx_info_head);
            txContent= (TextView) itemView.findViewById(R.id.tx_info_content);
        }
    }
}
