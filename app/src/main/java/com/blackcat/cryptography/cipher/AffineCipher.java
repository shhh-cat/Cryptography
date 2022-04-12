package com.blackcat.cryptography.cipher;

import java.math.BigInteger;

public class AffineCipher {
    private int a,b;
    private final int module = 26;

    public AffineCipher(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public AffineCipher(String a, String b) {
        this.a = Integer.parseInt(a);
        this.b = Integer.parseInt(b);
    }

    public String encrypt(String input) {
        StringBuilder builder = new StringBuilder();
        for (int in = 0; in < input.length(); in++) {
            char character = input.charAt(in);
            if (Character.isLetter(character)) {
                character = (char) ((a * (character - 'a') + b) % module + 'a');
            }
            builder.append(character);
        }
        return builder.toString();
    }

    public String decrypt(String input) {
        StringBuilder builder = new StringBuilder();
        // compute firstKey^-1 aka "modular inverse"
        BigInteger inverse = BigInteger.valueOf(a).modInverse(BigInteger.valueOf(module));
        // perform actual decryption
        for (int in = 0; in < input.length(); in++) {
            char character = input.charAt(in);
            if (Character.isLetter(character)) {
                int decoded = inverse.intValue() * (character - 'a' - b + module);
                character = (char) (decoded % module + 'a');
            }
            builder.append(character);
        }
        return builder.toString();
    }
}
