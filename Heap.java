package main;

import java.util.ArrayList;
import java.util.Collections;

//this class is to define the heap
public class Heap {
    ArrayList<PagesLink> A = new ArrayList<PagesLink>();
    int heap_size = -1;
    
    //left child of a node in the heap
    public int Left(int i) {
        return 2*(i+1) - 1;
    }
    
    //right child of a node in the heap
    public int Right(int i) {
        return 2*(i+1);
    }
    
    //parent node in the heap
    public int Parent(int i) {
        return (i-1)/2;
    }
    
  //deep copy the heap in order to keep the stable of the original heap
    @SuppressWarnings("unchecked")
    public Heap cpy(Heap new_heap) { 
        this.A = (ArrayList<PagesLink>) new_heap.A.clone(); //deep copy
        this.heap_size = new_heap.heap_size;
        return this;
    }
}
