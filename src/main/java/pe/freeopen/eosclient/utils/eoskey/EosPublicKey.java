package pe.freeopen.eosclient.utils.eoskey;

import org.bitcoinj.core.Base58;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
/**
 * EosPublicKey
 *
 * @Author: ChenMingHao
 * @Date: 2018-12-04 16:59
 */
public class EosPublicKey { private static ECNamedCurveParameterSpec params = ECNamedCurveTable.getParameterSpec("secp256k1");

    private ECPublicKey ecPublicKey;

    public EosPublicKey(byte[] publicKeyBytes) throws EosKeyException {

        createECPublicKey(publicKeyBytes);
    }

    public EosPublicKey(String eosPublicKey) throws EosKeyException {

        try {
            // Remove EOS prefix
            String publicKeyString = eosPublicKey.substring(3);

            byte[] publicKeyWithChecksum = Base58.decode(publicKeyString);

            byte[] publicKeyBytes = Arrays.copyOfRange(publicKeyWithChecksum, 0, publicKeyWithChecksum.length - 4);
            byte[] publicKeyChecksum = Arrays.copyOfRange(publicKeyWithChecksum, publicKeyWithChecksum.length - 4, publicKeyWithChecksum.length);

            MessageDigest messageDigest = MessageDigest.getInstance("RIPEMD160");
            byte[] calculatedKeyChecksumBytes = Arrays.copyOfRange(messageDigest.digest(publicKeyBytes), 0, 4);

            if (!Arrays.equals(calculatedKeyChecksumBytes, publicKeyChecksum)) {
                throw new IllegalArgumentException("Checksum in key and calculated one not match");
            }

            createECPublicKey(publicKeyBytes);

        }catch (NoSuchAlgorithmException e){
            throw new EosKeyException("Missing RIPEMD160 algorithm when getting MessageDigest instance. Check is Bouncing Castle is in dependencies and is it set as security provider \"Security.addProvider(new BouncyCastleProvider())\"");
        }

    }

    public String toEosString() throws EosKeyException {
        try {
            byte[] publicKeyBytes = ecPublicKey.getQ().getEncoded(true);

            MessageDigest messageDigest = MessageDigest.getInstance("RIPEMD160");
            byte[] publicKeyChecksum = Arrays.copyOfRange(messageDigest.digest(publicKeyBytes), 0, 4);

            byte[] publicKeyWithChecksum = ByteUtils.concatenate(publicKeyBytes, publicKeyChecksum);

            return "EOS" + Base58.encode(publicKeyWithChecksum);
        }catch (NoSuchAlgorithmException e){
            throw new EosKeyException("Missing RIPEMD160 algorithm when getting MessageDigest instance. Check is Bouncing Castle is in dependencies and is it set as security provider \"Security.addProvider(new BouncyCastleProvider())\"");
        }
    }

    public boolean verifySignature(String data, byte[] signatureBytes) throws EosKeyException {
        return verifySignature(data.getBytes(), signatureBytes);
    }

    private boolean verifySignature(byte[] dataBytes, byte[] signatureBytes) throws EosKeyException {
        try {
            Signature signature = Signature.getInstance("ECDsA", "BC");
            signature.initVerify(ecPublicKey);
            signature.update(dataBytes);
            return signature.verify(signatureBytes);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new EosKeyException("Missing algorithm when getting KeyFactory instance. Check is Bouncing Castle is in dependencies and is it set as security provider \"Security.addProvider(new BouncyCastleProvider())\"");
        } catch (SignatureException e) {
            throw new EosKeyException("Error verifying signature", e);
        } catch (InvalidKeyException e) {
            throw new EosKeyException("Error initializing signature with public key", e);
        }
    }

    private void createECPublicKey(byte[] publicKeyBytes) throws EosKeyException {
        try {

            KeyFactory keyFactory = KeyFactory.getInstance("ECDsA", "BC");

            ECPoint point = params.getCurve().decodePoint(publicKeyBytes);
            ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, params);
            ecPublicKey = (ECPublicKey) keyFactory.generatePublic(pubSpec);

        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new EosKeyException("Missing algorithm when getting KeyFactory instance. Check is Bouncing Castle is in dependencies and is it set as security provider \"Security.addProvider(new BouncyCastleProvider())\"");
        } catch (InvalidKeySpecException e) {
            throw new EosKeyException("Error generating key", e);
        }
    }
}
