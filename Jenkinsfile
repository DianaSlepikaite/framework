def mvnProfile    = 'galasa-dev'

pipeline {
// Initially run on any agent
   agent any
   environment {
//Configure Maven from the maven tooling in Jenkins
      def mvnHome = tool 'Default'
      PATH = "${mvnHome}/bin:${env.PATH}"
      
//Set some defaults
      def workspace = pwd()
      def mvnGoal    = 'install'
   }
   stages {
// If it is the master branch, version 0.3.0 and master on all the other branches
      stage('set-dev') {
         when {
           environment name: 'GIT_BRANCH', value: 'origin/master'
         }
         steps {
            script {
               mvnGoal       = 'deploy sonar:sonar'
            }
         }
      }
// If the test-preprod tag,  then set as appropriate
//      stage('set-test-preprod') {
//         when {
//           environment name: 'GIT_BRANCH', value: 'origin/testpreprod'
//         }
//         steps {
//            script {
//               mvnProfile    = 'galasa-preprod'
//            }
//         }
//     }

// for debugging purposes
      stage('report') {
         steps {
            echo "Branch/Tag         : ${env.GIT_BRANCH}"
            echo "Workspace directory: ${workspace}"
            echo "Maven Goal         : ${mvnGoal}"
            echo "Maven profile      : ${mvnProfile}"
         }
      }
   
// Set up the workspace, clear the git directories and setup the manve settings.xml files
      stage('prep-workspace') { 
         steps {
            configFileProvider([configFile(fileId: '86dde059-684b-4300-b595-64e83c2dd217', targetLocation: 'settings.xml')]) {
            }
            dir('repository/dev.galasa') {
               deleteDir()
            }
            dir('repository/dev/galasa') {
               deleteDir()
            }
         }
      }
      
      stage('maven') {
         steps {
            withSonarQubeEnv('GalasaSonarQube') {
               dir('galasa-parent') {
                  sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"

                  dir('dev.galasa') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.maven.repository.spi') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.maven.repository') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.resource.management') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.metrics') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.k8s.controller') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.docker.controller') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.obr') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('galasa-demo-archetype') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.api.authentication') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.api.health') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.api.bootstrap') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.api.launcher') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.api.testcatalog') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('dev.galasa.framework.api.runs') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }

                  dir('galasautils-maven-plugin') {
                     sh "mvn --settings ${workspace}/settings.xml -Dmaven.repo.local=${workspace}/repository -P ${mvnProfile} -B -e -fae --non-recursive ${mvnGoal}"
                  }
               }
            }
         }
      }
   }
   post {
       // triggered when red sign
       failure {
           slackSend (channel: '#project-galasa-devs', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
       }
    }
}