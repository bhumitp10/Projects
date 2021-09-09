/*avlt.h*/

// Name: Bhumit Patel
// Project 5 : Threaded AVL tree
// Professor: Hummel
// CS 251: Spring 2020

/* In this project, we implement avl trees, which are balanced trees. AVL trees
   insert an element in O(logN) time, they also make necessary rotations to 
   keep the tree balanced. Other functions such as range_search are also implementented
   which give all the keys that are in the inputed range of lower and upper key value.*/





#pragma once

#include <iostream>
#include <vector>

using namespace std;

template<typename KeyT, typename ValueT>
class avlt
{
private:
  struct NODE
  {
    KeyT   Key;
    ValueT Value;
    NODE*  Left;
    NODE*  Right;
    bool   isThreaded; // true => Right is a thread, false => non-threaded
    int    Height;     // height of tree rooted at this node
  };

  NODE* Root;  // pointer to root node of tree (nullptr if empty)
  int   Size;  // # of nodes in the tree (0 if empty)
  NODE* inorder; // stores the inorder state of the tree
    

public:
  //
  // default constructor:
  //
  // Creates an empty tree.
  //
  avlt()
  {
    Root = nullptr;        //intially, root is nullptr
    Size = 0;             // no element, so 0
    inorder = nullptr;    // inorder state is nullptr
  }


 /* This is a helper function that copies a tree. This function is used by copy 
     constructor and = operator. It traveres the tree that is copied in the 
     preOrder way. 
     */
  NODE* preOrder(NODE* copyNode, NODE* parent) 
  {
    
     if(copyNode == nullptr) // copying a null node
     {
        return nullptr;
     }
      
     NODE* newNode = new NODE; //create node
     newNode->Key = copyNode->Key; // copy key
     newNode->Value = copyNode->Value; //copy value
     newNode->isThreaded = copyNode->isThreaded; //copy Threaded state
     newNode->Height = copyNode->Height;
     
     
     newNode->Left = preOrder(copyNode->Left,newNode); // recursively calls preOrder
     // return NODE will be set to newNode's Left
     // Since the node created by the call will be to left of newNode, 
     // we pass newNode in parent parameter
                                                         
                                                         
     if(!newNode->isThreaded) // check if NODE is threaded
     {
         // not threaded, so other nodes to the right exist, call preOrder recursively
         // since the NODE is created to the Right, we pass newNode's parent node
         
         newNode->Right = preOrder(copyNode->Right,parent);
     }
     else
     {
         // no nodes to the right exist,so point Right to parent
         newNode->Right = parent;
     }
      
     return newNode;
  }




  //
  // copy constructor
  //
  // NOTE: makes an exact copy of the "other" tree, such that making the
  // copy requires no rotations.
  //
  avlt (const avlt& other)
  {
    //
    // TODO
    //
      
      this->Root = preOrder(other.Root,nullptr); // copies tree 'other'
      this->Size = other.Size; // copies other's size
    
    //  copy inorder internal state of other tree
      if(other.inorder == nullptr) // inorder state has not be set
      {
          this->inorder = nullptr; // set state to null
      }
      else  //find inorder node in newly created tree 'this', same interal node in 'other'
      {  
          this->inorder = this->Root; // set inorder to root node
         
          //while node is not found in
          
          while((other.inorder)->Key != this->inorder->Key )  
          {
              // greater,go to lesser key,so left
                       
              if((this->inorder)->Key > (other.inorder)->Key)
              {
                  this->inorder = this->inorder->Left; //advance to left node
              }
              else if(!this->inorder->isThreaded) //is not threaded
              {
                  this->inorder = this->inorder->Right; // advance to right node
              }
          }
      
      }
      
  }



/* Helper Function designed to  delete node. Traverses the tree in postorder way,
   and deletes the allocated nodes. Used by destructor and clear function. */
 
 void postTraverse(NODE* cur) 
   {
        if(cur == nullptr) return; // base case, cannot delete null node, return
        
        postTraverse(cur->Left); //recursive call, pass left node of cur
        
        if(!cur->isThreaded) //not threaded
        {
            postTraverse(cur->Right); //recursive call, pass right node of cur
        }
        
        delete cur; //delete node
   }




