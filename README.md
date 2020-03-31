
<img src="data/logo.png" align="right" width="460" height="239" alt="FlashCardApp">

# Flash Cards App



**What will the application do?**
- List of Flash Cards
- List of Decks that hold Cards
- create flashcards
- create decks
- review your flash cards


**Who will use it:**
- anyone who wants to study/memorize something 
    - students
    - teachers
    - ...

**Why is this project of interest to you?** <br>
For a lot of courses and classes there are a big chunk od the course that needs intensive
revision and using flash cards can be really helpful
some very good and some very basic. 
<br> 

#### User Stories
- As a user, I want to create decks
- As a user, I want to create flashcards
- As a user, I want to use flash cards in decks
    - add a card to a deck
    - remove a card from a deck
- As a user, I want to see the answer of a card
- As a user, I want to be able to save my account which contains decks and cards
- As a user, I want to be the app to save my account automatically when I quit the app
- As a user, I want to be able to optionally load my saved decks and cards when I type my name

- As a user, I want to be able to switch to dark mode and normal 


**Instructions for Grader**

- You can create Deck by:
    - first click launch button
    - click create account -> click on text box -> type name -> press enter
    - click create deck -> click on text box -> type name -> press enter
    
- You can create + add a flash card a flashcard by:
    - first click launch button
    - click create account -> click on text box -> write name -> press enter
    - click create deck -> click on text box -> type name -> press enter 
        - you need a deck before adding a flashcard. if a deck already exists it can be skipped
    - click create Flash Card -> click on text box -> type name -> press enter -> type card's back -> press enter
        - click on a deck box on the left side to add it to the deck
    
    
- You can see flashcards front and back by:
  - first click "decks" button
  - click on any previously made deck
  - click on any previously made cards in shows in the center
     - you can click more to flip the card again
     
- You can remove a flash card a flashcard by:
    - after creating a deck and a flashcard and adding it to the deck explained above
    - click on decks -> click on the deck of choice on the left
    - click on edit button -> click on the flashcard you want to delete -> confirm pop up -> select other flashcards to delete or click done button to stop editing mode
   
- You can remove a deck a flashcard by:
    - after creating a deck (optional: and a flashcard and adding it to the deck) explained above
    - click on "decks" button
    - click on edit button -> click on the deck you want to delete -> confirm pop up -> select other decks to delete or click done button to stop editing mode
   

- You can locate my visual component by clicking the moon/sun button on the bottom/right corner and it wil change between dark and light mode 
 
- You can trigger my audio component by causing an error by doing one of these
    - first click launch button
    - click launch -> click create account -> click on text box -> type that already exists as a save for example joe -> press enter
    - click launch -> click load account -> click on text box -> type name that has not been saved -> press enter
    - click create flash card account (without creating a deck) -> press enter
    
- You can save the state of my application by
 - after creating/loading account -> click the save button and it saves/ or quit because it saves automatically when you quit
 
- You can reload the state of my application by
    - first click launch button -> click load account -> click on text box -> type name that has been saved -> press enter
    - (for testing) the app initially has accounts saved with names: Joe, Mark
    
    
**Phase 4: Task 2**
- option 1: Test and design a class that is robust.  You must have at least one method that throws a checked exception.  You must have one test for the case where the exception is expected and another where the exception is not expected.
- FlashCard Class is the class that has been designed to be robust 
    - it checks where the flashcard app front and back are shorter than MAX_CARD_LENGTH or not
    
**Phase 4: Task 3**
- change 1: coupling
- I fixed the problem in the GUI class where changing the width and height would casue problems since the size of the buttons wouldn't change.
    - I made constants for width and height and the buttons are created based on them