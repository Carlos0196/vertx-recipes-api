package com.vertx.recipes.service.recipe.dto;

import com.mongodb.lang.NonNull;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true)
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
        RecipeDTOConverter.fromJson(json, this);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        RecipeDTOConverter.toJson(this, json);
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

}
