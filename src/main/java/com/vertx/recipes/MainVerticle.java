package com.vertx.recipes;

import com.vertx.recipes.controller.RecipeController;
import com.vertx.recipes.repository.RecipeRepositoryProvider;
import com.vertx.recipes.service.RecipeServiceProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start();

        // Init recipe service provider
        RecipeServiceProvider recipeServiceProvider = RecipeServiceProvider.getInstance();
        recipeServiceProvider.init();

        // Init recipe repository provider
        RecipeRepositoryProvider recipeRepositoryProvider = RecipeRepositoryProvider.getInstance();
        recipeRepositoryProvider.init(vertx, config());

        // Deploy http server
        Router router = Router.router(vertx);

        // Nos permitirá parsear el body handler para obtener el payload
        router.route().handler(BodyHandler.create());

        // Heartbeat
        router.get(config().getString("heartbeat.path", "/ping")).handler(context -> {
            JsonObject checkResult = new JsonObject().put("status", "UP");
            context.response().end(checkResult.encode());
        });

        // Register Recipe controller
        router.route(RecipeController.getApiPath()).handler(new RecipeController(router));

        // Server config with default data
        String host = config().getString("vertx.host", "0.0.0.0");
        int port = config().getInteger("vertx.port", 7777);

        // Http server
        vertx.createHttpServer().requestHandler(router).listen(port, host, res -> {
            if (res.succeeded()) {
                LOGGER.info(" started " + this.getClass().getSimpleName() + " on " + host + ":" + port);
            } else {
                LOGGER.info(" failed to start " + this.getClass().getSimpleName());
            }
        });
    }

}
