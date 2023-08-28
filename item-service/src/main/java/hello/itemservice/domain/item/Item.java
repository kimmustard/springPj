package hello.itemservice.domain.item;

import lombok.Data;

//@Data는 쓰는걸 지양하자
//@Getter @Setter 정도만 ..
@Data
public class Item {
	
	private Long id;
	private String itemName;
	//Integer로 쓴건 price가 안들어갈수도있다 (null로 들어갈수도있기때문에..)
	private Integer price;
	private Integer quantity;
	
	
	public Item() {
		
	}


	public Item(String itemName, Integer price, Integer quantity) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
	
	
	
}
