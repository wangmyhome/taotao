package com.taotao.common.httpclient;

public class HttpResult {

    private Integer code;
    
    private String data;

    
    public HttpResult(Integer code, String data) {
        super();
        this.code = code;
        this.data = data;
    }

    public HttpResult() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
}
