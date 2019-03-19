package pe.freeopen.eosclient.enums;

public enum EosErrorCodeEnum {

    Expired_Transaction("3040005","交易过期，过期时间可以设置长一点"),
    ;
    private String code;
    private String msg;

    EosErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode(){
        return code;
    }
}
