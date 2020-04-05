package importData;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

public class Transformer {

	
	public static Point transformPoint (Point inPoint, 
			String sourceEPSG, 
			String targetEPSG) {
		
		try {
			Geometry inGeom = ImportFunctions.geometryFactory.createGeometry(inPoint);

			/*
			 * Transformation
			 * The Stop coordinates are in EPSG:4326 (WGS84, unit: degree). 
			 * The EPSG database usually expects WGS84 with lat,lon axis order, 
			 * while most software expects them to have lon,lat axis order
			 * (see Open Source Geospatial Foundation (2020): Axis Order.
			 *   GeoTools. online at: https://docs.geotools.org/latest/userguide/library/referencing/order.html
			 *   (last checked: 31.03.2020)).
			 * Using the CRSAuthorityFactory, we can force the axis order of 
			 * our target- and soure-CRS to be lon,lat.
			 * Not doing this here will lead to a shift in the coordinates.
			 */
			CRSAuthorityFactory crsFactory = CRS.getAuthorityFactory(true);
			CoordinateReferenceSystem sourceCRS = crsFactory.createCoordinateReferenceSystem(sourceEPSG);
			CoordinateReferenceSystem targetCRS = crsFactory.createCoordinateReferenceSystem(targetEPSG);
			
			Geometry locGeom = null;
			MathTransform transform = null;
			// transform to targetEPSG
			transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
			locGeom = JTS.transform(inGeom, transform);
			// make a point from transformed coordinates
			return ImportFunctions.geometryFactory.createPoint(locGeom.getCoordinate());

		}  catch (FactoryException | TransformException e) {
			System.out.println("The program has problems with transforming the input coordinates. \n"
					+ "You may want to have a look at the coordinate system and\n"
					+ "the axis order they're in.");
			return ImportFunctions.geometryFactory.createPoint(new Coordinate(null));
		}
	}
	
}
