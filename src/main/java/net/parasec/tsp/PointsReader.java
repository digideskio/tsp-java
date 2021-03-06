package net.parasec.tsp;

import net.parasec.tsp.impl.Point;
import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Scanner;
import java.util.ArrayList;

/*
 * reads in simple point file. rows = points, cols = x y.
 * tsplib format can be pre-processed into this format with:
 *
 * cat tsplibformat.file |grep "^[0-9]" |awk '{print $2 " " $3}' 
 */
public final class PointsReader {
    private static final String CHARSET = "US-ASCII";
    private static final String NL = System.getProperty("line.separator");
    
    public static final Point[] read(final String tspFile) {     
	try {
	    FileInputStream fis = null;
	    FileChannel fc = null;
	    Scanner scanner = null;
	    try {
		fis = new FileInputStream(tspFile);
		fc = fis.getChannel();
		final MappedByteBuffer byteBuffer 
		    = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		final Charset charset = Charset.forName(CHARSET);
		final CharsetDecoder decoder = charset.newDecoder();
		final CharBuffer charBuffer = decoder.decode(byteBuffer);
		final ArrayList<Point> points = new ArrayList<Point>();
		scanner = new Scanner(charBuffer).useDelimiter(NL);
		while(scanner.hasNext()) {
		    final String line = scanner.next();
		    final String[] sline = line.split("\\s");
		    points.add(new Point(Double.parseDouble(sline[0]),
				         Double.parseDouble(sline[1])));
		}
		return points.toArray(new Point[]{}); 
	    } finally {
		if(scanner != null) 
		    scanner.close();
		if(fc != null ) 
		    fc.close();
		if(fis != null)
		    fis.close();
	    }
	} catch(final IOException e) {
	    System.err.println(e);
	}
	return null;
    } 

}

