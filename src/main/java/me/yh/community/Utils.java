package me.yh.community;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import java.util.Random;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * @return 랜덤코드 6자리
     */
    public static String createRandomCode() {
        int certCharLength = 6;
        final char[] characterTable = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        Random random = new Random(System.currentTimeMillis());
        int len = characterTable.length;
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < certCharLength; i++) {
            buf.append(characterTable[random.nextInt(len)]);
        }
        String randomCode = buf.toString();
        log.info("인증코드생성: '{}'", randomCode);
        return randomCode;
    }

    /**
     * @param msg      메시지
     * @param redirect 이동할 url
     */
    public static void redirectErrorPage(Model model, String msg, String redirect) {
        model.addAttribute("msg", msg);
        model.addAttribute("redirect", redirect);
    }
}
