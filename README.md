# <span style="color: green">Arvag</span>

Android application for the project Arvag.

## <span style="color: blue">Geocoding:</span>

For the representation of markers on a map (Google Maps for example), you can use a file (Excel for example) that contains a list of addresses you want to plot and run it through the script I left for you in this repository to get the corresponding coordinates:

_https://github.com/wahbimo/LatLng_

## <span style="color: blue">Image Scraping:</span>

During this project we encountered a challenge of downloading the pictures of the products from a list of product names and their brands. Because the number of images is enormous manual downloading is not an option. Image scraping was the only valid solution I have left as the other solutions like using image APIs dont give any results.

I used *selenium* library to control the firefox browser and made a set of commands to download my images through *duckduckgo* search engine.

Google images is not to use in the case of scraping one or few images at a time because the mapping of the screen changes each time we make a new research (ads, suggestions...). Yandex is a potential search engine for this technique.

Here is the script (to adapt) that I used to download the images of the products:

_https://github.com/wahbimo/image_scraping_
