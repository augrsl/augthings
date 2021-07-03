//*****************************************
// Ali Ulvi Gürselli
#include<string>

#ifndef NA_LINKED_LIST
#define NA_LINKED_LIST


class NodeNA {

public:
    NodeNA() {
        ageNext = 0;
        nameNext = 0;
    }
    NodeNA(std::string name,int age, NodeNA *nameptr , NodeNA *ageptr ){
        this->age = age;
		this->name = name;
		ageNext = ageptr;
		nameNext = nameptr;
    }
    
    int age;
    std::string name;
    NodeNA *ageNext;
    NodeNA *nameNext;
};

class PersonList {
public:
    PersonList() {
        headName = 0;
    	headAge = 0;
	}
   
    bool isEmpty() { 
        return headName == 0;
    }
    void add(std::string name, int age);
	bool remove(std::string name);
	void update(std::string name, int age);
	void printByAge();
	void printByName(); 
	void loadFile(std::string filename);
	void saveToFileByAge(std::string filename);
	void saveToFileByName(std::string filename);
	
	
private:
    NodeNA *headName,*headAge;
};


#endif
