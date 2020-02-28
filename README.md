# OSMAuth

OSMAuth is what allows Old School Minecraft to authenticate players on the server.

While we generally advise against using this software, you are free to do so.
We don't offer any kind of support if you are unable to get it working.

**Setup**

First, you need to setup a database and table that at least contains the columns 'username' and 'password'.
The passwords must be hashed using the SHA256 algorithm.
The table MUST be named "user", because of an oversight in the coding of this software. This might be addressed in the future.

The only other step is to configure the database connection in the config.

| Key | Description |
| --- | ----------- |
| hostname | The hostname used to connect to the database |
| port | The port to run the authentication server on, standard is 8080 |
| username | The MySQL username |
| password | The MySQL password |
| database | The name of the database to use |
