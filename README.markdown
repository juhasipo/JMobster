JMobster - Java Model to Backbone.js generator
==============================================
Version Preview-Alpha 0.1

### Purpose and Current Status
The purpose of this project is to enable automatic model generation
from Java POJOs to Backbone.js models. It also supports client side
validation generation from standard JSR-303 validation annotations.

The project has just started so the backwards compatibility may break
once in a while due to sudden urges to refactor the code. The current
version is not well tested yet with real Backbone.js client nor
a working server so there will be a lot of bugs.


### Requirements

* JDK 6 (SE or EE)
* Backbone.js
* Backbone.Validations

At the moment only Java to Backbone.js model conversion is supported.
Validation will require [Backbone.Validations](https://github.com/n-time/backbone.validations) plugin
to work. Java dependencies are handled in gradle build file.


Usage
-----

### Basic usage

Basic usage is simple. Create Java objects, use JSR-303 annotations and
give the models to the generator.

    public class UserDto {
        @NotNull
        @Size(max = 255)
        private String fullname;

        @NotNull
        @Size(max = 255)
        private String username;

        @NotNull
        @Min(1900)
        private Integer birthYear = 1900;

        @Size(min = 1)
        private String[] roles = { "VIEW_PAGES", "EDIT_OWN_PAGES" };

        // Getters and setters omitted
    }

And now you can create a model generator instance:

    ModelWriter modelWriter = new StreamModelWriter("models.js");
    ModelGenerator generator = JMobsterFactory.getInstance("Backbone.js", modelWriter);

And give the model class to the generator:

    generator.process(UserDto.class);

Note: You can also give multiple classes or an array of classes.

This will write a Backbone.js model file `models.js` to your working
directory. The default naming strategy will remove Dto suffix from the
model class name.

    /*
     * Auto-generated file
     */
    var Models = {
        User: Backbone.Model.extend({
            defaults: function() {
                return {
                    fullName: "",
                    username: "",
                    birthYear: 1900,
                    roles: ["VIEW_PAGES", "EDIT_OWN_PAGES"]
                }
            },
            validate: {
                fullname: {
                    required: true,
                    minlength: 0,
                    maxlength: 255
                },
                username: {
                    required: true,
                    minlength: 0,
                    maxlength: 255
                },
                birthYear: {
                    required: true,
                    type: "number",
                    min: 1900
                },
                roles: {
                    minlength: 1
                }
            }
        })
    };

### Classes and Default Values

Classes that are processed with JMobster have to have a default constructor. This
constructor is used for field default values. If a field shouldn't have a default
value, annotation IgnoreDefaultValue can be used. This can be handy e.g. for
id values that should not have default value in client side model.

The class fields' visibility doesn't matter and no getters nor setters are required
in order to JMobster to work.

Extending
---------

### Model Processors

Model processors are the basis for creating support for new framework. Model processors takes
models one by one and writes them to a stream (or any other output). Processor implemetations are
given to Model generator which calls appropriate methods of the processor.

Model processor only acts as the basis for the model generation and it depends on other classes. For example
the default Backbone.js implementation uses classes its own ValidationSectionWriter and DefaultValueSectionWriter
for producing corrsponding sections to the output stream. In addition these classes rely heavily on annotation
processors and providers as well as the value converters.

### Annotation Processors

Annotation processors are an important part of the model generation process. For each new framework
user has to create suitable processors for each supported validation annotation. One processor can
process one or more validation annotations at the same time. There are two types of annotations in
the processors - required and optional.

Required annotations have to present for a filed in order to be processed by an annotation processor.
Optional annotations may be present, but processor can work without them.

Each annotation processor must implement few basic features and may implement some extra features. Firstly the
extended class has to define required annotations for the processor. This can be done in the constructor by calling
the appropriate super class constructor. In addition a required type and optional annotations can be given.

    public MaxAnnotationProcessor() {
        super( "number", RequiredTypes.get(Max.class) );
        setBaseValidatorForClass(Max.class);
    }

Secondly the processor has to able to write itself to model writer. For this, the _writeValidatorsToStreamInternal_
method is implemented. In this method, the processed annotation can be aquired with _findAnnotation_ method. For example:

    @Override
    public void writeValidatorsToStreamInternal( ModelWriter writer ) {
        if( containsAnnotation(Max.class) ) {
            Max annotation = findAnnotation(Max.class);
            writer.write( "max: " ).write( "" + ((Max)annotation).value() );
        }
    }

In order to work well with JSR-303 validations, annotation processors have to implement group extracting.
This will basically take the groups parameter data and returns it the caller.

    @Override
    public Class[] getGroupsInternal(Annotation annotation) {
        return ((Max)annotation).groups();
    }

### Annotation Processors Providers

Annotation processor providers are a storage for annotarion processors. Provider is configured to contain all
required annotation processors and it contains the logic for generating the validation for the target
framework.

### Field Value Converters

Field value converters are used for converting field default values to the target framework and language. They
take an object and produce a string value the object represents in the target framework and language.

To add support for new language, a new implementation for interface FieldValueConverter has to be created.
FieldValueConverter takes a Field or a Class, and a defaultValueObject. With these it produces default value
for the given field. The default value is taken from the given defaultValueObjects corresponding field.

### Model Naming Strategies

In order to customize the produced model's name, a model naming strategy can be implemented. It basically
takes a Model object and returns the name for the model.
