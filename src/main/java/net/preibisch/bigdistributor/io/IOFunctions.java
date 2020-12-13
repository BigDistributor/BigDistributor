/*-
 * #%L
 * Software for the reconstruction of multi-view microscopic acquisitions
 * like Selective Plane Illumination Microscopy (SPIM) Data.
 * %%
 * Copyright (C) 2012 - 2020 Multiview Reconstruction developers.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */
package net.preibisch.bigdistributor.io;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.SwingUtilities;


import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;
import net.imglib2.Cursor;
import net.imglib2.FinalRealInterval;
import net.imglib2.RealInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

public class IOFunctions
{
    /**
     * Never instantiate this class, it contains only static methods
     */
    protected IOFunctions() { }

    public static boolean printIJLog = true;

    public static Img< FloatType > openAs32Bit( final File file )
    {
        return openAs32Bit( file, new ArrayImgFactory< FloatType >() );
    }

    @SuppressWarnings("unchecked")
    public static ArrayImg< FloatType, ? > openAs32BitArrayImg( final File file )
    {
        return (ArrayImg< FloatType, ? >)openAs32Bit( file, new ArrayImgFactory< FloatType >() );
    }

    public static Img< FloatType > openAs32Bit( final File file, final ImgFactory< FloatType > factory )
    {
        if ( !file.exists() )
            throw new RuntimeException( "File '" + file.getAbsolutePath() + "' does not exisit." );

        final ImagePlus imp = new Opener().openImage( file.getAbsolutePath() );

        if ( imp == null )
            throw new RuntimeException( "File '" + file.getAbsolutePath() + "' coult not be opened." );

        final Img< FloatType > img;

        if ( imp.getStack().getSize() == 1 )
        {
            // 2d
            img = factory.create( new int[]{ imp.getWidth(), imp.getHeight() }, new FloatType() );
            final ImageProcessor ip = imp.getProcessor();

            final Cursor< FloatType > c = img.localizingCursor();

            while ( c.hasNext() )
            {
                c.fwd();

                final int x = c.getIntPosition( 0 );
                final int y = c.getIntPosition( 1 );

                c.get().set( ip.getf( x, y ) );
            }

        }
        else
        {
            // >2d
            img = factory.create( new int[]{ imp.getWidth(), imp.getHeight(), imp.getStack().getSize() }, new FloatType() );

            final Cursor< FloatType > c = img.localizingCursor();

            // for efficiency reasons
            final ArrayList< ImageProcessor > ips = new ArrayList< ImageProcessor >();

            for ( int z = 0; z < imp.getStack().getSize(); ++z )
                ips.add( imp.getStack().getProcessor( z + 1 ) );

            while ( c.hasNext() )
            {
                c.fwd();

                final int x = c.getIntPosition( 0 );
                final int y = c.getIntPosition( 1 );
                final int z = c.getIntPosition( 2 );

                c.get().set( ips.get( z ).getf( x, y ) );
            }
        }

        imp.close();

        return img;
    }

    public static void printlnTS() { printlnTS( "" ); }
    public static void printlnTS( final Object object) { printlnTS( object.toString() ); }
    public static void printlnTS( final String string )
    {
        println( new Date( System.currentTimeMillis() ) + ": " + string );
    }

    public static void println() { println( "" ); }
    public static void println( final Object object) { println( object.toString() ); }
    public static void println( final String string )
    {
        if ( printIJLog )
        {
            if ( SwingUtilities.isEventDispatchThread() )
                IJ.log( string );
            else
                SwingUtilities.invokeLater( () -> IJ.log( string ) );
        }
        else
            System.out.println( string );
    }

    public static void printErr() { printErr( "" ); }
    public static void printErr( final Object object) { printErr( object.toString() ); }
    public static void printErr( final String string )
    {
        if ( printIJLog )
        {
            if ( SwingUtilities.isEventDispatchThread() )
                IJ.error( string );
            else
                SwingUtilities.invokeLater( () -> IJ.error( string ) );
        }
        else
            System.err.println( string );
    }


    public static String getShortName( final String fileName )
    {
        String shortName = fileName;
        shortName = shortName.replace('\\', '/');
        while (shortName.contains("/"))
            shortName = shortName.substring(shortName.indexOf("/") + 1, shortName.length());

        return shortName;
    }

    public static String printRealInterval( final RealInterval interval )
    {
        String out = "(Interval empty)";

        if ( interval == null || interval.numDimensions() == 0 )
            return out;

        out = "[" + interval.realMin( 0 );

        for ( int i = 1; i < interval.numDimensions(); i++ )
            out += ", " + interval.realMin( i );

        out += "] -> [" + interval.realMax( 0 );

        for ( int i = 1; i < interval.numDimensions(); i++ )
            out += ", " + interval.realMax( i );

        out += "], dimensions (" + ( interval.realMax( 0 ) - interval.realMin( 0 ) );

        for ( int i = 1; i < interval.numDimensions(); i++ )
            out += ", " + ( interval.realMax( i ) - interval.realMin( i ) );

        out += ")";

        return out;
    }
}
