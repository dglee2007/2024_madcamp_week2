pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven { url  = uri("https://jitpack.io") }
        maven { url = uri("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/") }
        maven{ url = uri("https://naver.jfrog.io/artifactory/maven/") }
    }
}

rootProject.name = "2024_madcamp_week2"
include(":app")
 