/* Name : Bhumit Patel
  Project 3: Maze Solving
  Class: CS 211

  In this project, the program takes a text file as an input, and 
  creates a maze using the inputs in the text file. The maze is a dynamically
  allocated 2d char array in the maze structure. It then implements the Depth First 
  search which uses a stack, which is also created by the program as a linked list.
  It prints out the solution after running DFS algorithmn.
  
   */


#include <stdio.h>
#include <stdlib.h>
#include <string.h>


/* Defines true and false as integer values */
#define TRUE 1
#define FALSE 0

int DebugMode; // flag for debug mode argument

/* Maze Structure, contains 2d char array which is the maze and its size,start points 
and endpoints */

typedef struct mazeStruct
{
 char **arr;    //dynamically allocated 2d char array, this is the maze
 int xsize, ysize; // row and column sizes
 int xstart, ystart; // start point of maze
 int xend, yend; //end point of maze
} maze;

/* This structure stores a points as linked list. Used by Stack. */
typedef struct pointStruct
{
  int x;                  // x coordinate
  int y;                  // y coordinate
  struct pointStruct* next; //pointer to next point
} point;

/* Structure for stack. Stack contains pointer to a linked list of points,
where the first element is the top.  Also, stores the size of the stack. */

typedef struct structstack
{
  point* head; //pointer to linked list of point
  int size; // stores size

} stack;

/* Given a point x,y, and the address of the pointer to a linkelist, it
will insert that point at the front of the linked list.*/

void insertAtFront(point** pointHead, int x, int y)
{
  point* newHead = (point*)malloc(sizeof(point)); //dynamically allocated space to store new point
  newHead->x = x;                   // set x
  newHead->y = y;                   // set y
  newHead->next = *pointHead;       // set next to point to the head of linked list 
  *pointHead = newHead;              // set head of list to newHead
}


/* Insert at a Back of a linked list of points. Returns a pointer to a point.*/

point* insertAtBack(point* head, int x, int y)
{
  if(head == NULL) //head doesn't exist
  {
    head = (point*) malloc(sizeof(point));  //allocate memory for new point
    head->x = x;                // set x
    head->y = y;                // set y
    head->next = NULL;          // set next to null
  }
  else // head node exists
  {
    point* cur = head;       // cur points to head 

    while(cur->next != NULL) // go until we reach the last node
    {
      cur = cur->next;        //cur points to next node
    }

    point* newPoint = (point*) malloc(sizeof(point));  //allocate memory for new point
    cur->next = newPoint;   //set the last element's next to new element
    newPoint->x = x;        //set x
    newPoint->y = y;         //set y
    newPoint->next = NULL;   //set newPoint's next to NULL
  }
  return head;      //return head
}


/* Removes a point x,y from a linked list of points.
   Takes in x,y and pointer to a pointer to linked list as input. */

void removeAny(point** head, int x,int y)
{
  if(*head == NULL)  //cannot remove empty list
  {
    return;
  }
  else if((*head)->x == x && (*head)->y)  //remove head node
  {
    point* oldHead = *head;     //store location to current head
    *head = (*head)->next;      // set cuurent head to point to its next point
    free(oldHead);            //delete oldHead

  }
  else          
  {
    point* cur = *head;     //cur points to head
    point* prev = NULL;       

    while(!(cur->x == x && cur->y == y))   //go till we find the point to remove
    {
      prev = cur;         //store location of prev point
      cur = cur->next;    //set cur to point to next

      if(cur == NULL)       
      {
        return;
      }
    }
    prev->next = cur->next; //set previos element's next to point to current's element next
    free(cur);  //delete current element
  }
}

/* Print a linedlist of points */

void printList(point* head)
{
  while(head != NULL)  //go until end of list
  {
    printf("X: %d, Y: %d\n",head->x , head->y); //print current point
    head = head->next; //go to next element
  }  

  return;
}

/* Deletes a linkedlist of points */

void clearList(point* head)
{
  point* cur = head;     //cur points to head

  while(cur != NULL)     //go till end of list
  {
    point* next = cur->next;  //store located of next elemenet
    free(cur);          //delete current element
    cur = next;     //cur points to next element
  }
}

/* Intizialize Stack to a size of zero and head of the linked list
  implemented as a stack to null/ */

void init(stack *s1)
{
  s1->head = NULL;   // initally empty, so head is  NULL
  s1->size = 0;     // initally, no elements
}

/* Returns true if stack is empty. False, if elements exist. */

