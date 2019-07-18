/**
 * Returns the iso version
 * 
 * @param branchName (optional) branch name to append
 * @param commitId (optional) the commit id to append
 * @param version ignore - only for test (to prevent from sh(..) mock
 * @return the new version
 */
String call(branchName = "none", commitId = "none", version = null) {
	if( version == null ) {
	    // first get the version string
	    version = sh(script: "date -u +\"%Y%m%d.%H%M%S\"", returnStdout: true).trim()
	}
	// TODO remove all special characters from branchName to get a version that may be parsed properly.
    if (branchName != null && branchName != "" && branchName != "none" && branchName != "null") version = version + "-" + branchName.replace('/','_')
    if (commitId != null && commitId != "" && commitId != "none" && commitId != "null") version = version + "-" + commitId

    return version;
}