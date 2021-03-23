package com.vertx.recipes.service.recipe;

import com.vertx.recipes.service.recipe.dto.RecipeDTO;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecipeServiceImpl implements RecipeService {

    public static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);

    private static final String COLLECTION = "recipe";

    private final MongoClient client;
    protected final Vertx vertx;

    public RecipeServiceImpl(Vertx vertx, JsonObject config) {
        JsonObject jsonObject = config.getJsonObject("mongo");
        this.client = MongoClient.create(vertx, jsonObject);
        this.vertx = vertx;
    }

    @Override
    public void saveRecipe(JsonObject json, Handler<AsyncResult<JsonObject>> resultHandler) {
        RecipeDTO recipe = new RecipeDTO(json);

        recipe.setId(new ObjectId().toString());
        client.save(COLLECTION,
            new JsonObject()
                .put("_id", recipe.getId())
                .put("title", recipe.getTitle())
                .put("recipe", recipe.getRecipe())
                .put("author", recipe.getAuthor())
                .put("createdAt", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())),
            ar -> {
                if (ar.succeeded()) {
                    resultHandler.handle(Future.succeededFuture(new RecipeDTO(recipe).toJson()));
                } else {
                    resultHandler.handle(Future.failedFuture(ar.cause()));
                }
            });
    }

    @Override
    public void getRecipe(String id, Handler<AsyncResult<JsonObject>> resultHandler) {
        LOGGER.info(String.format(" get recipe data %s ", id));
        JsonObject query = new JsonObject().put("_id", id);
        client.findOne(COLLECTION, query, null, ar -> {
            if (ar.succeeded()) {
                if (ar.result() == null) {
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    RecipeDTO recipe = new RecipeDTO(ar.result().put("id", ar.result().getString("_id")));
                    resultHandler.handle(Future.succeededFuture(recipe.toJson()));
                }
            } else {
                resultHandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }

    @Override
    public void getAllRecipes(Handler<AsyncResult<JsonArray>> resultHandler) {
        /*LOGGER.info(" get recipes data ");
        client.find(COLLECTION, null, null, ar -> {
            if (ar.succeeded()) {
                if (ar.result() == null) {
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    RecipeDTO recipe = new RecipeDTO(ar.result().put("id", ar.result().getString("_id")));
                    resultHandler.handle(Future.succeededFuture(recipe.toJson()));
                }
            } else {
                resultHandler.handle(Future.failedFuture(ar.cause()));
            }
        });*/
    }

    @Override
    public void removeRecipe(String id, Handler<AsyncResult<Void>> resultHandler) {
        LOGGER.info(String.format(" remove recipe data %s ", id));
        JsonObject query = new JsonObject().put("_id", id);
        client.removeDocument(COLLECTION, query, ar -> {
            if (ar.succeeded()) {
                resultHandler.handle(Future.succeededFuture());
            } else {
                resultHandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }
}
