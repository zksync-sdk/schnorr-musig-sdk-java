package io.zksync.sdk.musig.entity;

import com.sun.jna.Pointer;

public class MusigSigner {

    private final Pointer pointer;

    public MusigSigner(Pointer pointer) {
        this.pointer = pointer;
    }

    public Pointer getPointer() {
        return pointer;
    }

}
