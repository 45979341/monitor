package com.zhuzhou.framework.utils.previous;

public enum CloudResultCode {
    SUCCESS("0","成功"),

    FAIL("1","请求失败"),

    INITG("2","参数不能为空"),

    NON("4","内容不存在"),

    REPEAT("3","重复数据");

    private String code;
    private String value;


    CloudResultCode(String code, String value) {
        this.code = code;
        this.value = value;
    }


    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value )
    {
        this.value = value;
    }

}
