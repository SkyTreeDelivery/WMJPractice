package com.homework.wmj.TypeHandler;

import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Point;

@MappedTypes(Point.class)
public class PointTypeHandler extends AbstractGeometryTypeHandler<Point> {
}
