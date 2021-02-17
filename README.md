```

Building a Full Stack – CPlayer App
Milestone 1
Create REST API to fetch data
Create a static view. This view should have angular route path as /my-recommendations.
It should contain two sections:
Search section
Recommendation section
Create a search bar with a search button to search Player.
Search section will have text field with ID #search-text-field
A button to submit search text field content with ID #search-button
On submission of search text should query the results from cricapi
Display search results.
Get the results displayed in search results section.
Give an id #search-results to search result section.
Search results should show a series of card like components and a card should have the following attributes.
Assign .cplayer-card class to each player card and all the cplayer cards displayed in all sections should have the below details with attributes.
assign .cplayer_fullName class to describe FullName.
assign .cplayer_majorteams class to to describe majorteams.
assign .cplayer_imageUrl class to show image.
assign .cplayer_country class to show country name.
toggle .recommend & .unrecommend classes to recommend and unrecommend buttons.
Create a recommend button attached with every player card. Give recommend button a class .recommen
Click on Recommend button and the button should change to Unrecommend.
The Unrecommend button should have a class .unrecommend and Recommend button should have class .recommend.
View recommended cplayer list under my-recommendations section
Display all recommended player in this section. Recommended player should be displayed under my-recommendations section.
Unrecommend button, button should change to Recommend again, players should disappear from my-recommendations section
Milestone 2
Create a Dashboard view (Angular Route /dashboard) with three sections Display Favorite, Player statistics, recommendations for a player from cricpai one under the other.
This Dashboard is the default view to be shown.
The 3 sections are:
Favorite with Id #Favorite.
Player statistics with ID #pstatistics.
Recommendations tracks with ID #recommended
View all Favorite players under Favorite section
Display all Player statistics under Player statistics section
View all players recommendations from 3rd party tracks service provider (cricapi) under recommendations section
Milestone 3 - Functional Requirements
Pagination
In case of multiple records page should have pagination option to display data in card layout
Implement Authentication
User should not be able to add anything in favorites & recommendation list until logged in.
Create Login Page
Create Register Page (User's email address should be a userid)
Create Edit Profile / Change Password page (Email address cannot be changed)
Upload profile image while register & displaying the same in toolbar after login
Encrypt password using bcrypt while storing in database during registration
Milestone 4
Implement Test Automation – write Unit Tests for Backend and FrontEnd.
Add E2E Test Cases, Dockerize and Implement CI using Gitlab Runner.
Create the README.md file with Steps to Execute.
Acceptance Criteria
The final evaluation of submissions if the following is achieved:
Meeting the Core functional requirements as stated
Write Test Cases with each functionality with all the features
Follow the MVC model
Evaluation Rubrics (On a Rating from 1 to 10 – wherein 1 is Non Existent to 10 being Exceeded Expectations)
Functional Requirements
Non-Functional Requirements
Code Quality
Standards, Styles and Guidelines
Aesthetics and Accessibility

```