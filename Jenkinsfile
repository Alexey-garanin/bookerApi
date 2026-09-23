pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

environment {
        PATH = "/usr/local/bin:/opt/homebrew/bin:$PATH"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test in Docker') {
            steps {
                script {
                    sh 'docker build -t api-tests-runner:${BUILD_NUMBER} .'

                    sh 'docker run --rm api-tests-runner:${BUILD_NUMBER}'
                }
            }
        }


        /*
        [TBU] ALLURE REPORT
        stage('Publish Test Results') {
            steps {
                junit 'build/test-results/test/*.xml'
            }
        }
        */
    }

    post {
        always {
            sh 'docker image prune -f'
        }
        success {
            echo 'Tests successfully passed.'
        }
        failure {
            echo 'The tests failed.'
        }
    }
}