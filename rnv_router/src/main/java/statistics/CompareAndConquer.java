package statistics;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;

public class CompareAndConquer {

	public static void compareAndConquer(double[][] results,
			double[] routeNumbers,
			double[] currouteElement, 
			double criterium, 
			int startIdx, 
			boolean order) {
	
		// iterate through Elements in the results, starting with the startIdx
		for (int elementIdx = startIdx; elementIdx < results.length; elementIdx++) {
			/* Compare the length of the current Route to the length
			 * of each element in results.
			 * Use the Double.compare method for the comparison of the two doubles.
			 * > 0 means: 
			 *     results criterium value (of current Route) is larger or equal 
			 *     to value (length of comparison-Route)  
			 * == 0 means equality
			 * <= 0 means opposite of >= 0
			 */
			// value to compare to
			double value = results[elementIdx][1];
			// RouteNumber to compare
			final double NumberCopy = currouteElement[2];
			
			boolean breakcondition = false;
			
			boolean compareStatement;
			if (!order) {
				/* descending -> top 5 longest
				 * true if our current criterium value is larger than the value in results at this index
				 * and if the Number is not yet in the list of RouteNumbers
				 */
				compareStatement = ((Double.compare(currouteElement[1], value) >= 0) 
						&& DoubleStream.of(routeNumbers).noneMatch(num -> num == NumberCopy));
			} else {
				/* ascending -> top 5 shortest
				 * true if our current criterium value is smaller than the value in results at this index
				 * or if the length at this element is equal to 0 (comparing using the minimal value of
				 * a double to make sure it's equal considering rounding errors the imperfect representation
				 * of floating numbers by bytes
				 */
				compareStatement = ((Double.compare(currouteElement[1], value) <= 0 
						|| Math.abs(results[elementIdx][1] - 0) < 2 * Double.MIN_VALUE)
						&& DoubleStream.of(routeNumbers).noneMatch(num -> num == NumberCopy));
			}
			
			if (compareStatement) {
				// store the original element
				double[] orgElement = results[elementIdx];
				
				/* if the original element had been empty, break this loop instead 
				 * of resetting the currouteElement when searching for the top5 longest
				 * thus not reordering the results (not necessary here)
				 */
				if(orgElement[1]==0) {
					breakcondition = true;
				}
				results[elementIdx] = new double[] {currouteElement[0], currouteElement[1], currouteElement[2]};
				routeNumbers[elementIdx] = currouteElement[2];
				if (breakcondition && !order) {
					break;
				}
				
				/* Setting the original element as current Element
				 * lets us reorder the rest of the array.
				 * Say, for example, we have a results array of the
				 * following entries:
				 *     [ [Route1,6] , [Route2,4] , [Route3,3] , [Route4,2] , [Route5,1] ]
				 * and a current Element that we want to compare to these of
				 *     [Route6,5]
				 * In that case our current Element belongs in second place,
				 * where, originally, Route2 had been. Inserting Route6 in
				 * second place means we have to reorder the results array,
				 * shifting the other elements by 1.
				 * Thus we remember the original element at this spot, Route2
				 * and then repeat the comparison with this element instead.
				 * It will be inserted at the third position now, leaving us with
				 * Route3 as new original element, that we now store as current
				 * element, etc.
				 * As an array has a fixed length (here of 5), the last original
				 * element (in our example Route5) will be left over. But as the
				 * stop condition of this for loop will then be fulfilled, there's
				 * no attempt to enter it in a nonexistent sixth place.
				 * This loop breaks and the currouteElement will be set to null,
				 * freeing what little memory it had blocked.
				 */
				currouteElement = orgElement;
			}
		}
		currouteElement = null;
	}
	
	
	public static void compareAndConquer(long[][] results, 
			long[] routeNumbers,
			long[] currouteElement, 
			long criterium, 
			int startIdx, 
			boolean order) {
		
		// iterate through Elements in the results, starting with the startIdx
		for (int elementIdx = startIdx; elementIdx < results.length; elementIdx++) {
			/* Compare the duration of the current Route to the duration
			 * of each element in results.
			 * Use the Long.compare method for the comparison of the two longs.
			 * > 0 means: 
			 *     results criterium value (of current Route) is larger or equal 
			 *     to value (duration of comparison-Route)  
			 * == 0 means equality
			 * <= 0 means opposite of >= 0
			 */
			// value to compare to (length)
			long value = results[elementIdx][1];
			// RouteNumber to compare
			final long numberCopy = currouteElement[2];
			
			boolean breakcondition = false;
			
			boolean compareStatement;
			if (!order) {
				/* descending -> top 5 longest
				 * true if our current criterium value is larger than the value in results at this index
				 */
				compareStatement = (Long.compare(currouteElement[1], value) >= 0 
						&& LongStream.of(routeNumbers).noneMatch(num -> num == numberCopy));
			} else {
				/* ascending -> top 5 shortest
				 * true if our current criterium value is smaller than the value in results at this index
				 * or if the duration at this element is equal to 0 (comparing using the minimal value of
				 * a long to make sure it's equal considering rounding errors the imperfect representation
				 * of floating numbers by bytes
				 */
				compareStatement = ((Long.compare(currouteElement[1], value) <= 0 
						|| results[elementIdx][1] == 0)
						&& LongStream.of(routeNumbers).noneMatch(num -> num == numberCopy));
			}
			
			if (compareStatement) {
				// store the original element
				long[] orgElement = results[elementIdx];
				
				/* if the original element has been empty, break this loop instead 
				 * of resetting the currouteElement when searching for the top5 longest
				 * thus not reordering the results (not necessary here)
				 */
				if(orgElement[1]==0) {
					breakcondition = true;
				}
				results[elementIdx] = new long[] {currouteElement[0], currouteElement[1], currouteElement[2]};
				routeNumbers[elementIdx] = (long) currouteElement[2];
				if (breakcondition && !order) {
					break;
				}
				
				/* Setting the original element as current Element
				 * lets us reorder the rest of the array.
				 * Say, for example, we have a results array of the
				 * following entries:
				 *     [ [Route1,6] , [Route2,4] , [Route3,3] , [Route4,2] , [Route5,1] ]
				 * and a current Element that we want to compare to these of
				 *     [Route6,5]
				 * In that case our current Element belongs in second place,
				 * where, originally, Route2 had been. Inserting Route6 in
				 * second place means we have to reorder the results array,
				 * shifting the other elements by 1.
				 * Thus we remember the original element at this spot, Route2
				 * and then repeat the comparison with this element instead.
				 * It will be inserted at the third position now, leaving us with
				 * Route3 as new original element, that we now store as current
				 * element, etc.
				 * As an array has a fixed length (here of 5), the last original
				 * element (in our example Route5) will be left over. But as the
				 * stop condition of this for loop will then be fulfilled, there's
				 * no attempt to enter it in a nonexistent sixth place.
				 * This loop breaks and the currouteElement will be set to null,
				 * freeing what little memory it had blocked.
				 */
				currouteElement = orgElement;
			}
		}
		currouteElement = null;
	}
	
	
	public static void compareAndConquer(int[][] results, 
			String[] stopNames,
			int[] currstopElement, 
			int criterium,
			String currstopName,
			int startIdx) {

		// iterate through Elements in the results, starting with the startIdx
		for (int elementIdx = startIdx; elementIdx < results.length; elementIdx++) {
			/* see explanation above
			 */
			// value to compare to (frequency)
			int value = results[elementIdx][1];
			
			boolean breakcondition = false;

			/* descending -> top 5 busiest
			 * true if our current criterium value is larger than the value in results at this index
			 */
			if ((Integer.compare(currstopElement[1], value) >= 0) 
					&& Arrays.stream(stopNames).noneMatch(currstopName::equals)) {
				// store the original element
				int[] orgElement = results[elementIdx];
				String orgName = stopNames[elementIdx];

				/* if the original element has been empty, break this loop instead 
				 * of resetting the currstopElement when searching for the top5 interest
				 * thus not reordering the results (not necessary here)
				 */
				if(orgElement[1]==0) {
					breakcondition = true;
				}
				results[elementIdx] = new int[] {currstopElement[0], currstopElement[1]};
				stopNames[elementIdx] = currstopName;
				if (breakcondition) {
					break;
				}

				/* see explanation above
				 */
				currstopElement = orgElement;
				currstopName = orgName;
			}
		}
		currstopElement = null;
		currstopName = null;
	}
	
}