	//
  // destructor:
  //
  // Called automatically by system when tree is about to be destroyed;
  // this is our last chance to free any resources / memory used by
  // this tree.
  //
  virtual ~avlt()
  {
    //
    // TODO
    //
    postTraverse(Root);   //call to postTraverse, deletes allocated memory for the tree
  }

  //
  // operator=
  //
  // Clears "this" tree and then makes a copy of the "other" tree.
  //
  // NOTE: makes an exact copy of the "other" tree, such that making the
  // copy requires no rotations.
  //
  avlt& operator=(const avlt& other)
  {
    //
    // TODO:
    //
    
      this->clear(); // calls clear
      
      //calls helper function, which copies the node of other and returns root node
      this->Root = preOrder(other.Root,nullptr); 
      
      this->Size = other.Size; //copy size of other
      
      
      // copy internal state of other
      if(other.inorder == nullptr)  //inorder has not been set by other
      {
          this->inorder = nullptr; //set inorder state to null
      }
      else //find inorder node in newly created tree 'this', same as the interal node in 'other'
      {
          this->inorder = this->Root; // set inorder to Root node
          
          //while node is not found in
          while((other.inorder)->Key != (this->inorder)->Key )
          {
              // greater,go to lesser key,so left
              if((this->inorder)->Key > (other.inorder)->Key)
              {
                  this->inorder = (this->inorder)->Left; // advance to left node
              }
              else if (!this->inorder->isThreaded) //is not threaded
              {
                  this->inorder = (this->inorder)->Right; // advance to right node
              }
          }
      
      }
      
      return *this;
  }

  //
  // clear:
  //
  // Clears the contents of the tree, resetting the tree to empty.
  //
  void clear()
  {
    //
    // TODO
    //
        
     //call postTraverse, deletes tree through recursive postTraversal
      postTraverse(Root); 
      Size = 0;  // set size to 0
      Root = nullptr; // empty tree,so Root equals nullptr
      inorder = nullptr; // since tree is cleared, inorder state should be nullptr   
  }

  // 
  // size:
  //
  // Returns the # of nodes in the tree, 0 if empty.
  //
  // Time complexity:  O(1) 
  //
  int size() const
  {
    return Size; //return private member variable, keeping track of size
  }

  // 
  // height:
  //
  // Returns the height of the tree, -1 if empty.
  //
  // Time complexity:  O(1) 
  //
  int height() const
  {
    if (Root == nullptr)  //empty tree, so height is -1
      return -1;
    
    
      return Root->Height; //Root node exists,so its height is tree's height
  }

  // 
  // search:
  //
  // Searches the tree for the given key, returning true if found
  // and false if not.  If the key is found, the corresponding value
  // is returned via the reference parameter.
  //
  // Time complexity:  O(lgN) worst-case
  //
  bool search(KeyT key, ValueT& value) const
  {
    //
    // TODO
    //
    
    
    NODE* cur = Root; //cur points to Root node
     
    while(cur != nullptr) //traverse tree, if key is not found
    {
        
       if(key == cur->Key) // key found
       {
        
          value = cur->Value;         // set value to found key's value
          return true;      // found it!
       }
       else if(key < cur->Key) // key is less, go left
       {
           cur = cur->Left; //cur points to left node of cur
       }
       else
       {
           if(cur->isThreaded) // cannot go further, since no Node to right exist
           {
               cur = nullptr; // set to nullptr
           }    
           else // nodes to right exist, go right
           {
               cur = cur->Right; // advance cur pointer to cur's Right
           }    
       }
    }    
   
     return false; // not found, false
     
  }


   /* Helper Function used by range_search, returns true if the key 
      is in the range of x and y inclusive. */ 
   
  bool inRange(KeyT x,KeyT y, KeyT key) 
  {
      return (key >= x && key <= y);
  }




