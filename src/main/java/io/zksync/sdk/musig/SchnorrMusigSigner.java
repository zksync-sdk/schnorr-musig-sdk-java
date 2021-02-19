package io.zksync.sdk.musig;

import java.util.Arrays;
import java.util.Collection;

import io.zksync.sdk.musig.entity.AggregatedCommitment;
import io.zksync.sdk.musig.entity.AggregatedSignature;
import io.zksync.sdk.musig.entity.Commitment;
import io.zksync.sdk.musig.entity.MusigSigner;
import io.zksync.sdk.musig.entity.Precommitment;
import io.zksync.sdk.musig.entity.Signature;
import io.zksync.sdk.musig.exception.SchnorrMusigException;
import io.zksync.sdk.musig.utils.Bytes;

public class SchnorrMusigSigner {

    private final SchnorrMusigNative musig;
    private final MusigSigner signer;
    private final byte[] publicKey;

    SchnorrMusigSigner(SchnorrMusigNative musig, MusigSigner signer, byte[] publicKey) {
        this.musig = musig;
        this.signer = signer;
        this.publicKey = publicKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public Signature sign(byte[] privateKey, byte[] message) throws SchnorrMusigException {
        Signature.ByReference signature = new Signature.ByReference();

        int code = this.musig.schnorr_musig_sign(this.signer.getPointer(), privateKey, privateKey.length, message,
                message.length, signature);

        SchnorrMusigResultCodes result = SchnorrMusigResultCodes.byCode(code);

        if (result == SchnorrMusigResultCodes.OK) {
            return signature;
        } else {
            throw new SchnorrMusigException(result);
        }
    }

    public Precommitment computePrecommitment(int[] seed) throws SchnorrMusigException {
        Precommitment.ByReference precommitment = new Precommitment.ByReference();

        int code = this.musig.schnorr_musig_compute_precommitment(this.signer.getPointer(), seed, seed.length,
                precommitment);

        SchnorrMusigResultCodes result = SchnorrMusigResultCodes.byCode(code);

        if (result == SchnorrMusigResultCodes.OK) {
            return precommitment;
        } else {
            throw new SchnorrMusigException(result);
        }
    }

    public Commitment receivePrecommitments(Precommitment... precommitments) throws SchnorrMusigException {
        return receivePrecommitments(Arrays.asList(precommitments));
    }

    public Commitment receivePrecommitments(Collection<Precommitment> precommitments) throws SchnorrMusigException {
        byte[] precommitmentsData = Bytes.joinStructData(precommitments);
        Commitment.ByReference commitment = new Commitment.ByReference();

        int code = this.musig.schnorr_musig_receive_precommitments(this.signer.getPointer(), precommitmentsData,
                precommitmentsData.length, commitment);

        SchnorrMusigResultCodes result = SchnorrMusigResultCodes.byCode(code);

        if (result == SchnorrMusigResultCodes.OK) {
            return commitment;
        } else {
            throw new SchnorrMusigException(result);
        }
    }

    public AggregatedCommitment receiveCommitments(Commitment... commitments) throws SchnorrMusigException {
        return receiveCommitments(Arrays.asList(commitments));
    }

    public AggregatedCommitment receiveCommitments(Collection<Commitment> commitments) throws SchnorrMusigException {
        byte[] commitmentsData = Bytes.joinStructData(commitments);
        AggregatedCommitment.ByReference aggregatedCommitment = new AggregatedCommitment.ByReference();

        int code = this.musig.schnorr_musig_receive_commitments(this.signer.getPointer(), commitmentsData,
                commitmentsData.length, aggregatedCommitment);

        SchnorrMusigResultCodes result = SchnorrMusigResultCodes.byCode(code);

        if (result == SchnorrMusigResultCodes.OK) {
            return aggregatedCommitment;
        } else {
            throw new SchnorrMusigException(result);
        }
    }

    public AggregatedSignature aggregateSignature(Signature... signatures) throws SchnorrMusigException {
        return aggregateSignature(Arrays.asList(signatures));
    }

    public AggregatedSignature aggregateSignature(Collection<Signature> signatures) throws SchnorrMusigException {
        byte[] signaturesData = Bytes.joinStructData(signatures);
        AggregatedSignature.ByReference aggregateSignature = new AggregatedSignature.ByReference();

        int code = this.musig.schnorr_musig_receive_signature_shares(this.signer.getPointer(), signaturesData,
                signaturesData.length, aggregateSignature);

        SchnorrMusigResultCodes result = SchnorrMusigResultCodes.byCode(code);

        if (result == SchnorrMusigResultCodes.OK) {
            return aggregateSignature;
        } else {
            throw new SchnorrMusigException(result);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.musig.schnorr_musig_delete_signer(this.signer.getPointer());
        super.finalize();
    }

}
