# 1.gradle

在Gradle中，最重要的两个概念是*项目和任务*。
每一次构建都包括至少一个项目，每一个项目又包括一个或多个任务.每个build.gradle文件都代表着一个项目

初始化：项目实例会在该阶段被创建。如果一个项目有多个模块，并且每一个模块都有其对应的build.gradle文件，那么就会创建多个项目实例。
配置：在该阶段，构建脚本会被执行，并为每个项目实例创建和配置任务。
执行：在该阶段，Gradle将决定哪个任务会被执行。哪些任务被执行取决于开始该次构建的参数配置和该Gradle文件的当前目录。

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.2.3'
    }
}
```

在repositories代码块中，`JCenter库被配置为整个构建过程的依赖仓库`。JCenter是一个预配置的Maven仓库

# 2.Gradle Wrapper入门

Gradle是一个不断发展的工具，新版本可能会打破向后兼容性，而使用Gradle Wrapper可以避免这个问题，并能确保构建是可重复的。

Gradle Wrapper为微软的Windows操作系统提供了一个batch文件，为其他操作系统提供了一个shell脚本。当你运行这段脚本时，需要的Gradle版本会被自动下载（如果它还
不存在）和使用。其原理是，每个需要构建应用的开发者或自构建系统可以仅仅运行Wrapper，然后由Wrapper搞定剩余部分

你可以通过导航来到项目根目录，`在terminal上运行./gradlew -v或在命令行上运行gradlew.bat` -v来检查你的项目中的Gradle
Wrapper是否可用。

* $ gradlew tasks

打印出所有可用的任务列表。如果你添加了--all参数，那么你将获得每个任务对应依赖的详细介绍。

* gradlew assembleDebug
  为这个应用创建一个debug版本的APK。默认情况下，Gradle的Android插件会把APK保存在MyApp/app/build/outputs/apk目录下。

为了避免大量的终端输入，Gradle提供了一种快捷方式，即驼峰式缩写任务名称。例如，你可以通过运行gradlew
assDeb来执行assembleDebug，或直接 在命令行界面输入gradlew aD。

* Check：运行所有的检查，这通常意味着在一个连接的设备或模拟器上运行测试。
* Build：触发assemble和check。
* Clean：清除项目的输出。

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.2.3'
    }
}
allprojects {
    repositories {
        jcenter()
    }
}
```

repositories代码块将JCenter配置成一个仓库，在这种情况下，一个仓库意味着一系列的依赖包， 我们应用和依赖项目中可使用的一系列可下载的函数库。
dependencies代码块用于配置 `构建过程中的依赖包`。这也意味着你不能将你的应用或依赖项目所需要的依赖包包含在顶层构建文件中
每个Android模块都需要有Android插件，因为该插件可使其执行Android 相关的任务。
allprojects代码块可用来声明 `那些需要被用于所有模块的属性`。你甚至可以在allprojects中创建任务，这些任务最终会被运用到所有
模块。

* assemble：为每个构建版本创建一个APK。
* clean：删除所有的构建内容，例如APK文件。
* check：运行Lint检查，如果Lint发现一个问题，则可终止构建。
* build：同时运行assemble和check。
* connectedCheck：在连接设备或模拟器上运行测试。
* deviceCheck：一个占位任务，专为其他插件在远端设备上运行测试。
* installDebug和installRelease：在连接的设备或模拟器上安装特定版本。
* 所有的installtasks都会有相关的uninstall任务。

# 3. BuildConfig和资源

自SDK工具版本升级到17之后，构建工具都会生成一个叫作BuildConfig的类，该类包含一个按照构建类型设置值的DEBUG常量。如
果有一部分代码你只想在debugging时期运行，比如logging，那么DEBUG会非常有用。你可以通过Gradle来扩展该文件，这样在debug和
release时，就可以拥有不同的常量。

```groovy
android {
    buildTypes {
        debug {
            buildConfigField "String", "API_URL", "\"http://test.example.com/api\""
            buildConfigField "boolean", "LOG_HTTP_CALLS", "true"
        }
        release {
            buildConfigField "String", "API_URL",
                    "\"http://example.com/api\"" buildConfigField "boolean", "LOG_HTTP_CALLS", "false"
        }
    }
}
```

compile
apk
provided
testCompile
androidTestCompile

compile是默认的配置，在编译主应用时包含所有的依赖。该配置不仅会将依赖添加至类路径，还会生成对应的APK。
如果依赖使用apk配置，则该依赖只会被打包到APK，而不会添加到编译类路径。
provided配置则完全相反，其依赖不会被 打包进APK。这两个配置只适用于JAR依赖。如果试图在依赖项目中添加它们，那么将会导致错误。
testCompile和androidTestCompile配置会添加用于测试的额外依赖库。

# 4.创建构建类型

```groovy
android {
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
```

新模块的默认build.gradle文件配置了一个构建类型，叫作release。
该构建类型用于禁用清除无用的资源（通过设置minifyEnabled为false）和定义默认ProGuard配置文件的位置。让开发人员可以更直观地在他们的生产构建上使用ProGuard。

当默认的设置不够用时，我们可以很容易地创建自定义构建类型。新的构建类型只需在buildTypes代码块中新增一个对象即可。例如，下面的staging自定义构建类型：

```groovy
android {
    buildTypes {
        staging {
            applicationIdSuffix ".staging"
            versionNameSuffix "-staging"
            buildConfigField "String", "API_URL", "\"http://staging.example.com/api\""
        }
    }
}
```

此处添加了新的构建类型为staging,且该构建类型的applicationIdSuffix会自动添加后缀，versionNameSuffix同理，且添加API_URL
字段内容为http://staging.example.com/api
当使用staging类型来构建项目时，这些属性会起作用。

还可以给某个构建类型，添加依赖

```groovy
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:22.2.0'
    debugCompile 'de.mindpipe.android:android-logging-log4j:1.0.3'
}
```

给debug构建类型，添加log依赖

## 　product flavor

用来构建不同的类型版本，如付费和免费。

```groovy
android {
    productFlavors {
        red {
            applicationId 'com.gradleforandroid.red'
            versionCode 3
        }
        blue {
            applicationId 'com.gradleforandroid.blue'
            minSdkVersion 14
            versionCode 4
        }
    }
}
```

product flavor和构建类型相比有不同的属性。这是因为product
flavor是ProductFlavor类中的对象，就像存在于所有构建脚本的defaultConfi对象。这就意味着defaultConfi和你的所有product
flavors享有相同的
属性。

## 多种版本的构建
使用flavor维度是结合flavor
```groovy
android {
    flavorDimensions "color", "price"
    productFlavors {
        red {
            flavorDimension "color"
        }
        blue {
            flavorDimension "color"
        }
        free {
            flavorDimension "price"
        }
        paid {
            flavorDimension "price"
        }
    }
}
```
flavorDimensions数组定义了维度，维度的顺序非常重要。当结合两个flavor时，它们可能定义了相同的属性或资源。
在这种情况下，flavor维度数组的顺序就决定了哪个flavor配置将被覆盖。在上一个例子中，color维度覆盖了price维度。该顺序也决定了构建variant的名称
假设默认的构建配置是debug和release构建类型，那么前面例子中定义的flavor将会生成下面这些构建variant：
blueFreeDebug and blueFreeRelease
bluePaidDebug and bluePaidRelease
redFreeDebug and redFreeRelease
redPaidDebug and redPaidRelease