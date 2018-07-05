package com.microsoft.recognizers.text.matcher;

public interface IMatcher<T> {
    void init(Iterable<T>[] values, String[] ids);
    Iterable<MatchResult<T>> find(Iterable<T> queryText);
}
