package net.preibisch.bigdistributor.algorithm.clustering.scripting;

public enum JobType {
	PREPARE, PROCESS;

	public static final String PRE="pre";
	public static final String PROC="proc";
	
	public static String file(JobType type) {
		switch (type) {
		case PREPARE:
			return "_prepare.sh";
		case PROCESS:
			return "_task.sh";
		default:
			System.out.println("Default name: task.sh");
			return "task.sh";
		}
	}

	public static String str(JobType type) {
		switch (type) {
		case PREPARE:
			return "pre";
		case PROCESS:
			return "proc";
		default:
			System.out.println("Default name: proc");
			return "proc";
		}
	}

	public static JobType of(String job) throws Exception {
		switch (job) {
		case "pre":
			return PREPARE;
		case "proc":
			return PROCESS;
		default:
			throw new Exception("Invalide task type");
		}
	}
}
