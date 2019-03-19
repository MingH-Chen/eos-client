package pe.freeopen.eosclient.utils.eoskey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * EosKey工具
 *
 * @Author: ChenMingHao
 * @Date: 2018-12-04 17:27
 */
public class EosKeyUtils {
    static{
        Security.addProvider(new BouncyCastleProvider());
    }
    /**
     *  根据私钥获取公钥
     * @Author: ChenMingHao
     * @Date: 2018-12-04 17:29
     * @Param: privateKey
     * @Return java.lang.String
     */
    public static String getPublicKey(String privateKey) {

        try {
            EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKey);
            EosPublicKey eosPublicKeyFromPrivateKey = eosPrivateKey.getPublicKey();
            return eosPublicKeyFromPrivateKey.toEosString();
        } catch (EosKeyException ex) {
            return null;
        }
    }
}
