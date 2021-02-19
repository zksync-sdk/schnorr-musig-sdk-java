package io.zksync.sdk.musig.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.zksync.sdk.musig.SchnorrMusigStruct;

public final class Bytes {
    public static <T extends SchnorrMusigStruct> byte[] joinStructData(Collection<T> structs) {
        if (structs.size() > 0) {
            byte[] result = new byte[structs.size() * structs.iterator().next().getDataSize()];
            int offset = 0;
            for (T t : structs) {
                byte[] data = t.getData();
                System.arraycopy(t.getData(), 0, result, offset, data.length);
                offset += data.length;
            }
            return result;
        } else {
            return new byte[0];
        }
    }

    public static byte[] join(List<byte[]> data) {
        if (data.size() > 0) {
            int size = 0;
            for (byte[] b : data) {
                size += b.length;
            }
            byte[] result = new byte[size];
            int offset = 0;
            for (byte[] t : data) {
                System.arraycopy(t, 0, result, offset, t.length);
                offset += t.length;
            }
            return result;
        } else {
            return new byte[0];
        }
    }

    public static byte[] join(byte[][] data) {
        if (data.length > 0) {
            int size = 0;
            for (byte[] b : data) {
                size += b.length;
            }
            byte[] result = new byte[size];
            int offset = 0;
            for (byte[] t : data) {
                System.arraycopy(t, 0, result, offset, t.length);
                offset += t.length;
            }
            return result;
        } else {
            return new byte[0];
        }
    }

    public static <T extends SchnorrMusigStruct> boolean verify(List<T> data) {
        if (data.size() == 0) {
            return false;
        }

        byte[] bytes = data.get(0).getData();

        for (T t : data) {
            if (!Arrays.equals(bytes, t.getData())) {
                return false;
            }
        }

        return true;
    }
}
