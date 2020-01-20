package dev.alexengrig.suai.dp.labs.work1;

import dev.alexengrig.suai.dp.labs.work1.RSA.RSAParameters.PrivateRSAParameters;
import dev.alexengrig.suai.dp.labs.work1.RSA.RSAParameters.PublicRSAParameters;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

import static java.math.BigInteger.ONE;

public class RSA {
    public static final SecureRandom RANDOM = new SecureRandom();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input bit length:");
        int bitLength = Integer.parseInt(scanner.nextLine());
        RSAParameters parameters = new RSAParameters(bitLength);
        System.out.println(parameters);
        System.out.println("Input message:");
        String text = scanner.nextLine();
        byte[] bytes = text.getBytes();
        BigInteger message = new BigInteger(bytes);
        System.out.println(" Original message: " + message);
        PublicRSAParameters publicParameters = parameters.getPublicParameters();
        RSAEncoder encoder = new RSAEncoder(publicParameters);
        BigInteger encrypt = encoder.encrypt(message);
        System.out.println("Encrypted message: " + encrypt);
        PrivateRSAParameters privateParameters = parameters.getPrivateParameters();
        RSADecoder decoder = new RSADecoder(privateParameters);
        BigInteger decrypt = decoder.decrypt(encrypt);
        System.out.println("Decrypted message: " + decrypt);
    }

    public static class RSAParameters {
        private final BigInteger publicKey;
        private final BigInteger privateKey;
        private final BigInteger modulus;

        public RSAParameters(int bitLength) {
            this(new BigInteger("65537"), bitLength); // 2 ^ 16 + 1
        }

        public RSAParameters(BigInteger key, int bitLength) {
            publicKey = key; // e
            BigInteger p, q, phi;
            do {
                p = BigInteger.probablePrime(bitLength / 2, RANDOM);
                q = BigInteger.probablePrime(bitLength / 2, RANDOM);
                phi = p.subtract(ONE).multiply(q.subtract(ONE)); // f(n) = (p - 1) * (q - 1)
            } while (!publicKey.gcd(phi).equals(ONE) || p.equals(q)); // gcd(e, phi) = 1 and p != q
            privateKey = publicKey.modInverse(phi); // d = e ^ -1 mod f(n)
            modulus = p.multiply(q); // n = p * q
        }

        public PublicRSAParameters getPublicParameters() {
            return new PublicRSAParameters(this);
        }

        public PrivateRSAParameters getPrivateParameters() {
            return new PrivateRSAParameters(this);
        }

        @Override
        public String toString() {
            return "RSAParameters {\n" +
                    "\t publicKey = " + publicKey + ",\n" +
                    "\tprivateKey = " + privateKey + ",\n" +
                    "\t   modulus = " + modulus + "\n" +
                    "}";
        }

        public static class PublicRSAParameters {
            private final BigInteger key;
            private final BigInteger modulus;

            public PublicRSAParameters(RSAParameters parameters) {
                this.key = parameters.publicKey;
                this.modulus = parameters.modulus;
            }
        }

        public static class PrivateRSAParameters {
            private final BigInteger key;
            private final BigInteger modulus;

            public PrivateRSAParameters(RSAParameters parameters) {
                this.key = parameters.privateKey;
                this.modulus = parameters.modulus;
            }
        }
    }

    public static class RSAEncoder {
        private final BigInteger key;
        private final BigInteger modulus;

        public RSAEncoder(PublicRSAParameters parameters) {
            this.key = parameters.key;
            this.modulus = parameters.modulus;
        }

        public BigInteger encrypt(BigInteger message) {
            if (message.compareTo(modulus) >= 0) {
                String info = String.format("Message '%s' is greater than or equal to modulus '%s'.", message, modulus);
                throw new IllegalArgumentException(info);
            }
            return message.modPow(key, modulus); // c = m ^ e mod n
        }
    }

    public static class RSADecoder {
        private final BigInteger key;
        private final BigInteger modulus;

        public RSADecoder(PrivateRSAParameters parameters) {
            this.key = parameters.key;
            this.modulus = parameters.modulus;
        }

        public BigInteger decrypt(BigInteger ciphertext) {
            return ciphertext.modPow(key, modulus); // c = m ^ d mod n
        }
    }
}
