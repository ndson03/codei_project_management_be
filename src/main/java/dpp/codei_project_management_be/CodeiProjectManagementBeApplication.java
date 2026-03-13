package dpp.codei_project_management_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CodeiProjectManagementBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeiProjectManagementBeApplication.class, args);
    }

}
