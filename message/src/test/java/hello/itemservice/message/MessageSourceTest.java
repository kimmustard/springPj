package hello.itemservice.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {

	@Autowired
	MessageSource ms;
	
	
	
	@Test
	void helloMessage() {
		//(code, args, locale)
		//locale이 없으면 디폴트인 message.properties가 선택됨
		String result = ms.getMessage("hello", null, null);
		assertThat(result).isEqualTo("안녕");
	}
	
	@Test
	void notFoundMessageCode() {
		
		assertThatThrownBy(()-> ms.getMessage("no_code", null, null))
				.isInstanceOf(NoSuchMessageException.class);
	}
	
	@Test
	void notFoundMessageCodeDefaultMessage() {
		//메세지를 못찾으면 디폴트 "기본 메세지"를 잡아준다.
		String result = ms.getMessage("no_code", null,"기본 메세지" ,null);
		assertThat(result).isEqualTo("기본 메세지");
	}
		
	
	
	
	
	@Test
	void argumentMessage() {
		String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
		assertThat(message).isEqualTo("안녕 Spring");
		
	}
		
		
		
	@Test
	void defaultLang() {
		assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
		assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
	}
	
		
	@Test
	void enLang() {
		assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
	}
		
	
	
}
