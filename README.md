# This is the repository for the krypton programming language

## Krypton is a small, dynamically typed and strongly typed programming language.

### This is under construction for now.

_Program for binary search in krypton_

```
myAge = 20;

lo = 0
hi = 100
nTurns = 0
while ( lo < hi ) {
    mid = (lo + hi) / 2;
    if ( mid == myAge) {
        print "Your age is " + mid + ", predicted in " + nTurns + " turns";
        lo = hi;
    } else if (mid < myAge) {
        lo = mid + 1;
        nTurns = nTurns + 1;
        print "Current mid = " + mid;
    } else {
        hi = mid - 1;
        nTurns = nTurns + 1;
        print "Current mid = " + mid;

    }

}
```
