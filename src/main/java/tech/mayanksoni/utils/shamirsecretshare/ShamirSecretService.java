package tech.mayanksoni.utils.shamirsecretshare;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
@Component
public class ShamirSecretService {

    private final SecureRandom random = new SecureRandom();

    public List<Share> splitSecret(String secret, int numShares, int threshold) {
        BigInteger secretInt = new BigInteger(secret.getBytes());
        BigInteger[] coefficients = new BigInteger[threshold - 1];

        // Generate random coefficients for the polynomial
        for (int i = 0; i < threshold - 1; i++) {
            coefficients[i] = new BigInteger(secretInt.bitLength(), random);
        }

        List<Share> shares = new ArrayList<>();
        for (int i = 1; i <= numShares; i++) {
            BigInteger x = BigInteger.valueOf(i);
            BigInteger y = secretInt;

            // Calculate the y value for the share
            for (int j = 0; j < threshold - 1; j++) {
                y = y.add(coefficients[j].multiply(x.pow(j + 1)));
            }

            shares.add(new Share(x, y));
        }

        return shares;
    }

    public static class Share {
        private final BigInteger x;
        private final BigInteger y;

        public Share(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }

        public BigInteger getX() {
            return x;
        }

        public BigInteger getY() {
            return y;
        }
    }
}