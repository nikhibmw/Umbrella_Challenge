package com.foo.umbrella.data;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonAdapter.Factory;

@com.ryanharter.auto.value.moshi.MoshiAdapterFactory
public abstract class MoshiAdapterFactory implements JsonAdapter.Factory {
  public static JsonAdapter.Factory create() {
    return new AutoValueMoshi_MoshiAdapterFactory();
  }
}
