package com.vertx.recipes.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class RecipeHttpServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeHttpServerVerticle.class);

    public RecipeHttpServerVerticle() {
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        super.start();

    }


}
