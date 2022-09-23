package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Item item = (Item) target;

        //검증시작
        if (!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required"); // errorCode 규칙 > required.item.itemName 코드명.객체명.필드명 / 맨앞의 코드명만 적으면 된다.
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price","range", new Object[]{1000, 1000000}, null); // errorCode 규칙 > range.item.price 코드명.객체명.필드명 / 맨앞의 코드명만 적으면 된다.
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null); // errorCode 규칙 > max.item.quantity 코드명.객체명.필드명 / 맨앞의 코드명만 적으면 된다.
        }


        // 글로벌 에러처리
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                // 필드에관한 에러가 아니기 때문에 , ObjectError 사용
                errors.reject("totalPriceMin", new Object[]{1000, resultPrice}, null);
            }
        }

    }
}
