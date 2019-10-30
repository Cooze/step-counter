package org.cooze.stepcounter.core.protocol;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-07-29
 **/

public class NameValue<T> implements Serializable {

    private String name;
    private T value;

    public NameValue(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public NameValue() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NameValue{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameValue)) return false;
        NameValue<?> nameValue = (NameValue<?>) o;
        return Objects.equals(name, nameValue.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
