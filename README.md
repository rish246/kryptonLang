# krypton programming language

## Krypton is a small, dynamically typed and strongly typed programming language.

### This is under construction for now.



**Complex Number OOP in krypton**

```python
class Complex {
	def init(real, imag) {
		this.real = real;
		this.imag = imag;
	}

	def mod() {
		return sqrt(this.real * this.real + this.imag * this.imag);
	}

	def add(other) {
		result_real = this.real + other.real;
		result_imag = this.imag + other.imag;
		return new Complex(result_real, result_imag);
	}
	


	def substract(other) {
		neg_other = Complex.negate(other);
		return this.add(neg_other);

	}

	def prod(other) {
		result_real = this.real * other.real - this.imag * other.imag;
		result_imag = this.real * other.imag + this.imag * other.real;
		return new Complex(result_real, result_imag);
	}

	def toString() {
		return "(" + this.real + " + i" + this.imag + ")";
	}	

	################# Static methods #######################

	def negate(complex) {
		result = new Complex(complex.real, complex.imag);
		result.real = -(result.real);
		result.imag = -(result.imag);
		return result;
	}	

};

### It's wrong ... but it will work for now ###
def sqrt(num) {
	result = 1;
	for(i = 1; i * i <= num; i = i + 1) {
		result = i;
	}
	return result;
}


def main() {
	complex1 = new Complex(3, 4);
	
	complex2 = new Complex(-1, 3);

	print("Sum");
	sumTwo = complex1.add(complex2);
	print(complex1.toString() + " + " + complex2.toString() + " = " + sumTwo.toString());


	print("Product");
	prodTwo = complex1.prod(complex2);
	print(complex2.toString() + " * " + complex2.toString() + " = " + prodTwo.toString());


	print("Substraction");
	diffTwo = complex1.substract(complex2);
	print(complex1.toString() + " - " + complex2.toString() + " = " + diffTwo.toString());


}
	

main();



```

![plot](./Program%20outputs/OOP/Complex.jpg)

**Object Oriented BST in krypton**

```python
class BSTNode {
    def init(value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    def toString() {
        result = "";
        if(this.left != null) {
            result = result + this.left.toString();
        }

        result = result + this.value + ", ";

        if(this.right != null) {
            result = result + this.right.toString();
        }
        return result;
    }   
};



class BST {
    
    def init() {
        this.head = null;
    }

    ########################### Inserting elements ######################
    def insertValue(head, value) {
        if(head == null) {
            return new BSTNode(value);
        }

        if(value < head.value) {
            leftTree = insertValue(head.left, value);
            head.left = leftTree;
        }
        else if(value > head.value) {
            rightTree = insertValue(head.right, value);
            head.right = rightTree;
        }

        return head;

    }

    def insert(value) {
        this.head = this.insertValue(this.head, value);
    }


    ########################### Finding elements elements ######################

    def findValue(head, value) {
        if(head == null) {
            return false;
        }

        if(head.value == value) {
            return true;
        }

        if(head.value < value) {
            return findValue(head.right, value);
        }
            
        return findValue(head.left, value);
        
    }

    def find(value) {
        return findValue(this.head, value);
    }


    def findMax(head) {
        if(head == null) {
            return head;
        }
        while(head.right != null) {
            head = head.right;
        }
        return head;
    }

    def deleteValue(head, value) {
        if(head == null) {
            return head;
        }

        if(head.value > value) {
            leftTree = deleteValue(head.left, value); # evaluating in what env-> What z fuck () 
            # this should have returned null
            head.left = leftTree;

            return head;
        }

        if(head.value < value) {
            rightTree = deleteValue(head.right, value);
            head.right = rightTree;
            return head;
        }

        ########### delete the value ###############
        if(head.left == null && head.right == null) {
            head = null;
        }
        else if(head.left == null) {
            head = head.right;
        }
        else if(head.right == null) {
            head = head.left;
        }
        else {
            replaceNode = findMax(head.left);
            head.value = replaceNode.value;
            head.left = deleteValue(head.left, replaceNode.value);
            return head;
        }

        return head;

    }

    ######################### Deleting elements from BST #########################
    def delete(value) {
        this.head = deleteValue(this.head, value);
    }

    def printBST() {
        if(this.head == null) {
            print("{}");
        }
        else {
            printableRepresentation = "{" + this.head.toString() + "}";
            print(printableRepresentation);
        }
    }

};



def printOptions() {
    print("1. Insert element into set");
    print("2. Find element in set");
    print("3. Delete element from set");
    print("4. Print Options again");

}

def insertAction(set) {
    element = input(int, "Enter the element to insert : ");
    set.insert(element);
}

def findAction(set) {
    element = input(int, "Enter an element to find in set: ");
    isPresent = set.find(element);
    if(isPresent) {
        print("Element " + element + " is present in the set");
    }
    else {
        print("Element " + element + " is not present in the set");
    }
}

def deleteAction(set) {
    element = input(int, "Enter an element to delete in the set: ");
    set.delete(element);
}

def alterSet(set) {
    option = input(int, "Enter an option from above : ");
    if(option == 1) {
        insertAction(set);        
    }
    else if(option == 2) {
        findAction(set);
    }
    else if(option == 3) {
        deleteAction(set);
    } 
    else if(option == 4) {
        printOptions();
    }
    else {
        return -1;
    }

    print("");
    print("Current elements in the set : \n");
    set.printBST();
}

def main() {
    set = new BST();
    playing = true;
    printOptions();

    while(playing) {
        result = alterSet(set);
        playing = (-1 != result);
    }



}

main();


```

