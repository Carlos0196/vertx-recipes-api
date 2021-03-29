package com.vertx.recipes.controller;

import com.vertx.recipes.dto.RecipeDTO;
import com.vertx.recipes.service.RecipeService;
import com.vertx.recipes.service.RecipeServiceProvider;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

public class RecipeController implements Handler<RoutingContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);

    private static final String API_PATH = "recipes";

    private static final String API_SAVE = String.format("/%s", API_PATH);
    private static final String API_GET = String.format("/%s/:id", API_PATH);
    private static final String API_GET_ALL = String.format("/%s", API_PATH);
    private static final String API_DELETE = String.format("/%s/:id", API_PATH);

    public RecipeController(Router router) {
        router.post(API_SAVE).handler(this::apiPost);
        router.get(API_GET).handler(this::apiGet);
        router.get(API_GET_ALL).handler(this::apiGetAll);
        router.delete(API_DELETE).handler(this::apiDelete);
    }

    @Override
    public void handle(RoutingContext routingContext) {
        routingContext.next();
    }

    public static String getApiPath() {
        return String.format("/%s", API_PATH);
    }

    private void apiGetAll(RoutingContext context) {
        LOGGER.info("apiGet for all resources ");
        getRecipeService().findAll().toList().subscribe(
            recipes -> response(context, new JsonArray(recipes).encodePrettily(), 200),
            ex -> response(context, new JsonObject().put("error", ex.getMessage()).encodePrettily(), 500));;
    }

    private void apiGet(RoutingContext context) {
        LOGGER.info("apiGet for resource " + context.request().getParam("id"));
        String id = context.request().getParam("id");
        getRecipeService().findById(id).subscribe(
            r -> response(context, r.toJson().encodePrettily(), 200),
            ex -> response(context, new JsonObject().put("error", ex.getMessage()).encodePrettily(), 500),
            () -> response(context, new JsonObject().put("message", "not_found").encodePrettily(), 404));
    }

    private void apiPost(RoutingContext context) {
        RecipeDTO recipeDTO = new RecipeDTO(new JsonObject(context.getBodyAsString()));
        LOGGER.info("apiPost with values  " + recipeDTO.toString());

        if (StringUtils.isBlank(recipeDTO.getTitle()) ||
            StringUtils.isBlank(recipeDTO.getRecipe()) ||
            StringUtils.isBlank(recipeDTO.getAuthor())) {

            response(context, new JsonObject().put("error", new IllegalStateException("Sensor bad request").getMessage()).encodePrettily(), 400);
        } else {
            getRecipeService().save(recipeDTO).subscribe(
                r -> response(context, new JsonObject().put("message", String.format("recipe %s saved", r.getId())).encodePrettily(), 200),
                ex -> response(context, new JsonObject().put("error", ex.getMessage()).encodePrettily(), 500));
        }
    }

    private void apiDelete(RoutingContext context) {
        LOGGER.info("apiDelete for resource " + context.request().getParam("id"));
        String id = context.request().getParam("id");
        getRecipeService().delete(id).subscribe(
            () -> response(context, new JsonObject().put("message", "delete_success").encodePrettily(), 204),
            ex -> response(context, new JsonObject().put("error", ex.getMessage()).encodePrettily(), 500)
        );
    }

    private RecipeService getRecipeService() {
        return RecipeServiceProvider.getInstance().getRecipeService();
    }

    private void response(RoutingContext context, String result, int status) {
        context.response()
            .setStatusCode(status == 0 ? 200 : status)
            .putHeader("content-type", "application/json")
            .end(result);
    }
}
