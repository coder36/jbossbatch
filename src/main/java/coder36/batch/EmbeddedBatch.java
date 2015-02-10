package coder36.batch;

import java.util.Properties;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.StringUtils;

public class EmbeddedBatch  {
	    
    public static void launch( final String jobName, final Properties props ) {
    	// emulate JSR352 spring batch config search...
    	String contextPaths = "batch.xml,baseContext.xml,META-INF/batch-jobs/" + jobName + ".xml";
    	
    	new SimpleAsyncTaskExecutor().execute( new Runnable() {    		
    		public void run() {
		        try( ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(StringUtils.commaDelimitedListToStringArray( contextPaths )) ) {
		        	System.out.println( props );
		            JobParameters jobParameters = new DefaultJobParametersConverter().getJobParameters(props);
		            JobLauncher jobLauncher = (JobLauncher) context.getBean( "jobLauncher" );
		            jobLauncher.run( context.getBean( Job.class ) , jobParameters);
		        }
		        catch( Exception e ) {
		        	throw new RuntimeException( e );
		        }
    		}
    	});
    }

}	
