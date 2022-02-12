package com.ecsimsw.fastcrud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ClassUtils;

@AutoConfigureMockMvc
@SpringBootTest
public class WebIntegrationTest {

    private static final Class<?> SAMPLE_ENTITY_TYPE = SampleEntity.class;
    private static final String ENTITY_NAME = ClassUtils.getShortNameAsProperty(SAMPLE_ENTITY_TYPE);

    @MockBean
    private SampleEntityRepository mockRepository;

    @Captor
    private ArgumentCaptor<SampleEntity> entityCaptor;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SampleEntity dummyData = new SampleEntity(1L, "name");

    public WebIntegrationTest(
            @Autowired MockMvc mockMvc,
            @Autowired ObjectMapper objectMapper
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("post request :: 요청 entity를 저장한다.")
    @Test
    public void save() throws Exception {
        final SampleEntity testEntity = new SampleEntity("name");
        final String requestUrl = "/" + ENTITY_NAME;
        final MockHttpServletResponse response = mockMvc.perform(
                        post(requestUrl).content(objectMapper.writeValueAsString(testEntity))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(objectMapper.readValue(response.getContentAsString(), SampleEntity.class))
                .usingRecursiveComparison()
                .isEqualTo(testEntity);
    }

    @DisplayName("get request :: entity 전체 조회 결과를 반환한다.")
    @Test
    public void findAll() throws Exception {
        final List<SampleEntity> expectedResult = Arrays.asList(dummyData);
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
        final Long id = dummyData.getId();
        Mockito.when(mockRepository.findById(id))
                .thenReturn(Optional.of(dummyData));

        final String requestUrl = "/" + ENTITY_NAME + "/" + id;
        final MockHttpServletResponse response = mockMvc.perform(get(requestUrl))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(objectMapper.readValue(response.getContentAsString(), SampleEntity.class))
                .usingRecursiveComparison()
                .isEqualTo(dummyData);
    }

    @DisplayName("put request :: id에 해당하는 entity 데이터를 수정한다.")
    @Test
    public void update() throws Exception {
        final Long id = dummyData.getId();
        final SampleEntity updateData = new SampleEntity("newName");
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(dummyData));

        final String requestUrl = "/" + ENTITY_NAME + "/" + id;
        mockMvc.perform(put(requestUrl).content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        Mockito.verify(mockRepository).save(entityCaptor.capture());

        final SampleEntity captorValue = entityCaptor.getValue();
        assertThat(captorValue.getId()).isEqualTo(id);
        assertThat(captorValue.getName()).isEqualTo(updateData.getName());
    }

    @DisplayName("delete request :: id에 해당하는 entity 데이터를 삭제한다.")
    @Test
    public void deleteOne() throws Exception {
        final Long id = dummyData.getId();
        Mockito.when(mockRepository.findById(id)).thenReturn(Optional.of(dummyData));

        final String requestUrl = "/" + ENTITY_NAME + "/" + id;
        mockMvc.perform(delete(requestUrl))
                .andExpect(status().isNoContent())
                .andDo(print());

        Mockito.verify(mockRepository).delete(entityCaptor.capture());

        final SampleEntity captorValue = entityCaptor.getValue();
        assertThat(captorValue.getId()).isEqualTo(dummyData.getId());
    }

    private TypeReference<List<SampleEntity>> listTypeReference() {
        return new TypeReference<List<SampleEntity>>() {
        };
    }
}

@CRUD
@Entity
class SampleEntity {

    @GeneratedValue
    @Id
    private Long id;
    private String name;

    public SampleEntity() {
    }

    public SampleEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SampleEntity(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

interface SampleEntityRepository extends JpaRepository<SampleEntity, Long> {
}
