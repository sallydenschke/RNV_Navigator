package router;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import classes.Stop;
import classes.StopRegistry;

public class GeoHelpers {
	
	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
	
	public static Point getPointFromText(String coordinatesText) {
		
		String[] coordsText = coordinatesText.split(";");
		Coordinate coordinate = new Coordinate(Double.parseDouble(coordsText[0]), Double.parseDouble(coordsText[1]));
		Point Point = geometryFactory.createPoint(coordinate);
		return Point;
	}
	
	public static ArrayList<Stop> dynamicBuffer(Geometry inGeom,
			StopRegistry stopRegistry) {
		
		// create a small buffer of 10 meter (assuming the coordinates are
		// in a metric coordinate system) around point to start with
		double bufferVal = 10.0;
		Geometry buffer = inGeom.buffer(bufferVal);
		
		boolean intersect = false;
		ArrayList<Stop> candidates = new ArrayList<Stop>();
		int counter = 1;
		double interval = 10.0;
		boolean breakcond = false;

		/* dynamic buffer gets wider as long as there's no intersection
		 * This iterates through the Stops in the StopRegistry
		 * and checks for intersection with the buffer for each Stop.
		 * If there's an intersection the Stop is considered a candidate.
		 * If there is no Stop that intersects with the buffer,
		 * the buffer distance (bufferVal) is set higher.
		 * Then the process is repeated.
		 * To ensure this  does not take for ever, the interval with which
		 * the buffer distance is set higher changes:
		 *     Until the 10th iteration, the buffer grows by 10 meter
		 *     From the 10 iteration on, the buffer grows by 100 meter
		 *     When the buffer has reached 1km without intersection(99th iteration),
		 *      it grows by 10km
		 *     When the buffer has reached 100km without intersection (190th iteration),
		 *      it grows by 100km
		 *     When the buffer has reached 1,000km without intersection (199th iteration),
		 *      it grows by 1,000km
		 *     When the buffer has reached 10,000km without intersection (208th iteration),
		 *      this process fails. It is assumed that the coordinates did not meet
		 *      the guidelines ("<lon>;<lat>" in EPSG:32632). 
		 */
		while (!intersect && !breakcond) {
			Iterator<Map.Entry<Integer, Stop>>it = stopRegistry.iterate();
			// iterate through StopRegistry
			while (it.hasNext()) {
				// get the StopRegistry entry
				Map.Entry<Integer, Stop> pair = (Map.Entry<Integer, Stop>)it.next();
				Stop currStop = pair.getValue();
				Geometry compStop = geometryFactory.createGeometry(currStop.getLocation());
				if (buffer.intersects(compStop)) {
					intersect = true;
					candidates.add(currStop);
				}
			}
			// dynamic growth
			if (counter == 10) {
				interval = interval*10.0;
			} else if (counter == 99) {
				interval = interval*10.0;
				// buffer is now 1km wide
			} else if (counter == 190) {
				System.out.println("No Stop within 100 km");
				interval = interval*100.0;
				// buffer is now 100km wide
			} else if (counter == 199) {
				System.out.println("No Stop within 1,000 km");
				interval = interval*10.0;
				// buffer is now 1,000km wide
			} else if (counter == 208) {
				System.out.println("No Stop within 10,000 km without intersecting.\n"
						+ "This ends now.");
				// buffer is now 10,000km wide
				breakcond = true;
			}
			
			if (!intersect) {
				bufferVal += interval;
				buffer = inGeom.buffer(bufferVal);
			}
			counter +=1;
		}
		return candidates;
	}
}
