pipeline {
    agent any

    tools {
        maven 'maven'
    }

    stages {
        stage('Deploy to QA') {
            steps {
                echo 'deploy to qa'
            }
        }

        stage('Run Regression Automation Tests') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    git 'https://github.com/rakeshbabuvs/Selenium-TestNG-POM-Framework.git'
                    sh '''
                        MAVEN_OPTS="--add-opens jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED" mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testrunner/test_regression.xml
                    '''
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
                        results: [[path: 'allure-results']]
                    ])
                }
            }
        }

        stage('Publish Extent Report') {
            steps {
                publishHTML([allowMissing: false,
                             alwaysLinkToLastBuild: false,
                             keepAll: true,
                             reportDir: 'reports',
                             reportFiles: 'TestExecutionReport.html',
                             reportName: 'HTML Regression Extent Report',
                             reportTitles: ''])
            }
        }
    }
}
