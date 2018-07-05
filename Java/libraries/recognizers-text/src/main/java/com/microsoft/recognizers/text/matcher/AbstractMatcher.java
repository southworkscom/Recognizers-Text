package com.microsoft.recognizers.text.matcher;

public abstract class AbstractMatcher<T> implements IMatcher<T> {
    abstract void insert(Iterable<T> value, String id);

    protected void batchInsert(Iterable<T>[] values, String[] ids) {
        if (values.length != ids.length) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < values.length; i++) {
            insert(values[i], ids[i]);
        }
    }

    boolean isMatch(Iterable<T> queryText) {
        return find(queryText) != null;
    }
}
