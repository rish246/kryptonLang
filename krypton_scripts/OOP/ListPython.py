class Node:
    def __init__(self, val, next = None):
        self.val = val 
        self.next = next 
        

class List:
    def __init__(self):
        self.head = None 

    def add(self, val):
        if self.head is None:
            self.head = Node(val)
        else:
            temp = self.head
            while temp.next is not None:
                temp = temp.next 
            temp.next = Node(val)

    def show(self):
        temp = self.head
        while temp is not None:
            print(temp.val, sep=', ')
            temp = temp.next 

    
list = List()
for i in range(10):
    list.add(i)

list.show()