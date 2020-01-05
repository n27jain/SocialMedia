# SocialMedia
**Last Update:** 9/16/2019

This project is a shell social media app that is yet to be named. Currently it is called Fame Game.


## Project API's and Technologies:
  - [Android Studio] (Java) (XML) (Kotlin) (IDE)
  - [Firebase] (API) (RealTime DataBase) (Virtual Storage) (Authentication)
  - [CircleImageView] (API package)
  - [Picasso] (API for Downloading and Displaying Images)
  - [Android Image Cropper] (API) (gallery intent)

## Objectives

This app's primary focus is to create an interesting and private social media app for the developer and his friends. The future focus is to release the app for all users.
There are 5 phases to be implemented for the completion of the project.

**Phase 1:** Create the shell App with Register/Sign-In Activity, Posts activity, messenger, friends, settings, and notifications.

**Phase 2:** Create beautify the UI and focus purely on the front end.

**Phase 3:** Insert a game into the app using Unity. (Another side project).

**Phase 4:** Induce a Karma system into the social media app and make it competitive. 

**Phase 5:** Secure app and then release. (Limit Users to save data consumption).
 
## Application Walkthrough

### RegisterOrSignInActivity.java 

![SignInOrRegister](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F1.jpg?alt=media&token=199ba9da-7c81-4d3c-9a02-1558bbbba416)
![SetUp for register](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F2.jpg?alt=media&token=7aa05d3f-5eb2-42e3-a74e-84104cf9f23e)
![Set Profile Image](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F3.jpg?alt=media&token=968e8ddd-a1eb-4410-8325-21de923496ec)
![Country Selection](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F6.jpg?alt=media&token=bdc32b05-940b-4f82-aa21-829e6550873e)
![Gender Selection](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F7.jpg?alt=media&token=ae32a030-8c3b-4519-a8d6-46239f46f2ad)

---(Sign In and  First Time Registration)---

Upon opening the app for the first time user is presented to the register/login screen. Currently user can log in with google or with an email. Future implementation will allow users to sign in with Facebook as  Our app does not store the users password. The data is stored in firebase.
Pressing the google button auto-registers the user. SIGN IN OR REGISTER still needs to be verified with a link in future implementation. 

The set up screen is displayed if the user is found to be new. The ```CircleImageView``` is clickable. If clicked it allows the user to select or take a new profile picture which is then cropped in a circular shape. Country and Gender are scroll-able fields that were custom made using adapters. When the app is published, for every country we want to release the app to we can add to the Country field with its respective int.

Updating the profile will take us to the MainActivity. 

### MainActivity.java

![MainActivity Default Screen](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F8.jpg?alt=media&token=8a315667-0467-424f-afa9-33daccb6a693)

---(Main Screen Containing Fragments)---

This Activity contains most of the apps functionality. By default this screen will have the ```FeedFragment.java``` loaded. This screen allows the user to create a new post and see their posts as well as that of their friends. 

### Feeds

![All Posts of user and friends](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F11.jpg?alt=media&token=b286291a-5e55-431a-a599-f943a3f93c72)
![Example of post object](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F12.jpg?alt=media&token=604ba37b-2926-45d8-85f7-c294ed67d66c)
![Example after posting](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F13.jpg?alt=media&token=a571f534-f687-4af0-9bda-ee48fe9e5610)

---(Feed Fragment)---
This fragment accesses the firebase db to find all posts by the user and their friends and then displays the posts in order from most recent to least recent.

### Friends

![Friends - My Friends](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F9.jpg?alt=media&token=c7533ac8-172f-4544-aa60-aa995b8631ef)
![Friends - Find Friends](https://firebasestorage.googleapis.com/v0/b/socialmedia-310cd.appspot.com/o/ScreenShots%2F10.jpg?alt=media&token=9c170110-e474-49a6-a014-f561d59d1561)

This fragment is accessed once friend button is pressed in the ```MainActivity.java``` file. This fragment contains 2 fragments that can be accessed by pressing either the my friends button or find my friends button. My friends screen displays all your friends that you can filter though with the search bar. Find my friends screen will allow the user to find their friend by inputting the username of their friend. After pressing ADD, the account will disappear from the list of search results. 

```python
import foobar

foobar.pluralize('word') # returns 'words'
foobar.pluralize('goose') # returns 'geese'
foobar.singularize('phenomena') # returns 'phenomenon'
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
I need to create a licence here when crucial parts of the app are complete.

[Android Studio]: <https://developer.android.com/studio>
[Firebase]: <https://firebase.google.com/>
[Picasso]: <https://square.github.io/picasso/>
[Android Image Cropper]: <https://github.com/ArthurHub/Android-Image-Cropper>
[CircleImageView]:<https://github.com/hdodenhof/CircleImageView>
