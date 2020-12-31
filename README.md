# News About Europe for Android


**About the program**

You can download the app from here: http://news-about-europe.herokuapp.com/, after you choose "Android version" menu.

As a user, you can select a news agency and a country on the main screen of the app. Then you get the list of articles about the country, from the news collection of the selected agency. You can scroll the list, and if you touch an article title, the article will be opened in your browser. You can add/delete the article to/from your favorites with the icon at the end of the article\'s line. By default, articles are sorted by publishing date, in descending order. If you touch the name of a column, the list will be reordered by title, author or date. You can change descending and ascending order, just touch again the column's name.

As a user, you can list your favorite articles. You can delete articles from favorites. You can reorder this list too, with touching column names. 

The app gets article datas from News API (https://newsapi.org/). As a user, you can save your own API key.


**About programming**

With this project I have wanted to practice sending request to a REST API from an Android app, and work up the data package which comes from the API.

I practiced simple locally data storage too. The app stores API key and data of favorite articles in the Shared Preferences.

The project also was a chance to learn the foundations of responsive UI design -- I applied ConstraintLayout, and layouts have portrait and landscape orientation too.

The next step of development will be creating dual pane view for tablets.