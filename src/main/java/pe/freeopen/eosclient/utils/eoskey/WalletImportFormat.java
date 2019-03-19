package pe.freeopen.eosclient.utils.eoskey;

import org.bitcoinj.core.Base58;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
/**
 * WalletImportFormat
 *
 * @Author: ChenMingHao
 * @Date: 2018-12-04 16:54
 */
public class WalletImportFormat {public static byte[] toBytes(String wif) throws EosKeyException {
    try {

        byte[] wifBytes = Base58.decode(wif);

        byte[] keyWithPrefixBytes = Arrays.copyOfRange(wifBytes, 0, wifBytes.length - 4);
        byte[] keyChecksumBytes = Arrays.copyOfRange(wifBytes, wifBytes.length - 4, wifBytes.length);

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(keyWithPrefixBytes);
        byte[] calculatedChecksumBytes = Arrays.copyOfRange(messageDigest.digest(messageDigest.digest()), 0, 4);

        if (!Arrays.equals(calculatedChecksumBytes, keyChecksumBytes)) {
            throw new IllegalArgumentException("Checksum in key and calculated one not match");
        }

        if (keyWithPrefixBytes[0] != (byte) 128) {
            throw new IllegalArgumentException("Key not starts with 128");
        }

        return Arrays.copyOfRange(keyWithPrefixBytes, 1, keyWithPrefixBytes.length);
    } catch (NoSuchAlgorithmException e) {
        throw new EosKeyException("Missing SHA-256 algorithm when getting MessageDigest instance. Should be available in by default in Java");
    }
}

    public static BigInteger toBigInteger(String wif) throws EosKeyException {
        byte[] keyBytes = toBytes(wif);

        BigInteger pkBigint = new BigInteger(keyBytes);
        if(pkBigint.signum()<1){
            pkBigint = new BigInteger(ByteUtils.concatenate(new byte[1], keyBytes));
        }

        return pkBigint;
    }

    public static boolean isValid(String wif) throws EosKeyException {
        try{
            toBytes(wif);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
