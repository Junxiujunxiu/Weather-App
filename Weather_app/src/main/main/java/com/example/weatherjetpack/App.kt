package com.example.weatherjetpack

import android.app.Application
import com.example.weatherjetpack.network.Api
import com.example.weatherjetpack.repositories.WeatherRepo
import com.example.weatherjetpack.repositories.WeatherRepoImpl
import okhttp3.OkHttpClient
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// 1.declare the retrofit instance in first singleton---->
// 2.retrieve the retrofit instance declared in the first singleton------>
// 3.use it to create the instance of Api interface-------->
//4.then we can inject this Api to other parts of application that needs it.------->ends

//the Application class is a base class for maintaining global application state.
//this class will be executed first to  initialize a third-party library, set up a
// database connection, or perform any other kind of one-time setup, doing so in the onCreate method
class App: Application() {
    //called when the application is starting
    override fun onCreate() {
        super.onCreate()
        //initializes the dependency injection framework.
        //define a couple of singletons for the retrofit instance and the API interface
        startKoin {
            // Modules are containers for declaring and organizing your dependencies.---> Dependency: Retrofit.Builder()
            // Retrofit.Builder() is considered a dependency because it represents a crucial component (the builder for
            // Retrofit instances) that your application relies on to handle network requests
            //method below it are configurations.--->(GsonConverterFactory.create()), .baseUrl(""), .build().
            //The Retrofit.Builder instance is configured to handle network requests
            modules(module {
                //declares a singleton instance of Retrofit.Builder().
                single {
                    // used for making network requests
                    Retrofit
                        .Builder()
                        //convert JSON responses into Kotlin objects and vice versa.
                        .addConverterFactory(GsonConverterFactory.create())
                        //sets the base URL for all requests made through the Retrofit instance.
                        .baseUrl("http://dataservice.accuweather.com/")
                        // After calling build, the Retrofit instance is ready to be used for making network requests.
                        .build()
                }
                //second singleton
                single{
                    // retrieves the Retrofit instance from the Koin container
                    val retrofit:Retrofit = get()
                    // uses the retrieved Retrofit instance to create an implementation of the Api interface
                   // creating an instance of the Api interface, which was defined earlier for making API requests.
                    //then we can use it to  other parts of the application that need to make API requests
                    retrofit.create(Api::class.java)
                }
                //third singleton
                //tells Koin that when something in the application asks for a WeatherRepo,
                // it should provide the instance created in the single block, which is an
                // instance of WeatherRepoImpl backed by the Api interface.
                //with the Api interface injected as a dependency
                single{
                    //retrieves an instance of the Api interface from the Koin container ---> from second singleton
                    //get() is used to obtain dependencies in a Koin module.
                    val api:Api = get()
                    //creates an instance of WeatherRepoImpl using the api
                    // instance obtained from the Koin container.
                    //pass api instance as the dependency
                    WeatherRepoImpl(api)
                    //associate an interface (WeatherRepo::class) with its implementation (WeatherRepoImpl)
                    //let it know which implementation to provide
                    //The bind statement ensures that when the application requests a
                // WeatherRepo, it receives the instance created within this single block.
                }bind WeatherRepo::class
            })
        }
    }
}


    //-----------------------------------------------knowledge list--------------------------------------------------------

        /* what is dependency? ---they are objects a specific class depends on(database, HTTP Clients, Integers, String)
       1. so for the computer, CPU, graphic card, hard drive and so forth make a computer and the computer depends on them.
       2. so these single components are computer's dependencies. if we think of this computer as a class in programming,
       3. we can create the single components in the computer class or we can create them somewhere else.
        and these component(dependency) are same in all the computer.--> so the  dependency injection provides a powerful way to
         customize, configure, and manage dependencies in your application. like different graphic card for different computer


        private val processor = Processir("Intel i7)"
        private val ram RAM(32)
        private val hardDrive = HardDrive(1024)
        private val graphicsCard = GraphicsCard("NVDIA GeForce)"

        so if i change any of these parameter, it will change for all the computers. so the dependency injection allows me
        to customize these parameters. we can even customize the lifetime of dependencies. like we don't want our
        graphic card to run all the time but we only want it to run when we run the game.

        Dependency Injection:

        By declaring Retrofit.Builder() as a dependency and using dependency injection (in this case, with Koin),
        you are centralizing the management of this dependency. This can make your code more modular, testable,
        and easier to maintain.
        */

//-----------------------------------------------Important--------------------------------------------------------

/*the code android:name = ".App"> in the manifest file means that  the App class is the application class for your Android application*
android:name=".App" indicates that the App class is the application class.
The . before App signifies that the class is in the same package as the manifest file.
 If the class is in a different package, you would provide the full package
 name like android:name="com.example.myapp.App"./
 android:name attribute: Specifies the class that will be instantiated when the application is started.
 So, in summary, this configuration is specifying that the App class is the application class for your Android application.
 */
