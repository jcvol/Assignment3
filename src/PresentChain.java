public class PresentChain  {
    Node head;

    static class Node {
        public Present present;
        public volatile Node prev;
        public volatile Node next;

        Node(Present present) {
            this.present = present;
            prev = null;
            next = null;
        }
    }

    // checks to see if the linked list is empty
    public synchronized boolean isEmpty(){
        return (head == null);
    }

    // inserts a present (basically a node) into the chain (doubly-linked list)
    // the presents are arranged by the tag number, in increasing order.
    // this locks the present chain while a servant is inserting a gift
    public synchronized void insert(Present present) {
        Node n = new Node(present);

        if (head == null) {
            head = n;
        }else{
            Node traverse = head;
            while (traverse.next != null) {
                if (traverse.prev == null && traverse.present.tag >= present.tag){
                    n.next = traverse;
                    traverse.prev = n;
                    head = n;
                    return;
                }else if (traverse.prev != null && traverse.present.tag >= present.tag){
                    n.next = traverse;
                    n.prev = traverse.prev;
                    traverse.prev.next = n;
                    traverse.prev = n;
                    return;
                }
                traverse = traverse.next;
            }
            if (traverse.present.tag >= present.tag) {
                n.next = traverse;
                n.prev = traverse.prev;
                if (traverse.prev != null){
                    traverse.prev.next = n;
                }else{
                    head = n;
                }
                traverse.prev = n;
                return;
            }
            traverse.next = n;
            n.prev = traverse;
        }
    }

    //  the servant removes the head of the doubly-linked list, thanking the guests in order
    //  the present chain is locked while this operation happens
    public synchronized Present remove (){
        final var oldHead = head;
        if (oldHead != null){
            head = oldHead.next;
            return oldHead.present;
        }
        return null;
    }

    // the servants searching for the requested tag number from the minotaur
    // the present chain is locked while this operation happens
    public synchronized boolean searchChain(int tag){
        if (head == null) {
            return false;
        }else{
            Node traverse = head;
            while (traverse.next != null) {
                if (traverse.present.tag > tag){
                    return false;
                }else if (traverse.present.tag == tag){
                    return true;
                }
                traverse = traverse.next;
            }
            return false;
        }
    }
}
