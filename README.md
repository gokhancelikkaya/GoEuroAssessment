Java Developer Test
===================

You may run the program from command line via following command.

java -jar GoEuroTest.jar "CITY_NAME"

It allows only one parameter and will return error if you provide less or more. If the city name contains more than one word (e.g. New Mexico) write it within quotes (eg. "New Mexico").

Currently the error messages return only in english, but other languages can be added easily via adding a new keyword property file. For example if you add Keywords_de.properties  file and change default language to "de" in config.properties file, you will receive error messages in German.

