package com.brestopencampus.spark;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by sca on 29/10/16.
 */
public class JSonTransformer implements ResponseTransformer {

  private Gson gson = new Gson();

  @Override
  public String render(Object model) {
    return gson.toJson(model);
  }

}
