pipeline {
    agent { any 'maven:3.8.1-adoptopenjdk-11' } 
    stages {
        stage('Example Build') {
            steps {
                sh 'mvn clean install'
            }
        }
    }
}