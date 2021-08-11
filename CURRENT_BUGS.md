1. When you create a function with same name as that of a state variable, it gives the access to that variable outside also.. Check what is going on
   
**eg**:
```python
class C {
    def init(x) {
        this.x = x;
    }
};

def x() {
    print("Hello");
}

c = new C(3);
print(x);

```

**expected output**
```python
something like: com.Rishabh.Expression.ClosureExpression
```

**actual output**
```python
3
```


2. Invalid instance method not reporting errors:
    **eg**:
```python
class C {
    def init(x) {
        this.x = c;
    }
};

c = new C(3);
```

**expected output**
```python
Error: Invalid identifier "c" at line number 3
```

**actual output**
```python
No error thrown
```