package org.bert.carehelper;

import org.junit.Test;

import static org.bert.carehelper.common.RSAtUtils.decrypt;
import static org.bert.carehelper.common.RSAtUtils.encrypt;
import static org.bert.carehelper.common.RSAtUtils.genKeyPair;

import java.util.Map;


public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Map<Integer,String> keyMap = genKeyPair();
        //加密字符串
        String content = "JCccc-+你/好,我是需要被*加*密*的*内*容。";
        System.out.println("加密前内容:"+content);
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = encrypt(content, keyMap.get(0));
        System.out.println("加密后的内容为:" + messageEn);
        String messageDe = decrypt(messageEn, keyMap.get(1));
        System.out.println("还原后的内容为:" + messageDe);
    }
}