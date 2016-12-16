package com.brestopencampus.spark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import spark.Redirect;
import java.util.*;
import com.mongodb.*;
import org.bson.types.ObjectId;



/**
 * Created by sca on 05/12/16.
 */
@Data
@AllArgsConstructor
public class Beer {

  String name;
  String id;
  double alcohol;

  public String getId() {
    return id;
  }

  public void setId(String id){
    this.id = id;
  }

  public Beer(BasicDBObject dbObject) {
    this.id = ((ObjectId) dbObject.get("_id")).toString();
    this.name = dbObject.getString("name");
    this.alcohol = dbObject.getDouble("alcohol");
  }

  public Beer(String name, float alcohol) {
    this.name = name;
    this.alcohol = alcohol;
    this.id = UUID.randomUUID().toString();
  }

  public String getName() {
    return name;
  }

  public double getAlcohol() {
    return alcohol;
  }
}
