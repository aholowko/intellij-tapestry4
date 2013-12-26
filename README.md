# Tapestry 4.1 Support for Intellij IDEA

Intellij-Tapestry adds basic support for [Tapestry 4.1](http://tapestry.apache.org/tapestry4.1/) development.

## Installing
To install the latest release (and get automatic updates), install this plugin using your IDE's plugin manager:

* In _Settings->Plugins_, choose "Browse repositories".  Find "tapestry" on the list, right-click, then select "Download and Install"

## Features

* `Ctrl-Alt-Shift J` Switching back and forth between page- and component classes and their markup partners
* `Ctrl-Alt-Shift K` Place cursor on a ognl expression in the markup and switch to its associated java code
* Navigatable Linemarker Info in the markup with a clickable link that takes you to the associated java code

## TODO and Wishlist

* Proper OGNL parsing
* Autocompletion on ognl expressions in the template
* LineMarker References from java-code to its usages in templates

## License

This project is licensed under Apache 2.0 see the LICENSE file in the project root

## Issues

* Switching from a java code back to its template does take you to the first hit and will not find other template usages that are declared in a *.jwc file
* *.page files are ignored, only *.jwc files are parsed

## Contributing
Contributions are welcome at the [github project site](https://github.com/linuxswords/intellij-tapestry4)

To get started: `git clone https://github.com/linuxswords/intellij-tapestry4.git`