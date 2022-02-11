package com.ecsimsw.fastcrud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecsimsw.fastcrud.sample.TestEntity;
import com.ecsimsw.fastcrud.sample.TestEntityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class WebIntegrationTest {

    private static final String ENTITY_NAME = "testEntity";

    @MockBean
    private TestEntityRepository mockRepository;

    @Captor
    private ArgumentCaptor<TestEntity> entityCaptor;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private TestEntity originData;

    public WebIntegrationTest(
            @Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        originData = new TestEntity(1L, "name");
    }

    @DisplayName("get request :: entity 전체 조회 결과를 반환한다.")
    @Test
    public void findAll() throws Exception {
        final List<TestEntity> expectedResult = Arrays.asList(originData);
        Mockito.when(mockRepository.findAll()).thenReturn(expectedResult);

        final String requestUrl = "/" + ENTITY_NAME;
        final MockHttpServletResponse response = mockMvc.perform(get(requestUrl))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(objectMapper.readValue(response.getContentAsString(), listTypeReference()))
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }

    @DisplayName("get request :: 해당하는 entity 단일 조회 결과를 반환한다.")
    @Test
    public void findOne() throws Exception {
        final Long id = originData.getId();
        Mockito.when(mockRepository.findById(id))
                .thenReturn(Optional.of(originData));

        final String requestUrl = "/" + ENTITY_NAME + "/" + id;
        final MockHttpServletResponse response = mockMvc.perform(get(requestUrl))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(objectMapper.readValue(response.getContentAsString(), TestEntity.class))
                .usingRecursiveComparison()
                .isEqualTo(originData);
    }

    @DisplayName("put request :: id에 해당하는 entity 데이터를 수정한다.")
    @Test
    public void update() throws Exception {
        final Long id = originData.getId();
        final TestEntity updateData = new TestEntity("newName");
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(originData));

        final String requestUrl = "/" + ENTITY_NAME + "/" + id;
        mockMvc.perform(put(requestUrl).content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        Mockito.verify(mockRepository).save(entityCaptor.capture());

        final TestEntity captorValue = entityCaptor.getValue();
        assertThat(captorValue.getId()).isEqualTo(id);
        assertThat(captorValue.getName()).isEqualTo(updateData.getName());
    }

    @DisplayName("delete request :: id에 해당하는 entity 데이터를 삭제한다.")
    @Test
    public void deleteOne() throws Exception {
        final Long id = originData.getId();
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(originData));

        final String requestUrl = "/" + ENTITY_NAME + "/" + id;
        mockMvc.perform(delete(requestUrl))
                .andExpect(status().isOk())
                .andDo(print());

        Mockito.verify(mockRepository).delete(entityCaptor.capture());

        final TestEntity captorValue = entityCaptor.getValue();
        assertThat(captorValue.getId()).isEqualTo(originData.getId());
    }

    private TypeReference<List<TestEntity>> listTypeReference() {
        return new TypeReference<List<TestEntity>>() {
        };
    }
}
