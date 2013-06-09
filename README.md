JMobster - Java Model to Backbone.js generator
==============================================
Version preview-alpha 0.3

### Purpose and Current Status

The purpose of this project is to enable automatic model generation from Java POJOs to Backbone.js models. It also
supports client side validation generation from standard JSR-303 validation annotations. At the moment it's still
more of an proof of concept type of project, but in future as I found some use for it may become more mature.

The project has just started so the backwards compatibility may break once in a while due to sudden urges to refactor
the code. The current version is not well tested yet with real Backbone.js client nor a working server so there will
be a lot of bugs.

![JMobster process](https://raw.github.com/juhasipo/JMobster/master/img/process.png)


### Requirements

**Java**

* JDK 6 (SE or EE)
* [SLF4J](http://www.slf4j.org/)
* JSR-303 annotations (e.g. [hibernate-validation](http://www.hibernate.org/subprojects/validator/download.html))

**JavaScript**

* Backbone.js (Tested only with 1.0.0)
* [Backbone.Validation](https://github.com/thedersen/backbone.validation) (Tested only with v0.7.1)

At the moment only Java to Backbone.js model conversion is supported. Validation requires
Backbone.Validation plugin to work. Java dependencies are handled in Gradle build file.


Usage
-----

### Basic usage

Basic usage is simple. Create Java classes, use JSR-303 annotations and give the classes to the generator.
```java
public class UserDto {
    @NotNull
    @Size(max = 255)
    private String fullname = "John";

    @NotNull
    @Size(max = 255)
    private String username = "Smith";

    @NotNull
    @Min(1900)
    private Integer birthYear = 1950;

    @Size(min = 1)
    private String[] roles = {};

    // Getters and setters omitted
}
```

JMobster works in two phases. In the first phase JMobster models are generated from Java entities/beans/DTOs with
*ModelFactory*. In the second phase these models are given to JMobster *ModelGenerator* which processes and converts
the given models to appropritate target platform format.

In the next example a *ModelFactory* is created and then it is used to create JMobster models
from three DTO classes:
```java
ModelFactory factory = JMobsterFactory.getModelFactoryBuilder()
                .setFieldScanMode( FieldScanMode.DIRECT_FIELD_ACCESS )
                .setFieldGroups( GroupMode.ANY_OF_REQUIRED )
                .setValidatorFactory( new JSR303ValidatorFactory() )
                .build();
Collection<Model> models = factory.createAll( MyModelDto1.class, MyModelDto2.class, MyModelDto3.class );
```
This *ModelFactory* is same for all target languages and frameworks.

In the next example a *ModelGenerator* is configured which then will take previously created JMobster models:
```java
// Setup writers
JavaScriptContext context = new JavaScriptContext(new StringBufferWriter(), OutputMode.JSON);

// Setup generator
FieldValueConverter converter = new JavaToJSValueConverter(
        ConverterMode.NULL_AS_DEFAULT,
        EnumConverter.EnumMode.STRING,
        JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN
);

BackboneModelProcessor backboneModelProcessor =
        new BackboneModelProcessor
            .Builder(context)
            .setValueConverter(converter)
            .setModelProcessors(
                    new DefaultValueProcessor.Builder()
                            .build(),
                    new ValidatorProcessor.Builder()
                            .setValidatorWriterManager(new ValidatorWriterSet(JSR303Validators.get()))
                            .build()
            )
            .build();
            
ModelGenerator generator = JMobsterFactory.getModelGenerator(backboneModelProcessor);
generator.processAll( models );
```
First a *FieldValueConverter* is set up. In this case we use *JavaToJSValueConverter* which does as the name suggests,
converts Java to JavaScript. 

Next we create a *ModelProcessor* which actually converts given class fields, in this case a *BackboneModelProcessor*.
The *BackboneModelProcessor* has a convenient builder which enables easy customization of the processor. The
*setModelProcessors* method allows you to supply on or more processors and they will be executed in the given order.
The builder requires a *LanguageContext* that is used for writing the validation rules. The given *LanguageContext* is
used for all *ModelProcessors* given to *BackboneModelProcessor*.

Finally we create a *ModelGenerator* which can take our previously generated *Model* objects and generates
our output.


### Property Scanning

There are two field scanning mode in JMobster: _DIRECT\_FIELD\_ACCESS_ and _BEAN\_PROPERTY_. The _DIRECT\_FIELD\_ACCESS_
mode is the default mode and in it every member variable will be recognized as a field.  The validator annotations must
be written for the member variables. The mode doesn't care about the visibility of the fields, so all public, protected
package and private fields are considered equal in that sense. Also every member variable from super classes will be
included.

**Notice**: When using _DIRECT\_FIELD\_ACCESS_ mode the scanning may be restricted by Java's *SecurityManager*.

_BEAN\_PROPERTY_ mode will use standard getter methods to find available model fields (e.g. _getName()_ for field "name").
This mode will ignore _getClass()_ getter. in this mode the annotations must be written to the getter methods in order
them to work since it won't be possible to find the corresponding member variable just with the method name (the getter
can return formatted or combined field data). In this mode, only the public getters are taken into account so any other
getters won't show up in the model. Like in _DIRECT\_FIELD\_ACCESS_ mode the super classes are included in scanning.

In the next example a simple class is generated by using the different field scanning modes.

```java
public class ScanningModeDemo {
    @Pattern(regexp = "[\\w]*")
    private String firstName = "John";
    @Pattern(regexp = "[\\w]*")
    private String lastName = "Doe";

    @Size(min = 0, max = 255)
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
```

In _DIRECT\_FIELD\_ACCESS_ this will result:

```javascript
var Models = {
    ScanningModeDemo: Backbone.Model.extend({
        validate: {
            firstName: {
                pattern: /[\w]*/
            },
            lastName: {
                pattern: /[\w]*/
            }
        }
    })
};
```

In _BEAN\_PROPERTY_ mode this will result:

```javascript
var Models = {
    ScanningModeDemo: Backbone.Model.extend({
        validate: {
            fullName: {
                maxlength: 255
            }
        }
    })
};
```

In both modes the _IgnoreField_ annotation will ignore the field and it won't be used in the generated model. For
_BEAN\_PROPERTY_ mode the annotations must be written for the getter method and in _DIRECT\_FIELD\_ACCESS_ for the
member variable.


In addition to scanning modes, there are extra settings that determine what kind of fields are included. The current
options are to toggle static and/or final fields. Final field mode affects both scanning modes, but static field mode
only works with _DIRECT\_FIELD\_ACCESS_ mode. By default final fields are included but static fields are not.

### Validator and Field Groups

JMobster uses JSR-303 like groups for filtering validators and fields that are added to output models. Like JSR-303
also JMobster supports group inheritance. Groups are specified with interfaces. For validators the groups are given
as the JSR-303 *groups* attribute. For fields there is a special annotation *FieldGroupFilter* which also contains
*groups* attribute. The *groups* takes a list of classes that specify which groups the validator or filter belongs to.

In addition JMobster also supports different grouping modes. The grouping mode will determine how the validator and
field groups are interpreted. Supported grouping modes are:

1. *GroupMode.ANY_OF_REQUIRED*
2. *GroupMode.EXACTLY_REQUIRED*
3. *GroupMode.AT_LEAST_REQUIRED*

The default mode is *GroupMode.ANY_OF_REQUIRED* which allows validator or field to be included in model if any of the
specified groups exist in the validator or field. *GroupMode.EXACTLY_REQUIRED* mode will include validator or field when the
group combination is exactly the same. *GroupMode.AT_LEAST_REQUIRED* mode will include validator or field when there
are at least the specified groups.

Groups and groupmodes can be given to *ModelFactory* when building the class and, after the factory has been built,
with *setValidatorFilterGroups* and *setFieldFilterGroups* methods.

```java
ModelFactory factory = JMobsterFactory.getModelFactoryBuilder()
                .setFieldScanMode( FieldScanMode.DIRECT_FIELD_ACCESS )
                .setFieldGroups( GroupMode.ANY_OF_REQUIRED )
                .setValidatorGroups( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class )
                .setValidatorFactory( new JSR303ValidatorFactory() )
                .build();

factory.setValidatorFilterGroups(GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class);
factory.setFieldFilterGroups(GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class);
```


Default Process
---------------

JMobster has default implementations for standard JSR-303 validations and a set of default implementations for
processing models. The default classes should work for most cases. There is also a small subset of Hibernate's
own validators.

Supported JSR-303 validators

 * NotNull
 * Min
 * Max
 * Pattern
 * Size

Supported Hibernate validators

 * Email
 * Length
 * NotEmpty

Configuring
-----------

### Custom Validators

To support new validators you will have implement several classes.

#### Validator Annotation

The first phase is to create your annotation. The annotation should have a method called
*groups* which has to return an array of *Classes*. This method is used for filtering annotations
when constructing *ModelFields*.


#### Validator Class

The next phase is to create a wrapper for your annotation. The wrapper will be used in later phase of the process
to get annotation values that should be written to *DataWriter*. These validator wrapper classes are generic and
language independent. They only need to be implemented once per annotation.

Here is an example of *MyValidator* for a custom *MyAnnotation* validator annotation:
```java
public class MyValidator extends BaseValidator {
    private String requiredValue;
    private String optionalValue;

    @InitMethod
    public void init(MyAnnotation myAnnotation, Optional<MyOptionalAnnotation> myOptionalAnnotation) {
        this.requiredValue = myAnnotation.value();
        if( myOptionalAnnotation.isPresent() ) {
            this.optionalValue = myOptionalAnnotation.value();
        }
    }

    // Getters
}
```

As you can see, the *MyValidator* contains a two fields *requiredValue* and *optionalValue*. Values is set in the *init* method
which takes required and optional annotations as parameters. These parameters are used to determine what annotations
the validator requires. Required annotations are given as is in the argument list and optional annotations are given
by using an *Optional* class with the desired optional annotation as generic parameter. Difference
between required and optional annotations is that required annotations are quaranteed to be present when the
*Validator* is initialized. Optional annotations are not. You must always check that optional parameters are present
before using them. Otherwise an *IllegalStateException* is thrown.

In case the validator doesn't need any data from annotations, you can configure the required annotation(s) with
*RequiresAnnotations* annotation which takes one or more annotation classes as parameter:

```java
@RequiresAnnotations({Annotation1.class, Annotation2.class})
public class MyNoDataValidator extends BaseValidator {
}
```


#### Configuring Factory for the Validator

Now that you have your *Validator* class, it requires required and optional annotations. This is done in
*ValidatorFactories*. You have two choises:

1. Use *DefaultValidatorFactory and configure* it to use your own custom validator
2. Create your own *ValidatorFactory* which you configure by yourself

Using the *DefaultValidatorFactory* is the easiest choise and it will contain supported JSR-303 validators. You can
configure like following:

```java
DefaultValidatorFactory validatorFactory = new DefaultValidatorFactory();
validatorFactory.setValidator(MyValidator.class);
```

This will add *MyValidator* class to the factory. Required and optional parameters are automagically checked
from the init methods.

#### ValidatorWriter Class

Now you have your validator configured. Next you can make your *ValidatorWriter*. *ValidatorWriters* are language
and framework specific. They get previously written *Validator* class as parameter.

Here is an example of a *ValidatorWriter*:
```java
public class MyValidatorWriter extends BaseValidatorWriter<MyValidator, JavaScriptContext, JavaScriptWriter> {

    @Override
    protected void write( JavaScriptContext context, MyValidator validator, ItemStatus status ) {
        String value = validator.getRequiredValue();
        if( validator.hasOptionalValue() ) {
            value += validator.getOptionalValue();
        }
        context.getWriter().writeKeyValue("value", value, isLast);
    }
}
```

The example implementation uses a *JavaScriptContext* *LanguageContext* which can be used for the output. The first
generic parameter must match your *Validator* (in this case *MyValidator*) and the second is the *LanguageContext* used.
The third parameter is the *DataWriter* used and must match the writer in the *LanguageContext*.
The *LanguageContext* and *DataWriter* must match the ones that your framework's *ValidatorWriterManager* uses.
More about implementing *ValidatorWriterManager* is discused in chapter Supporting New Target Framework.

The write method gets a *LanguageContext*, *Validator* and an *ItemStatus* as parameters. The *LanguageContext*
contains a writer that can be used for the output. It can also contain other context information such as what kind
of output is desired (e.g. JSON/plain JavaScript). *ItemStatus* parameter contains information of the position
in a list the validation rule is written.

**Notice**: *ValidatorWriters* instances are reused so the internal state will stay across calls.


#### Configuring ValidatorWriter to ValidatorWriterManager

Once you have your *ValidatorWriter*, you have to configure *ValidatorWriterSet* to use your *ValidatorWriter*.
The created *ValidatorWriterSet* can be given to *ValidatorProcessor*.

```java
ValidatorWriterSet validatorWriterSet = new ValidatorWriterSet(Arrays.asList(new MyValidatorWriter()));
```

Now you have added support for your own validation annotation.

### Custom Target Platform

While the JMobster library itself doesn't provide wide support for different target languages or frameworks, it still
provides ways to implement that support. Implementing support for a new framework is separeted into to two phases:

1. Support for the language
2. Support for the framework

If your platform happens to use language JMobster already supports, then the second one is only needed to be
implemented.

#### Supporting New Language

To implement support for new language it is recommended that a customized *DataWriter* is created. JMobster provides
a simple *DataWriter* with few helpful write methods, but a higher level *DataWriter* can be useful.
Custom *DataWriters* have to implement *DataWriter* interface. The implementation itself can rely either on inheritance
or delegation.

In addition to the *DataWriter*, a customized *LanguageContext* can be created. This has to support the *DataWriter*
that is going to be used for the language i.e. *LanguageContext<CustomDataWriter>*. For convenience you can extend
this *LanguageContext* to simplify certain generic parameter declarations.

```java
public class MyLanguageContext extends LanguageContext<CustomDataWriter> {
}

// Now you can write
ValidatorWriterManager<MyLanguageContext, CustomDataWriter> manager;
// and
public abstract class JavaScriptValidatorWriter<T extends Validator>
        extends BaseValidatorWriter<T, JavaScriptContext, JavaScriptWriter> {
    // ...
}

// Instead of
ValidatorWriterManager<LanguageContext<CustomDataWriter>, CustomDataWriter> manager;
// and
public abstract class JavaScriptValidatorWriter<T extends Validator>
        extends BaseValidatorWriter<T, LanguageContext<CustomDataWriter>, CustomDataWriter> {
    // ...
}
```


#### Supporting New Target Framework

Supporting a new framework requires implementation of *ValidatorWriters* and *ModelProcessor* interface. Implementing
*ValidatorWriter* classes was covered in Custom Validators chapter. This chapter will cover implementing *ModelProcessor*.

To get started creating a *ModelProcessor*, *BaseModelProcessor* base class can be used. For now it doesn't contain
any functions, but contains the most useful classes that you will need. There are three methods that need to be
implemented: *void startProcessing(ItemStatus status)*, *void endProcessing(ItemStatus status)* and *void processModel( Model model, ItemStatus status )*.
The start and end methods are called when processing of all models given for processing is started/ended. *ProcessModel*
method will be called for each model exactly once. Writing the models is usually done in this method and at this phase
the models are already processed so that they contain all necessary fields, validators and annotations etc. so no
filtering should be required.

What you need for your *ModelProcessor* is: *ValidatorWriterManager*. *LanguageContext* (and therefore *DataWriter*)
is provided abstract *BaseModelProcessor* class. *LanguageContext* and *DataWriter* can be a custom versions for a certain
language or a generic ones. Only requirement is that they have to support the used *ValidatorWriterManager*.

For *ValidatorWriterManager* a *ValidatorWriterSet* can be used. In *ModelProcessor* you can use the *ValidatorWriterManager*
interface, but when supplying the *ValidatorWriter* objects, you can use the convenience class *ValidatorWriterSet*
so there is no need to create a new class that can provide the actual writer objects.

**Notice** *DataWriter* given as generic parameter has to be compatible with your *ValidatorWriters*. What this means
is that if your *ValidatorWriter* requires a writer that implements *DataWriter* but you specify *ValidatorWriterManager*
to use only *DataWriter*, the system won't work because you can't even add your *ValidatorWriters* to *ValidatorWriterManager*.


### Model Naming Strategies

In order to customize the produced model's name, a model naming strategy can be implemented. It basically
takes a Model class and returns the name for the model as string.

