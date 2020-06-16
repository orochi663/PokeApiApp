package com.example.pokeapiapp;

import android.graphics.Bitmap;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by RTanweer on 11/2/2019.
 */
// JSON Object for parsing PokeMon Data
class PokemonInfo {
    private static String TAG = "PokemonInfo";

    private static String INFO_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static String TOTAL_INFO_URL = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=";

    private static List<String> infoUrls;

    // Pokemon Data
    class Ability{
        String name;
        boolean isHidden;
        int slot;
    }

    class Move{
        String name;
    }

    class Sprites{
        String backDefault;
        String backShiny;
        String backFemale;
        String backShinyFemale;
        String frontDefault;
        String frontShiny;
        String frontFemale;
        String frontShinyFemale;
    }

    class Stats{
        int baseStat;
        int effort;
        String name;
    }

    class Type {
        int slot;
        String name;
    }

    List<Move> moves;
    List<Ability> abilities;
    List<Stats> stats;
    List<Type> types;

    int baseExperience;
    int height;
    // held_items
    int id;
    // is_default;
    String name;
    int order;
    int weight;
    String species;
    private Sprites sprites;
    Bitmap front;
    Bitmap back;

    static void init() {// This must execute in the beginning to get the number of pokemon records.
        // this will give us count of number of pokemonsLoadingDialog.newInstance().show();
        try {
            String infoString = URLClient.getResponseString(INFO_URL);
            JSONObject jInfoObj = new JSONObject(infoString);
            int maxObjects = (Integer) jInfoObj.get("count");
            infoUrls = new ArrayList<>();
            String totalInfoString = URLClient.getResponseString(TOTAL_INFO_URL+maxObjects);
            JSONObject jsonUrlObject = new JSONObject(totalInfoString);
            JSONArray jResults = jsonUrlObject.getJSONArray("results");
            for(int i = 0; i < jResults.length(); i++) {
                JSONObject jResultObj = jResults.getJSONObject(i);
                infoUrls.add(jResultObj.getString("url"));
            }

        }catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private PokemonInfo(String url) {
        parseJSon(url);
    }

    private void parseJSon(String url) {
        try {
            String responseString = URLClient.getResponseString(url);
            JSONObject jsonObject = new JSONObject(responseString);
            baseExperience = jsonObject.getInt("base_experience");
            height = jsonObject.getInt("height");
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            order = jsonObject.getInt("order");
            weight = jsonObject.getInt("weight");

            JSONObject jSpecies = jsonObject.getJSONObject("species");
            species = jSpecies.getString("name");

            // Get Sprites URL from JSonArray
            JSONObject jSprites = jsonObject.getJSONObject("sprites");
            sprites = new Sprites();
            sprites.frontDefault = jSprites.getString("front_default");
            sprites.frontShiny = jSprites.getString("front_shiny");
            sprites.frontFemale = jSprites.getString("front_female");
            sprites.frontShinyFemale = jSprites.getString("front_shiny_female");
            sprites.backDefault = jSprites.getString("back_default");
            sprites.backShiny = jSprites.getString("back_shiny");
            sprites.backFemale = jSprites.getString("back_female");
            sprites.backShinyFemale = jSprites.getString("back_shiny_female");

            loadSprites();// load two images if available otherwise load default

            // Get Moves from JSonArray
            moves = new ArrayList<>();
            JSONArray jMoves = jsonObject.getJSONArray("moves");
            for(int i = 0; i < jMoves.length(); i++) {
                // Create Move Object;
                Move move = new Move();
                JSONObject jMoveObj = jMoves.getJSONObject(i);
                JSONObject jMoveDetail = jMoveObj.getJSONObject("move");
                move.name = jMoveDetail.getString("name");
                moves.add(move);
            }

            // Get Abilities from JSonArray
            abilities = new ArrayList<>();
            JSONArray jAbilities = jsonObject.getJSONArray("abilities");
            for(int i = 0; i < jAbilities.length(); i++) {
                Ability ability = new Ability();
                JSONObject abilityObj = jAbilities.getJSONObject(i);
                ability.isHidden = abilityObj.getBoolean("is_hidden");
                ability.slot = abilityObj.getInt("slot");
                JSONObject jAbilitiesDetails = abilityObj.getJSONObject("ability");
                ability.name = jAbilitiesDetails.getString("name");
                abilities.add(ability);
            }

            // Get Stats from JSonArray
            stats = new ArrayList<>();
            JSONArray jStats = jsonObject.getJSONArray("stats");
            for(int i = 0; i < jStats.length(); i++) {
                Stats stat = new Stats();
                JSONObject jStatsObj = jStats.getJSONObject(i);
                stat.baseStat = jStatsObj.getInt("base_stat");
                stat.effort = jStatsObj.getInt("effort");
                JSONObject jStatDetailObj = jStatsObj.getJSONObject("stat");
                stat.name = jStatDetailObj.getString("name");
                stats.add(stat);
            }

            // Get Stats from JSonArray
            types = new ArrayList<>();
            JSONArray jType = jsonObject.getJSONArray("types");
            for(int i = 0; i < jType.length(); i++) {
                JSONObject jTypeObj = jType.getJSONObject(i);
                Type type = new Type();
                type.slot = jTypeObj.getInt("slot");
                JSONObject jTypeDetails = jTypeObj.getJSONObject("type");
                type.name = jTypeDetails.getString("name");
                types.add(type);
            }
        }catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void loadSprites() throws ExecutionException, InterruptedException {
        // front image Only one ..
        if (sprites.frontDefault != null) {
            front = URLClient.getResponseBitmap(sprites.frontDefault);
        } else if (sprites.frontShiny != null) {
            front = URLClient.getResponseBitmap(sprites.frontShiny);
        } else if (sprites.frontFemale != null) {
            front = URLClient.getResponseBitmap(sprites.frontFemale);
        } else if (sprites.frontShinyFemale != null) {
            front = URLClient.getResponseBitmap(sprites.frontShinyFemale);
        } else {
            front = null;
        }

        // back image
        if (sprites.backDefault != null) {
            back = URLClient.getResponseBitmap(sprites.backDefault);
        } else if (sprites.backShiny != null) {
            back = URLClient.getResponseBitmap(sprites.backShiny);
        } else if (sprites.backFemale != null) {
            back = URLClient.getResponseBitmap(sprites.backFemale);
        } else if (sprites.backShinyFemale != null) {
            back = URLClient.getResponseBitmap(sprites.backShinyFemale);
        } else {
            back = null;
        }
    }

    static PokemonInfo requestNewObject(){
        Random random = new Random();
        int next = random.nextInt(infoUrls.size());
        String url = infoUrls.get(next);
        // Log.d(TAG, "<<<<<<<<<<<<<<<<<"+url);
        return new PokemonInfo(url);
    }
}
