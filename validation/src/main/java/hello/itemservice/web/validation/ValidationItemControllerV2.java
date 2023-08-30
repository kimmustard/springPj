package hello.itemservice.web.validation;

import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

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

    //BindingResult bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야한다.
    //Item 객체의 바인딩 결과를 담아야 하기 때문에 순서가 중요하다!
    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    
    	//검증 로직
    	//hasText 문자열 유효성 검증 유틸 메소드 (null인것, 길이가0인것 공백인것 하나라도 포함되면 false)
    	//글자가 없다면
    	if (!StringUtils.hasText(item.getItemName())) {
    		bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
    	}
    	if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    		bindingResult.addError(new FieldError("item", "price", "가격은 1,000원 ~ 1,000,000원까지 허용합니다."));
    	}
    	if (item.getQuantity() == null || item.getQuantity() >= 9999) {
    		bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
    	}
    	
    	//특정 필드가 아닌 복합 룰 검증
    	if (item.getPrice() != null && item.getQuantity() != null) {
    		 int resultPrice = item.getPrice() * item.getQuantity();
    		 if (resultPrice < 10000) {
    			bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값 = " + resultPrice));
    		 }
    	}
    	
    	
    	//검증에 실패하면 다시 입력 폼으로
    	//bindingResult.hasErrors <<는 bindingResult 에러가 있으면~ 라는뜻
    	//bindingResult는 자동으로 뷰에 넘어가기 때문에 model이 필요없다.
    	if(bindingResult.hasErrors()) {
    		log.info("errors={} ", bindingResult);
    		return "validation/v2/addForm";
    	}
    	
    	
    	//성공 로직
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

