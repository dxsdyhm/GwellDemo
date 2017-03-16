package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwelldemo.R;

import java.util.Locale;

import entity.RecordFile;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by xiyingzhu on 2017/3/14.
 */
public class RecordFileProvider extends ItemViewProvider<RecordFile, RecordFileProvider.ViewHolder> {
    private OnItemClickListner listner;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_recordfile, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final RecordFile recordFile) {
        String text = String.format(Locale.getDefault(), "%d、%s", holder.getAdapterPosition(), recordFile.getName());
        holder.txRecordFile.setText(text);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.onItemClick(holder.getAdapterPosition(), recordFile);
                }
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txRecordFile;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txRecordFile = (TextView) itemView.findViewById(R.id.tx_record_file);


        }
    }

    public interface OnItemClickListner {
        void onItemClick(int position, RecordFile recordFile);
    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        this.listner = listner;
    }
}
