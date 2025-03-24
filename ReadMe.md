# Repository For Families - Backend

# What is it?
Repository For Families is a project whose goal is to allow families to better communicate
with each other. There features of this app revolve around common situations that arise in a family whose solution
can be provided by an application.

For example: One common situation a family faces is where two members have a irregular work schedule and both share
a car. The problem that arises in this situation is where both members need to 
ask each other for their schedule and arrange a plan to share their car. 
This back and forth becomes tedious to the point that one may forget to ask 
completely or the other may end up giving the wrong answer. The solution this application
provides is a online calendar where members post their schedule for other family memebers to view. With their scheduled
events they can attach shared resources that notify family members how each shared resource is going 
to be used for the day. 

# What was used to implement it
Spring boot is the web framework that was used to create this application. Why? I'm familiar with it and 
I'm familiar with java so using spring was the best choice. 

# What features are implemented?
* ## Users
Users are represented with an email, password, first name, and last name. Sign up and log in were implemented using
the Spring Framework. Namely creating a custom authentication provider (which is email and password) and checking if it is
available in the database. 

* ## Groups
Groups are needed to implement all other features of the application. Groups are meant for all members of a family to join.
Groups have 3 main attributes and id (uuid), name, and the owner (which is a foreign key that references a user). Users can create a group by
providing a name and once it is created they are given admin privileges. User can join a group by copying and pasting the id
of the group. If it is valid they must wait for an admin of the group to approve their entrance. Once apart of a group users 
can then take advantage of other features.  

See GroupsController to see all api endpoints related to groups.  
 

* ## Events
Events are dates that some user wants to let other members know that something will happen. The fields are self explanatory. There are no restrictions on the events that a user can post to the group.
The only restriction is that is the user enters a start time they must also enter an end time.

* ## Resources
Resources are shared items in a group. Resources are defined mainly by an id(uuid), name and optional description. There is also a type attribute for future
features. Resources can be attached to an event and signifying that the resource will be used for the duration (or longer) of the event. Only the owner of the
resource can approve the use of the resource. 

* ## Lists
To be implemented

* ## Announcements
To be implemented

# How is the project structured?
The project is structured based on the features that are available within the application. For example, all the code related 
to the announcement feature is all contained in one folder and aptly named announcement. Each folder follows this pattern.
