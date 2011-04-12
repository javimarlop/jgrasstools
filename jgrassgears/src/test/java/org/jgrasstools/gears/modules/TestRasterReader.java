/*
 * This file is part of JGrasstools (http://www.jgrasstools.org)
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * JGrasstools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jgrasstools.gears.modules;

import static java.lang.Double.NaN;

import java.io.File;
import java.net.URL;

import org.geotools.coverage.grid.GridCoverage2D;
import org.jgrasstools.gears.io.rasterreader.RasterReader;
import org.jgrasstools.gears.utils.HMTestCase;
import org.jgrasstools.gears.utils.HMTestMaps;
/**
 * Test {@link RasterReader}.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class TestRasterReader extends HMTestCase {

    private String arcPath;
    private String grassPath;

    protected void setUp() throws Exception {
        URL testUrl = this.getClass().getClassLoader().getResource("dtm_test.asc");
        arcPath = new File(testUrl.toURI()).getAbsolutePath();
        testUrl = this.getClass().getClassLoader().getResource("gbovest/testcase/cell/test");
        grassPath = new File(testUrl.toURI()).getAbsolutePath();
    }

    public void testRasterReaderAll() throws Exception {

        RasterReader reader = new RasterReader();
        reader.file = arcPath;
        reader.fileNovalue = -9999.0;
        reader.geodataNovalue = Double.NaN;
        reader.process();
        GridCoverage2D readCoverage = reader.geodata;
        checkMatrixEqual(readCoverage.getRenderedImage(), HMTestMaps.mapData);

        reader = new RasterReader();
        reader.file = grassPath;
        reader.fileNovalue = -9999.0;
        reader.geodataNovalue = Double.NaN;
        reader.process();
        readCoverage = reader.geodata;
        checkMatrixEqual(readCoverage.getRenderedImage(), HMTestMaps.mapData);
    }

    public void testRasterReaderBoundsOnly() throws Exception {
        double[][] mapData = new double[][]{//
        {1000, 1000, 1200, 1250, 1300, 1350, 1450}, //
                {750, 850, 860, 900, 1000, 1200, 1250}, //
                {700, 750, 800, 850, 900, 1000, 1100}, //
                {650, 700, 750, 800, 850, 490, 450}, //
                {430, 500, 600, 700, 800, 500, 450}, //
                {700, 750, 760, 770, 850, 1000, 1150} //
        };

        double n = 5140020.0;
        double s = 5139840.0;
        double w = 1640710.0;
        double e = 1640920.0;
        double xres = 30.0;
        double yres = 30.0;
        RasterReader reader = new RasterReader();
        reader.file = arcPath;
        reader.pNorth = n;
        reader.pSouth = s;
        reader.pWest = w;
        reader.pEast = e;
        reader.pXres = xres;
        reader.pYres = yres;
        reader.process();
        GridCoverage2D readCoverage = reader.geodata;
        checkMatrixEqual(readCoverage.getRenderedImage(), mapData);

        reader = new RasterReader();
        reader.file = grassPath;
        reader.pNorth = n;
        reader.pSouth = s;
        reader.pWest = w;
        reader.pEast = e;
        reader.pXres = xres;
        reader.pYres = yres;
        reader.process();
        readCoverage = reader.geodata;

        checkMatrixEqual(readCoverage.getRenderedImage(), mapData);
    }

    public void testRasterReaderResOnly() throws Exception {
        double[][] mapData1 = new double[][]{//
        {NaN, 850.0, 900.0, 1200.0, 1500.0}, //
                {410.0, 700.0, 800.0, 490.0, 1500.0}, //
                {600.0, 750.0, 770.0, 1000.0, 1500.0}, //
                {910.0, 1001.0, 1200.0, 1300.0, 1500.0} //
        };
        double[][] mapData2 = new double[][]{//
        {800.0, 1000.0, 1200.0, 1300.0, 1450.0}, //
                {500.0, 700.0, 800.0, 900.0, 1100.0}, //
                {450.0, 430.0, 600.0, 800.0, 450.0}, //
                {600.0, 750.0, 780.0, 1000.0, 1250.0} //
        };

        double xres = 60.0;
        double yres = 60.0;
        RasterReader reader = new RasterReader();
        reader.pXres = xres;
        reader.pYres = yres;
        reader.file = arcPath;
        reader.process();
        GridCoverage2D readCoverage = reader.geodata;
        checkMatrixEqual(readCoverage.getRenderedImage(), mapData1);

        reader = new RasterReader();
        reader.pXres = xres;
        reader.pYres = yres;
        reader.file = grassPath;
        reader.process();
        readCoverage = reader.geodata;
        checkMatrixEqual(readCoverage.getRenderedImage(), mapData2);
    }

    public void testRasterReaderBoundsAndRes() throws Exception {
        double[][] mapData1 = new double[][]{//
        {1000.0, 1200.0, 1250.0, 1300.0, 1450.0}, //
                {750.0, 860.0, 900.0, 1000.0, 1250.0}, //
                {650.0, 750.0, 800.0, 850.0, 450.0}, //
                {430.0, 600.0, 700.0, 800.0, 450.0}, //
                {700.0, 760.0, 770.0, 850.0, 1150.0} //
        };
        double[][] mapData2 = new double[][]{//
        {1000.0, 1200.0, 1250.0, 1300.0, 1450.0}, //
                {750.0, 860.0, 900.0, 1000.0, 1250.0}, //
                {700.0, 800.0, 850.0, 900.0, 1100.0}, //
                {430.0, 600.0, 700.0, 800.0, 450.0}, //
                {700.0, 760.0, 770.0, 850.0, 1150.0} //
        };

        double n = 5140020.0;
        double s = 5139840.0;
        double w = 1640710.0;
        double e = 1640920.0;
        double xres = 40.0;
        double yres = 40.0;
        RasterReader reader = new RasterReader();
        reader.file = arcPath;
        reader.pNorth = n;
        reader.pSouth = s;
        reader.pWest = w;
        reader.pEast = e;
        reader.pXres = xres;
        reader.pYres = yres;
        reader.process();
        GridCoverage2D readCoverage = reader.geodata;
        checkMatrixEqual(readCoverage.getRenderedImage(), mapData1);

        reader = new RasterReader();
        reader.file = grassPath;
        reader.pNorth = n;
        reader.pSouth = s;
        reader.pWest = w;
        reader.pEast = e;
        reader.pXres = xres;
        reader.pYres = yres;
        reader.process();
        readCoverage = reader.geodata;
        checkMatrixEqual(readCoverage.getRenderedImage(), mapData2);
    }
}