package com.peploleum.insight.service.util;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.service.dto.*;

/**
 * Created by GFOLGOAS on 04/04/2019.
 */
public class InsightUtil {
    public static Class<? extends InsightEntity> getClassFromType(InsightEntityType type) {
        switch (type) {
            case Biographics:
                return Biographics.class;
            case Equipment:
                return Equipment.class;
            case Event:
                return Event.class;
            case Location:
                return Location.class;
            case Organisation:
                return Organisation.class;
            case RawData:
                return RawData.class;
            default:
                return InsightEntity.class;
        }
    }

    public static String getEntityFieldNameFromType(InsightEntityType type) {
        switch (type) {
            case Biographics:
                return "biographicsName";
            case Equipment:
                return "equipmentName";
            case Event:
                return "eventName";
            case Location:
                return "locationName";
            case Organisation:
                return "organisationName";
            case RawData:
                return "rawDataName";
            default:
                return null;
        }
    }
}
