 package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

import org.junit.Test;

import com.stephen.crawler.Spider;

//this class is to build the maxheap and execute users' order
public class MaxHeap {
    
	//swap method to make the following swap easier 
    public void swap(ArrayList<PagesLink> A, int i, int j) {
        PagesLink t = A.get(i);
        A.set(i, A.get(j));
        A.set(j, t);
    }
    
    //maxHeapify to make the heap into a max heap
    public void maxHeapify(Heap h, int i) {
        int l, r, largest = i;
        l = h.Left(i);
        r = h.Right(i);
        
        if (l <= h.heap_size && h.A.get(l).final_score > h.A.get(i).final_score) {
            largest = l;
        }
        
        if (r <= h.heap_size && h.A.get(r).final_score > h.A.get(largest).final_score) {
            largest = r;
        }
        
        if (largest != i) {
            swap(h.A, i, largest);
            maxHeapify(h, largest);
        }
    }
    
    //build a heap and maxheapify it, make sure the heap as a max heap
    public void buildMaxHeap(Heap h) {
        h.heap_size = h.A.size() - 1;
        for (int i=(h.A.size()-1)/2; i >= 0; --i) {
            maxHeapify(h, i);
        }
    }
    
    //do heap sort, maintain the unsorted heap as maxheap
    public void heapSort(Heap h) {
        buildMaxHeap(h);
        for (int i = h.A.size()-1; i >= 1; --i) {
            swap(h.A, i, 0);
            h.heap_size -= 1;
            maxHeapify(h, 0);
        }
        //after sorting, the nodes are from small to large while we need large to small, then swap them
        Collections.reverse(h.A); 
    }
    
    //remove and return the largest key in the heap
    public PagesLink heapExtractMax(Heap h) throws Exception {
        if (h.heap_size <= 0) {
            throw new Exception("heap underflow");
        }
        PagesLink max = h.A.get(0);
        h.A.set(0, h.A.get(--h.heap_size));
//        h.heap_size -= 1;
        maxHeapify(h, 0);
        return max;
    }
    //每次执行后，都会少一个数，size变化，整个变化，导致整个 heap变掉后面操作有问题
    
    //increase the new key value to element i, the new key must be larger than the old one
    public void heapIncreaseKey(Heap h, int i, int key) throws Exception {
        if (key < h.A.get(i).final_score) {
            throw new Exception("new key is smaller than current key");
        }
        PagesLink tmp = h.A.get(i);
        tmp.final_score = key;
        h.A.set(i, tmp);
        while(i > 0 && h.A.get(h.Parent(i)).final_score < h.A.get(i).final_score) {
            swap(h.A, i, h.Parent(i));
            i = h.Parent(i);
        }
    }
    
   //insert the link into the heap 
    public void maxHeapInsert(Heap h, PagesLink link) throws Exception {
        h.heap_size += 1;
        int key = link.final_score;
        PagesLink tmp = link.cpy(link); //deep copy
        tmp.final_score = -99999999;
        h.A.add(tmp);
        heapIncreaseKey(h, h.heap_size, key);
    }
    
    //return the element of h with the largest key value
    public PagesLink heapMaximum(Heap h) {
         return h.A.get(0);
    }
    
   //get the top 20 pageslink out of the 30 links
    public ArrayList<PagesLink> getTop20(Heap h) throws Exception {
        ArrayList<PagesLink> top = new ArrayList<PagesLink>(); //the arraylist use to save the top
        Heap tmp_heap = new Heap(); //copy the heap
        tmp_heap = tmp_heap.cpy(h);
        for (int i = 0; i < 20; ++i) {
            top.add(heapExtractMax(tmp_heap)); //extract the max value from the copy link which will not influence the original one
        }
        return top;
    }
    
    //method for user to insert links and pagerank score
    public void insertNewLink(Heap h) throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input the new url: ");
        String url = scan.next();
        System.out.println("Please input the pagerank score: ");
        int score = scan.nextInt();
        while (score <= 0 || score > 400) {
    		System.out.print("please make sure you input is between 0 and 400.");
    		score = scan.nextInt();
    }
        
        PagesLink new_link = new PagesLink();
        new_link.address = url;
        new_link.final_score = score;
        maxHeapInsert(h, new_link);
    }
    
    //method for user to increase score to the link they choose
    public void increaseScore(Heap h) throws Exception{ //heap gets the parameter when defining, so we just need to call heap below
	    Scanner scan = new Scanner(System.in);
	    System.out.println("Please enter the index of url you want to choose:");
	    int idx = scan.nextInt();
	    System.out.println("Please enter the score you want to increase:");
	    int score = scan.nextInt();
	    
	    heapIncreaseKey(h, idx, score);
	}
    
    //method to print out the url of links
    public void printLinks(ArrayList<PagesLink> A) {
        for(int i = 0; i < A.size(); ++i) {
        	    int counter = i + 1;
    		    System.out.print(counter + "th link: ");
            System.out.println(A.get(i).address);
        }
        System.out.println(); //only links
    }
    
    //method to print out the url links with their pagerank scores
    public void printLinksWithScore(ArrayList <PagesLink> A) {
        for(int i = 0; i < A.size(); ++i) {
        	    int counter = i + 1;
    		    System.out.print(counter + "th link: "); 
            System.out.print(A.get(i).address);
            System.out.print(", PageRank score:");
            System.out.println(A.get(i).final_score);
        }
        System.out.println(); 
    }
    
    //method to print out the four factor scores of each link and their total scores
    public void printLinksWithFullScore(ArrayList<PagesLink> A) {
        for(int i = 0; i < A.size(); ++i) {
        	    int counter = i + 1;
        		System.out.print(counter + "th link: ");
            System.out.print(A.get(i).address);
            System.out.print(", 4 Factor scores:");
            System.out.print(A.get(i).score_0 + ",");
            System.out.print(A.get(i).score_1 + ",");
            System.out.print(A.get(i).score_2 + ",");
            System.out.print(A.get(i).score_3);
            System.out.print(", final score:");
            System.out.println(A.get(i).final_score);
        }
        System.out.println(); 
    }
    
  private String String(int i) {
		// TODO Auto-generated method stub
		return null;
	}

