# NestedScrollableHost
[ ![JitPack](https://img.shields.io/github/release/jd-alexander/likebutton.svg?label=jitpack) ](https://jitpack.io/#jd-alexander/likebutton)
[![Build Status](https://travis-ci.org/jd-alexander/LikeButton.svg)](https://travis-ci.org/jd-alexander/LikeButton)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-LikeButton-green.svg?style=true)](https://android-arsenal.com/details/1/3038)

This is a java implementation of ViewPager2 wrapper class to enable nested scrolling in viewpager2 (which is disabled by default unlike ViewPager)

---

# Gradle Dependency
#### Repository

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
#### Dependency

Add this to your module's `build.gradle` file:

```gradle
dependencies {
	...
	implementation 'com.github.BUSY-LOOPING:NestedScrollableHost:0.0.2'
	}
}
```

(**If the class is still not acessible, add this to your 'settings.gradle' file**):
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" } //this line is important
        jcenter() // Warning: this repository is going to shut down soon
    }
}
```

---
