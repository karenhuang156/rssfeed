package karenhuang.rssfeed;

import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by karenhuang on 20/08/16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private List<Item> itemList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description, creator, pubDate;

        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            creator = (TextView) view.findViewById(R.id.creator);
            pubDate = (TextView) view.findViewById(R.id.pubDate);

        }
    }

    public ItemAdapter (List<Item> itemList){
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.title.setText(item.title);
        holder.description.setText(item.description);
        holder.creator.setText(item.creator);
        holder.pubDate.setText(item.pubDate);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
