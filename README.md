# krypton programming language

## Krypton is a small, dynamically typed and strongly typed programming language.

### This is under construction for now.
**Foreach Loops in krypton**

```python
def newEdge(source, destination, weight) {
    return {"source" : source, "dest" : destination, "weight" : weight};
}

def minCost_Kruskal(edgeList, nVertices) {

    def swap(lst, i, j) {
        temp = lst[i];
        lst[i] = lst[j];
        lst[j] = temp;
    }

    def sort(edgeList, func) {
        for(i = 0; i < len(edgeList); i = i + 1) {
            for(j = 1; j < len(edgeList); j = j + 1) {
                if(func(edgeList[j], edgeList[j - 1])) {
                    swap(edgeList, j, j - 1);
                }
            }
        }
    }

    def calculateMinCost() {
        isVisited = {};

        minCost = 0;
        minCostTree = [];
        for (edge in edgeList) {
            if (isVisited[edge["dest"]] == 0 || isVisited[edge["source"]] == 0) {
                minCost = minCost + edge["weight"];
                isVisited[edge["dest"]] = 1;
                isVisited[edge["source"]] = 1;
                minCostTree = minCostTree + edge;
            }
        }

        return minCost;
    }


    sort(edgeList, lambda(e1, e2) { return e1["weight"] < e2["weight"]; });

    return calculateMinCost();
}


def main() {
    e1 = newEdge(1, 3, 4);
    e2 = newEdge(3, 4, 7);
    e3 = newEdge(4, 2, 6);
    e4 = newEdge(3, 2, 5);
    e5 = newEdge(1, 2, 3);

    edgeList = [e1, e2, e3, e4, e5];
    print("Kruskal's Algorithm implementation in krypton");
    print("Minimum Cost -> " + minCost_Kruskal(edgeList, 4));

}

main();

```

![plot](./Program%20outputs/KruskalAlgo.jpg)







**Binary Search Tree In Krypton**


_Structure of BST Node_
```python
def createNode(value) {
    return {"value" : value, "left" : null, "right" : null};
}
```

_Inorder Traversal of BST_
```python
def inorderTraversal(head) {
    res = "";
    if(head != null) {
        res = res + inorderTraversal(head["left"]);
        res = res + head["value"] + ", ";
        res = res + inorderTraversal(head["right"]);
    }
    return res;
}
```

_Adding Values to a BST_
```python
def insertNode(head, node) {
    if(head == null) {
        return node;
    }

    if(node["value"] < head["value"]) {
        leftTree = insertNode(head["left"], node);
        head["left"] = leftTree;
        return head;
    }

    if(node["value"] > head["value"]) {
        rightTree = insertNode(head["right"], node);
        head["right"] = rightTree;
        return head;
    }

    return head;
}

# lets create the BST
head = insertNode(null, createNode(4));
print("Original Tree " + inorderTraversal(head));
insertNode(head, createNode(3));
insertNode(head, createNode(5));
insertNode(head, createNode(2));
insertNode(head, createNode(7));
insertNode(head, createNode(6));

print("Final Tree " + inorderTraversal(head));



```

_output_
![plot](./Program%20outputs/BST/InsertElements.jpg)

_Finding values in BST_

```python
def findValue(head, value) {
    if(head == null) {
        return false;
    }

    if(head["value"] == value) {
        return true;
    }

    if(head["value"] < value) {
        return findValue(head["right"], value);
    }

    return findValue(head["left"], value);
}

# lets create the BST
print("Our Tree : " + inorderTraversal(head));

print("Finding values in bst");
print("2 : " + findValue(head, 2));
print("3 : " + findValue(head, 3));
print("9 : " + findValue(head, 9));
print("-1 : " + findValue(head, -1));

```
_output_
![plot](./Program%20outputs/BST/FindValues.jpg)


