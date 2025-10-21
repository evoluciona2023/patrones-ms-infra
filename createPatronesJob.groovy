pipelineJob('Patrones-MS-Build') {
  description('Build & Push de imagen Docker para patrones-ms (local con GHCR)')
  definition { cpsScm { scm { git { remote { url('https://github.com/evoluciona2023/patrones-ms.git'); credentials('ghcr-credentials') } branch('main') } } scriptPath('Jenkinsfile') } }
  triggers { scm('H/5 * * * *') }
}

pipelineJob('Patrones-MS-Deploy') {
  description('Actualiza Helm values.yaml con el tag de imagen y ArgoCD despliega (GitOps local)')
  parameters { stringParam('IMAGE_TAG', '', 'Tag de imagen a desplegar (por ej. número de build)') }
  definition { cpsScm { scm { git { remote { url('https://github.com/evoluciona2023/patrones-ms-infra.git'); credentials('github-credentials') } branch('main') } } scriptPath('Jenkinsfile') } }
}
