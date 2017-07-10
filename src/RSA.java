import java.math.BigInteger;
import java.util.Random;

public class RSA {

    private BigInteger n;
    private BigInteger e;
    private BigInteger d;

    public RSA() {
        Random random = new Random();
        BigInteger p = new BigInteger(512, 30, random);
        BigInteger q = new BigInteger(512, 30, random);
        BigInteger lambda = lcm(p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE));

        this.n = p.multiply(q);
        this.d = comprime(lambda);
        this.e = d.modInverse(lambda);
    }

    public RSA(BigInteger e, BigInteger n) {
        this.e = e;
        this.n = n;
    }

    private static BigInteger comprime(BigInteger input) {
        BigInteger candidate = BigInteger.valueOf(2);
        while (true) {
            if (input.gcd(candidate).equals(BigInteger.ONE)) {
                return candidate;
            }
            candidate = candidate.add(BigInteger.ONE);
        }
    }

    private BigInteger lcm(BigInteger input1, BigInteger input2) {
        return input1.multiply(input2).divide(input1.gcd(input2));
    }

    private BigInteger decrypt(BigInteger encryptedMessage) {
        return encryptedMessage.modPow(d, n);
    }

    public String decryptString(String encryptedMessage) {
        return new String(decrypt(new BigInteger(encryptedMessage)).toByteArray());
    }

    private BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }

    public String encryptString(String message) {
        byte[] data = message.getBytes();
        return encrypt(new BigInteger(data)).toString();
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getE() {
        return e;
    }
}
