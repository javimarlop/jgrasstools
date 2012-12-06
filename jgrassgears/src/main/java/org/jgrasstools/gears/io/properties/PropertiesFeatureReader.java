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
package org.jgrasstools.gears.io.properties;

import java.io.File;
import java.io.IOException;

import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Keywords;
import oms3.annotations.Label;
import oms3.annotations.License;
import oms3.annotations.Out;
import oms3.annotations.Status;
import oms3.annotations.UI;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ForceCoordinateSystemFeatureReader;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.jgrasstools.gears.libs.modules.JGTConstants;
import org.jgrasstools.gears.libs.modules.JGTModel;
import org.jgrasstools.gears.libs.monitor.IJGTProgressMonitor;
import org.jgrasstools.gears.libs.monitor.LogProgressMonitor;
import org.jgrasstools.gears.utils.CrsUtilities;
import org.jgrasstools.gears.utils.features.FeatureExtender;
import org.jgrasstools.gears.utils.files.FileUtilities;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

@Description("Utility class for reading properties files to geotools featurecollections.")
@Author(name = "Andrea Antonello", contact = "http://www.hydrologis.com")
@Keywords("IO, Properties, Feature, Vector, Reading")
@Label(JGTConstants.FEATUREREADER)
@Status(Status.CERTIFIED)
@UI(JGTConstants.HIDE_UI_HINT)
@License("General Public License Version 3 (GPLv3)")
public class PropertiesFeatureReader extends JGTModel {
    @Description("The properties file.")
    @UI(JGTConstants.FILEIN_UI_HINT)
    @In
    public String file = null;

    @Description("The progress monitor.")
    @In
    public IJGTProgressMonitor pm = new LogProgressMonitor();

    @Description("The read feature collection.")
    @Out
    public SimpleFeatureCollection geodata = null;

    @Execute
    public void readFeatureCollection() throws Exception {
        if (!concatOr(geodata == null, doReset)) {
            return;
        }

        /*
         * Works on types:
         * 
         * _=id:Integer,name:String,geom:Point
         * fid1=1|jody garnett|POINT(0 0)
         * fid2=2|brent|POINT(10 10)
         * fid3=3|dave|POINT(20 20)
         * fid4=4|justin deolivera|POINT(30 30)
         */

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = null;
        try {
            File propertiesFile = new File(file);
            pm.beginTask("Reading features from properties file: " + propertiesFile.getName(), -1);
            PropertyDataStore store = new PropertyDataStore(propertiesFile.getParentFile());

            String name = FileUtilities.getNameWithoutExtention(propertiesFile);
            CoordinateReferenceSystem crs = CrsUtilities.readProjectionFile(propertiesFile.getAbsolutePath(), "properties");

            geodata = FeatureCollections.newCollection();

            Query query = new Query(name);
            reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT);
            ForceCoordinateSystemFeatureReader newReader = new ForceCoordinateSystemFeatureReader(reader, crs);
            while( newReader.hasNext() ) {
                SimpleFeature feature = newReader.next();
                geodata.add(feature);
            }
        } finally {
            pm.done();
            if (reader != null)
                reader.close();
        }
    }

    /**
     * Fast read access mode. 
     * 
     * @param path the properties file path.
     * @return the read {@link FeatureCollection}.
     * @throws IOException
     */
    public static SimpleFeatureCollection readPropertiesfile( String path ) throws Exception {

        PropertiesFeatureReader reader = new PropertiesFeatureReader();
        reader.file = path;
        reader.readFeatureCollection();

        return reader.geodata;
    }

}
