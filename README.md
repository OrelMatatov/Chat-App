# Chat-App
Multi client chat application using Java Sockets

## Introduction
- This project allows people to communicate with each other through chat
- They have the option to choose their room they would like to communicate
- My incentive to build this kind of project is to practice building a multi-threaded server.

## Set Up
- Run git clone on the command line / download the zip file
- In Cient.java file make sure to allow multiple instances: Run --> Edit Configurations --> Modify options --> check "allow multiple instances" --> Apply and OK
- Run Server.java
- Run Client.java as much as you want: that will determinant the number of clients in the chat

## How To Use The Application
- The user is asked to type his username and the room he would like to join
- Send the message
- The user has the option to leave his current room and enter another one by typing [server]:leave:(the number of the new room). For example [server]:leave:3
- Closing the console window will disconnect and remove the user from the chat

