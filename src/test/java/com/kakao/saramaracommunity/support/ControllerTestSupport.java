package com.kakao.saramaracommunity.support;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.saramaracommunity.attach.controller.AttachController;
import com.kakao.saramaracommunity.attach.service.AttachService;
import com.kakao.saramaracommunity.bucket.controller.BucketController;
import com.kakao.saramaracommunity.bucket.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser(roles = "USER")
@WebMvcTest(controllers = {
        AttachController.class,
        BucketController.class
})
public class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AttachService attachService;

    @MockBean
    protected BucketService bucketService;

}
