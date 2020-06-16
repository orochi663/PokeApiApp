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
public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder>{


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView slot;

        ViewHolder(View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.type_name);
            slot = itemView.findViewById(R.id.slot);
        }
    }

    private List<PokemonInfo.Type> data;

    TypeAdapter() {
        data = new ArrayList<>();
    }

    void setData(List<PokemonInfo.Type> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.row_layout_types, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        PokemonInfo.Type value = data.get(position);

        TextView name = holder.name;
        TextView slot = holder.slot;

        name.setText(value.name);
        slot.setText(Integer.toString(value.slot));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}