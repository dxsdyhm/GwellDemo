package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwelldemo.R;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by xiyingzhu on 2017/5/10.
 */
public class SerialAppProvider extends ItemViewBinder<String, SerialAppProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_serialapp, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String s) {
        holder.txt_item.setText(s);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView txt_item;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_item = (TextView) itemView.findViewById(R.id.txt_item);
        }
    }
}
