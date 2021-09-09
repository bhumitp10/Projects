#include <iostream>
#include <vector>
#include <algorithm>

#include "avlt.h"

#include "catch.hpp"

using namespace std;


TEST_CASE("(1) empty tree")
{
  avlt<int, int>  tree;

  REQUIRE(tree.size() == 0);
  REQUIRE(tree.height() == -1);
}


TEST_CASE("(2) case 1 at the root")
{
  avlt<int, int>  tree;

  vector<int> keys = { 100, 80, 60 };
  vector<int> heights = { 0, 1, 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());

  auto maxH = std::max_element(heights.begin(), heights.end());
  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  int value;

  for (int key : keys)
  {
    REQUIRE(tree.search(key, value));
    REQUIRE(value == -key);
  }

  //
  // heights correct after rebalance?
  //
  for (size_t i = 0; i < keys.size(); ++i)
  {
    REQUIRE((tree % keys[i]) == heights[i]);
  }
    
}


TEST_CASE("(3) case 2 at the root")
{
  avlt<int, int>  tree;

  vector<int> keys = { 100, 50, 75 };
  vector<int> heights = { 0,0 , 1 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());

  auto maxH = std::max_element(heights.begin(), heights.end());
  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  int value;

  for (int key : keys)
  {
    REQUIRE(tree.search(key, value));
    REQUIRE(value == -key);
  }

  //
  // heights correct after rebalance?
  //
  for (size_t i = 0; i < keys.size(); ++i)
  {
    REQUIRE((tree % keys[i]) == heights[i]);
  }
    
  
}

TEST_CASE("(4) case 3 at the root")
{
  avlt<int, int>  tree;

  vector<int> keys = { 100, 150, 125 };
  vector<int> heights = { 0,0 , 1 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());

  auto maxH = std::max_element(heights.begin(), heights.end());
  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  int value;

  for (int key : keys)
  {
    REQUIRE(tree.search(key, value));
    REQUIRE(value == -key);
  }

  //
  // heights correct after rebalance?
  //
  for (size_t i = 0; i < keys.size(); ++i)
  {
    REQUIRE((tree % keys[i]) == heights[i]);
  }
    
  
}


TEST_CASE("(5) case 4 at the root")
{
  avlt<int, int>  tree;

  vector<int> keys = { 100, 150, 200 };
  vector<int> heights = { 0,1 , 0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());

  auto maxH = std::max_element(heights.begin(), heights.end());
  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  int value;

  for (int key : keys)
  {
    REQUIRE(tree.search(key, value));
    REQUIRE(value == -key);
  }

  //
  // heights correct after rebalance?
  //
  for (size_t i = 0; i < keys.size(); ++i)
  {
    REQUIRE((tree % keys[i]) == heights[i]);
  }
    
  
}

TEST_CASE("(6) case 1 at the subtree")
{
  avlt<int, int>  tree;

  vector<int> keys = { 100, 150,50,25,20 };
  vector<int> heights = { 2,0,0,1,0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());

  auto maxH = std::max_element(heights.begin(), heights.end());
  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  int value;

  for (int key : keys)
  {
    REQUIRE(tree.search(key, value));
    REQUIRE(value == -key);
  }

  //
  // heights correct after rebalance?
  //
  for (size_t i = 0; i < keys.size(); ++i)
  {
    REQUIRE((tree % keys[i]) == heights[i]);
  }
    
  
}

TEST_CASE("(7) case 2 at the subtree")
{
  avlt<int, int>  tree;

  vector<int> keys = { 100, 150,50,30,40 };
  vector<int> heights = { 2,0,0,0,1 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());

  auto maxH = std::max_element(heights.begin(), heights.end());
  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  int value;

  for (int key : keys)
  {
    REQUIRE(tree.search(key, value));
    REQUIRE(value == -key);
  }

  //
  // heights correct after rebalance?
  //
  for (size_t i = 0; i < keys.size(); ++i)
  {
    REQUIRE((tree % keys[i]) == heights[i]);
  }
    
  
}



TEST_CASE("(8) case 3 at the subtree")
{
  avlt<int, int>  tree;

  vector<int> keys = { 100, 150,50,75,60 };
  vector<int> heights = { 2,0,0,0,1 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());

  auto maxH = std::max_element(heights.begin(), heights.end());
  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  int value;

  for (int key : keys)
  {
    REQUIRE(tree.search(key, value));
    REQUIRE(value == -key);
  }

  //
  // heights correct after rebalance?
  //
  for (size_t i = 0; i < keys.size(); ++i)
  {
    REQUIRE((tree % keys[i]) == heights[i]);
  }
    
  
}
TEST_CASE("(9) case 3 at the subtree")
{
  avlt<int, int>  tree;

  vector<int> keys = { 100, 150,50,175,200 };
  vector<int> heights = { 2,0,0,1,0 };

  for (int key : keys)
  {
    tree.insert(key, -key);
  }

  //
  // size and height?  after rebalance should be 1:
  //
  REQUIRE(tree.size() == keys.size());

  auto maxH = std::max_element(heights.begin(), heights.end());
  REQUIRE(tree.height() == *maxH);
  // 
  // values inserted?
  //
  int value;

  for (int key : keys)
  {
    REQUIRE(tree.search(key, value));
    REQUIRE(value == -key);
  }

  //
  // heights correct after rebalance?
  //
  for (size_t i = 0; i < keys.size(); ++i)
  {
    REQUIRE((tree % keys[i]) == heights[i]);
  }
    
  
}