package com.joevullo.switchyard;

import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;

public class ScopeDemo {

    public void method() throws Exception {
        
        Callable<String> primary = () -> {
            Thread.sleep(200);
            return "result from primary";
        };

        Callable<String> fallback = () -> {
            Thread.sleep(50);
            return "result from fallback";
        };

        try (var scope = StructuredTaskScope.open(Joiner.<String>anySuccessfulResultOrThrow())) {
            
            scope.fork(primary);
            scope.fork(fallback);

            String firstResult = scope.join();
            System.out.println("Won the race: " + firstResult);
        }
    }
    
}
