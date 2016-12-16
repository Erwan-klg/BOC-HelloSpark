package com.brestopencampus.spark;

import com.brestopencampus.spark.model.Beer;
import spark.Request;
import spark.Response;
import spark.Route;

import com.mongodb.*;
import org.bson.types.ObjectId;


import static spark.Spark.*;

/**
 * Created by sca on 29/10/16.
 */
public class Application {

  public static void main(String[] args) throws Exception {

    exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
    port(8080);

    BeerDao dao = new BeerDao(mongo());

    get("/", (req, res) -> "Hello Beers");

    /**
     * REST Part
     */
    JSonTransformer jsonT = new JSonTransformer();
    // recherche de bière, si rien il renvoi toute les bières
    get("/json/Beers", (req, res) -> {
        String name=req.queryParams("name");
        String alcohol=req.queryParams("alcohol");
        if(name != null) {
            return dao.findByName(name);
        }
        else if (alcohol != null) {
            return dao.findByAlcohol(alcohol);
        }
        else{
            return dao.all();
        }
    }, jsonT);
    get("/json/Beers/:id", "application/json",  (req, res) ->  dao.find(req.params("id"))
        , jsonT);

    post("/json/Beers", (req, res) -> {
          Beer b = dao.add(new Beer(req.queryParams("name"), Float.parseFloat(req.queryParams("alcohol"))));
          res.status(201);
          return b;
    }, jsonT);
    delete("/json/Beers/:id", (ICRoute) (req) -> dao.remove(req.params("id")));
  }

  private static DB mongo() throws Exception {
    String host = "127.0.0.1";//System.getenv("MONGODB_DB_HOST");
    if (host == null) {
      MongoClient mongoClient = new MongoClient("localhost");
      return mongoClient.getDB("todoapp");
    }
    int port = 27017;//Integer.parseInt(System.getenv("MONGODB_DB_PORT"));
    String dbname = "HelloSpark";//System.getenv("HelloSpark");
    String username = System.getenv("MONGODB_DB_USERNAME");
    String password = System.getenv("MONGODB_DB_PASSWORD");
    MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
    MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), mongoClientOptions);
    mongoClient.setWriteConcern(WriteConcern.SAFE);
    DB db = mongoClient.getDB(dbname);
    return db;/*
    if (db.authenticate(username, password.toCharArray())) {
      return db;
    } else {
      throw new RuntimeException("Not able to authenticate with MongoDB");
    }*/
  }



  @FunctionalInterface
  private interface ICRoute extends Route {
    default Object handle(Request request, Response response) throws Exception {
      handle(request);
      return "";
    }
    void handle(Request request) throws Exception;
  }

}
