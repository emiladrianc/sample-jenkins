def call() {
	String pom = 'pom.xml'
	
    return (fileExists(pom))? pom : "pom.xml"
}
