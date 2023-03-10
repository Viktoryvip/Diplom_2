package base;
import skeleton.*;
import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public static User getRandom() {
        String name = RandomStringUtils.randomAlphabetic(8);
        String email = RandomStringUtils.randomAlphabetic(8) + "@mail.ru";
        String password = RandomStringUtils.randomAlphabetic(8);
        return new User(name, email, password);
    }
}