  //
  // range_search
  //
  // Searches the tree for all keys in the range [lower..upper], inclusive.
  // It is assumed that lower <= upper.  The keys are returned in a vector;
  // if no keys are found, then the returned vector is empty.
  //
  // Time complexity: O(lgN + M), where M is the # of keys in the range
  // [lower..upper], inclusive.
  //
  // NOTE: do not simply traverse the entire tree and select the keys
  // that fall within the range.  That would be O(N), and thus invalid.
  // Be smarter, you have the technology.
  //
  vector<KeyT> range_search(KeyT lower, KeyT upper)
  {
    vector<KeyT>  keys;

    //
    // TODO
    // 
    
    NODE* cur = Root;
    NODE* least = nullptr;
    
    /*Find the lowest in range key in the tree*/
    
    while(cur != nullptr)  //go till cur is not nullptr
    { 
        if(inRange(lower,upper,cur->Key))    //cur is in range
        {
          
           /* Check if cur is least in range found so far or if no in range is found */
           
          if(least == nullptr || cur->Key < least->Key) 
           {
               least = cur;         //cur is now least in range key
           }
           
        }
                
        if(cur->Left != nullptr && ( cur->Key > lower ))    //least range key exists to left
        {
            cur = cur->Left;
     
        }
        else if(!cur->isThreaded  && (cur->Key < upper))  //least range key exists to right
        {
           cur = cur->Right;
        }
        else
        {
           break;
        }
       
    }
    
    cur = least;      //cur equals the least in range key
    
    
    
    if(least != nullptr)      // least inclusive key exists in the tree
    {
        /* Now Traverse in an inorder way still a key not in range is found */
    
        while(cur != nullptr && inRange(lower,upper,cur->Key))
        {
            keys.push_back(cur->Key);      //cur's key is in range, push into vector
            
            if(cur->Right != nullptr ) //check if next inorder node is not nullptr
             {
                 if(cur->isThreaded) //cur node is threaded
                 {
                    cur = cur->Right; // advance  to right
                 }
                 else
                 {
                    //advance  to the left most node from cur's right node 
                    cur = this->leftMost(cur->Right); 
                 }
             }
             else //cannot advance node
             {
                 break;
             } 
        } 
    }
    
    return keys;     //return all in range keys
  }


   /* This functions returns the height of a node passed in as a pointer to node.*/
   
  int getHeight(NODE* cur)
  {
     int height;
     
     if(cur == nullptr)   //node is nullptr,so height is -1
     {
         height = -1;
     }
     else    //node exists
     {
         height = cur->Height;  //set height to cur's height
     }
      
     return height;     
  }
  
  
  /* Returns the balance of the node. The balance of node is determined
     by the difference in left and right nodes of the node. */
  
  int getBalance(NODE* cur)
  {
     int L;    // stores height of left node
     int R;    // stores height of right node
     
     if(cur == nullptr)    // cur is nullptr
     {
         return 0;         //balance is 0
     }
     
     if(cur->Left == nullptr)   // left node doesn't exists, so height is -1
     {
         L = -1;
     }
     else      //left node exists, set L to left node's height
     {
         L = (cur->Left)->Height;
     }


     if(cur->isThreaded || cur->Right == nullptr)   //right node doesnt exist, so set R to -1
     {
         R = -1;
     }
     else              // right node exists
     {
         R = (cur->Right)->Height;
     }
     
     return (L-R);     //return the difference in left and right node's height
  }

   
  int getMax(int x,int y)   // returns the maximun value between x and y
  {
     if(x > y)    // x is greater than y
     {
         return x;    //return x
     }
     
     return y;   
  }
   
   
   void RightRotate(NODE* Parent, NODE* N)
   {
       
     NODE* L = N->Left;      
     NODE* A = L->Left;
     NODE* B = L->Right;
     NODE* C = N->Right;
     
     L->Right = N;     //make the N its left node's  right children
     
     N->Left = B;      // N's left points to its left's node's right children
     
     if(L->isThreaded)      //if L is threaded
     {
         N->Left = nullptr;     //set N's left to nullptr
     }
     
     
     if(Parent == nullptr)   //N is Root node
     {
        Root = L;
     }
     else if(Parent-> Left == N)   // N is left child of its parent
     {
       Parent->Left = L;  
     }
     else                       // N is right child of its parent
     {
        Parent->Right = L;
     }
     
     int aHeight = getHeight(A);  //store height of A
     
     int bHeight = getHeight(B); // height of B
     
     if(L->isThreaded)          //L isThreaded, so B's height is -1
          bHeight = -1;
     
     int cHeight = getHeight(C);     //store height of C
     
     if(N->isThreaded)        //N isThreaded, so C's height is -1
         cHeight = -1;
     
     N->Height = max(bHeight,cHeight)+1;  //N's height depends on B and C
     L->Height = max(N->Height,aHeight)+1;  //L's height depends on N and A
     
     L->isThreaded = false;           // L cannot be threaded anymore
   }

  
  
