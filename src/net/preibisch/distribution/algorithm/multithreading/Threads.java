package net.preibisch.distribution.algorithm.multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ij.Prefs;

public class Threads
{
	public static int numThreads() { return Math.max( 1, Prefs.getThreads() ); }

	public static ExecutorService createExService() { return createExService( numThreads() ); }
	public static ExecutorService createExService( final int numThreads ) { return Executors.newFixedThreadPool( numThreads ); }
}
