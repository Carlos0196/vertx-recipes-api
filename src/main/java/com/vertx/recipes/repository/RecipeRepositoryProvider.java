package com.vertx.recipes.repository;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public final class RecipeRepositoryProvider {

    private RecipeRepository recipeRepository;

    /* Singleton */
    private static RecipeRepositoryProvider instance = new RecipeRepositoryProvider();

    public synchronized RecipeRepository init(Vertx vertx, JsonObject config) {
        setRecipeRepository(new RecipeRepositoryImpl(vertx, config));
        return getRecipeRepository();
    }

    public static RecipeRepositoryProvider getInstance() {
        return instance;
    }

    private RecipeRepositoryProvider() {
    }

    public RecipeRepository getRecipeRepository() {
        return recipeRepository;
    }

    private void setRecipeRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

}
