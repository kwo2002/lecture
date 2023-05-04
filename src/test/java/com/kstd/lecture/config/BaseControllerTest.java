package com.kstd.lecture.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeMatchingHeaders;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.kstd.com", uriPort = 80)
public class BaseControllerTest {
  protected MockMvc mockMvc;

  @BeforeEach
  public void setUp(WebApplicationContext context,
                    RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                    .withRequestDefaults(prettyPrint(),
                            removeMatchingHeaders("Pragma", "Vary", "X-Content-Type-Options",
                                    "X-XSS-Protection", "Cache-Control", "Expires", "Strict-Transport-Security",
                                    "X-Frame-Options", "Content-Length", "_csrf", "Host"))
                    .withResponseDefaults(prettyPrint(),
                            removeMatchingHeaders("Pragma", "Vary", "X-Content-Type-Options",
                                    "X-XSS-Protection", "Cache-Control", "Expires", "Strict-Transport-Security",
                                    "X-Frame-Options", "Content-Length")))
            .build();
  }
}
