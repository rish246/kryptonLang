# This is the repository for the krypton programming language

## Krypton is a small, dynamically typed and strongly typed programming language.

### This is under construction for now.

_Program for binary search in krypton_

```java
myAge = 20;
lo = 0;
hi = 100;
nTurns = 1;
while ( lo < hi ) {
    mid = (lo + hi) / 2;
    if ( mid == myAge) {
        print ("Your age is " + mid + ", predicted in " + nTurns + " turns");
        lo = hi;
    } else if (mid < myAge) {
        lo = mid + 1;
        nTurns = nTurns + 1;
        print ("Current mid = " + mid);
    } else {
        hi = mid - 1;
        nTurns = nTurns + 1;
        print ("Current mid = " + mid);

    }
}

```


*Program for printing star pattern in krypton*
```java
nRows = 3;
iRow = 1;

while (iRow <= nRows) {
    iCol = 1;
    print(" ");

    curRow = "";

    while (iCol <= iRow) {
        curRow = curRow + "* ";
        iCol = iCol + 1;
    }

    print(curRow);
    iRow = iRow + 1;
}
```


