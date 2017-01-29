package de.ek.private_timeline.list;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.plumillonforge.android.chipview.ChipView;

import de.ek.private_timeline.R;

/**
 * Created by Enrico on 02.11.2016.
 */

public class ViewHolderTimeLine extends RecyclerView.ViewHolder{
    TextView content;
    ChipView tagView;
    TextView time;

    ViewHolderTimeLine(View itemView) {
        super(itemView);

        content = (TextView) itemView.findViewById(R.id.tv_content);
        tagView = (ChipView) itemView.findViewById(R.id.tag_list);
        time = (TextView) itemView.findViewById(R.id.tv_time);
    }
}
