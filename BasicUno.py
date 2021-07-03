import random
import sys

class UnoCard:
    
    Colors={1:"Yellow",2:"Red",3:"Green",4:"Blue"}
    
    def __init__(self, c, n):
        self.c=c
        self.n=n
        
    def __str__(self):
        return self.Colors[self.c]+" "+str(self.n)
        
    def canPlay(self,other):
        if self.c==other.c or self.n==other.n:
            return True
        else:
            return False

class CollectionOfUnoCards:
    
    def __init__(self):
        self.Cards=[]
        
    def addCard(self,c):
        self.Cards.append(c)
    
    def makeDeck(self):
        for k in range(2):
            for i in range(1,len(UnoCard.Colors.keys())+1):
                for j in range(1,9+1):
                    self.addCard(UnoCard(i,j))
            
    def shuffle(self):
        for i in range(len(self.Cards)):
            index1=random.randint(0, len(self.Cards)-1)
            index2=random.randint(0, len(self.Cards)-1)
            self.Cards[index1],self.Cards[index2]=self.Cards[index2],self.Cards[index1]

    def getNumCards(self): 
        return len(self.Cards)

    def __str__(self):
        collectiontostr=""
        for i in range(len(self.Cards)):
            collectiontostr=collectiontostr+str(self.getCard(i))+"\n"
        return collectiontostr
        
    def getTopCard(self):
        return self.Cards[len(self.Cards)-1]
        
    def canPlay(self, c):
        for i in range(len(self.Cards)):
            if self.Cards[i].c==c.c:
                return True
        for i in range(len(self.Cards)):
            if self.Cards[i].n==c.n:
                return True
        return False
        
    def getCard(self, index):
        return self.Cards[index]
        
    
    
class Uno:
    
    def __init__(self):
        
        self.deck=CollectionOfUnoCards()
        self.deck.makeDeck()
        self.deck.shuffle()
        
        self.lastPlayedCard=None
        
        self.hand1=CollectionOfUnoCards()
        self.hand2=CollectionOfUnoCards()

    def playGame(self):
        #dealing 7 cards to each player.
        
        for i in range(7):
            self.hand1.addCard(self.deck.getTopCard())
            self.deck.Cards.remove(self.deck.getTopCard())
        for i in range(7):
            self.hand2.addCard(self.deck.getTopCard())
            self.deck.Cards.remove(self.deck.getTopCard())
            
        print("Player 1 Cards:")
        print(self.hand1)
        print("Player 2 Cards:")
        print(self.hand2)

        
        
        self.lastPlayedCard=random.choice(self.hand1.Cards) # Start game
        print("Player 1 discards first card... :")
        self.hand1.Cards.remove(self.lastPlayedCard)
        print(self.lastPlayedCard)
        
        playNum=2
        
        while True:
            
            self.playTurn(playNum)
            if(playNum==2):
                playNum=1
            elif(playNum==1):
                playNum=2
                
                
            if(len(self.hand1.Cards)==0):
                sys.exit("Player 1 won the game !")
            elif(len(self.hand2.Cards)==0):
                sys.exit("Player 2 won the game !")
            elif(len(self.deck.Cards)==0):
                sys.exit("Game is a draw !")


        
    def playTurn(self, player):
        
        takeCard=True
        if player==1:
            for i in range(len(self.hand1.Cards)):
                if self.hand1.canPlay(self.lastPlayedCard)==True:
                    
                    while True:
                        x=random.choice(self.hand1.Cards)
                        if(x.canPlay(self.lastPlayedCard)==True):
                            self.hand1.Cards.remove(x)
                            self.lastPlayedCard=x
                            takeCard=False
                            print("Player 1 discarded:")
                            print(x)
                            return
                    
                    
            if takeCard==True:
                        
                self.hand1.addCard(self.deck.getTopCard())  
                print("Player 1 gets :")
                print(self.deck.getTopCard())
                self.deck.Cards.remove(self.deck.getTopCard())
                
                
                    
        if player==2:
            for i in range(len(self.hand2.Cards)):
                if self.hand2.canPlay(self.lastPlayedCard)==True:
                    
                    while True:
                        x=random.choice(self.hand2.Cards)
                        if(x.canPlay(self.lastPlayedCard)==True):
                            self.hand2.Cards.remove(x)
                            takeCard=False
                            self.lastPlayedCard=x
                            print("Player 2 discarded:")
                            print(x)
                            return
                    

            if takeCard==True:
            
                self.hand2.addCard(self.deck.getTopCard())
                print("Player 2 gets :")
                print(self.deck.getTopCard())
                self.deck.Cards.remove(self.deck.getTopCard())            
                    
    
                

    #def printResult(self): # Results are printed in playGame function



def main():
    my_game = Uno()
    my_game.playGame()

main()
