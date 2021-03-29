package com.vertx.recipes.service;

import com.vertx.recipes.dto.RecipeDTO;
import com.vertx.recipes.repository.RecipeRepository;
import com.vertx.recipes.repository.RecipeRepositoryProvider;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class RecipeServiceImpl implements RecipeService {

    public RecipeServiceImpl() {
    }

    @Override
    public Flowable<RecipeDTO> findAll() {
        return getRecipeRepository().findAll()
            .map(r -> r.put("id", r.getString("_id")))
            .map(RecipeDTO::new);
    }

    @Override
    public Maybe<RecipeDTO> findById(String id) {
        return getRecipeRepository().findById(id)
            .map(r -> r.put("id", r.getString("_id")))
            .map(RecipeDTO::new);
    }

    @Override
    public Single<RecipeDTO> save(RecipeDTO recipe) {
        return getRecipeRepository().save(recipe.toJson())
            .map(r -> r.put("id", r.getString("_id")))
            .map(RecipeDTO::new);
    }

    @Override
    public Completable delete(String id) {
        return getRecipeRepository().delete(id);
    }

    private RecipeRepository getRecipeRepository() {
        return RecipeRepositoryProvider.getInstance().getRecipeRepository();
    }

}
