package com.example.pokeapiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RTanweer on 11/3/2019.
 */
public class StatsAdapter  extends RecyclerView.Adapter<StatsAdapter.ViewHolder>{

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView baseStat;
        TextView effort;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.stats_name);
            baseStat = itemView.findViewById(R.id.base_stat);
            effort = itemView.findViewById(R.id.effort);
        }
    }

    private List<PokemonInfo.Stats> data;

    StatsAdapter() {
        data = new ArrayList<>();
    }

    void setData(List<PokemonInfo.Stats> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.row_layout_stats, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        PokemonInfo.Stats value = data.get(position);

        TextView name = holder.name;
        TextView baseStat = holder.baseStat;
        TextView effort = holder.effort;

        name.setText(value.name);
        baseStat.setText(Integer.toString(value.baseStat));
        effort.setText(Integer.toString(value.effort));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}