  void LeftRotate(NODE* Parent, NODE* N)
  {
     
     NODE* R = N->Right;
     
     NODE* A = N->Left;
     NODE* B = R->Left;
     NODE* C = R->Right;
     
     R->Left = N;     //set N as its Right Node's Left Child
     N->Right = B;    //set N's Right Node's Left Child to be N's Right Child
     
     
     if(B == nullptr)    //B is nullptr
     {
        N->Right = R;     // N's Right points to R
        N->isThreaded = true;     //make N threaded
     }
     
     
     if(Parent == nullptr)      //N is Root Node
      {
         Root = R;    
      }
      else if(Parent->Right == N)    //N is a right child of its parent node
      {
         Parent->Right = R;
      }
      else                       //N is a left child of its parent node
      {
         Parent->Left = R;
      }
      
      int aHeight = getHeight(A);   //height of A
     
      int bHeight = getHeight(B);   //height of B 
     
      if(N->isThreaded)       //N is threaded, B's height is -1
          bHeight = -1;
     
      int cHeight = getHeight(C);     //height of C
     
      if(R->isThreaded)         //R is threaded, C's height is -1
          cHeight = -1;
      
      
      
      N->Height = max(aHeight,bHeight)+1;    //N's height depends on A's height and B's height
      R->Height = max(N->Height,cHeight)+1;    //R's height depends on N and C      
  
  }  
    
    
    
    
    

  //
  // insert
  //
  // Inserts the given key into the tree; if the key has already been insert then
  // the function returns without changing the tree.  Rotations are performed
  // as necessary to keep the tree balanced according to AVL definition.
  //
  // Time complexity:  O(lgN) worst-case
  //
  void insert(KeyT key, ValueT value)
  {
    //
    // TODO
    //
    
    NODE* cur = Root; //set cur to Root
    NODE* prev = nullptr; // prev is nullptr,since Root is first node
    
    vector<NODE*> nodes;
    
    while(cur != nullptr) // cur is not null
    {
        
        if(key == cur->Key) //key is found
        {
           return; //return, since key already exists, cannot overwrite with value
           
        }
        
        nodes.push_back(cur);
         
         if(key < cur->Key) //key is less than cur's key, go left
        {
           prev = cur; //update prev to cur
           cur = cur->Left; //advance cur to its left
        }
        else
        {
            prev = cur; //update prev to cur
            if(cur->isThreaded) // no tree exists to right
            {
                cur = nullptr; // set cur to nullptr
            }    
            else // tree exists to right
            {
                cur = cur->Right; //set cur to right
            }    
        }
    }
    
    NODE* newNode = new NODE; // create new node
    newNode->Key = key; //set  key
    newNode->Value = value; //set value 
    newNode->Height = 0;
    
    if(prev == nullptr) //inserting to empty tree
    {
        Root = newNode;            //newNode is root
        Root->Right = nullptr;    // set Right to nullptr
    }
    else if(key < prev->Key)     //newNode is left of its parent
    {
        prev->Left = newNode;   // set parent's left to point to newNode
        newNode->Right = prev;  // set newNode's right to point to its parent
    }
    else
    {      
       newNode->Right = prev->Right;   //newNode's right inherents parent's right node
       prev->Right = newNode;    //set parent's right to point to newNode
       
       if(prev->isThreaded)   //prev is threaded
       {
          //since node is inserted to its right,prev is not threaded anymore       
          prev->isThreaded = false;  
          
       }
       
    }
    
    newNode->isThreaded = true; 
    newNode->Left = nullptr;  //set left of newNode to nullptr
    Size++; //increase size
    
    
    while (!nodes.empty())    //walk back up the tree
    {
      NODE* N = nodes.back();    //N is last node in the vector
      nodes.pop_back();   
        
      int HL = getHeight(N->Left);       //N's Left child's height
      int HR = getHeight(N->Right);     // N's Right child's height
      
      int newHeight =  (N->isThreaded)? HL + 1 : getMax(HL,HR) + 1; // new height of N 
      int balance = getBalance(N);        //if N is balanced
      
      
      if(newHeight == N->Height )    //height did not change break
      {
          break;
      }
      else if(balance == 0 || balance == 1 || balance == -1)  //Node N is not broken
      {
         N->Height = newHeight;      //set N's Height to its new height
      }
      else     // N is broken, identify case, and rotate
      { 
          
          NODE* Parent;
          
          if(nodes.size() == 0)     //N is root node       
          {
              Parent = nullptr;
          }
          else     //N's parent is the second last element in the vector
          { 
              Parent = nodes[nodes.size()-1];
          }   
          
          
          if(key < N->Key)          //case 1 or case 2
          {
               if(getBalance(N->Left) ==  -1)               // case 2
              {
                  LeftRotate(N,N->Left);   //left rotate at N's left child
              }
              
              RightRotate(Parent,N);     //right rotate N
          }
          else                       //case 3 or 4
          {
               if(getBalance(N->Right) == 1)    // case 3
              {
                  RightRotate(N,N->Right);    //right rotate at N's right child
              }
             
             LeftRotate(Parent,N);    //Left rotate N
          } 
       } 
      

     } 
    
    
    }
    
    
  

