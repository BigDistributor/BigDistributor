package net.preibisch.distribution.plugin;

import java.util.Map.Entry;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;

public class Duplicate {
public static void main(String[] args) {
	for (Entry<String, ResourceList> dup :
        new ClassGraph().scan().getAllResources().classFilesOnly().findDuplicatePaths()) {
    System.out.println(dup.getKey());              // Classfile path
    for (Resource res : dup.getValue()) {
        System.out.println(" -> " + res.getURL()); // Resource URL, showing classpath element
    }
}
}
}
