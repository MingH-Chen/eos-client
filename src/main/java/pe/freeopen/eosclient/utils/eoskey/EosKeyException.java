package pe.freeopen.eosclient.utils.eoskey;

/**
 * EosKeyException
 *
 * @Author: ChenMingHao
 * @Date: 2018-12-04 16:55
 */
public class EosKeyException extends Exception {
    public EosKeyException(String s) {
        super(s);
    }

    public EosKeyException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
