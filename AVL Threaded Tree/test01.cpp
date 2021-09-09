#include <iostream>
#include <vector>

#include "avlt.h"

#include "catch.hpp"

using namespace std;







TEST_CASE("(4) root LR, RR") 
{
  avlt<int, int>  tree;

  vector<int> keys = { 100,50,75 };
  vector<int> heights = { 0, 1, 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

   tree.dump(std::cout);
  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());
  
  
 // auto maxH = std::max_element(heights.begin(), heights.end());
//  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  

 
}
TEST_CASE("(5) root RR, LR") 
{
  avlt<int, int>  tree;

  vector<int> keys = { 100,150,125 };
  vector<int> heights = { 0, 1, 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

   tree.dump(std::cout);
  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());
  
  
 // auto maxH = std::max_element(heights.begin(), heights.end());
//  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  

 
}


TEST_CASE("(6) subtree  RR") 
{
  avlt<int, int>  tree;

  vector<int> keys = { 100,50,150,25,20 };
  vector<int> heights = { 0, 1, 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

   tree.dump(std::cout);
  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());
  
  
 // auto maxH = std::max_element(heights.begin(), heights.end());
//  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  
}

TEST_CASE("(7) subtree  LR") 
{
  avlt<int, int>  tree;

  vector<int> keys = { 100,50,150,175,200 };
  vector<int> heights = { 0, 1, 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

   tree.dump(std::cout);
  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());
  
  
 // auto maxH = std::max_element(heights.begin(), heights.end());
//  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  
}


TEST_CASE("(8) subtree  RR LR") 
{
  avlt<int, int>  tree;

  vector<int> keys = { 100,50,150,75,60 };
  vector<int> heights = { 0, 1, 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

   tree.dump(std::cout);
  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());
  
  
 // auto maxH = std::max_element(heights.begin(), heights.end());
//  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  
}
TEST_CASE("(9) Root3 LR RR") 
{
  avlt<int, int>  tree;

  vector<int> keys = { 100,50,150,40,75,60 };
  vector<int> heights = { 0, 1, 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

   tree.dump(std::cout);
  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());
  
  
 // auto maxH = std::max_element(heights.begin(), heights.end());
//  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  
}
TEST_CASE("(10) subtree  LR RR") 
{
  avlt<int, int>  tree;

  vector<int> keys = { 100,50,150,30,40 };
  vector<int> heights = { 0, 1, 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

   tree.dump(std::cout);
  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());
  
  
 // auto maxH = std::max_element(heights.begin(), heights.end());
//  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  
}