package com.peploleum.insight.service.util;

import com.peploleum.insight.domain.*;
import com.peploleum.insight.domain.enumeration.InsightEntityType;
import com.peploleum.insight.domain.map.GeometryCollection;
import com.peploleum.insight.domain.map.InsightShape;
import com.vividsolutions.jts.geom.Coordinate;
import org.elasticsearch.common.geo.builders.PointBuilder;

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

    public static GeometryCollection getGeometryFromCoordinate(String[] coordinates) {
        if (coordinates.length == 2) {
            final double latitude = Double.valueOf(coordinates[0]);
            final double longitude = Double.valueOf(coordinates[1]);
            final PointBuilder pointBuilder = new PointBuilder().coordinate(new Coordinate(longitude, latitude));
            final InsightShape geometry = new InsightShape();
            geometry.setType(pointBuilder.type().toString().toLowerCase());
            geometry.setCoordinates(new Double[]{pointBuilder.longitude(), pointBuilder.latitude()});
            GeometryCollection geometryCollection = new GeometryCollection();
            geometryCollection.setGeometries(new InsightShape[]{geometry});
            return geometryCollection;
        }
        return null;
    }
}
