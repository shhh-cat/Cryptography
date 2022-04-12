package com.blackcat.cryptography.cipher;

public class CaesarCipher {
    private final String key = "aáàạảãăắằặẳẵâấầậẩẫbcdđeéẹẻẽêếềệểễfghiíìịỉĩjklmnoóòọỏõôốồộổỗơớờợởỡpqrstuúùụủũưứừựửữvwxyýỳỵỷỹAÁÀẠẢÃĂẮẰẶẲẴÂẤẦẬẨẪBCDĐEÉẸẺẼÊẾỀỆỂỄFGHIÍÌỊỈĨJKLMNOÓÒỌỎÕÔỐỒỘỔỖƠỚỜỢỞỠPQRSTUÚÙỤỦŨƯỨỪỰỬỮVWXYÝỲỴỶỸ0123456789`~!@#$%^&*()";
    private Integer k = null;

    public CaesarCipher(Integer k) {
        this.k = k;
    }

    public String encrypt(String plaintext) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            try {
                int j = (key.indexOf(plaintext.charAt(i)) + k) % key.length();
                result.append(key.charAt(j));
            }
            catch (ArithmeticException ignored) {
                result.append(plaintext.charAt(i));
            }
        }
        return result.toString();
    }

    public String decrypt(String ciphertext) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i++) {
            try {
                int j = (key.indexOf(ciphertext.charAt(i)) - k) % key.length();
                result.append(key.charAt(j));
            }
            catch (ArithmeticException ignored) {
                result.append(ciphertext.charAt(i));
            }
        }
        return result.toString();
    }

    public Integer getK() {
        return k;
    }

    public void setK(Integer k) {
        this.k = k;
    }
}
