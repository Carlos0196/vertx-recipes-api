package com.vertx.recipes.dto;

import com.mongodb.lang.NonNull;
import io.vertx.core.json.JsonObject;

import java.util.Iterator;
import java.util.Map;

public class RecipeDTO {

    @NonNull
    private String id;

    @NonNull
    private String title;

    @NonNull
    private String recipe;

    @NonNull
    private String author;

    public RecipeDTO(RecipeDTO other) {
        this.setId(other.getId());
        this.setTitle(other.getTitle());
        this.setRecipe(other.getRecipe());
        this.setAuthor(other.getAuthor());
    }

    public RecipeDTO(JsonObject json) {
        fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        toJson(this, json);
        return json;
    }

    @Override
    public String toString() {
        return this.toJson().encodePrettily();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(@NonNull String recipe) {
        this.recipe = recipe;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@NonNull String author) {
        this.author = author;
    }


    public static void fromJson(@NonNull Iterable<Map.Entry<String, Object>> json, RecipeDTO obj) {
        Iterator var2 = json.iterator();

        while(var2.hasNext()) {
            Map.Entry<String, Object> member = (Map.Entry)var2.next();
            String var4 = (String)member.getKey();
            byte var5 = -1;
            switch(var4.hashCode()) {
                case -1406328437:
                    if (var4.equals("author")) {
                        var5 = 0;
                    }
                    break;
                case -934914674:
                    if (var4.equals("recipe")) {
                        var5 = 2;
                    }
                    break;
                case 3355:
                    if (var4.equals("id")) {
                        var5 = 1;
                    }
                    break;
                case 110371416:
                    if (var4.equals("title")) {
                        var5 = 3;
                    }
            }

            switch(var5) {
                case 0:
                    if (member.getValue() instanceof String) {
                        obj.setAuthor((String)member.getValue());
                    }
                    break;
                case 1:
                    if (member.getValue() instanceof String) {
                        obj.setId((String)member.getValue());
                    }
                    break;
                case 2:
                    if (member.getValue() instanceof String) {
                        obj.setRecipe((String)member.getValue());
                    }
                    break;
                case 3:
                    if (member.getValue() instanceof String) {
                        obj.setTitle((String)member.getValue());
                    }
            }
        }

    }

    public static void toJson(RecipeDTO obj, @NonNull JsonObject json) {
        toJson(obj, json.getMap());
    }

    public static void toJson(@NonNull RecipeDTO obj, Map<String, Object> json) {
        if (obj.getAuthor() != null) {
            json.put("author", obj.getAuthor());
        }

        if (obj.getId() != null) {
            json.put("id", obj.getId());
        }

        if (obj.getRecipe() != null) {
            json.put("recipe", obj.getRecipe());
        }

        if (obj.getTitle() != null) {
            json.put("title", obj.getTitle());
        }

    }
}
