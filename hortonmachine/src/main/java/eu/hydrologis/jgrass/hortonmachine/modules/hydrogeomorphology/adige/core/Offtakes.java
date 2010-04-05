/*
 * JGrass - Free Open Source Java GIS http://www.jgrass.org 
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package eu.hydrologis.jgrass.hortonmachine.modules.hydrogeomorphology.adige.core;

import java.io.PrintStream;
import java.util.HashMap;

import eu.hydrologis.jgrass.hortonmachine.libs.models.HMConstants;
import eu.hydrologis.jgrass.hortonmachine.libs.monitor.IHMProgressMonitor;

/**
 * Utility class for handling of Offtakes mappings and data retrival. 
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class Offtakes implements DischargeContributor {

    private final HashMap<String, Integer> offtakes_pfaff2idMap;
    private final HashMap<Integer, Double> offtakes_id2valuesQMap;
    private final IHMProgressMonitor out;

    /**
     * Constructor.
     * 
     * @param offtakes_pfaff2idMap {@link HashMap map} of pfafstetter numbers versus
     *                      offtakes points id.
     * @param offtakes_id2valuesQMap map of offtakes points id versus discharge value.
     * @param out {@link PrintStream} for warning handling.
     */
    public Offtakes( HashMap<String, Integer> offtakes_pfaff2idMap,
            HashMap<Integer, Double> offtakes_id2valuesQMap, IHMProgressMonitor out ) {
        this.offtakes_pfaff2idMap = offtakes_pfaff2idMap;
        this.offtakes_id2valuesQMap = offtakes_id2valuesQMap;
        this.out = out;
    }

    public Double getDischarge( String pNum, double inputDischarge ) {
        Integer damId = offtakes_pfaff2idMap.get(pNum);
        if (damId != null) {
            Double discharge = offtakes_id2valuesQMap.get(damId);
            if (discharge != null) {
                if (inputDischarge >= discharge) {
                    return inputDischarge - discharge;
                } else {
                    out
                            .errorMessage("WARNING: offtake discharge at "
                                    + pNum
                                    + " is greater than the river discharge. Offtake discharge set to 0 to continue.");
                    return inputDischarge;
                }
            }
        }
        return HMConstants.doubleNovalue;
    }

}