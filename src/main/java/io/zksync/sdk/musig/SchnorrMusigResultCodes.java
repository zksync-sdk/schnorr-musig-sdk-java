package io.zksync.sdk.musig;

public enum SchnorrMusigResultCodes {
    OK(0),
    INVALID_INPUT_DATA(1),
    ENCODING_ERROR(2),
    SIGNATURE_VERIFICATION_FAILED(3),
    INTERNAL_ERROR(4),
    INVALID_PUBKEY_LENGTH(100),
    NONCE_COMMITMENT_NOT_GENERATED(101),
    NONCE_PRECOMMITMENTS_NOT_RECEIVED(102),
    NONCE_PRECOMMITMENTS_AND_PARTICIPANTS_NOT_MATCH(103),
    NONCE_COMMITMENTS_NOT_RECEIVED(104),
    NONCE_COMMITMENTS_AND_PARTICIPANTS_NOT_MATCH(105),
    SIGNATURE_SHARE_AND_PARTICIPANTS_NOT_MATCH(106),
    COMMITMENT_IS_NOT_IN_CORRECT_SUBGROUP(107),
    INVALID_COMMITMENT(108),
    INVALID_PUBLIC_KEY(109),
    INVALID_PARTICIPANT_POSITION(110),
    AGGREGATED_NONCE_COMMITMENT_NOT_COMPUTED(111),
    CHALLENGE_NOT_GENERATED(112),
    INVALID_SIGNATURE_SHARE(113),
    INVALID_SEED(114);

    private final int code;

    private SchnorrMusigResultCodes(int code) {
        this.code = code;
    }

    public static SchnorrMusigResultCodes byCode(int code) {
        for (SchnorrMusigResultCodes err : SchnorrMusigResultCodes.values()) {
            if (err.code == code) {
                return err;
            }
        }
        throw new IllegalArgumentException("Invalid code");
    }
}
