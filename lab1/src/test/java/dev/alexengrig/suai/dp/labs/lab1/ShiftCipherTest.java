package dev.alexengrig.suai.dp.labs.lab1;

import org.junit.Assert;
import org.junit.Test;

public class ShiftCipherTest {
    private final ShiftCipher shiftCipher = new ShiftCipher();

    @Test
    public void check_encrypt() {
        String encrypt = shiftCipher.encrypt("пример шифрования", 2, "ключ");
        Assert.assertEquals("ймгжчм фгрмикэзгь", encrypt);
    }

    @Test
    public void check_decrypt() {
        String decrypt = shiftCipher.decrypt("ймгжчм фгрмикэзгь", 2, "ключ");
        Assert.assertEquals("пример шифрования", decrypt);
    }

}