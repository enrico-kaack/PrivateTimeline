package de.ek.private_timeline.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RecyclerViewAdapterTimeLine extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<TimelineObject> list = Collections.emptyList();
    Context context;
    DateFormat dateFormat;

    class ViewHolderText extends RecyclerView.ViewHolder {
        TextView content;
        ChipView tagView;
        TextView time;

        ViewHolderText(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.tv_content);
            tagView = (ChipView) itemView.findViewById(R.id.tag_list);
            time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    class ViewHolderSingleImage extends RecyclerView.ViewHolder {
        TextView content;
        ChipView tagView;
        TextView time;
        ImageView single_image;

        ViewHolderSingleImage(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.tv_content);
            tagView = (ChipView) itemView.findViewById(R.id.tag_list);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            single_image = (ImageView) itemView.findViewById(R.id.iv_single_img);
        }
    }

    class ViewHolderMultipleImage extends RecyclerView.ViewHolder {
        TextView content;
        ChipView tagView;
        TextView time;
        LinearLayout image_list;

        ViewHolderMultipleImage(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.tv_content);
            tagView = (ChipView) itemView.findViewById(R.id.tag_list);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            image_list = (LinearLayout) itemView.findViewById(R.id.image_list);
        }
    }


    public RecyclerViewAdapterTimeLine(List<TimelineObject> list, Context context) {
        this.list = list;
        this.context = context;
        dateFormat = DateFormat.getDateTimeInstance();
    }

    public void setNewData(List<TimelineObject> timelineObjectList) {
        this.list = timelineObjectList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
         return list.get(position).getTyp();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        switch (viewType){
            case Typ.TEXT:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
                ViewHolderText holder = new ViewHolderText(v);
                return holder;
            case Typ.SINGLE_IMAGE:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_single_image, parent, false);
                ViewHolderSingleImage holder2 = new ViewHolderSingleImage(v2);
                return holder2;
            case Typ.MULTIPLE_IMAGES:
                View v3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_multiple_images, parent, false);
                ViewHolderMultipleImage holder3 = new ViewHolderMultipleImage(v3);
                return holder3;
            default:
                View v0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
                ViewHolderText holder0 = new ViewHolderText(v0);
                return holder0;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TimelineObject item = list.get(position);

        switch (holder.getItemViewType()) {
            case Typ.TEXT:
                ViewHolderText holderText = (ViewHolderText)holder;
                holderText.content.setText(list.get(position).getContent());

                List<Chip> chipList = new ArrayList<>(item.getTags().size());
                for (Tag t:item.getTags()) {
                    chipList.add(t);
                }
                holderText.tagView.setChipList(chipList);
                holderText.time.setText(dateFormat.format(item.getTime()));
                break;
            case Typ.SINGLE_IMAGE:
                ViewHolderSingleImage holderSingleImage = (ViewHolderSingleImage)holder;
                holderSingleImage.content.setText(list.get(position).getContent());

                List<Chip> chipList2 = new ArrayList<>(item.getTags().size());
                for (Tag t:item.getTags()) {
                    chipList2.add(t);
                }
                holderSingleImage.tagView.setChipList(chipList2);
                holderSingleImage.time.setText(dateFormat.format(item.getTime()));
                Glide.with(context).load(item.getAttributeValue("image_path")).fitCenter().into(holderSingleImage.single_image);
                break;
            case Typ.MULTIPLE_IMAGES:
                ViewHolderMultipleImage holderMultipleImage = (ViewHolderMultipleImage)holder;
                holderMultipleImage.content.setText(list.get(position).getContent());

                List<Chip> chipList3 = new ArrayList<>(item.getTags().size());
                for (Tag t:item.getTags()) {
                    chipList3.add(t);
                }
                holderMultipleImage.tagView.setChipList(chipList3);
                holderMultipleImage.time.setText(dateFormat.format(item.getTime()));

                for (int i = 0; i< Integer.parseInt(item.getAttributeValue("image_count"));i++){
                    ImageView imgView = new ImageView(context);
                    holderMultipleImage.image_list.addView(imgView);
                    Glide.with(context).load(item.getAttributeValue("image_path" + i)).fitCenter().into(imgView);
                }

                break;

            default:
                ViewHolderText holderText0 = (ViewHolderText)holder;
                holderText0.content.setText(list.get(position).getContent());

                List<Chip> chipList0 = new ArrayList<>(item.getTags().size());
                for (Tag t:item.getTags()) {
                    chipList0.add(t);
                }
                holderText0.tagView.setChipList(chipList0);
                holderText0.time.setText(dateFormat.format(item.getTime()));
        }


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
