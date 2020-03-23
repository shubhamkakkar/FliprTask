
# Kanban Board by Sankalp

  Working Video Link:- [https://youtu.be/-4WIYgLIVBc](https://youtu.be/-4WIYgLIVBc)
  [PPT Link]()

  ## Apk

  [Apk](https://github.com/sankalpchauhan-me/MPTask/assets/apk/kanban.apk)

## Highlights

- **MVVM Architectural pattern**

- **Used Android Jetpack components**

- Proper Folder Structure

- No third party library (**except MikePenz Material Drawer**)
-  Fast Query
    
-   Draggable Items using Native ItemTouchHelper Class
    
-   Archive Functionality
    
-   Intutive Minimal UI
    
-   Reminders
    
-   Offline Sync
    
-   Attachments


# Gif

<p  align="center">

<img  src="/assets/gif/app.gif"  width="250"  alt="app"/>  <br>

</p>

  

## Application Architecture (MVVM)

![](https://miro.medium.com/max/1622/1*5b-8CCT6MvQrWrep4aQUIw.png)  
With Firebase interacting with repository, the application is designed in a way to be scalable, any backend can be Swapped in future without much changes in codes, be it
AWS, Room, Retrofit, Mongo the application will run smoothly.
With MVVM unit testing also becomes easier (However die to time constraints unit testing has not been performed in this app)
  

# Screenshots

[![TeamBoard](assets/screenshots/1.jpeg)](assets/screenshots/1.jpeg)

[![SignInPage](assets/screenshots/2.jpeg](assets/screenshots/2.jpeg)

[![BoardPage](assets/screenshots/3.jpeg)](assets/screenshots/3.jpeg)

[![HomePage](assets/screenshots/4.jpeg)](assets/screenshots/4.jpeg)

[![CreateBoard](assets/screenshots/5.jpeg)](assets/screenshots/5.jpeg)

[![UpdateCard](assets/screenshots/6.jpeg)](assets/screenshots/6.jpeg)

[![BoardList](hassets/screenshots/7.jpeg)](assets/screenshots/7.jpeg)


# How to build ?


Open terminal and type the below command to generate debug build

`./gradlew assembleDebug`

Open terminal and type the below command to generate release build

`./gradlew assembleRelease`

After running the above command, By default the output can be found in app/build/outputs/apk/debug/apk-debug.apk