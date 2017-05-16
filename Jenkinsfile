pipeline {
  agent any
  stages {
    stage('Git') {
      steps {
        git(url: 'https://github.com/nitrico/LastAdapter', branch: 'master')
        timestamps() {
          echo 'Message'
        }
        
      }
    }
    stage('') {
      steps {
        sh './gradlew build'
      }
    }
  }
}