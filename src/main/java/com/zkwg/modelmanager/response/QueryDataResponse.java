package com.zkwg.modelmanager.response;

public class QueryDataResponse<QueryDdata> extends CommonResponse<QueryDdata> {


    public QueryDataResponse(QueryDdata data) {
        super(data);
    }

    public QueryDataResponse(int code, String message, QueryDdata data) {
        super(code, message, data);
    }
}
