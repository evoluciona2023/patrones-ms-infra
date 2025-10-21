pipeline {
    agent any

    environment {
        GIT_CREDENTIALS_ID = "github-credentials"
        IMAGE_TAG = "${env.IMAGE_TAG ?: env.BUILD_NUMBER}"
        INFRA_REPO = "https://github.com/evoluciona2023/patrones-ms-infra.git"
        CHART_PATH = "charts/patrones-ms/values.yaml"
    }

    parameters {
        string(name: 'IMAGE_TAG', defaultValue: '', description: 'Tag de imagen publicado por el job de build (por defecto BUILD_NUMBER).')
    }

    stages {
        stage('Checkout Infra') {
            steps { git branch: 'main', url: "${INFRA_REPO}", credentialsId: "${GIT_CREDENTIALS_ID}" }
        }
        stage('Update Helm values (image tag)') {
            steps { sh 'sed -i "s|tag: \".*\"|tag: \"${IMAGE_TAG}\"|g" ${CHART_PATH}' }
        }
        stage('Commit & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${GIT_CREDENTIALS_ID}", usernameVariable: 'GIT_USER', passwordVariable: 'GIT_TOKEN')]) {
                    sh 'git config user.name "jenkins-bot" && git config user.email "jenkins@bot.local" && git add ${CHART_PATH} && git commit -m "ci: bump image tag to ${IMAGE_TAG}" || echo "Nada que commitear" && git push https://${GIT_USER}:${GIT_TOKEN}@github.com/evoluciona2023/patrones-ms-infra.git HEAD:main'
                }
            }
        }
        stage('Notify') { steps { echo "✅ Tag actualizado a ${IMAGE_TAG} en values.yaml. ArgoCD sincronizará automáticamente." } }
    }
}
