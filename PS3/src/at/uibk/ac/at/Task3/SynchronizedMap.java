package at.uibk.ac.at.Task3;

import java.util.AbstractMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by Florian on 04.12.2015.
 */
public class SynchronizedMap<K, V> extends AbstractMap<K, V> {

    private AbstractMap<K, V> map;

    public SynchronizedMap(AbstractMap<K, V> map)
    {
        this.map = map;
    }

    @Override
    public synchronized V put(K key, V value)
    {
        return map.put(key, value);
    }

    @Override
    public synchronized Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public synchronized V getOrDefault(Object key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public synchronized void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    @Override
    public synchronized void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        map.replaceAll(function);
    }

    @Override
    public synchronized V putIfAbsent(K key, V value) {
        return map.putIfAbsent(key, value);
    }

    @Override
    public synchronized boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    @Override
    public synchronized boolean replace(K key, V oldValue, V newValue) {
        return map.replace(key, oldValue, newValue);
    }

    @Override
    public synchronized V replace(K key, V value) {
        return map.replace(key, value);
    }

    @Override
    public synchronized V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public synchronized V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return map.computeIfPresent(key, remappingFunction);
    }

    @Override
    public synchronized V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return map.compute(key, remappingFunction);
    }

    @Override
    public synchronized V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return map.merge(key, value, remappingFunction);
    }
}

