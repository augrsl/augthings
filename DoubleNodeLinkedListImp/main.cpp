
#include <iostream>
#include "PersonList.h"

using namespace std;


int main()
{
  
  PersonList list;
  list.loadFile("input.txt");

  list.printByName();
  list.printByAge();


  list.remove("deniz");
  list.remove("ismail");
  list.printByName();
  list.printByAge();

  list.saveToFileByAge("age.txt");
  list.saveToFileByName("name.txt");
}

