pipeline{
    agent {
        label 'maven'
    }
    environment{
        OCP_PROJECT = '77c02f-dev'
        IMAGE_PROJECT = '77c02f-tools'
        IMAGE_TAG = 'latest'
        APP_SUBDOMAIN_SUFFIX = '77c02f-test'
        APP_DOMAIN = 'apps.silver.devops.gov.bc.ca'
        TAG = 'test'
        REPO_NAME = 'educ-grad-assessment-api'
        ORG = 'bcgov'
        BRANCH = 'main'
        SOURCE_REPO_URL = 'https://github.com/${ORG}/${REPO_NAME}'
        SOURCE_REPO_URL_RAW = 'https://raw.githubusercontent.com/${ORG}/${REPO_NAME}'
    }
    stages{
        stage('Deploy to TEST') {
            steps{
                script {
                    openshift.withCluster() {
                        openshift.withProject(OCP_PROJECT) {
                            echo "Applying Deployment ${REPO_NAME}-dc"
                            def dc = openshift.apply(
                                    openshift.process("-f", "${SOURCE_REPO_URL_RAW}/${BRANCH}/tools/openshift/api.dc.yaml",
                                            "REPO_NAME=${REPO_NAME}", "HOST_ROUTE=${DEV_HOST_ROUTE}")
                            ).narrow('dc')
                            timeout(10) {
                                dc.rollout().latest()
                                dc.rollout().status('--watch')
                            }
                        }
                    }
                }
            }
            post{
                success {
                    echo "${REPO_NAME} successfully deployed to TEST"
                    script {
                        openshift.withCluster() {
                            openshift.withProject(IMAGE_PROJECT) {
                                echo "Tagging image"
                                openshift.tag("${IMAGE_PROJECT}/${REPO_NAME}:latest", "${REPO_NAME}:${TAG}")
                            }
                        }
                    }
                }
                failure {
                    echo "${REPO_NAME} deployment to TEST Failed!"
                }
            }
        }
    }
}
