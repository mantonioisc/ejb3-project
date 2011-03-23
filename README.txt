Sample project to demonstrate EJB 3.1 use with Java EE 6 

-------------------------------------------------------------------------------------------------------------------------------------------
Generated using maven archetype plugin
	mvn archetype:generate

-------------------------------------------------------------------------------------------------------------------------------------------	
ejb3-project
	Parent project with common dependencies
board-generator-ejb
	Stateles Session Bean example
snakes-and-ladders-ejb
	Stateful Session Bean example
dice-ejb
	Singleton Bean example
snakes-and-ladders-ear
	Project that generates an EAR file ready to be deployed with these EJBs inside: board-generator-ejb, snakes-and-ladders-ejb, dice-ejb

-------------------------------------------------------------------------------------------------------------------------------------------
To build execute at parent project directory
	mvn install
Build artifact will be in snakes-and-ladders-ear/target/snakes-and-ladders-ear.ear
Install goal is necessary since it's a multi module project.

-------------------------------------------------------------------------------------------------------------------------------------------
Original archetype generated files were tweaked. Depedencies and source encoding moved into parent pom.xml. EAR project 
added dependencies to the other EJB modules, also in <configuration> for maven-ear-plugin added <ejbModule> with EJB 
information to produce application.xml .

-------------------------------------------------------------------------------------------------------------------------------------------
Arquillian downloaded from https://repository.jboss.org/nexus/content/repositories/releases .
Arquillian version should be changed when a more stable one is release changing the property ${arquillian.version} in parent pom.xml.
ShrinkWrap downloaded as transitive dependency of Arquillian.
Dependency javaee-api version 6 added by archetype:generate was removed due to runtime classloading errors and replaced by open-ejb 
implementation as stated at http://community.jboss.org/wiki/WhatsthecauseofthisexceptionjavalangClassFormatErrorAbsentCode .

-------------------------------------------------------------------------------------------------------------------------------------------
Arquillian is a framework for testing an EJB deploying it in embed containers, as open-ejb and jboss.
ShrinkWrap is used by Archiquillian to create a jar file that contains the EJB, and then deploy the file in the embed container all in the
unit test.

-------------------------------------------------------------------------------------------------------------------------------------------
Many test containers were configured using maven profiles(default open-ejb) as explained on:
http://docs.jboss.org/arquillian/reference/latest/en-US/html_single/ section 9

-------------------------------------------------------------------------------------------------------------------------------------------
Imported into SVN repository with
	svn import ejb3-project svn://localhost/ejb3-project

-------------------------------------------------------------------------------------------------------------------------------------------
Added a project for domain classes, as a simple jar, imported by other sub-projects that needs those types.

Also the scope of openejb-core changed to provided to avoid the dependencies of OpenEJB end in the ear file. Either way the tests runs in a 
embed OpenEJB.
