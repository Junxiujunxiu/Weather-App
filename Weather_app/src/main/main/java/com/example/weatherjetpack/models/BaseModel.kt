package com.example.weatherjetpack.models
// represents a restricted hierarchy where all the subclasses must be declared within
// the same file where the sealed class is declared, or they must be nested inside the
// sealed class itself.
//so it is sealed class hierarchies representing different states in a system

sealed class BaseModel<out T> {
    //subclasses of the sealed class BaseModel<T>
    // provide specific implementations for the sealed class, and BaseModel<T>
    // serves as a common base class for both Success and Error.
    data class Success<T>(val data: T):BaseModel<T>()
    // This indicates that instances of BaseModel<Nothing> are not expected to produce
    // any meaningful value, reflecting a situation where normal execution is interrupted.
    data class Error(val error:String):BaseModel<Nothing>()
    //declare a singleton object named loading - object- singleton
    //and the Nothing indicates that it represents a state where no meaningful value is expected
    object Loading:BaseModel<Nothing>()

}

//with out T, i can assign a variable whose type is subtype class to a new variable.
//see the example below
/*
* sealed class baseModel<out T> {
    data class Success<T>(val data: T) : baseModel<T>()
    data class Error(val error: String) : baseModel<Nothing>()
    object Loading : baseModel<Nothing>()
}

fun main() {
    val success: baseModel.Success<String> = baseModel.Success("Data loaded")

    // Covariant assignment is allowed
    val base: baseModel<String> = success

    // You can treat 'base' as a more general 'baseModel<String>' type
    processModel(base)
}

fun processModel(model: baseModel<Any>) {
    when (model) {
        is baseModel.Success -> println("Success: ${model.data}")
        is baseModel.Error -> println("Error: ${model.error}")
        is baseModel.Loading -> println("Loading...")
    }
}*/









//----------------------------------------------knowledege----------------------

/*
<out T>--when i use invariant type it can produce(return) or consume(use as parameter)
but the both type must match and when i use covariance type, it cannot be consume(used
as a parameter)

INVARIANT EXAMPLE

invariant ---can consume and produce
 class Container<T>(val value: T) {
    fun consume(newValue: T) {
        // Some code that uses newValue
    }

    fun produce(): T {
        return value
    }
}


VOVARIANT EXAMPLE

Covariant ---can produce, cannot consume
class Container<out T>(val value: T) {
    // Error: Using T as a parameter type is not allowed
    fun consume(newValue: T) {
        // Some code that uses newValue
    }

    // OK: Producing values of type T is allowed
    fun produce(): T {
        return value
    }
}

Generic type T--------allows me to use flexible constructor paramter type, it also applies
to functions and interface and other stuff.(note, it is not a return type, but the value type

 */