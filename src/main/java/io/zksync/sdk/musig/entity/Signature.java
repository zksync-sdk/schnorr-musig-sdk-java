package io.zksync.sdk.musig.entity;

import com.sun.jna.Structure;

import io.zksync.sdk.musig.utils.Constants;
import io.zksync.sdk.musig.SchnorrMusigStruct;

public class Signature extends SchnorrMusigStruct {

    public static class ByReference extends Signature implements Structure.ByReference { }

    private Signature() {
        super(new byte[Constants.STANDARD_ENCODING_LENGTH]);
    }

    @Override
    public int getDataSize() {
        return Constants.STANDARD_ENCODING_LENGTH;
    }

}
