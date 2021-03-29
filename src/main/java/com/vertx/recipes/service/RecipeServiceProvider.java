package com.vertx.recipes.service;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public final class RecipeServiceProvider {

    private RecipeService recipeService;

    /* Singleton */
    private static RecipeServiceProvider instance = new RecipeServiceProvider();

    public synchronized RecipeService init() {
        setRecipeService(new RecipeServiceImpl());
        return getRecipeService();
    }

    public static RecipeServiceProvider getInstance() {
        return instance;
    }

    private RecipeServiceProvider() {
    }

    public RecipeService getRecipeService() {
        return recipeService;
    }

    private void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

}
