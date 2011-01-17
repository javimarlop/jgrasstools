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
package org.jgrasstools.gears.modules;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.jgrasstools.gears.modules.v.transformer.FeatureTransformer;
import org.jgrasstools.gears.utils.HMTestCase;
import org.jgrasstools.gears.utils.HMTestMaps;
import org.jgrasstools.gears.utils.math.NumericsUtilities;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
/**
 * Test for the transformer module.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class TestFeatureTransformer extends HMTestCase {

    @SuppressWarnings("nls")
    public void testFeatureTransformer() throws Exception {

        SimpleFeatureCollection testFC = HMTestMaps.testFC;

        FeatureTransformer transformer = new FeatureTransformer();
        transformer.inGeodata = testFC;
        transformer.pTransX = 10.0;
        transformer.pTransY = 10.0;
        transformer.process();
        SimpleFeatureCollection outFC = transformer.outGeodata;

        FeatureIterator<SimpleFeature> inFeatureIterator = testFC.features();
        FeatureIterator<SimpleFeature> outFeatureIterator = outFC.features();
        SimpleFeature outFeature = outFeatureIterator.next();
        assertNotNull(outFeature);
        SimpleFeature inFeature = inFeatureIterator.next();

        Coordinate inCoord = ((Geometry) inFeature.getDefaultGeometry()).getCoordinate();
        Coordinate outCoord = ((Geometry) outFeature.getDefaultGeometry()).getCoordinate();

        double distance = inCoord.distance(outCoord);
        double checkDistance = NumericsUtilities.pythagoras(10, 10);
        assertEquals(distance, checkDistance, 0.001);

    }
}