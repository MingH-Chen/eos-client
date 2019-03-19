package pe.freeopen.eosclient.api.request;

import lombok.Data;

/**
 * Eosio.Code权限实体
 *
 * @Author: ChenMingHao
 * @Date: 2018-12-04 17:47
 */
@Data
public class EosCodeAuth {

    boolean auth;

    String contractAccount;

    String actor;
}
