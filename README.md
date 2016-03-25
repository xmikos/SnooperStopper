SnooperStopper
==============

Android device ecryption password manager and failed unlock attempts monitor.

SnooperStopper allows you to have a different device encryption password than screen unlock pattern/PIN/password. You can have a strong device encryption password (which you only need to enter once on booting your device) but a simple pattern/PIN/password to unlock your screen many times a day.

If an attacker tries to guess your simple pattern/PIN/password, he has only a few tries (default is 3) after which the device is going to shutdown and he needs to enter your strong device encryption password. This secures you against brute force attacks.

## Why it is needed

Android always sets the device encryption password to the same as the screen unlock pattern/PIN/password. This results in very weak security, because you should set your encryption password as strong as possible. But nobody wants to enter a long password all the time just to unlock screen.

There is Android issue [#29468](https://code.google.com/p/android/issues/detail?id=29468) requesting different passwords for encryption and screen lock, but it seems to be ignored by Google (it is there from 2012 and recently marked _Obsolete_ by Google).

## Usage

### Requirements

- Android >= 4.0.3
- enabled device encryption (_Settings_ => _Security_ => _Encrypt phone_ )
- root (Android doesn't allow apps to change device encryption password or
  reboot your device without root access)

### Installation

<a href="https://play.google.com/store/apps/details?id=cz.eutopia.snooperstopper">
<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60" alt="Get it on Google Play">
</a>

Or (better) download it from [Eutopia.cz F-Droid Repository](https://fdroid.eutopia.cz/).

### Configuration

After installation, start the SnooperStopper app and grant it superuser permissions. Then enable device admin in app, which allows SnooperStopper to monitor failed unlock attempts and a shutdown event if the maximum number is exceeded.

Whenever you change your screen unlock pattern/PIN/password, Android also changes your device encryption password, so you have to set your strong encryption password again. SnooperStopper automatically opens a window where you can change it right after that, so you should never forget about it.

## Credits

Whole device encryption password changing code is taken from Nikolay Elenkov's [Cryptfs Password Manager](https://github.com/nelenkov/cryptfs-password-manager).

## License

See [LICENSE](LICENSE).