  //
  // []
  //
  // Returns the value for the given key; if the key is not found,
  // the default value ValueT{} is returned.
  //
  // Time complexity:  O(lgN) worst-case
  //
  ValueT operator[](KeyT key) const
  {
    //
    // TODO
    //
    ValueT val; //create variable val to store return value of search
    
    if(this->search(key,val)) // call search, pass in val as parameter, if true is returned
    {
        return val; //val 
    }
    
    //key does not exist so return default value 
    
    return ValueT{};
  }

  //
  // ()
  //
  // Finds the key in the tree, and returns the key to the "right".
  // If the right is threaded, this will be the next inorder key.
  // if the right is not threaded, it will be the key of whatever
  // node is immediately to the right.
  //
  // If no such key exists, or there is no key to the "right", the
  // default key value KeyT{} is returned.
  //
  // Time complexity:  O(lgN) worst-case
  //
  KeyT operator()(KeyT key) const
  {
    //
    // TODO
    //
     NODE* cur = Root; //set cur to Root
     while(cur!=nullptr) //if cur is a node
    {
        
        if(key == cur->Key) // found key
        {
          if(cur->Right == nullptr) // not threaded, so return default key value
          {
              return KeyT{};
          }
          return (cur->Right)->Key; //threaded, so return next inorder key
        }
        else if(key < cur->Key) // key is less than cur's key
        {
           cur = cur->Left; //go to left
        }
        else
        {
            if(cur->isThreaded) //no nodes to right
            {
                cur = nullptr; //set cur to nullptr
            }    
            else //node exists to right
            {
                cur = cur->Right;  //go to right
            }    
        }
    }    
    return KeyT{}; //return default if key does not exists
  
  }

  /* Helper function designed to find left most node from an inputed head node. 
    This function is used by begin() and next() to traverse tree  inorderly. */
     
  NODE* leftMost(NODE* head)
  {
      
      if(head == nullptr)   //head is nullptr, so return nullptr
          return nullptr;
      
      NODE* cur = head; //set cur to head
      while(cur->Left != nullptr) //while cur's Left is not null
      {
          cur = cur->Left;  //advance cur to its Left
      }
      return cur; //return cur, which is now leftMost node from head or head
  }



