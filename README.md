# slf4j-timbre

This project is an [SLF4J](http://www.slf4j.org/) binding (interface) for Clojure's [Timbre](https://github.com/ptaoussanis/timbre) logging framework. It allows Timbre to receive log messages emitted by code designed to use SLF4J.

If your Clojure project depends on a Java library which speaks SLF4J – such as Jetty – but you'd rather all its logs went to your existing Timbre setup instead of needing a separate SLF4J configuration, then this project is for you.

## Usage

Simply add `slf4j-timbre` to your project dependencies:

[![Clojars Project](http://clojars.org/com.fzakaria/slf4j-timbre/latest-version.svg)](http://clojars.org/com.fzakaria/slf4j-timbre)

That is all!

## Other logging frameworks

In addition to SLF4J, `slf4j-timbre` can receive logs from projects designed around Log4j, java.util.logging (JUL), and Apache Commons Logging (JCL). To do this, add the corresponding dependency to your project:
```clojure
[org.slf4j/log4j-over-slf4j "1.7.36"]
[org.slf4j/jul-to-slf4j "1.7.36"]
[org.slf4j/jcl-over-slf4j "1.7.36"]
```

Logs from Log4j/JUL/JCL projects are then forwarded to SLF4J, which in turn forwards them to Timbre.

## Troubleshooting

`slf4j-timbre` requires `[org.slf4j/slf4j-api "1.7.14"]` or later, and `[com.taoensso/timbre "4.3.0-RC1"]` or later.

If you are having problems, ensure your project or its (transitive) dependencies are not pulling in earlier versions of these libraries, as these may shadow the required newer versions. You can check for this using `lein deps :tree`.

For other problems please [open an issue](https://github.com/fzakaria/slf4j-timbre/issues) on GitHub!

## License

Copyright © 2022 [rufoa](https://github.com/rufoa), [Farid Zakaria](https://github.com/fzakaria)

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.