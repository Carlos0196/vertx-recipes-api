package com.vertx.recipes.verticle;

import com.vertx.recipes.service.recipe.RecipeService;
import com.vertx.recipes.service.recipe.dto.RecipeDTO;
import io.vertx.blueprint.microservice.common.RestAPIVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.commons.lang3.StringUtils;

public class RecipeHttpServerVerticle extends RestAPIVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeHttpServerVerticle.class);

    private static final String API_PATH = "recipes";

    private static final String API_SAVE = String.format("/%s", API_PATH);
    private static final String API_GET = String.format("/%s/:id", API_PATH);
    private static final String API_GET_ALL = String.format("/%s", API_PATH);
    private static final String API_DELETE = String.format("/%s/:id", API_PATH);

    public RecipeHttpServerVerticle() {
    }

    @Override
    public void start(Promise<Void> promise) throws Exception {
        super.start();

        Router router = Router.router(vertx);

        // CORS
        enableCorsSupport(router);

        // Nos permitirá parsear el body handler para obtener el payload
        router.route().handler(BodyHandler.create());

        // Routes
        router.post(API_SAVE).handler(this::apiPost);
        router.get(API_GET).handler(this::apiGet);
        router.get(API_GET_ALL).handler(this::apiGetAll);
        router.delete(API_DELETE).handler(this::apiDelete);

        // Health check
        enableHeartbeatCheck(router, config());

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

    private void apiPost(RoutingContext context) {
        RecipeDTO recipeDTO = new RecipeDTO(new JsonObject(context.getBodyAsString()));
        LOGGER.info("apiPost with values  " + recipeDTO.toString());

        if (StringUtils.isBlank(recipeDTO.getTitle()) ||
            StringUtils.isBlank(recipeDTO.getRecipe()) ||
            StringUtils.isBlank(recipeDTO.getAuthor())) {

            badRequest(context, new IllegalStateException("Sensor bad request"));
        } else {
            getRecipeService().saveRecipe(recipeDTO.toJson(), ar -> {
                if (ar.succeeded()) {
                    RecipeDTO newRecipeDTO = new RecipeDTO(ar.result());
                    JsonObject result = new JsonObject().put("message", String.format("recipe %s saved", newRecipeDTO.getId()));

                    resultBody(context, result, 200);
                } else {
                    internalError(context, ar.cause());
                }
            });
        }
    }

    private void apiGet(RoutingContext context) {
        String id = context.request().getParam("id");
        getRecipeService().getRecipe(id, resultHandlerNonEmpty(context));
    }

    private void apiGetAll(RoutingContext context) {
        /*String id = context.request().getParam("id");
        getRecipeService().getRecipe(id, resultHandlerNonEmpty(context));*/
    }

    private void apiDelete(RoutingContext context) {
        String id = context.request().getParam("id");
        getRecipeService().removeRecipe(id, deleteResultHandler(context));
    }

    private RecipeService getRecipeService() {
        return RecipeService.createProxy(vertx, RecipeService.SERVICE_ADDRESS);
    }

}
