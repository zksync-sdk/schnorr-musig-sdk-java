package io.zksync.sdk.musig;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

import io.zksync.sdk.musig.entity.AggregatedCommitment;
import io.zksync.sdk.musig.entity.AggregatedPublicKey;
import io.zksync.sdk.musig.entity.AggregatedSignature;
import io.zksync.sdk.musig.entity.Commitment;
import io.zksync.sdk.musig.entity.Precommitment;
import io.zksync.sdk.musig.entity.Signature;

interface SchnorrMusigNative extends Library {
    Pointer schnorr_musig_new_signer(
        byte[] encoded_pubkeys,
        long encoded_pubkeys_len,
        long position
    );

    void schnorr_musig_delete_signer(Pointer signer);

    int schnorr_musig_aggregate_pubkeys(
        byte[] encoded_pubkeys,
        long encoded_pubkeys_len,
        AggregatedPublicKey.ByReference aggregate_pubkeys
    );

    int schnorr_musig_compute_precommitment(
        Pointer signer,
        int[] seed,
        long seed_len,
        Precommitment.ByReference precommitment
    );

    int schnorr_musig_receive_commitments(
        Pointer signer,
        byte[] input,
        long input_len,
        AggregatedCommitment.ByReference aggregated_commitment
    );

    int schnorr_musig_receive_precommitments(
        Pointer signer,
        byte[] input,
        long input_len,
        Commitment.ByReference commitment
    );

    int schnorr_musig_receive_signature_shares(
        Pointer signer,
        byte[] input,
        long input_len,
        AggregatedSignature.ByReference signature_shares
    );

    int schnorr_musig_sign(
        Pointer signer,
        byte[] private_key,
        long private_key_len,
        byte[] message,
        long message_len,
        Signature.ByReference signature
    );

    int schnorr_musig_verify(
        byte[] message,
        long message_len,
        byte[] encoded_pubkeys,
        long encoded_pubkeys_len,
        byte[] encoded_signature,
        long encoded_signature_len
    );

}
