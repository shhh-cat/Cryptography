package com.blackcat.cryptography.cipher;

public class VigenereCipher {

    public VigenereCipher setKey(String key) {
        this.key = key;
        return this;
    }

    private String key;

    public VigenereCipher() {
    }

    private static String LowerToUpper(String s)
    {
        StringBuilder str =new StringBuilder(s);
        for(int i = 0; i < s.length(); i++)
        {
            if(Character.isLowerCase(s.charAt(i)))
            {
                str.setCharAt(i, Character.toUpperCase(s.charAt(i)));
            }
        }
        s = str.toString();
        return s;
    }

    public static String generateKey(String str, String key)
    {
        str = LowerToUpper(str);
        key = LowerToUpper(key);
        int x = str.length();
        StringBuilder r = new StringBuilder();

        int i = 0;
        int j = 0;
        while (i < x) {
            r.append(key.charAt(j));
            j++;
            if (j >= key.length()) j = 0;
            i++;
        }
        return r.toString();
    }

    public String encrypt(String str)
    {
        String cipher_text="";

        for (int i = 0; i < str.length(); i++)
        {
            // converting in range 0-25
            int x = (str.charAt(i) + key.charAt(i)) %26;

            // convert into alphabets(ASCII)
            x += 'A';

            cipher_text+=(char)(x);
        }
        return cipher_text;
    }

    public String decrypt(String cipher_text)
    {
        String orig_text="";

        for (int i = 0 ; i < cipher_text.length() &&
                i < key.length(); i++)
        {
            // converting in range 0-25
            int x = (cipher_text.charAt(i) -
                    key.charAt(i) + 26) %26;

            // convert into alphabets(ASCII)
            x += 'A';
            orig_text+=(char)(x);
        }
        return orig_text;
    }
}
