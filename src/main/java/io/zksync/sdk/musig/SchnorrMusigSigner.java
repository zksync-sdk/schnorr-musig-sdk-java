package io.zksync.sdk.musig;

import java.util.Arrays;
import java.util.Collection;

import io.zksync.sdk.musig.entity.AggregatedCommitment;
import io.zksync.sdk.musig.entity.AggregatedPublicKey;
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

    SchnorrMusigSigner(SchnorrMusigNative musig, MusigSigner signer) {
        this.musig = musig;
        this.signer = signer;
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

    public boolean verify(byte[] message, AggregatedSignature[] signatures,
            AggregatedPublicKey[] aggregatedPublicKeys) throws SchnorrMusigException {
        return verify(message, Arrays.asList(signatures), Arrays.asList(aggregatedPublicKeys));
    }

    public boolean verify(byte[] message, Collection<AggregatedSignature> signatures,
            Collection<AggregatedPublicKey> aggregatedPublicKeys) throws SchnorrMusigException {
        byte[] encodedSignature = Bytes.joinStructData(signatures);
        byte[] encodedPubkeys = Bytes.joinStructData(aggregatedPublicKeys);

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

    public boolean verify(byte[] message, AggregatedSignature[] signatures, byte[] publicKeys)
            throws SchnorrMusigException {
        return verify(message, Arrays.asList(signatures), publicKeys);
    }

    public boolean verify(byte[] message, Collection<AggregatedSignature> signatures, byte[] publicKeys)
            throws SchnorrMusigException {
        byte[] encodedSignature = Bytes.joinStructData(signatures);

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
