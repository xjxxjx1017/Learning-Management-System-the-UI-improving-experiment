# Learning Management System
A learning management system and performed experiments on the system to provide a better user interface.

#### Features
* user privilege management
* assignment management
* forums

#### Structures
* client-serve
* server-database
* Flat structure design, single page (similar to Facebook or Tumblr)

#### Technologies
* Tomcat / Java
* HTML5 / Ajax / jQuery
* MySQL
  
#### Improving methods
* HTML5
* Ajax
* D3 (Data-Driven Documents)
* Bootstrap
  
#### Conclusion
  Looking back at our system, Moodle and Blackboard, we are surprised by how much a system relying on new technologies can make a difference. 
  
  When we designed the structure of our system, we relied on Ajax to modify the web content dynamically. It comes naturally that by using Ajax, we tend to perform functions on the same page, which leads to a flat design structure. Without using Ajax, there would be many situations, where we need to either direct users to a new page or bring up a dialogue. 
  
  Commonly used systems like Moodle and Blackboard use Ajax in some places, for example, when uploading assignments, but their structures remain the same because most of their content does not rely on Ajax.

#### Suggestions
##### Ajax
Use it as early as possible in the development stage. It has a possitive impact on the web site structure.

##### Bootstrap
Good for beautification.

##### html5
Provides ease-in, ease-out feature in CSS, good for beautification.

##### D3
Only for visualizing large amount of data, not good for widgets. The reason is that it's graphic system is based on SVG.
