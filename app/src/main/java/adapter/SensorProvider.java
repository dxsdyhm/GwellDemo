package adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gwelldemo.R;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by xiyingzhu on 2017/3/14.
 */
public class SensorProvider extends ItemViewBinder<Integer, SensorProvider.ViewHolder> {
    private OnItemClickListner listner;

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_sensor, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Integer integer) {
        if (holder.getAdapterPosition()/8==0){
            holder.btn_item.setBackgroundColor(Color.CYAN);
        }else if (holder.getAdapterPosition()/8==8){
            holder.btn_item.setBackgroundColor(Color.YELLOW);
        }else {
            holder.btn_item.setBackgroundColor(Color.GREEN);
        }
        if (integer == 0){
            holder.btn_item.setBackgroundColor(Color.RED);
        }

        holder.btn_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.onItemClick(holder.getAdapterPosition(), integer);
                }
            }
        });
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button btn_item;

        public ViewHolder(View itemView) {
            super(itemView);
            this.btn_item = (Button) itemView.findViewById(R.id.btn_item);
        }
    }

    public interface OnItemClickListner {
        void onItemClick(int position, Integer integer);
    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        this.listner = listner;
    }
}
