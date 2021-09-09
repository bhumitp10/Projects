#include <iostream>
#include <vector>

#include "avlt.h"

#include "catch.hpp"

using namespace std;

int main(){
    
  avlt<int, int>  tree;

  vector<int> keys = { 55,48,64,47,50,70,54 };
//  vector<int> heights = { 0, 1, 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }
  
  tree.dump(std::cout);
  
  tree.insert(67,-67);
  tree.dump(std::cout);
   
}