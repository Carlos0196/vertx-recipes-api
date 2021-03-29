package com.vertx.recipes.repository;

import io.reactivex.*;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.mongo.MongoClient;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecipeRepositoryImpl implements RecipeRepository {

    private static final String COLLECTION = "recipe";

    private final MongoClient client;
    protected final Vertx vertx;

    public RecipeRepositoryImpl(Vertx vertx, JsonObject config) {
        JsonObject jsonObject = config.getJsonObject("mongo");
        this.client = MongoClient.create(vertx, jsonObject);
        this.vertx = vertx;
    }

    @Override
    public Flowable<JsonObject> findAll() {
        return Flowable.create(flowableEmitter -> {
            ReadStream<JsonObject> readStream = client.findBatch(COLLECTION, new JsonObject());
            readStream.handler(flowableEmitter::onNext);
            readStream.exceptionHandler(flowableEmitter::onError);
            readStream.endHandler((ar) -> flowableEmitter.onComplete());
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Maybe<JsonObject> findById(String id) {
        return Maybe.create(maybeEmitter -> {
            JsonObject query = new JsonObject().put("_id", id);
            client.findOne(COLLECTION, query, null, ar -> {
                if (ar.succeeded()) {
                    if (ar.result() == null) {
                        maybeEmitter.onComplete();
                    } else {
                        maybeEmitter.onSuccess(ar.result().put("id", ar.result().getString("_id")));
                    }
                } else {
                    maybeEmitter.onError(ar.cause());
                }
            });
        });
    }

    @Override
    public Single<JsonObject> save(JsonObject json) {
        return Single.create(singleEmitter -> {
            json.put("_id", new ObjectId().toString());
            json.put("createdAt", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            client.save(COLLECTION, json,
                ar -> {
                    if (ar.succeeded()) {
                        singleEmitter.onSuccess(json);
                    } else {
                        singleEmitter.onError(ar.cause());
                    }
                });
        });
    }

    @Override
    public Completable delete(String id) {
        return Completable.create(completableEmitter -> {
            JsonObject query = new JsonObject().put("_id", id);
            client.removeDocument(COLLECTION, query, ar -> {
                if (ar.succeeded()) {
                    completableEmitter.onComplete();
                } else {
                    completableEmitter.onError(ar.cause());
                }
            });
        });
    }
}
