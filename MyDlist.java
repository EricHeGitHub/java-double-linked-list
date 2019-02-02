
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MyDlist extends DList {
	/** Constructor that creates an empty list */
	public MyDlist() {
		super();
	}

	public MyDlist(String f) throws IOException, FileNotFoundException {
		if (f.equals("stdin")) {
			// get input from the keyboard
			Scanner s = new Scanner(System.in);
			// each line is a string
			String line = s.nextLine();
			while (!line.equals("")) {
				// create a node to represent the string
				DNode n = new DNode(line, header, trailer);
				if (size == 0) {
					// for the first string, just put the string between the header and the trailer
					header.setNext(n);
					trailer.setPrev(n);
					size += 1;
					line = s.nextLine();
				} else {
					// from the second string and on, it is connected to the predecessor and successor.
					n.setPrev(trailer.getPrev());
					n.setNext(trailer);
					trailer.getPrev().setNext(n);
					trailer.setPrev(n);
					;
					line = s.nextLine();
					size += 1;
				}
			}
		} else {
			FileReader reader = new FileReader(f);
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			// Read from a file with each word consisting of a string
			while ((str = br.readLine()) != null) {
				String[] strs = str.split(" +");
				for (int i = 0; i < strs.length; i++) {
					// The same as with input from keyboard
					DNode n = new DNode(strs[i], header, trailer);
					if (size == 0) {
						header.setNext(n);
						trailer.setPrev(n);
						size += 1;
					} else {
						n.setPrev(trailer.getPrev());
						n.setNext(trailer);
						trailer.getPrev().setNext(n);
						trailer.setPrev(n);
						size += 1;
					}
				}
			}
		}

	}

	public void printList() {
		if (size == 0) {
			// Nothing to print when there is no element in the list.
			System.out.println("The Double link is empty, so there is nothing to print.");
		} else {
			// n is the first node next to header initially
			DNode n = new DNode(null, null, null);
			n = this.getFirst();
			// As header and trailer have null as their string, when n is null or the string is null
			// we can tell we have reached the end.
			while (n != null && n.getElement() != null) {
				System.out.println(n.getElement());
				// move to the next node
				n = n.getNext();
			}
		}
	}

	public static MyDlist cloneList(MyDlist u) {
		// create a new list
		MyDlist v = new MyDlist();
		// if size is 0, just return a null list.
		if (u.size == 0) {
			return v;
		} else {
			// get the first element of list u 
			DNode pointerU = u.header.getNext();
			// get the first element of list v, at the moment, it is the trailer
			// as the list v is empty.
			DNode pointerV = v.header.getNext();
			for (int i = 0; i < u.size; i++) {
				// set the pointed element of v with the same value of the element from v
				pointerV.setElement(pointerU.getElement());
				// add an empty node at the end of v
				DNode nextNode = new DNode(null, pointerV, null);
				pointerV.setNext(nextNode);
				// if these is still some elements from u then move the pointers in
				// both lists by one.
				// else the already added empty node is set as the trailor of v
				if (pointerU.getNext().getElement() != null) {
					pointerV = pointerV.getNext();
					pointerU = pointerU.getNext();
				} else {
					v.trailer = nextNode;
				}
				// add the size of v by one
				v.size++;
			}
			// return the duplicated list v
			return v;
		}
	}

	/**
	  the big Oh notation of the union method is the addition of all the
	  primitive operations where n1 represents the size of list1 and n2
	  represents the size of list2. The number of primitive action in this
	  method is 21 + 20 * n1 + 5 * n1 * n2. As all the elements in list1 and
	  list2 are distinct, the main idea of this algorithm is to exclude the
	  replicated elements from list1 after being compared with list2. Once
	  replication detected, such element is taken away from list1. When no
	  repeated elements exist, list1 is concatenated with list2 resulting in
	  the union of two lists, thus in the worst scenario, codes in the loop
	  'for (int j = 0; j < list2.size(); j++)' will be executed n1 times at
	  most when list1 is a subset of list2. We assume that n is the larger one
	  between n1 and n2. In this case, 22 + 21 * n1 + 7 * n1 * n2 < 7 * n ^ 2 +
	  21 * n + 22, with n ^ 2 being n to the power of 2. O(22 + 21 * n1 + 7 *
	  n1 * n2) = O(7 * n ^ 2 +21 * n + 22) = O(n ^ 2). In conclusion, the big O
	  notation is O(n ^ 2).
	 **/
	public static MyDlist union(MyDlist u, MyDlist v) {
		// copy the original list to avoid corruption of them.
		MyDlist list1 = cloneList(u); // 1
		MyDlist list2 = cloneList(v); // 1
		DNode n1 = list1.header; // 1
		// if list1 is empy then return list2
		if (list1.isEmpty()) { // 1
			return list2; // 1
		}
		// traverse every element in list1
		for (int i = 0; i < list1.size(); i++) { // 1 + n1 + 1 + n1
			n1 = n1.getNext(); // 2 * n1
			DNode n2 = list2.header; // n1
			// for each element in list1, traverse every element in list2
			for (int j = 0; j < list2.size(); j++) { // n1 * (1 + n2 + 1 + n2)
				n2 = n2.getNext();// n1 * n2 * 2
				// if the element in list one is duplicated, delete such element from list one and 
				// reduce the size of 
				if (n1.getElement().equals(n2.getElement())) {// n1 * n2 * 3
					n1.getPrev().setNext(n1.getNext());// n1 * 3
					n1.getNext().setPrev(n1.getPrev());// n1 * 3
					DNode tmp = n1;// n1
					// n1 goes back to the previous unreplicated element,
					// this guarentees that even if the size of list1 changes
					// the pointer will not point to null.
					n1 = n1.getPrev();// n1 * 2
					tmp.setElement(null);// n1
					tmp.setNext(null);// n1
					tmp.setPrev(null);// n1
					list1.size--; // n1
					// as the elements in one list is distinct, there is no need to 
					// further compare the rest of the list.
					break; // n1
				}
			}
		}
		// connect list1 and list2
		list1.getLast().setNext(list2.getFirst()); // 3
		list2.getFirst().setPrev(list1.getLast());// 3
		// put the trailor of list1 and header list2 to the trash
		list1.trailer.setElement(null);// 1
		list1.trailer.setNext(null); // 1
		list1.trailer.setPrev(null);// 1
		list2.header.setElement(null);// 1
		list2.header.setNext(null);// 1
		list2.header.setPrev(null);// 1
		list1.trailer = list2.trailer; // 1
		// the size of list1 now is the addition of list1 and list2.
		list1.size = list1.size + list2.size; // 1
		return list1; // 1
	}

	/**
	  Similar to union method, in intersect method, we first find the repeated
	  elements in list1 and list2 and add these replicated elements to a new
	  list and return such newly created list. As the distinction property of
	  elements in each list, the codes, as in the union method, will be
	  executed at most n1 times in the worst scenario. The number of primitive
	  operation is 9 + 14 * n1 + 7 * n1 * n2 < 9 + 14 * n + 7 * n ^ 2 where n
	  is the larger one between n1 and n2. In conclusion, the big O notation
	  for this algorithm is O(9 + 14 * n1 + 7 * n1 * n2) = O(9 + 14 * n + 7 * n
	  ^ 2 ) = O(n ^ 2)
	 **/
	public static MyDlist intersection(MyDlist u, MyDlist v) {
		// create two new lists and an intersect list for the final result
		MyDlist list1 = cloneList(u);// 1
		MyDlist list2 = cloneList(v);// 1
		MyDlist intersect = new MyDlist();// 1
		DNode n1 = list1.header;// 1
		// When list1 is empty, return empty.
		if (list1.isEmpty()) {// 1
			return null;// 1
		}
		// traverse every element in list1
		for (int i = 0; i < list1.size(); i++) {// 1 + n1 + 1 + n1
			n1 = n1.getNext(); // 2 * n1
			DNode n2 = list2.header; // n1
			//  for every element in list1, traverse every element in list2
			for (int j = 0; j < list2.size(); j++) { // n1 * (1 + n2 + 1 + n2)
				n2 = n2.getNext(); // n1 * n2 * 2
				// if the two elements are equal, put such element of list1 to intersect list.
				if (n1.getElement().equals(n2.getElement())) { // n1 * n2 *3
					DNode n = new DNode(n1.getElement(), null, null); // n1
					// attach the element to the end of the intersect list.
					intersect.addLast(n);// 6 * n1
					// as the elements in one list is distinct, there is no need to 
					// further compare the rest of the list.
					break;// n1
				}
			}
		}

		return intersect; // 1
	}

	public static void main(String[] args) throws Exception {

		System.out.println("please type some strings, one string each line and an empty line for the end of input:");
		/**
		 * Create the first doubly linked list by reading all the strings from
		 * the standard input.
		 */
		MyDlist firstList = new MyDlist("stdin");

		/** Print all elememts in firstList */
		firstList.printList();

		/**
		 * Create the second doubly linked list by reading all the strings from
		 * the file myfile that contains some strings.
		 */

		/** Replace the argument by the full path name of the text file */
		MyDlist secondList = new MyDlist(
				"/Users/Eric/Desktop/Academic Study/Data Structures and Algorithms/Assignment1/myfile.txt");

		/** Print all elememts in secondList */
		secondList.printList();

		/** Clone firstList */
		MyDlist thirdList = cloneList(firstList);

		/** Print all elements in thirdList. */
		thirdList.printList();

		/** Clone secondList */
		MyDlist fourthList = cloneList(secondList);

		/** Print all elements in fourthList. */
		fourthList.printList();

		/** Compute the union of firstList and secondList */
		MyDlist fifthList = union(firstList, secondList);

		/** Print all elements in fifthList. */
		 fifthList.printList();

		/** Compute the intersection of thirdList and sixthList */
		MyDlist sixthList = intersection(thirdList, fourthList);

		/** Print all elements in sixthList. */
		sixthList.printList();
	}

}
