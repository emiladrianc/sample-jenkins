
def call(body) {
    //  evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config

    // buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    // timeout(time: 10, unit: 'MINUTES')
    // timestamps()
    // disableConcurrentBuilds()

    body()

    properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '5', numToKeepStr: '5')), disableConcurrentBuilds()])


	 //properties([
	// 	buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '5', daysToKeepStr: '', numToKeepStr: '5')),
	// 	timeout(time: 10, unit: 'MINUTES')
	 	//timestamps()
	// 	// disableConcurrentBuilds()
     ])
	
    try {
        runPipeline config
    } catch (exc) {
		// write on slack
		throw exc;
    }
}

def runPipeline(config) {
    println "running Pipeline using config ${config}"
    println "Branch name is ${env.BRANCH_NAME}"

    // TODO: Rename variables
    RUN_UNIT_TESTS = true
	UNIT_TESTS_REPORTS_ENABLED = true
	RUN_SONAR_QUBE_ANALISYS = true
    BUILD_DISCARDER_NUMBER_TO_KEEP = '5'
	
	initConfig(config)

    node() {

        deleteDir()
		echo "Directory deleted"
		
		checkout scm
		echo "Checkout done"

        config.pomPath = getMvnPom()
		echo "Got path"
		
		stage ("CI: Build") {
            config.version = setReleaseVersionInPomIso(config)

            currentBuild.displayName = "Version $config.version"

            ciBuild(config)
        }

        stage ("CI: Tests") {

            ciTest(config)
        }
    }
}

def initConfig(config) {

    config.runUnitTests = true
    config.showUnitTestsReport = true
	config.runSonarQube = true
 
    config.runUnitTests = config.runUnitTests ?: false
    RUN_UNIT_TESTS = config.runUnitTests
    echo "RUN_UNIT_TESTS=${RUN_UNIT_TESTS}"
	
	config.showUnitTestsReport = config.showUnitTestsReport ?: false
    UNIT_TESTS_REPORTS_ENABLED = config.showUnitTestsReport
    echo "UNIT_TESTS_REPORTS_ENABLED=${UNIT_TESTS_REPORTS_ENABLED}"
	 
	config.runSonarQube = config.runSonarQube ?: false
    RUN_SONAR_QUBE_ANALISYS = config.runSonarQube
    echo "RUN_SONAR_QUBE_ANALISYS=${RUN_SONAR_QUBE_ANALISYS}"
}

def ciBuild(Map config) {
    echo "CI: Build"
}

def ciTest(Map config) {
    echo "CI: Unit test"
}