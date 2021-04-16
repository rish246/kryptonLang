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

```python
def reduce(func, acc, n) {
    for(i = 1; i <= n; i = i + 1) {
        acc = func(acc, i);
    }
    return acc;
}

def prod(acc, n) {
    return acc * n;
}

def sum(acc, n) {
    return acc + n;
}

prodSixNumbers = reduce(prod, 1, 6);
sumFive = reduce(sum, 0, 5);
```

![plot](./Program%20outputs/reduceHOF.jpg)

**Lambda expression in krypton**

```python
def filter(func, n) {
    res = "";

    for(i = 1; i <=n; i = i + 1) {
        funRes = func(i);
        if(func(i)) {
            res = res + i + ", ";
        }
    }

    return res;
}

oddNumbers = filter(lambda(x) { return x % 2 != 0; }, 50);

evenFunc = lambda(x) { return x % 2 == 0; }

filter(evenFunc, 50);
```

![plot](./Program%20outputs/LambdaExpressions.jpg)

**Map, filter and reduce on lists**

_Helper function to print the lists_

```python
def printList(lst, len) {
    for(i = 0; i < len; i = i + 1) {
        print(lst[i]);
    }
}
```

1. _Map_

```python
def map(func, list, lenList) {
    result = [];

    for(i = 0; i < lenList; i = i + 1) {
        result = result + func(list[i]);
    }

    print("Initial list");
    printList(list, lenList);

    print("FInal list");
    printList(result, lenList);
}

# call #
map(lambda(x) { return x + 1; }, [1, 2, 3], 3);
```

_output_
![plot](./Program%20outputs/mapListHOF.jpg)

2. _Filter_

```python
def filter(func, list, lenList) {
    result = [];
    resultLen = 0;

    for(i = 0; i < lenList; i = i + 1) {
        if(func(list[i])) {
            result = result + list[i];
            resultLen = resultLen + 1;
        }
    }

    print("Initial list");
    printList(list, lenList);

    print("FInal list");
    printList(result, resultLen);

}

# call #
filter(lambda(x) { return x % 2 == 0; }, [1, 2, 3, 4], 4);
```

_output_
![plot](./Program%20outputs/filterListHOF.jpg)

3. Reduce

```python
def reduce(func, list, acc, lenList) {
    result = acc;
    for(i = 0; i < lenList; i = i + 1) {
        result = func(result, list[i]);
    }

    print("Initial list");
    printList(list, lenList);

    print("final result");
    print(result);

}
```

_output_
![plot](./Program%20outputs/reduceListHOF.jpg)

**Factorial DP using krypton**

```python
def factorialDP(n) {
    dp = [-1] * (n + 1);

    dp[0] = dp[1] = 1;

    for(i = 2; i <= n; i = i + 1) {
        dp[i] = i * dp[i-1];
    }

    return dp[n];
}
```

_output_
![plot](./Program%20outputs/factorialDP.jpg)

**Nth fibonacci number using dynamic programming in krypton**

```python
def fibonacci(n) {
    dp = [0] * (n + 1);

    dp[0] = dp[1] = 1;

    for(i = 2; i <= n; i = i + 1) {
        dp[i] = dp[i-1] + dp[i-2];
    }

    return dp[n];
}
```

_output_
![plot](./Program%20outputs/fibonacciDP.jpg)

**Binary search in a sorted list in krypton**

```python
def binarySearch(lst, len, key) {

    def helper(start, end) {
        res = -1;

        if(start <= end) {
            mid = start + (end - start) / 2;

            if(lst[mid] == key) {
                res = mid;
            }
            else if(lst[mid] > key) {
                res = helper(start, mid - 1);
            }
            else {
                res = helper(mid + 1, end);
            }
        }


        return res;

    }


    start = 0;
    end = len - 1;


    locationKey = helper(start, end);
    if(locationKey == -1) {
        print("Oops the key is not present in the list ");
    }
    else {
        print("Element " + key + " is present at location " + locationKey);
    }

}
```

_output_
![plot](./Program%20outputs/binarySearchList.jpg)

**Mergesort algorithm implementation in krypton**

```python
def sublist(lst, start, end) {
    res = [];

    for(i = start; i < end; i = i + 1) {
        res = res + lst[i];
    }

    return res;
}


def merge(leftList, lLen, rightList, rLen) {
    res = [];

    if(lLen == 0) {
        res = rightList;
    }
    else if(rLen == 0) {
        res = leftList;
    }
    else {
        if(leftList[0] < rightList[0]) {
            res = res + leftList[0] + merge(sublist(leftList, 1, lLen), lLen - 1, rightList, rLen);
        }
        else
        {
            res = res + rightList[0] + merge(leftList, lLen, sublist(rightList, 1, rLen), rLen - 1);
        }
    }

    return res;

}

def mergeSort(lst, len) {

    def mergeSortHelper(lst, start, end) {
        res = [];
        if(start == end) {
            res = res + lst[start];
        }
        else {
            mid = start + (end - start) / 2;

            leftSorted = mergeSortHelper(lst, start, mid);

            lLen = mid - start + 1;

            rightSorted = mergeSortHelper(lst, mid + 1, end);

            rLen = end - mid;

            res = merge(leftSorted, lLen, rightSorted, rLen);
        }

        return res;

    }


    start = 0;
    end = len - 1;
    return mergeSortHelper(lst, start, end);

}


printList(mergeSort([1, 2, 3, 2, 1, 5, 3], 7), 7);

```

_output_
![plot](./Program%20outputs/mergeSort.jpg)
