package hello.itemservice.web.basic;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;

@Controller
@RequestMapping("/basic/items")
//@RequiredArgsConstructor
public class BasicItemController {

	private final ItemRepository itemRepository;

	@Autowired
	public BasicItemController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
	
	//상품목록
	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		return "basic/items";
	}
	
	//상품상세
	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item",item);
		return "basic/item";
	}
	
	//상품등록 폼
	@GetMapping("/add")
	public String addForm() {
		return "basic/addForm";
	}
	
	//상품등록 폼 저장 (같은 url이지만 http 메서드로 기능을 구별한다./)
//	@PostMapping("/add")
	public String addItemV1(@RequestParam String itemName,
						@RequestParam int price,
						@RequestParam Integer quantity,
						Model model) {
		Item item = new Item();
		item.setItemName(itemName);
		item.setPrice(price);
		item.setQuantity(quantity);
		
		itemRepository.save(item);
		model.addAttribute("item",item);
		
		return "basic/item";
	}
	
	
//	@PostMapping("/add")
	public String addItemV2(@ModelAttribute("item") Item item, Model model) {	
		itemRepository.save(item);
//		model.addAttribute("item",item);
		
		return "basic/item";
	}
	
//	@PostMapping("/add")
	public String addItemV3(@ModelAttribute Item item) {
				
		itemRepository.save(item);
		
		return "basic/item";
	}

//	@PostMapping("/add")
	public String addItemV4(Item item) {
		itemRepository.save(item);
		return "basic/item";
	}
	

//	@PostMapping("/add")
	public String addItemV5(Item item) {
		itemRepository.save(item);
		return "redirect:/basic/items/" + item.getId();
	}
	
	@PostMapping("/add")
	public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/basic/items/{itemId}";
	}
	
	
	
	
	//상품 수정 폼
	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item",item);
		return "basic/editForm";
	}
	
	//상품 수정
	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
		itemRepository.update(itemId, item);
		return "redirect:/basic/items/{itemId}";
	}
	
	
	
	
	
	/**
	 * 테스트용 데이터 추가 
	 */
	//  '초기화 콜백'을 적용할 수 있는 어노테이션
	@PostConstruct
	public void init() {
		itemRepository.save(new Item("testA", 10000, 10));
		itemRepository.save(new Item("testB", 20000, 20));
	}
	
}
