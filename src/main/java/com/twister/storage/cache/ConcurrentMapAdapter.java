/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twister.storage.cache;

import com.google.common.collect.ForwardingMap;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * A forwarding map that adapts the delegate to the {@link ConcurrentMap}
 * interface. The adaption is not thread-safe.
 *
 * @author bmanes@google.com (Ben Manes)
 */
final class ConcurrentMapAdapter<K, V> extends ForwardingMap<K, V> implements ConcurrentMap<K, V> {
  private final Map<K, V> delegate;

  ConcurrentMapAdapter(Map<K, V> delegate) {
    this.delegate = delegate;
  }

  @Override
  public V putIfAbsent(K key, V value) {
    V currentValue = get(key);
    return (currentValue == null)
        ? put(key, value)
        : currentValue;
  }

  @Override
  public boolean remove(Object key, Object value) {
    if (value.equals(get(key))) {
      remove(key);
      return true;
    }
    return false;
  }

  @Override
  public V replace(K key, V value) {
    return containsKey(key)
        ? put(key, value)
        : null;
  }

  @Override
  public boolean replace(K key, V oldValue, V newValue) {
    V currentValue = get(key);
    if (oldValue.equals(currentValue)) {
      put(key, newValue);
      return true;
    }
    return false;
  }

  @Override
  protected Map<K, V> delegate() {
    return delegate;
  }
}
