# sbt-raml2md #

An SBT plugin that uses [webapi-parser](https://github.com/raml-org/webapi-parser), 
[Twirl (Play template engine)](https://github.com/playframework/twirl) and 
[Remark](https://github.com/giflw/remark-java) to convert RAML 1.0 into 
readable markdown files that can be used as documentation for your RESTful APIs.

## Usage ##

Add sbt-raml2md as a dependency in `project/plugins.sbt`:

```scala
resolvers += Resolver.bintrayIvyRepo("peterhazell", "sbt-plugins")

addSbtPlugin("peterhazell" %% "sbt-raml2md" % "0.1.0")
```

> sbt version 0.13.5 and scala version 2.12.2 or higher are required to use this plugin.

From the root directory of your project, run ``sbt raml2md``, this will find any RAML files and output the resulting markdown files.

## Output ##
Separate markdown files will be produced containing the high level API documentation as well as more detailed documentation for each endpoint and associated HTTP method your API supports.

#### Documentation currently supported ####

- API name
- Methods and paths
- Names and descriptions
- Query parameter information
- URI parameter information
- Request body JSON schemas
- Response HTTP status codes
- Response body JSON schemas

> Examples are not currently supported due to an issue with the underlying 
RAML parser being used, the issue has been raised as a bug [here](https://github.com/raml-org/webapi-parser/issues/34).