//java test class to get the 30 links
    @Test
    public void test() throws Exception {
        int candidate_num = 30;
        
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input the key word: ");
        String keyword = scan.next();
        System.out.println("please choose score type, 'enter' or 'random': ");
        String type = scan.next();
        while (!(type.equals("enter") || type.equals("random"))) {
            System.out.println("please choose score type, 'enter' or 'random': ");
            type = scan.next();
        }
        
        Spider spider = new Spider();
        spider.search("https://google.com/search?q=", keyword, candidate_num);
        //initialization
        Heap heap = new Heap();
        
        int n = 1; //counter
        for(String value: spider.pagesRecord){ //record the links from the spider, each value stands for one link
            PagesLink tmp = new PagesLink();
            //trimmed the links
            int start = 0;
            if (value.startsWith("https://www.google.com/search?q=")) {
            		start = 31;
            }
            else if (value.startsWith("https://")) {
        			start = 8;
            }
            int end = Math.min(start + 30, value.length());//get the minimum one from start+30 and length
            value = value.substring(start, end);
            tmp.address = value; //make the address of the tmp equals to the previous link
            //if the user choose to enter scores for each link
            if (type.equals("enter")) {
                System.out.println("Add the "+ (n++) + "th link into max heap. Link is: ");
                System.out.println(value); //print out the link
                System.out.print("please enter the first score: ");
                tmp.score_0 = scan.nextInt(); 
                while (tmp.score_0 <= 0 || tmp.score_0 > 100) {
                		System.out.print("please make sure you input is between 0 and 100.");
                		tmp.score_0 = scan.nextInt();
                }
                System.out.print("please enter the second score: ");
                tmp.score_1 = scan.nextInt(); 
                while (tmp.score_1 <= 0 || tmp.score_1 > 100) {
            		System.out.print("please make sure you input is between 0 and 100.");
            		tmp.score_1 = scan.nextInt();
                }
                System.out.print("please enter the third score: ");
                tmp.score_2 = scan.nextInt(); 
                while (tmp.score_2 <= 0 || tmp.score_2 > 100) {
            		System.out.print("please make sure you input is between 0 and 100.");
            		tmp.score_2 = scan.nextInt();
                }
                System.out.print("please enter the fourth score: ");
                tmp.score_3 = scan.nextInt(); 
                while (tmp.score_3 <= 0 || tmp.score_3 > 100) {
            		System.out.print("please make sure you input is between 0 and 100.");
            		tmp.score_3 = scan.nextInt();
                }
            }
            //if the user choose to get random score, the random score will be less than 100 for each factor
            else if (type.equals("random")) {
                tmp.score_0 = 1 + (int)(Math.random() * 100);
                tmp.score_1 = 1 + (int)(Math.random() * 100);
                tmp.score_2 = 1 + (int)(Math.random() * 100);
                tmp.score_3 = 1 + (int)(Math.random() * 100);
            } 
            tmp.final_score = tmp.score_0 + tmp.score_1 + tmp.score_2 + tmp.score_3;
            maxHeapInsert(heap, tmp); //insert the tmp into the heap
        }

        buildMaxHeap(heap); //build a new heap

        // The first part requirements
        printLinks(heap.A);
        //printLinksWithScore(heap.A); //the order of output is the order of the heap

        printLinksWithFullScore(heap.A); 
        
        PagesLink max = heapMaximum(heap); //find the largest score and print out the link
        System.out.print(max.address); 
        System.out.print(", PageRank score:");
        System.out.println(max.final_score);
        
        System.out.println();
        
        heapSort(heap); //the output is in the order with highest score to lowest score, not the order of the heap
        printLinksWithScore(heap.A);
        
        
        // The second part requirements
        buildMaxHeap(heap); //build a heap with the order of a heap
        
        ArrayList<PagesLink> top = getTop20(heap); //get the top 20 scores
        printLinksWithScore(top); //print them out
        
        insertNewLink(heap); //user inserts the new link
        top = getTop20(heap); //renew the top 20 with the new insertion
        printLinksWithScore(top);
    
        increaseScore(heap);
        top = getTop20(heap);
        printLinksWithScore(top);
        //heapIncreaseKey(heap, 0, 500);
       //top = getTop20(heap);
       //printLinksWithScore(top);

        //PagesLink maxE = heapExtractMax(heap); //get the max value
        //System.out.print(maxE.address);
        //System.out.print(", PageRank score:");
        //System.out.println(maxE.final_score);
        //user can choose to see the max value
        System.out.println("Do you need to see the Max? Answer 'yes' or 'no'");
        String flag = scan.next();
        if(flag.equals("yes")) {
          	PagesLink maxE = heapExtractMax(heap);
         	System.out.print(maxE.address);
            System.out.print(", PageRank score:");
            System.out.println(maxE.final_score);
        }
    }

private int min(int i, int length) {
	// TODO Auto-generated method stub
	return 0;
}
    
}
