package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwelldemo.R;

import entity.RecordFile;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by xiyingzhu on 2017/3/14.
 */
public class RecordFileProvider extends ItemViewProvider<RecordFile,RecordFileProvider.ViewHolder> {

        @NonNull
        @Override
        protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
            View root=inflater.inflate(R.layout.item_recordfile,parent,false);
            return new ViewHolder(root);
        }

        @Override
        protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final RecordFile recordFile) {
            holder.txRecordFile.setText(recordFile.getName());

        }


        static class ViewHolder extends RecyclerView.ViewHolder{
            private final TextView txRecordFile;

            public ViewHolder(View itemView) {
                super(itemView);
                this.txRecordFile= (TextView) itemView.findViewById(R.id.tx_record_file);

            }
        }
    }
