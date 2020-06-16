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
public class AbilitiesAdapter extends RecyclerView.Adapter<AbilitiesAdapter.ViewHolder>{

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView isHidden;
        TextView slot;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.abilities_name);
            isHidden = itemView.findViewById(R.id.is_hidden);
            slot = itemView.findViewById(R.id.slot);
        }
    }

    private List<PokemonInfo.Ability> data;

    AbilitiesAdapter() {
        data = new ArrayList<>();
    }

    void setData(List<PokemonInfo.Ability> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AbilitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View rootView = inflater.inflate(R.layout.row_layout_abilities, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AbilitiesAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        PokemonInfo.Ability value = data.get(position);

        TextView name = holder.name;
        TextView isHidden = holder.isHidden;
        TextView slot = holder.slot;

        name.setText(value.name);
        isHidden.setText(Boolean.toString(value.isHidden));
        slot.setText(Integer.toString(value.slot));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}