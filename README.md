# Source code for project "INTERACTIVE GESTURE-BASED MUSIC ON A MOBILE PHONE"


To install, you must first have the neccessary OpenCV Packages on your phone. To download them, go to https://opencv.org/releases/

This project uses OpenCV Version 3.4.0. JSyn library is also needed for the installation of this project.

A min SDK version of 15 is needed for this Android project, with the target SDK version being 26.

This project was written using the Android SDK, using Java. Android Studio was used as the IDE.


# Using the application

To enter playing mode, you must first at least have ONE scale saved into the database, and ONE synth patch.

To create a simple C major scale 
 - Go to Create -> Notes/Chords
 - Type in 8 for 'number of chords', swipe right.
 - On the ADD NEW buttons, press them once, and you will see a dropdown menu with the Note name.
 - Select the notes that form the C major scale, whereby the first ADD NEW button should correspond to lowest note, and the bottom ADD NEW button corresponding to the highest note.
 - Press Enter, and type in a name for the scale
 
 To create a simple synth patch, simply go to NEW SOUND and press 'SAVE', and type in the patch name. This will use the default synth parameters as a synth patch.


# Important class files 

MainActivity - The application starts of in the MainActivity, and it includes a menu screen to enter playing mode, or to create a new sound or scale

EditSoundActivity - The activity responsible for creating a new synthesizer patch. In this activity, the parameters of the synthesizers are displayed on screen. To hear the sound of the synthesizer, simply press "PLAY SOUND". To save the patch, simply press "SAVE". (Ignore the buttons "PLAY" and "STOP", they are bugged buttons that don't really do anything.)





 
