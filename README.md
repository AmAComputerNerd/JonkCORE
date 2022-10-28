# JonkCORE
A core plugin containing classes and utility systems for my other plugins.

## So... why?
I've been getting pretty sick of more or less copy-pasting stuff from project to project. For example, I use a User class as a wrapper for a player, as it enables me to set custom values per plugin based on my needs (i.e. my uStaff plugin with storage necessary for a player's staffmode status and inventory).

Eventually, I got so sick of having to do this I decided; ***Fuck it**, time to try my hand at making a core/library plugin.*

Sooo... that's when I made this plugin, named it something random for now but name isn't all that important.

## Features
I'm pretty terrible at writing this stuff - I'm used to doing standalone systems that work seemlessly with the plugin they're implemented in, so the problems I would need to overcome here would be creating a system that enables:
  * **Seemless transfer** - I need a way to convert different instances of an object between plugins, and more over, concrete methods of accessing these sub-plugin objects.
  * **Adaptability** - Although all of my sub-plugins typically utilise the same/similar architecture, I needed a way to ensure that the objects were as adaptable as possible. In particular, that data could be seemlessly transferred between two plugins **without any data loss**.

I went through various iterations before I reached this final (sorta, I'm gonna be honest - it's not final) method of achieving this. In case you're interested (you aren't), I'll list those at the bottom of the README.
My final iteration involves a combination of interfaces and a special storage system for object data, Attributes. To put it simply, an Attribute represents an unimplemented variable (when compared to the base implementation / interface).
Attributes are stored within objects implementing the <code>CrossPluginObject</code> interface, and such objects share attribute methods and a <code>pack()</code> & <code>unpack()</code> method.

For example, in the case that a conversion needed to be made between an external implementation of <code>IUser</code>, let's call it <code>USUser</code>, and the default implementation within JonkCORE (<code>User</code>), the following would need to be done:

```
  // let 'usUser' be the instance of USUser
  
  // first, we pack the object to convert it back to a generic IUser
  IUser generic = usUser.pack();
  // next, we create a blank copy of the User object
  User converted = User.empty();
  // finally, we copy the generic object's data to the User object
  converted.unpack(generic);
  
  // This may also be simplified to 2 lines, or even 1 line if used as a method parameter.
  User converted = User.empty();
  converted.unpack(usUser.pack());
  
  // 1 line example with a method
  System.out.println(User.empty().unpack(usUser.pack()));
```

Alternatively, if you know what plugin the provided object comes from ~~and have the workspace linked as a library~~, you may also use a <code>UserConverter</code> to simplify the conversion.

```
  // let 'plugin' be the instance of the sub-plugin.
  // let 'user' be a generic IUser.
  
  // retrieve the instance of UserConverter
  IUserConverter genericConverter = plugin.getUserConverter();
  // convert the user. the resulting user would be an instance of that sub-plugin's User object, though in this case we will just assign it to a generic IUser variable
  IUser convertedUser = genericConverter.convert(user);
```

## Finishing up
It's definitely not my best work, but considering it's my first time making such a library I think I did alright. Is there some stuff that'll need to change? **YES!**
I'm already planning to re-do the ChatRoom code / system for example, but I still do consider this plugin a partial success just considering the fact *it works!*

## Will you hurry up and show us your failed attempts?
Alright geez, see below ig. I'll give a brief description of each:

  - **Attempt 1:** I tried to do a 'one object fits all' approach - newsflash, **shit** idea. I found the whole thing way too limiting and annoying to code around, so I scrapped it.
  - **Attempt 2:** I attempted to make parent superclasses within this plugin which others could extend to provide functionality. The main thing that killed this idea was the storage problem - plugins would need to make their own events for joining and leaving for their custom objects, which would then conflict with each other in the *singular storage file* if there were multiple plugins that used this as a library installed. It didn't end up surviving long after that, though you can still see remnants of the idea in my main plugin file with the variable-assigned events.
  - **Final Attempt:** In my final attempt, I realised the solution was going to be interfaces - where each plugin that used this library could create their own storage, user, and chatroom instances to work in synchronocity with other plugins. This solved the massive problem with storage in the previous attempt, as each plugin could handle their own events, objects and functionality without directly affecting other plugins.

-----

*stop reading already, you've done enough for today*
  
