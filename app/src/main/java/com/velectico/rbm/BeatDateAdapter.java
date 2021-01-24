package com.velectico.rbm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.velectico.rbm.beats.model.BeatDate;
import com.velectico.rbm.beats.model.BeatDateDetails;
import java.util.ArrayList;

public class BeatDateAdapter extends RecyclerView.Adapter<BeatDateAdapter.beatViewHolder> {
    Context context;
    ArrayList<BeatDateDetails> beatDateList;

    public BeatDateAdapter(Context context, ArrayList<BeatDateDetails> beatDateList) {
        this.context = context;
        this.beatDateList = beatDateList;
    }

    @NonNull
    @Override
    public beatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.beat_layout_view,parent,false);
        return new beatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull beatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return beatDateList.size();
    }

    public class beatViewHolder extends RecyclerView.ViewHolder {
        TextView tv_start_date, tv_end_date;
        public beatViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_start_date=itemView.findViewById(R.id.tv_start_date);
            tv_end_date=itemView.findViewById(R.id.tv_end_date);
        }
    }
}
