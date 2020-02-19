package net.preibisch.distribution.algorithm.clustering.scripting;

public enum TaskType {
	FUSION, NON_RIGID;
	
	public static String getTaskFile(TaskType task) throws RuntimeException{ 
		switch (task) {
		case FUSION:
			return "/Users/Marwan/Desktop/Task/Fusion.jar";
		case NON_RIGID:
			return "/Users/Marwan/Desktop/Task/NonRigid.jar";

		default:
			throw new RuntimeException("Invalid task");
		}
	}

}
