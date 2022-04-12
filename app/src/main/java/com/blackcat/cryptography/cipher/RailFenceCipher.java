package com.blackcat.cryptography.cipher;

public class RailFenceCipher {
    private int key;

    public RailFenceCipher(int key) {
        this.key = key;
    }

    private int getTerm(int iteration, int row, int size) {
        if ((size == 0) || (size == 1)) {
            return 1;
        }
        if((row == 0) || (row == size-1)) { // Max. distance is achieved at the ends and equally (size-1)*2
            return (size-1)*2;
        }

        if (iteration % 2 == 0) { // In the description of the method above this identity is demonstrated
            return (size-1-row)*2;
        }
        return 2*row;
    }

    public String encrypt(String message) {
        if (key < 0) {
            throw new ArithmeticException("Negative key value");
        } else if (key == 0) {
            key = 1;
        }
        StringBuilder encodedMessage = new StringBuilder();

        for(int row = 0; row < key; row++) { // Look rows
            int iter = 0; // The number of the character in the row
            for(int i = row; i < message.length(); i += getTerm(iter++, row, key)) {
                // i - the number of row character in source line

                encodedMessage.append(message.charAt(i)); // "Add characters line by row"
            }


        }
        return encodedMessage.toString();
    }

    public String decrypt(String message) {
        if (key < 0) {
            throw new ArithmeticException("Negative key value");
        }
        StringBuilder decodedMessage = new StringBuilder(message);
        int currPosition = 0; // Position in source string
        for(int row = 0; row < key; row++) { // Look rows
            int iter = 0; // The number of the character in the row
            for(int i = row; i < message.length(); i += getTerm(iter++, row, key)) {
                decodedMessage.setCharAt(i, message.charAt(currPosition++));
            }


        }

        return decodedMessage.toString();
    }

}