_Delete values from BST_
```python
def deleteValue(head, value) {

    def findMax(head) {
        if(head["right"] == null) {
            return head;
        }
        return findMax(head["right"]);
    }


    def removeValues(head) {
        if(head["value"] > value) {
            LeftTree = removeValues(head["left"]);
            head["left"] = LeftTree;
            return head;
        }
        else if(head["value"] < value) {
            RightTree = removeValues(head["right"]);
            head["right"] = RightTree;
            return head;
        }
        else {
            if(head["left"] == null && head["right"] == null) {
                return null;
            }

            else if(head["right"] == null) {
                return head["left"];
            }

            else if(head["left"] == null) {
                return head["right"];
            }
            else {
                maxLeftTree = findMax(head["left"]);
                head["value"] = maxLeftTree["value"];
                head["left"] = deleteValue(head["left"], head["value"]);
                return head;
            }

            return null;

        }

    }
    doesValueExist = findValue(head, value);
    if(!doesValueExist) {
        print("Oops.. the value does not exist");
        return head;
    }

    head = removeValues(head);

    return head;
}



print("Remove values from bst");
print("Remove 4");
head = deleteValue(head, 4);
print("Final Tree");
print(inorderTraversal(head));

print("Remove 7");
head = deleteValue(head, 7);
print("Final Tree");
print(inorderTraversal(head));

print("Remove 2");
head = deleteValue(head, 2);

print("Final Tree");
print(inorderTraversal(head));



```

_output_
![plot](./Program%20outputs/BST/RemoveValues.jpg)



**Foreach Loops in krypton**

```python
def countFrequencies(lst) {

    def countHelper() {
        counts = {};
        for(value in lst) {
            counts[value] = counts[value] + 1;
        }
        return counts;
    }

    counts = countHelper();
    for(entry in counts) {
        print("Element " + entry[0] + " -> " + entry[1]);
    }
}


ourList = [1, 2, 3, 2, 1, 6, 7];
countFrequencies(ourList);

```

![plot](./Program%20outputs/countFrequency.jpg)


**Binary Tree program using objects in krypton**

```python
def inorderTraversal(head) {
    res = "";
    if(head != null) {
        res = res + inorderTraversal(head["left"]);
        res = res + head["value"] + ", ";
        res = res + inorderTraversal(head["right"]);
    }
    return res;
}

headNode = {"value" : 2, "left" : null, "right" : null};
leftNode = {"value" : 1, "left" : null, "right" : null};
rightNode = {"value" : 3, "left" : null, "right" : null};

leftLeftNode = {"value" : 4, "left" : null, "right" : null};
rightLeftNode = {"value" : 5, "left" : null, "right" : null};


# start linking
headNode["left"] = leftNode;
headNode["right"] = rightNode;

leftNode["left"] = leftLeftNode;
rightNode["left"] = rightLeftNode;
# print
print("Inorder traversal of our binary tree");
print(inorderTraversal(headNode));

```

![plot](./Program%20outputs/BinaryTree.jpg)


**Linked List using objects in krypton**

```python
def printList(head) {
    if(head != null) {
        print(head["value"]);
        printList(head["next"]);
    }
}

headNode = {"value" : 1, "next" : null};
firstNode = {"value" : 2, "next" : null};
secondNode = {"value" : 3, "next" : null};

headNode["next"] = firstNode;
firstNode["next"] = secondNode;

print("Printing Linked List");
printList(headNode);
```

![plot](./Program%20outputs/LinkedList.jpg)





**Connected Components program in krypton**