![plot](./Program%20outputs/OOP/BST.jpg)

**Basic Tic-Tac-Toe in krytpon**

```python

def printBoard(board) {
    for(row in board) {
        print(row);
    }
}

def isSame(lst) {
    for(i = 1; i < len(lst); i = i + 1) {
        if(lst[i] != lst[i-1]) {
            return false;
        }
    }

    return len(lst) == 3;
}

def getDiagonals(board, row, col) {
    leftToRightDiagonal = [];
    [r, c] = [row, col];
    while(r < len(board) && c < len(board[0])) {
        leftToRightDiagonal = leftToRightDiagonal + board[r][c];
        r = r + 1;
        c = c + 1;
    }

    [r, c] = [row - 1, col - 1];
    while(r >= 0 && c >= 0) {
        leftToRightDiagonal = leftToRightDiagonal + board[r][c];
        r = r - 1;
        c = c - 1;
    }



    rightToLeftDiagonal = [];
    [r, c] = [row, col];
    while(r >= 0 && c < len(board[0])) {
        print("" + r + ", " + c);
        rightToLeftDiagonal = rightToLeftDiagonal + board[r][c];
        r = r - 1;
        c = c + 1;
    }

    [r, c] = [row + 1, col - 1];
    while(r < len(board) && c >= 0) {
        rightToLeftDiagonal = rightToLeftDiagonal + board[r][c];
        r = r + 1;
        c = c - 1;
    }


    return [leftToRightDiagonal, rightToLeftDiagonal];
}

def checkGameOver(board, row, col) {
    curRow = board[row];

    if(isSame(curRow)) {
        return true;
    }

    curCol = [];
    for(r = 0; r < len(board); r = r + 1) {
        curCol = curCol + board[r][col];
    }

    if(isSame(curCol)) {
        return true;
    }

    [leftToRightDiagonal, rightToLeftDiagonal] = getDiagonals(board, row, col);

    if(isSame(leftToRightDiagonal) || isSame(rightToLeftDiagonal)) {
        return true;
    }

    return false;

}


def computerMove(board, computerChar) {
    [computer_x, computer_y] = [0, 0];

    for(row = 0; row < len(board); row = row + 1) {

        for(col = 0; col < len(board); col = col + 1) {

            if(board[row][col] == ".") {

                board[row][col] = computerChar;

                [computer_x, computer_y] = [row, col];

                col = len(board);

                row = len(board);
            }
        }
    }

    return [computer_x, computer_y];
}


def playerMove(board) {
    isChosen = false;
    [player_x, player_y] = [0, 0];

    while(!isChosen) {
        player_x = input(int, "Enter row : ");

        player_y = input(int, "Enter col : ");

        if(board[player_x][player_y] == ".") {
            isChosen = true;
        }
        else {
            print("Slot already filled.. chose again");
        }
    }

    return [player_x, player_y];
}


def startGame(board, playerChar) {
    computerChar = "O";
    if(playerChar == "O") {
        computerChar = "X";
    }

    isGameOver = false;
    while(!isGameOver) {
        printBoard(board);

        [player_x, player_y] = playerMove(board);

        board[player_x][player_y] = playerChar;

        isGameOver = checkGameOver(board, player_x, player_y);

        if(isGameOver) {
            print("Player Won");
            return 1;
        }

        [computer_x, computer_y] = computerMove(board, computerChar);

        isGameOver = checkGameOver(board, computer_x, computer_y);

        if(isGameOver) {
            print("Player Lost");
            return 1;
        }

    }

}


def getPlayerChar() {

    isCharValid = false;
    playerChar = "";
    while(!isCharValid) {
        playerChar = input(str, "Enter a symbol : X or O : ");

        if(playerChar == "X" || playerChar == "O") {
            isCharValid = true;
        }
        else {
            print("Invalid Character entered.. Enter either X or O");
        }

    }

    return playerChar;
}

def main() {
    print("Lets play Tic-Tac-Toe");

    continueGame = true;
    while(continueGame) {
        board = [[".", ".", "."], [".", ".", "."], [".", ".", "."]];
        printBoard(board);

        playerChar = getPlayerChar();

        startGame(board, playerChar);

        printBoard(board);

        playAgain = input(str, "Do you want to play again.. Y or N : ");

        continueGame = (playAgain == "Y");
    }


}

main();

```