int is_empty(stack *s1)
{
  if( s1->size == 0)  
    return TRUE;  // stack empty, so return true
  
    return FALSE; //not empty, returns false
}


/* Pushes point (x,y) on to the stack. Updates size of the
  stack. */

void push(stack *s1, int x, int y)
{
  //call insertAtFront, this inserts a point(x,y) to front of the list
  insertAtFront(&(s1->head),x,y);

  s1->size = s1->size + 1; // increase size by 1
  
  if(DebugMode)  //Debug Mode is ON
  {
    printf("Pushed: X: %d, Y: %d\n",x,y);
  }
}

/* Pops the element on the top of the stack/  */

void pop(stack *s1)
{
  if(s1->head == NULL) //cannot pop, empty stack
    return;


  if(DebugMode)  //print if debug mode is ON
  {
    printf("Popped: X: %d, Y: %d\n",s1->head->x, s1->head->y);
  }

  /* removeAny removes a point(x,y) from a linked list (stack impelemeted as linekd list),
    it is called to remove point stored in top of the stack*/
  
  removeAny(&(s1->head),s1->head->x,s1->head->y);
  
  s1->size = s1->size - 1; // decrease size

}

/* Returns the address of the point at the top of the stack,
  this allows the modification of the point and access of it.*/

point* top(stack *s1)
{
  if(s1->size == 0)    //stack is empty
    return NULL;

  return s1->head;   //return address of the point at top of the stack
}

/* ClearStack, deletes all the points in the element and sets
   size to 0. It calls clearList, which deletes a dynamically 
   allocated linked list of point.*/

void clearStack(stack *s1)
{
  clearList(s1->head);  //call to clearList, deletes points

  s1->size = 0;  //set size to 0
}


/* Searches through a linked list of points for a point(x,y)
and returns true if found, false if not. */

int search(int x, int y, point* head)
{
  point* cur = head;          //set cur to head

  if(head == NULL) return FALSE; //list is empty,so not found

  while(cur != NULL)  //go till the end of the list is reached
  {
    if(cur->x == x && cur->y == y) //found point(x,y)
    { 
      return TRUE;            //found it!!
    }
    cur = cur->next; //go to the next point in the list
  }

  return FALSE;       //not found, return false

}


/* This function determines whether a point can be vistied in a maze.
    A point can be visted if it is a point in the maze and it is not a blocked
    point, and if it has not been visted before. */

int canVisit(int x, int y, maze* m1, point* unvisted)
{
  if(x >= 1 && x <= m1->xsize && y >= 1 && y <= m1->ysize)  // point(x,y) is point in maze
  {
    
    // if point(x,y) is not blocked point and it is unvisted

    if(m1->arr[x][y] != '*' && search(x,y,unvisted))   
    {
      return TRUE;     // can Visit!!!
    }

  }

  return FALSE;   // cannot visit point!!
}


// This function prints a linked list of points in reverse order using recurison.

void printList2(point* head)
{
    if(head == NULL)   //point doesn't exist
    {
      return;
    }

    printList2(head->next);   //call to print next point

    printf("X: %d, Y: %d\n",head->x , head->y);  //prints point, after recursive call to next is finished
}


/* This function pushes a point that can be vistied on to the stack. It removes the point 
   from unvisted list, and inserts in point at back of visted list.*/

void canVisitHelper(int x, int y, point** unvisted, point* visted, stack *s1)
{
  push(s1,x,y);     //push to the top of tack s1
  removeAny(unvisted,x,y);  //remove from unvisted list
  insertAtBack(visted,x,y);   //insert point to visted list

}

/* Clears the dynamically allocated maze, and deletes the dynamically allocated 2d char array. */

void clearMaze(maze* m)
{
  for(int i=0; i < m->xsize + 2; i++)  //go through each row
  {
    free(m->arr[i]);            //deletes columns in each row
  } 

  free(m->arr);         //deletes rows
  free(m);      //delete maze

}



