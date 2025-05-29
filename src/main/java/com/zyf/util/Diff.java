package com.zyf.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Data
@Accessors(chain = true)
public class Diff<T> {

    private List<T> addList;
    private List<T> delList;
    // 已存在的对象集合 对应的新值
    private Map<T, T> updateMap;

    public List<T> getEffectList() {
        final List<T> list = new ArrayList<>(addList);
        list.addAll(getExistsList());
        return list;
    }

    public Diff<T> addConsumer(BiConsumer<Diff<T>, List<T>> biConsumer) {
        if (isNotEmpty(addList)) {
            biConsumer.accept(this, addList);
        }
        return this;
    }

    public Diff<T> delConsumer(BiConsumer<Diff<T>, List<T>> biConsumer) {
        if (isNotEmpty(delList)) {
            biConsumer.accept(this, delList);
        }
        return this;
    }

    private boolean isNotEmpty(List<T> list) {
        return list != null && !list.isEmpty();
    }

    public Diff<T> existsConsumer(BiConsumer<Diff<T>, List<T>> biConsumer) {
        final List<T> existsList = getExistsList();
        if (isNotEmpty(existsList)) {
            biConsumer.accept(this, existsList);
        }
        return this;
    }


    public Diff<T> updateConsumer(BiConsumer<Diff<T>, Map<T, T>> biConsumer) {
        if (updateMap != null && !updateMap.isEmpty()) {
            biConsumer.accept(this, updateMap);
        }
        return this;
    }

    public Diff<T> forEachUpdateMapConsumer(BiConsumer<T, T> biConsumer) {
        if (updateMap != null && !updateMap.isEmpty()) {
            for (Map.Entry<T, T> entry : updateMap.entrySet()) {
                final T oldPo = entry.getKey();
                final T newPo = entry.getValue();
                biConsumer.accept(oldPo, newPo);
            }
        }
        return this;
    }

    public List<T> getExistsList() {
        return getUpdateMapKeys();
    }

    public List<T> getUpdateMapKeys() {
        return updateMap.keySet().stream().toList();
    }

    public List<T> getUpdateMapValues() {
        return updateMap.values().stream().toList();
    }
}
