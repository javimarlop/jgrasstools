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
package org.jgrasstools.gears.modules.utils.fileiterator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Initialize;
import oms3.annotations.Keywords;
import oms3.annotations.Label;
import oms3.annotations.License;
import oms3.annotations.Name;
import oms3.annotations.Out;
import oms3.annotations.Status;
import oms3.annotations.UI;

import org.geotools.referencing.CRS;
import org.jgrasstools.gears.libs.modules.JGTConstants;
import org.jgrasstools.gears.libs.modules.JGTModel;
import org.jgrasstools.gears.utils.files.FileTraversal;
import org.jgrasstools.gears.utils.files.FileUtilities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

@Description("A module that iterates over files in a folder")
@Author(name = "Silvia Franceschi, Andrea Antonello", contact = "www.hydrologis.com")
@Keywords("Iterator, File")
@Label(JGTConstants.LIST_READER)
@Status(Status.DRAFT)
@Name("fileiterator")
@License("http://www.gnu.org/licenses/gpl-3.0.html")
public class FileIterator extends JGTModel {

    @Description("The folder on which to iterate")
    @UI(JGTConstants.FOLDERIN_UI_HINT)
    @In
    public String inFolder;

    @Description("Regular expression to match the file names.")
    @In
    public String pRegex = null;

    @Description("The code defining the coordinate reference system, composed by authority and code number (ex. EPSG:4328). Applied in the case the file is missing.")
    @UI(JGTConstants.CRS_UI_HINT)
    @In
    public String pCode;

    @Description("The current file of the list of files in the folder.")
    @Out
    public String outCurrentfile = null;

    @Description("All the files that were found matching.")
    @Out
    public List<File> filesList = null;

    @Description("All the file path that were found matching.")
    @Out
    public List<String> pathsList = null;

    private int fileIndex = 0;

    private String prjWkt;

    @Initialize
    public void initProcess() {
        doProcess = true;
    }

    @Execute
    public void process() throws Exception {
        if (pCode != null) {
            CoordinateReferenceSystem crs = CRS.decode(pCode);
            prjWkt = crs.toWKT();
        }

        if (filesList == null) {
            filesList = new ArrayList<File>();
            pathsList = new ArrayList<String>();

            new FileTraversal(){
                public void onFile( final File f ) {
                    if (pRegex == null) {
                        filesList.add(f);
                        pathsList.add(f.getAbsolutePath());
                    } else {
                        if (f.getName().matches(".*" + pRegex + ".*")) { //$NON-NLS-1$//$NON-NLS-2$
                            filesList.add(f);
                            pathsList.add(f.getAbsolutePath());
                        }
                    }
                }
            }.traverse(new File(inFolder));

            if (prjWkt != null) {
                for( File file : filesList ) {
                    String nameWithoutExtention = FileUtilities.getNameWithoutExtention(file);
                    if (nameWithoutExtention != null) {
                        File prjFile = new File(file.getParentFile(), nameWithoutExtention + ".prj"); //$NON-NLS-1$
                        if (!prjFile.exists()) {
                            // create it
                            FileUtilities.writeFile(prjWkt, prjFile);
                        }
                    }
                }
            }

        }

        if (filesList.size() > fileIndex)
            outCurrentfile = filesList.get(fileIndex).getAbsolutePath();

        if (fileIndex == filesList.size() - 1) {
            doProcess = false;
        }
        fileIndex++;

    }

    /**
     * Utility to add to all found files in a given folder the prj file following the supplied epsg.
     * 
     * @param folder the folder to browse.
     * @param epsg the epsg from which to take the prj.
     * @throws Exception
     */
    public static void addPrj( String folder, String epsg ) throws Exception {
        FileIterator fiter = new FileIterator();
        fiter.inFolder = folder;
        fiter.pCode = epsg;
        fiter.process();
    }
}
