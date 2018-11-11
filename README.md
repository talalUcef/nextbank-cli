# Project spec
[See the project spec](https://gist.github.com/abachar/d20bdcd07dac589feef8ef21b487648c#file-kata-md)

# Solution details
In this project, for simplicity, a customer can have one account, 
and the bank has a single type of account.  

A customer therefore can deposit and withdraw money into and 
from his account.

Since the app domain is relatively simple for this basic solution,
it was not to add complex design patterns.
 
# Run the project on local
The project is a java 10 and spring shell based project.

To build the project use : `mvnw clean install`

to run the project use : `java -jar target/nextbank-cli-{version}.jar` 

The system do not provide the functionality of adding new customers for now,
but it provide a list of customers for test purpose :
- Guido van Rossum
- Yukihiro Matsumoto
- James Gosling
- Dennis Ritchie
- Bjarne Stroustrup
- Rasmus Lerdorf
- Larry Wall
- Brendan Eich

All customers username and password are first name in lowercase, 
so for Guido van Rossum, the username is guido and password is guido

# Tips
The spring shell project do not integrate very well with spring test, 
that why for now the project do not have integration and unit tests, 
they will be added in the future.