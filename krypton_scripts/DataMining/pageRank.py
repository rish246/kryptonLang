# lets implement pageRank in krypt
class Page:
    nPages = 0 # static member... just like in python

    def __init__(this, name):
        this.name = name
        this.rank = 1
        this.outgoing_links = []
        this.incoming_links = []
        Page.nPages = Page.nPages + 1
    

    def add_OG_Link(this, page):
        this.outgoing_links.append(page)
        page.incoming_links.append(this)
    

    def compute_rank(this, d):
        this.rank = (1 - d)
        for page in this.incoming_links:
            additionValue = d * ((page.rank) / len(page.outgoing_links))
            this.rank = this.rank + additionValue
        
        return this.rank
    

def main():
    d = 0.5
    A = Page("A")
    B = Page("B")
    C = Page("C")

    pages = [A, B, C]

    A.add_OG_Link(C)
    A.add_OG_Link(B)
    C.add_OG_Link(A)
    B.add_OG_Link(C)



    for _ in range(100):
        cur_iter_ranks = ''
        for page in pages:
            page.compute_rank(d)
            cur_iter_ranks += str(page.rank) + ', '
            
        print(cur_iter_ranks)
        
    print('---------- Final Page Ranks ------------')
    for page in pages:
        print(page.rank)
    


main()