package dev.J.RepositoryForFamilies.Resources;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@CrossOrigin(origins = {"http://192.168.50.237:3000","http://localhost:3000"},allowCredentials = "true")
@RestController
public class ResourcesController {

    private final ResourceService resourceService;

    @PostMapping("/auth/api/test")
    public void test(){
        resourceService.deleteResource(null,"j@gmail.com",null);
    }

}
