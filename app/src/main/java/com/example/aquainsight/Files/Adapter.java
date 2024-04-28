package com.example.aquainsight.Files;

import static com.example.aquainsight.NewRaiseActivty.ADD;
import static com.example.aquainsight.NewRaiseActivty.ISSUE;
import static com.example.aquainsight.NewRaiseActivty.SEEN;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aquainsight.R;

import java.util.ArrayList;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList<Map<String, Object>> data;
    public Adapter(ArrayList<Map<String, Object>> data) {
        this.data=data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_activity,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.headline.setText(data.get(position).get(ADD).toString());
        holder.issue.setText(data.get(position).get(ISSUE).toString());
        boolean review=(boolean) data.get(position).get(SEEN);
        if(review==true)
            holder.Reviewed.setText("Yes");
        else
            holder.Reviewed.setText("No");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView headline,
                Reviewed,
                issue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headline=itemView.findViewById(R.id.FHeadline);
            Reviewed=itemView.findViewById(R.id.Reviewed);
            issue=itemView.findViewById(R.id.CIssue);
        }
    }
}