![plot](./Program%20outputs/tic-tac-toe.jpg)

**Priority Queue implementation in krypton**

```python

def swap(lst, idx1, idx2) {
    temp = lst[idx1];
    lst[idx1] = lst[idx2];
    lst[idx2] = temp;
}

def insertItem(myPriorityQueue, item) {
    def bubbleUp() {
        curIdx = len(myPriorityQueue) - 1;
        parentIdx = (curIdx - 1) / 2;

        while(parentIdx >= 0 && myPriorityQueue[parentIdx] > myPriorityQueue[curIdx]) {
            swap(myPriorityQueue, parentIdx, curIdx);
            curIdx = parentIdx;
            parentIdx = (curIdx - 1) / 2;
        }
    }


    myPriorityQueue = myPriorityQueue + item;
    bubbleUp();
    return myPriorityQueue;
}


def popItem(myPriorityQueue) {
    def popEnd() {
        newPQ = [];
        for(i = 0; i < len(myPriorityQueue) - 1; i = i + 1) {
            newPQ = newPQ + myPriorityQueue[i];
        }
        return newPQ;
    }


    def min(idx1, idx2) {

        if(myPriorityQueue[idx1] < myPriorityQueue[idx2]) {
            return [idx1, myPriorityQueue[idx1]];
        }
        return [idx2, myPriorityQueue[idx2]];
    }

    def bubbleDown() {
        firstIdx = 0;

        isBubblingDown = true;

        while(isBubblingDown) {
            leftChildIdx = 2 * firstIdx + 1;
            rightChildIdx = 2 * firstIdx + 2;

            if(leftChildIdx < len(myPriorityQueue) && rightChildIdx < len(myPriorityQueue)) {
                [minIdx, minCost] = min(leftChildIdx, rightChildIdx);
                if(myPriorityQueue[firstIdx] > minCost) {
                    swap(myPriorityQueue, firstIdx, minIdx);
                    firstIdx = minIdx;
                }
            }
            else if(leftChildIdx < len(myPriorityQueue) && myPriorityQueue[leftChildIdx] < myPriorityQueue[firstIdx]) {
                swap(myPriorityQueue, leftChildIdx, firstIdx);
                firstIdx = leftChildIdx;
            }
            else if(rightChildIdx < len(myPriorityQueue)  && myPriorityQueue[rightChildIdx] < myPriorityQueue[firstIdx]) {
                swap(myPriorityQueue, rightChildIdx, firstIdx);
                firstIdx = rightChildIdx;
            }
            else {
                isBubblingDown = false;
            }
        }
    }


    firstIdx = 0;
    lastIdx = len(myPriorityQueue) - 1;
    swap(myPriorityQueue, firstIdx, lastIdx);
    myPriorityQueue = popEnd();
    bubbleDown();
    return myPriorityQueue;

}

def main() {
    myPriorityQueue = [];

    myPriorityQueue = insertItem(myPriorityQueue, 2);
    myPriorityQueue = insertItem(myPriorityQueue, 4);
    myPriorityQueue = insertItem(myPriorityQueue, 1);
    myPriorityQueue = insertItem(myPriorityQueue, 5);
    myPriorityQueue = insertItem(myPriorityQueue, -1);
    myPriorityQueue = insertItem(myPriorityQueue, -7);
    myPriorityQueue = insertItem(myPriorityQueue, -3);
    myPriorityQueue = insertItem(myPriorityQueue, -2);



    print(myPriorityQueue);


    print("Removing Items");
    myPriorityQueue = popItem(myPriorityQueue);
    print(myPriorityQueue);

}

main();

```

![plot](./Program%20outputs/priorityQueue.jpg)

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
