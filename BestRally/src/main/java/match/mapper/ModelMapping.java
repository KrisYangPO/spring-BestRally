package match.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapping {
	
	@Bean
	ModelMapper modelMap() {
		return new ModelMapper();
	}
}