int main (int argc, char **argv)
{
  DebugMode = FALSE;    //initally debugMode is false
  
  int arg = argc;
  int k;
  int indexFile = 1;  //store the index at which filename will be located

  for(k=0;k < argc;k++)
  {
    if(strcmp(argv[k],"-d") == 0)   // turn debugMode ON 
    {
      DebugMode = TRUE;     // set debugMode to true
      arg = arg - 1;    // only two command line arguements are allowed now

      if(k == 1)      // "if -d" is at 1, then filename should be at 2, since only 3 commandline arguements are allowed
      {
        indexFile = 2;   //set indexFile to 2
      }

      break;

    }
  }
  
  if(DebugMode) //debugMode ON
  {
    printf ("Debugging Information \n");  
  }
  
  
  
  maze* m1 = (maze*)malloc(sizeof(maze));  //dynamically allocate space for maze structure

  int xpos, ypos;
  int i,j;

  FILE *src;

  
  if(arg > 2)     // too many arguments for program to handle, print to stderr, and exit program
  {
    fprintf(stderr, "Too many command line arguments\n");
    exit(-1);
  }


  /* verify the proper number of command line arguments were given */
  if(arg != 2) 
  {
     printf("Usage: %s <input file name>\n", argv[0]);
     exit(-1);
  }
   
  /* Try to open the input file. */
  if ( ( src = fopen( argv[indexFile], "r" )) == NULL )
  {
    printf ( "Can't open input file: %s", argv[indexFile] );
    exit(-1);
  }

  int count = 0;
  int input1;
  int input2;

  while(count !=3)   //run till 3 valid inputs are encountered
  {
    /* read in the size, starting and ending positions in the maze */
    if(fscanf(src, "%d %d", &input1, &input2) != EOF)
    {
      if(count == 0)    // find size of maze
      {

        if(input1 > 0 && input2 > 0)  // input size is valid
        {
          m1->xsize = input1;
          m1->ysize = input2;
          count++;
        }  
        else      //invalid size, print to stderr
        {
          fprintf(stderr, "Invalid: Maze Size Must be Greater than 0\n");
        }

      }
      else if(count == 1)    //find valid start position 
      {  

        if(input1 >= 1 && input1 <= m1->xsize)   //valid ROW position
        {

          if(input2 >= 1 && input2 <= m1->ysize)  //valid Column 
          {  
            m1->xstart = input1;    //set xstart to inputed x start position
            m1->ystart = input2;    //set ystart to inputed y start position

            count++;       
          }
          else  //invalid column position, print to stderr
          {
            fprintf(stderr, "Invalid: Column %d is outside from range 1 to %d \n", input2,m1->ysize);
          }

        }
        else  //invalid row position, print to stderr
        {
          fprintf(stderr, "Invalid: Row %d is outside from range 1 to %d \n", input1,m1->xsize);
        }

      }
      else if(count == 2)   //find valid end position
      {

        if(input1 >= 1 && input1 <= m1->xsize) //valid xpostion
        {

            if(input2 >= 1 && input2 <= m1->ysize){      //valid yposition

              if((input1 != m1->xstart || input2 != m1->ystart))      //start position != end position
              {

                m1->xend = input1;
                m1->yend = input2;
                count++;

              }
              else //invalid end position, print to stderr
              {
                fprintf(stderr, "Invalid End Position: End Position is same as Start Position!\n");
              }


            }
            else //invalid column position, print to stderr
            {
              fprintf(stderr, "Invalid: Column %d is outside from range 1 to %d \n", input2,m1->ysize);
            }


        }
        else //invalid column position, print to stderr
        {
          fprintf(stderr, "Invalid: Row %d is outside from range 1 to %d \n", input1,m1->xsize);
        }


      }


    }
    else //invalid inputs, cannot create a Maze, print to stderr, exit program
    {
      fprintf(stderr, "Not enough Valid Inputs to create a Maze!\n");
      fclose(src);
      exit(-1);

    }

  }
  
  /* print them out to verify the input */
  printf ("size: %d, %d\n", m1->xsize, m1->ysize);
  printf ("start: %d, %d\n", m1->xstart, m1->ystart);
  printf ("end: %d, %d\n", m1->xend, m1->yend);

  //allocate space for rows in maze arr
  m1->arr = (char**)malloc(sizeof(char*)*(m1->xsize+2));


  //allocate space for columns in each row for maze arr
  for(i=0;i < m1->xsize+2; i++)
  {
    m1->arr[i] = (char*)malloc(sizeof(char)*(m1->ysize+2));
  }




  /* initialize the maze to empty */
  for (i = 0; i < m1->xsize+2; i++)
     for (j = 0; j < m1->ysize+2; j++)
       m1->arr[i][j] = '.';

  /* mark the borders of the maze with *'s */
  for (i=0; i < m1->xsize+2; i++)
    {
     m1->arr[i][0] = '*';
     m1->arr[i][m1->ysize+1] = '*';
    }
  for (i=0; i < m1->ysize+2; i++)
    {
     m1->arr[0][i] = '*';
     m1->arr[m1->xsize+1][i] = '*';
    }

  /* mark the starting and ending positions in the maze */
  m1->arr[m1->xstart][m1->ystart] = 's';
  m1->arr[m1->xend][m1->yend] = 'e';
		  

  /* mark the blocked positions in the maze with *'s */

  while (fscanf (src, "%d %d", &xpos, &ypos) != EOF) //while end of file is not reached
    {
      if(xpos >= 1 && xpos <= m1->xsize)      //valid xposition
      {
        
        if(ypos >= 1 && ypos <= m1->ysize)  //valid yposition
        {

          if(xpos != m1->xstart || ypos != m1->ystart)  //cannot block start position
          {
            if(xpos != m1->xend || ypos != m1->yend)      //cannot block end position
            {
              m1->arr[xpos][ypos] = '*';

            }
            else  //invalid block position
            {
              fprintf(stderr, "Invalid : Cannot Block End Position!\n");
            }

          }
          else  //invalid block position
          {
            fprintf(stderr, "Invalid : Cannot Block Start Position!\n");
          }

        }
        else //invalid column position, print to stderr
        {
          fprintf(stderr, "Invalid: Column %d is outside from range 1 to %d \n", ypos,m1->ysize);
        }

      }
      else //invalid row position, print to stderr
      {
        fprintf(stderr, "Invalid: Row %d is outside from range 1 to %d \n", xpos,m1->xsize);
      }

    }

  /* print out the initial maze */
  for (i = 0; i < m1->xsize+2; i++)
    {
     for (j = 0; j < m1->ysize+2; j++)
       printf ("%c", m1->arr[i][j]);
     printf("\n");
    }

  fclose(src);

  point* unvisted = NULL;   //create  linked list to store unvistied points

  point* visted = NULL; //create linked list to store visted points
  
 
  /* Insert all unblocked points to unvisted list*/

  for(i = 1; i < m1->xsize + 1; i++)
    {
      for (j = 1;j < m1->ysize + 1;j++)
      {
        if(m1->arr[i][j] != '*')
        {
          unvisted = insertAtBack(unvisted, i,j);
        }
      }
    }

  //remove start position from unvisted list
  removeAny(&unvisted,m1->xstart,m1->ystart);

  // mark start position as visted
  visted = insertAtBack(visted,m1->xstart,m1->ystart);

  stack* s = (stack*)malloc(sizeof(stack));   //allocate memory for stack
  init(s);      //initialize stack

  push(s,m1->xstart,m1->ystart);    //push start position on top of stack
  
  while(!is_empty(s))   //loop till stack is not empty
  {
    if(s->head->x == m1->xend && s->head->y == m1->yend)  //end position reached, exit loop
    {
      break;
    }
    else 
    {
      int x = top(s)->x;    //get xposition of point on top of the stack
      int y = top(s)->y;    //get yposition of point on top of the stack
      
      
      if(canVisit(x+1,y,m1,unvisted))  //check if can visi point to the right to point at top of the stack
      {

        canVisitHelper(x+1,y,&unvisted,visted,s);  //visit point

      }
      else if(canVisit(x-1,y,m1,unvisted))   //check if can vist point to the left
      {

        canVisitHelper(x-1,y,&unvisted,visted,s);  //visit point

      }
      else if(canVisit(x,y+1,m1,unvisted))  //check if can visit point above
      {

        canVisitHelper(x,y+1,&unvisted,visted,s);  //visit point

      }
      else if(canVisit(x,y-1,m1,unvisted))  //check if can visit point  below
      {

        canVisitHelper(x,y-1,&unvisted,visted,s);  //visit point

      }
      else        // cannot visit any neighboring point, so pop top of the stack
      {
       pop(s); 
      }


    }


  }


  if(!is_empty(s))      //stack is not empty, solution found
  {
    printf("\n ****SOLUTION**** \n\n");
    printList2(s->head);      //prints solution from starting point to ending point
  }
  else  //stack is empty, no solution
  {
    printf("\n**** NO SOLUTION! ***** \n\n");
  }


  clearList(unvisted);    // deallocate unvisted list
  clearList(visted);      // deallocate visted list

  clearStack(s);       // deallocate stack's linked list
  free(s);            // deallocate the stack structure

  clearMaze(m1);      // deallocate maze
}
