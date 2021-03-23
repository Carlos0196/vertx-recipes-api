package com.vertx.recipes.service.recipe;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface RecipeService {

    public static final String SERVICE_NAME = "recipe-eb-service";
    public static final String SERVICE_ADDRESS = "service.recipe";

    public abstract void saveRecipe(JsonObject json, Handler<AsyncResult<JsonObject>> resultHandler);

    public abstract void getRecipe(String id, Handler<AsyncResult<JsonObject>> resultHandler);

    public abstract void getAllRecipes(Handler<AsyncResult<JsonArray>> resultHandler);

    public abstract void removeRecipe(String id, Handler<AsyncResult<Void>> resultHandler);

    static RecipeService createProxy(Vertx vertx, String address) {
        return new RecipeServiceVertxEBProxy(vertx, address);
    }

}
