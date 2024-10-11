package com.stark.webbanhang.helper.base.construct;

import com.stark.webbanhang.api.user.dto.response.CategoryResponse;
import com.stark.webbanhang.api.user.dto.response.PageResponse;

import java.util.List;
import java.util.UUID;

public abstract class RestfullService<T, P , S , U> {
    public abstract List<T> getAll(P p);

    public abstract T get(UUID id);

    public abstract T store(S s);

    public abstract T update(UUID id, U  u);

    public abstract void destroy(UUID id);

    public abstract PageResponse<T> getAll(int page, int size);
}