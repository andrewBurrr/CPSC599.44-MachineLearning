package ca.ucalgary.rules599.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("trainer.metrics")
@Data
public class TrainerConfig {
    public int support;
    public Resource dataFile;



}
