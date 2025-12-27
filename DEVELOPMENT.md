# Development

 * Put signing key in place (key.pub/sec; search for maven).
 * Log in (credentials: search for sonatype) and generate a short-lived token here: https://central.sonatype.com/usertoken
 * Run `JRELEASER_DEPLOY_MAVEN_MAVENCENTRAL_SONATYPE_USERNAME= JRELEASER_DEPLOY_MAVEN_MAVENCENTRAL_SONATYPE_PASSWORD= JRELEASER_GITHUB_TOKEN=z ./gradlew jreleaserDeploy`
