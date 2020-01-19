package dev.alexengrig.suai.dp.labs.work1;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

import static java.math.BigInteger.ONE;

public class RSA {
    public static final SecureRandom RANDOM = new SecureRandom();

    private final BigInteger publicKey;
    private final BigInteger privateKey;
    private final BigInteger modulus;

    public RSA(int bitLength) {
        this(new BigInteger("65537"), bitLength); // 2 ^ 16 + 1
    }

    public RSA(BigInteger key, int bitLength) {
        publicKey = key; // e
        BigInteger p, q, phi;
        do {
            p = BigInteger.probablePrime(bitLength / 2, RANDOM);
            q = BigInteger.probablePrime(bitLength / 2, RANDOM);
            phi = p.subtract(ONE).multiply(q.subtract(ONE)); // f(n) = (p - 1) * (q - 1)
        } while (!publicKey.gcd(phi).equals(ONE) || p.equals(q)); // p != q and gcd(e, phi) = 1
        privateKey = publicKey.modInverse(phi); // d = e ^ -1 mod f(n)
        modulus = p.multiply(q); // n = p * q
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input bit length:");
        int bitLength = Integer.parseInt(scanner.nextLine());
        String text = scanner.nextLine();
        RSA rsa = new RSA(bitLength);
        System.out.println(rsa);
        byte[] bytes = text.getBytes();
        BigInteger message = new BigInteger(bytes);
        BigInteger encrypt = rsa.encrypt(message);
        BigInteger decrypt = rsa.decrypt(encrypt);
        System.out.println(" Original message: " + message);
        System.out.println("Encrypted message: " + encrypt);
        System.out.println("Decrypted message: " + decrypt);
    }

    public BigInteger encrypt(BigInteger message) {
        if (message.compareTo(modulus) >= 0) {
            String info = String.format("Message '%s' is greater than or equal to modulus '%s'.", message, modulus);
            throw new IllegalArgumentException(info);
        }
        return message.modPow(publicKey, modulus); // c = m ^ e mod n
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(privateKey, modulus); // c = m ^ d mod n
    }

    @Override
    public String toString() {
        return "RSA{\n" +
                "\tpublicKey=" + publicKey + ",\n" +
                "\tprivateKey=" + privateKey + ",\n" +
                "\tmodulus=" + modulus + "\n" +
                "}";
    }
}
