package de.ek.private_timeline.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plumillonforge.android.chipview.Chip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ek.private_timeline.R;
import de.ek.private_timeline.persistence.Tag;
import de.ek.private_timeline.persistence.TimelineObject;

/**
 * Created by Enrico on 02.11.2016.
 */

public class RecyclerViewAdapterTimeLine extends RecyclerView.Adapter<ViewHolderTimeLine> {
    List<TimelineObject> list = Collections.emptyList();
    Context context;

    public RecyclerViewAdapterTimeLine(List<TimelineObject> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setNewData(List<TimelineObject> timelineObjectList) {
        this.list = timelineObjectList;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolderTimeLine onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        ViewHolderTimeLine holder = new ViewHolderTimeLine(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolderTimeLine holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.content.setText(list.get(position).getContent());

        List<Chip> chipList = new ArrayList<>(list.get(position).getTags().size());
        for (Tag t:list.get(position).getTags()) {
            chipList.add(t);
        }
        holder.tagView.setChipList(chipList);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, TimelineObject data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(TimelineObject data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }


}
