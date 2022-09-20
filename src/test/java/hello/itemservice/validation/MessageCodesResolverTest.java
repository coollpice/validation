package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;


public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Test
    void messageCodesResolverObject() {
        /*
         * 에러메세지 코드를 디테일한 것부터 탐색해서 에러메세지를 생성한다.
         * errorCode.objectName
         * errorCode
         */
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        Assertions.assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        /*
         * 에러메세지 코드를 디테일한 것부터 탐색해서 에러메세지를 생성한다.
         * errorCode.objectName.field
         * errorCode.field
         * errorCode.fieldType
         * errorCode
         */
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        Assertions.assertThat(messageCodes).containsExactly("required.item.itemName", "required.itemName", "required.java.lang.String", "required");
    }
}
