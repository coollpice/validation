package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    //    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult , RedirectAttributes redirectAttributes , Model model) {

        // model 에 담기는 객체명 , 객체의 필드명 , 메세지내용

        log.info(item.toString());

        //검증시작
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item" , "itemName" , "상품명은 필수입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item" , "price" , "가격은 1,000 ~ 1,000,000 까지 허용됩니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item" , "quantity" , "수량은 최대 9,999개 까지만 허용됩니다."));
        }


        // 글로벌 에러처리
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                // 필드에관한 에러가 아니기 때문에 , ObjectError 사용
                bindingResult.addError(new ObjectError("item" , "가격 * 수량의 합은 10,000원 이상이여야 합니다. 현재 값 : " + resultPrice));
            }
        }

        //검증 실패시 다시 입력 폼으로

        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult , RedirectAttributes redirectAttributes , Model model) {

        // model 에 담기는 객체명 , 객체의 필드명 , 메세지내용
        log.info(item.toString());

        //검증시작
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품명은 필수입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용됩니다."));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999개 까지만 허용됩니다."));
        }


        // 글로벌 에러처리
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                // 필드에관한 에러가 아니기 때문에 , ObjectError 사용
                bindingResult.addError(new ObjectError("item" , null, null, "가격 * 수량의 합은 10,000원 이상이여야 합니다. 현재 값 : " + resultPrice));
            }
        }

        //검증 실패시 다시 입력 폼으로

        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult , RedirectAttributes redirectAttributes , Model model) {

        // model 에 담기는 객체명 , 객체의 필드명 , 메세지내용
        log.info(item.toString());

        //검증시작
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }


        // 글로벌 에러처리
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                // 필드에관한 에러가 아니기 때문에 , ObjectError 사용
                bindingResult.addError(new ObjectError("item" , new String[]{"totalPriceMin"}, new Object[]{1000, resultPrice}, null));
            }
        }

        //검증 실패시 다시 입력 폼으로

        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    //    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult , RedirectAttributes redirectAttributes , Model model) {

        // model 에 담기는 객체명 , 객체의 필드명 , 메세지내용
        log.info(item.toString());

        //검증 실패시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        //검증시작
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required"); // errorCode 규칙 > required.item.itemName 코드명.객체명.필드명 / 맨앞의 코드명만 적으면 된다.
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price","range", new Object[]{1000, 1000000}, null); // errorCode 규칙 > range.item.price 코드명.객체명.필드명 / 맨앞의 코드명만 적으면 된다.
        }

        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null); // errorCode 규칙 > max.item.quantity 코드명.객체명.필드명 / 맨앞의 코드명만 적으면 된다.
        }


        // 글로벌 에러처리
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{1000, resultPrice}, null);
            }
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult , RedirectAttributes redirectAttributes , Model model) {

        // model 에 담기는 객체명 , 객체의 필드명 , 메세지내용
        log.info(item.toString());

        itemValidator.validate(item, bindingResult);

        //검증 실패시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult , RedirectAttributes redirectAttributes , Model model) {

        // model 에 담기는 객체명 , 객체의 필드명 , 메세지내용
        log.info(item.toString());

        //검증 실패시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

