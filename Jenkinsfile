
pipeline {
    agent any
    environment {
        JAVA_HOME = '/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    tools {
        maven 'maven' // Ensure 'maven' is the name of the Maven tool configured in Jenkins
    }

    stages {
        stage('Build') {
            steps {
                git 'https://github.com/jglick/simple-maven-project-with-tests.git'
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }
            post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage("Deploy to Dev") {
            steps {
                echo("Deploy to Dev")
            }
        }

        stage("Deploy to QA") {
            steps {
                echo("Deploy to QA")
            }
        }

        stage('Run Regression Automation Tests') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    git 'https://github.com/rakeshbabuvs/Selenium-TestNG-POM-Framework.git'
                    sh "mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunner/test_sanity.xml"
                }
            }
        }

        stage('Publish Allure Reports') {
            steps {
                script {
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']] // Ensure this path is correct
                    ])
                }
            }
        }

        stage('Publish Extent Report') {
            steps {
                publishHTML([allowMissing: false,
                             alwaysLinkToLastBuild: false,
                             keepAll: true,
                             reportDir: 'target/reports', // Ensure this path is correct
                             reportFiles: 'TestExecutionReport.html',
                             reportName: 'HTML Regression Extent Report',
                             reportTitles: ''])
            }
        }

        stage("Deploy to Stage") {
            steps {
                echo("Deploy to Stage")
            }
        }

        stage('Publish Sanity Extent Report') {
            steps {
                publishHTML([allowMissing: false,
                             alwaysLinkToLastBuild: false,
                             keepAll: true,
                             reportDir: 'target/reports', // Ensure this path is correct
                             reportFiles: 'TestExecutionReport.html',
                             reportName: 'HTML Sanity Extent Report',
                             reportTitles: ''])
            }
        }
    }
}

