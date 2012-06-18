JMobster - Java Model to Backbone.js generator
==============================================
Version Preview-Alpha 0.1

The Project
-----------
The purpose of this project is to enable automatic model generation
from Java POJOs to Backbone.js models. It also supports client side
validation generation from standard JSR-303 validation annotations.


Current Status
--------------
The project has just started so the backwards compatibility may break
once in a while due to sudden urges to refactor the code. The current
version is not well tested yet with real Backbone.js client nor
a working server so there will be a lot of bugs.

Requirements
------------
At the moment only Java to Backbone.js model conversion is supported.
Validation will require [Backbone.Validations](https://github.com/n-time/backbone.validations) plugin
to work.

Usage
-----
Basic usage is simple. Create Java objects, use JSR-303 annotations and
give the models to the generator.

    public class UserDto {
        @NotNull
        @Size(max = 255)
        private String fullName;

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
directory.

    /*
     * Auto-generated file
     */
    var Models = {
        UserDto: Backbone.Model.extend({
            defaults: function() {
                return {
                    fullName: "",
                    username: "",
                    birthYear: 1900,
                    roles: ["VIEW_PAGES", "EDIT_OWN_PAGES"]
                }
            },
            validate: {
                fullName: {
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

