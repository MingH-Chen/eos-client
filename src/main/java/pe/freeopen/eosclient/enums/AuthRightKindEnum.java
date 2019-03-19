package pe.freeopen.eosclient.enums;

public enum AuthRightKindEnum {

    OWNER("owner"),
    ACTIVE("active"),
    ;
    private String code;

    AuthRightKindEnum(String code) {
        this.code = code;
    }


    public String getCode(){
        return code;
    }
}