```python
def printGrid(grid, rows, cols) {
    for(row = 0; row < rows; row = row + 1) {
        printRow(grid, row, cols);
    }
}

def printRow(board, rowNum, cols) {
    rowRes = "";
    for(col = 0; col < cols; col = col + 1) {
        nComponent = board[rowNum][col];
        if(nComponent < 0) {
            nComponent = nComponent * -1;
        }

        rowRes = rowRes + "  " + nComponent + "   ";
    }
    print(rowRes);
}



###############################################################33
grid = [[1, 1, 0, 1, 1],
        [0, 1, 1, 0, 0],
        [0, 0, 0, 0, 1],
        [1, 1, 1, 0, 1],
        [0, 1, 0, 1, 0]];

nRows = 5;
nCols = 5;
def isValid(row, col) {
    return (row >= 0 && row < nRows && col >= 0 && col < nCols);
}

def dfs(row, col, count) {
    if(isValid(row, col)
    && grid[row][col] == 1)
    {
        grid[row][col] = -count;
        dfs(row+1, col, count);
        dfs(row - 1, col, count);
        dfs(row, col+1, count);
        dfs(row, col - 1, count);

    }
}
print("---------------------------------");
print("Original Grid");
printGrid(grid, nRows, nCols);

count = 0;

for(row = 0; row < nRows; row = row + 1) {
    for(col = 0; col < nCols; col = col + 1) {
        if(grid[row][col] == 1){
            count = count + 1;
            dfs(row, col, count);
        }
    }
}

print("There are total " + count + " connected components in the grid");
print("---------------------------------");
print("Final Grid");
printGrid(grid, nRows, nCols);


```

_output_
![plot](./Program%20outputs/connectedComponents.jpg)




**Depth first search path finding algorithm in krypton**

```python
def printBoard(board, rows, cols) {
    curRow = "";
    for(row = 0; row < rows; row = row + 1) {
        printRow(board, row, cols);
    }
}

def printRow(board, rowNum, cols) {
    rowRes = "";
    for(col = 0; col < cols; col = col + 1) {
        rowRes = rowRes + board[rowNum][col] + ", ";
    }
    print(rowRes);
}

board = [   [".", ".", "X", "X", "X"],
            [".", "X", ".", ".", "."],
            [".", ".", ".", ".", "."],
            [".", ".", "X", "X", "."],
            ["X", "X", ".", "X", "."]];

print("Original Board");
print("---------------");
printBoard(board, 5, 5);


# run a dfs on board
rows = 5;
cols = 5;
def isValid(row, col) {
    return (row >= 0 && row < rows && col >= 0 && col < cols);
}


def pathFinding(curRow, curCol) {

    if(curRow == (rows - 1) && curCol == (cols - 1)) {
        board[curRow][ curCol] = "#";
        return true;
    }
     else if(isValid(curRow, curCol)
          && board[curRow][curCol] != "X"
          && board[curRow][ curCol] != "#")
     {

        res = false;
        board[curRow][ curCol] = "#";
        res = res || pathFinding(curRow + 1, curCol);
        res = res || pathFinding(curRow - 1, curCol);
        res = res || pathFinding(curRow, curCol + 1);
        res = res || pathFinding(curRow, curCol - 1);
        if(!res) {
            board[curRow][curCol] = ".";
            return false;
        }

        return true;

     }

    return false;
}

foundPath = pathFinding(0, 0);
print("");
print("Final Board");
print("---------------");

if(foundPath == true) {
    print("Path exists");
}
else {
    print("Couldn't find path");
}

printBoard(board, rows, cols);


```

_output_
![plot](./Program%20outputs/dfs_pathfinding_grid_not_solved.jpg)
![plot](./Program%20outputs/dfs_pathfinding_grid.jpg)






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

**Map, filter and reduce on lists**

1. _Map_

```python
def map(func, list) {
    result = [];

    for(value in list) {
        result = result + func(value);
    }

    print("Initial list");
    print(list);

    print("FInal list");
    print(result);
}

print("Map function result");
map(lambda(x) { return x + 1; }, [1, 2, 3]);
```


2. _Filter_

```python
def filter(func, list) {
    result = [];

    for(value in list) {
        if(func(value)) {
            result = result + value;
        }
    }

    print("Initial list");
    print(list);

    print("FInal list");
    print(result);

}
print("");
print("Filter function result");
filter(lambda(x) { return x % 2 == 0; }, [1, 2, 3, 4]);
```


3. Reduce

```python
def reduce(func, list, acc) {
    result = acc;
    for(value in list) {
        result = func(result, value);
    }

    print("Initial list");
    print(list);

    print("final result");
    print(result);

}

print("");
print("Reduce function result");
reduce(lambda(acc, x) { return acc + x; }, [1, 2, 3], 0);
```

_output_
![plot](./Program%20outputs/higerOrderFunctionsCombined.jpg)


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




