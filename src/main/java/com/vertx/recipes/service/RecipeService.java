package com.vertx.recipes.service;

import com.vertx.recipes.dto.RecipeDTO;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface RecipeService {

    Flowable<RecipeDTO> findAll();

    Maybe<RecipeDTO> findById(String id);

    Single<RecipeDTO> save(RecipeDTO recipe);

    Completable delete(String id);

}
