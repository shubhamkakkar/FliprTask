
# Kanban Board by Sankalp

  Working Video Link:- [https://youtu.be/-4WIYgLIVBc](https://youtu.be/-4WIYgLIVBc)
  [PPT Link]()

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

  

## Application Architecture (MVVM)

![](https://miro.medium.com/max/1622/1*5b-8CCT6MvQrWrep4aQUIw.png)  
With Firebase interacting with repository, the application is designed in a way to be scalable, any backend can be Swapped in future without much changes in codes, be it
AWS, Room, Retrofit, Mongo the application will run smoothly.
With MVVM unit testing also becomes easier (However die to time constraints unit testing has not been performed in this app)

## Apk

[Apk](https://github.com/sankalpchauhan-me/MPTask/assets/apk/kanban.apk)

# Gif

<p  align="center">

<img  src="/assets/gif/app.gif"  width="250"  alt="app"/>  <br>

</p>

  

# Screenshots

[![Home](https://github.com/sankalpchauhan-me/FliprTask/1.jpeg)](assets/screenshots/1.jpeg)

[![Home](https://github.com/sankalpchauhan-me/FliprTask/2.jpeg)](assets/screenshots/2.jpeg)

[![Home](https://github.com/sankalpchauhan-me/FliprTask/3.jpeg)](assets/screenshots/3.jpeg)

[![Home](https://github.com/sankalpchauhan-me/FliprTask/4.jpeg)](assets/screenshots/4.jpeg)

[![Home](https://github.com/sankalpchauhan-me/FliprTask/5.jpeg)](assets/screenshots/5.jpeg)

[![Home](https://github.com/sankalpchauhan-me/FliprTask/6.jpeg)](assets/screenshots/6.jpeg)

[![Home](https://github.com/sankalpchauhan-me/FliprTask/7.jpeg)](assets/screenshots/7.jpeg)


# How to build ?


Open terminal and type the below command to generate debug build

`./gradlew assembleDebug`

Open terminal and type the below command to generate release build

`./gradlew assembleRelease`

After running the above command, By default the output can be found in app/build/outputs/apk/debug/apk-debug.apk