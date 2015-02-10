Running spring batch in a JEE6 container
========================================

Sample spring batch project to allow you to run a batch job within a JBOSS EAP6.3 container.  

Aims
----

* Run batch jobs inside jboss container.
* Deploy onto JBOSS EAP6.3
* Utilise container
* Use JPA to manage entities
* Use a Postgresql datasource

Connect to:

* JBOSS transaction manager
* JNDI Datasource
* Entitymanager 


Initial Setup
-------------

Create a postgresql data source, with the JNDI name: `java:/postgresql_ds`.  Heads up - in JBOSS you can deploy the postresql 
driver like you would deploy a war file.
  

	git clone http://github.com/coder36/jbossbatch
	cd jbossbatch
	mvn clean install

Deploy .war file into jboss. 


Launching a job
---------------

Each job is invoked by calling a rest url:

curl http://localhost:8080/jbossbatch/launch/<batch_job>?jobParam=val

e.g.
http://localhost:8080/jbossbatch/launch/load_csv?fileName=classpath:data.txt


The REST entry point is `coder36.batch.BatchLauncher`.  This will return with `OK` immediately indicating that a job
has been asked to start.   (This does not mean that it has completed, or even successfully run).  

va
How it works
------------

A new spring context is spun up for each job.  This runs in a separate thread.  There is no simple or defined way to tell JBOSS to manage threads, so
I've used a SimpleAsyncTaskExecutor.  (Weblogic allows you to plug into a WorkManager).  

The context is created from the following XML by `code36.batch.EmbeddedBatch`:
		
		/baseContext.xml
		/batch.xml
		/META-INF/batch-jobs/<batch_job>.xml
		
Deliberately I've tried to follow what JSR352 does when it creates a job.
 
 
Conclusion
----------

Ideally I would have used Spring batch's implementation of JSR352 (which is part of JEE7) but I couldn't get it to work nicely with 
the JBOSS EAP6.3 (JEE6) container.  The most dangerous symptom of it not working was that it would not roll back transactions on skip events.

* Using pure spring batch, it is possible to integrate into the JBOSS container, and utilise spring batch's skip, replay, roll back mechanisms. 





 
