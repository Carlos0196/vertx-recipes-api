package com.vertx.recipes.repository;

import com.vertx.recipes.dto.RecipeDTO;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

public interface RecipeRepository {

    Flowable<JsonObject> findAll();

    Maybe<JsonObject> findById(String id);

    Single<JsonObject> save(JsonObject recipe);

    Completable delete(String id);

}
