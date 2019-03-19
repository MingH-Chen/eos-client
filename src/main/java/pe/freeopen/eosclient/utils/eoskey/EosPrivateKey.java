package pe.freeopen.eosclient.utils.eoskey;

import org.bitcoinj.core.Base58;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
/**
 * EosPrivateKey
 *
 * @Author: ChenMingHao
 * @Date: 2018-12-04 16:50
 */
public class EosPrivateKey {
    private static ECNamedCurveParameterSpec params = ECNamedCurveTable.getParameterSpec("secp256k1");

    private ECPrivateKey ecPrivateKey;

    public EosPrivateKey(String keyInWalletImportFormat) throws EosKeyException {
        BigInteger keyBigInteger = WalletImportFormat.toBigInteger(keyInWalletImportFormat);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("ECDsA", "BC");

            ECCurve curve = params.getCurve();
            EllipticCurve ellipticCurve = EC5Util.convertCurve(curve, params.getSeed());
            ECParameterSpec params2 = EC5Util.convertSpec(ellipticCurve, params);
            ECPrivateKeySpec keySpec = new java.security.spec.ECPrivateKeySpec(keyBigInteger, params2);

            ecPrivateKey = (ECPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new EosKeyException("Missing algorithm when getting KeyFactory instance. Check is Bouncing Castle is in dependencies and is it set as security provider \"Security.addProvider(new BouncyCastleProvider())\"");
        } catch (InvalidKeySpecException e) {
            throw new EosKeyException("Error generating key", e);
        }
    }

    public EosPublicKey getPublicKey() throws EosKeyException {
        ECPoint Q = params.getG().multiply(ecPrivateKey.getD());
        byte[] publicBytes = Q.getEncoded(false);

        return new EosPublicKey(publicBytes);
    }

    public String toWif() throws EosKeyException {
        try {
            byte[] privateKeyBytes = ecPrivateKey.getD().toByteArray();

            if (privateKeyBytes[0] == 0) {
                privateKeyBytes = Arrays.copyOfRange(privateKeyBytes, 1, privateKeyBytes.length);
            }

            byte[] prefix = new byte[]{(byte) 128};
            byte[] privateKeyWithPrefixBytes = ByteUtils.concatenate(prefix, privateKeyBytes);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(privateKeyWithPrefixBytes);
            byte[] calculatedChecksumBytes = Arrays.copyOfRange(messageDigest.digest(messageDigest.digest()), 0, 4);

            return Base58.encode(ByteUtils.concatenate(privateKeyWithPrefixBytes, calculatedChecksumBytes));
        } catch (NoSuchAlgorithmException e) {
            throw new EosKeyException("Missing SHA-256 algorithm when getting MessageDigest instance. Should be available in by default in Java");
        }
    }

    public byte[] sign(String data) throws EosKeyException {
        return sign(data.getBytes());
    }

    public byte[] sign(byte[] dataBytes) throws EosKeyException {

        try {
            Signature signature = Signature.getInstance("ECDsA", "BC");
            signature.initSign(ecPrivateKey, new SecureRandom());
            signature.update(dataBytes);
            byte[] signatureBytes = signature.sign();

            return signatureBytes;
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new EosKeyException("Missing algorithm when getting Signature instance. Check is Bouncing Castle is in dependencies and is it set as security provider \"Security.addProvider(new BouncyCastleProvider())\"");
        } catch (SignatureException e) {
            throw new EosKeyException("Error updating or creating signature", e);
        } catch (InvalidKeyException e) {
            throw new EosKeyException("Error initializing signature with private key", e);
        }
    }
}
