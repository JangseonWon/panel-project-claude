package com.gcgenome.lims.api;

import com.gcgenome.lims.dto.Slice;
import elemental2.dom.Response;
import elemental2.promise.Promise;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseToSlice {
    <T> Promise<Slice<T>> map(Response response, Class<T> clazz) {
        Slice<T> slice = new Slice<>();
        var totalCnt = Long.parseLong(response.headers.get("X-Total-Count"));
        var totalPage = Long.parseLong(response.headers.get("X-Total-Page"));
        var currentPage = Long.parseLong(response.headers.get("X-Current-Page"));
        slice.totalElement(totalCnt).totalPage(totalPage).currentPage(currentPage);
        return response.json().then(json -> Promise.resolve(slice.content((T[]) json)));
    }
}