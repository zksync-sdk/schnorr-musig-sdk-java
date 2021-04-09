package io.zksync.sdk.musig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.web3j.utils.Numeric;

import io.zksync.sdk.musig.entity.*;
import io.zksync.sdk.musig.exception.SchnorrMusigException;
import io.zksync.sdk.musig.utils.Bytes;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class SchnorrMusigTest {

    private static final int[] SEED = new int[] { 16807, 282475249, 1622650073, 984943658 };
    private static final byte[] MSG = "hello".getBytes(StandardCharsets.UTF_8);

    private SchnorrMusig musig;

    @BeforeEach
    public void init() {
        this.musig = SchnorrMusig.load();
    }

    @Test
    public void test() {
        assertTrue(true);
    }

    @Test
    public void testSingle() throws SchnorrMusigException {
        final String expected = "0x02bae431c052b9e4f7c9b511904a577c7ba5e035625879d5253440793337f7ff";
        byte[] privateKey = new byte[] {1, 31, 91, -103, 8, 76, 92, 46, 45, 94, 99, 72, -114, 15, 113, 104, -43, -103, -91, -64, 31, -23, -2, -60, -55, -106, 5, 116, 61, -91, -24, 92};
        byte[] publicKey = new byte[] {23, -100, 58, 89, 20, 125, 48, 49, 108, -120, 102, 40, -123, 35, 72, -55, -76, 42, 24, -72, 33, 8, 74, -55, -17, 121, -67, 115, -23, -71, 78, -115};
        SchnorrMusigSigner signer = this.musig.createSigner(publicKey, 0);
        Precommitment precommitment = signer.computePrecommitment(SEED);
        assertEquals("0x93ae6e6df739d76c088755078ed857e95119909c97bdd5cdc8aa12286abc0984", Numeric.toHexString(precommitment.getData()));
        Commitment commitment = signer.receivePrecommitments(precommitment);
        assertEquals("0xa18005f171a323d022a625e71aa53864ca6d1851a1fc50585b7627fba3f6c69f", Numeric.toHexString(commitment.getData()));
        AggregatedCommitment aggregatedCommitment = signer.receiveCommitments(commitment);
        assertEquals("0xa18005f171a323d022a625e71aa53864ca6d1851a1fc50585b7627fba3f6c69f", Numeric.toHexString(aggregatedCommitment.getData()));
        Signature signature = signer.sign(privateKey, MSG);
        final String actual = Numeric.toHexString(signature.getData());

        assertEquals(expected, actual);

        AggregatedSignature aggregateSignature = signer.aggregateSignature(signature);

        assertTrue(this.musig.verify(MSG, aggregateSignature, publicKey));
    }

    @Test
    public void testMultiple() {
        byte[][] privateKeys = new byte[][] {
            new byte[] {1,31,91,-103,8,76,92,46,45,94,99,72,-114,15,113,104,-43,-103,-91,-64,31,-23,-2,-60,-55,-106,5,116,61,-91,-24,92},
            new byte[] {5,-66,-6,29,-59,-66,-72,-86,116,-61,72,-106,111,82,84,112,43,-64,-87,97,62,81,-98,-77,-17,47,-24,-60,68,-12,13,51},
            new byte[] {3,-51,-119,71,-87,15,115,-88,117,98,53,116,-8,-32,-29,-45,-58,-85,-40,-7,54,123,-91,68,51,-19,2,-73,-90,37,51,-39},
            new byte[] {2,85,108,35,44,-5,108,-126,116,-84,126,46,85,-2,31,-121,-74,-34,-31,25,-65,98,-93,-57,-124,16,45,-26,-62,92,37,18},
            new byte[] {1,121,16,-119,-75,59,-18,104,33,71,-20,-68,94,38,50,83,41,-94,28,-119,74,98,5,-121,108,88,121,-115,28,38,-118,-28},
        };
        byte[][] publicKeys = new byte[][] {
            new byte[] {23,-100,58,89,20,125,48,49,108,-120,102,40,-123,35,72,-55,-76,42,24,-72,33,8,74,-55,-17,121,-67,115,-23,-71,78,-115},
            new byte[] {10,-12,-71,-92,-23,-30,-75,-92,-44,-48,-90,-46,-21,-102,-15,-102,-67,-99,-116,95,0,-101,80,-13,-47,95,-86,126,112,100,-10,-97},
            new byte[] {-50,-81,-40,-53,21,-95,0,-25,-83,13,-29,-41,63,125,-52,-24,-71,-29,36,60,-73,-37,-42,78,59,11,10,121,-102,-109,-77,-120},
            new byte[] {63,6,62,-21,40,-71,18,-96,89,-4,-118,-116,100,33,-20,89,-51,45,-113,42,25,64,43,9,125,120,-33,-118,56,100,-9,15},
            new byte[] {40,107,64,71,20,-37,-122,117,29,-110,92,118,-49,119,7,9,-105,-28,-120,101,-100,74,-65,116,-52,114,-102,55,17,-68,27,-92},
        };

        byte[] allPublicKeys = Bytes.join(publicKeys);

        List<SchnorrMusigSigner> signers = IntStream.range(0, publicKeys.length)
            .mapToObj(pos -> musig.createSigner(allPublicKeys, pos))
            .collect(Collectors.toList());

        List<Precommitment> precommitments = signers.stream()
            .map(signer -> signer.computePrecommitment(SEED))
            .collect(Collectors.toList());
        assertTrue(Bytes.verify(precommitments));

        List<Commitment> commitments = signers.stream()
            .map(signer -> signer.receivePrecommitments(precommitments))
            .collect(Collectors.toList());
        assertTrue(Bytes.verify(commitments));

        List<AggregatedCommitment> aggregatedCommitments = signers.stream()
            .map(signer -> signer.receiveCommitments(commitments))
            .collect(Collectors.toList());
        assertTrue(Bytes.verify(aggregatedCommitments));

        List<Signature> signatures = IntStream.range(0, signers.size())
            .mapToObj(pos -> signers.get(pos).sign(privateKeys[pos], MSG))
            .collect(Collectors.toList());

        List<AggregatedSignature> aggregatedSignatures = signers.stream()
            .map(signer -> signer.aggregateSignature(signatures))
            .collect(Collectors.toList());
        assertTrue(Bytes.verify(aggregatedSignatures));

        AggregatedPublicKey aggregatedPublicKey = this.musig.aggregatePublicKeys(allPublicKeys);

        /// Check all aggregated signatures to all public keys
        assertTrue(this.musig.verify(MSG, aggregatedSignatures.get(0), allPublicKeys));

        /// Check all aggregated signatures to aggregated public key
        assertTrue(this.musig.verify(MSG, aggregatedSignatures.get(0), aggregatedPublicKey));
     }

}
