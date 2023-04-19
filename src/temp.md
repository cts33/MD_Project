## Hamcrest dependency

 testImplementation "org.hamcrest:hamcrest-all:$hamcrestVersion"

 reads much more like a human sentence

implementation—The dependency is available in all source sets, including the test source sets.
testImplementation—The dependency is only available in the test source set.
androidTestImplementation—The dependency is only available in the androidTest source set.


The AndroidX Test libraries include classes and methods that provide you with versions of components like Applications and Activities that are meant for tests. When you have a local test where you need simulated Android framework classes (such as an Application Context), follow these steps to properly set up AndroidX Test:

Add the AndroidX Test core and ext dependencies
Add the Robolectric Testing library dependency
Annotate the class with the AndroidJunit4 test runner
Write AndroidX Test code

    // AndroidX Test - JVM testing
testImplementation "androidx.test.ext:junit-ktx:$androidXTestExtKotlinRunnerVersion"

    testImplementation "androidx.test:core-ktx:$androidXTestCoreVersion"

 testImplementation "org.robolectric:robolectric:$robolectricVersion"


 What is AndroidX Test?
 AndroidX Test is a collection of libraries for testing. It includes classes and methods that give you versions of components like Applications and Activities, that are meant for tests. As an example, this code you wrote is an example of an AndroidX Test function for getting an application context.

 What is Robolectric?
 The simulated Android environment that AndroidX Test uses for local tests is provided by Robolectric. Robolectric is a library that creates a simulated Android environment for tests and runs faster than booting up an emulator or running on a device. Without the Robolectric dependency, you'll get this error:

What does @RunWith(AndroidJUnit4::class) do?
A test runner is a JUnit component that runs tests. Without a test runner, your tests would not run. There's a default test runner provided by JUnit that you get automatically. @RunWith swaps out that default test runner.

The AndroidJUnit4 test runner allows for AndroidX Test to run your test differently depending on whether they are instrumented or local tests.

Use InstantTaskExecutorRule
InstantTaskExecutorRule is a JUnit Rule. When you use it with the @get:Rule annotation, it causes some code in the InstantTaskExecutorRule class to be run before and after the tests (to see the exact code, you can use the keyboard shortcut Command+B to view the file).
This rule runs all Architecture Components-related background jobs in the same thread so that the test results happen synchronously, and in a repeatable order. When you write tests that include testing LiveData, use this rule
testImplementation "androidx.arch.core:core-testing:$archTestingVersion"

What is getContentIfNotHandled?

In the TO-DO app you are using a custom Event class to make LiveData represent one-time events (like navigation or a snack bar popping up). getContentIfNotHandled provides the "one-time" capability. Called the first time, it gets the content of the Event. Any additional calls to getContentIfNotHandled for the same content will return null. This is how Event data is accessed in the app code, and thus we're using it for the tests. You can learn more about events here.





