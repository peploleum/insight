package com.peploleum.insight.service;

import com.peploleum.insight.domain.kibana.EntityMappingInfo;
import com.peploleum.insight.domain.kibana.KibanaDashboardGenerationParameters;
import com.peploleum.insight.service.dto.KibanaObjectReferenceDTO;

import java.util.List;
import java.util.Set;

/**
 * Created by GFOLGOAS on 04/04/2019.
 */
public interface InsightKibanaService {
    void generateAndPostKibanaDashboard(final KibanaDashboardGenerationParameters dashboardParameters);

    List<KibanaObjectReferenceDTO> getDashboardRef();

    Set<EntityMappingInfo> getEntitiesMappingInfo();

    void deleteAllDashboard();

    void deleteSingleKibanaObject(final String objectId);
}
