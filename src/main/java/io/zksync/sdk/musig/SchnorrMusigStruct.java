package io.zksync.sdk.musig;

import com.sun.jna.Structure;

import java.util.Collections;
import java.util.List;

public abstract class SchnorrMusigStruct extends Structure {

    protected SchnorrMusigStruct(byte[] data) {
        this.data = data;
    }

    public byte[] data;

    public byte[] getData() {
        return data;
    }

    public abstract int getDataSize();

    @Override
    protected List<String> getFieldOrder() {
        return Collections.singletonList("data");
    }

}
