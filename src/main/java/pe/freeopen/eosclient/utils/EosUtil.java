package pe.freeopen.eosclient.utils;

import pe.freeopen.eosclient.api.result.error.Error;
import pe.freeopen.eosclient.client.exception.ApiException;
import pe.freeopen.eosclient.dto.ErrorInfoDTO;

import java.util.regex.Pattern;

/**
 * EosUtil
 *
 * @Author: ChenMingHao
 * @Date: 2018-12-15 14:50
 */
public class EosUtil {

    /**
     * 检测账户名
     * @Author: ChenMingHao
     * @Date: 2018-12-15 14:53
     * @Param: name
     * @Return boolean
     */
    public static boolean checkName(String name){
        String pattern = "^[a-z\\.12345]{12}$";
        return Pattern.matches(pattern, name);
    }

    /**
     * 获取错误信息
     * @Author: ChenMingHao
     * @Date: 2018-12-15 15:56
     * @Param: ex
     * @Return pe.freeopen.eosclient.dto.ErrorInfoDTO
     */
    public static ErrorInfoDTO getErrorInfo(ApiException ex){

        ErrorInfoDTO errorInfoDTO = new ErrorInfoDTO();
        Error err = ex.getError().getError();
        errorInfoDTO.setCode(err.getCode());
        String msg = err.getWhat();
        msg+="("+err.getCode()+")";
        if(err.getDetails().length>0){
            msg+="-"+err.getDetails()[0].getMessage();
        }
        errorInfoDTO.setMessage(msg);
        return  errorInfoDTO;
    }
}
