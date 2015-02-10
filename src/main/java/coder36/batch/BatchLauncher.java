package coder36.batch;

import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path( "/launch" )
@Produces({"application/json"})
public class BatchLauncher {
	
	@Path( "/{jobName}")
	@GET
	public String launch( @PathParam("jobName") String jobName, @Context UriInfo info ) throws Exception {
		Properties p = new Properties();
		
		for ( String k : info.getQueryParameters().keySet() ) {
			p.put( k, info.getQueryParameters().get(k).get(0) );
		}
		
		EmbeddedBatch.launch( jobName, p );		
		return "OK";
	}	
			
}

