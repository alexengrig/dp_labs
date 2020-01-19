package dev.alexengrig.suai.dp.labs.lab1;

import java.util.stream.Collectors;

public class ShiftCipher {

    private final String alphabet;

    public ShiftCipher() {
        this("абвгдеёжзийклмнопрстуфхцчшщъыьэюя");
    }

    public ShiftCipher(String alphabet) {
        this.alphabet = alphabet.toLowerCase();
    }

    public static void main(String[] args) {

    }

    public String encrypt(String message, int key, String keyword) {
        StringBuilder builder = new StringBuilder();
        String alphabet = shiftAlphabet(key, keyword);
        for (char ch : message.toCharArray()) {
            if (this.alphabet.indexOf(ch) > -1) {
                builder.append(alphabet.charAt(this.alphabet.indexOf(ch)));
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    public String decrypt(String message, int key, String keyword) {
        StringBuilder builder = new StringBuilder();
        String alphabet = shiftAlphabet(key, keyword);
        for (char ch : message.toCharArray()) {
            if (alphabet.indexOf(ch) > -1) {
                builder.append(this.alphabet.charAt(alphabet.indexOf(ch)));
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    private String shiftAlphabet(int key, String keyword) {
        StringBuilder builder = new StringBuilder();
        String word = distinctWord(keyword.toLowerCase());
        String alphabet = distinctAlphabet(word);
        if (key > 0) {
            builder
                    .append(alphabet, alphabet.length() - key, alphabet.length())
                    .append(word)
                    .append(alphabet, 0, alphabet.length() - key);
        } else {
            builder
                    .append(word)
                    .append(alphabet);
        }
        return builder.toString();
    }

    private String distinctWord(String word) {
        return word.chars()
                .distinct()
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());
    }

    private String distinctAlphabet(String word) {
        return alphabet.chars()
                .mapToObj(i -> String.valueOf((char) i))
                .filter(string -> !word.contains(string))
                .collect(Collectors.joining());
    }
}
