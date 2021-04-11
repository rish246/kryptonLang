# krypton programming language

## Krypton is a small, dynamically typed and strongly typed programming language.

### This is under construction for now.

**Program for binary search in krypton**

```python
myAge = 20;
lo = 0;
hi = 100;
nTurns = 1;
while (lo < hi) {
  mid = (lo + hi) / 2;
  if (mid == myAge) {
    print("Your age is " + mid + ", predicted in " + nTurns + " turns");
    lo = hi;
  } else if (mid < myAge) {
    lo = mid + 1;
    nTurns = nTurns + 1;
    print("Current mid = " + mid);
  } else {
    hi = mid - 1;
    nTurns = nTurns + 1;
    print("Current mid = " + mid);
  }
}
```

![plot](./Program%20outputs/BinarySearch.png)

**Program for printing star pattern in krypton**

```python
nRows = 3;
for (iRow = 0; iRow < nRows; iRow = iRow + 1) {
  curRow = "";
  for (iCol = 0; iCol <= iRow; iCol = iCol + 1) {
    curRow = curRow + "* ";
  }
  print(curRow);
}
```

![plot](./Program%20outputs/StarPattern.png)

**Program for printing prime numbers in krypton**

```python
upperBound = 100;
print("");
for (i = 2; i <= upperBound; i = i + 1) {
  isPrimeI = true;

  # check if i is divisible by some number if yes, i is not prime else i is prime #
        
  for (j = 2; j * j <= i; j = j + 1) {
    if (i % j == 0) 
      isPrimeI = false;
  }

  if (isPrimeI) 
    print("" + i + " is a prime number");
  
}
```

![plot](./Program%20outputs/primeNumberProgram.jpg)


**Program for defining and calling not parametered functions in krypton**

```python
def factorial(n) {
    fact = 1;
         
    for(i = 1; i <= n; i = i + 1) {
        fact = fact * i;
    }
    
    print(fact);
}
```

![plot](./Program%20outputs/factorialFunctionParams.jpg)



**Program for finding nth fibonacci number using recursion**

```python
def fibonacci(n) {
    res = 1;
    if ( n < 2 ) {
        res = n;
    } 
    else {
        res = fibonacci(n-1) + fibonacci(n-2);
    }
    return res;
}
```

![plot](./Program%20outputs/fibonacciRecursive.jpg)


**function closures in krypton**

```python
def countTo(to) {

    def counter(from) {
        res = " ";
        for( i = from; i <= to; i = i + 1) {
            res = res + i + " ";
        }
        return res;
    }

    res = "";
    res = "Invalid value passed in the function";
    if (to >= 1)
        res = counter(1);


    return res;
}
```

![plot](./Program%20outputs/functionClosures.jpg)

**First class functions in krypton**

```python
def printSquares(squareFunc, n) {
    for(i = 1; i <= n; i = i + 1) {
        print("Square of " + i + " is " + squareFunc(i));
    }
}

def square(n) {
    return n * n;
}

printSquares(square, 5);
```

![plot](./Program%20outputs/firstClassFunctionsInKrypton.jpg)


**Higher order functions and currying in krypton**

```python
def prodNTimes(n) {
    def func(x) {
        res = 1;
        for(i = 1; i <= n; i = i + 1) {
            res = res * x;
        }
        return res;
    }

    return func;
}

cube = prodNTimes(3);
square = prodNTimes(2);

```

![plot](./Program%20outputs/higherOrderFuncsAndCurrying.jpg)

