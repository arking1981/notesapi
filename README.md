# notesapi

This is a simple REST-style service, providing basic notes taking and querying functionalities. 
To download it, do

  git clone https://github.com/arking1981/notesapi.git

then navigate to the base directory of the project

cd notesapi/notes
  
and contents are as following

  -rw-r--r-- 1 root root     265 Sep 12 04:04 Readme
  drwxr-xr-x 3 root root    4096 Sep 12 04:04 src
  -rw-r--r-- 1 root root      50 Sep 12 04:41 ant_clean_deploy.sh
  -rw-r--r-- 1 root root    4230 Sep 12 04:43 build.xml
  
Two ways of publishing are available, via Tomcat Container or a Command Line Server. 

I. publish it into a tomcat server. I only tested it working in tomcat8/java8 setup
  1. edit the build.xml to reflect your install/folder/to/tomcat/server
    the first configuration item in build.xml needs to be changed as accordingly 
  
      <!-- #### Change for your system. #### -->
      <property name="tomcat.home" value="/usr/share/tomcat8" />
      
  2. run the provided script to build and deploy it
    
    sh ant_clean_deploy.sh
    it will output the progress like
    
      Buildfile: /root/notesapi/notes/build.xml

      clean:

      BUILD SUCCESSFUL
      Total time: 0 seconds
      Buildfile: /root/notesapi/notes/build.xml

      clean:

      prepare:
          [mkdir] Created dir: /root/notesapi/notes/build
          [mkdir] Created dir: /root/notesapi/notes/build/WEB-INF
          [mkdir] Created dir: /root/notesapi/notes/build/WEB-INF/classes
          [mkdir] Created dir: /root/notesapi/notes/build/WEB-INF/lib
          [mkdir] Created dir: /root/notesapi/notes/build/WEB-INF/data
           [copy] Copying 5 files to /root/notesapi/notes/build
           [copy] Copying 1 file to /root/notesapi/notes/build/WEB-INF
           [copy] Copying 7 files to /root/notesapi/notes/build/WEB-INF/lib

      compile:
          [javac] /root/notesapi/notes/build.xml:86: warning: 'includeantruntime' was not set, defaulting to          build.sysclasspath=last; set to false for repeatable builds
          [javac] Compiling 5 source files to /root/notesapi/notes/build/WEB-INF/classes

      deploy:
            [war] Building war: /root/notesapi/notes/api.war
           [copy] Copying 1 file to /usr/share/tomcat8/webapps

      BUILD SUCCESSFUL
      Total time: 1 second
      
  3. if your tomcat service is running by now, you should be able to use it using apis like get/post
    
    curl -i -H "Content-Type: application/json" -X GET  http://localhost:8080/api/notes
    curl -i -H "Content-Type: application/json" -X POST -d '{"body" : "Pick up milk!"}' http://localhost:8080/api/notes
    curl -i -H "Content-Type: application/json" -X GET  http://localhost:8080/api/notes?query=milk
  
    if tomcat is not running, use "service tomcat8 start" or according command to bring it up
    
  4. as an example, I set up a simple web server in an EC2 instances and can be accessed 
    @http://34.210.68.110:8080/api/notes/
    
=======================================================
II. to publish it using a local command line server
  In case the tomcat is not installed and there is a simple way to publish the service for development/test purpose
  1. ensure ant is installed 
  2. in this folder(notesapi/notes), run
    
    ant jar
    and it will log the progress as below:
      Buildfile: /root/notesapi/notes/build.xml

      compile4jar:
          [mkdir] Created dir: /root/notesapi/notes/dist
          [javac] Compiling 5 source files to /root/notesapi/notes/dist

      copy-dependencies:
          [mkdir] Created dir: /root/notesapi/notes/dist/lib
            [jar] Building jar: /root/notesapi/notes/dist/lib/dependencies-all.jar

      jar:
            [jar] Building jar: /root/notesapi/notes/notes.jar

      BUILD SUCCESSFUL
      Total time: 3 seconds
  3. a fat jar file will be created
    
    -rw-r--r-- 1 root root 4462201 Sep 12 14:14 notes.jar
    
  4. it is executable and to run it
    
    java -jar notes.jar
      Sep 12, 2017 2:16:21 PM com.sun.jersey.server.impl.application.WebApplicationImpl _initiate
      INFO: Initiating Jersey application, version 'Jersey: 1.10 11/02/2011 03:53 PM'
      Publishing RestfulNotesApi on http://localhost:9876/api/notes. Hit any key to stop.
    
  5. as noted, it can be accessed @http://localhost:9876/api/notes from the local machine
    press any key to stop the server
    
    
    
  
