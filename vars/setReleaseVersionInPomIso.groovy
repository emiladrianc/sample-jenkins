def call(Map args) {

	return call( args.pomPath, args.version )
}

def call( String pomPath, String aversion = null) {
	String version = aversion ?: getReleaseVersionIso( "${env.BRANCH_NAME}" )
	
	//mvn "-f ${pomPath} versions:set -DnewVersion=${version} -DgenerateBackupPoms=false"
	
	return version
}
