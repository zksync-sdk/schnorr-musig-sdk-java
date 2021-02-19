package io.zksync.sdk.musig.entity;

import com.sun.jna.Structure;

import io.zksync.sdk.musig.utils.Constants;
import io.zksync.sdk.musig.SchnorrMusigStruct;

public class AggregatedSignature extends SchnorrMusigStruct {

    public static class ByReference extends AggregatedSignature implements Structure.ByReference { }

    private AggregatedSignature() {
        super(new byte[Constants.AGG_SIG_ENCODING_LENGTH]);
    }

    @Override
    public int getDataSize() {
        return Constants.AGG_SIG_ENCODING_LENGTH;
    }

}
