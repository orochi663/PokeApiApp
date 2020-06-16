package com.example.pokeapiapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by RTanweer on 11/2/2019.
 */
public class MainContentFragment extends Fragment {
    private static final String FINISHED_LOADING = "com.exmple.pokeapi.FINISHED_LOADING";

    private TextView name;
    private TextView base_experience;
    private TextView height;
    private TextView id;
    private TextView order;
    private TextView weight;
    private TextView species;
    private ImageView front;
    private ImageView back;

    View helpView;
    View errorView;

    RecyclerView abilitiesView;
    RecyclerView movesView;
    RecyclerView statsView;
    RecyclerView typesView;

    AbilitiesAdapter abilitiesAdapter;
    MoveAdapter moveAdapter;
    StatsAdapter statsAdapter;
    TypeAdapter typeAdapter;


    private LoadingIntentReciever loadingIntentReciever;

    private Handler handler = new Handler();

    private Runnable initPokemonData = new Runnable() {
        @Override
        public void run() {
            PokemonInfo.init(); // initialize loading
            broadcastFinished();
        }
    };

    private Runnable loadNextPokemon = new Runnable() {
        @Override
        public void run() {
            PokemonInfo nextPokemon = PokemonInfo.requestNewObject();
            loadPokemonInfoToViews(nextPokemon);
            broadcastFinished();
        }
    };

    public MainContentFragment() {
        // Required empty public constructor
    }

    static MainContentFragment newInstance() {
        MainContentFragment fragment = new MainContentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_content, container, false);

        name = view.findViewById(R.id.name);
        base_experience = view.findViewById(R.id.base_experience);
        height = view.findViewById(R.id.height);
        id = view.findViewById(R.id.id);
        order = view.findViewById(R.id.order);
        weight = view.findViewById(R.id.weight);
        species = view.findViewById(R.id.species);
        front = view.findViewById(R.id.front);
        back = view.findViewById(R.id.back);

        abilitiesView = (RecyclerView) view.findViewById(R.id.abilities_view);
        movesView = (RecyclerView) view.findViewById(R.id.moves_view);
        statsView = (RecyclerView) view.findViewById(R.id.stats_view);
        typesView = (RecyclerView) view.findViewById(R.id.types_view);

        abilitiesAdapter = new AbilitiesAdapter();
        moveAdapter = new MoveAdapter();
        statsAdapter = new StatsAdapter();
        typeAdapter = new TypeAdapter();

        abilitiesView.setAdapter(abilitiesAdapter);
        movesView.setAdapter(moveAdapter);
        statsView.setAdapter(statsAdapter);
        typesView.setAdapter(typeAdapter);

        LinearLayoutManager layoutManagerAbilities = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManagerMoves = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManagerStats = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManagerTypes = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);

        abilitiesView.setLayoutManager(layoutManagerAbilities);
        movesView.setLayoutManager(layoutManagerMoves);
        statsView.setLayoutManager(layoutManagerStats );
        typesView.setLayoutManager(layoutManagerTypes);

        helpView = view.findViewById(R.id.help_text);
        helpView.setVisibility(VISIBLE);
        errorView = view.findViewById(R.id.error_view);
        errorView.setVisibility(GONE);

        final Button refresh = view.findViewById(R.id.refresh);
        if(refresh != null) {
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshData();
                }
            });
        }
        return view;
    }

    private void showLoading() {
        FragmentManager manager = Objects.requireNonNull(this.getActivity()).getSupportFragmentManager();
        LoadingDialog loadingDialog = LoadingDialog.getInstance();
        if(!loadingDialog.isVisible()) {
            LoadingDialog.getInstance().show(manager, LoadingDialog.TAG);
        }
    }

    private void hideLoading() {
        LoadingDialog loadingDialog = LoadingDialog.getInstance();
        if(loadingDialog.isVisible()) {
            LoadingDialog.getInstance().dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showLoading();
        loadingIntentReciever = new LoadingIntentReciever();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(loadingIntentReciever, new IntentFilter(FINISHED_LOADING));
        handler.postDelayed(initPokemonData, 200); // delayed execution for UI to finish rendering
    }

    void refreshData() {
        showLoading();
        handler.postDelayed(loadNextPokemon, 200); // delayed execution for UI to finish rendering
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(loadingIntentReciever);
        loadingIntentReciever = null;
    }

    private void loadPokemonInfoToViews(PokemonInfo pokemonInfo) {
        if(pokemonInfo != null) {
            helpView.setVisibility(GONE);

            // Pokemon Data
            name.setText(pokemonInfo.name);
            base_experience.setText(Integer.toString(pokemonInfo.baseExperience));
            height.setText(Integer.toString(pokemonInfo.height));
            id.setText(Integer.toString(pokemonInfo.id));
            order.setText(Integer.toString(pokemonInfo.order));
            weight.setText(Integer.toString(pokemonInfo.weight));
            species.setText(pokemonInfo.species);
            if(pokemonInfo.front != null) {
                front.setImageBitmap(pokemonInfo.front);
            } else {
                front.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.not_found_front));
            }
            if(pokemonInfo.back != null) {
                back.setScaleX(1);
                back.setScaleY(1);
                back.setImageBitmap(pokemonInfo.back);
            } else {
                back.setScaleX(-1);
                back.setScaleY(-1);
                back.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.not_found_front));
            }

            Objects.requireNonNull(getView()).findViewById(R.id.scrollView).setVisibility(VISIBLE);

            abilitiesAdapter.setData(pokemonInfo.abilities);
            moveAdapter.setData(pokemonInfo.moves);
            statsAdapter.setData(pokemonInfo.stats);
            typeAdapter.setData(pokemonInfo.types);

        } else {
            // Show Error
            helpView.setVisibility(VISIBLE);
            errorView.setVisibility(VISIBLE);
        }
    }

    private void broadcastFinished() {
        Intent intent = new Intent();
        intent.setAction(FINISHED_LOADING);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    // Simple reciever for local intent
    class LoadingIntentReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(FINISHED_LOADING )) {
                hideLoading();
            }
        }
    }
}
