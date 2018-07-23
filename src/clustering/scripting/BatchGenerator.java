package clustering.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import clustering.MyCallBack;
import tools.Config;

public class BatchGenerator {

	public static void GenerateBatch(MyCallBack callback) {
		boolean error = false;
		File file = new File(Config.getTempFolderPath());
		String filePath = file.getAbsolutePath()+"/submit.cmd";
		int totalInputFiles = 94;
		int tasksPerJob = 10;
		int jobs = totalInputFiles / tasksPerJob;
		int restPortions = totalInputFiles % tasksPerJob;
		try (PrintWriter out = new PrintWriter(filePath)) {
		    out.println("#!/bin/bash");
		    int i=0;
		    for (i=0;i<jobs;i++){
		    	if(i==0) {
		    	out.println("qsub -N \"task_"+i+"\" -t "+i*tasksPerJob+"-"+(i+1)*tasksPerJob+" ./job.sh");
		    	}else {
		    		out.println("qsub -N \"task_"+i+"\" -t "+i*tasksPerJob+"-"+(i+1)*tasksPerJob+" -hold_jid task_"+(i-1)+" ./job.sh");
		    	}
		    	out.println("qsub -N \"prov_"+i+"\" -t "+i+" -hold_jid task_"+i+" ./provider.sh");
		    }
		    if(restPortions>0) {
		    	out.println("qsub -N \"task_"+i+"\" -t "+i*tasksPerJob+"-"+(i*tasksPerJob+restPortions)+" -hold_jid task_"+(i-1)+" ./job.sh");
		    	out.println("qsub -N \"prov_"+i+"\" -t "+i+" -hold_jid task_"+i+" ./provider.sh");
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
	BatchGenerator.GenerateBatch(new MyCallBack() {
		
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