  //
  // %
  //
  // Returns the height stored in the node that contains key; if key is
  // not found, -1 is returned.
  //
  // Example:  cout << tree%12345 << endl;
  //
  // Time complexity:  O(lgN) worst-case
  //
  int operator%(KeyT key) const
  {
    //
    // TODO
    //
    
    NODE* cur = Root;      //set cur to Root
    
    while(cur!= nullptr)   //go till cur is not nullptr
    {
        if(cur->Key == key)    //found key
        {
            return cur->Height;   //return height
            
        }
        else if(key < cur->Key )    //key is less than cur, go left
        {
            cur = cur->Left;
        }
        else if(!cur->isThreaded)   //cur is not threaded,so nodes exists to right
        {
            cur = cur->Right;    // go right
        }
        else    //end of tree, cannot go left or right, break
        {
            break;
        }
    }
    
    return -1;       //not found, return -1
  }

  //
  // begin
  //
  // Resets internal state for an inorder traversal.  After the 
  // call to begin(), the internal state denotes the first inorder
  // key; this ensure that first call to next() function returns
  // the first inorder key.
  //
  // Space complexity: O(1)
  // Time complexity:  O(lgN) worst-case
  //
  // Example usage:
  //    tree.begin();
  //    while (tree.next(key))
  //      cout << key << endl;
  //
  void begin()
  {
    //
    // TODO
    //
     if(this->Root == nullptr) //if tree is empty
    {
        inorder = nullptr; //inorder is nullptr,since no tree exists
        return;
    }  
    
    inorder = leftMost(Root); //set inorder to leftMost node in the tree  
  }

  //
  // next
  //
  // Uses the internal state to return the next inorder key, and 
  // then advances the internal state in anticipation of future
  // calls.  If a key is in fact returned (via the reference 
  // parameter), true is also returned.
  //
  // False is returned when the internal state has reached null,
  // meaning no more keys are available.  This is the end of the
  // inorder traversal.
  //
  // Space complexity: O(1)
  // Time complexity:  O(lgN) worst-case
  //
  // Example usage:
  //    tree.begin();
  //    while (tree.next(key))
  //      cout << key << endl;
  //
  bool next(KeyT& key)
  {
    //
    // TODO
    //
    if(inorder == nullptr) // no next inorder node, return false
    {
        return false;
    }
         
    key = inorder->Key; //set key to inorder key, "returns" key of current inorder node
   
   // advance internal state to next inorder key 
   if(inorder->Right != nullptr ) //check if next inorder node is not nullptr
     {
         if(inorder->isThreaded) //inorder node is threaded
         {
            inorder = inorder->Right; // advance state to right
         }
         else
         {
            //advance state to the left most node from inorder's right node 
            inorder = leftMost(inorder->Right); 
         }
     }
     else //cannot advance node
     {
         inorder = nullptr;
     } 
        
      //since key was "returned", return true
     return true;
     
  }


 void inorderOutput(ostream& output,NODE* cur) const
  {
      if(cur == nullptr) //empty tree, just return
          return;
       
      // recursively call inorderOutput, passing left node of cur 
      inorderOutput(output,cur->Left); 
      
     //output, if not threaded
     if(!cur->isThreaded ||  cur->Right == nullptr)
      {
          output << "(" << cur->Key << "," << cur->Value  << "," << cur->Height  << ")" << endl;
      }
      else  // output,if threaded
      {
          output << "(" << cur->Key << "," << cur->Value << "," << cur->Height << "," << cur->Right->Key << ")" << endl;
      }
      
      
      if(!cur->isThreaded) //if cur is not threaded
      {
          // recursively call inorderOutput, passing right node of cur
          inorderOutput(output,cur->Right); 
      }
      
      
  }


  //
  // dump
  // 
  // Dumps the contents of the tree to the output stream, using a
  // recursive inorder traversal.
  //
  void dump(ostream& output) const
  {
    output << "**************************************************" << endl;
    output << "********************* AVLT ***********************" << endl;

    output << "** size: " << this->size() << endl;
    output << "** height: " << this->height() << endl;

    //
    // inorder traversal, with one output per line: either 
    // (key,value,height) or (key,value,height,THREAD)
    //
    // (key,value,height) if the node is not threaded OR thread==nullptr
    // (key,value,height,THREAD) if the node is threaded and THREAD denotes the next inorder key
    //

    //
    // TODO
    //

    inorderOutput(output,this->Root);  

    output << "**************************************************" << endl;
  }
	
};

