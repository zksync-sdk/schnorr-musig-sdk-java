package io.zksync.sdk.musig.entity;

import com.sun.jna.Structure;

import io.zksync.sdk.musig.utils.Constants;
import io.zksync.sdk.musig.SchnorrMusigStruct;

public class AggregatedCommitment extends SchnorrMusigStruct {

    public static class ByReference extends AggregatedCommitment implements Structure.ByReference { }

    private AggregatedCommitment() {
        super(new byte[Constants.STANDARD_ENCODING_LENGTH]);
    }

    @Override
    public int getDataSize() {
        return Constants.STANDARD_ENCODING_LENGTH;
    }

}
