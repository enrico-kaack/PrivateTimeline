package de.ek.private_timeline.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ek.private_timeline.R;
import de.ek.private_timeline.persistence.Tag;
import de.ek.private_timeline.persistence.TimelineObject;
import de.ek.private_timeline.persistence.Typ;

/**
 * Created by Enrico on 02.11.2016.
 */

public class RecyclerViewAdapterPreview extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<String> list = Collections.emptyList();
    Context context;
    RecyclerView rc_view;

    private ItemInteractionListener listener;

    class ViewHolderImage extends RecyclerView.ViewHolder{
        ImageView img;
        ImageButton btnDelete;

        private ItemInteractionListener listener;

        ViewHolderImage(View itemView, final ItemInteractionListener listener) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.image_preview);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btn_delete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });


        }
    }



    public RecyclerViewAdapterPreview(List<String> list, Context context, ItemInteractionListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    public void setNewData(List<String> timelineObjectList) {
        this.list = timelineObjectList;
        this.notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_preview_image, parent, false);
        ViewHolderImage holder = new ViewHolderImage(v, listener);
        return holder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String item = list.get(position);


        ViewHolderImage holderImage = (ViewHolderImage)holder;
        Glide.with(context).load(item).centerCrop().into(holderImage.img);
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

    public String getObjectForPosition(int pos){
        return list.get(pos);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, String data) {
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
