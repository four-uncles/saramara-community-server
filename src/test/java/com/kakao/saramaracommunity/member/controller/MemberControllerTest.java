package com.kakao.saramaracommunity.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.saramaracommunity.member.dto.MemberSaveRequestDto;
import com.kakao.saramaracommunity.member.entity.Role;
import com.kakao.saramaracommunity.member.entity.Type;
import com.kakao.saramaracommunity.member.service.MemberSerivce;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(MemberController.class)
//@Import({RestDocsConfig.class})
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private RestDocumentationResultHandler restDocs;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberSerivce memberSerivce;

//    @BeforeEach
//    void setUp(
//            WebApplicationContext context,
//            RestDocumentationContextProvider provider
//    ) {
//        WebApplicationContext webApplicationContext;
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(documentationConfiguration(provider))
////                .alwaysDo(restDocs)
//                .build();
//    }

    @Test
    @WithMockUser(roles = "USER")
    public void 사용자_회원가입() throws Exception {
        //given
        MemberSaveRequestDto requestDto = MemberSaveRequestDto.builder()
                .type(Type.LOCAL)
                .email("lango@kakao.com")
                .nickname("lango")
                .password("test123")
                .role(Role.USER)
                .picture("test")
                .build();
        System.out.println("requestDto: " + requestDto);

        //when with then
//        mockMvc.perform(post("/api/register")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON_UTF8)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                )
//                .andExpect(status().isOk())
//                .andDo(restDocs.document());

        mockMvc.perform(post("/api/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(requestDto))
                )
                .andDo(print())
                .andDo(document("member/register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());
    }

}
