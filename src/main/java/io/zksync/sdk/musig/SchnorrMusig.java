package io.zksync.sdk.musig;

import java.io.IOException;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;

import org.scijava.nativelib.BaseJniExtractor;
import org.scijava.nativelib.NativeLoader;

import io.zksync.sdk.musig.entity.MusigSigner;
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

}
