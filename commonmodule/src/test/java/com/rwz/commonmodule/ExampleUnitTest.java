package com.rwz.commonmodule;

import com.rwz.commonmodule.utils.safety.EncryptionUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        String str = "@53b22895bd2cceca23b81790331103c9bbffd0a3f69c98106f7b26dd0e6855bb@bd0755ec963f3381ea91baeb6a519dc831a7d540e6a1b9aec94f367c64fa6cc7";
        String encode = EncryptionUtil.encodeBase64(str.getBytes());
        System.out.println("encode = " + encode);
//

    }
}