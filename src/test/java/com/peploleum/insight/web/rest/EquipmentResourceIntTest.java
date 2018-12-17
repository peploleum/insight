package com.peploleum.insight.web.rest;

import com.peploleum.insight.InsightApp;

import com.peploleum.insight.domain.Equipment;
import com.peploleum.insight.repository.EquipmentRepository;
import com.peploleum.insight.repository.search.EquipmentSearchRepository;
import com.peploleum.insight.service.EquipmentService;
import com.peploleum.insight.service.dto.EquipmentDTO;
import com.peploleum.insight.service.mapper.EquipmentMapper;
import com.peploleum.insight.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;


import static com.peploleum.insight.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.peploleum.insight.domain.enumeration.EquipmentType;
/**
 * Test class for the EquipmentResource REST controller.
 *
 * @see EquipmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsightApp.class)
public class EquipmentResourceIntTest {

    private static final String DEFAULT_EQUIPMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_DESCRIPTION = "BBBBBBBBBB";

    private static final EquipmentType DEFAULT_EQUIPMENT_TYPE = EquipmentType.WEAPON;
    private static final EquipmentType UPDATED_EQUIPMENT_TYPE = EquipmentType.TOOL;

    private static final String DEFAULT_EQUIPMENT_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_COORDINATES = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPMENT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPMENT_SYMBOL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_EQUIPMENT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_EQUIPMENT_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_EQUIPMENT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_EQUIPMENT_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentService equipmentService;

    /**
     * This repository is mocked in the com.peploleum.insight.repository.search test package.
     *
     * @see com.peploleum.insight.repository.search.EquipmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private EquipmentSearchRepository mockEquipmentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EquipmentResource equipmentResource = new EquipmentResource(equipmentService);
        this.restEquipmentMockMvc = MockMvcBuilders.standaloneSetup(equipmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createEntity() {
        Equipment equipment = new Equipment()
            .equipmentName(DEFAULT_EQUIPMENT_NAME)
            .equipmentDescription(DEFAULT_EQUIPMENT_DESCRIPTION)
            .equipmentType(DEFAULT_EQUIPMENT_TYPE)
            .equipmentCoordinates(DEFAULT_EQUIPMENT_COORDINATES)
            .equipmentSymbol(DEFAULT_EQUIPMENT_SYMBOL)
            .equipmentImage(DEFAULT_EQUIPMENT_IMAGE)
            .equipmentImageContentType(DEFAULT_EQUIPMENT_IMAGE_CONTENT_TYPE);
        return equipment;
    }

    @Before
    public void initTest() {
        equipmentRepository.deleteAll();
        equipment = createEntity();
    }

    @Test
    public void createEquipment() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);
        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate + 1);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getEquipmentName()).isEqualTo(DEFAULT_EQUIPMENT_NAME);
        assertThat(testEquipment.getEquipmentDescription()).isEqualTo(DEFAULT_EQUIPMENT_DESCRIPTION);
        assertThat(testEquipment.getEquipmentType()).isEqualTo(DEFAULT_EQUIPMENT_TYPE);
        assertThat(testEquipment.getEquipmentCoordinates()).isEqualTo(DEFAULT_EQUIPMENT_COORDINATES);
        assertThat(testEquipment.getEquipmentSymbol()).isEqualTo(DEFAULT_EQUIPMENT_SYMBOL);
        assertThat(testEquipment.getEquipmentImage()).isEqualTo(DEFAULT_EQUIPMENT_IMAGE);
        assertThat(testEquipment.getEquipmentImageContentType()).isEqualTo(DEFAULT_EQUIPMENT_IMAGE_CONTENT_TYPE);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(1)).save(testEquipment);
    }

    @Test
    public void createEquipmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment with an existing ID
        equipment.setId("existing_id");
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(0)).save(equipment);
    }

    @Test
    public void checkEquipmentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setEquipmentName(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.save(equipment);

        // Get all the equipmentList
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId())))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].equipmentDescription").value(hasItem(DEFAULT_EQUIPMENT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].equipmentType").value(hasItem(DEFAULT_EQUIPMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].equipmentCoordinates").value(hasItem(DEFAULT_EQUIPMENT_COORDINATES.toString())))
            .andExpect(jsonPath("$.[*].equipmentSymbol").value(hasItem(DEFAULT_EQUIPMENT_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].equipmentImageContentType").value(hasItem(DEFAULT_EQUIPMENT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].equipmentImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_EQUIPMENT_IMAGE))));
    }
    
    @Test
    public void getEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.save(equipment);

        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipment.getId()))
            .andExpect(jsonPath("$.equipmentName").value(DEFAULT_EQUIPMENT_NAME.toString()))
            .andExpect(jsonPath("$.equipmentDescription").value(DEFAULT_EQUIPMENT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.equipmentType").value(DEFAULT_EQUIPMENT_TYPE.toString()))
            .andExpect(jsonPath("$.equipmentCoordinates").value(DEFAULT_EQUIPMENT_COORDINATES.toString()))
            .andExpect(jsonPath("$.equipmentSymbol").value(DEFAULT_EQUIPMENT_SYMBOL.toString()))
            .andExpect(jsonPath("$.equipmentImageContentType").value(DEFAULT_EQUIPMENT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.equipmentImage").value(Base64Utils.encodeToString(DEFAULT_EQUIPMENT_IMAGE)));
    }

    @Test
    public void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.save(equipment);

        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment
        Equipment updatedEquipment = equipmentRepository.findById(equipment.getId()).get();
        updatedEquipment
            .equipmentName(UPDATED_EQUIPMENT_NAME)
            .equipmentDescription(UPDATED_EQUIPMENT_DESCRIPTION)
            .equipmentType(UPDATED_EQUIPMENT_TYPE)
            .equipmentCoordinates(UPDATED_EQUIPMENT_COORDINATES)
            .equipmentSymbol(UPDATED_EQUIPMENT_SYMBOL)
            .equipmentImage(UPDATED_EQUIPMENT_IMAGE)
            .equipmentImageContentType(UPDATED_EQUIPMENT_IMAGE_CONTENT_TYPE);
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(updatedEquipment);

        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipmentList.get(equipmentList.size() - 1);
        assertThat(testEquipment.getEquipmentName()).isEqualTo(UPDATED_EQUIPMENT_NAME);
        assertThat(testEquipment.getEquipmentDescription()).isEqualTo(UPDATED_EQUIPMENT_DESCRIPTION);
        assertThat(testEquipment.getEquipmentType()).isEqualTo(UPDATED_EQUIPMENT_TYPE);
        assertThat(testEquipment.getEquipmentCoordinates()).isEqualTo(UPDATED_EQUIPMENT_COORDINATES);
        assertThat(testEquipment.getEquipmentSymbol()).isEqualTo(UPDATED_EQUIPMENT_SYMBOL);
        assertThat(testEquipment.getEquipmentImage()).isEqualTo(UPDATED_EQUIPMENT_IMAGE);
        assertThat(testEquipment.getEquipmentImageContentType()).isEqualTo(UPDATED_EQUIPMENT_IMAGE_CONTENT_TYPE);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(1)).save(testEquipment);
    }

    @Test
    public void updateNonExistingEquipment() throws Exception {
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.toDto(equipment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipmentMockMvc.perform(put("/api/equipment")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipment in the database
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(0)).save(equipment);
    }

    @Test
    public void deleteEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.save(equipment);

        int databaseSizeBeforeDelete = equipmentRepository.findAll().size();

        // Get the equipment
        restEquipmentMockMvc.perform(delete("/api/equipment/{id}", equipment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Equipment> equipmentList = equipmentRepository.findAll();
        assertThat(equipmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Equipment in Elasticsearch
        verify(mockEquipmentSearchRepository, times(1)).deleteById(equipment.getId());
    }

    @Test
    public void searchEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.save(equipment);
        when(mockEquipmentSearchRepository.search(queryStringQuery("id:" + equipment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(equipment), PageRequest.of(0, 1), 1));
        // Search the equipment
        restEquipmentMockMvc.perform(get("/api/_search/equipment?query=id:" + equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId())))
            .andExpect(jsonPath("$.[*].equipmentName").value(hasItem(DEFAULT_EQUIPMENT_NAME)))
            .andExpect(jsonPath("$.[*].equipmentDescription").value(hasItem(DEFAULT_EQUIPMENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].equipmentType").value(hasItem(DEFAULT_EQUIPMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].equipmentCoordinates").value(hasItem(DEFAULT_EQUIPMENT_COORDINATES)))
            .andExpect(jsonPath("$.[*].equipmentSymbol").value(hasItem(DEFAULT_EQUIPMENT_SYMBOL)))
            .andExpect(jsonPath("$.[*].equipmentImageContentType").value(hasItem(DEFAULT_EQUIPMENT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].equipmentImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_EQUIPMENT_IMAGE))));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Equipment.class);
        Equipment equipment1 = new Equipment();
        equipment1.setId("id1");
        Equipment equipment2 = new Equipment();
        equipment2.setId(equipment1.getId());
        assertThat(equipment1).isEqualTo(equipment2);
        equipment2.setId("id2");
        assertThat(equipment1).isNotEqualTo(equipment2);
        equipment1.setId(null);
        assertThat(equipment1).isNotEqualTo(equipment2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentDTO.class);
        EquipmentDTO equipmentDTO1 = new EquipmentDTO();
        equipmentDTO1.setId("id1");
        EquipmentDTO equipmentDTO2 = new EquipmentDTO();
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO2.setId(equipmentDTO1.getId());
        assertThat(equipmentDTO1).isEqualTo(equipmentDTO2);
        equipmentDTO2.setId("id2");
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO1.setId(null);
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
    }
}
