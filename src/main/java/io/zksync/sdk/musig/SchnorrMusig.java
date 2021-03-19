package io.zksync.sdk.musig;

import java.io.IOException;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

import org.scijava.nativelib.BaseJniExtractor;
import org.scijava.nativelib.NativeLoader;

import io.zksync.sdk.musig.entity.AggregatedPublicKey;
import io.zksync.sdk.musig.entity.AggregatedSignature;
import io.zksync.sdk.musig.entity.MusigSigner;
import io.zksync.sdk.musig.exception.SchnorrMusigException;
import io.zksync.sdk.musig.utils.Bytes;

public class SchnorrMusig {
    private static final String LIBRARY_NAME = "musig_c";

    private SchnorrMusigNative musig;

    static {
        try {
            NativeLoader.loadLibrary(LIBRARY_NAME);
            NativeLibrary.addSearchPath(LIBRARY_NAME, ((BaseJniExtractor) NativeLoader.getJniExtractor()).getJniDir().getAbsolutePath());
            NativeLibrary.addSearchPath(LIBRARY_NAME, ((BaseJniExtractor) NativeLoader.getJniExtractor()).getNativeDir().getAbsolutePath());
        } catch (SecurityException e) {
            throw new IllegalStateException("Cannot load native library", e);
        } catch (IOException ignored) {}
    }

    /**
     * Load and initialize Schnorr Musig native library
     *
     * @return SchnorrMusig library instance
     */
    public static SchnorrMusig load() {
        SchnorrMusig musig = new SchnorrMusig();
        musig.musig = Native.load(LIBRARY_NAME, SchnorrMusigNative.class);
        return musig;
    }

    /**
     * Create signer instance with given public key list and position
     * 
     * @param publicKeys - list of public keys
     * @param position - position of public key after encoding
     * @return Instance of SchnorrMusigSigner
     */
    public SchnorrMusigSigner createSigner(List<byte[]> publicKeys, int position) {
        byte[] encodedPublicKeys = Bytes.join(publicKeys);
        Pointer signer = this.musig.schnorr_musig_new_signer(encodedPublicKeys, encodedPublicKeys.length, position);
        return new SchnorrMusigSigner(this.musig, new MusigSigner(signer));
    }

    /**
     * Create signer instance with given encoded public keys and position
     * 
     * @param encodedPublicKeys - encoded list of public keys {@see Bytes.join}
     * @param position - position of public key after encoding
     * @return Instance of SchnorrMusigSigner
     */
    public SchnorrMusigSigner createSigner(byte[] encodedPublicKeys, int position) {
        Pointer signer = this.musig.schnorr_musig_new_signer(encodedPublicKeys, encodedPublicKeys.length, position);
        return new SchnorrMusigSigner(this.musig, new MusigSigner(signer));
    }

    /**
     * Create signer instance with given public key
     * 
     * @param publicKey - signle public key
     * @return Instance of SchnorrMusigSigner
     */
    public SchnorrMusigSigner createSigner(byte[] publicKey) {
        Pointer signer = this.musig.schnorr_musig_new_signer(publicKey, publicKey.length, 0);
        return new SchnorrMusigSigner(this.musig, new MusigSigner(signer));
    }

    public boolean verify(byte[] message, AggregatedSignature signatures, AggregatedPublicKey aggregatedPublicKeys) throws SchnorrMusigException {
        byte[] encodedSignature = signatures.getData();
        byte[] encodedPubkeys = aggregatedPublicKeys.getData();

        int code = this.musig.schnorr_musig_verify(message, message.length, encodedPubkeys, encodedPubkeys.length,
                encodedSignature, encodedSignature.length);

        SchnorrMusigResultCodes result = SchnorrMusigResultCodes.byCode(code);

        if (result == SchnorrMusigResultCodes.OK) {
            return true;
        } else if (result == SchnorrMusigResultCodes.SIGNATURE_VERIFICATION_FAILED) {
            return false;
        } else {
            throw new SchnorrMusigException(result);
        }
    }

    public boolean verify(byte[] message, AggregatedSignature signature, byte[] publicKeys)
            throws SchnorrMusigException {
        byte[] encodedSignature = signature.getData();

        int code = this.musig.schnorr_musig_verify(message, message.length, publicKeys, publicKeys.length,
                encodedSignature, encodedSignature.length);

        SchnorrMusigResultCodes result = SchnorrMusigResultCodes.byCode(code);

        if (result == SchnorrMusigResultCodes.OK) {
            return true;
        } else if (result == SchnorrMusigResultCodes.SIGNATURE_VERIFICATION_FAILED) {
            return false;
        } else {
            throw new SchnorrMusigException(result);
        }
    }

    public AggregatedPublicKey aggregatePublicKeys(byte[] publicKeys) throws SchnorrMusigException {
        AggregatedPublicKey.ByReference aggregatedPublicKey = new AggregatedPublicKey.ByReference();

        int code = this.musig.schnorr_musig_aggregate_pubkeys(publicKeys, publicKeys.length, aggregatedPublicKey);

        SchnorrMusigResultCodes result = SchnorrMusigResultCodes.byCode(code);

        if (result == SchnorrMusigResultCodes.OK) {
            return aggregatedPublicKey;
        } else {
            throw new SchnorrMusigException(result);
        }
    }

}
