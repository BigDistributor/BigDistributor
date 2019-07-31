package net.preibisch.distribution.algorithm.clustering.scripting;

public enum TaskType {
	PREPARE, PROCESS;

	public static String file(TaskType type) {
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

	public static String str(TaskType type) {
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

	public static TaskType of(String task) throws Exception {
		switch (task) {
		case "pre":
			return PREPARE;
		case "proc":
			return PROCESS;
			default:
				throw new Exception("Invalide task type");
		}
	}
}
