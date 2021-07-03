

#include <iostream>
#include <fstream>
#include <bits/stdc++.h> 
#include "PersonList.h"

using namespace std;

void PersonList::loadFile(string filename){
	
	ifstream fin;	
	
	char file2open[filename.length()];
	strcpy(file2open,filename.c_str());
	
	fin.open(file2open);
	string nextName;
	int nextAge;
	
	fin>>nextName;
	string nameHead=nextName;
	fin>>nextAge;
	int ageHead=nextAge;
	
	headName = new NodeNA(nextName,nextAge,0,0);
	headAge = new NodeNA(nextName,nextAge,0,0);

	
	string temp;
	string name2add;
	int age2add;
	
	while(fin>>temp){
	
		name2add=temp;
		fin>>age2add;
	
		add(name2add,age2add);
		
	}
	
}

void PersonList::add(std::string name, int age){
	

	NodeNA* node2add = new NodeNA(name,age,0,0);
	NodeNA* cur;
	NodeNA* prev;
	bool cont=true;
	
	if(node2add->name.compare(headName->name)==-1){ 
		
		node2add->nameNext=headName;
		headName=node2add;
		cont=false;
	}
	else if(headName->nameNext==0){ //
		headName->nameNext=node2add;
		node2add->nameNext=0;
	
		cont=false;
	}
	else{ //
		cur=headName->nameNext;
		prev=headName;
		for(;cur!=0;){
			if(node2add->name.compare(cur->name)==-1){
			
				node2add->nameNext=cur;
				prev->nameNext=node2add;
				cont=false;
				break;
				
			}
			prev=cur;
			cur=cur->nameNext;
		}
	}
	if(cont==true){ //
		
		for (NodeNA *tmp = headName; tmp != 0; tmp = tmp->nameNext){
			if(tmp->nameNext==0){
			
        		tmp->nameNext=node2add;
        		node2add->nameNext=0;
        		break;
			}
		}
	}
	

	cont=true;
	
	if(node2add->age<headAge->age){ 

		node2add->ageNext=headAge;
		headAge=node2add;
		cont=false;
	}
	else if(headAge->ageNext==0){ //
		headAge->ageNext=node2add;
		node2add->ageNext=0;

		cont=false;
	}
	else{ //
		cur=headAge->ageNext;
		prev=headAge;
		for(;cur!=0;){
			if(node2add->age<cur->age){
			
				node2add->ageNext=cur;
				prev->ageNext=node2add;
				cont=false;
				break;
				
			}
			prev=cur;
			cur=cur->ageNext;
		}
	}	
	if(cont==true){ //
		
		for (NodeNA *tmp = headAge; tmp != 0; tmp = tmp->ageNext){
			if(tmp->ageNext==0){
			
        		tmp->ageNext=node2add;
        		node2add->ageNext=0;
				break;
			}
		}
	}
	
}
	
void PersonList::printByAge(){
	for (NodeNA *tmp = headAge; tmp != 0; tmp = tmp->ageNext){
		cout<<tmp->name<<" "<<tmp->age<<endl;
	}
	cout<<"End of the age ordered list..."<<endl;
}

void PersonList::printByName(){
	for (NodeNA *tmp = headName; tmp != 0; tmp = tmp->nameNext){
		cout<<tmp->name<<" "<<tmp->age<<endl;
	}
	cout<<"End of th name ordered list..."<<endl;
}

void PersonList::saveToFileByAge(std::string filename){
	
	ofstream fout;
	char file2open[filename.length()];
	strcpy(file2open,filename.c_str());
	fout.open(file2open);
	for (NodeNA *tmp = headAge; tmp != 0; tmp = tmp->ageNext){
		fout<<tmp->name<<" "<<tmp->age<<endl;
	}
	fout.close();
	
}

void PersonList::saveToFileByName(std::string filename){
	
	ofstream fout;
	char file2open[filename.length()];
	strcpy(file2open,filename.c_str());
	fout.open(file2open);
	for (NodeNA *tmp = headName; tmp != 0; tmp = tmp->nameNext){
		fout<<tmp->name<<" "<<tmp->age<<endl;
	}
	fout.close();
	
}

bool PersonList::remove(std::string name){
	
	if(isEmpty()){
		return 0;
	}
	
	else if(headName->name.compare(name)==0){
		if(headAge->name.compare(name)==0){ 
			headName=headName->nameNext;
			headAge=headAge->ageNext;
			return true;
		}
		else{  
			headName=headName->nameNext;
			
			NodeNA *curAge=headAge->ageNext;
			NodeNA *prevAge=headAge;
			for(;curAge!=0;){
				
				if(curAge->name.compare(name)==0){
					
					prevAge->ageNext=curAge->ageNext;
					curAge->ageNext=NULL;
				

					delete curAge;
					curAge=NULL;
					return true;
					
				}
				prevAge=curAge;
				curAge = curAge->ageNext;
				
			}
			
		}
		return true;
	}
	else{
		if(headAge->name.compare(name)==0){
			
		}
		else{
		
			NodeNA *curName = headName->nameNext;
			NodeNA *prevName=headName;
		
			for (; curName != 0;){
			
				if(curName->name.compare(name)==0){
					prevName->nameNext=curName->nameNext;
					curName->nameNext=NULL;
				
					NodeNA *curAge=headAge->ageNext;
					NodeNA *prevAge=headAge;
					for(;curAge!=0;){
				
						if(curAge->name.compare(name)==0){
					
							prevAge->ageNext=curAge->ageNext;
							curAge->ageNext=NULL;
				

							delete curAge;
							curAge=NULL;
							return true;
					
						}
						prevAge=curAge;
						curAge = curAge->ageNext;
				
					}
				
					delete curName;
					curName=NULL;
					return true;
				}
				prevName=curName;
				curName = curName->nameNext;
			
			}	
		}
	}
	cout<<endl;
	return false;
}

void PersonList::update(std::string name, int age){
	
	remove(name);
	add(name,age);
	
	

	
}


	

