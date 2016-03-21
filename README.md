SnooperStopper
==============

Android device ecryption password manager and failed unlock attempts monitor

SnooperStopper allows you to have different device encryption password than
screen unlock pattern/PIN/password. You can have strong device encryption
password (which you only need to enter once after booting your device) but
simple pattern/PIN/password for unlocking your screen.

If attacker tries to guess your simple pattern/PIN/password, he has only
few tries (default is 3) after which the device is rebooted and he needs
to enter your strong device encryption password again.


Where to get it:
----------------

<a href="https://play.google.com/store/apps/details?id=cz.eutopia.snooperstopper">
<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60" alt="Get it on Google Play">
</a>

Or download it from [Eutopia.cz F-Droid Repository](https://fdroid.eutopia.cz/).


Why is it needed:
-----------------

Android always sets device encryption password same as screen unlock pattern/PIN/password.
This is very unfortunate, because you should have encryption password as strong
as possible, but nobody wants to enter long password all the time just to unlock screen.

There is Android issue [#29468](https://code.google.com/p/android/issues/detail?id=29468)
requesting different passwords for encryption and screen lock, but it seems to be
ignored by Google (it is there from 2012 and recently marked _Obsolete_ by Google).


How to use it:
--------------

After installation, start SnooperStopper and grant it superuser permissions. Then
enable device admin in app, which allows SnooperStopper to monitor failed screen
unlock attempts and reboot device if maximum number is exceeded.

Whenever you change your screen unlock pattern/PIN/password, Android also changes
your device encryption password, so you have to set your strong encryption
password again. SnooperStopper automatically opens window where you can change it
right after you change your screen unlock pattern/PIN/password, so you should never
forget about it.


Requirements:
-------------

- Android >= 4.0.3
- enabled device encryption (_Settings_ => _Security_ => _Encrypt phone_ )
- root (Android doesn't allow apps to change device encryption password or
  reboot your device without root access)


Credits:
--------

Whole device encryption password changing code is taken from Nikolay Elenkov's
[Cryptfs Password Manager](https://github.com/nelenkov/cryptfs-password-manager).
