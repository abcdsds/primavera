package com.genius.primavera.application.article;

import com.genius.primavera.domain.model.article.Article;
import com.genius.primavera.domain.model.article.ArticleDto;
import com.genius.primavera.domain.model.article.WriteType;
import com.genius.primavera.interfaces.WithMockPrimaveraUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WriteArticleServiceTest {

	@Autowired
	private WriteArticleService writeArticleService;

	private WriteArticleService mockWriteArticleService;

	private static ArticleDto.WriteArticle writeRequestArticle;
	private static Article article_1;
	private static Article article_1_1;
	private static Article article_1_1_1;
	private static Article article_1_1_1_1;
	private static Article article_1_1_2;
	private static Article article_1_2;
	private static Article article_2;
	private static Article article_2_1;
	private static Article article_2_2;
	private static Article article_2_3;

	@BeforeAll
	public static void setUp() {
		writeRequestArticle = new ArticleDto.WriteArticle();
		writeRequestArticle.setPId(0);
		writeRequestArticle.setSubject("제목_1");
		writeRequestArticle.setContents("원글");
		writeRequestArticle.setWriteType(WriteType.FORM);
	}

	@Test
	@Order(1)
	@DisplayName("Mock 글 쓰기")
	public void mockWriteTest() {
		mockWriteArticleService = Mockito.mock(WriteArticleService.class);
		given(this.mockWriteArticleService.save(writeRequestArticle)).willReturn(new Article());
		Assertions.assertEquals(new Article(), mockWriteArticleService.save(writeRequestArticle));
	}

	@Test
	@Order(2)
	@DisplayName("원글 첫번째 쓰기")
	@WithMockPrimaveraUserDetails
	public void writeTest() {
		article_1 = writeArticleService.save(writeRequestArticle);
		article_1 = writeArticleService.findById(article_1.getId());
		Assertions.assertEquals(null, article_1.getParent());
		Assertions.assertEquals(1, article_1.getStep());
	}

	@Test
	@Order(3)
	@DisplayName("원글 첫번째 답글 쓰기")
	@WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
	public void writeFirstReplyTest() {
		Article origin = writeArticleService.findById(article_1.getId());
		writeRequestArticle.setPId(origin.getId());
		writeRequestArticle.setSubject(origin.getSubject() + "_1");
		writeRequestArticle.setContents("원글 첫번째 답글 쓰기");
		writeRequestArticle.setWriteType(WriteType.REPLY);
		article_1_1 = writeArticleService.save(writeRequestArticle);
		Assertions.assertEquals(article_1.getReference(), article_1_1.getReference());
		Assertions.assertEquals(2, article_1_1.getLevel());
		Assertions.assertEquals(2, article_1_1.getStep());
	}

	@Test
	@Order(4)
	@DisplayName("원글 두번째 답글 쓰기")
	@WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
	public void writeSecondReplyTest() {
		Article origin = writeArticleService.findById(article_1.getId());
		writeRequestArticle.setPId(origin.getId());
		writeRequestArticle.setSubject(origin.getSubject() + "_2");
		writeRequestArticle.setContents("원글 첫번째 두번째 답글 쓰기");
		writeRequestArticle.setWriteType(WriteType.REPLY);
		article_1_2 = writeArticleService.save(writeRequestArticle);
		Assertions.assertEquals(article_1.getReference(), article_1_2.getReference());
		Assertions.assertEquals(2, article_1_2.getLevel());
		Assertions.assertEquals(2, article_1_2.getStep());
	}

	@Test
	@Order(5)
	@DisplayName("원글 첫번째 답글 첫번째 답글 쓰기")
	@WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
	public void writeFirst_FirstReplyTest() {
		Article origin = writeArticleService.findById(article_1_1.getId());
		writeRequestArticle.setPId(origin.getId());
		writeRequestArticle.setSubject(origin.getSubject() + "_1");
		writeRequestArticle.setContents("원글 첫번째 답글 첫번째 답글 쓰기");
		writeRequestArticle.setWriteType(WriteType.REPLY);
		article_1_1_1 = writeArticleService.save(writeRequestArticle);
		Assertions.assertEquals(article_1.getReference(), article_1_1_1.getReference());
		Assertions.assertEquals(3, article_1_1_1.getLevel());
		Assertions.assertEquals(4, article_1_1_1.getStep());
	}

	@Test
	@Order(6)
	@DisplayName("원글 두번째 쓰기")
	@WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
	public void writeSecondTest() {
		writeRequestArticle.setPId(0);
		writeRequestArticle.setSubject("제목_2");
		writeRequestArticle.setContents("원글");
		writeRequestArticle.setWriteType(WriteType.FORM);
		article_2 = writeArticleService.save(writeRequestArticle);
		article_2 = writeArticleService.findById(article_2.getId());
		Assertions.assertEquals(null, article_1.getParent());
		Assertions.assertEquals(1, article_1.getStep());
	}

	@Test
	@Order(7)
	@DisplayName("원글 첫번째 답글 첫번째 답글 첫번째 답글 쓰기")
	@WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
	public void writeFirst_FirstReply_FirstReplyTest() {
		Article origin = writeArticleService.findById(article_1_1_1.getId());
		writeRequestArticle.setPId(origin.getId());
		writeRequestArticle.setSubject(origin.getSubject() + "_1");
		writeRequestArticle.setContents("원글 첫번째 답글 첫번째 답글 첫번째 답글 쓰기");
		writeRequestArticle.setWriteType(WriteType.REPLY);
		article_1_1_1_1 = writeArticleService.save(writeRequestArticle);
		Assertions.assertEquals(4, article_1_1_1_1.getLevel());
		Assertions.assertEquals(5, article_1_1_1_1.getStep());
	}

	@Test
	@Order(8)
	@DisplayName("원글 첫번째 답글 첫번째 답글 두번째 답글 쓰기")
	@WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
	public void writeFirst_FirstReply_SecondReplyTest() {
		Article origin = writeArticleService.findById(article_1_1.getId());
		writeRequestArticle.setPId(origin.getId());
		writeRequestArticle.setSubject(origin.getSubject() + "_2");
		writeRequestArticle.setContents("원글 첫번째 답글 첫번째 답글 쓰기");
		writeRequestArticle.setWriteType(WriteType.REPLY);
		article_1_1_2 = writeArticleService.save(writeRequestArticle);
		Assertions.assertEquals(article_1.getReference(), article_1_1_2.getReference());
		Assertions.assertEquals(3, article_1_1_2.getLevel());
		Assertions.assertEquals(4, article_1_1_2.getStep());
	}

	@Test
	@Order(9)
	@DisplayName("원글 두번째 답글 첫번째 쓰기")
	@WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
	public void writeSecond_FirstReply_Test() {
		Article origin = writeArticleService.findById(article_2.getId());
		writeRequestArticle.setPId(origin.getId());
		writeRequestArticle.setSubject(origin.getSubject() + "_1");
		writeRequestArticle.setContents("원글 두번째 답글 첫번째 쓰기");
		article_2_1 = writeArticleService.save(writeRequestArticle);
		Assertions.assertEquals(article_2.getReference(), article_2_1.getReference());
		Assertions.assertEquals(2, article_2_1.getLevel());
		Assertions.assertEquals(2, article_2_1.getStep());
	}

	@Test
	@Order(10)
	@DisplayName("원글 두번째 답글 두번째 쓰기")
	@WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
	public void writeSecond_SecondReply_Test() {
		Article origin = writeArticleService.findById(article_2.getId());
		writeRequestArticle.setPId(origin.getId());
		writeRequestArticle.setSubject(origin.getSubject() + "_2");
		writeRequestArticle.setContents("원글 두번째 답글 두번째 쓰기");
		writeRequestArticle.setWriteType(WriteType.REPLY);
		article_2_2 = writeArticleService.save(writeRequestArticle);

		Assertions.assertEquals(article_2.getReference(), article_2_2.getReference());
		Assertions.assertEquals(2, article_2_2.getLevel());
		Assertions.assertEquals(2, article_2_2.getStep());
	}

	@Test
	@Order(11)
	@DisplayName("원글 두번째 답글 세번째 쓰기")
	@WithUserDetails(value = "Genius Choi", userDetailsServiceBeanName = "primaveraUserDetailsService")
	public void writeSecond_ThirdReply_Test() {
		Article origin = writeArticleService.findById(article_2.getId());
		writeRequestArticle.setPId(origin.getId());
		writeRequestArticle.setSubject(origin.getSubject() + "_3");
		writeRequestArticle.setWriteType(WriteType.REPLY);
		writeRequestArticle.setContents("원글 두번째 답글 세번째 쓰기");
		article_2_3 = writeArticleService.save(writeRequestArticle);
		Assertions.assertEquals(article_2.getReference(), article_2_3.getReference());
		Assertions.assertEquals(2, article_2_3.getLevel());
		Assertions.assertEquals(2, article_2_3.getStep());
	}
}