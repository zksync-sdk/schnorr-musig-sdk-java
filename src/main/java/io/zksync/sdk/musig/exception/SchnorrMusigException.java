package io.zksync.sdk.musig.exception;

import io.zksync.sdk.musig.SchnorrMusigResultCodes;

public class SchnorrMusigException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final SchnorrMusigResultCodes code;

    public SchnorrMusigException(SchnorrMusigResultCodes code) {
        super(message(code));
        this.code = code;
    }

    public SchnorrMusigResultCodes getCode() {
        return code;
    }

    private static String message(SchnorrMusigResultCodes code) {
        switch (code) {
            case INVALID_INPUT_DATA:
                return "Invalid input length";
            case ENCODING_ERROR:
                return "Can't encode output";
            case INTERNAL_ERROR:
                return "Internal error";
            case SIGNATURE_VERIFICATION_FAILED:
                return "Failed to verify signature";
            case INVALID_PUBKEY_LENGTH:
                return "Public key length should be at least 1";
            case NONCE_COMMITMENT_NOT_GENERATED:
                return "Nonce is not generated";
            case NONCE_PRECOMMITMENTS_NOT_RECEIVED:
                return "Other parties' pre-commitments are not received yet";
            case NONCE_PRECOMMITMENTS_AND_PARTICIPANTS_NOT_MATCH:
                return "Number of pre-commitments and participants does not match";
            case NONCE_COMMITMENTS_NOT_RECEIVED:
                return "Other parties' commitment are not received yet";
            case NONCE_COMMITMENTS_AND_PARTICIPANTS_NOT_MATCH:
                return "Number of commitments and participants does not match";
            case SIGNATURE_SHARE_AND_PARTICIPANTS_NOT_MATCH:
                return "Number of signature share and participants does not match";
            case COMMITMENT_IS_NOT_IN_CORRECT_SUBGROUP:
                return "Commitment is not in a correct subgroup";
            case INVALID_COMMITMENT:
                return "Commitments does not match with hash";
            case INVALID_PUBLIC_KEY:
                return "Invalid public key";
            case INVALID_PARTICIPANT_POSITION:
                return "Position of signer does not match with number of parties";
            case AGGREGATED_NONCE_COMMITMENT_NOT_COMPUTED:
                return "Aggregated commitment is not computed";
            case CHALLENGE_NOT_GENERATED:
                return "Challenge for fiat-shamir transform is not generated";
            case INVALID_SIGNATURE_SHARE:
                return "Signature is not verified";
            case INVALID_SEED:
                return "Seed length must be 128 bytes";
            default:
                return "Unknown error";
        }
    }

}
