/*
Trevor Marsano / tlmarsano@gmail.com
CIS 232 / Scovil
Assignment 2
 */

package cis232.a2;

import java.util.Iterator;
import weiss.util.AbstractCollection;
import weiss.util.Collection;
import weiss.util.ConcurrentModificationException;
import weiss.util.List;
import weiss.util.ListIterator;
import weiss.util.NoSuchElementException;

public class A2232TMar<AnyType extends Comparable<? super AnyType>>
        extends AbstractCollection<AnyType> implements List<AnyType> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int NOT_FOUND = -1;
    private AnyType[] theItems;
    private int theSize;
    private int modCount = 0;

    public A2232TMar() {
        this.clear();
    }

    public A2232TMar(Collection<? extends AnyType> var1) {
        this.theSize = 0;
        this.theItems = (AnyType[]) new Comparable[var1.size()];
        ++this.modCount;
        Iterator var2 = var1.iterator();

        for ( int i = 0 ; i < var1.size() ; i++)
        {
            AnyType var3 = (AnyType) var2.next();
            this.add(var3);
        }
    }

    public A2232TMar(int arrSize) {
        this.theSize = 0;
        this.theItems = (AnyType[]) new Comparable[arrSize];
        ++this.modCount;

        for ( int i = 0 ; i < arrSize ; i++)
        {
            AnyType var3 = this.theItems[i];
            this.add(var3);
        }
    }

    public int size() {
        return this.theSize;
    }

    public AnyType get(int var1) {
        if(var1 >= 0 && var1 < this.size()) {
            return this.theItems[var1];
        } else {
            throw new ArrayIndexOutOfBoundsException("Index " + var1 + "; size " + this.size());
        }
    }

    public AnyType set(int var1, AnyType var2) {
        if(var1 >= 0 && var1 < this.size()) {
            AnyType var3 = this.theItems[var1];
            this.theItems[var1] = var2;

            nullCheck();

            //sorts only the elements that need sorting
            for (int i = 0; i < this.size() ; i++)
            {
                int min = i;
                for (int j = i + 1; j < this.size() ; j++)
                    if (this.theItems[min].compareTo(this.theItems[j]) > 0)
                        min = j;

                AnyType temp = this.theItems[i];
                this.theItems[i] = this.theItems[min];
                this.theItems[min] = temp;
            }

            return var3;
        } else {
            throw new ArrayIndexOutOfBoundsException("Index " + var1 + "; size " + this.size());
        }

    }

    public boolean contains(AnyType var1) {
        return this.findPos(var1) != -1;
    }

    private int findPos(AnyType var1) {
        for(int var2 = 0; var2 < this.size(); ++var2) {
            if(var1 == null) {
                if(this.theItems[var2] == null) {
                    return var2;
                }
            } else if(var1.equals(this.theItems[var2])) {
                return var2;
            }
        }

        return -1;
    }

    public boolean add(AnyType var1) {
        if(this.theItems.length == this.size()) {
            AnyType[] var2 = this.theItems;
            this.theItems = (AnyType[]) new Comparable[this.theItems.length * 2 + 1];

            for(int var3 = 0; var3 < this.size(); ++var3) {
                this.theItems[var3] = var2[var3];
            }
        }
        this.theItems[this.theSize++] = var1;
        ++this.modCount;

        nullCheck();

        //sorts only the elements that need sorting
        for (int i = 0; i < this.size() ; i++)
        {
            int min = i;
            for (int j = i + 1; j < this.size() ; j++)
                if (this.theItems[min].compareTo(this.theItems[j]) > 0)
                    min = j;

            AnyType temp = this.theItems[i];
            this.theItems[i] = this.theItems[min];
            this.theItems[min] = temp;
        }

        return true;
    }

    public void add(int index, AnyType element) {
        if(this.theItems.length == this.size()) {
            AnyType[] var2 = this.theItems;
            this.theItems = (AnyType[]) new Comparable[this.theItems.length * 2 + 1];

            for(int var3 = 0; var3 < this.size(); ++var3) {
                this.theItems[var3] = var2[var3];
            }
        }
        this.theItems[index] = element;
        ++this.modCount;

        nullCheck();

        //sorts only the elements that need sorting
        for (int i = 0; i < this.size() ; i++) {
            int min = i;
            for (int j = i + 1; j < this.size(); j++)
                if (this.theItems[min].compareTo(this.theItems[j]) > 0)
                    min = j;

            AnyType temp = this.theItems[i];
            this.theItems[i] = this.theItems[min];
            this.theItems[min] = temp;
        }
    }



    public boolean remove(AnyType var1) {
        int var2 = this.findPos(var1);
        if(var2 == -1) {
            return false;
        } else {
            this.remove(var2);
            return true;
        }
    }

    public AnyType remove(int var1) {
        AnyType var2 = this.theItems[var1];

        for(int var3 = var1; var3 < this.size() - 1; ++var3) {
            this.theItems[var3] = this.theItems[var3 + 1];
        }
        --this.theSize;
        ++this.modCount;
        return var2;
    }

    public void clear() {
        this.theSize = 0;
        this.theItems = (AnyType[]) new Comparable[10];
        ++this.modCount;
    }

    //performs a check on the array for null elements
    //and removes them prior to sorting
    public void nullCheck()
    {
        for (int i = 0 ; i < this.size(); i++)
        {
            if (this.theItems[i] == null)
                this.remove(i);
        }
    }

    public class myResult implements Result {

        AnyType mode;
        int count = 0;

        public myResult() {
            mode = mode();
            count = count();
        }

        //returns the mode value of each instance of myResult
        public AnyType mode() {
            AnyType newMode = theItems[0];
            int count = 1;
            int count2 = 1;

            for (int i = 1; i < theItems.length; i++) {
                if (theItems[i - 1] == theItems[i]) {
                    count++;
                } else {
                    count = 1;
                }
                if (count >= count2) {
                    newMode = theItems[i];
                    count2 = count;
                }
            }
            return newMode;
        }
        //returns the frequency of the mode value of each instance of myResult
        public int count() {
            int count = 0;
             for (int i = 1; i < theItems.length; i++) {
                 if (theItems[i - 1] == theItems[i]) {
                     count++;
                 }
             }
             return count;
        }
    }

    //returns a container holding mode() and count() values
    public Result getMode(){

        return new myResult();
    }


    //modified binary search from pg. 247
    public static <AnyType extends Comparable<AnyType>> int binSearch(A2232TMar list, AnyType x)
    {
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (x.compareTo((AnyType) list.get(mid)) < 0) {
                high = mid - 1;
            }
            else if (x.compareTo((AnyType) list.get(mid)) > 0) {
                low = mid + 1;
            }
            else return mid;
        }
        return -1;
    }


    public weiss.util.Iterator<AnyType> iterator() {
        return new A2232TMar.ArrayListIterator(0);
    }

    public ListIterator<AnyType> listIterator(int var1) {
        return new A2232TMar.ArrayListIterator(var1);
    }

    private class ArrayListIterator implements ListIterator<AnyType> {
        private int current;
        private int expectedModCount;
        private boolean nextCompleted;
        private boolean prevCompleted;

        ArrayListIterator(int var2) {
            this.expectedModCount = A2232TMar.this.modCount;
            this.nextCompleted = false;
            this.prevCompleted = false;
            if(var2 >= 0 && var2 <= A2232TMar.this.size()) {
                this.current = var2;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        public boolean hasNext() {
            if(this.expectedModCount != A2232TMar.this.modCount) {
                throw new ConcurrentModificationException();
            } else {
                return this.current < A2232TMar.this.size();
            }
        }

        public boolean hasPrevious() {
            if(this.expectedModCount != A2232TMar.this.modCount) {
                throw new ConcurrentModificationException();
            } else {
                return this.current > 0;
            }
        }

        public AnyType next() {
            if(!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                this.nextCompleted = true;
                this.prevCompleted = false;
                return A2232TMar.this.theItems[this.current++];
            }
        }

        public AnyType previous() {
            if(!this.hasPrevious()) {
                throw new NoSuchElementException();
            } else {
                this.prevCompleted = true;
                this.nextCompleted = false;
                return A2232TMar.this.theItems[--this.current];
            }
        }

        public void remove() {
            if(this.expectedModCount != A2232TMar.this.modCount) {
                throw new ConcurrentModificationException();
            } else {
                if(this.nextCompleted) {
                    A2232TMar.this.remove(--this.current);
                } else {
                    if(!this.prevCompleted) {
                        throw new IllegalStateException();
                    }

                    A2232TMar.this.remove(this.current);
                }

                this.prevCompleted = this.nextCompleted = false;
                ++this.expectedModCount;
            }
        }
    }

}
