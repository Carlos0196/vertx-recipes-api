package com.vertx.recipes;

import com.vertx.recipes.service.recipe.RecipeService;
import com.vertx.recipes.service.recipe.RecipeServiceProvider;
import com.vertx.recipes.verticle.RecipeHttpServerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ServiceBinder;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start();

        // Init recipe service provider
        RecipeServiceProvider recipeServiceProvider = RecipeServiceProvider.getInstance();
        recipeServiceProvider.init(vertx, config());

        // Bind recipe service
        new ServiceBinder(vertx)
            .setAddress(RecipeService.SERVICE_ADDRESS)
            .register(RecipeService.class, recipeServiceProvider.getRecipeService());

        // Deploy recipe verticle
        deployVerticle()
            .onSuccess(ar -> {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            })
            .onFailure(ar -> startPromise.fail(ar.getCause()));
    }

    private Future<Void> deployVerticle() {
        Promise<Void> promise = Promise.promise();
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setConfig(config());
        deploymentOptions.setInstances(Runtime.getRuntime().availableProcessors() * 2);

        // Http server verticle
        vertx.deployVerticle(RecipeHttpServerVerticle.class.getName(), deploymentOptions, ar -> {
            if (ar.succeeded()) {
                LOGGER.info(String.format("Deployment verticle ok RecipeHttpServerVerticle "));
                promise.complete();
            } else {
                LOGGER.info(String.format("Deployment verticle ko RecipeHttpServerVerticle " + ar.cause()));
                promise.fail(ar.cause());
            }
        });

        return promise.future();
    }
}
