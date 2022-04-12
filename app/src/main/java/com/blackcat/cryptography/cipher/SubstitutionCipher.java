package com.blackcat.cryptography.cipher;

public class SubstitutionCipher {
    private String key;

    public SubstitutionCipher(String key) {
        this.key = key;
    }

    public String encrypt(String plaintext)
    {
        String ciphertext = "";
        String[] keySpace = new String[key.length()];
        for(int i = 0; i < key.length(); i++) keySpace[i] = String.valueOf(key.charAt(i));

        for(int i = 0; i < plaintext.length(); i++)
        {
            int index = plaintext.charAt(i) - 65;
            if( index > keySpace.length || index < 0)
            {
                ciphertext += String.valueOf(plaintext.charAt(i));
            }
            else
            {
                ciphertext += keySpace[index];
            }
        }

        return ciphertext;
    }

    public String decrypt(String ciphertext)
    {
        String plaintext = "";
        for(int i = 0; i < ciphertext.length(); i++)
        {
            char character = ciphertext.charAt(i);
            int index = key.indexOf(character);
            int ascii = index + 65;
            if( ascii < 65 || ascii > 90)
            {
                plaintext += String.valueOf(character);
            }
            else
            {
                plaintext += String.valueOf((char)ascii);
            }
        }

        return plaintext;
    }
}
