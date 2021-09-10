pipeline{
    agent {
        label 'maven'
    }
    environment{
        OCP_PROJECT = '77c02f-dev'
        IMAGE_PROJECT = '77c02f-tools'
        IMAGE_TAG = 'latest'
        APP_SUBDOMAIN_SUFFIX = '77c02f-test'
        REPO_NAME = 'educ-grad-assessment-api'
        APP_NAME = 'educ-grad-assessment-api'
        APP_DOMAIN = 'apps.silver.devops.gov.bc.ca'
        TAG = 'test'
        REPO_NAME = 'educ-grad-assessment-api'
        ORG = 'bcgov'
        BRANCH = 'develop/km'
        SOURCE_REPO_URL = 'https://github.com/${ORG}/${REPO_NAME}'
        SOURCE_REPO_URL_RAW = 'https://raw.githubusercontent.com/${ORG}/${REPO_NAME}'
    }
    stages{
        stage('Deploy to TEST') {
            steps{
                script {
                    openshift.withCluster() {
                        openshift.withProject(OCP_PROJECT) {
                            def dcTemplate = openshift.process('-f',
                                    '${SOURCE_REPO_URL_RAW}/${BRANCH}/tools/openshift/api.dc.yaml',
                                    "REPO_NAME=${REPO_NAME}", "NAMESPACE=${OCP_PROJECT}",
                                    "HOST_ROUTE=${REPO_NAME}-${APP_SUBDOMAIN_SUFFIX}.${APP_DOMAIN}")

                            echo "Applying Deployment ${REPO_NAME}"
                            def dc = openshift.apply(dcTemplate).narrow('dc')

                            echo "Waiting for deployment to roll out"
                            // Wait for deployments to roll out
                            timeout(10) {
                                dc.rollout().status('--watch=true')
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
                            openshift.withProject(TOOLS_NAMESPACE) {
                                echo "Tagging image"
                                openshift.tag("${TOOLS_NAMESPACE}/${REPO_NAME}:latest", "${REPO_NAME}:${TAG}")
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
