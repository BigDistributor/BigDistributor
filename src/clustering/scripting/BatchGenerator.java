package clustering.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import clustering.MyCallBack;
import tools.Config;

public class BatchGenerator {

	public static void GenerateBatch(int tasksPerJob ,int totalInputFiles,MyCallBack callback) {
		boolean error = false;
		File file = new File(Config.getTempFolderPath());
		String filePath = file.getAbsolutePath()+"/submit.cmd";
		int jobs = totalInputFiles / tasksPerJob;
		int restPortions = totalInputFiles % tasksPerJob;
		try (PrintWriter out = new PrintWriter(filePath)) {
		    out.println("#!/bin/bash");
		    out.println( "cd " + Config.getClusterPath());
		    int i=0;
		    for (i=0;i<jobs;i++){
		    	if(i==0) {
		    	out.println("qsub -N \"task_"+i+"\" -t "+i*tasksPerJob+"-"+(i+1)*tasksPerJob+" ./task.sh");
		    	}else {
		    		out.println("qsub -N \"task_"+i+"\" -t "+i*tasksPerJob+"-"+(i+1)*tasksPerJob+" -hold_jid task_"+(i-1)+" ./task.sh");
		    	}
		    	out.println("qsub -N \"prov_"+i+"\" -t "+i+" -hold_jid task_"+i+" -v uuid="+Config.getUUID()+" ./logProvider.sh");
		    }
		    if(restPortions>0) {
		    	out.println("qsub -N \"task_"+i+"\" -t "+i*tasksPerJob+"-"+(i*tasksPerJob+restPortions)+" -hold_jid task_"+(i-1)+" ./task.sh");
		    	out.println("qsub -N \"prov_"+i+"\" -t "+i+" -hold_jid task_"+i+" -v uuid='"+Config.getUUID()+"' ./logProvider.sh");
		    }
		} catch (FileNotFoundException e) {
			callback.onError(e.toString());
			e.printStackTrace();
			error = true;
		}
		if(!error) {
			callback.onSuccess();
		}
	}
public static void main(String[] args) {
	BatchGenerator.GenerateBatch(10,94,new MyCallBack() {
		
		@Override
		public void onSuccess() {
			System.out.println("Done!");
			
		}
		
		@Override
		public void onError(String error) {
			System.out.println(error);
			
		}
		
		@Override
		public void log(String log) {
			// TODO Auto-generated method stub
			
		}
	});
}
}
